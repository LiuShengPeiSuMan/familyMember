package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.ALL_MEMBER;
import static com.example.familymembermanagement.util.UrlApiUtil.DELETE_EXAMINE;
import static com.example.familymembermanagement.util.UrlApiUtil.FAMILY_DETAILED;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.FamilyMember;
import com.example.familymembermanagement.pojo.returndata.AllMemberReturnData;
import com.example.familymembermanagement.pojo.returndata.FamilyMemberReturnData;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.example.familymembermanagement.util.ToolUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 家族成员管理
 */
public class FamilyMemberFragment extends Fragment implements UnifiedRuleFragment, View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText fm_edittext;
    private ImageView fm_search;
    private ListView fm_listview;
    private TextView fm_count;
    private Dialog dialog;
    private CustomDialog customDialog;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    //自定义弹框控件
    private TextView dfm_name, dfm_sex, dfm_age, dfm_homeaddress, dfm_dateofbirth, dfm_marriedofnot, dfm_education,
            dfm_work, dfm_workAddress, dfm_phone, dfm_email, dfm_date;
    private Gson gson = new Gson();
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    @SuppressWarnings("all")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x01) {
                String data = msg.obj.toString();
                requestSuccess(data);
            } else if (msg.what == 0x02) {
                String data = msg.obj.toString();
                requestFail(data);
            } else if (msg.what == 0x11) {
                String data = msg.obj.toString();
                success(data);
            } else if (msg.what == 0x12) {
                String data = msg.obj.toString();
                fail(data);
            }
        }
    };

    public FamilyMemberFragment(Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.toolbar = toolbar;
        this.nav = nav;
        this.drawerLayout = drawerLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.familymember_fragment, null);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView(View view) {
        fm_edittext = view.findViewById(R.id.fm_edittext);
        fm_search = view.findViewById(R.id.fm_search);
        fm_listview = view.findViewById(R.id.fm_listview);
        fm_count = view.findViewById(R.id.fm_count);
    }

    @Override
    public void initData() {
        Map<String, Object> params = new HashMap<>();
        String search = fm_edittext.getText().toString();
        if (search == null || search.equals("")) {
            params.put("name", "");
        } else {
            params.put("name", search);
        }
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall(params);
    }

    @Override
    public void initEvent() {
        fm_search.setOnClickListener(this);
        fm_listview.setOnItemClickListener(this);
    }

    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        AllMemberReturnData allMemberReturnData = gson.fromJson(data, AllMemberReturnData.class);
        List<Map<String, Object>> list = new ArrayList<>();
        if (allMemberReturnData != null) {
            List<FamilyMember> familyMember = allMemberReturnData.getData();
            if (familyMember != null && familyMember.size() > 0) {
                for (FamilyMember f : familyMember) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("fm_name", f.getName());
                    Integer sex = f.getSex();
                    if (sex == 0) {
                        params.put("fm_sex", "女");
                    } else if (sex == 1) {
                        params.put("fm_sex", "男");
                    }
                    params.put("fm_age", f.getAge());
                    String date = f.getCreateTime();
                    params.put("fm_date", date);
                    params.put("id", f.getId());
                    list.add(params);
                }
            }
            fm_count.setText("共" + familyMember.size() + "条");
        }
        String[] from = {"fm_name", "fm_sex", "fm_date"};
        int[] to = {R.id.fm_name, R.id.fm_sex, R.id.fm_date};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.fm_list, from, to);
        fm_listview.setAdapter(adapter);
        drawerLayout.closeDrawer(nav);
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        drawerLayout.closeDrawer(nav);
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(ALL_MEMBER);
        //单个参数的请求
        String name = (String) params.get("name");
        FormBody formBody = new FormBody.Builder()
                .add("name", name)
                .build();
        builder.post(formBody);
        Request request = builder.build();
        //设置超时时间
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
        //创建Call
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                message.what = 0x02;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                message.what = 0x01;
                message.obj = data;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 获取每一项详细数据
     */
    public void apiCall02(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(FAMILY_DETAILED);
        //单个参数的请求
        String id = (String) params.get("id");
        FormBody formBody = new FormBody.Builder()
                .add("id", id)
                .build();
        builder.post(formBody);
        Request request = builder.build();
        //设置超时时间
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
        //创建Call
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                message.what = 0x12;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                message.what = 0x11;
                message.obj = data;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 查询每一项详细信息
     */
    public void success(String data) {
        customDialog.dismiss();
        FamilyMemberReturnData familyMemberReturnData = gson.fromJson(data, FamilyMemberReturnData.class);
        FamilyMember familyMember = familyMemberReturnData.getData();
        //如果数据不为空就弹出详细信息弹框
        if (familyMember != null) {
            //弹出对话框
            dialog = new Dialog(getActivity());
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.fm_dialog, null);
            //初始化家庭成员弹框
            memberInitView(v);
            //为控件赋值
            dfm_name.setText(familyMember.getName());
            Integer sex = familyMember.getSex();
            if (sex == 0) {
                dfm_sex.setText("女");
            } else {
                dfm_sex.setText("男");
            }
            dfm_age.setText(familyMember.getAge().toString());
            dfm_homeaddress.setText(familyMember.getHomeAddress());
            String[] s = familyMember.getDateOfBirth().split(" ");
            dfm_dateofbirth.setText(s[0]);
            Integer marriedOfNot = familyMember.getMarriedOfNot();
            if (marriedOfNot == 0) {
                dfm_marriedofnot.setText("否");
            } else {
                dfm_marriedofnot.setText("是");
            }
            //获取学历
            Integer education = familyMember.getEducation();
            String education1 = ToolUtil.education(education);
            dfm_education.setText(education1);
            dfm_work.setText(familyMember.getWork());
            dfm_workAddress.setText(familyMember.getWorkAddress());
            dfm_phone.setText(familyMember.getPhone());
            dfm_email.setText(familyMember.getEmail());
            dfm_date.setText(familyMember.getCreateTime());
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(v);
            dialog = builder.create();
            dialog.show();
        }

    }

    public void fail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 初始化自定义弹框控件
     */
    private void memberInitView(View v) {
        dfm_name = v.findViewById(R.id.dfm_name);
        dfm_sex = v.findViewById(R.id.dfm_sex);
        dfm_age = v.findViewById(R.id.dfm_age);
        dfm_homeaddress = v.findViewById(R.id.dfm_homeaddress);
        dfm_dateofbirth = v.findViewById(R.id.dfm_dateofbirth);
        dfm_marriedofnot = v.findViewById(R.id.dfm_marriedofnot);
        dfm_education = v.findViewById(R.id.dfm_education);
        dfm_work = v.findViewById(R.id.dfm_work);
        dfm_workAddress = v.findViewById(R.id.dfm_workAddress);
        dfm_phone = v.findViewById(R.id.dfm_phone);
        dfm_email = v.findViewById(R.id.dfm_email);
        dfm_date = v.findViewById(R.id.dfm_date);
    }

    /**
     * 搜索单击事件
     */
    @Override
    public void onClick(View view) {
        String search_name = fm_edittext.getText().toString();
        Map<String, Object> map = new HashMap<>();
        if (search_name == null || search_name.equals("")) {
            map.put("name", "");
        } else {
            map.put("name", search_name);
        }
        apiCall(map);
    }

    /**
     * 每一项点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //获取每一项数据
        Map<String, Object> listData = (Map<String, Object>) adapterView.getItemAtPosition(i);
        //获取id
//        String id = (String) listData.get("id");
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall02(listData);
    }
}
