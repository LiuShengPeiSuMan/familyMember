package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.Jurisdication;
import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IJurisdictionService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/jurisdiction")
public class JurisdictionController {

    @Autowired
    private IJurisdictionService jurisdictionService;

    /**
     * 查询所有用户权限
     */
    @PostMapping(value = "/queryLogin")
    public Result<List<UserLogin>> queryLogin(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
        List<UserLogin> userLogins = jurisdictionService.queryLogin(name);
        return Result.success(userLogins);
    }

    /**
     * 重置（修改）用户权限
     */
    @PostMapping(value = "/resetUserLogin")
    public Result<String> resetUserLogin(@RequestParam(value = "id", defaultValue = "", required = false) String id,
                                         @RequestParam(value = "loginName", defaultValue = "", required = false) String loginName,
                                         @RequestParam(value = "account", defaultValue = "", required = false) String account) {
        if (id == null || id.equals("")) {
            return Result.fail("id不能为空");
        }
        if (loginName == null || loginName.equals("")) {
            return Result.fail("登录人不能为空");
        }
        if (account == null || account.equals("")) {
            return Result.fail("账号不能为空");
        }
        String data = jurisdictionService.resetUserLogin(id, loginName, account);
        return Result.success(data);
    }

    /**
     * 添加普通成员登录权限
     */
    @PostMapping(value = "/addPuTongLogin")
    public Result<String> addPuTongLogin(@RequestParam(value = "loginName", defaultValue = "", required = false) String loginName,
                                         @RequestParam(value = "puTongName", defaultValue = "", required = false) String puTongName,
                                         @RequestParam(value = "email", defaultValue = "", required = false) String email) {
        if (loginName == null || loginName.equals("")) {
            return Result.fail("登录人姓名不能为空");
        }
        if (puTongName == null || puTongName.equals("")) {
            return Result.fail("普通家族成员姓名不能为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("loginName", loginName);
        params.put("puTongName", puTongName);
        params.put("email", email);
        String data = jurisdictionService.addPuTongLogin(params);
        return Result.success(data);
    }

    /**
     * 解除成员登录权限
     */
    @PostMapping(value = "/relieveLogin")
    public Result<String> relieveLogin(@RequestParam(value = "id", defaultValue = "", required = false) String id,
                                       @RequestParam(value = "loginName", defaultValue = "", required = false) String loginName,
                                       @RequestParam(value = "account", defaultValue = "", required = false) String account) {
        if (id == null || id.equals("")) {
            return Result.fail("不能为空");
        }
        if (loginName == null || loginName.equals("")) {
            return Result.fail("登录人不能为空");
        }
        if (account == null || account.equals("")) {
            return Result.fail("账号不能为空");
        }
        String data = jurisdictionService.relieveLogin(id, loginName, account);
        return Result.success(data);
    }

    /**
     * 查询登录人详细信息
     */
    @PostMapping(value = "/userLogin")
    public Result<UserLogin> userLogin(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return Result.fail("id不能为空");
        }
        UserLogin login = jurisdictionService.login(id);
        return Result.success(login);
    }

    /**
     * 查询所有家族成员
     */
    @PostMapping(value = "/selectNameEmail")
    public Result<List<Jurisdication>> selectFamilyMember() {
        List<Jurisdication> jurisdications = jurisdictionService.queryAll();
        return Result.success(jurisdications);
    }

}
