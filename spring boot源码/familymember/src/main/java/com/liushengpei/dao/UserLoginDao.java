package com.liushengpei.dao;

import com.liushengpei.pojo.UserLogin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserLoginDao {

    /*
     * 用户登录查询
     * */
    UserLogin userlogin(String account, String password);

    /*
     * 添加用户登录信息
     * */
    Integer addUser(UserLogin userLogin);

    /**
     * 查询成员条数
     */
    Integer selectMember(String account);

    /**
     * 根据邮箱查询用户
     */
    UserLogin selectEmailUser(String email);

    /**
     * 查询所有登录用户
     */
    List<UserLogin> queryUserLogin(String name);

    /**
     * 重置用户密码
     */
    Integer resetPassword(Map<String, Object> params);

    /**
     * 查看此成员是否已有权限
     */
    Integer isLogin(String nickname);

    /**
     * 解除成员登录权限
     */
    Integer relieveUserLogin(Map<String, Object> params);

    /**
     * 查询登录人详细信息
     */
    UserLogin query(String id);

    /**
     * 查询有没有族长登录权限
     */
    Integer queryZz();
}
