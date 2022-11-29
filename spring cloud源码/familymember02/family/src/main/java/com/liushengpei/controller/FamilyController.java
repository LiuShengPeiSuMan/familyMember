package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.service.IFamilyService;
import org.checkerframework.checker.units.qual.A;
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
     * 添加家庭成员（对外提供接口）
     */
    @PostMapping(value = "/addFamily")
    public String addFamily(@RequestBody FamilyBriefIntroduction introduction) {
        if (introduction == null) {
            return "添加成员简介不能为空";
        }
        String s = familyService.addFamily(introduction);
        return s;
    }

    /**
     * 添加家庭成员与户主关系（对外提供接口）
     */
    @PostMapping(value = "/addPeopleHouse")
    public String addPeopleHouse(@RequestBody PeopleHouse house) {
        if (house == null) {
            return "参数不能为空";
        }
        String s = familyService.addPeopleHouse(house);
        return s;
    }

    /**
     * 查询户主所有家庭成员（对外提供接口）
     */
    @PostMapping(value = "/queryAllFamily")
    public List<PeopleHouse> queryAllFamily(@RequestParam(value = "houseId", defaultValue = "", required = false) String houseId) {
        if (houseId == null || houseId.equals("")) {
            return null;
        }
        List<PeopleHouse> peopleHouses = familyService.queryFamilyAll(houseId);
        return peopleHouses;
    }

    /**
     * 添加家庭成员
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
     * 修改家庭成员信息
     */
    @PostMapping(value = "/updateFamily")
    public Result<String> updateFamily(@RequestBody FamilyMember member) {
        if (member == null) {
            return Result.fail("参数不能为空");
        }
        return Result.success();
    }
}
