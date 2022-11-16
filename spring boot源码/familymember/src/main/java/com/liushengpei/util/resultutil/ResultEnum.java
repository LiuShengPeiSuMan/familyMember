package com.liushengpei.util.resultutil;

public enum ResultEnum {
    YF_0000(1000, "success"),

    WAITING_IN_LINE(30013, "正在排队中"),
    YF_9999(1001,"fail");


    private Integer code;
    private String msg;

    ResultEnum(Integer code , String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
