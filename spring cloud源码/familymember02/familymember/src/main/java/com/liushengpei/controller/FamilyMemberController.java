package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.service.IFamilyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import util.resultutil.Result;

import java.util.List;

/**
 * 家族成员
 */
@RestController
@RequestMapping(value = "/familymember")
public class FamilyMemberController {

    @Autowired
    private IFamilyMemberService memberService;

    /**
     * 添加家族成员(对外提供接口)
     */
    @PostMapping(value = "/addFamilyMember")
    public String addFamilyMember(@RequestBody FamilyMember member) {
        if (member == null) {
            return "参数不能为空";
        }
        Integer integer = memberService.addFamilyMember(member);
        if (integer > 0) {
            return "添加成功";
        }
        return "添加失败";
    }

    /**
     * 查询id(对外提供接口)
     */
    @PostMapping(value = "/queryId")
    public String queryId(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
        if (name == null || name.equals("")) {
            return "参数不能为空";
        }
        String id = memberService.queryId(name);
        return id;
    }

    /**
     * 查询家族成员详细信息(对外提供接口)
     */
    @PostMapping(value = "/queryInformation")
    public FamilyMember queryInformation(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return null;
        }
        FamilyMember familyMember = memberService.queryFamilyMember(id);
        return familyMember;
    }

    /**
     * 查询所有家族成员
     */
    @PostMapping(value = "/familyMemberList")
    public Result<List<FamilyMember>> familyMemberList() {
        List<FamilyMember> familyMemberList = memberService.familyMemberList();
        return Result.success(familyMemberList);
    }

    /**
     * 查询家族成员详细信息
     */
    @PostMapping(value = "/queryFamilyMember")
    public Result<FamilyMember> queryFamilyMember(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return Result.fail("家族成员id不能为空");
        }
        FamilyMember familyMember = memberService.queryFamilyMember(id);
        return Result.success(familyMember);
    }

}
