package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.pojo.UpdateFamilyDetailed;
import com.liushengpei.service.IFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.domain.FamilyMember;
import util.domain.FamilyVO;
import util.resultutil.Result;

import java.util.List;

/**
 * 家庭成员管理
 */
@RestController
@RequestMapping(value = "/family")
public class FamilyController {

    @Autowired
    private IFamilyService familyService;

    /**
     * 添加家庭成员
     *
     * @param familyVO
     */
    @PostMapping(value = "/addToFamily")
    public Result<String> addToFamily(@RequestBody FamilyVO familyVO) {
        if (familyVO == null) {
            return Result.fail("添加家庭成员的参数不能为空");
        }
        String family = familyService.addToFamily(familyVO);
        return Result.success(family);
    }

    /**
     * 查询所有家庭成员
     *
     * @param loginName 登录人姓名
     */
    @PostMapping(value = "/allFamilyPeople")
    public Result<List<FamilyBriefIntroduction>> allFamilyPeople(@RequestParam(value = "loginName", defaultValue = "", required = false) String loginName) {
        if (loginName == null || loginName.equals("")) {
            return Result.fail("登录人姓名不能为空");
        }
        List<FamilyBriefIntroduction> familyBriefIntroductions = familyService.allFamily(loginName);
        return Result.success(familyBriefIntroductions);
    }

    /**
     * 查询家庭成员全部信息
     *
     * @param familyPeopleId 家族成员id
     */
    @PostMapping(value = "/queryFamily")
    public Result<FamilyMember> queryFamily(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId) {
        if (familyPeopleId == null || familyPeopleId.equals("")) {
            return Result.fail("家族成员id不能为空");
        }
        FamilyMember member = familyService.familyDetailed(familyPeopleId);
        return Result.success(member);
    }

    /**
     * 修改家庭成员信息
     *
     * @param detailed 修改家庭成员基本信息
     */
    @PostMapping(value = "/updateFamily")
    public Result<String> updateFamily(@RequestBody UpdateFamilyDetailed detailed) {
        if (detailed == null) {
            return Result.fail("参数不能为空");
        }
        String msg = familyService.updateFamily(detailed);
        return Result.success(msg);
    }

    /**
     * 删除家庭成员
     *
     * @param familyPeopleId 家族成员id
     * @param reason         删除原因
     * @param loginName      登录人
     */
    @PostMapping(value = "/deleteFamily")
    public Result<String> deleteFamily(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId,
                                       @RequestParam(value = "reason", defaultValue = "", required = false) String reason,
                                       @RequestParam(value = "loginName", defaultValue = "", required = false) String loginName) {
        if (familyPeopleId == null || familyPeopleId.equals("")) {
            return Result.fail("家族成员id不能为空");
        }
        if (reason == null || reason.equals("")) {
            return Result.fail("删除原因不能为空");
        }
        if (loginName == null || loginName.equals("")) {
            return Result.fail("登录人不能为空");
        }
        String msg = familyService.deleteFamily(familyPeopleId, loginName, reason);
        return Result.success(msg);
    }
}
