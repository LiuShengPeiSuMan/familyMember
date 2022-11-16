package com.example.familymembermanagement.activity;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;
import static com.example.familymembermanagement.util.UrlApiUtil.EMAIL_LOGIN;
import static com.example.familymembermanagement.util.UrlApiUtil.LOGIN_URL;
import static com.example.familymembermanagement.util.UrlApiUtil.SEND_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.UserLogin;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.pojo.returndata.UserLoginReturnData;
import com.example.familymembermanagement.standard.UnifiedRule;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
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

/**
 * 邮箱登录
 */
@SuppressWarnings("all")
public class Email_LoginActivity extends AppCompatActivity implements UnifiedRule, View.OnClickListener {

    private EditText emailloginet, emailcode;
    private Button getcode, emaillogin;
    private Gson gson = new Gson();
    private CustomDialog loadDialog;
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x01:
                    String data1 = msg.obj.toString();
                    AddHouseReturnData addHouseReturnData = gson.fromJson(data1, AddHouseReturnData.class);
                    if (addHouseReturnData.getData() != null) {
                        if (addHouseReturnData.getData().equals("发送成功")) {
                            countDownTime();
                        }
                        ToastUtil.toast(Email_LoginActivity.this, addHouseReturnData.getData());
                    }
                    loadDialog.dismiss();
                    break;
                case 0x02:
                    ToastUtil.toast(Email_LoginActivity.this, "服务器连接失败");
                    loadDialog.dismiss();
                    break;
                case 0x11:
                    String data = msg.obj.toString();
                    requestSuccess(data);
                    break;
                case 0x12:
                    requestFail(msg.obj.toString());
                    break;
                case 1000:
                    //设置按钮倒计时
                    getcode.setText(msg.obj.toString());
                    if (msg.obj.toString() == "0") {
                        getcode.setText("发送验证码");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        emailloginet = findViewById(R.id.emailloginet);
        emailcode = findViewById(R.id.emailcode);
        getcode = findViewById(R.id.getcode);
        emaillogin = findViewById(R.id.emaillogin);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

    }

    /**
     * 初始化事件监听
     */
    @Override
    public void initEvent() {
        getcode.setOnClickListener(this);
        emaillogin.setOnClickListener(this);
    }

    /**
     * 请求成功
     */
    @Override
    public void requestSuccess(String data) {
        UserLoginReturnData userLoginReturnData = gson.fromJson(data, UserLoginReturnData.class);
        if (userLoginReturnData.getCode() == 1001) {
            Toast.makeText(Email_LoginActivity.this, userLoginReturnData.getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            UserLogin userLogin = userLoginReturnData.getData();
            if (userLogin != null) {
                //跳转到主页
                switch (userLogin.getRole()) {
                    case 1:
                        Intent intent1 = new Intent(Email_LoginActivity.this, HomepageActivity.class);
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
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(Email_LoginActivity.this, HuZhuActivity.class);
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
                        break;
                    case 3:
                        Intent intent3 = new Intent(Email_LoginActivity.this, PuTongActivity.class);
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
                        break;
                }
            }
        }
        loadDialog.dismiss();
    }

    /**
     * 请求失败
     */
    @Override
    public void requestFail(String data) {
        Toast.makeText(Email_LoginActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
        loadDialog.dismiss();
    }

    /**
     * 发送验证码接口调用
     */
    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(SEND_CODE);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("email", params.get("email").toString())
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
     * 事件监听
     */
    @Override
    public void onClick(View view) {
        String email = emailloginet.getText().toString();
        switch (view.getId()) {
            case R.id.getcode:  //获取验证码
                //获取邮箱
                if (email == null || email.equals("")) {
                    ToastUtil.toast(this, "请输入邮箱");
                    break;
                }
                //发请求
                Map<String, Object> params = new HashMap<>();
                params.put("email", email);
                apiCall(params);
                Message message = new Message();
                loadDialog = new CustomDialog(this, "正在发送...");
                loadDialog.show();
                break;
            case R.id.emaillogin:   //登录
                String code = emailcode.getText().toString();
                if (code == null || code.equals("")) {
                    ToastUtil.toast(this, "请输入验证码");
                    break;
                }
                Map<String, Object> params02 = new HashMap<>();
                params02.put("email", email);
                params02.put("code", code);
                apiCall02(params02);
                loadDialog = new CustomDialog(this, "正在登录...");
                loadDialog.show();
                break;
        }
    }

    /**
     * 邮箱登录接口调用
     */
    public void apiCall02(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(EMAIL_LOGIN);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("email", params.get("email").toString())
                .add("code", params.get("code").toString())
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
     * 设置按钮倒计时
     */
    private void countDownTime() {
        //用安卓自带的CountDownTimer实现
        CountDownTimer mTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "millisUntilFinished: " + millisUntilFinished);
                getcode.setBackgroundColor(Color.parseColor("#F47070"));
                getcode.setText(millisUntilFinished / 1000 + "秒后重发");
            }

            @Override
            public void onFinish() {
                getcode.setEnabled(true);
                getcode.setBackgroundColor(Color.parseColor("#F44336"));
                getcode.setText("发送验证码");
                cancel();
            }
        };
        mTimer.start();
        getcode.setEnabled(false);
    }
}