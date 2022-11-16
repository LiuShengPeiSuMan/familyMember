package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.HOME_DATA;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.activity.HomepageActivity;
import com.example.familymembermanagement.pojo.FamilyMember;
import com.example.familymembermanagement.pojo.Notice;
import com.example.familymembermanagement.pojo.returndata.HomeReturnData;
import com.example.familymembermanagement.pojo.returndata.NoticeAndBirthday;
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
 * 首页
 */
@SuppressWarnings("all")
public class HomeFragment extends Fragment implements UnifiedRuleFragment {

    private TextView notice, birthday;
    private ViewFlipper homeFlipper;
    private ListView home_listview;
    private LinearLayout linear;
    private CustomDialog customDialog;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Gson gson = new Gson();
    //初始化okhttp
    private static OkHttpClient client = new OkHttpClient();
    private int image[] = new int[]{R.mipmap.beijing01, R.mipmap.beijing02};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x01) {
                //请求成功
                String data = msg.obj.toString();
                requestSuccess(data);
                System.err.println("====================" + data);
            } else if (msg.what == 0x02) {
                //请求失败
                String data = msg.obj.toString();
                requestFail(data);
            }
        }
    };

    public HomeFragment(Toolbar zz_toolbar, NavigationView zz_navigationView, DrawerLayout zz_drawerLayout) {
        this.toolbar = zz_toolbar;
        this.drawerLayout = zz_drawerLayout;
        this.navigationView = zz_navigationView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, null);
        //初始化控件
        initView(view);
        //初始化数据
        initData();
        for (int i = 0; i < image.length; i++) {
            homeFlipper.addView(getimageview(image[i]));
        }
        //为ViewFlipper去添加动画效果
        homeFlipper.setInAnimation(HomeFragment.this.getActivity(), R.anim.right_out);
        homeFlipper.setOutAnimation(HomeFragment.this.getActivity(), R.anim.left_in);
        //开始播放
        homeFlipper.startFlipping();
        return view;
    }

    //设置图片
    private ImageView getimageview(int resID) {
        ImageView image = new ImageView(getActivity().getApplicationContext());
        image.setBackgroundResource(resID);
        return image;
    }

    @Override
    public void initView(View view) {
        //获取轮播图
        homeFlipper = view.findViewById(R.id.homeflipper);
        /**
         * 添加通知
         * */
        notice = view.findViewById(R.id.hometongzhi);
        //listview
        home_listview = view.findViewById(R.id.home_listview);
        //今日寿星
        birthday = view.findViewById(R.id.birthday);
        //生日列表
        linear = view.findViewById(R.id.linear);
    }

    @Override
    public void initData() {
        //请求
        customDialog = new CustomDialog(getActivity(), "正在加载...");
        customDialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void requestSuccess(String data) {
        customDialog.dismiss();
        //解析数据
        HomeReturnData homeReturnData = gson.fromJson(data, HomeReturnData.class);
        //有返回数据
        if (homeReturnData.getCode() == 1000) {
            if (homeReturnData.getData() != null) {
                //获取数据
                NoticeAndBirthday noticeAndBirthday = homeReturnData.getData();
                //获取通知数据
                List<Notice> natice = noticeAndBirthday.getNatice();
                //没有通知数据
                if (natice == null || natice.size() == 0) {
                    notice.setText("暂无通知，请尽情期待！");
                } else {
                    StringBuilder builder = new StringBuilder();
                    for (Notice n : natice) {
                        //通知内容
                        String noticeContent = n.getNoticeContent();
                        builder.append(noticeContent + "<-->");
                        //获取通知类型，设置不同的字体颜色
                        if (n.getNoticeType() == 0) {
                            notice.setTextColor(Color.parseColor("#efb336"));
                        } else if (n.getNoticeType() == 1) {
                            notice.setTextColor(Color.parseColor("#13227a"));
                        } else if (n.getNoticeType() == 2) {
                            notice.setTextColor(Color.RED);
                        } else if (n.getNoticeType() == 3) {
                            notice.setTextColor(Color.BLACK);
                        }
                    }
                    //设置通知
                    notice.setText(builder.toString());
                    System.err.println("==================" + builder.toString());
                }

                /**
                 * 获取家族成员生日数据
                 * */
                List<FamilyMember> birthday = noticeAndBirthday.getBirthday();
                //如果家族成员今天或者未来七天没有过生日的则设置壁纸暂无数据
                if (birthday == null) {
                    //设置暂无数据图片
                    linear.setBackgroundResource(R.mipmap.nodata);
                    //linear.setBackground(R.mipmap.nodata);
                } else {
                    //设置适配器
                    List<Map<String, Object>> data01 = new ArrayList<>();
                    for (FamilyMember f : birthday) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", f.getName());
                        //日期格式转换
//                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        String format = dateFormat.format(f.getDateOfBirth());
                        map.put("date", f.getDateOfBirth());
                        data01.add(map);
                    }
                    String[] from = {"name", "date"};
                    int[] to = {R.id.name, R.id.date};
                    SimpleAdapter adapter = new SimpleAdapter(HomeFragment.this.getActivity(), data01, R.layout.home_listview, from, to);
                    home_listview.setAdapter(adapter);
                }
            }
        }
        drawerLayout.closeDrawer(navigationView);
    }

    @Override
    public void requestFail(String data) {
        customDialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 获取数据
     */
    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(HOME_DATA);
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
}
