package com.liushengpei.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushengpei.dao.UserLoginDao;
import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IUserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static util.constant.ConstantToolUtil.*;

/**
 * 用户登录
 */
@Service
public class UserLoginServiceImpl implements IUserLoginService {

    @Autowired
    private UserLoginDao loginDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 查询有没有族长数据
     */
    @Override
    public Integer zZCount() {
        Integer num = loginDao.zZNumber();
        return num;
    }

    /**
     * 初始化族长登录权限
     */
    @Override
    public String initZzLogin(String name, String account, String password, String email) {
        if (account.length() < 6) {
            return "账号长度不能少于六位数";
        }
        if (password.length() < 8) {
            return "登录密码不能少于八位数";
        }
        //校验邮箱格式
        if (!email.matches(EMAIL_CHECK)) {
            return "邮箱格式不正确";
        }
        UserLogin login = new UserLogin();
        login.setId(UUID.randomUUID().toString().substring(0, 32));
        login.setAccount(account);
        login.setPassword(password);
        login.setLoginEmail(email);
        login.setNickname(name);
        //族长权限
        login.setRole(1);
        login.setCreateTime(new Date());
        login.setCreateUser(name);
        login.setUpdateUser(name);
        login.setUpdateTime(new Date());
        login.setDelFlag(0);
        Integer integer = loginDao.addZzLogin(login);
        if (integer > 0) {
            return "添加成功";
        }
        return "添加失败";
    }

    /**
     * 成员登录
     */
    @Override
    public UserLogin login(String account, String password) throws JsonProcessingException {
        //查缓存
        String user = (String) redisTemplate.opsForValue().get(USER_LOGIN + account);
        //缓存没有查数据库
        if (user == null) {
            //数据库查出的数据存到缓存
            Map<String, Object> params = new HashMap<>();
            params.put("account", account);
            params.put("password", password);
            //返回结果
            UserLogin login = loginDao.login(params);
            //数据库没有
            if (login == null) {
                //用户信息存到缓存
                redisTemplate.opsForValue().set(USER_LOGIN + account, null, USER_LOGIN_TIME, TimeUnit.MINUTES);
                return null;
            }
            //手动序列化
            String uservalue = mapper.writeValueAsString(login);
            //用户信息存到缓存
            redisTemplate.opsForValue().set(USER_LOGIN + account, uservalue, USER_LOGIN_TIME, TimeUnit.MINUTES);
            return login;
        }
        UserLogin login = JSONObject.parseObject(user, UserLogin.class);
        if (!login.getPassword().equals(password)){
            return null;
        }
        return login;
    }
}
