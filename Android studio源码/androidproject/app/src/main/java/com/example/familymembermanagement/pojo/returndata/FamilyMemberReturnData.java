package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.FamilyMember;

/**
 * 接收家族成员返回信息
 */
public class FamilyMemberReturnData {
    private Integer code;
    private String msg;
    private FamilyMember data;

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

    public FamilyMember getData() {
        return data;
    }

    public void setData(FamilyMember data) {
        this.data = data;
    }
}
