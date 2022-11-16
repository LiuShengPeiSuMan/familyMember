package com.example.familymembermanagement.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolUtil {
    /**
     * 时间格式转换
     */
    public static String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
        return format;
    }

    /**
     * 时间格式转换无时分秒
     */
    public static String formatDateNoTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(date);
        return format;
    }

    /**
     * 出生日期字符串转日期
     */
    public static Date toDate(String date) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 学历判断
     */
    public static String education(Integer education) {
        String ed = null;
        if (education == null || education.equals("")) {
            ed = null;
        } else {
            switch (education) {
                case 0:
                    ed = "未上学";
                    break;
                case 1:
                    ed = "幼儿园";
                    break;
                case 2:
                    ed = "小学";
                    break;
                case 3:
                    ed = "初中";
                    break;
                case 4:
                    ed = "普高";
                    break;
                case 5:
                    ed = "中专";
                    break;
                case 6:
                    ed = "大学";
                    break;
                case 7:
                    ed = "大专";
                    break;
                case 8:
                    ed = "研究生";
                    break;
                case 9:
                    ed = "博士";
                    break;
                case 10:
                    ed = "硕士";
                    break;
            }
        }
        return ed;
    }

    /**
     * 学历转数字
     */
    public static int educationToInt(String education) {
        int i = 0;
        switch (education) {
            case "未上学":
                i = 0;
                break;
            case "幼儿园":
                i = 1;
                break;
            case "小学":
                i = 2;
                break;
            case "初中":
                i = 3;
                break;
            case "普高":
                i = 4;
                break;
            case "中专":
                i = 5;
                break;
            case "大学":
                i = 6;
                break;
            case "大专":
                i = 7;
                break;
            case "研究生":
                i = 8;
                break;
            case "博士":
                i = 9;
                break;
            case "硕士":
                i = 10;
                break;
        }
        return i;
    }

    /**
     * 判断是不是指定学历
     */
    public static boolean inputEd(String e) {
        boolean off = false;
        String[] education = {"未上学", "幼儿园", "小学", "初中", "中专", "普高", "大专", "大学", "研究生", "博士", "硕士"};
        for (int i = 0; i < education.length; i++) {
            if (e.equals(education[i])) {
                off = true;
                break;
            }
        }
        return off;
    }

}
