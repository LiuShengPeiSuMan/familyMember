package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import util.domain.FamilyMember;

import java.util.Map;

@FeignClient(value = "family-member")
public interface FamilyMemberFeign {

    /**
     * 添加家族成员
     */
    @PostMapping(value = "/foreign/addFamilyMember")
    String addFamilyMember(@RequestBody FamilyMember member);

    /**
     * 查询家族成员详细信息
     */
    @PostMapping(value = "/foreign/queryInformation")
    FamilyMember queryInformation(@RequestParam(value = "id", defaultValue = "", required = false) String id);

    /**
     * 删除家族成员
     */
    @PostMapping(value = "/foreign/delFamilyMember")
    String delFamilyMember(@RequestBody Map<String, Object> params);
}
