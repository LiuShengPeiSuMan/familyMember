package com.liushengpei.controller;

import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 对外提供接口
 */
@RestController
@RequestMapping(value = "/foreign")
public class ForeignController {

    @Autowired
    private IForeignService foreignService;

    /**
     * 修改家庭总人口数
     *
     * @param params
     */
    @PostMapping(value = "/addFamilyNumber")
    public String addFamilyNumber(@RequestBody Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "参数不能为空";
        }
        Integer num = foreignService.addFamilyPeopleNum(params);
        if (num == 0) {
            return "添加失败";
        }
        return "添加成功";
    }

    /**
     * 减少家庭人口数
     *
     * @param params
     */
    @PostMapping(value = "/reduceFamilyNum")
    public String reduceFamilyNum(@RequestBody Map<String, Object> params) {
        if (params.isEmpty()) {
            return "见家庭成员数量参数不能为空";
        }
        String msg = foreignService.reduceFamilyNum(params);
        return msg;
    }

    /**
     * 根据家族成员id查询户主id
     *
     * @param familyMemberId 家族成员id
     */
    @PostMapping(value = "/queryHouseId")
    public String queryHouseId(@RequestParam(value = "familyMemberId", defaultValue = "", required = false) String familyMemberId) {
        if (familyMemberId == null || familyMemberId.equals("")) {
            return "家族成员id不能为空";
        }
        String houseId = foreignService.queryHouseId(familyMemberId);
        return houseId;
    }

    /**
     * 根据名称查询户主id
     */
    @PostMapping(value = "/houseId")
    public String houseId(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
        if (name == null || name.equals("")) {
            return "登录人姓名不能为空";
        }
        String houseId = foreignService.queryHouseIdByName(name);
        return houseId;
    }
}
