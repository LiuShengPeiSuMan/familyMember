package com.liushengpei.service;

import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.Jurisdication;
import com.liushengpei.pojo.UserLogin;

import java.util.List;
import java.util.Map;

/**
 * 权限管理
 */
public interface IJurisdictionService {

    /**
     * 查询所有用户权限
     */
    List<UserLogin> queryLogin(String name);

    /**
     * 重置或修改用户权限
     */
    String resetUserLogin(String id, String loginName,String account);

    /**
     * 添加普通成员登录权限
     */
    String addPuTongLogin(Map<String, Object> params);

    /**
     * 解除成员登录权限
     */
    String relieveLogin(String id, String loginName,String account);

    /**
     * 查询登录详细信息
     */
    UserLogin login(String id);

    /**
     * 查询所有成员
     */
    List<Jurisdication> queryAll();
}
