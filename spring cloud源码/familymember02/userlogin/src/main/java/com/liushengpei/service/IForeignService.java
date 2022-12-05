package com.liushengpei.service;

import com.liushengpei.pojo.UserLogin;

import java.util.Date;
import java.util.List;

public interface IForeignService {

    /**
     * 查询登录用户
     */
    List<UserLogin> queryLoginList();

    /**
     * 添加登录权限
     */
    Integer addLogin(UserLogin login);

    /**
     * 修改登录密码
     */
    Integer updatePassword(String password, String id, String updateUser);

    /**
     * 解除用户登录权限
     */
    Integer relievePwd(String id, String updateUser);

    /**
     * 查询登录邮箱个数
     */
    Integer emailCount(String email);
}
