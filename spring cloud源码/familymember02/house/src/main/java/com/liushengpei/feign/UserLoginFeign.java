package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import util.domain.UserLogin;

@FeignClient(value = "userlogin")
public interface UserLoginFeign {

    /**
     * 添加登录权限
     *
     * @param login 登录参数
     */
    @PostMapping(value = "/foreign/addLogin")
    Integer addLogin(@RequestBody UserLogin login);

    /**
     * 查询登录邮箱个数
     */
    @PostMapping(value = "/foreign/emailCount")
    Integer emailCount(@RequestParam(value = "email", defaultValue = "", required = false) String email);
}
