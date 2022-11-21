package com.liushengpei.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushengpei.dao.UserLoginDao;
import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IUserLoginService;
import com.liushengpei.util.EmailSendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.liushengpei.util.ConstantToolUtil.*;

@Service
public class UserLoginServiceImpl implements IUserLoginService {

    @Autowired
    private UserLoginDao loginDao;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    /*
     * 基于缓存用户登录实现
     * */
    @Override
    public UserLogin cacheLogin(String account, String password) throws JsonProcessingException {

        //查询缓存
        String user = (String) redisTemplate.opsForValue().get(USER_LOGIN + account);
        //反序列化
        if (user == null) {
            //缓存没有查询数据库
            UserLogin userlogin = loginDao.userlogin(account, password);
            //数据库没有
            if (userlogin == null) {
                //用户信息存到缓存
                redisTemplate.opsForValue().set(USER_LOGIN + account, null, USER_LOGIN_TIME, TimeUnit.MINUTES);
                return null;
            }
            //手动序列化
            String uservalue = mapper.writeValueAsString(userlogin);
            //用户信息存到缓存
            redisTemplate.opsForValue().set(USER_LOGIN + account, uservalue, USER_LOGIN_TIME, TimeUnit.MINUTES);
            return userlogin;
        }
        //json转对象
        UserLogin userLogin = JSONObject.parseObject(user, UserLogin.class);
        if (!userLogin.getPassword().equals(password)) {
            return null;
        }
        return userLogin;
    }

    /*
     * 基于session的登录
     * */
    @Override
    public UserLogin sessionLogin(HttpSession session, String account, String password) {
        //查询用户信息
        UserLogin userlogin = loginDao.userlogin(account, password);
        if (userlogin == null) {
            return null;
        }
        //将用户信息添加到session
        session.setAttribute(SESSION_USER, userlogin.getAccount());
        return userlogin;
    }

    /**
     * 添加成员登录信息
     */
    @Override
    public String addUserLogin(UserLogin userLogin) {
        //判断有没有添加过此成员
        String account = userLogin.getAccount();
        if (account != null && !account.equals("")) {
            //查询成员数量
            Integer number = loginDao.selectMember(userLogin.getAccount());
            if (number == 0) {
                //添加用户
                Integer integer = loginDao.addUser(userLogin);
                if (integer > 0) {
                    return "添加成功";
                }
            }
        }
        return "添加成员登录信息失败";
    }

    /**
     * 邮箱发送验证码
     */
    @Override
    public String emailSendCode(String loginEmail) {
        //判断此邮箱是否存在
        UserLogin userLogin = loginDao.selectEmailUser(loginEmail);
        if (userLogin == null) {
            return "此邮箱号不存在！";
        }
        //随机成功验证码
        Random random = new Random();
        int code = random.nextInt(7513) + 1311;
        System.err.println(code);
        //发送邮件
        String msg = EmailSendUtil.pureTextEmail(mailSender, loginEmail, "登录验证码", String.valueOf(code));
        //验证码存入到缓存
        if (msg != null && msg.equals("发送成功")) {
            redisTemplate.opsForValue().set(USER_EMAILLOGIN + loginEmail, code, 10L, TimeUnit.MINUTES);
        }
        System.err.println(msg);
        return msg;
    }

    /**
     * 邮箱登录
     */
    @Override
    public UserLogin emailLogin(String email, String code) {
        //缓存查询验证码
        Integer c = (Integer) redisTemplate.opsForValue().get(USER_EMAILLOGIN + email);
        if (c == null || String.valueOf(c).equals("")) {
            return null;
        }
        if (!String.valueOf(c).equals(code)) {
            return null;
        }
        //查询登录成员
        UserLogin userLogin = loginDao.selectEmailUser(email);
        if (userLogin == null) {
            return null;
        }
        return userLogin;
    }

    /**
     * 查询有没有族长登录
     */
    @Override
    public String loginZz() {
        Integer integer = loginDao.queryZz();
        if (integer == 0) {
            return "0";
        }
        return "1";
    }

    /**
     * 初始化族长权限
     */
    @Override
    public String initZuZhang(String name, String account, String pwd, String email) {
        Integer zz = loginDao.queryZz();
        if (zz > 0) {
            return "已有族长角色，请登录！";
        }
        UserLogin login = new UserLogin();
        login.setId(UUID.randomUUID().toString().substring(0, 32));
        login.setAccount(account);
        login.setPassword(pwd);
        login.setLoginEmail(email);
        login.setNickname(name);
        //族长角色
        login.setRole(1);
        login.setCreateUser(name);
        login.setCreateTime(new Date());
        login.setUpdateUser(name);
        login.setUpdateTime(new Date());
        Integer integer = loginDao.addUser(login);
        if (integer == 0) {
            return "初始化族长失败";
        }
        return "初始化族长成功";
    }

}
