package com.liushengpei.service.impl;

import com.liushengpei.feign.BabyFeign;
import com.liushengpei.feign.FamilyMemberFeign;
import com.liushengpei.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 统计
 */
@Service
public class DataServiceImpl implements IDataService {

    @Autowired
    private BabyFeign babyFeign;
    @Autowired
    private FamilyMemberFeign memberFeign;

    /**
     * 数据统计
     */
    @Override
    public Map<String, Object> statisticsData() {
        Map<String, Object> map = new HashMap<>();
        //男女比例
        List<Map<String, Object>> maps = memberFeign.manAndWomanNum();
        map.put("proportion", maps);
        //年龄段数量统计
        List<Integer> queryAllAge = memberFeign.queryAllAge();
        //存储年龄段数据
        Map<String, Integer> ageGroup = new HashMap<>();
        if (!queryAllAge.isEmpty()) {
            int child = 0;
            int juvenile = 0;
            int youth = 0;
            int mittle = 0;
            int old = 0;
            for (Integer i : queryAllAge) {
                //年龄转换
                Integer age = i;
                if (age >= 0 && age <= 5) {
                    //幼儿
                    ageGroup.put("child", child += 1);
                } else {
                    ageGroup.put("child", child);
                }
                if (age > 5 && age <= 20) {
                    //少年
                    ageGroup.put("juvenile", juvenile += 1);
                } else {
                    ageGroup.put("juvenile", juvenile);
                }
                if (age > 20 && age <= 40) {
                    //青年
                    ageGroup.put("youth", youth += 1);
                } else {
                    ageGroup.put("youth", youth);
                }
                if (age > 40 && age <= 50) {
                    //中年
                    ageGroup.put("mittle", mittle += 1);
                } else {
                    ageGroup.put("mittle", mittle);
                }
                if (age > 50) {
                    //老年
                    ageGroup.put("old", old += 1);
                } else {
                    ageGroup.put("old", old);
                }
            }
        }
        map.put("ageGroup", ageGroup);
        //出生成员年度统计
        List<Date> dates = babyFeign.yearData();
        Map<String, Integer> month = new HashMap<>();
        if (!dates.isEmpty()) {
            //一月
            int jan = 0;
            //二月
            int feb = 0;
            //三月
            int mer = 0;
            //四月
            int apr = 0;
            //五月
            int may = 0;
            //六月
            int jun = 0;
            //七月
            int jul = 0;
            //八月
            int aug = 0;
            //九月
            int sept = 0;
            //十月
            int oct = 0;
            //十一月
            int nov = 0;
            //十二月
            int dec = 0;
            for (Date d : dates) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = dateFormat.format(d);
                String[] split = format.split("-");
                String m = split[1];
                if (m.equals("01")) {
                    month.put("jan", jan += 1);
                } else {
                    month.put("jan", jan);
                }
                if (m.equals("02")) {
                    month.put("feb", feb += 1);
                } else {
                    month.put("feb", feb);
                }
                if (m.equals("03")) {
                    month.put("mer", mer += 1);
                } else {
                    month.put("mer", mer);
                }
                if (m.equals("04")) {
                    month.put("apr", apr += 1);
                } else {
                    month.put("apr", apr);
                }
                if (m.equals("05")) {
                    month.put("may", may += 1);
                } else {
                    month.put("may", may);
                }
                if (m.equals("06")) {
                    month.put("jun", jun += 1);
                } else {
                    month.put("jun", jun);
                }
                if (m.equals("07")) {
                    month.put("jul", jul += 1);
                } else {
                    month.put("jul", jul);
                }
                if (m.equals("08")) {
                    month.put("aug", aug += 1);
                } else {
                    month.put("aug", aug);
                }
                if (m.equals("09")) {
                    month.put("sept", sept += 1);
                } else {
                    month.put("sept", sept);
                }
                if (m.equals("10")) {
                    month.put("oct", oct += 1);
                } else {
                    month.put("oct", oct);
                }
                if (m.equals("11")) {
                    month.put("nov", nov += 1);
                } else {
                    month.put("nov", nov);
                }
                if (m.equals("12")) {
                    month.put("dec", dec += 1);
                } else {
                    month.put("dec", dec);
                }
            }
            map.put("month", month);
        }
        return map;
    }
}
