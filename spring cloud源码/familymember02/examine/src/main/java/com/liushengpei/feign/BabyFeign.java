package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import util.domain.BabySituation;

/**
 * 出生成员
 */
@FeignClient(value = "baby-service")
public interface BabyFeign {

    /**
     * 查询出生成员信息
     */
    @PostMapping(value = "/foreign/queryBaby")
    BabySituation queryBaby(@RequestParam(value = "id", defaultValue = "", required = false) String id);

    /**
     * 添加出生成员
     */
    @PostMapping(value = "/foreign/addBabySitatus")
    String addBabySitatus(@RequestBody BabySituation babySituation);
}
