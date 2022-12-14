package com.liushengpei.util.resultutil;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

import static com.liushengpei.util.resultutil.ResultEnum.YF_0000;
import static com.liushengpei.util.resultutil.ResultEnum.YF_9999;

/*
 * 封装返回结果
 * */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 4580737268023862568L;

    //返回码
    private Integer code;

    //消息
    private String msg;

    //数据
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public Result() {
    }

    public Result(Integer code) {
        this.code = code;
    }

    //是否成功（自定义结果码为1000为成功）
    @JsonIgnore
    public boolean isSuccess() {
        return this.code == 1000;
    }


    //成功时引用

    public static <T> Result<T> success() {
        return success(YF_0000);
    }

    public static <T> Result<T> success(T data) {
        return success(YF_0000, data);
    }

    public static <T> Result<T> success(ResultEnum re) {
        return success(re, null);
    }

    public static <T> Result<T> success(ResultEnum re, T data) {
        Integer code = re.getCode();
        String msg = re.getMsg();
        return success(code, msg, data);
    }

    public static <T> Result<T> success(Integer code, String msg, T data) {
        Result<T> result = new Result<>(1000);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }


    //失败时引用
    public static <T> Result<T> fail() {
        return fail(YF_9999);
    }

    public static <T> Result<T> fail(ResultEnum re) {
        return fail(re, null);
    }

    public static <T> Result<T> fail(String msg) {
        return fail(YF_9999.getCode(), msg, null);
    }

    public static <T> Result<T> fail(T data) {
        return fail(YF_9999, data);
    }

    public static <T> Result<T> fail(ResultEnum re, T data) {
        Integer code = re.getCode();
        String msg = re.getMsg();
        return fail(code, msg, data);
    }

    public static <T> Result<T> fail(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
