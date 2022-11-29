package com.liushengpei.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IUserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.resultutil.Result;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private IUserLoginService service;

    /**
     * 查询族长数据
     */
    @PostMapping(value = "/zzcount")
    public Result<Integer> test() {
        Integer integer = service.zZCount();
        return Result.success(integer);
    }

    /**
     * 初始化族长登录权限
     */
    @PostMapping(value = "/initZzLogin")
    public Result<String> initZzLogin(@RequestParam(value = "name", defaultValue = "", required = false) String name,
                                      @RequestParam(value = "account", defaultValue = "", required = false) String account,
                                      @RequestParam(value = "password", defaultValue = "", required = false) String password,
                                      @RequestParam(value = "loginEmail", defaultValue = "", required = false) String loginEmail) {
        if (name == null || name.equals("")) {
            return Result.fail("登录人不能为空");
        }
        if (account == null || account.equals("")) {
            return Result.fail("登录账号不能为空");
        }
        if (password == null || password.equals("")) {
            return Result.fail("登录密码不能为空");
        }
        String s = service.initZzLogin(name, account, password, loginEmail);
        return Result.success(s);
    }

    /**
     * 成员登录
     */
    @PostMapping(value = "/login")
    public Result<UserLogin> login(@RequestParam(value = "account", defaultValue = "", required = false) String account,
                                   @RequestParam(value = "password", defaultValue = "", required = false) String password) {
        if (account == null || account.equals("")) {
            return Result.fail("登录账号不能为空");
        }
        if (password == null || password.equals("")) {
            return Result.fail("登录密码不能为空");
        }
        UserLogin login = null;
        try {
            login = service.login(account, password);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (login == null) {
            return Result.fail("账号或密码错误");
        }
        return Result.success(login);

    }
}
