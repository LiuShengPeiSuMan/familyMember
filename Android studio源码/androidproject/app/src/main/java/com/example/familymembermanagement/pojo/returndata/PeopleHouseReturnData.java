package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.PeopleHouse;

import java.util.List;

/**
 * 接收家族成员与户主关系数据
 */
public class PeopleHouseReturnData {

    private Integer code;
    private String msg;
    private List<PeopleHouse> data;

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

    public List<PeopleHouse> getData() {
        return data;
    }

    public void setData(List<PeopleHouse> data) {
        this.data = data;
    }
}
