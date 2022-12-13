package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.ALL_HOUSE;
import static com.example.familymembermanagement.util.UrlApiUtil.FAMILY_DETAILED;
import static com.example.familymembermanagement.util.UrlApiUtil.HOUSE_ALL_FAMILY;
import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_FAMILY_DETAILED;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.familymembermanagement.pojo.PeopleHouse;
import com.example.familymembermanagement.pojo.returndata.FamilyMemberReturnData;
import com.example.familymembermanagement.pojo.returndata.PeopleHouseReturnData;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
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
 * 户主——家庭成员
 */
public class MemberDetailedFragment extends Fragment implements UnifiedRuleFragment, AdapterView.OnItemClickListener {

    private ListView mdf_list;
    private String householderId;
    private Dialog dialog;
    private CustomDialog customDialog;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
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

    public MemberDetailedFragment(String householderId, Toolbar toolbar, NavigationView navigationView, DrawerLayout drawerLayout) {
        this.householderId = householderId;
        this.toolbar = toolbar;
        this.nav = navigationView;
        this.drawerLayout = drawerLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.member_detailed_fragment, null);
        toolbar.setVisibility(View.GONE);
        //关闭手势打开
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    @Override
    public void initView(View view) {
        mdf_list = view.findViewById(R.id.mdf_list);
    }

    @Override
    public void initData() {
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("householderId", householderId);
        apiCall(map);
    }

    @Override
    public void initEvent() {
        mdf_list.setOnItemClickListener(this);
    }

    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        //解析返回数据
        PeopleHouseReturnData peopleHouseReturnData = gson.fromJson(data, PeopleHouseReturnData.class);
        List<PeopleHouse> peopleHouses = peopleHouseReturnData.getData();
        List<Map<String, Object>> list = new ArrayList<>();
        if (peopleHouses.size() > 0) {
            for (PeopleHouse p : peopleHouses) {
                Map<String, Object> map = new HashMap<>();
                map.put("familyPeopleId", p.getFamilyPeopleId());
                map.put("name", p.getName());
                map.put("relationship", p.getRelationship());
                map.put("addtime", p.getCreateTime());
                list.add(map);
            }
        }
        String[] from = {"name", "relationship", "addtime"};
        int[] to = {R.id.mdfhouse_name, R.id.mdfhouse_relationship, R.id.mdfhouse_addtime};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.mdf_house_list, from, to);
        mdf_list.setAdapter(adapter);
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getContext(), "服务器连接失败");
    }

    /**
     * 获取家庭成员详细信息
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
            if (education != null) {
                switch (education) {
                    case 0:
                        dfm_education.setText("未上学");
                        break;
                    case 1:
                        dfm_education.setText("幼儿园");
                        break;
                    case 2:
                        dfm_education.setText("小学");
                        break;
                    case 3:
                        dfm_education.setText("初中");
                        break;
                    case 4:
                        dfm_education.setText("普高");
                        break;
                    case 5:
                        dfm_education.setText("中专");
                        break;
                    case 6:
                        dfm_education.setText("大学");
                        break;
                    case 7:
                        dfm_education.setText("大专");
                        break;
                    case 8:
                        dfm_education.setText("研究生");
                        break;
                    case 9:
                        dfm_education.setText("博士");
                        break;
                    case 10:
                        dfm_education.setText("硕士");
                        break;
                }
            }
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
        ToastUtil.toast(getActivity(), "服务器请求失败");
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

    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(HOUSE_ALL_FAMILY);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("householderId", params.get("householderId").toString())
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
        builder.url(QUERY_FAMILY_DETAILED);
        //单个参数的请求
        String familyPeopleId = (String) params.get("familyPeopleId");
        FormBody formBody = new FormBody.Builder()
                .add("familyPeopleId", familyPeopleId)
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
     * 家庭成员详细信息
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //获取每一项数据
        Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
        String familyPeopleId = (String) map.get("familyPeopleId");
        System.err.println("===============" + familyPeopleId);
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall02(map);
    }


}
