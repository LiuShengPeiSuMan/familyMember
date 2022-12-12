package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "house-server")
public interface HouseFeign {

    /**
     * 添加户主家庭总人口数量
     */
    @PostMapping(value = "/foreign/addFamilyNumber")
    String addFamilyNumber(@RequestBody Map<String, Object> params);

    /**
     * 减少家庭人口数
     */
    @PostMapping(value = "/foreign/reduceFamilyNum")
    String reduceFamilyNum(@RequestBody Map<String, Object> params);

    /**
     * 根据名称查询户主id
     */
    @PostMapping(value = "/foreign/houseId")
    String houseId(@RequestParam(value = "name", defaultValue = "", required = false) String name);
}
