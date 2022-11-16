package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.DELETE_EXAMINE;
import static com.example.familymembermanagement.util.UrlApiUtil.EXAMINE_RECORD;
import static com.example.familymembermanagement.util.UrlApiUtil.HOME_DATA;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
 * 审核的历史记录
 */
public class ExamineRecordActivity extends Fragment implements UnifiedRuleFragment,
        View.OnClickListener, AdapterView.OnItemSelectedListener,
        AdapterView.OnItemLongClickListener {

    private Spinner examine_spinner;
    private ImageView examine_search;
    private ListView examine_listview;
    private CustomDialog customDialog;
    //首页视图
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Gson gson = new Gson();
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    //下拉框的值
    private String search;
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
                success(data);
            } else if (msg.what == 0x12) {
                String data = msg.obj.toString();
                fail(data);
            }
        }
    };

    //初始化数据
    public ExamineRecordActivity(Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.toolbar = toolbar;
        this.navigationView = nav;
        this.drawerLayout = drawerLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.examine_record_fragment, null);
        //控件隐藏
        toolbar.setVisibility(View.GONE);
        navigationView.setVisibility(View.GONE);
        //关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView(View view) {
        examine_spinner = view.findViewById(R.id.examine_spinner);
        examine_search = view.findViewById(R.id.examine_search);
        examine_listview = view.findViewById(R.id.examine_listview);
    }

    @Override
    public void initData() {
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        //Map<String,Object> map=new HashMap<>();
        apiCall(null);
    }

    /**
     * 添加事件监听
     */
    @Override
    public void initEvent() {
        examine_search.setOnClickListener(this);
        examine_spinner.setOnItemSelectedListener(this);
        examine_listview.setOnItemLongClickListener(this);
    }

    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        List<Map<String, Object>> list = new ArrayList<>();
        if (data != null) {
            ExamineReturn examineReturn = gson.fromJson(data, ExamineReturn.class);
            if (examineReturn.getData() != null) {
                List<Examine> data1 = examineReturn.getData();
                for (Examine e : data1) {
                    Map<String, Object> map = new HashMap<>();
                    //审核类型
                    Integer type = e.getExamineType();
                    if (type == 0) {
                        map.put("extype", "添加出生成员");
                    } else if (type == 1) {
                        map.put("extype", "添加家庭成员");
                    } else if (type == 2) {
                        map.put("extype", "删除家庭成员");
                    }
                    //审核状态
                    Integer status = e.getExamineStatus();
                    if (status == 0) {
                        map.put("exstatus", "待审核");
                    } else if (status == 1) {
                        map.put("exstatus", "审核通过");
                    } else if (status == 2) {
                        map.put("exstatus", "审核驳回");
                    }
                    map.put("id", e.getId());
                    //审核时间
//                    Date time = e.getExamineTime();
//                    String formatDate = ToolUtil.formatDate(time);
                    map.put("extime", e.getExamineTime());
                    list.add(map);
                }
            }
        }
        String[] from = {"extype", "exstatus", "extime"};
        int[] to = {R.id.extype, R.id.exstatus, R.id.extime};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.examine_record_list, from, to);
        examine_listview.setAdapter(adapter);
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 请求审核记录数据
     */
    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(EXAMINE_RECORD);
        //单个参数的请求
        FormBody formBody = null;
        if (params == null) {
            formBody = new FormBody.Builder().build();
        } else {
            formBody = new FormBody.Builder()
                    .add("status", params.get("status").toString())
                    .build();
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
     * 图片监听
     */

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.examine_search) {
            Map<String, Object> map = new HashMap<>();
            if (search.equals("全部记录")) {
                map.put("status", "");
            } else if (search.equals("审核通过")) {
                map.put("status", 1);
            } else if (search.equals("审核驳回")) {
                map.put("status", 2);
            }
            apiCall(map);
        }
    }

    /**
     * 下拉框监听
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //获取下拉框的值
        search = adapterView.getItemAtPosition(i).toString();
        long id = adapterView.getItemIdAtPosition(i);
        System.err.println(id + search);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * listview长按监听事件
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //震动
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);//震动时间(ms)
        //获取单击时的数据
        Map<String, Object> itemdata = (Map<String, Object>) adapterView.getItemAtPosition(i);
        //弹出对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("确认删除此项?");
        builder.setIcon(R.mipmap.ti_shi);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String id = (String) itemdata.get("id");
                //传递请求参数
                Map<String, Object> pamars = new HashMap<>();
                pamars.put("id", id);
                apiCall02(pamars);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
        return false;
    }

    /**
     * 删除历史记录请求
     */
    public void apiCall02(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(DELETE_EXAMINE);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("ids", params.get("id").toString())
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
     * 请求成功
     */
    public void success(String data) {
        ToastUtil.toast(getActivity(), "删除成功");
        Map<String, Object> map = new HashMap<>();
        if (search.equals("全部记录")) {
            map.put("status", "");
        } else if (search.equals("审核通过")) {
            map.put("status", 1);
        } else if (search.equals("审核驳回")) {
            map.put("status", 2);
        }
        apiCall(map);
    }

    /**
     * 请求失败
     */
    public void fail(String data) {
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }
}
