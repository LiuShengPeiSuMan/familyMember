package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.ADD_FAMILY;
import static com.example.familymembermanagement.util.UrlApiUtil.ADD_HOUSE;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.AddFamily;
import com.example.familymembermanagement.pojo.AddHouse;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.example.familymembermanagement.util.ToolUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
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
 * 添加家庭成员
 */
public class AddFamilyFragment extends Fragment implements UnifiedRuleFragment, View.OnClickListener, AdapterView.OnItemSelectedListener {

    //控件
    private EditText f_name, f_age, f_homeAddress, f_date, f_work, f_workAddress, f_phone,
            f_email, f_reason, f_relationship;
    //控件值
    private String name, sex, age, homeaddress, date, married, school, work, reason,
            workaddress, phone, email, relationship;
    private Button fadd_ok;
    private Spinner f_school;
    private RadioGroup fgroup_sex, fgroup_married;
    private String loginId, loginName;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private Gson gson = new Gson();
    //自定义加载弹框
    private CustomDialog loadDialog;
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
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

    public AddFamilyFragment(String loginId, String loginName,
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
        View view = inflater.inflate(R.layout.add_family_fragment, null);
        toolbar.setVisibility(View.GONE);
        //关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initView(view);
        initEvent();
        return view;
    }

    @Override
    public void initView(View view) {
        f_name = view.findViewById(R.id.f_name);
        f_age = view.findViewById(R.id.f_age);
        f_homeAddress = view.findViewById(R.id.f_houseaddress);
        f_date = view.findViewById(R.id.f_date);
        f_work = view.findViewById(R.id.f_work);
        f_workAddress = view.findViewById(R.id.f_workaddress);
        f_phone = view.findViewById(R.id.f_phone);
        f_email = view.findViewById(R.id.f_email);
        fadd_ok = view.findViewById(R.id.fadd_ok);
        f_school = view.findViewById(R.id.f_school);
        fgroup_sex = view.findViewById(R.id.fgroup_sex);
        fgroup_married = view.findViewById(R.id.fgroup_married);
        f_reason = view.findViewById(R.id.f_reason);
        f_relationship = view.findViewById(R.id.f_relationship);
    }

    @Override
    public void initData() {
        name = f_name.getText().toString();
        age = f_age.getText().toString();
        homeaddress = f_homeAddress.getText().toString();
        date = f_date.getText().toString();
        work = f_work.getText().toString();
        workaddress = f_workAddress.getText().toString();
        phone = f_phone.getText().toString();
        email = f_email.getText().toString();
        reason = f_reason.getText().toString();
        relationship = f_relationship.getText().toString();
    }

    @Override
    public void initEvent() {
        fadd_ok.setOnClickListener(this);
        f_date.setOnClickListener(this);
        f_school.setOnItemSelectedListener(this);
    }

    @Override
    public void requestSuccess(String data) {
        AddHouseReturnData addHouseReturnData = gson.fromJson(data, AddHouseReturnData.class);
        String data1 = addHouseReturnData.getData();
        loadDialog.dismiss();
        ToastUtil.toast(getActivity(), data1);
    }

    @Override
    public void requestFail(String data) {
        loadDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    @Override
    public void apiCall(Map<String, Object> params) {
        //添加家庭成员
        AddFamily family = new AddFamily();
        family.setLoginId(params.get("loginId").toString());
        family.setLoginName(params.get("loginName").toString());
        family.setRelationship(params.get("relationship").toString());
        family.setName(params.get("name").toString());
        //性别
        String sex = params.get("sex").toString();
        if (sex.equals("男")) {
            family.setSex(1);
        } else {
            family.setSex(0);
        }
        family.setAge(Integer.valueOf(params.get("age").toString()));
        family.setHomeAddress(params.get("homeAddress").toString());
        family.setDateOfBirth(params.get("dateOfBirth").toString() + " 10:10:10");
        //是否已婚
        String marriedOfNot = params.get("marriedOfNot").toString();
        if (marriedOfNot.equals("是")) {
            family.setMarriedOfNot(1);
        } else {
            family.setMarriedOfNot(0);
        }
        //学历
        String education = params.get("education").toString();
        int i = ToolUtil.educationToInt(education);
        family.setEducation(i);
        //工作
        family.setWork(params.get("work").toString());
        family.setWorkAddress(params.get("workAddress").toString());
        family.setPhone(params.get("phone").toString());
        family.setEmail(params.get("email").toString());
        family.setReason(params.get("reason").toString());
        //消息
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(ADD_FAMILY);
        //请求参数
        String notice = gson.toJson(family);
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

    /**
     * 提交按钮
     */
    @Override
    public void onClick(View view) {
        initData();
        if (view.getId() == R.id.fadd_ok) {
            //户主姓名
            if (name == null || name.equals("")) {
                ToastUtil.toast(getActivity(), "请填写户主姓名");
                return;
            }
            //获取性别
            for (int i = 0; i < fgroup_sex.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) fgroup_sex.getChildAt(i);
                if (radioButton.isChecked()) {
                    sex = radioButton.getText().toString();
                    break;
                }
            }
            //年龄
            if (age == null || age.equals("")) {
                ToastUtil.toast(getActivity(), "请输入年龄");
                return;
            }
            //家庭住址
            if (homeaddress == null || homeaddress.equals("")) {
                ToastUtil.toast(getActivity(), "请输入家庭住址");
                return;
            }
            //出生日期
            if (date == null || date.equals("")) {
                ToastUtil.toast(getActivity(), "请选择出生日期");
                return;
            }
            //是否已婚
            for (int j = 0; j < fgroup_married.getChildCount(); j++) {
                RadioButton radioButton = (RadioButton) fgroup_married.getChildAt(j);
                if (radioButton.isChecked()) {
                    married = radioButton.getText().toString();
                    break;
                }
            }
            //学历
            if (school == null || school.equals("")) {
                ToastUtil.toast(getActivity(), "学历不能为空");
                return;
            }
            //工作职位
            if (work == null || work.equals("")) {
                ToastUtil.toast(getActivity(), "工作职位不能为空");
                return;
            }
            //工作地点
            if (workaddress == null || workaddress.equals("")) {
                ToastUtil.toast(getActivity(), "工作地点不能为空");
                return;
            }
            //电话号码
            if (phone == null || phone.equals("")) {
                ToastUtil.toast(getActivity(), "电话号码不能为空");
                return;
            }
            //电子邮箱
//            if (email == null || email.equals("")) {
//                ToastUtil.toast(getActivity(), "电子邮箱不能为空");
//                return;
//            }
            //添加原因
            if (reason == null || reason.equals("")) {
                ToastUtil.toast(getActivity(), "添加原因不能为空");
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("loginId", loginId);
            map.put("loginName", loginName);
            map.put("relationship", relationship);
            map.put("reason", reason);
            map.put("name", name);
            map.put("age", age);
            map.put("sex", sex);
            map.put("homeAddress", homeaddress);
            map.put("dateOfBirth", date);
            map.put("marriedOfNot", married);
            map.put("education", school);
            map.put("work", work);
            map.put("workAddress", workaddress);
            map.put("phone", phone);
            map.put("email", email);
            loadDialog = new CustomDialog(getActivity(), "正在添加...");
            loadDialog.show();
            apiCall(map);
        } else if (view.getId() == R.id.f_date) {
            showDatePickDlg();
        }
    }

    /**
     * 学历选择
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //获取下拉框被选中的值
        school = adapterView.getItemAtPosition(i).toString();
        long id = adapterView.getItemIdAtPosition(i);
        System.err.println(id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //日历对话框
    private void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                f_date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
