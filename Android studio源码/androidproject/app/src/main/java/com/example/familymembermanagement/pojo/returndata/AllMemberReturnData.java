package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.FamilyMember;

import java.util.List;

/**
 * 查询所有
 * */
public class AllMemberReturnData {
    private Integer code;
    private String msg;
    private List<FamilyMember> data;

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

    public List<FamilyMember> getData() {
        return data;
    }

    public void setData(List<FamilyMember> data) {
        this.data = data;
    }
}
