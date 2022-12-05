package com.liushengpei.service.impl;

import com.liushengpei.feign.FamilyMemberFeign;
import com.liushengpei.feign.UserLoginFeign;
import com.liushengpei.service.IJurisdictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.domain.FamilyMember;
import util.domain.UserLogin;
import util.tool.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static util.constant.ConstantToolUtil.PUTONG;

/**
 * 权限管理
 */
@Service
public class JurisdictionServiceImpl implements IJurisdictionService {

    @Autowired
    private FamilyMemberFeign memberFeign;
    @Autowired
    private UserLoginFeign loginFeign;

    /**
     * 查询用户登录权限
     */
    @Override
    public List<UserLogin> queryLoginAll() {
        List<UserLogin> userLogins = loginFeign.queryLoginList();
        return userLogins;
    }

    /**
     * 未赋予登录权限的用户
     */
    @Override
    public List<FamilyMember> unUserLogin() {
        List<FamilyMember> unLogin = new ArrayList<>();
        //查询全部用户
        List<FamilyMember> all = memberFeign.allFamilyMember();
        //查询已有登录权限的用户
        List<UserLogin> logins = loginFeign.queryLoginList();
        boolean off = true;
        for (FamilyMember f : all) {
            for (UserLogin u : logins) {
                //如果姓名相等说名已有登录权限
                if (f.getName().equals(u.getNickname())) {
                    off = false;
                    break;
                }
            }
            if (off) {
                unLogin.add(f);
            } else {
                off = true;
            }
        }
        return unLogin;
    }

    /**
     * 添加权限
     *
     * @param name       姓名
     * @param loginEmail 登录邮箱
     * @param loginName  登录人
     */
    @Override
    public String addJurisdiction(String name, String loginEmail, String loginName) {
        //判断有没有重复的登录邮箱
        Integer count = loginFeign.emailCount(loginEmail);
        if (count > 0) {
            return "此登录邮箱已存在，请重新输入或略过！";
        }
        UserLogin login = new UserLogin();
        login.setId(UUID.randomUUID().toString().substring(0, 32));
        //汉字转拼音
        String account = Util.toPinYin(name);
        login.setAccount(PUTONG + account);
        login.setPassword(UUID.randomUUID().toString().substring(0, 8));
        login.setLoginEmail(loginEmail);
        login.setNickname(name);
        //普通家族成员角色
        login.setRole(3);
        login.setCreateTime(new Date());
        login.setCreateUser(loginName);
        login.setUpdateTime(new Date());
        login.setUpdateUser(loginName);
        login.setDelFlag(0);
        Integer num = loginFeign.addLogin(login);
        if (num == 0) {
            return "添加失败";
        }
        return "权限添加成功！";
    }

    /**
     * 重置登录密码
     */
    @Override
    public String resetPwd(String id, String loginName) {
        //随机生成8位数密码
        String password = UUID.randomUUID().toString().substring(0, 8);
        Integer num = loginFeign.updatePwd(password, id, loginName);
        if (num == 0) {
            return "重置密码失败";
        }
        return "重置密码成功！";
    }

    /**
     * 解除用户登录权限
     */
    @Override
    public String relievePwd(String id, String loginName) {
        Integer relieve = loginFeign.relieve(id, loginName);
        if (relieve == 0) {
            return "解除登录权限失败";
        }
        return "解除登录权限成功";
    }

}
