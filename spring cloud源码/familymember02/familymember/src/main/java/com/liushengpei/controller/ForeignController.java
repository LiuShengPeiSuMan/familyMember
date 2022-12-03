package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.service.IFamilyMemberService;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/foreign")
public class ForeignController {

    @Autowired
    private IForeignService foreignService;

    /**
     * 修改家庭成员信息
     *
     * @param params
     */
    @PostMapping(value = "/updateFamilyMember")
    public String updateFamilyMember(@RequestBody Map<String, Object> params) {
        if (params.isEmpty()) {
            return "修改家庭成员参数不能为空";
        }
        String msg = foreignService.updateFamilyMember(params);
        return msg;
    }

    /**
     * 添加家族成员
     *
     * @param member
     */
    @PostMapping(value = "/addFamilyMember")
    public String addFamilyMember(@RequestBody FamilyMember member) {
        if (member == null) {
            return "参数不能为空";
        }
        Integer integer = foreignService.addFamilyMember(member);
        if (integer > 0) {
            return "添加成功";
        }
        return "添加失败";
    }

    /**
     * 查询id
     *
     * @param name 家族成员姓名
     */
    @PostMapping(value = "/queryId")
    public String queryId(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
        if (name == null || name.equals("")) {
            return "参数不能为空";
        }
        String id = foreignService.queryId(name);
        return id;
    }

    /**
     * 查询家族成员详细信息
     *
     * @param id 家族成员id
     */
    @PostMapping(value = "/queryInformation")
    public FamilyMember queryInformation(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return null;
        }
        FamilyMember familyMember = foreignService.queryFamilyMember(id);
        return familyMember;
    }

    /**
     * 删除家族成员
     *
     * @param params
     */
    @PostMapping(value = "/delFamilyMember")
    public String delFamilyMember(@RequestBody Map<String, Object> params) {
        if (params.isEmpty()) {
            return "删除家族成员的参数不能为空";
        }
        String msg = foreignService.delFamilyMember(params);
        return msg;
    }
}
