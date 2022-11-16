package com.example.familymembermanagement.activity;

import static com.example.familymembermanagement.util.UrlApiUtil.LOGIN_URL;
import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_ZZ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.pojo.returndata.UserLoginReturnData;
import com.example.familymembermanagement.pojo.UserLogin;
import com.example.familymembermanagement.standard.UnifiedRule;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ProgressDialogUtil;
import com.example.familymembermanagement.util.ToastUtil;
import com.example.familymembermanagement.util.UrlApiUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//登录页面
@SuppressWarnings("all")
public class LoginActivity extends AppCompatActivity implements UnifiedRule,
        View.OnClickListener, View.OnTouchListener {

    private EditText account, password;
    private Button login;
    private TextView ipConfig, initZz, emailLogin;
    private String ip, port;
    private SharedPreferences sp;
    private Gson gson = new Gson();
    //自定义加载弹框
    private CustomDialog loadDialog;
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    //消息接收
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x01) {
                requestSuccess(msg.obj.toString());
            } else if (msg.what == 0x02) {
                requestFail(msg.obj.toString());
            } else if (msg.what == 0x11) {
                String data = msg.obj.toString();
                success(data);
            } else if (msg.what == 0x12) {
                String data = msg.obj.toString();
                fail(data);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        //获取控件
        initData();
        initView();
        initEvent();
        //隐藏配置IP控件
        ipConfig.setVisibility(View.GONE);
        initZz.setVisibility(View.GONE);
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        login = findViewById(R.id.login);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        ipConfig = findViewById(R.id.ipconfig);
        emailLogin = findViewById(R.id.emaillogin);
        initZz = findViewById(R.id.initZz);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        apiCall02();
        //获取SharedPreferences数据
        sp = getSharedPreferences("ipport", MODE_PRIVATE);
        ip = sp.getString("ip", "127.0.0.1");
        port = sp.getString("port", "8080");
    }

    /**
     * 事件监听
     */
    @Override
    public void initEvent() {
        login.setOnClickListener(this);
        ipConfig.setOnClickListener(this);
        initZz.setOnClickListener(this);
        emailLogin.setOnClickListener(this);
        emailLogin.setOnTouchListener(this);

    }

    /**
     * 请求成功
     */
    @Override
    public void requestSuccess(String data) {
        UserLoginReturnData returnData = gson.fromJson(data, UserLoginReturnData.class);
        if (returnData.getCode() == 1001) {   //接收失败信息
            Toast.makeText(LoginActivity.this, returnData.getMsg(), Toast.LENGTH_SHORT).show();
        } else if (returnData.getData() != null && returnData.getCode() == 1000) {
            UserLogin userLogin = returnData.getData();
            //跳转到主页
            switch (userLogin.getRole()) {
                case 1:
                    Intent intent1 = new Intent(LoginActivity.this, HomepageActivity.class);
                    intent1.putExtra("id", userLogin.getId());
                    intent1.putExtra("account", userLogin.getAccount());
                    intent1.putExtra("password", userLogin.getPassword());
                    intent1.putExtra("loginEmail", userLogin.getLoginEmail());
                    intent1.putExtra("nickname", userLogin.getNickname());
                    intent1.putExtra("role", userLogin.getRole().toString());
                    intent1.putExtra("createTime", userLogin.getCreateTime().toString());
                    intent1.putExtra("createUser", userLogin.getCreateUser());
                    intent1.putExtra("updateTime", userLogin.getUpdateTime().toString());
                    intent1.putExtra("updateUser", userLogin.getUpdateUser());
                    intent1.putExtra("delFlag", userLogin.getDelFlag().toString());
//                    Bundle bundle=new Bundle();
//                    bundle.putString("");
                    startActivity(intent1);
                    //finish();
                    break;
                case 2:
                    Intent intent2 = new Intent(LoginActivity.this, HuZhuActivity.class);
                    intent2.putExtra("id", userLogin.getId());
                    intent2.putExtra("account", userLogin.getAccount());
                    intent2.putExtra("password", userLogin.getPassword());
                    intent2.putExtra("loginEmail", userLogin.getLoginEmail());
                    intent2.putExtra("nickname", userLogin.getNickname());
                    intent2.putExtra("role", userLogin.getRole().toString());
                    intent2.putExtra("createTime", userLogin.getCreateTime().toString());
                    intent2.putExtra("createUser", userLogin.getCreateUser());
                    intent2.putExtra("updateTime", userLogin.getUpdateTime().toString());
                    intent2.putExtra("updateUser", userLogin.getUpdateUser());
                    intent2.putExtra("delFlag", userLogin.getDelFlag().toString());
                    startActivity(intent2);
                    //finish();
                    break;
                case 3:
                    Intent intent3 = new Intent(LoginActivity.this, PuTongActivity.class);
                    intent3.putExtra("id", userLogin.getId());
                    intent3.putExtra("account", userLogin.getAccount());
                    intent3.putExtra("password", userLogin.getPassword());
                    intent3.putExtra("loginEmail", userLogin.getLoginEmail());
                    intent3.putExtra("nickname", userLogin.getNickname());
                    intent3.putExtra("role", userLogin.getRole().toString());
                    intent3.putExtra("createTime", userLogin.getCreateTime().toString());
                    intent3.putExtra("createUser", userLogin.getCreateUser());
                    intent3.putExtra("updateTime", userLogin.getUpdateTime().toString());
                    intent3.putExtra("updateUser", userLogin.getUpdateUser());
                    intent3.putExtra("delFlag", userLogin.getDelFlag().toString());
                    startActivity(intent3);
                    //finish();
                    break;
            }
        }
        loadDialog.dismiss();
    }

    /**
     * 请求失败
     */
    @Override
    public void requestFail(String message) {
        ToastUtil.toast(getApplicationContext(), "服务器连接失败");
        loadDialog.dismiss();
    }

    public void success(String data) {
        AddHouseReturnData addHouseReturnData = gson.fromJson(data, AddHouseReturnData.class);
        String off = addHouseReturnData.getData();
        //族长没有登录权限
        if (off.equals("0")) {
            //初始化族长控件可见
            initZz.setVisibility(View.VISIBLE);
        } else {
            //初始化族长控件隐藏
            initZz.setVisibility(View.GONE);
        }
    }

    public void fail(String data) {
        ToastUtil.toast(getApplicationContext(), "服务器连接失败");
    }

    /**
     * api调用
     */
    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(LOGIN_URL);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("account", params.get("account").toString())
                .add("password", params.get("password").toString())
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
                //returnFail(e.getMessage());
                message.what = 0x02;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                // returnSuccess(data);
                message.what = 0x01;
                message.obj = data;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 调用族长有没有登录权限
     */
    public void apiCall02() {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(QUERY_ZZ);
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
     * 事件监听
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                //UrlApiUtil util = new UrlApiUtil();
                String user = account.getText().toString();
                String pwd = password.getText().toString();
                if (user == null || user.equals("")) {
                    //toast("用户名不能为空");
                    ToastUtil.toast(getApplicationContext(), "用户名不能为空");
                    return;
                }
                if (pwd == null || pwd.equals("")) {
                    ToastUtil.toast(getApplicationContext(), "密码不能为空");
                    return;
                }
                /**
                 * 发送请求
                 * */
                loadDialog = new CustomDialog(this, "正在登录...");
                loadDialog.show();
                Map<String, Object> params = new HashMap<>();
                params.put("account", user);
                params.put("password", pwd);
                apiCall(params);
                break;
            case R.id.emaillogin:
                Intent intent = new Intent(LoginActivity.this, Email_LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ipconfig:
                //对话框
                final Dialog[] dialog = new Dialog[1];
                LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
                View v = inflater.inflate(R.layout.ip_config_dialog, null);
                EditText edIp = v.findViewById(R.id.ip);
                EditText edPort = v.findViewById(R.id.host);
                //赋值
                if (ip != null && !ip.equals("")) {
                    if (port != null && !port.equals("")) {
                        edIp.setText(ip);
                        edPort.setText(port);
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setView(v);
                dialog[0] = builder.create();
                dialog[0].show();
                //自定义弹框按钮单击事件
                v.findViewById(R.id.ok_config).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText ip2 = v.findViewById(R.id.ip);
                        EditText port = v.findViewById(R.id.host);

                        if (ip2.getText().toString() == null || ip2.getText().toString().equals("")) {
                            ToastUtil.toast(LoginActivity.this, "请输入IP");
                            return;
                        }
                        if (port.getText().toString() == null || port.getText().toString().equals("")) {
                            ToastUtil.toast(LoginActivity.this, "请输入端口");
                            return;
                        }
                        //存储到SharedPreferences
                        sp = getSharedPreferences("ipport", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("ip", ip2.getText().toString());
                        System.err.println(ip2.getText().toString());
                        editor.putString("port", port.getText().toString());
                        System.err.println(port.getText().toString());
                        editor.commit();
                        dialog[0].dismiss();
                    }
                });
                break;
            case R.id.initZz:   //初始化族长
                Intent intent1 = new Intent(LoginActivity.this, InitZuZhangActivity.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 触摸事件
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //手指按下
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            emailLogin.setTextColor(Color.YELLOW);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //手指抬起
            emailLogin.setTextColor(Color.parseColor("#504E4E"));
        }
        return false;
    }
}