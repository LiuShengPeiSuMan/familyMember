package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.ADD_BABY;
import static com.example.familymembermanagement.util.UrlApiUtil.RELEASE_NOTICE;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.AddBaby;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 添加出生成员
 */
public class AddBabyFragment extends Fragment implements UnifiedRuleFragment, View.OnClickListener {

    private String loginId, loginName;
    private EditText ba_name, ba_date, ba_father, ba_mother, ba_reason;
    private String name, date, father, mother, reason, sex, healthy;
    private RadioGroup bagroup_sex, bagroup_healthy;
    private Button ba_ok;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private Gson gson = new Gson();
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    //加载
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

    public AddBabyFragment(String loginId, String loginName,
                           Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.loginId = loginId;
        this.loginName = loginName;
        this.toolbar = toolbar;
        this.nav = nav;
        this.drawerLayout = drawerLayout;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_baby_fragment, null);
        initView(view);
        //隐藏
        toolbar.setVisibility(View.GONE);
        //关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //initData();
        initEvent();
        return view;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView(View view) {
        ba_name = view.findViewById(R.id.ba_name);
        ba_date = view.findViewById(R.id.ba_date);
        ba_father = view.findViewById(R.id.ba_father);
        ba_mother = view.findViewById(R.id.ba_mother);
        ba_reason = view.findViewById(R.id.ba_reason);
        bagroup_sex = view.findViewById(R.id.bagroup_sex);
        bagroup_healthy = view.findViewById(R.id.bagroup_healthy);
        ba_ok = view.findViewById(R.id.ba_ok);
    }

    @Override
    public void initData() {
        name = ba_name.getText().toString();
        //获取性别
        for (int i = 0; i < bagroup_sex.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) bagroup_sex.getChildAt(i);
            if (radioButton.isChecked()) {
                sex = radioButton.getText().toString();
                break;
            }
        }
        //是否健康
        for (int j = 0; j < bagroup_healthy.getChildCount(); j++) {
            RadioButton radioButton = (RadioButton) bagroup_healthy.getChildAt(j);
            if (radioButton.isChecked()) {
                healthy = radioButton.getText().toString();
                break;
            }
        }
        father = ba_father.getText().toString();
        mother = ba_mother.getText().toString();
        date = ba_date.getText().toString();
        reason = ba_reason.getText().toString();
    }

    @Override
    public void initEvent() {
        ba_ok.setOnClickListener(this);
        ba_date.setOnClickListener(this);
    }

    @Override
    public void requestSuccess(String data) {
        AddHouseReturnData addHouseReturnData = gson.fromJson(data, AddHouseReturnData.class);
        String data1 = addHouseReturnData.getData();
        dialog.dismiss();
        ToastUtil.toast(getActivity(), data1);
    }

    @Override
    public void requestFail(String data) {
        dialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    @Override
    public void apiCall(Map<String, Object> params) {
        AddBaby addBaby = new AddBaby();
        addBaby.setLoginId(loginId);
        addBaby.setLoginName(loginName);
        addBaby.setBabyName(name);
        if (sex.equals("男婴")) {
            addBaby.setBabySex(1);
        } else {
            addBaby.setBabySex(0);
        }

        if (healthy.equals("健康")) {
            addBaby.setBabyHealthy(0);
        } else {
            addBaby.setBabyHealthy(1);
        }
        addBaby.setBabyFather(father);
        addBaby.setBabyMother(mother);
        addBaby.setBabyDateOfBirth(date + " 00:00:00");
        addBaby.setReason(reason);
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(ADD_BABY);
        //请求参数
        String notice = gson.toJson(addBaby);
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

    @Override
    public void onClick(View view) {
        initData();
        if (view.getId() == R.id.ba_ok) {
            if (name == null || name.equals("")) {
                ToastUtil.toast(getActivity(), "出生成员名称不能为空");
                return;
            }
            if (sex == null || sex.equals("")) {
                ToastUtil.toast(getActivity(), "性别不能为空");
                return;
            }
            if (date == null || date.equals("")) {
                ToastUtil.toast(getActivity(), "出生日期不能为空");
                return;
            }
            if (father == null || father.equals("")) {
                ToastUtil.toast(getActivity(), "婴儿父亲不能为空");
                return;
            }
            if (mother == null || mother.equals("")) {
                ToastUtil.toast(getActivity(), "婴儿母亲不呢为空");
                return;
            }
            if (reason == null || reason.equals("")) {
                ToastUtil.toast(getActivity(), "添加原因不能为空");
                return;
            }
            if (healthy == null || healthy.equals("")) {
                ToastUtil.toast(getActivity(), "健康状态不能为空");
                return;
            }
            dialog = new CustomDialog(getActivity(), "正在添加...");
            dialog.show();
            apiCall(null);
        } else if (view.getId() == R.id.ba_date) {
            //弹出日历对话框
            showDatePickDlg();
        }

    }

    //日历对话框
    private void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                ba_date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
