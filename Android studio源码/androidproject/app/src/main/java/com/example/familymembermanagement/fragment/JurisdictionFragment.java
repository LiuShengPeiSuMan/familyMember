package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_ALL_LOGIN;
import static com.example.familymembermanagement.util.UrlApiUtil.RELIEVE_LOGIN;
import static com.example.familymembermanagement.util.UrlApiUtil.RESET_USERLOGIN;
import static com.example.familymembermanagement.util.UrlApiUtil.USER_LOGIN;

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
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.familymembermanagement.pojo.Jurisdiction;
import com.example.familymembermanagement.pojo.returndata.AddHouseReturnData;
import com.example.familymembermanagement.pojo.returndata.JurisdictionReturnData;
import com.example.familymembermanagement.pojo.returndata.JurisdictionReturnData02;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
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
 * ????????????
 */
@SuppressWarnings("all")
public class JurisdictionFragment extends Fragment implements UnifiedRuleFragment, AdapterView.OnItemClickListener, View.OnClickListener {

    private TextView ju_count;
    private TextView jurisdiction_name, jurisdiction_account, jurisdiction_password, jurisdiction_email,
            jurisdiction_role, jurisdiction_addTime, jurisdiction_updateTime, jurisdiction_rest, jurisdiction_relieve;
    private EditText ju_edittext;
    private ImageView ju_search, ju_add;
    private ListView ju_list;
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private Gson gson = new Gson();
    private CustomDialog customDialog;
    private Dialog dialog;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private String loginName;
    private Integer role;
    //?????????okhttp
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
                success03(data);
            } else if (msg.what == 0x22) {
                String data = msg.obj.toString();
                fail03(data);
            } else if (msg.what == 0x31) {
                String data = msg.obj.toString();
                success04(data);
            } else if (msg.what == 0x32) {
                String data = msg.obj.toString();
                fail04(data);
            }
        }
    };

    //???????????????
    public JurisdictionFragment(String loginName, Integer role, Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.loginName = loginName;
        this.role = role;
        this.toolbar = toolbar;
        this.nav = nav;
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
        View view = inflater.inflate(R.layout.jurisdiction_fagment, null);
        toolbar.setVisibility(View.VISIBLE);
        //??????????????????
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    @Override
    public void initView(View view) {
        ju_count = view.findViewById(R.id.ju_count);
        ju_search = view.findViewById(R.id.ju_search);
        ju_edittext = view.findViewById(R.id.ju_edittext);
        ju_add = view.findViewById(R.id.ju_add);
        ju_list = view.findViewById(R.id.ju_list);
    }

    @Override
    public void initData() {
        customDialog = new CustomDialog(getActivity(), "????????????...");
        customDialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {
        ju_search.setOnClickListener(this);
        ju_add.setOnClickListener(this);
        ju_list.setOnItemClickListener(this);
    }

    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        //????????????
        JurisdictionReturnData jurisdictionReturnData = gson.fromJson(data, JurisdictionReturnData.class);
        List<Jurisdiction> jurisdictions = jurisdictionReturnData.getData();
        List<Map<String, Object>> list = new ArrayList<>();
        if (jurisdictions.size() > 0) {
            for (Jurisdiction j : jurisdictions) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", j.getId());
                map.put("name", j.getNickname());
                Integer role = j.getRole();
                if (role == 1) {
                    map.put("role", "??????");
                } else if (role == 2) {
                    map.put("role", "??????");
                } else if (role == 3) {
                    map.put("role", "????????????");
                }
                map.put("account", j.getAccount());
                map.put("password", j.getPassword());
                list.add(map);
            }
        }
        ju_count.setText("???" + jurisdictions.size() + "???");
        //???????????????
        String[] from = {"name", "role", "account", "password"};
        int[] to = {R.id.ju_name, R.id.ju_role, R.id.ju_account, R.id.ju_password};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.ju_list, from, to);
        ju_list.setAdapter(adapter);
        //???????????????
        drawerLayout.closeDrawer(nav);
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "?????????????????????");
    }

    /**
     * ????????????????????????
     */
    public void success(String data) {
        JurisdictionReturnData02 jurisdictionReturnData02 = gson.fromJson(data, JurisdictionReturnData02.class);
        Jurisdiction jur = jurisdictionReturnData02.getData();
        if (jur != null) {
            //???????????????
            dialog = new Dialog(getActivity());
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.jurisdiction_dialog, null);
            initViewDialog(v);
            //????????????
            jurisdiction_name.setText(jur.getNickname());
            jurisdiction_account.setText(jur.getAccount());
            jurisdiction_password.setText(jur.getPassword());
            jurisdiction_email.setText(jur.getLoginEmail());
            Integer role = jur.getRole();
            if (role == 1) {
                jurisdiction_role.setText("??????");
            } else if (role == 2) {
                jurisdiction_role.setText("??????");
            } else if (role == 3) {
                jurisdiction_role.setText("????????????");
            }
            jurisdiction_addTime.setText(jur.getCreateTime());
            jurisdiction_updateTime.setText(jur.getUpdateTime());
            //??????
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(v);
            dialog = builder.create();
            dialog.show();
            //???????????????????????????????????????
            if (role == 1 || role == 2) {
                jurisdiction_relieve.setFocusable(false);
                jurisdiction_relieve.setBackgroundColor(Color.parseColor("#8a8a8a"));
            }
            if (role == 3) {
                //????????????
                jurisdiction_relieve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog = new CustomDialog(getActivity(), "????????????????????????...");
                        customDialog.show();
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", jur.getId());
                        map.put("account", jur.getAccount());
                        apiCall03(map);
                    }
                });
                //??????????????????????????????
                jurisdiction_relieve.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //????????????
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            jurisdiction_relieve.setBackgroundColor(Color.parseColor("#a2072c"));
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //????????????
                            jurisdiction_relieve.setBackgroundColor(Color.parseColor("#F60202"));
                        }
                        return false;
                    }
                });
            }
            //????????????
            jurisdiction_rest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialog = new CustomDialog(getActivity(), "????????????????????????...");
                    customDialog.show();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", jur.getId());
                    map.put("account", jur.getAccount());
                    apiCall04(map);
                }
            });
            //??????????????????????????????
            jurisdiction_rest.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    //????????????
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        jurisdiction_rest.setBackgroundColor(Color.parseColor("#0061b2"));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //????????????
                        jurisdiction_rest.setBackgroundColor(Color.parseColor("#2196F3"));
                    }
                    return false;
                }
            });
        }
        customDialog.dismiss();
    }

    /**
     * ????????????????????????
     */
    public void success03(String data) {
        AddHouseReturnData addHouseReturnData = gson.fromJson(data, AddHouseReturnData.class);
        String message = addHouseReturnData.getData();
        customDialog.dismiss();
        dialog.dismiss();
        apiCall(null);
        ToastUtil.toast(getActivity(), message);

    }

    public void fail03(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "?????????????????????");
    }

    /**
     * ??????????????????
     */
    public void success04(String data) {
        AddHouseReturnData addHouseReturnData = gson.fromJson(data, AddHouseReturnData.class);
        String message = addHouseReturnData.getData();
        customDialog.dismiss();
        dialog.dismiss();
        apiCall(null);
        ToastUtil.toast(getActivity(), message);
    }

    public void fail04(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "?????????????????????");
    }

    /**
     * ?????????????????????
     */
    public void initViewDialog(View view) {
        jurisdiction_name = view.findViewById(R.id.jurisdiction_name);
        jurisdiction_account = view.findViewById(R.id.jurisdiction_account);
        jurisdiction_password = view.findViewById(R.id.jurisdiction_password);
        jurisdiction_email = view.findViewById(R.id.jurisdiction_email);
        jurisdiction_role = view.findViewById(R.id.jurisdiction_role);
        jurisdiction_addTime = view.findViewById(R.id.jurisdiction_addTime);
        jurisdiction_updateTime = view.findViewById(R.id.jurisdiction_updateTime);
        jurisdiction_rest = view.findViewById(R.id.jurisdiction_rest);
        jurisdiction_relieve = view.findViewById(R.id.jurisdiction_relieve);
    }

    public void fail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "?????????????????????");
    }

    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //??????Request??????
        Request.Builder builder = new Request.Builder();
        builder.url(QUERY_ALL_LOGIN);
        //?????????????????????
        FormBody formBody;
        if (params == null) {
            formBody = new FormBody.Builder()
                    .add("name", "")
                    .build();
        } else {
            formBody = new FormBody.Builder()
                    .add("name", params.get("name").toString())
                    .build();
        }
        builder.post(formBody);
        Request request = builder.build();
        //??????????????????
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
        //??????Call
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
     * ???????????????????????????
     */
    public void apiCall02(Map<String, Object> params) {
        Message message = new Message();
        //??????Request??????
        Request.Builder builder = new Request.Builder();
        builder.url(USER_LOGIN);
        //?????????????????????
        FormBody formBody = new FormBody.Builder()
                .add("id", params.get("id").toString())
                .build();
        builder.post(formBody);
        Request request = builder.build();
        //??????????????????
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
        //??????Call
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
     * ??????????????????
     */
    public void apiCall03(Map<String, Object> params) {
        Message message = new Message();
        //??????Request??????
        Request.Builder builder = new Request.Builder();
        builder.url(RELIEVE_LOGIN);
        //?????????????????????
        FormBody formBody = new FormBody.Builder()
                .add("id", params.get("id").toString())
                .add("loginName", loginName)
                .add("account", params.get("account").toString())
                .build();
        builder.post(formBody);
        Request request = builder.build();
        //??????????????????
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
        //??????Call
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
     * ??????????????????
     */
    public void apiCall04(Map<String, Object> params) {
        Message message = new Message();
        //??????Request??????
        Request.Builder builder = new Request.Builder();
        builder.url(RESET_USERLOGIN);
        //?????????????????????
        FormBody formBody = new FormBody.Builder()
                .add("id", params.get("id").toString())
                .add("loginName", loginName)
                .add("account", params.get("account").toString())
                .build();
        builder.post(formBody);
        Request request = builder.build();
        //??????????????????
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
        //??????Call
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                message.what = 0x32;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                message.what = 0x31;
                message.obj = data;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
        customDialog = new CustomDialog(getActivity(), "????????????...");
        customDialog.show();
        apiCall02(map);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ju_search) {
            String tj = ju_edittext.getText().toString();
            Map<String, Object> map = new HashMap<>();
            map.put("name", tj);
            customDialog = new CustomDialog(getActivity(), "????????????...");
            customDialog.show();
            apiCall(map);
        } else if (view.getId() == R.id.ju_add) {
            //????????????????????????
            transaction = manager.beginTransaction();
            transaction.replace(R.id.linearfragment, new AddJurisdictionFragment(loginName, toolbar, nav, drawerLayout));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
