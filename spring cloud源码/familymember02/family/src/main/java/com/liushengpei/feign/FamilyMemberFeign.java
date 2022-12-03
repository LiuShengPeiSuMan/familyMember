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
     * 查询家庭成员详细信息
     */
    @PostMapping(value = "/foreign/queryInformation")
    FamilyMember queryInformation(@RequestParam(value = "id", defaultValue = "", required = false) String id);

    /**
     * 修改家庭成员信息
     */
    @PostMapping(value = "/foreign/updateFamilyMember")
    String updateFamilyMember(@RequestBody Map<String, Object> params);
}
