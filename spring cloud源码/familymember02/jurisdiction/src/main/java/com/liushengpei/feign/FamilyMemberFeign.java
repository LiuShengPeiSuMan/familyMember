package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import util.domain.FamilyMember;

import java.util.List;

/**
 * 家族成员
 */
@FeignClient(value = "family-member")
public interface FamilyMemberFeign {
    /**
     * 查询所有家族成员
     */
    @PostMapping(value = "/foreign/allFamilyMember")
    List<FamilyMember> allFamilyMember();
}
