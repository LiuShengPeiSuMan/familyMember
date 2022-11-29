package com.liushengpei.dao;

import com.liushengpei.pojo.UserLogin;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserLoginDao {

    /**
     * 查询数据库有没有族长数据
     */
    Integer zZNumber();

    /**
     * 添加族长登录权限
     */
    Integer addZzLogin(UserLogin login);

    /**
     * 成员登录
     */
    UserLogin login(Map<String, Object> params);

    /**
     * 根据邮箱查询登录成员
     */
    UserLogin userAsEmail(String email);
}
