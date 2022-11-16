package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_HISTORY;
import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_HOUSE_ID;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.Examine;
import com.example.familymembermanagement.pojo.returndata.ExamineReturn;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.example.familymembermanagement.util.ToolUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
 * 提交记录
 */
public class HistoryFragment extends Fragment implements UnifiedRuleFragment {

    private TextView history_count;
    private ListView history_list;
    private String loginName;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
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

    public HistoryFragment(String loginName, Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.loginName = loginName;
        this.toolbar = toolbar;
        this.nav = nav;
        this.drawerLayout = drawerLayout;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, null);
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
        history_count = view.findViewById(R.id.history_count);
        history_list = view.findViewById(R.id.history_list);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        dialog = new CustomDialog(getActivity(), "正在加载...");
        dialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {

    }

    /**
     * 请求成功
     * */
    @Override
    public void requestSuccess(String data) {
        dialog.dismiss();
        ExamineReturn examineReturn = gson.fromJson(data, ExamineReturn.class);
        List<Examine> data1 = examineReturn.getData();
        List<Map<String, Object>> list = new ArrayList<>();
        if (!data1.isEmpty()) {
            for (Examine e : data1) {
                Map<String, Object> map = new HashMap<>();
                Integer type = e.getExamineType();
                if (type == 0) {
                    map.put("type", "添加出生成员");
                } else if (type == 1) {
                    map.put("type", "添加家庭成员");
                } else if (type == 2) {
                    map.put("type", "删除家庭成员");
                }
                Integer status = e.getExamineStatus();
                if (status == 0) {
                    map.put("status", "待审核");
                } else if (status == 1) {
                    map.put("status", "审核通过");
                } else if (status == 2) {
                    map.put("status", "审核驳回");
                }
                String createTime = e.getCreateTime();
                map.put("submitTime", createTime);
                String examineTime = e.getExamineTime();
                map.put("time", examineTime);
                list.add(map);
            }
        }
        history_count.setText("共" + data1.size() + "条记录");
        //设置适配器
        String[] from = {"type", "status", "submitTime", "time"};
        int[] to = {R.id.hi_type, R.id.hi_status, R.id.hi_submit, R.id.hi_time};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.history_list, from, to);
        history_list.setAdapter(adapter);
        drawerLayout.closeDrawer(nav);
    }

    @Override
    public void requestFail(String data) {
        dialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器加载失败");
    }

    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(QUERY_HISTORY);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
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
}
