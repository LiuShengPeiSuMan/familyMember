package com.liushengpei.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IUserLoginService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

import static com.liushengpei.util.ConstantToolUtil.EMAIL_CHECK;

@RestController
@RequestMapping("/userlogin")
public class LoginController {

    @Autowired
    private IUserLoginService service;

    /*
     * 基于缓存的用户登录
     * */
    @PostMapping(value = "/cacheLogin")
    public Result<UserLogin> cacheLogin(@RequestParam(value = "account", defaultValue = "", required = false) String account,
                                        @RequestParam(value = "password", defaultValue = "", required = false) String password) {

        if (account == null || account.equals("")) {
            return Result.fail("账号不能为空");
        }
        if (password == null || password.equals("")) {
            return Result.fail("密码不能为空");
        }
        UserLogin user = null;
        try {
            user = service.cacheLogin(account, password);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (user == null) {
            return Result.fail("账号或密码错误");
        }
        return Result.success(user);
    }

    /*
     * 基于session的登录
     * */
    @PostMapping(value = "/sessionLogin")
    public Result<UserLogin> sessionLogin(HttpSession session, @RequestParam(value = "account", defaultValue = "", required = false) String account,
                                          @RequestParam(value = "password", defaultValue = "", required = false) String password) {

        if (account == null || account.equals("")) {
            return Result.fail("成员账号不能为空");
        }
        if (password == null || password.equals("")) {
            return Result.fail("成员密码不能为空");
        }
        UserLogin userLogin = service.sessionLogin(session, account, password);
        return Result.success(userLogin);
    }

    /**
     * 邮箱发送验证码
     */
    @PostMapping(value = "/emailCodeSend")
    public Result<String> emailCodeSend(@RequestParam(value = "email", defaultValue = "", required = false) String email) {
        if (email == null || email.equals("")) {
            return Result.fail("登录邮箱不能为空");
        }
        if (!email.matches(EMAIL_CHECK)) {
            return Result.fail("登录邮箱格式有误");
        }
        String msg = service.emailSendCode(email);
        return Result.success(msg);
    }

    /*
     * 电子邮箱登录
     * */
    @PostMapping(value = "/emailLogin")
    public Result<UserLogin> emailLogin(String email, String code) {
        if (email == null || email.equals("")) {
            return Result.fail("邮箱不能为空");
        }
        if (code == null || code.equals("")) {
            return Result.fail("验证码不能为空");
        }
        UserLogin userLogin = service.emailLogin(email, code);
        if (userLogin == null) {
            return Result.fail("邮箱验证失败，请重新验证");
        }
        return Result.success(userLogin);
    }


    /*
     * 添加用户
     * */
    @PostMapping(value = "/addUser")
    public Result<String> addUser(@RequestBody(required = false) UserLogin userLogin) {
        if (userLogin == null) {
            return Result.fail("添加用户信息不能为空");
        }
        userLogin.setId(UUID.randomUUID().toString().substring(0, 32));
        userLogin.setCreateTime(new Date());
        userLogin.setUpdateTime(new Date());
        String success = service.addUserLogin(userLogin);
        return Result.success(success);
    }

    /**
     * 查询有没有族长登录
     */
    @PostMapping(value = "/loginZz")
    public Result<String> loginZz() {
        String s = service.loginZz();
        return Result.success(s);
    }

    /**
     * 初始化族长权限
     */
    @PostMapping(value = "/addInitZz")
    public Result<String> addInitZz(@RequestParam(value = "name", defaultValue = "", required = false) String name,
                                    @RequestParam(value = "account", defaultValue = "", required = false) String account,
                                    @RequestParam(value = "pwd", defaultValue = "", required = false) String pwd,
                                    @RequestParam(value = "email", defaultValue = "", required = false) String email) {
        if (name == null || name.equals("")) {
            return Result.fail("族长姓名不能为空");
        }
        if (account == null || account.equals("")) {
            return Result.fail("账号不能为空");
        }
        if (pwd == null || pwd.equals("")) {
            return Result.fail("密码不能为空");
        }
        if (email == null || email.equals("")) {
            return Result.fail("再次确认密码不能为空");
        }
        String s = service.initZuZhang(name, account, pwd, email);
        return Result.success(s);
    }
}
