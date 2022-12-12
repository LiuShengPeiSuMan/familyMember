package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import util.domain.UserLogin;

import java.util.List;

@FeignClient(value = "userlogin")
public interface UserLoginFeign {
    /**
     * 查询登录用户
     */
    @PostMapping(value = "/foreign/queryLoginList")
    List<UserLogin> queryLoginList();

    /**
     * 添加登录权限
     *
     * @param login 登录参数
     */
    @PostMapping(value = "/foreign/addLogin")
    Integer addLogin(@RequestBody UserLogin login);

    /**
     * 修改登录密码
     */
    @PostMapping(value = "/foreign/updatePwd")
    Integer updatePwd(@RequestParam(value = "password", defaultValue = "", required = false) String password,
                      @RequestParam(value = "id", defaultValue = "", required = false) String id,
                      @RequestParam(value = "updateUser", defaultValue = "", required = false) String updateUser);

    /**
     * 解除用户登录权限
     */
    @PostMapping(value = "/foreign/relieve")
    Integer relieve(@RequestParam(value = "id", defaultValue = "", required = false) String id,
                    @RequestParam(value = "updateUser", defaultValue = "", required = false) String updateUser);

    /**
     * 查询登录邮箱个数
     */
    @PostMapping(value = "/foreign/emailCount")
    Integer emailCount(@RequestParam(value = "email", defaultValue = "", required = false) String email);

    /**
     * 查询登录用户的详细信息
     */
    @PostMapping(value = "/foreign/queryUserLogin")
    UserLogin queryUserLogin(@RequestParam(value = "id", defaultValue = "", required = false) String id);
}
