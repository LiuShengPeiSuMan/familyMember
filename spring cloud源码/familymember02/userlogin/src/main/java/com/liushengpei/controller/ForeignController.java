package com.liushengpei.controller;

import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.resultutil.Result;

import java.util.List;

/**
 * 对外提供接口
 */
@RestController
@RequestMapping(value = "foreign")
public class ForeignController {

    @Autowired
    private IForeignService foreignService;

    /**
     * 查询登录用户
     */
    @PostMapping(value = "/queryLoginList")
    public List<UserLogin> queryLoginList() {
        List<UserLogin> list = foreignService.queryLoginList();
        return list;
    }

    /**
     * 添加登录权限
     */
    @PostMapping(value = "/addLogin")
    public Integer addLogin(@RequestBody UserLogin login) {
        if (login == null) {
            return 0;
        }
        Integer num = foreignService.addLogin(login);
        return num;
    }

    /**
     * 修改登录密码
     */
    @PostMapping(value = "/updatePwd")
    public Integer updatePwd(@RequestParam(value = "password", defaultValue = "", required = false) String password,
                             @RequestParam(value = "id", defaultValue = "", required = false) String id,
                             @RequestParam(value = "updateUser", defaultValue = "", required = false) String updateUser) {
        if (id == null || id.equals("")) {
            return 0;
        }
        if (password == null || password.equals("")) {
            return 0;
        }
        if (updateUser == null || updateUser.equals("")) {
            return 0;
        }
        Integer num = foreignService.updatePassword(password, id, updateUser);
        return num;
    }

    /**
     * 解除用户登录权限
     */
    @PostMapping(value = "/relieve")
    public Integer relieve(@RequestParam(value = "id", defaultValue = "", required = false) String id,
                           @RequestParam(value = "updateUser", defaultValue = "", required = false) String updateUser) {
        if (id == null || id.equals("")) {
            return 0;
        }
        if (updateUser == null || updateUser.equals("")) {
            return 0;
        }
        Integer num = foreignService.relievePwd(id, updateUser);
        return num;
    }

    /**
     * 查询登录邮箱个数
     */
    @PostMapping(value = "/emailCount")
    public Integer emailCount(@RequestParam(value = "email", defaultValue = "", required = false) String email) {
        if (email == null || email.equals("")) {
            return 0;
        }
        Integer count = foreignService.emailCount(email);
        return count;
    }
}
