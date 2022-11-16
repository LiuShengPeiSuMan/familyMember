package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.HOME_DATA;
import static com.example.familymembermanagement.util.UrlApiUtil.RELEASE_NOTICE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.Notice;
import com.example.familymembermanagement.standard.UnifiedRule;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 发通知
 */
@SuppressWarnings("all")
public class NoticeFragment extends Fragment implements UnifiedRuleFragment, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner noticespinner;
    private EditText noticetext;
    private Button sendnotice;
    //通知类型
    private String result;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private CustomDialog dialog;
    private Gson gson = new Gson();
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x01) {
                requestSuccess(msg.obj.toString());
            } else if (msg.what == 0x02) {
                requestFail(msg.obj.toString());
            }
        }
    };

    public NoticeFragment(Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.toolbar = toolbar;
        this.nav = nav;
        this.drawerLayout = drawerLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_fragment, null);
        initView(view);
        drawerLayout.closeDrawer(nav);
        initData();
        initEvent();
        return view;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView(View view) {
        noticespinner = view.findViewById(R.id.noticespinner);
        noticetext = view.findViewById(R.id.noticetext);
        sendnotice = view.findViewById(R.id.sendnotice);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        //发布通知点击事件
        sendnotice.setOnClickListener(this);
        noticespinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sendnotice) {
            String text = noticetext.getText().toString();
            if (noticespinner == null || result.equals("")) {
                ToastUtil.toast(getActivity(), "请选择通知类型");
                return;
            }
            if (text == null || text.equals("")) {
                ToastUtil.toast(getActivity(), "请输入通知内容");
                return;
            }
            dialog = new CustomDialog(getActivity(), "正在发布...");
            dialog.show();
            //发请求
            Map<String, Object> params = new HashMap<>();
            params.put("type", result);
            params.put("content", noticetext.getText().toString());
            apiCall(params);
        }
    }

    /**
     * 下拉框监听逻辑
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //获取下拉框被选中的值
        result = adapterView.getItemAtPosition(i).toString();
        long id = adapterView.getItemIdAtPosition(i);
        System.err.println(id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * 请求成功
     */
    @Override
    public void requestSuccess(String data) {
        dialog.dismiss();
        ToastUtil.toast(getActivity(), "发布成功");
        noticetext.setText("");
    }

    /**
     * 请求失败
     */
    @Override
    public void requestFail(String data) {
        dialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    @Override
    public void apiCall(Map<String, Object> params) {
        //通知类型
        String type = (String) params.get("type");
        //通知内容
        String content = (String) params.get("content");
        Notice notices = new Notice();
        //通知类型
        if (type.equals("紧急通知")) {
            notices.setNoticeType(0);
        } else if (type.equals("一般通知")) {
            notices.setNoticeType(1);
        } else if (type.equals("喜庆通知")) {
            notices.setNoticeType(2);
        } else if (type.equals("丧事通知")) {
            notices.setNoticeType(3);
        }
        //通知内容
        notices.setNoticeContent(content);
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(RELEASE_NOTICE);
        //请求参数
        String notice = gson.toJson(notices);
        //FormBody formBody = new FormBody.Builder().add("notice", notice).build();
        //传递对象类型的参数
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), notice);
        //发送post请求
        builder.post(body);
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
}
