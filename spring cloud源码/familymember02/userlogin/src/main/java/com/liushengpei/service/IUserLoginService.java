package com.liushengpei.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liushengpei.pojo.UserLogin;

public interface IUserLoginService {

    /**
     * 查询数据库有没有族长登录数据
     */
    Integer zZCount();

    /**
     * 初始化族长登录权限
     */
    String initZzLogin(String name, String account, String password, String email);

    /**
     * 成员登录
     */
    UserLogin login(String account, String password) throws JsonProcessingException;
}
