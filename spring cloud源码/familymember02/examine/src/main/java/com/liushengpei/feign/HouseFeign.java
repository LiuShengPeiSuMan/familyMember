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
    String addFamilyNumber(@RequestBody Map<String,Object> params);
}
