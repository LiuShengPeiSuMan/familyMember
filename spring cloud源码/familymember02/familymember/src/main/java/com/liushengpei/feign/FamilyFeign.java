package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "family-service")
public interface FamilyFeign {

    /**
     * 更新家族成员简介年龄
     *
     * @param familyPeopleId 家族成员id
     */
    @PostMapping(value = "/foreign/updateFamilyAge")
    Integer updateFamilyAge(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId);
}
