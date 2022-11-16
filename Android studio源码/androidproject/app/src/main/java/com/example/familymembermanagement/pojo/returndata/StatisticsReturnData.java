package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.StatisticsData;

/**
 * 接收统计图返回的数据
 */
public class StatisticsReturnData {

    private Integer code;
    private String msg;
    private StatisticsData data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public StatisticsData getData() {
        return data;
    }

    public void setData(StatisticsData data) {
        this.data = data;
    }
}
