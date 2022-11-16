package com.example.familymembermanagement.activity;

import static com.example.familymembermanagement.util.UrlApiUtil.ADD_ZUZHANG;
import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_ZZ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
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
 * 初始化族长信息
 */
public class InitZuZhangActivity extends AppCompatActivity implements UnifiedRule, View.OnClickListener {

    private EditText zz_name, zz_email, zz_account, zz_pwd, zz_pwdTwo;
    private Button zz_ok;
    private Gson gson = new Gson();
    private OkHttpClient client = new OkHttpClient();
    private CustomDialog dialog;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_zu_zhang);
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        zz_name = findViewById(R.id.zz_name);
        zz_email = findViewById(R.id.zz_email);
        zz_account = findViewById(R.id.zz_account);
        zz_pwd = findViewById(R.id.zz_pwd);
        zz_pwdTwo = findViewById(R.id.zz_pwdTwo);
        zz_ok = findViewById(R.id.zz_ok);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        zz_ok.setOnClickListener(this);
    }

    @Override
    public void requestSuccess(String data) {
        dialog.dismiss();
        AddHouseReturnData addHouseReturnData = gson.fromJson(data, AddHouseReturnData.class);
        String dataData = addHouseReturnData.getData();
        ToastUtil.toast(this, dataData);
        finish();
    }

    @Override
    public void requestFail(String data) {
        dialog.dismiss();
        ToastUtil.toast(this, "服务器连接失败");
    }

    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(ADD_ZUZHANG);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("name", params.get("name").toString())
                .add("account", params.get("account").toString())
                .add("pwd", params.get("pwd").toString())
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
        String format = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        String name = zz_name.getText().toString();
        String email = zz_email.getText().toString();
        String account = zz_account.getText().toString();
        String pwd = zz_pwd.getText().toString();
        String pwdTwo = zz_pwdTwo.getText().toString();
        //判断是否为空
        if (name == null || name.equals("")) {
            ToastUtil.toast(this, "请输入族长姓名");
            return;
        }
//        if (email==null || email.equals("")){
//            ToastUtil.toast(this,"请输入登录邮箱");
//            return;
//        }
        if (account == null || account.equals("")) {
            ToastUtil.toast(this, "请输入登录账号");
            return;
        }
        if (pwd == null || pwd.equals("")) {
            ToastUtil.toast(this, "请输入登录密码");
            return;
        }
        if (pwdTwo == null || pwdTwo.equals("")) {
            ToastUtil.toast(this, "请再次确认登录密码");
            return;
        }
        //判断邮箱格式
        if (!email.matches(format)) {
            ToastUtil.toast(this, "邮箱格式不正确，请重新输入！");
            return;
        }
        //判断账号是否符合要求
        if (account.length() < 6) {
            ToastUtil.toast(this, "登录账号长度不能少于6位");
            return;
        }
        //判断密码长度不能少于8位
        if (pwd.length() < 8) {
            ToastUtil.toast(this, "登录密码长度不能少于8位");
            return;
        }
        //判断密码是否一直
        if (!pwd.equals(pwdTwo)) {
            ToastUtil.toast(this, "密码不一致，请重新输入！");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("account", account);
        map.put("pwd", pwdTwo);
        dialog = new CustomDialog(this, "正在初始化族长...");
        dialog.show();
        apiCall(map);

    }
}