package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_FAMILY_INFORMATION;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.FamilyBriefIntroduction;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.pojo.returndata.FamilyReturnData;
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
 * 家庭成员
 */
public class FamilyFragment extends Fragment implements UnifiedRuleFragment, View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView family_list;
    private ImageView add_family;
    private TextView family_count;
    private String loginId, huZhuId, loginName;
    private CustomDialog customDialog;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    //角色
    private Integer role;
    private Gson gson = new Gson();
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    private FragmentManager manager;
    private FragmentTransaction transaction;
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

    //接收上个页面传递过来的数据
    public FamilyFragment(String huZhuId, Integer role, String loginId, String loginName,
                          Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.huZhuId = huZhuId;
        this.role = role;
        this.loginId = loginId;
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
        View view = inflater.inflate(R.layout.family_fragment, null);
        initView(view);
        toolbar.setVisibility(View.VISIBLE);
        //打开手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //只有户主可见
        if (role == 1 || role == 3) {
            //隐藏
            add_family.setVisibility(View.GONE);
        }
        initData();
        initEvent();
        return view;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView(View view) {
        family_list = view.findViewById(R.id.family_list);
        add_family = view.findViewById(R.id.add_family);
        family_count = view.findViewById(R.id.family_count);
    }

    @Override
    public void initData() {
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {
        add_family.setOnClickListener(this);
        if (role == 2) {
            family_list.setOnItemClickListener(this);
        }
    }

    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        FamilyReturnData familyReturnData = gson.fromJson(data, FamilyReturnData.class);
        List<FamilyBriefIntroduction> family = familyReturnData.getData();
        List<Map<String, Object>> list = new ArrayList<>();
        if (family.size() > 0) {
            for (FamilyBriefIntroduction f : family) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", f.getName());
                map.put("familyPeopleId", f.getFamilyPeopleId());
                Integer sex = f.getSex();
                if (sex == 1) {
                    map.put("sex", "男");
                } else {
                    map.put("sex", "女");
                }
                map.put("age", f.getAge());
                Integer isMarry = f.getIsMarry();
                if (isMarry == 1) {
                    map.put("isMarry", "已婚");
                } else {
                    map.put("isMarry", "未婚");
                }
                list.add(map);
            }
        }
        family_count.setText(family.size() + "口人");
        String[] from = {"name", "sex", "age", "isMarry"};
        int[] to = {R.id.family_name, R.id.family_sex, R.id.family_age, R.id.family_isMarry};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.family_list, from, to);
        family_list.setAdapter(adapter);
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
        builder.url(QUERY_FAMILY_INFORMATION);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("huZhuId", huZhuId)
                .add("name", loginName)
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

    @Override
    public void onClick(View view) {
        //跳转到添加户主页
        transaction = manager.beginTransaction();
        transaction.replace(R.id.linearfragment, new AddFamilyFragment(loginId, loginName, toolbar, nav, drawerLayout));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * 点击家庭成员,跳转到操作家庭成员页面
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
        //跳转到添加户主页
        transaction = manager.beginTransaction();
        transaction.replace(R.id.linearfragment, new FamilyUpdateDeleteFragemnt(map.get("familyPeopleId").toString(), loginName, toolbar, nav, drawerLayout));
        transaction.addToBackStack(null);
        transaction.commit();
        System.err.println("========" + map.get("familyPeopleId").toString());
    }
}
