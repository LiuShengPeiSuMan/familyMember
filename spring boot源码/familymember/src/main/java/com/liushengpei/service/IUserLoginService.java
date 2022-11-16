package com.liushengpei.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liushengpei.pojo.EmailSendParam;
import com.liushengpei.pojo.UserLogin;
import com.liushengpei.util.EmailSendUtil;

import javax.servlet.http.HttpSession;

/*
 * 用户登录
 * */
public interface IUserLoginService {

    /*
     * 基于缓存用户登录
     * */
    UserLogin cacheLogin(String account, String password) throws JsonProcessingException;

    /*
     * 基于session登录
     * */
    UserLogin sessionLogin(HttpSession session, String account, String password);

    /*
     * 添加用户登录信息
     * */
    String addUserLogin(UserLogin userLogin);

    /*
     * 邮箱发送验证码
     * */
    String emailSendCode(String loginEmail);

    /**
     * 邮箱登录
     */
    UserLogin emailLogin(String email, String code);

    /**
     * 查看有没有族长登录
     */
    String loginZz();

    /**
     * 初始化族长权限
     */
    String initZuZhang(String name,String account, String pwd, String email);
}
