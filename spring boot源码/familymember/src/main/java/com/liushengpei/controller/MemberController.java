package com.liushengpei.controller;

import com.github.pagehelper.PageInfo;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.service.IMemberService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 全部成员管理
 */
@RestController
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired
    private IMemberService memberService;

    /**
     * 查询所有家族成员
     */
    @PostMapping(value = "/allMember")
    public Result<List<FamilyMember>> allMember(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
        List<FamilyMember> familyMembers = memberService.allMember(name);
        return Result.success(familyMembers);
    }

    /**
     * 分页查询
     */
    @PostMapping(value = "/pageMember")
    public Result<PageInfo<FamilyMember>> pageMember(@RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
                                                     @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        if (pageNum == null || pageNum == 0) {
            return Result.fail("分页数量不能为空");
        }
        if (pageSize == null || pageSize == 0) {
            return Result.fail("分页条数不能为空");
        }
        PageInfo<FamilyMember> familyMemberPageInfo = memberService.pageMember(pageNum, pageSize);
        return Result.success(familyMemberPageInfo);
    }

    /**
     * 查询家族成员详细信息
     */
    @PostMapping(value = "/familyDetailed")
    public Result<FamilyMember> familyDetailed(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return Result.fail("参数不能为空");
        }
        FamilyMember detailed = memberService.detailed(id);
        return Result.success(detailed);
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
