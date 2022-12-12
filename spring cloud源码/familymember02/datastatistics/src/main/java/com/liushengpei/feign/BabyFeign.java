package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@FeignClient(value = "baby-service")
public interface BabyFeign {

    /**
     * 查询本年度出生成员每月人数
     */
    @PostMapping(value = "/foreign/yearData")
    List<Date> yearData();
}
