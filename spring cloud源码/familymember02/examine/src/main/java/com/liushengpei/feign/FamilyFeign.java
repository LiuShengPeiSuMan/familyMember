package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import util.domain.FamilyBriefIntroduction;
import util.domain.PeopleHouse;

@FeignClient(value = "family-service")
public interface FamilyFeign {

    /**
     * 添加家庭成员简介
     */
    @PostMapping(value = "/family/addFamily")
    String addFamily(@RequestBody FamilyBriefIntroduction introduction);

    /**
     * 添加家庭成员与户主关系
     */
    @PostMapping(value = "/family/addPeopleHouse")
    String addPeopleHouse(@RequestBody PeopleHouse house);
}
