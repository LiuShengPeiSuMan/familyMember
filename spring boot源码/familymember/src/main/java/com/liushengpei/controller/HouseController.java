package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.HouseSituation;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;
import com.liushengpei.service.IHouseService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 户主管理
 */
@RestController
@RequestMapping(value = "/house")
public class HouseController {

    @Autowired
    private IHouseService houseService;

    /**
     * 查询所有户主
     */
    @PostMapping(value = "/queryAllHouse")
    public Result<List<HouseSituation>> queryAllHouse(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
        List<HouseSituation> houseSituations = houseService.queryAllHouse(name);
        return Result.success(houseSituations);
    }

    /**
     * 查询户主家庭成员
     */
    @PostMapping(value = "/queryAllPeopleHouse")
    public Result<List<PeopleHouse>> queryAllPeopleHouse(@RequestParam(value = "householderId", defaultValue = "", required = false) String householderId) {

        if (householderId == null || householderId.equals("")) {
            return Result.fail("户主id不能为空");
        }
        List<PeopleHouse> peopleHouses = houseService.queryAllPeopleHouse(householderId);
        return Result.success(peopleHouses);
    }

    /**
     * 查询家族成员详细信息
     */
    @PostMapping(value = "/queryFamilyDetailed")
    public Result<FamilyMember> queryFamilyDetailed(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId) {

        if (familyPeopleId == null || familyPeopleId.equals("")) {
            return Result.fail("家族成员id不能为空");
        }
        FamilyMember familyMember = houseService.queryFamilyDetailed(familyPeopleId);
        return Result.success(familyMember);
    }

    /**
     * 添加户主（族长添加）
     */
    @RequestMapping(value = "/addHouse")
    public Result<String> addHouse(@RequestBody FamilyMemberUtilVO memberUtilVO) {
        if (memberUtilVO == null) {
            return Result.fail("参数不能为空");
        }
        String data = houseService.addHouse(memberUtilVO);
        return Result.success(data);
    }
}
