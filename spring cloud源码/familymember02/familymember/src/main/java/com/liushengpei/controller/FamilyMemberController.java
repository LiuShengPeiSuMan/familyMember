package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.service.IFamilyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
     * 查询所有家族成员
     */
    @PostMapping(value = "/familyMemberList")
    public Result<List<FamilyMember>> familyMemberList() {
        List<FamilyMember> familyMemberList = memberService.familyMemberList();
        return Result.success(familyMemberList);
    }

    /**
     * 查询家族成员详细信息
     *
     * @param id 家族成员id
     */
    @PostMapping(value = "/queryFamilyMember")
    public Result<FamilyMember> queryFamilyMember(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return Result.fail("家族成员id不能为空");
        }
        FamilyMember familyMember = memberService.queryFamilyMember(id);
        return Result.success(familyMember);
    }

    /**
     * 定时任务，自动更新年龄
     */
    //@Scheduled(cron = "0 40 14 * * ? ")
    @Scheduled(cron = "0 0 0 * * ? ")
    public void addAge() {
        System.err.println("定时任务已启动");
        memberService.addAge();
        System.err.println("定时任务已关闭");
    }
}
