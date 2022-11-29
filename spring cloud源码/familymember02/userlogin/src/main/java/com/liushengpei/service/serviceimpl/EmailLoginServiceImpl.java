package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.UserLoginDao;
import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IEmailLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import util.sendemail.EmailSendUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static util.constant.ConstantToolUtil.*;

/**
 * 电子邮箱登录
 */
@Service
public class EmailLoginServiceImpl implements IEmailLoginService {

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private UserLoginDao loginDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取验证码
     */
    @Override
    public String getCode(String email) {
        //判断邮箱格式是否正确
        if (!email.matches(EMAIL_CHECK)) {
            return "邮箱格式不正确";
        }
        UserLogin login = loginDao.userAsEmail(email);
        //判断有没有空
        if (login == null) {
            return "此邮箱不能登录";
        }
        //随机成功验证码
        Random random = new Random();
        int code = random.nextInt(7513) + 1311;
        System.err.println(code);
        //发送邮件
        String msg = EmailSendUtil.pureTextEmail(mailSender, email, "登录验证码", String.valueOf(code));
        //数据存入到缓存
        if (msg != null && msg.equals("发送成功")) {
            redisTemplate.opsForValue().set(USER_LOGIN + email, code, EMAIL_CODE, TimeUnit.MINUTES);
        }
        System.err.println(msg);
        return msg;
    }

    /**
     * 电子邮箱登录
     */
    @Override
    public UserLogin emailLogin(String email, String code) {
        //查询缓存验证码
        Integer emailCode = (Integer) redisTemplate.opsForValue().get(USER_LOGIN + email);
        if (emailCode == null || String.valueOf(emailCode).equals("")) {
            return null;
        }
        if (!String.valueOf(emailCode).equals(code)) {
            return null;
        }
        //查询数据
        UserLogin login = loginDao.userAsEmail(email);
        if (login == null) {
            return null;
        }
        return login;
    }
}
