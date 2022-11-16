package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.Jurisdiction;

/**
 * 查询每项详细信息
 */
public class JurisdictionReturnData02 {
    private Integer code;
    private String msg;
    private Jurisdiction data;

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

    public Jurisdiction getData() {
        return data;
    }

    public void setData(Jurisdiction data) {
        this.data = data;
    }
}
