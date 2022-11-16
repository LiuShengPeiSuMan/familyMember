package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.Jurisdiction;

import java.util.List;

/**
 * 权限管理返回值接收
 */
public class JurisdictionReturnData {

    private Integer code;
    private String msg;
    private List<Jurisdiction> data;

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

    public List<Jurisdiction> getData() {
        return data;
    }

    public void setData(List<Jurisdiction> data) {
        this.data = data;
    }
}
