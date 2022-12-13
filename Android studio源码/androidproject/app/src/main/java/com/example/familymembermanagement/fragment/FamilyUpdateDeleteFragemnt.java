package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.DELETE_FAMILY;
import static com.example.familymembermanagement.util.UrlApiUtil.FAMILY_DETAILED;
import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_FAMILY_INFORMATION;
import static com.example.familymembermanagement.util.UrlApiUtil.UPDATE_FAMILY;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.FamilyMember;
import com.example.familymembermanagement.pojo.UpdateFamily;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.pojo.returndata.FamilyMemberReturnData;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.example.familymembermanagement.util.ToolUtil;
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
 * 家族成员修改和删除
 */
public class FamilyUpdateDeleteFragemnt extends Fragment implements UnifiedRuleFragment, View.OnClickListener {

    private ImageView in_update, in_ok, in_delete;

    private EditText in_name, in_sex, in_age, in_isMarry, in_houseAddress, in_date,
            in_education, in_work, in_workAddress, in_phone, in_email, del_reason;
    private LinearLayout uandd;
    private Button delete_ok;

    private String familyPeopleId, loginName;
    private Dialog dialog;

    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private Gson gson = new Gson();
    //初始化okhttp
    private OkHttpClient client = new OkHttpClient();
    private CustomDialog customDialog;
    private FamilyMember familyMember;

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
                updateSuccess(data);
            } else if (msg.what == 0x12) {
                String data = msg.obj.toString();
                updateFail(data);
            } else if (msg.what == 0x21) {
                String data = msg.obj.toString();
                deleteSuccess(data);
            } else if (msg.what == 0x22) {
                String data = msg.obj.toString();
                deleteFail(data);
            }
        }
    };

    //获取家族成员id
    public FamilyUpdateDeleteFragemnt(String familyPeopleId, String loginName,
                                      Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.familyPeopleId = familyPeopleId;
        this.loginName = loginName;
        this.toolbar = toolbar;
        this.nav = nav;
        this.drawerLayout = drawerLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.family_update_delete_fragment, null);
        initView(view);
        //隐藏
        toolbar.setVisibility(View.GONE);
        //关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //隐藏提交按钮
        in_ok.setVisibility(View.GONE);
        touch();
        initData();
        initEvent();
        return view;
    }

    /**
     * 触摸
     */
    @SuppressWarnings("all")
    public void touch() {
        uandd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                after(in_isMarry);
                after(in_houseAddress);
                after(in_education);
                after(in_work);
                after(in_workAddress);
                after(in_phone);
                after(in_email);
                //隐藏按钮
                in_ok.setVisibility(View.GONE);
                return false;
            }
        });
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView(View view) {
        in_update = view.findViewById(R.id.in_update);
        in_ok = view.findViewById(R.id.in_ok);
        in_delete = view.findViewById(R.id.in_delete);
        in_name = view.findViewById(R.id.in_name);
        in_sex = view.findViewById(R.id.in_sex);
        in_age = view.findViewById(R.id.in_age);
        in_isMarry = view.findViewById(R.id.in_isMarry);
        in_houseAddress = view.findViewById(R.id.in_houseAddress);
        in_date = view.findViewById(R.id.in_date);
        in_education = view.findViewById(R.id.in_education);
        in_work = view.findViewById(R.id.in_work);
        in_workAddress = view.findViewById(R.id.in_workAddress);
        in_phone = view.findViewById(R.id.in_phone);
        in_email = view.findViewById(R.id.in_email);
        uandd = view.findViewById(R.id.uandd);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        customDialog = new CustomDialog(getActivity(), "正在加载数据...");
        customDialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {
        in_update.setOnClickListener(this);
        in_delete.setOnClickListener(this);
        in_ok.setOnClickListener(this);
    }


    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        FamilyMemberReturnData familyMemberReturnData = gson.fromJson(data, FamilyMemberReturnData.class);
        //获取数据
        familyMember = familyMemberReturnData.getData();
        if (familyMember != null) {
            in_name.setText(familyMember.getName());
            Integer sex = familyMember.getSex();
            if (sex == 1) {
                in_sex.setText("男");
            } else {
                in_sex.setText("女");
            }
            in_age.setText(familyMember.getAge().toString());
            Integer marriedOfNot = familyMember.getMarriedOfNot();
            if (marriedOfNot == 1) {
                in_isMarry.setText("是");
            } else {
                in_isMarry.setText("否");
            }
            in_houseAddress.setText(familyMember.getHomeAddress());
            String[] s = familyMember.getDateOfBirth().split(" ");
            in_date.setText(s[0]);
            //学历
            String education = ToolUtil.education(familyMember.getEducation());
            System.err.println(education);
            in_education.setText(education);
            in_work.setText(familyMember.getWork());
            in_workAddress.setText(familyMember.getWorkAddress());
            in_phone.setText(familyMember.getPhone());
            in_email.setText(familyMember.getEmail());
        }
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 修改成功请求或失败方法
     */
    public void updateSuccess(String data) {
        after(in_isMarry);
        after(in_houseAddress);
        after(in_education);
        after(in_work);
        after(in_workAddress);
        after(in_phone);
        after(in_email);
        //提示
        customDialog.dismiss();
        AddHouseReturnData returnData = gson.fromJson(data, AddHouseReturnData.class);
        ToastUtil.toast(getActivity(), returnData.getData());
    }

    public void updateFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 删除成功或失败方法
     */
    public void deleteSuccess(String data) {
        AddHouseReturnData returnData = gson.fromJson(data, AddHouseReturnData.class);
        String msg = returnData.getData();
        ToastUtil.toast(getActivity(), msg);
        //关闭弹框
        dialog.dismiss();
        customDialog.dismiss();
        //销毁fragment
        getActivity().onBackPressed();
    }

    public void deleteFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    @Override
    public void apiCall(Map<String, Object> params) {

        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(FAMILY_DETAILED);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("id", familyPeopleId)
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

    /**
     * 点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.in_update:    //更新
                change(in_isMarry);
                change(in_houseAddress);
                change(in_education);
                change(in_work);
                change(in_workAddress);
                change(in_phone);
                change(in_email);
                //按钮可见
                in_ok.setVisibility(View.VISIBLE);
                break;
            case R.id.in_delete:    //删除
                //取消边框
                after(in_isMarry);
                after(in_houseAddress);
                after(in_education);
                after(in_work);
                after(in_workAddress);
                after(in_phone);
                after(in_email);
                //弹出自定义对话框
                dialog = new Dialog(getActivity());
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View v = inflater.inflate(R.layout.family_delete_dialog, null);
                //删除原因
                del_reason = v.findViewById(R.id.del_reason);
                //确认删除按钮
                delete_ok = v.findViewById(R.id.delete_ok);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
                //点击删除
                delete_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //获取原因
                        String reason = del_reason.getText().toString();
                        if (reason == null || reason.equals("")) {
                            ToastUtil.toast(getActivity(), "请填写删除原因");
                            return;
                        }
                        //发送删除请求
                        customDialog = new CustomDialog(getActivity(), "正在删除...");
                        customDialog.show();
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", familyMember.getId());
                        map.put("reason", reason);
                        deleteApiCall(map);
                    }
                });
                break;
            case R.id.in_ok:    //确认
                if (in_isMarry.getText() == null || in_isMarry.getText().toString().equals("")) {
                    ToastUtil.toast(getActivity(), "是否已婚不能为空");
                    in_isMarry.setBackgroundResource(R.drawable.error_tips);
                    return;
                }
                String marry = in_isMarry.getText().toString();
                if (!marry.equals("是") && !marry.equals("否")) {
                    ToastUtil.toast(getActivity(), "是否已婚请填写是或否");
                    in_isMarry.setBackgroundResource(R.drawable.error_tips);
                    return;
                }
                if (in_houseAddress.getText() == null || in_houseAddress.getText().toString().equals("")) {
                    ToastUtil.toast(getActivity(), "家庭住址不能为空");
                    return;
                }
                String education = in_education.getText().toString();
                if (in_education.getText() == null || in_education.getText().toString().equals("")) {
                    in_education.setBackgroundResource(R.drawable.error_tips);
                    ToastUtil.toast(getActivity(), "学历不能为空");
                    return;
                }
                //判断填写的是不是指定学历
                boolean inputEd = ToolUtil.inputEd(education);
                if (!inputEd) {
                    ToastUtil.toast(getActivity(), "学历只能填【未上学,幼儿园,小学,初中,中专,普高,大专,大学,研究生,博士,硕士】");
                    in_education.setBackgroundResource(R.drawable.error_tips);
                    return;
                }
                if (in_work.getText() == null || in_work.getText().toString().equals("")) {
                    in_work.setBackgroundResource(R.drawable.error_tips);
                    ToastUtil.toast(getActivity(), "工作职位不能为空");
                    return;
                }
                if (in_workAddress.getText() == null || in_workAddress.getText().toString().equals("")) {
                    in_workAddress.setBackgroundResource(R.drawable.error_tips);
                    ToastUtil.toast(getActivity(), "工作地点不能为空");
                    return;
                }
                if (in_phone.getText() == null || in_phone.getText().toString().equals("")) {
                    in_phone.setBackgroundResource(R.drawable.error_tips);
                    ToastUtil.toast(getActivity(), "联系电话不能为空");
                    return;
                }
                if (in_email.getText() == null || in_email.getText().toString().equals("")) {
                    in_email.setBackgroundResource(R.drawable.error_tips);
                    ToastUtil.toast(getActivity(), "电子邮箱不能为空");
                    return;
                }
                //弹出加载框
                customDialog = new CustomDialog(getActivity(), "正在提交修改...");
                customDialog.show();
                //发请求
                updateApiCall(null);
                break;
        }
    }

    /**
     * EditText获取焦点设置边框
     */
    public void change(EditText editText) {
        editText.setFocusable(true);
        editText.setBackgroundResource(R.drawable.family_update_delete);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //显示软键盘
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    //点击提交之后
    public void after(EditText editText) {
        editText.setFocusable(false);
        editText.setBackground(null);
    }

    /**
     * 更新请求
     */
    public void updateApiCall(Map<String, Object> params) {
        UpdateFamily updateFamily = new UpdateFamily();
        updateFamily.setId(familyPeopleId);
        //是否已婚
        String ed = in_isMarry.getText().toString();
        if (ed.equals("是")) {
            updateFamily.setMarriedOfNot(1);
        } else {
            updateFamily.setMarriedOfNot(0);
        }
        updateFamily.setHomeAddress(in_houseAddress.getText().toString());
        //学历
        int education = ToolUtil.educationToInt(in_education.getText().toString());
        updateFamily.setEducation(education);
        updateFamily.setWork(in_work.getText().toString());
        updateFamily.setWorkAddress(in_workAddress.getText().toString());
        updateFamily.setPhone(in_phone.getText().toString());
        updateFamily.setEmail(in_email.getText().toString());
        updateFamily.setLoginName(loginName);
        //消息
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(UPDATE_FAMILY);
        //单个参数的请求
        String json = gson.toJson(updateFamily);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
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
     * 删除请求
     */
    public void deleteApiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(DELETE_FAMILY);
        //单个参数的请求
        String id = (String) params.get("id");
        FormBody formBody = new FormBody.Builder()
                .add("id", id)
                .add("reason", params.get("reason").toString())
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
                message.what = 0x22;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                message.what = 0x21;
                message.obj = data;
                handler.sendMessage(message);
            }
        });
    }
}
