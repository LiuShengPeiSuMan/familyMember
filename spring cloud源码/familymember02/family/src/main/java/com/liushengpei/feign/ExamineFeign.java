package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import util.domain.Examine;

@FeignClient(value = "examine-service")
public interface ExamineFeign {

    /**
     * 添加审核记录
     */
    @PostMapping(value = "/foreign/addExamine")
    Integer addExamine(@RequestBody Examine examine);

    /**
     * 判断删除家族成员有没有重复删除同一个人
     */
    @PostMapping(value = "/foreign/countPeopleExamine")
    Integer countPeopleExamine(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId);
}
