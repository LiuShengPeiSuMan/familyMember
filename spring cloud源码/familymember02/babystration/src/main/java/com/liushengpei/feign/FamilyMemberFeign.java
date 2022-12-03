package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 家族成员
 */
@FeignClient(value = "family-member")
public interface FamilyMemberFeign {

    /**
     * 查询id
     *
     * @param name 家族成员姓名
     */
    @PostMapping(value = "/foreign/queryId")
    String queryId(@RequestParam(value = "name", defaultValue = "", required = false) String name);
}
