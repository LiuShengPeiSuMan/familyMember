package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.ALL_HOUSE;

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
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.HouseSituation;
import com.example.familymembermanagement.pojo.returndata.HouseReturnData;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.example.familymembermanagement.util.ToolUtil;
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
 * 户主管理
 */
public class HouseFragment extends Fragment implements UnifiedRuleFragment, View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView add_house, house_search;
    private TextView house_count;
    private EditText house_edittext;
    private ListView house_list;
    private CustomDialog customDialog;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    //登录角色（1：族长，2：户主，3：普通家族成员）
    private Integer role;
    private String loginName;
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
            }
        }
    };

    public HouseFragment(Integer role, String loginName, Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.role = role;
        this.loginName = loginName;
        this.toolbar = toolbar;
        this.nav = nav;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.house_fragment, null);
        toolbar.setVisibility(View.VISIBLE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
        add_house = view.findViewById(R.id.add_house);
        house_search = view.findViewById(R.id.house_search);
        house_count = view.findViewById(R.id.house_count);
        house_edittext = view.findViewById(R.id.house_edittext);
        house_list = view.findViewById(R.id.house_list);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //如果角色不是族长就隐藏控件
        if (role != 1) {
            //隐藏添加户主控件
            add_house.setVisibility(View.GONE);
            //设置宽度
            house_edittext.getLayoutParams().width = 600;
        }
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {
        add_house.setOnClickListener(this);
        house_search.setOnClickListener(this);
        house_list.setOnItemClickListener(this);

    }

    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        //解析数据
        HouseReturnData houseReturnData = gson.fromJson(data, HouseReturnData.class);
        List<HouseSituation> houseSituations = houseReturnData.getData();
        //判断有没有家庭成员
        List<Map<String, Object>> list = new ArrayList<>();
        if (houseSituations != null && houseSituations.size() > 0) {
            for (HouseSituation h : houseSituations) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", h.getId());
                map.put("ho_name", h.getName());
                map.put("ho_count", h.getPeopleNumber() + "人");
                String date = h.getCreateTime();
                map.put("ho_date", date);
                list.add(map);
            }
        }
        house_count.setText("共" + houseSituations.size() + "条");
        String[] from = {"ho_name", "ho_count", "ho_date"};
        int[] to = {R.id.ho_name, R.id.ho_count, R.id.ho_date};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.house_list, from, to);
        house_list.setAdapter(adapter);
        drawerLayout.closeDrawer(nav);
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        drawerLayout.closeDrawer(nav);
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 查询所有户主请求
     */
    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(ALL_HOUSE);
        //单个参数的请求
        FormBody formBody = null;
        if (params != null) {
            formBody = new FormBody.Builder()
                    .add("name", params.get("name").toString())
                    .build();
        } else {
            formBody = new FormBody.Builder().build();
        }
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
     * 搜索和添加事件监听
     */
    @Override
    public void onClick(View view) {
        //搜索
        if (view.getId() == R.id.house_search) {
            //根据姓名查询
            String name = house_edittext.getText().toString();
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            apiCall(map);
        } else if (view.getId() == R.id.add_house) {   //添加户主
            //跳转到添加户主页
            transaction = manager.beginTransaction();
            transaction.replace(R.id.linearfragment, new AddHouseFragment(loginName, toolbar, nav, drawerLayout));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * listview事件监听
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //获取每项数据
        Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
        //跳转到家庭成员详情页面
        transaction = manager.beginTransaction();
        transaction.replace(R.id.linearfragment, new MemberDetailedFragment(map.get("id").toString(), toolbar, nav, drawerLayout));
        transaction.addToBackStack(null);
        transaction.commit();
        System.err.println("=======" + map.get("id").toString());
    }
}
