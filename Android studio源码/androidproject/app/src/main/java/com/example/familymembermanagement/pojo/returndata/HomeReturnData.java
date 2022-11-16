package com.example.familymembermanagement.pojo.returndata;

/**
 * 首页数据接收
 * */
public class HomeReturnData {

    private Integer code;
    private String msg;
    private NoticeAndBirthday data;

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

    public NoticeAndBirthday getData() {
        return data;
    }

    public void setData(NoticeAndBirthday data) {
        this.data = data;
    }
}
