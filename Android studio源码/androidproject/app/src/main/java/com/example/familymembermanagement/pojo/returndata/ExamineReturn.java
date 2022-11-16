package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.Examine;

import java.util.List;

/**
 * 接收所有审核记录
 */
public class ExamineReturn {

    private Integer code;
    private String msg;
    private List<Examine> data;

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

    public List<Examine> getData() {
        return data;
    }

    public void setData(List<Examine> data) {
        this.data = data;
    }
}
