package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.UserLogin;

/**
 * 登录返回值接收
 */
public class UserLoginReturnData {
    private Integer code;
    private String msg;
    private UserLogin data;

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

    public UserLogin getData() {
        return data;
    }

    public void setData(UserLogin data) {
        this.data = data;
    }
}
