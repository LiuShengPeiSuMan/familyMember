package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.ALL_EXAMINE;
import static com.example.familymembermanagement.util.UrlApiUtil.EXAMINE_DETAILED;
import static com.example.familymembermanagement.util.UrlApiUtil.FINDINGS_OFAUTIT;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.BabySituation;
import com.example.familymembermanagement.pojo.Examine;
import com.example.familymembermanagement.pojo.FamilyMember;
import com.example.familymembermanagement.pojo.returndata.BabySituationReturnData;
import com.example.familymembermanagement.pojo.returndata.ExamineReturn;
import com.example.familymembermanagement.pojo.returndata.FamilyMemberReturnData;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.example.familymembermanagement.util.ToolUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * 待审核
 */
@SuppressWarnings("all")
public class ExamineFragment extends Fragment implements UnifiedRuleFragment, AdapterView.OnItemClickListener, View.OnClickListener {

    private String loginName;

    public ExamineFragment() {
        this.loginName = loginName;
    }

    private ListView examin_record;
    private TextView examine_count, examine_history;
    private Dialog dialog;
    private CustomDialog customDialog;
    //首页视图
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    //fragment事务
    private FragmentManager manager;
    private FragmentTransaction transaction;
    //审核类型
    private Integer examineType = null;
    //添加新生儿控件
    private TextView type, user, date, status, name, babysex, healthy, mother, father, dateOfBirth, adopt, reject;
    //家族成员控件
    private TextView metype, meuser, medate, mestatus, mename, mesex, meage, meaddress, medayofbirth, mereason, meadopt, mereject;
    private String reason, eid;
    private Gson gson = new Gson();
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
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
            } else if (msg.what == 0x21) {
                String data = msg.obj.toString();
                successTwo(data);
            } else if (msg.what == 0x22) {
                String data = msg.obj.toString();
                failTwo(data);
            }
        }
    };

    //获取数据
    public ExamineFragment(String loginName, Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.loginName = loginName;
        this.toolbar = toolbar;
        this.navigationView = nav;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.examine_fragment, null);
        initView(view);
        initData();
        //可见
        toolbar.setVisibility(View.VISIBLE);
        navigationView.setVisibility(View.VISIBLE);
        //打开手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        initEvent();
        //查看历史记录
        examine_history.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //手指按下
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    examine_history.setTextColor(Color.parseColor("#13227a"));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //手指抬起
                    examine_history.setTextColor(Color.parseColor("#2196F3"));
                }
                return false;
            }
        });
        return view;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView(View view) {
        //数据列表
        examin_record = view.findViewById(R.id.examin_record);
        //数据条数
        examine_count = view.findViewById(R.id.examine_count);
        //历史记录
        examine_history = view.findViewById(R.id.examine_history);
    }

    @Override
    public void initData() {
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall(null);
    }

    /**
     * 添加点击事件
     */
    @Override
    public void initEvent() {
        examine_history.setOnClickListener(this);
        examin_record.setOnItemClickListener(this);
    }

    /**
     * 请求成功
     */
    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        ExamineReturn examineReturn = gson.fromJson(data, ExamineReturn.class);
        if (examineReturn != null) {
            //获取数据
            List<Examine> examines = examineReturn.getData();
            if (examines != null) {
                List<Map<String, Object>> list = new ArrayList<>();
                for (Examine e : examines) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("eid", e.getId());
                    params.put("id", e.getBabyOrpeopleId());
                    params.put("reason", e.getReason());
                    params.put("submitUser", e.getSubmitUser());
                    String format = e.getCreateTime();
                    params.put("submitTime", format);
                    if (e.getExamineType() == 0) {
                        params.put("examineType", "添加出生成员");
                    } else if (e.getExamineType() == 1) {
                        params.put("examineType", "添加家庭成员");
                    } else if (e.getExamineType() == 2) {
                        params.put("examineType", "删除家庭成员");
                    }
                    list.add(params);
                }
                String[] from = {"submitUser", "submitTime", "examineType"};
                int[] to = {R.id.submit_user, R.id.submit_time, R.id.examine_type};
                //设置适配器
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.examin_record, from, to);
                examin_record.setAdapter(adapter);
            }
            //设置条数
            examine_count.setText("共" + examines.size() + "条");
        }
        //关闭
        drawerLayout.closeDrawer(navigationView);
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        //关闭
        drawerLayout.closeDrawer(navigationView);
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 查看审核详细信息请求结果
     */
    public void success(String data) {
        //添加新生儿
        if (examineType == 0) {
            //解析返回数据
            BabySituationReturnData babySituationReturnData = gson.fromJson(data, BabySituationReturnData.class);
            BabySituation baby = babySituationReturnData.getData();
            if (baby != null) {
                //弹出对话框
                dialog = new Dialog(getActivity());
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View v = inflater.inflate(R.layout.examine_baby_dialog, null);
                //初始化新生儿弹框
                babyInitView(v);
                type.setText("添加出生成员");
                user.setText(baby.getCreateUser());
                date.setText(baby.getCreateTime());
                status.setText("待审核");
                name.setText(baby.getName());
                if (baby.getSex() == 0) {
                    babysex.setText("女婴");
                } else if (baby.getSex() == 1) {
                    babysex.setText("男婴");
                }
                if (baby.getHealthy() == 0) {
                    healthy.setText("健康");
                } else if (baby.getHealthy() == 1) {
                    healthy.setText("亚健康");
                }
                mother.setText(baby.getMother());
                father.setText(baby.getFather());
                String[] s = baby.getDateOfBirth().split(" ");
                dateOfBirth.setText(s[0]);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
                //通过单击事件
                adopt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("id", eid);
                        params.put("type", 0);
                        params.put("status", 1);
                        params.put("examineUser", loginName);
                        apiCall03(params);
                    }
                });
                //驳回单击事件
                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("id", eid);
                        params.put("type", 0);
                        params.put("status", 2);
                        params.put("examineUser", loginName);
                        apiCall03(params);
                    }
                });
                System.err.println(eid);
                //通过触摸事件
                adopt.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //手指按下
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            adopt.setBackgroundColor(Color.parseColor("#0061b2"));
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //手指抬起
                            adopt.setBackgroundColor(Color.parseColor("#2196F3"));
                        }
                        return false;
                    }
                });
                //驳回触摸事件
                reject.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //手指按下
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            reject.setBackgroundColor(Color.parseColor("#a2072c"));
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //手指抬起
                            reject.setBackgroundColor(Color.parseColor("#F60202"));
                        }
                        return false;
                    }
                });
            } else {
                ToastUtil.toast(getActivity(), "暂无详细数据");
            }
        } else {
            FamilyMemberReturnData familyMemberReturnData = gson.fromJson(data, FamilyMemberReturnData.class);
            FamilyMember familyMember = familyMemberReturnData.getData();
            if (familyMember != null) {
                //弹出对话框
                dialog = new Dialog(getActivity());
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View v = inflater.inflate(R.layout.examine_member_dialog, null);
                //初始化家庭成员弹框
                memberInitView(v);
                //设置数据
                if (examineType == 1) {
                    metype.setText("添加家庭成员");
                } else if (examineType == 2) {
                    metype.setText("删除家庭成员");
                }
                meuser.setText(familyMember.getCreateUser());
                medate.setText(familyMember.getCreateTime());
                mestatus.setText("待审核");
                mename.setText(familyMember.getName());
                if (familyMember.getSex() == 0) {
                    mesex.setText("女");
                } else if (familyMember.getSex() == 1) {
                    mesex.setText("男");
                }
                meage.setText(familyMember.getAge().toString());
                meaddress.setText(familyMember.getHomeAddress());
                String[] s = familyMember.getDateOfBirth().split(" ");
                medayofbirth.setText(s[0]);
                mereason.setText(reason);
                //弹出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(v);
                dialog = builder.create();
                dialog.show();
                //通过单击事件
                meadopt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("id", eid);
                        if (examineType == 1) {
                            params.put("type", 1);
                        } else if (examineType == 2) {
                            params.put("type", 2);
                        }
                        customDialog = new CustomDialog(getActivity(), "正在通过...");
                        customDialog.show();
                        params.put("status", 1);
                        params.put("examineUser", loginName);
                        apiCall03(params);
                    }
                });
                //驳回单击事件
                mereject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("id", eid);
                        if (examineType == 1) { //添加家庭成员
                            params.put("type", 1);
                        } else if (examineType == 2) {  //删除家庭成员
                            params.put("type", 2);
                        }
                        customDialog = new CustomDialog(getActivity(), "正在驳回...");
                        customDialog.show();
                        params.put("status", 2);
                        params.put("examineUser", loginName);
                        apiCall03(params);
                    }
                });
                //通过触摸事件
                meadopt.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //手指按下
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            meadopt.setBackgroundColor(Color.parseColor("#0061b2"));
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //手指抬起
                            meadopt.setBackgroundColor(Color.parseColor("#2196F3"));
                        }
                        return false;
                    }
                });
                //驳回触摸事件
                mereject.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //手指按下
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            mereject.setBackgroundColor(Color.parseColor("#a2072c"));
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //手指抬起
                            mereject.setBackgroundColor(Color.parseColor("#F60202"));
                        }
                        return false;
                    }
                });
            } else {
                ToastUtil.toast(getActivity(), "暂无详细数据");
            }
        }
        customDialog.dismiss();
    }

    public void fail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    public void successTwo(String data) {
        //关闭弹窗
        customDialog.dismiss();
        dialog.dismiss();
        apiCall(null);

    }

    public void failTwo(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 查询所有审核记录请求
     */
    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(ALL_EXAMINE);
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
     * 查询审核记录详细信息
     */
    public void apiCall02(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(EXAMINE_DETAILED);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("id", params.get("id").toString())
                .add("type", params.get("type").toString())
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
     * 通过或驳回
     */
    public void apiCall03(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(FINDINGS_OFAUTIT);
        //单个参数的请求
        FormBody formBody = new FormBody.Builder()
                .add("id", params.get("id").toString())
                .add("type", params.get("type").toString())
                .add("status", params.get("status").toString())
                .add("examineUser", params.get("examineUser").toString())
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

    /**
     * 查看历史记录点击事件
     */
    @Override
    public void onClick(View view) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.linearfragment, new ExamineRecordActivity(toolbar, navigationView, drawerLayout));
        transaction.addToBackStack(null);
        transaction.commit();

    }

    /**
     * listView点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //获取listview每个item数据
        Map<String, Object> itemdata = (Map<String, Object>) adapterView.getItemAtPosition(i);
        eid = (String) itemdata.get("eid");
        String id = (String) itemdata.get("id");
        String type = (String) itemdata.get("examineType");
        //原由
        reason = (String) itemdata.get("reason");
        if (type.equals("添加出生成员")) { //0
            examineType = 0;
        } else if (type.equals("添加家庭成员")) { //1
            examineType = 1;
        } else if (type.equals("删除家庭成员")) { //2
            examineType = 2;
        }
        //发请求
        customDialog = new CustomDialog(getActivity(), "正在加载数据...");
        customDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", examineType);
        apiCall02(map);
    }

    /**
     * 添加新生儿弹框初始化视图
     */
    public void babyInitView(View v) {
        type = v.findViewById(R.id.type);
        user = v.findViewById(R.id.user);
        date = v.findViewById(R.id.date);
        status = v.findViewById(R.id.status);
        name = v.findViewById(R.id.name);
        babysex = v.findViewById(R.id.babysex);
        healthy = v.findViewById(R.id.healthy);
        mother = v.findViewById(R.id.mother);
        father = v.findViewById(R.id.father);
        dateOfBirth = v.findViewById(R.id.dateOfBirth);
        //通过
        adopt = v.findViewById(R.id.adopt);
        //驳回
        reject = v.findViewById(R.id.reject);
    }

    /**
     * 家庭成员控件初始化
     */
    public void memberInitView(View v) {
        metype = v.findViewById(R.id.metype);
        meuser = v.findViewById(R.id.meuser);
        medate = v.findViewById(R.id.medate);
        mestatus = v.findViewById(R.id.mestatus);
        mename = v.findViewById(R.id.mename);
        mesex = v.findViewById(R.id.mesex);
        meage = v.findViewById(R.id.meage);
        meaddress = v.findViewById(R.id.meaddress);
        medayofbirth = v.findViewById(R.id.medayofbirth);
        mereason = v.findViewById(R.id.mereason);
        meadopt = v.findViewById(R.id.meadopt);
        mereject = v.findViewById(R.id.mereject);
    }

}
