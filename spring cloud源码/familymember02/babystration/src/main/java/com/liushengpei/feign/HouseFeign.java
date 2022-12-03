package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 户主管理
 */
@FeignClient(value = "house-server")
public interface HouseFeign {

    /**
     * 根据家族成员id查询户主id
     *
     * @param familyMemberId 家族成员id
     */
    @PostMapping(value = "/foreign/queryHouseId")
    String queryHouseId(@RequestParam(value = "familyMemberId", defaultValue = "", required = false) String familyMemberId);
}
