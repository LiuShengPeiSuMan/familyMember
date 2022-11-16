package com.example.familymembermanagement.activity;

import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_HOUSE_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.fragment.FamilyFragment;
import com.example.familymembermanagement.fragment.FamilyMemberFragment;
import com.example.familymembermanagement.fragment.HistoryFragment;
import com.example.familymembermanagement.fragment.HomeFragment;
import com.example.familymembermanagement.fragment.HouseFragment;
import com.example.familymembermanagement.fragment.NewLifeBabyFragment;
import com.example.familymembermanagement.fragment.StatisticsFragment;
import com.example.familymembermanagement.pojo.UserLogin;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 户主登录
 */
public class HuZhuActivity extends AppCompatActivity {

    private NavigationView hz_navigationView;
    private Toolbar hz_toolbar;
    private DrawerLayout hz_drawerLayout;
    private UserLogin userLogin = new UserLogin();
    private String huZhuId;
    private Gson gson = new Gson();
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    @SuppressWarnings("all")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x01) {
                String data = msg.obj.toString();
                success(data);
            } else if (msg.what == 0x02) {
                String data = msg.obj.toString();
                fail(data);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hu_zhu);
        //初始化数据
        initDate();
        //初始化控件
        initView();
        apiCall();
        //数据初始化
        //获取fragment
        FragmentManager manager = getSupportFragmentManager();
        //开启fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        //初始化fragment
        Fragment fm = new HomeFragment(hz_toolbar, hz_navigationView, hz_drawerLayout);
        hz_toolbar.setTitle("首页");
        transaction.replace(R.id.linearfragment, fm);
        transaction.commit();
        hz_navigationView.setItemIconTintList(null);
        //获取头文件
        View headerView = hz_navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.img);
        imageView.setImageResource(R.mipmap.dacheng);
        TextView textView = headerView.findViewById(R.id.hearderimg);
        textView.setText(userLogin.getNickname());
        //指定选项不可见
        //设置监听
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(HuZhuActivity.this, "头文件", Toast.LENGTH_LONG).show();
            }
        });

        //toolbar的点击事件
        hz_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hz_drawerLayout.openDrawer(hz_navigationView);
            }
        });

        //侧边栏的单击事件
        hz_navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //获取fragment
                FragmentManager manager = getSupportFragmentManager();
                //开启fragment事务
                FragmentTransaction transaction = manager.beginTransaction();
                //初始化fragment
                Fragment fm = null;
                switch (item.getItemId()) {
                    case R.id.it1:
                        //首页fragment
                        fm = new HomeFragment(hz_toolbar, hz_navigationView, hz_drawerLayout);
                        hz_toolbar.setTitle("首页");
                        break;
                    case R.id.it2:
                        fm = new FamilyMemberFragment(hz_toolbar, hz_navigationView, hz_drawerLayout);
                        hz_toolbar.setTitle("家族成员");
                        break;
                    case R.id.it3:
                        fm = new HouseFragment(userLogin.getRole(), userLogin.getNickname(), hz_toolbar, hz_navigationView, hz_drawerLayout);
                        hz_toolbar.setTitle("户主管理");
                        break;
                    case R.id.it4:
                        fm = new FamilyFragment(huZhuId, userLogin.getRole(), userLogin.getId(), userLogin.getNickname(), hz_toolbar, hz_navigationView, hz_drawerLayout);
                        hz_toolbar.setTitle("家庭成员");
                        break;
                    case R.id.it5:
                        fm = new NewLifeBabyFragment(userLogin.getRole(), userLogin.getNickname(), userLogin.getId(), hz_toolbar, hz_navigationView, hz_drawerLayout);
                        hz_toolbar.setTitle("出生成员");
                        break;
                    case R.id.it6:
                        fm = new StatisticsFragment(hz_toolbar, hz_navigationView, hz_drawerLayout);
                        hz_toolbar.setTitle("成员统计");
                        break;
                    case R.id.it7:
                        fm = new HistoryFragment(userLogin.getNickname(),hz_toolbar, hz_navigationView, hz_drawerLayout);
                        hz_toolbar.setTitle("提交记录");
                        break;
                }
                //替换fragment
                transaction.replace(R.id.linearfragment, fm);
                //提交fragment
                transaction.commit();
                return true;
            }
        });
    }

    public void apiCall() {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(QUERY_HOUSE_ID);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("name", userLogin.getNickname())
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

    public void success(String data) {
        AddHouseReturnData addHouseReturnData = gson.fromJson(data, AddHouseReturnData.class);
        huZhuId = addHouseReturnData.getData();
        System.err.println("======huZhuId======" + huZhuId);
    }

    public void fail(String data) {
        ToastUtil.toast(getApplication(), "服务器连接失败");
    }

    //初始化数据
    public void initDate() {
        Intent intent = getIntent();
        userLogin.setId(intent.getStringExtra("id"));
        userLogin.setAccount(intent.getStringExtra("account"));
        userLogin.setPassword(intent.getStringExtra("password"));
        userLogin.setLoginEmail(intent.getStringExtra("loginEmail"));
        userLogin.setNickname(intent.getStringExtra("nickname"));
        String role = intent.getStringExtra("role");
        userLogin.setRole(Integer.valueOf(role));
        String createTime = intent.getStringExtra("createTime");
        userLogin.setCreateTime(createTime);
        String createUser = intent.getStringExtra("createUser");
        userLogin.setCreateUser(createUser);
        String updateTime = intent.getStringExtra("updateTime");
        userLogin.setUpdateTime(updateTime);
        userLogin.setUpdateUser(intent.getStringExtra("updateUser"));
    }

    //初始化控件
    public void initView() {
        hz_navigationView = findViewById(R.id.hz_nav);
        hz_toolbar = findViewById(R.id.hz_toolbar);
        hz_drawerLayout = findViewById(R.id.hz_drawerlayout);
    }
}