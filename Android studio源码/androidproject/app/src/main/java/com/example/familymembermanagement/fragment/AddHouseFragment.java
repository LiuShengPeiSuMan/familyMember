package com.example.familymembermanagement.fragment;

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
import com.example.familymembermanagement.activity.HomepageActivity;
import com.example.familymembermanagement.pojo.AddHouse;
import com.example.familymembermanagement.pojo.HouseSituation;
import com.example.familymembermanagement.pojo.Notice;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.pojo.returndata.HouseReturnData;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.example.familymembermanagement.util.ToolUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 添加户主
 */
public class AddHouseFragment extends Fragment implements UnifiedRuleFragment,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String loginName;
    //控件
    private EditText h_name, h_age, h_houseaddress, h_date, h_work, h_workaddress, h_phone,
            h_email, h_houseNumber;
    //控件值
    private String name, sex, age, houseaddress, date, married, school, work,
            workaddress, phone, email, houseNumber, peopleNumber, deadNumber;
    private Button add_ok;
    private Spinner h_school;
    private RadioGroup group_sex, group_married;
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

    public AddHouseFragment(String loginName, Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.loginName = loginName;
        this.toolbar = toolbar;
        this.nav = nav;
        this.drawerLayout = drawerLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_house_fragment, null);
        initView(view);
        toolbar.setVisibility(View.GONE);
        //关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //initData();
        initEvent();
        return view;
    }

    /**
     * 初始化视图控件
     */
    @Override
    public void initView(View view) {
        h_name = view.findViewById(R.id.h_name);
        h_age = view.findViewById(R.id.h_age);
        h_houseaddress = view.findViewById(R.id.h_houseaddress);
        h_date = view.findViewById(R.id.h_date);
        h_work = view.findViewById(R.id.h_work);
        h_workaddress = view.findViewById(R.id.h_workaddress);
        h_phone = view.findViewById(R.id.h_phone);
        h_email = view.findViewById(R.id.h_email);
        h_houseNumber = view.findViewById(R.id.h_houseNumber);
        add_ok = view.findViewById(R.id.add_ok);
        h_school = view.findViewById(R.id.h_school);
        group_sex = view.findViewById(R.id.group_sex);
        group_married = view.findViewById(R.id.group_married);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        name = h_name.getText().toString();
        age = h_age.getText().toString();
        houseaddress = h_houseaddress.getText().toString();
        date = h_date.getText().toString();
        work = h_work.getText().toString();
        workaddress = h_workaddress.getText().toString();
        phone = h_phone.getText().toString();
        email = h_email.getText().toString();
        houseNumber = h_houseNumber.getText().toString();
//        peopleNumber = h_peopleNumber.getText().toString();
//        deadNumber = h_deadNumber.getText().toString();
    }

    /**
     * 初始化监听事件
     */
    @Override
    public void initEvent() {
        add_ok.setOnClickListener(this);
        h_date.setOnClickListener(this);
        h_school.setOnItemSelectedListener(this);
    }

    /**
     * 请求成功
     */
    @Override
    public void requestSuccess(String data) {
        AddHouseReturnData addHouseReturnData = gson.fromJson(data, AddHouseReturnData.class);
        String data1 = addHouseReturnData.getData();
        loadDialog.dismiss();
        ToastUtil.toast(getActivity(), data1);
    }

    /**
     * 请求失败
     */
    @Override
    public void requestFail(String data) {
        loadDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 接口调用
     */
    @Override
    public void apiCall(Map<String, Object> params) {
        //添加户主对象
        AddHouse addHouse = new AddHouse();
        addHouse.setLoginName(params.get("loginName").toString());
        addHouse.setName(params.get("name").toString());
        addHouse.setAge(Integer.valueOf(params.get("age").toString()));
        String sex = params.get("sex").toString();
        if (sex.equals("男")) {
            addHouse.setSex(1);
        } else {
            addHouse.setSex(0);
        }
        addHouse.setHomeAddress(params.get("homeAddress").toString());
        //addHouse.setDateOfBirth(new Date());
        addHouse.setDateOfBirth(params.get("dateOfBirth").toString() + " 00:00:00");
        String married = params.get("marriedOfNot").toString();
        if (married.equals("是")) {
            addHouse.setMarriedOfNot(1);
        } else {
            addHouse.setMarriedOfNot(0);
        }
        //学历
        String education = params.get("education").toString();
        int i = ToolUtil.educationToInt(education);
        addHouse.setEducation(i);
        //工作
        addHouse.setWork(params.get("work").toString());
        addHouse.setWorkAddress(params.get("workAddress").toString());
        addHouse.setPhone(params.get("phone").toString());
        addHouse.setEmail(params.get("email").toString());
        addHouse.setHouseNumber(Integer.valueOf(params.get("houseNumber").toString()));
        //消息
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(ADD_HOUSE);
        //请求参数
        String notice = gson.toJson(addHouse);
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

    /**
     * 添加按钮，出生日期按钮
     */
    @Override
    public void onClick(View view) {
        //点击添加按钮
        if (view.getId() == R.id.add_ok) {
            initData();
            //户主姓名
            if (name == null || name.equals("")) {
                ToastUtil.toast(getActivity(), "请填写户主姓名");
                return;
            }
            //获取性别
            for (int i = 0; i < group_sex.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) group_sex.getChildAt(i);
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
            if (houseaddress == null || houseaddress.equals("")) {
                ToastUtil.toast(getActivity(), "请输入家庭住址");
                return;
            }
            //出生日期
            if (date == null || date.equals("")) {
                ToastUtil.toast(getActivity(), "请选择出生日期");
                return;
            }
            //是否已婚
            for (int j = 0; j < group_married.getChildCount(); j++) {
                RadioButton radioButton = (RadioButton) group_married.getChildAt(j);
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
            //门牌号
            if (houseNumber == null || houseNumber.equals("")) {
                ToastUtil.toast(getActivity(), "门牌号不能为空");
                return;
            }
            //发请求
            Map<String, Object> map = new HashMap<>();
            map.put("loginName", loginName);
            map.put("name", name);
            map.put("age", age);
            map.put("sex", sex);
            map.put("homeAddress", houseaddress);
            map.put("dateOfBirth", date);
            map.put("marriedOfNot", married);
            map.put("education", school);
            map.put("workAddress", workaddress);
            map.put("work", work);
            map.put("phone", phone);
            map.put("email", email);
            map.put("houseNumber", houseNumber);
            apiCall(map);
            loadDialog = new CustomDialog(getActivity(), "正在添加...");
            loadDialog.show();
        } else if (view.getId() == R.id.h_date) {
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
                h_date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
