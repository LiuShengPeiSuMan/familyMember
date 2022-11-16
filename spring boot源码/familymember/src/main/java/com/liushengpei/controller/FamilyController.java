package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;
import com.liushengpei.service.IFamilyService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 家庭成员
 */
@RestController
@RequestMapping(value = "/family")
public class FamilyController {

    @Autowired
    private IFamilyService familyService;

    /**
     * 添加家庭成员
     */
    @PostMapping(value = "/addFamily")
    public Result<String> addFamily(@RequestBody FamilyMemberUtilVO familyMemberUtilVO) {
        if (familyMemberUtilVO == null) {
            return Result.fail("参数不能为空");
        }
        String data = familyService.addFamily(familyMemberUtilVO);
        return Result.success(data);
    }

    /**
     * 删除家庭成员
     */
    @PostMapping(value = "/deleteFamily")
    public Result<String> deleteFamily(@RequestParam(value = "id", defaultValue = "", required = false) String id,
                                       @RequestParam(value = "reason", defaultValue = "", required = false) String reason) {

        if (id == null || id.equals("")) {
            return Result.fail("id不能为空");
        }
        if (reason == null || reason.equals("")) {
            return Result.fail("删除原因不能为空");
        }
        String data = familyService.deleteFamily(id, reason);
        return Result.success(data);
    }

    //查询家庭成员信息
    @PostMapping(value = "/queryFamilyInformation")
    public Result<List<FamilyBriefIntroduction>> queryFamilyInformation(@RequestParam(value = "huZhuId", defaultValue = "", required = false) String huZhuId,
                                                                        @RequestParam(value = "name", defaultValue = "", required = false) String name) {

        if (huZhuId == null || huZhuId.equals("")) {
            return Result.fail("户主id不能为空");
        }
        List<FamilyBriefIntroduction> familyBriefIntroductions = familyService.queryAllFamily(huZhuId, name);
        return Result.success(familyBriefIntroductions);
    }

    /**
     * 修改家庭成员信息
     */
    @PostMapping(value = "/updateFamily")
    public Result<String> updateFamily(@RequestBody FamilyMemberUtilVO memberUtilVO) {
        if (memberUtilVO == null) {
            return Result.fail("参数不能为空");
        }
        String data = familyService.updateFamily(memberUtilVO);
        return Result.success(data);
    }

    /**
     * 查询该成员所属户主id
     */
    @PostMapping(value = "/queryHouseId")
    public Result<String> queryHouseId(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
        if (name != null || name.equals("")) {
            Result.fail("姓名不能为空");
        }
        String houseId = familyService.queryHouseId(name);
        return Result.success(houseId);
    }
}
