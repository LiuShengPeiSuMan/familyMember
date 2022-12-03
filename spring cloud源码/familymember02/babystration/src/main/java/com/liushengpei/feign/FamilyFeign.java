package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "family-service")
public interface FamilyFeign {

    /**
     * 查询户主id
     *
     * @param familyPeopleId 家族成员id
     */
    @PostMapping(value = "/foreign/houseId")
    String houseId(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId);
}
