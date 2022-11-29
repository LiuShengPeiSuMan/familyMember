package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import util.domain.Examine;

@FeignClient(value = "examine-service")
public interface ExamineFeign {

    /**
     * 添加审核记录
     */
    @PostMapping(value = "/foreign/addExamine")
    Integer addExamine(@RequestBody Examine examine);
}
