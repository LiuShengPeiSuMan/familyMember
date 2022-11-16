package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.BABY_DETAILED;
import static com.example.familymembermanagement.util.UrlApiUtil.YEAR_BABY;

import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.BabySituation;
import com.example.familymembermanagement.pojo.returndata.BabySituationReturnData;
import com.example.familymembermanagement.pojo.returndata.YearBabyReturnData;
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
 * 新生命
 */
public class NewLifeBabyFragment extends Fragment implements UnifiedRuleFragment, View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView baby_count;
    private ImageView add_baby;
    private ListView baby_list;
    //登录角色
    private Integer role;
    private TextView b_name, b_sex, b_father, b_mother, b_health, b_dateOfBirth, b_addTime;
    private String loginName, loginId;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private Gson gson = new Gson();
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    //加载
    private CustomDialog customDialog;
    private Dialog dialog;
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
            } else if (msg.what == 0x11) {
                String data = msg.obj.toString();
                detailedSuccess(data);
            } else if (msg.what == 0x12) {
                String data = msg.obj.toString();
                detailedFail(data);
            }
        }
    };

    //获取角色
    public NewLifeBabyFragment(Integer role, String loginName, String loginId,
                               Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.role = role;
        this.loginName = loginName;
        this.loginId = loginId;
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
        View view = inflater.inflate(R.layout.new_lifebaby_fragment, null);
        initView(view);
        //可见
        toolbar.setVisibility(View.VISIBLE);
        //打开手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if (role == 2) {
            //添加可见
            add_baby.setVisibility(View.VISIBLE);
        } else {
            //添加隐藏
            add_baby.setVisibility(View.GONE);
        }
        initData();
        initEvent();
        return view;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView(View view) {
        baby_count = view.findViewById(R.id.baby_count);
        add_baby = view.findViewById(R.id.add_baby);
        baby_list = view.findViewById(R.id.baby_list);
    }

    @Override
    public void initData() {
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {
        add_baby.setOnClickListener(this);
        baby_list.setOnItemClickListener(this);
    }

    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        YearBabyReturnData yearBabyReturnData = gson.fromJson(data, YearBabyReturnData.class);
        List<BabySituation> babySituations = yearBabyReturnData.getData();
        List<Map<String, Object>> list = new ArrayList<>();
        if (babySituations != null && babySituations.size() > 0) {
            for (BabySituation b : babySituations) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", b.getId());
                map.put("name", b.getName());
                Integer sex = b.getSex();
                if (sex == 0) {
                    map.put("sex", "女婴");
                } else {
                    map.put("sex", "男婴");
                }
                map.put("father", b.getFather());
                map.put("mother", b.getMother());
                list.add(map);
            }
            baby_count.setText("共" + babySituations.size() + "条");
        }
        //设置适配器
        String[] from = {"name", "sex", "father", "mother"};
        int[] to = {R.id.baby_name, R.id.baby_sex, R.id.baby_father, R.id.baby_mather};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.baby_list, from, to);
        baby_list.setAdapter(adapter);
        drawerLayout.closeDrawer(nav);
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");

    }

    /**
     * 查询家庭成员详细信息
     */
    public void detailedSuccess(String data) {
        customDialog.dismiss();
        BabySituationReturnData babySituationReturnData = gson.fromJson(data, BabySituationReturnData.class);
        BabySituation babySituation = babySituationReturnData.getData();
        if (babySituation != null) {
            //弹出对话框
            dialog = new Dialog(getActivity());
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.baby_dialog, null);
            //初始化控件
            initviewBabyDialog(v);
            b_name.setText(babySituation.getName());
            Integer sex = babySituation.getSex();
            if (sex == 1) {
                b_sex.setText("男婴");
            } else {
                b_sex.setText("女婴");
            }
            b_father.setText(babySituation.getFather());
            b_mother.setText(babySituation.getMother());
            Integer healthy = babySituation.getHealthy();
            if (healthy == 0) {
                b_health.setText("健康");
            } else {
                b_health.setText("亚健康");
            }
            b_dateOfBirth.setText(babySituation.getDateOfBirth());
            b_addTime.setText(babySituation.getCreateTime());
            //弹出对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(v);
            dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * 初始化自定义弹框控件
     */
    private void initviewBabyDialog(View v) {
        b_name = v.findViewById(R.id.b_name);
        b_sex = v.findViewById(R.id.b_sex);
        b_father = v.findViewById(R.id.b_father);
        b_mother = v.findViewById(R.id.b_mother);
        b_health = v.findViewById(R.id.b_health);
        b_dateOfBirth = v.findViewById(R.id.b_dateOfBirth);
        b_addTime = v.findViewById(R.id.b_addTime);
    }

    public void detailedFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(YEAR_BABY);
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
     * 查询出生成员详细信息
     */
    public void apiCall02(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(BABY_DETAILED);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("id", params.get("id").toString())
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
        //跳转到添加户主页
        transaction = manager.beginTransaction();
        transaction.replace(R.id.linearfragment, new AddBabyFragment(loginId, loginName,toolbar,nav,drawerLayout));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall02(map);
    }
}
