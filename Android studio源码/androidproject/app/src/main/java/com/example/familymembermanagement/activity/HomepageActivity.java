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
import com.example.familymembermanagement.fragment.ExamineFragment;
import com.example.familymembermanagement.fragment.FamilyFragment;
import com.example.familymembermanagement.fragment.FamilyMemberFragment;
import com.example.familymembermanagement.fragment.HomeFragment;
import com.example.familymembermanagement.fragment.HouseFragment;
import com.example.familymembermanagement.fragment.JurisdictionFragment;
import com.example.familymembermanagement.fragment.NewLifeBabyFragment;
import com.example.familymembermanagement.fragment.NoticeFragment;
import com.example.familymembermanagement.fragment.StatisticsFragment;
import com.example.familymembermanagement.pojo.UserLogin;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//主页面
public class HomepageActivity extends AppCompatActivity {

    private NavigationView zz_navigationView;
    private Toolbar zz_toolbar;
    private DrawerLayout zz_drawerLayout;
    private UserLogin userLogin = new UserLogin();
    private String huZhuId;
    // private long mExitTime = System.currentTimeMillis();
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
        setContentView(R.layout.activity_homepage);
        //初始化数据
        initDate();
        //初始化控件
        initView();
        apiCall();

        //获取fragment
        FragmentManager manager = getSupportFragmentManager();
        //开启fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        //初始化fragment
        Fragment fm = new HomeFragment(zz_toolbar, zz_navigationView, zz_drawerLayout);
        zz_toolbar.setTitle("首页");
        transaction.replace(R.id.linearfragment, fm);
        transaction.commit();
        zz_navigationView.setItemIconTintList(null);
        //获取头文件
        View headerView = zz_navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.img);
        imageView.setImageResource(R.mipmap.huangshang);
        TextView textView = headerView.findViewById(R.id.hearderimg);
        textView.setText(userLogin.getNickname());
        //设置监听
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomepageActivity.this, userLogin.getNickname(), Toast.LENGTH_LONG).show();
            }
        });

        //toolbar的点击事件
        zz_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zz_drawerLayout.openDrawer(zz_navigationView);
            }
        });

        //侧边栏的单击事件
        zz_navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                        fm = new HomeFragment(zz_toolbar, zz_navigationView, zz_drawerLayout);
                        zz_toolbar.setTitle("首页");
                        break;
                    case R.id.it2:
                        fm = new ExamineFragment(userLogin.getNickname(), zz_toolbar, zz_navigationView, zz_drawerLayout);
                        zz_toolbar.setTitle("待审核");
                        break;
                    case R.id.it3:
                        fm = new FamilyMemberFragment(zz_toolbar, zz_navigationView, zz_drawerLayout);
                        zz_toolbar.setTitle("家族成员");
                        break;
                    case R.id.it4:
                        fm = new HouseFragment(userLogin.getRole(), userLogin.getNickname(), zz_toolbar, zz_navigationView, zz_drawerLayout);
                        zz_toolbar.setTitle("户主管理");
                        break;
                    case R.id.it5:
                        fm = new FamilyFragment(huZhuId, userLogin.getRole(), userLogin.getId(), userLogin.getNickname(), zz_toolbar, zz_navigationView, zz_drawerLayout);
                        zz_toolbar.setTitle("家庭成员");
                        break;
                    case R.id.it6:
                        fm = new NoticeFragment(zz_toolbar, zz_navigationView, zz_drawerLayout);
                        zz_toolbar.setTitle("发布通知");
                        break;
                    case R.id.it7:
                        fm = new NewLifeBabyFragment(userLogin.getRole(), userLogin.getNickname(), userLogin.getId(), zz_toolbar, zz_navigationView, zz_drawerLayout);
                        zz_toolbar.setTitle("出生成员");
                        break;
                    case R.id.it8:
                        fm = new StatisticsFragment(zz_toolbar, zz_navigationView, zz_drawerLayout);
                        zz_toolbar.setTitle("成员统计");
                        break;
                    case R.id.it9:
                        fm = new JurisdictionFragment(userLogin.getNickname(), userLogin.getRole(), zz_toolbar, zz_navigationView, zz_drawerLayout);
                        zz_toolbar.setTitle("权限管理");
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
        zz_navigationView = findViewById(R.id.zz_nav);
        zz_toolbar = findViewById(R.id.zz_toolbar);
        zz_drawerLayout = findViewById(R.id.zz_drawerlayout);
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

//    //双击返回
//    @Override
//    public void onBackPressed() {
//        if (System.currentTimeMillis() - mExitTime < 800) {
//            finish();
//        } else {
//            ToastUtil.toast(HomepageActivity.this, "再按一次返回");
//            mExitTime = System.currentTimeMillis();
//        }
//    }
}