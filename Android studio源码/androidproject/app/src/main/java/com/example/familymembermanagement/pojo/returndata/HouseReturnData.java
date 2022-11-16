package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.HouseSituation;

import java.util.List;

/**
 * 接收户主返回数据
 */
public class HouseReturnData {

    private Integer code;
    private String msg;
    private List<HouseSituation> data;

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

    public List<HouseSituation> getData() {
        return data;
    }

    public void setData(List<HouseSituation> data) {
        this.data = data;
    }
}
