package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.HouseSituation;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.service.IHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.domain.FamilyVO;
import util.resultutil.Result;

import java.util.List;

/**
 * 户主管理
 */
@RestController
@RequestMapping(value = "/house")
public class HouseController {

    @Autowired
    private IHouseService service;

    /**
     * 添加户主
     */
    @PostMapping(value = "/addHouse")
    public Result<String> addHouse(@RequestBody FamilyVO familyVO) {
        if (familyVO == null) {
            return Result.fail("参数不能为空");
        }

        String s = service.addHouse(familyVO);
        return Result.success(s);
    }

    /**
     * 查询全部户主
     */
    @PostMapping(value = "/queryHouse")
    public Result<List<HouseSituation>> queryHouse() {
        //查询全部户主
        List<HouseSituation> houseSituations = service.queryHouses();
        return Result.success(houseSituations);
    }

    /**
     * 查询户主所有家庭成员
     */
    @PostMapping(value = "/queryFamilyAll")
    public Result<List<PeopleHouse>> queryFamilyAll(@RequestParam(value = "houseId", defaultValue = "", required = false) String houseId) {
        if (houseId == null || houseId.equals("")) {
            return Result.fail("户主id不能为空");
        }
        List<PeopleHouse> peopleHouses = service.queryFamilyAll(houseId);
        return Result.success(peopleHouses);
    }

    /**
     * 查询家庭成员详细信息
     */
    @PostMapping(value = "/queryFamilyMember")
    public Result<FamilyMember> queryFamilyMember(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId) {
        if (familyPeopleId == null || familyPeopleId.equals("")) {
            return Result.fail("家族成员id不能为空");
        }
        FamilyMember member = service.queryFamilyMember(familyPeopleId);
        return Result.success(member);

    }
}
