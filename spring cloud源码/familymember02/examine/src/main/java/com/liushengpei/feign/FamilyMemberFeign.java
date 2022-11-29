package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import util.domain.FamilyMember;

@FeignClient(value = "family-member")
public interface FamilyMemberFeign {

    /**
     * 添加家族成员
     */
    @PostMapping(value = "/familymember/addFamilyMember")
    String addFamilyMember(@RequestBody FamilyMember member);
}
