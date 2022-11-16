package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.Jurisd;

import java.util.List;

public class JurisdReturnData {

    private Integer code;
    private String msg;
    private List<Jurisd> data;

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

    public List<Jurisd> getData() {
        return data;
    }

    public void setData(List<Jurisd> data) {
        this.data = data;
    }
}
