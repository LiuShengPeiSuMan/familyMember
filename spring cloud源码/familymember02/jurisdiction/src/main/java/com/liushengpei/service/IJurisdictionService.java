package com.liushengpei.service;

import util.domain.FamilyMember;
import util.domain.UserLogin;

import java.util.List;

/**
 * 权限管理
 */
public interface IJurisdictionService {

    /**
     * 查询所有登录权限
     */
    List<UserLogin> queryLoginAll();

    /**
     * 查询登录人详细信息
     */
    UserLogin queryUserLogin(String id);

    /**
     * 查询未赋予登录权限的用户
     */
    List<FamilyMember> unUserLogin();

    /**
     * 添加登录权限
     */
    String addJurisdiction(String name, String loginEmail, String loginName);

    /**
     * 重置登录密码
     */
    String resetPwd(String id, String loginName, String account);

    /**
     * 解除用户登录权限
     */
    String relievePwd(String id, String loginName, String account);
}
