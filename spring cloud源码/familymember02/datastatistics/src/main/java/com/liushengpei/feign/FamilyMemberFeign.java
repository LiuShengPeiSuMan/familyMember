package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@FeignClient(value = "family-member")
public interface FamilyMemberFeign {

    /**
     * 查询男女比例
     */
    @PostMapping(value = "/foreign/manAndWomanNum")
    List<Map<String, Object>> manAndWomanNum();

    /**
     * 查询所有人年龄
     */
    @PostMapping(value = "/foreign/queryAllAge")
    List<Integer> queryAllAge();
}
