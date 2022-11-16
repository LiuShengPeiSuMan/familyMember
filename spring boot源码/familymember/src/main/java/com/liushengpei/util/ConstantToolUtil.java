package com.liushengpei.util;

import java.util.UUID;

/**
 * 常量
 */
public class ConstantToolUtil {
    //规定缓存key
    public static final String USER_LOGIN = "user:cachelogin:";
    //邮件登录key
    public static final String USER_EMAILLOGIN = "user:emaillogin:";
    //缓存用户登录信息时长
    public static final long USER_LOGIN_TIME = 30L;
    //session登录key
    public static final String SESSION_USER = "account";
    //email格式校验
    public static final String EMAIL_CHECK = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$";
    //手机号格式校验
    public static final String PHONE_CHECK = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";

    //发件人邮箱
    public static final String EMAIL_FROM = "2726118282@qq.com";

    //族长账号前缀
    public static final String ZUZHANG = "zuzhang";
    //户主账号前缀
    public static final String HUZHU = "huzhu";
    //普通成员账号前缀
    public static final String PUTONG = "putong";
    //随机设置密码
    public static final String PASSWORD= UUID.randomUUID().toString().substring(0, 8).replace('-', '6');
}
