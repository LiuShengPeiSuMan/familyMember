package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.ADD_LOGIN_PUTONG;
import static com.example.familymembermanagement.util.UrlApiUtil.SELECT_NAME_EMAIL;
import static com.example.familymembermanagement.util.UrlApiUtil.USER_LOGIN;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.Jurisd;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.pojo.returndata.JurisdReturnData;
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
 * 添加用户权限
 */
public class AddJurisdictionFragment extends Fragment implements UnifiedRuleFragment, View.OnClickListener {

    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private ListView selected_list, notSelect_list;
    private Button add_Juris;
    private String loginName;
    //未有登录权限的集合
    List<Map<String, Object>> listNot;
    //已选择的登录成员
    List<Map<String, Object>> listHave;
    //添加权限所需要的参数
    Map<String, Object> map;
    private Gson gson = new Gson();
    private CustomDialog customDialog;
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

    public AddJurisdictionFragment(String loginName, Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.loginName = loginName;
        this.toolbar = toolbar;
        this.nav = nav;
        this.drawerLayout = drawerLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_jurisdiction_fragment, null);
        //隐藏
        toolbar.setVisibility(View.GONE);
        //关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView(View view) {
        selected_list = view.findViewById(R.id.selected_list);
        notSelect_list = view.findViewById(R.id.notSelect_list);
        add_Juris = view.findViewById(R.id.add_Juris);
    }

    @Override
    public void initData() {
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {
        add_Juris.setOnClickListener(this);
        notSelect_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> data = (Map<String, Object>) adapterView.getItemAtPosition(i);
                insertItemData(data);
            }
        });
    }

    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        JurisdReturnData jurisdReturnData = gson.fromJson(data, JurisdReturnData.class);
        List<Jurisd> jurisds = jurisdReturnData.getData();
        listNot = new ArrayList<>();
        if (!jurisds.isEmpty()) {
            for (Jurisd j : jurisds) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", j.getName());
                map.put("email", j.getEmail());
                listNot.add(map);
            }
        }
        String[] from = {"name"};
        int[] to = {R.id.no};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listNot, R.layout.juris_notselectd_list, from, to);
        notSelect_list.setAdapter(adapter);
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 添加用户权限
     */
    public void success(String data) {
        customDialog.dismiss();
        AddHouseReturnData data1 = gson.fromJson(data, AddHouseReturnData.class);
        String data2 = data1.getData();
        ToastUtil.toast(getActivity(), data2);
        selected_list.setAdapter(null);
        apiCall(null);
    }

    public void fail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(SELECT_NAME_EMAIL);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder().build();
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
     * 添加普通成员登录权限
     */
    public void apiCall02(Map<String, Object> map) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(ADD_LOGIN_PUTONG);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("loginName", map.get("loginName").toString())
                .add("puTongName", map.get("puTongName").toString())
                .add("email", map.get("email").toString())
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

    @Override
    public void onClick(View view) {
        if (map == null || map.isEmpty()) {
            ToastUtil.toast(getActivity(), "请选择需要添加权限的用户");
            return;
        }
        customDialog = new CustomDialog(getActivity(), "正在加载权限...");
        customDialog.show();
        apiCall02(map);
    }

    /**
     * 向item插入数据
     */
    public void insertItemData(Map<String, Object> data) {
        //添加请求参数
        map = new HashMap<>();
        map.put("loginName", loginName);
        map.put("puTongName", data.get("name").toString());
        map.put("email", data.get("email").toString());
        listHave = new ArrayList<>();
        Map<String, Object> have = new HashMap<>();
        have.put("name", data.get("name").toString());
        have.put("email", data.get("email").toString());
        listHave.add(have);
        String[] from = {"name"};
        int[] to = {R.id.yes};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listHave, R.layout.juris_selected_list, from, to);
        selected_list.setAdapter(adapter);
    }

}
