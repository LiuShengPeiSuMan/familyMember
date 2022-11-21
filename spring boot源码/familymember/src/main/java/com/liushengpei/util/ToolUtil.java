package com.liushengpei.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolUtil {

    /**
     * 日期格式转换
     */
    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        return time;
    }

    /**
     * 汉字转拼音
     */
    public static String toPinYin(String chinese) {
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
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
}
