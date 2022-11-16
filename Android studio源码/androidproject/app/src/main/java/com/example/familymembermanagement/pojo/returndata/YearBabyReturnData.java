package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.BabySituation;

import java.util.List;

/**
 * 查询本年度出生成员
 */
public class YearBabyReturnData {
    private Integer code;
    private String msg;
    private List<BabySituation> data;

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

    public List<BabySituation> getData() {
        return data;
    }

    public void setData(List<BabySituation> data) {
        this.data = data;
    }
}
