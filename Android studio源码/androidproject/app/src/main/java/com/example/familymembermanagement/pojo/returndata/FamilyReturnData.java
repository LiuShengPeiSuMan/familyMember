package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.FamilyBriefIntroduction;

import java.util.List;

/**
 * 家庭成员
 */
public class FamilyReturnData {
    private Integer code;
    private String msg;
    private List<FamilyBriefIntroduction> data;

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

    public List<FamilyBriefIntroduction> getData() {
        return data;
    }

    public void setData(List<FamilyBriefIntroduction> data) {
        this.data = data;
    }
}
