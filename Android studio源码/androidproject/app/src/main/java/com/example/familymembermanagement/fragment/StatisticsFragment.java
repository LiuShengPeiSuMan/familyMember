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
 * ?????????
 */
public class StatisticsFragment extends Fragment implements UnifiedRuleFragment {

    //???????????????
    private LineChart line_statistical;
    //?????????
    private BarChart columnar_statistical;
    //?????????
    private PieChart cake_statistical;
    //?????????????????????
    private BabyMonth month;//???????????????
    //???????????????
    private AgeGroup ageGroup;
    //???????????????
    private List<SexNumber> proportion;

    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private Gson gson = new Gson();
    //?????????okhttp
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
        dialog = new CustomDialog(getActivity(), "????????????...");
        dialog.show();
        apiCall(null);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void requestSuccess(String data) {
        //????????????
        StatisticsReturnData statisticsReturnData = gson.fromJson(data, StatisticsReturnData.class);
        StatisticsData data1 = statisticsReturnData.getData();
        //???????????????
        month = data1.getMonth();
        //???????????????
        ageGroup = data1.getAgeGroup();
        //???????????????
        proportion = data1.getProportion();

        //????????????
        lineChartData(line_statistical);
        //????????????
        lineChartStyle(line_statistical);
        //??????????????????
        initBarChart();
        //??????????????????
        initPieChart(cake_statistical);
        setPieChartData(cake_statistical);
        dialog.dismiss();
        drawerLayout.closeDrawer(nav);
    }

    @Override
    public void requestFail(String data) {
        dialog.dismiss();
        ToastUtil.toast(getActivity(), "?????????????????????");
    }

    /**
     * ?????????????????????
     */
    @Override
    public void apiCall(Map<String, Object> params) {
        Message message = new Message();
        //??????Request??????
        Request.Builder builder = new Request.Builder();
        builder.url(STATISTICS);
        //?????????????????????
        FormBody formBody = new FormBody.Builder().build();
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

    //????????????????????????
    private void lineChartData(LineChart line_statistical) {
        //????????????
        List<Entry> entries = new ArrayList<>();
        //?????????????????????
        Entry entryJan = new Entry(1, month.getJan());
        entries.add(entryJan);
        //?????????????????????
        Entry entryFeb = new Entry(2, month.getFeb());
        entries.add(entryFeb);
        //?????????????????????
        Entry entryMer = new Entry(3, month.getMer());
        entries.add(entryMer);
        //?????????????????????
        Entry entryApr = new Entry(4, month.getApr());
        entries.add(entryApr);
        //?????????????????????
        Entry entryMay = new Entry(5, month.getMay());
        entries.add(entryMay);
        //?????????????????????
        Entry entryJun = new Entry(6, month.getJun());
        entries.add(entryJun);
        //?????????????????????
        Entry entryJul = new Entry(7, month.getJul());
        entries.add(entryJul);
        //?????????????????????
        Entry entryAug = new Entry(8, month.getAug());
        entries.add(entryAug);
        //?????????????????????
        Entry entrySept = new Entry(9, month.getSept());
        entries.add(entrySept);
        //?????????????????????
        Entry entryOct = new Entry(10, month.getOct());
        entries.add(entryOct);
        //????????????????????????
        Entry entryNov = new Entry(11, month.getNov());
        entries.add(entryNov);
        //????????????????????????
        Entry entryDec = new Entry(12, month.getDec());
        entries.add(entryDec);
        //?????????????????????
        LineDataSet dataSet = new LineDataSet(entries, "????????????????????????");
        //????????????
        dataSet.setColor(Color.RED);//??????????????????
        //??????????????????
        dataSet.setCircleColor(Color.parseColor("#7d7d7d"));
        //??????????????????
        dataSet.setLineWidth(1f);
        //??????????????????
        LineData lineData = new LineData(dataSet);
        //??????????????????
        lineData.setDrawValues(true);
        //???????????????????????????
        line_statistical.setData(lineData);
        //????????????
        line_statistical.invalidate();
    }

    //????????????????????????
    private void lineChartStyle(LineChart line_statistical) {
        //????????????Y???
        YAxis axisRight = line_statistical.getAxisRight();
        //??????
        axisRight.setEnabled(false);
        //????????????Y???
        YAxis axisLeft = line_statistical.getAxisLeft();
        //??????
        axisLeft.setEnabled(true);
        //???Y??????0??????
        axisLeft.setAxisMinimum(0f);
        //??????X???
        XAxis xAxis = line_statistical.getXAxis();
        //??????????????????
        xAxis.setTextColor(Color.parseColor("#333333"));
        //??????lable??????
        xAxis.setLabelCount(11);
        //??????????????????
        xAxis.setTextSize(11f);
        //??????X??????1??????
        xAxis.setAxisMinimum(1f);
        //????????????????????????
        xAxis.setDrawGridLines(false);
        //x?????????
        xAxis.setDrawAxisLine(true);
        //??????label???
        xAxis.setDrawLabels(true);
        //??????X?????????,??????
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //????????????
        Legend legend = line_statistical.getLegend();
        //????????????
        legend.setForm(Legend.LegendForm.LINE);
        //??????
        legend.setTextColor(Color.BLUE);
        //??????????????????
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//????????????
        //???????????????
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //????????????
        legend.setYOffset(20f);
        //X????????????????????????
        Description description = new Description();
        description.setEnabled(true);
        description.setText("???");
        //??????????????????????????????
        line_statistical.setDescription(description);

    }

    /**
     * ??????????????????
     */
    private void initBarChart() {
        //???????????????
        columnar_statistical.getDescription().setEnabled(false);
        //???????????????
        columnar_statistical.setExtraOffsets(20, 20, 20, 20);
        //???????????????
        setAxis();
        //????????????
        setLegend();
        //????????????
        setData();
    }

    //????????????(?????????)
    private void setData() {
        List<IBarDataSet> sets = new ArrayList<>();
        //?????????????????????
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, ageGroup.getChild()));
        barEntries.add(new BarEntry(1, ageGroup.getJuvenile()));
        barEntries.add(new BarEntry(2, ageGroup.getYouth()));
        barEntries.add(new BarEntry(3, ageGroup.getMittle()));
        barEntries.add(new BarEntry(4, ageGroup.getOld()));
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        //????????????????????????
        barDataSet.setValueTextSize(10f);
        //??????????????????
        barDataSet.setValueTextColor(Color.RED);
        //????????????
        barDataSet.setColor(Color.parseColor("#1AE61A"));
        barDataSet.setLabel("?????????");
        barDataSet.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value + "???";
            }
        });
        sets.add(barDataSet);
        BarData barData = new BarData(sets);
        barData.setBarWidth(0.3f);
        columnar_statistical.setData(barData);
    }

    //????????????
    private void setLegend() {
        //????????????
        Legend legend = columnar_statistical.getLegend();
        //??????????????????
        legend.setFormSize(8f);
        //?????????????????????
        legend.setTextSize(10f);
        //?????????????????????????????????
        legend.setDrawInside(true);
        //??????????????????
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setYOffset(10f);
        legend.setXOffset(10f);

    }

    //???????????????
    private void setAxis() {
        //??????X???
        XAxis xAxis = columnar_statistical.getXAxis();//??????x???
        //??????X???????????????????????????????????????
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //??????x?????????????????????
        xAxis.setLabelCount(5);
        //??????????????????
        xAxis.setTextSize(12f);
        String[] name = {"??????", "??????", "??????", "??????", "??????"};
        //??????X????????????????????????
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
        //???????????????X??????????????????????????????
        xAxis.setYOffset(15f);
        //??????Y??????Y?????????????????????????????????
        YAxis axisRight = columnar_statistical.getAxisRight();
        //??????Y?????????
        axisRight.setEnabled(false);
        //????????????Y???
        YAxis axisLeft = columnar_statistical.getAxisLeft();
        //?????????
        axisLeft.setAxisMinimum(0f);
        //?????????
        axisLeft.setAxisMaximum(100f);
        axisLeft.setTextSize(15f);
    }

    //????????????????????????
    private void setPieChartData(PieChart cake_statistical) {
        String[] sex = {"???", "???"};
        Integer[] num = new Integer[2];
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (proportion.size() > 1) {
            for (SexNumber s : proportion) {
                if (s.getSex().equals("???")) {
                    num[0] = s.getNum();
                } else if (s.getSex().equals("???")) {
                    num[1] = s.getNum();
                }
            }
        } else {
            for (SexNumber s : proportion) {
                if (s.getSex().equals("???")) {
                    num[0] = s.getNum();
                } else {
                    num[0] = 0;
                }
                if (s.getSex().equals("???")) {
                    num[1] = s.getNum();
                } else {
                    num[1] = 0;
                }
            }
        }

        //??????????????????????????????
        Integer i = num[0]; //???
        Integer j = num[1]; //???
        Integer count = i + j;  //??????
        entries.add(new PieEntry((float) i / count * 100, sex[0]));
        entries.add(new PieEntry((float) j / count * 100, sex[1]));
//        //??????????????????????????????
//        for (int i = 0; i < 2; i++) {
//            entries.add(new PieEntry((float) Math.random() * 100, sex[i]));
//        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        //????????????????????????
        dataSet.setSliceSpace(3f);
        //?????????????????????
        dataSet.setSelectionShift(5f);
        //????????????????????????
        ArrayList<Integer> color = new ArrayList<>();
        color.add(Color.BLUE);
        color.add(Color.RED);
        dataSet.setColors(color);
        //????????????
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(cake_statistical));
        //??????????????????
        data.setValueTextSize(11f);
        //????????????
        data.setValueTextColor(Color.WHITE);
        cake_statistical.setData(data);
        //????????????
        cake_statistical.invalidate();
    }

    //????????????????????????
    private void initPieChart(PieChart cake_statistical) {
        //??????????????????????????????
        cake_statistical.setUsePercentValues(true);
        cake_statistical.getDescription().setEnabled(false);
        //?????????????????????
        cake_statistical.setExtraOffsets(5, 5, 5, 5);
        //??????????????????(????????????????????????)
        cake_statistical.setDragDecelerationFrictionCoef(0.05f);
        //???????????????
        //cake_statistical.setCenterText("????????????");
//        cake_statistical.setCenterTextSize(30f);
//        cake_statistical.setHoleColor(Color.LTGRAY);
        //?????????????????????
        cake_statistical.setTransparentCircleColor(Color.YELLOW);
        //???????????????
        cake_statistical.setTransparentCircleAlpha(200);
        //???????????????
        cake_statistical.setDrawHoleEnabled(false);
        //?????????????????????
        cake_statistical.setHoleRadius(30f);
        cake_statistical.setTransparentCircleRadius(32f);
        //?????????????????????
        cake_statistical.setDrawCenterText(true);
        //???????????????????????????
        cake_statistical.setRotationAngle(0);
        //????????????????????????
        cake_statistical.setHighlightPerTapEnabled(true);
        //??????????????????
        cake_statistical.setRotationEnabled(true);
        //??????????????????
        cake_statistical.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //ToastUtil.toast(getActivity(), "" + h.getX());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        //???????????????
        Legend legend = cake_statistical.getLegend();
        //??????????????????
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //??????
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //??????
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //????????????
        legend.setXEntrySpace(7f);
        //lable??????????????????
        legend.setYEntrySpace(3f);
        //lable???????????????
        legend.setYOffset(10f);
        //?????????????????????????????????
        cake_statistical.setEntryLabelColor(Color.WHITE);
        //??????????????????
        cake_statistical.setEntryLabelTextSize(10f);

    }


}
