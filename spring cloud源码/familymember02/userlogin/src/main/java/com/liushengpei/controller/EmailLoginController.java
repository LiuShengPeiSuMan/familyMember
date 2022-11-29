package com.liushengpei.controller;

import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IEmailLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.resultutil.Result;

/**
 * 电子邮箱登录
 */
@RestController
@RequestMapping(value = "/emailLogin")
public class EmailLoginController {

    @Autowired
    private IEmailLoginService emailLoginService;

    /**
     * 获取邮箱验证码
     */
    @PostMapping(value = "/getCode")
    public Result<String> getCode(@RequestParam(value = "email", defaultValue = "", required = false) String email) {
        if (email == null || email.equals("")) {
            return Result.fail("邮箱不能为空");
        }
        String code = emailLoginService.getCode(email);
        return Result.success(code);
    }

    /**
     * 邮箱登录
     */
    @PostMapping(value = "/login")
    public Result<UserLogin> login(@RequestParam(value = "email", defaultValue = "", required = false) String email,
                                   @RequestParam(value = "code", defaultValue = "", required = false) String code) {
        if (email == null || email.equals("")) {
            return Result.fail("电子邮箱不能为空");
        }
        if (code == null || code.equals("")) {
            return Result.fail("邮箱验证码不能为空");
        }
        UserLogin login = emailLoginService.emailLogin(email, code);
        if (login == null) {
            return Result.fail("邮箱验证失败,请重新发送");
        }
        return Result.success(login);
    }
}
