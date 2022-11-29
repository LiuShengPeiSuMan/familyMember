package com.liushengpei.service;

import com.liushengpei.pojo.UserLogin;

public interface IEmailLoginService {

    /**
     * 获取验证码
     */
    String getCode(String email);

    /**
     * 邮箱登录
     */
    UserLogin emailLogin(String email, String code);
}
