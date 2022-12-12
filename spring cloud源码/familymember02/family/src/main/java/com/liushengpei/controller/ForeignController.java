package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 对外提供接口
 */
@RestController
@RequestMapping(value = "foreign")
public class ForeignController {

    @Autowired
    private IForeignService foreignService;

    /**
     * 添加家庭成员
     *
     * @param introduction 添加家庭成员简介基本信息
     */
    @PostMapping(value = "/addFamily")
    public String addFamily(@RequestBody FamilyBriefIntroduction introduction) {
        if (introduction == null) {
            return "添加成员简介不能为空";
        }
        String s = foreignService.addFamily(introduction);
        return s;
    }

    /**
     * 添加家庭成员与户主关系
     *
     * @param house
     */
    @PostMapping(value = "/addPeopleHouse")
    public String addPeopleHouse(@RequestBody PeopleHouse house) {
        if (house == null) {
            return "参数不能为空";
        }
        String s = foreignService.addPeopleHouse(house);
        return s;
    }

    /**
     * 查询户主所有家庭成员
     *
     * @param houseId 户主id
     */
    @PostMapping(value = "/queryAllFamily")
    public List<PeopleHouse> queryAllFamily(@RequestParam(value = "houseId", defaultValue = "", required = false) String houseId) {
        if (houseId == null || houseId.equals("")) {
            return null;
        }
        List<PeopleHouse> peopleHouses = foreignService.queryFamilyAll(houseId);
        return peopleHouses;
    }

    /**
     * 删除家庭成员简介
     *
     * @param params
     */
    @PostMapping(value = "/delIntroduction")
    public String delIntroduction(@RequestBody Map<String, Object> params) {
        if (params.isEmpty()) {
            return "删除家庭成员简介参数不能为空";
        }
        String msg = foreignService.delIntroduction(params);
        return msg;
    }

    /**
     * 删除家族成员与户主关系
     *
     * @param params
     */
    @PostMapping(value = "/delPeopleHouse")
    public String delPeopleHouse(@RequestBody Map<String, Object> params) {
        if (params.isEmpty()) {
            return "删除家族成员与户主关系参数不能为空";
        }

        String msg = foreignService.delPeopleHouse(params);
        return msg;
    }

    /**
     * 查询户主id
     *
     * @param familyPeopleId 家族成员id
     */
    @PostMapping(value = "/houseId")
    public String houseId(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId) {
        if (familyPeopleId == null || familyPeopleId.equals("")) {
            return "家族成员id不能为空";
        }
        String houseId = foreignService.queryHouseId(familyPeopleId);
        return houseId;
    }

    /**
     * 更新家族成员简介年龄
     *
     * @param familyPeopleId 家族成员id
     */
    @PostMapping(value = "/updateFamilyAge")
    public Integer updateFamilyAge(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId) {
        if (familyPeopleId == null || familyPeopleId.equals("")) {
            return 0;
        }
        Integer num = foreignService.updateAge(familyPeopleId);
        return num;
    }
}
