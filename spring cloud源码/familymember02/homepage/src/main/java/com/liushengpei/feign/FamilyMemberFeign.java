package com.liushengpei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import util.domain.FamilyMember;

import java.util.List;

/**
 * 家族成员管理
 */
@FeignClient(value = "family-member")
public interface FamilyMemberFeign {

    /**
     * 查询一周之内过生日的成员
     */
    @PostMapping(value = "/foreign/queryDateOfBirth")
    List<FamilyMember> queryDateOfBirth();

}
