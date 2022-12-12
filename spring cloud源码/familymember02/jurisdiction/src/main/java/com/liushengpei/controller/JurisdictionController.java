package com.liushengpei.controller;

import com.liushengpei.service.IJurisdictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.domain.FamilyMember;
import util.domain.UserLogin;
import util.resultutil.Result;

import java.util.List;

/**
 * 权限管理
 */
@RestController
@RequestMapping(value = "/jurisdiction")
public class JurisdictionController {

    @Autowired
    private IJurisdictionService jurisdictionService;

    /**
     * 查询所有用户登录权限
     */
    @PostMapping(value = "/loginAll")
    public Result<List<UserLogin>> loginAll() {
        List<UserLogin> userLogins = jurisdictionService.queryLoginAll();
        return Result.success(userLogins);
    }

    /**
     * 查询登录人的详细信息
     *
     * @param id 登录人id
     */
    @PostMapping(value = "/queryUserLogin")
    public Result<UserLogin> queryUserLogin(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return Result.fail("登录id不能为空");
        }
        UserLogin login = jurisdictionService.queryUserLogin(id);
        return Result.success(login);
    }

    /**
     * 查询未有登录权限的用户
     */
    @PostMapping(value = "/unLogin")
    public Result<List<FamilyMember>> unLogin() {
        List<FamilyMember> familyMembers = jurisdictionService.unUserLogin();
        return Result.success(familyMembers);
    }

    /**
     * 添加权限
     *
     * @param name       姓名(添加的人的姓名)
     * @param loginEmail 登录邮箱
     * @param loginName  登录姓名（登录人的姓名）
     */
    @PostMapping(value = "/addJurisdiction")
    public Result<String> addJurisdiction(@RequestParam(value = "name", defaultValue = "", required = false) String name,
                                          @RequestParam(value = "loginEmail", defaultValue = "", required = false) String loginEmail,
                                          @RequestParam(value = "loginName", defaultValue = "", required = false) String loginName) {
        if (name == null || name.equals("")) {
            return Result.fail("姓名不能为空");
        }
        if (loginName == null || loginName.equals("")) {
            return Result.fail("登录人不能为空");
        }
        String msg = jurisdictionService.addJurisdiction(name, loginEmail, loginName);
        return Result.success(msg);
    }

    /**
     * 重置登录密码
     *
     * @param id        用户登录id
     * @param loginName 登录人
     * @param account   登录账号
     */
    @PostMapping(value = "/resetPassword")
    public Result<String> resetPassword(@RequestParam(value = "id", defaultValue = "", required = false) String id,
                                        @RequestParam(value = "loginName", defaultValue = "", required = false) String loginName,
                                        @RequestParam(value = "account", defaultValue = "", required = false) String account) {
        if (id == null || id.equals("")) {
            return Result.fail("id不能为空");
        }
        if (loginName == null || loginName.equals("")) {
            return Result.fail("登录人不能为空");
        }
        String msg = jurisdictionService.resetPwd(id, loginName, account);
        return Result.success(msg);
    }

    /**
     * 解除登录权限
     *
     * @param id        用户登录id
     * @param loginName 登录人
     * @param account   登录账号
     */
    @PostMapping(value = "/relievePassword")
    public Result<String> relievePassword(@RequestParam(value = "id", defaultValue = "", required = false) String id,
                                          @RequestParam(value = "loginName", defaultValue = "", required = false) String loginName,
                                          @RequestParam(value = "account", defaultValue = "", required = false) String account) {
        if (id == null || id.equals("")) {
            return Result.fail("id不能为空");
        }
        if (loginName == null || loginName.equals("")) {
            return Result.fail("登录人不能为空");
        }
        String msg = jurisdictionService.relievePwd(id, loginName, account);
        return Result.success(msg);

    }

}
