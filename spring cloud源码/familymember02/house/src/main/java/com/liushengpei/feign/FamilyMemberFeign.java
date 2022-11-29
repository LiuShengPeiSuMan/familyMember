package com.liushengpei.feign;

import com.liushengpei.pojo.FamilyMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "family-member")
public interface FamilyMemberFeign {

    /**
     * 添加家庭成员
     */
    @PostMapping(value = "/familymember/addFamilyMember")
    String addFamilyMember(@RequestBody FamilyMember member);

    /**
     * 查询id
     */
    @PostMapping(value = "/familymember/queryId")
    String queryId(@RequestParam(value = "name", defaultValue = "", required = false) String name);

    /**
     * 查询家族成员详细信息
     */
    @PostMapping(value = "/familymember/queryInformation")
    FamilyMember queryInformation(@RequestParam(value = "id", defaultValue = "", required = false) String id);
}
