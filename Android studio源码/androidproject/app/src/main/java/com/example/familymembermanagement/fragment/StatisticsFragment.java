package com.example.familymembermanagement.fragment;

import static com.example.familymembermanagement.util.UrlApiUtil.QUERY_FAMILY_INFORMATION;
import static com.example.familymembermanagement.util.UrlApiUtil.STATISTICS;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.familymembermanagement.R;
import com.example.familymembermanagement.pojo.AgeGroup;
import com.example.familymembermanagement.pojo.BabyMonth;
import com.example.familymembermanagement.pojo.SexNumber;
import com.example.familymembermanagement.pojo.StatisticsData;
import com.example.familymembermanagement.pojo.returndata.StatisticsReturnData;
import com.example.familymembermanagement.standard.UnifiedRuleFragment;
import com.example.familymembermanagement.util.CustomDialog;
import com.example.familymembermanagement.util.ToastUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 统计图
 */
public class StatisticsFragment extends Fragment implements UnifiedRuleFragment {

    //折线统计图
    private LineChart line_statistical;
    //柱状图
    private BarChart columnar_statistical;
    //饼形图
    private PieChart cake_statistical;
    //统计图所需数据
    private BabyMonth month;//折线图数据
    //柱状图数据
    private AgeGroup ageGroup;
    //饼状图数据
    private List<SexNumber> proportion;

    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private Gson gson = new Gson();
    //初始化okhttp
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

    public StatisticsFragment(Toolbar toolbar, NavigationView nav, DrawerLayout drawerLayout) {
        this.toolbar = toolbar;
        this.nav = nav;
        this.drawerLayout = drawerLayout;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment, null);
        initView(view);
        initData();
        initEvent();
        return view;
    }


    @Override
    public void initView(View view) {
        line_statistical = view.findViewById(R.id.line_statistical);
        columnar_statistical = view.findViewById(R.id.columnar_statistical);
        cake_statistical = view.findViewById(R.id.cake_statistical);
    }

    @Override
    public void initData() {
        dialog = new CustomDialog(getActivity(), "正在加载...");
        dialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void requestSuccess(String data) {
        //解析数据
        StatisticsReturnData statisticsReturnData = gson.fromJson(data, StatisticsReturnData.class);
        StatisticsData data1 = statisticsReturnData.getData();
        //折线图数据
        month = data1.getMonth();
        //柱状图数据
        ageGroup = data1.getAgeGroup();
        //饼状图数据
        proportion = data1.getProportion();

        //设置数据
        lineChartData(line_statistical);
        //设置样式
        lineChartStyle(line_statistical);
        //初始化柱状图
        initBarChart();
        //初始化饼状图
        initPieChart(cake_statistical);
        setPieChartData(cake_statistical);
        dialog.dismiss();
        drawerLayout.closeDrawer(nav);
    }

    @Override
    public void requestFail(String data) {
        dialog.dismiss();
        ToastUtil.toast(getActivity(), "服务器连接失败");
    }

    /**
     * 统计图接口调用
     */
    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //构造Request对象
        Request.Builder builder = new Request.Builder();
        builder.url(STATISTICS);
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

    //初始化折线图数据
    private void lineChartData(LineChart line_statistical) {
        //添加数据
        List<Entry> entries = new ArrayList<>();
        //添加一月份数据
        Entry entryJan = new Entry(1, month.getJan());
        entries.add(entryJan);
        //添加二月分数据
        Entry entryFeb = new Entry(2, month.getFeb());
        entries.add(entryFeb);
        //添加三月份数据
        Entry entryMer = new Entry(3, month.getMer());
        entries.add(entryMer);
        //添加四月份数据
        Entry entryApr = new Entry(4, month.getApr());
        entries.add(entryApr);
        //添加五月分数据
        Entry entryMay = new Entry(5, month.getMay());
        entries.add(entryMay);
        //添加六月份数据
        Entry entryJun = new Entry(6, month.getJun());
        entries.add(entryJun);
        //添加七月份数据
        Entry entryJul = new Entry(7, month.getJul());
        entries.add(entryJul);
        //添加八月份数据
        Entry entryAug = new Entry(8, month.getAug());
        entries.add(entryAug);
        //添加九月份数据
        Entry entrySept = new Entry(9, month.getSept());
        entries.add(entrySept);
        //添加十月份数据
        Entry entryOct = new Entry(10, month.getOct());
        entries.add(entryOct);
        //添加十一月分数据
        Entry entryNov = new Entry(11, month.getNov());
        entries.add(entryNov);
        //添加十二月分数据
        Entry entryDec = new Entry(12, month.getDec());
        entries.add(entryDec);
        //定义线性数据集
        LineDataSet dataSet = new LineDataSet(entries, "出生成员年度统计");
        //设置样式
        dataSet.setColor(Color.RED);//设置折线颜色
        //设置圆点颜色
        dataSet.setCircleColor(Color.parseColor("#7d7d7d"));
        //设置线条宽度
        dataSet.setLineWidth(1f);
        //定义线性数据
        LineData lineData = new LineData(dataSet);
        //显示原点数值
        lineData.setDrawValues(true);
        //把数据添加到组件中
        line_statistical.setData(lineData);
        //更新组件
        line_statistical.invalidate();
    }

    //初始化折线图样式
    private void lineChartStyle(LineChart line_statistical) {
        //获取右侧Y轴
        YAxis axisRight = line_statistical.getAxisRight();
        //禁用
        axisRight.setEnabled(false);
        //获取左侧Y轴
        YAxis axisLeft = line_statistical.getAxisLeft();
        //显示
        axisLeft.setEnabled(true);
        //让Y轴从0开始
        axisLeft.setAxisMinimum(0f);
        //获取X轴
        XAxis xAxis = line_statistical.getXAxis();
        //设置文本颜色
        xAxis.setTextColor(Color.parseColor("#333333"));
        //设置lable数量
        xAxis.setLabelCount(11);
        //设置文本大小
        xAxis.setTextSize(11f);
        //设置X轴从1开始
        xAxis.setAxisMinimum(1f);
        //设置网格线不显示
        xAxis.setDrawGridLines(false);
        //x轴显示
        xAxis.setDrawAxisLine(true);
        //显示label值
        xAxis.setDrawLabels(true);
        //设置X轴位置,底端
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置图例
        Legend legend = line_statistical.getLegend();
        //显示形状
        legend.setForm(Legend.LegendForm.LINE);
        //颜色
        legend.setTextColor(Color.BLUE);
        //图例设置位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//顶端对齐
        //水平右对齐
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //向下偏移
        legend.setYOffset(20f);
        //X轴右下角描述信息
        Description description = new Description();
        description.setEnabled(true);
        description.setText("月");
        //描述信息添加到组件中
        line_statistical.setDescription(description);

    }

    /**
     * 初始化柱状图
     */
    private void initBarChart() {
        //不显示描述
        columnar_statistical.getDescription().setEnabled(false);
        //设置偏移量
        columnar_statistical.setExtraOffsets(20, 20, 20, 20);
        //设置坐标轴
        setAxis();
        //设置图例
        setLegend();
        //设置数据
        setData();
    }

    //设置数据(柱状图)
    private void setData() {
        List<IBarDataSet> sets = new ArrayList<>();
        //柱状图添加数据
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, ageGroup.getChild()));
        barEntries.add(new BarEntry(1, ageGroup.getJuvenile()));
        barEntries.add(new BarEntry(2, ageGroup.getYouth()));
        barEntries.add(new BarEntry(3, ageGroup.getMittle()));
        barEntries.add(new BarEntry(4, ageGroup.getOld()));
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        //柱子顶部数值大小
        barDataSet.setValueTextSize(10f);
        //数值字体颜色
        barDataSet.setValueTextColor(Color.RED);
        //柱子颜色
        barDataSet.setColor(Color.parseColor("#1AE61A"));
        barDataSet.setLabel("年龄段");
        barDataSet.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value + "人";
            }
        });
        sets.add(barDataSet);
        BarData barData = new BarData(sets);
        barData.setBarWidth(0.3f);
        columnar_statistical.setData(barData);
    }

    //设置图例
    private void setLegend() {
        //获取图例
        Legend legend = columnar_statistical.getLegend();
        //图例图形大小
        legend.setFormSize(8f);
        //图例的文字大小
        legend.setTextSize(10f);
        //设置图例在图中是否显示
        legend.setDrawInside(true);
        //设置方向垂直
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setYOffset(10f);
        legend.setXOffset(10f);

    }

    //设置坐标轴
    private void setAxis() {
        //设置X轴
        XAxis xAxis = columnar_statistical.getXAxis();//获取x轴
        //设置X轴显示在下方，默认是在上方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //设置x轴上的标签个数
        xAxis.setLabelCount(5);
        //设置文本大小
        xAxis.setTextSize(12f);
        String[] name = {"幼儿", "少年", "青年", "中年", "老年"};
        //设置X轴显示数据的格式
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if ((int) value < name.length) {
                    return name[(int) value];
                } else {
                    return "";
                }
            }
        });
        //设置标签对X轴的偏移量，垂直方向
        xAxis.setYOffset(15f);
        //设置Y轴，Y轴右两天，分别为左和右
        YAxis axisRight = columnar_statistical.getAxisRight();
        //右侧Y轴隐藏
        axisRight.setEnabled(false);
        //获取左侧Y轴
        YAxis axisLeft = columnar_statistical.getAxisLeft();
        //最小值
        axisLeft.setAxisMinimum(0f);
        //最大值
        axisLeft.setAxisMaximum(100f);
        axisLeft.setTextSize(15f);
    }

    //初始化饼状图数据
    private void setPieChartData(PieChart cake_statistical) {
        String[] sex = {"男", "女"};
        Integer[] num = new Integer[2];
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (proportion.size() > 1) {
            for (SexNumber s : proportion) {
                if (s.getSex().equals("男")) {
                    num[0] = s.getNum();
                } else if (s.getSex().equals("女")) {
                    num[1] = s.getNum();
                }
            }
        } else {
            for (SexNumber s : proportion) {
                if (s.getSex().equals("男")) {
                    num[0] = s.getNum();
                } else {
                    num[0] = 0;
                }
                if (s.getSex().equals("女")) {
                    num[1] = s.getNum();
                } else {
                    num[1] = 0;
                }
            }
        }

        //饼形图需要添加的数据
        Integer i = num[0]; //男
        Integer j = num[1]; //女
        Integer count = i + j;  //总数
        entries.add(new PieEntry((float) i / count * 100, sex[0]));
        entries.add(new PieEntry((float) j / count * 100, sex[1]));
//        //饼形图需要添加的数据
//        for (int i = 0; i < 2; i++) {
//            entries.add(new PieEntry((float) Math.random() * 100, sex[i]));
//        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        //不同块之间的间隙
        dataSet.setSliceSpace(3f);
        //选择块突出大小
        dataSet.setSelectionShift(5f);
        //每块数据添加颜色
        ArrayList<Integer> color = new ArrayList<>();
        color.add(Color.BLUE);
        color.add(Color.RED);
        dataSet.setColors(color);
        //添加数据
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(cake_statistical));
        //设置文字大小
        data.setValueTextSize(11f);
        //字体颜色
        data.setValueTextColor(Color.WHITE);
        cake_statistical.setData(data);
        //刷新数据
        cake_statistical.invalidate();
    }

    //饼形图组件初始化
    private void initPieChart(PieChart cake_statistical) {
        //数据显示百分比的形式
        cake_statistical.setUsePercentValues(true);
        cake_statistical.getDescription().setEnabled(false);
        //图形对应的位置
        cake_statistical.setExtraOffsets(5, 5, 5, 5);
        //设置摩擦系数(值越小摩擦力越大)
        cake_statistical.setDragDecelerationFrictionCoef(0.05f);
        //设置中心圆
        //cake_statistical.setCenterText("男女比例");
//        cake_statistical.setCenterTextSize(30f);
//        cake_statistical.setHoleColor(Color.LTGRAY);
        //过度圆圈的颜色
        cake_statistical.setTransparentCircleColor(Color.YELLOW);
        //过度透明度
        cake_statistical.setTransparentCircleAlpha(200);
        //关闭中心圆
        cake_statistical.setDrawHoleEnabled(false);
        //过度圆圈的半径
        cake_statistical.setHoleRadius(30f);
        cake_statistical.setTransparentCircleRadius(32f);
        //是否显示中心字
        cake_statistical.setDrawCenterText(true);
        //初始弧度，开始位置
        cake_statistical.setRotationAngle(0);
        //是否支持单击事件
        cake_statistical.setHighlightPerTapEnabled(true);
        //是否可以转动
        cake_statistical.setRotationEnabled(true);
        //添加单击事件
        cake_statistical.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //ToastUtil.toast(getActivity(), "" + h.getX());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        //图例的设置
        Legend legend = cake_statistical.getLegend();
        //设置对齐方式
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //右侧
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //垂直
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //水平间距
        legend.setXEntrySpace(7f);
        //lable之间垂直间距
        legend.setYEntrySpace(3f);
        //lable对应的位置
        legend.setYOffset(10f);
        //设置显示数据的字体颜色
        cake_statistical.setEntryLabelColor(Color.WHITE);
        //显示字体大小
        cake_statistical.setEntryLabelTextSize(10f);

    }


}
