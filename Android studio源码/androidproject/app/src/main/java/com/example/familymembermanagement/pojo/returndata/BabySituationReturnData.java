package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.BabySituation;

/**
 * 接收新生儿数据
 */
public class BabySituationReturnData {

    private Integer code;
    private String msg;
    private BabySituation data;

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

    public BabySituation getData() {
        return data;
    }

    public void setData(BabySituation data) {
        this.data = data;
    }
}
