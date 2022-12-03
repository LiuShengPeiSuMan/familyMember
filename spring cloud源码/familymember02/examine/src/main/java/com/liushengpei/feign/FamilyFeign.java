package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import util.domain.FamilyBriefIntroduction;
import util.domain.PeopleHouse;

import java.util.Map;

@FeignClient(value = "family-service")
public interface FamilyFeign {

    /**
     * 添加家庭成员简介
     */
    @PostMapping(value = "/foreign/addFamily")
    String addFamily(@RequestBody FamilyBriefIntroduction introduction);

    /**
     * 添加家庭成员与户主关系
     */
    @PostMapping(value = "/foreign/addPeopleHouse")
    String addPeopleHouse(@RequestBody PeopleHouse house);

    /**
     * 删除家庭成员简介
     */
    @PostMapping(value = "/foreign/delIntroduction")
    String delIntroduction(@RequestBody Map<String, Object> params);

    /**
     * 删除家族成员与户主关系
     */
    @PostMapping(value = "/foreign/delPeopleHouse")
    String delPeopleHouse(@RequestBody Map<String, Object> params);

    /**
     * 查询户主id
     */
    @PostMapping(value = "/foreign/houseId")
    String houseId(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId);
}
