package com.liushengpei.feign;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("family-service")
public interface FamilyFeign {

    /**
     * 添加家庭成员
     */
    @PostMapping(value = "/family/addFamily")
    String addFamily(@RequestBody FamilyBriefIntroduction introduction);

    /**
     * 添加家庭成员与户主关系
     */
    @PostMapping(value = "/family/addPeopleHouse")
    String addPeopleHouse(@RequestBody PeopleHouse house);

    /**
     * 查询户主所有家庭成员
     */
    @PostMapping(value = "/family/queryAllFamily")
    List<PeopleHouse> queryAllFamily(@RequestParam(value = "houseId", defaultValue = "", required = false) String houseId);
}
