package com.liushengpei.controller;

import com.liushengpei.pojo.Examine;
import com.liushengpei.service.IExamineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.domain.FamilyVO;
import util.resultutil.Result;

import java.util.List;

@RestController
@RequestMapping(value = "/examine")
public class ExamineController {

    @Autowired
    private IExamineService examineService;

    /**
     * 查询所有待审核记录
     */
    @PostMapping(value = "/queryAllExamine")
    public Result<List<Examine>> queryAllExamine() {
        List<Examine> examines = examineService.queryAllExamine();
        return Result.success(examines);
    }

    /**
     * 审核的详细信息
     *
     * @param peopleFamilyId 家族成员id
     * @param type           审核类型（0：添加出生成员，1：添加家庭成员，2：删除家庭成员）
     */
    @PostMapping(value = "/queryExamineDetailed")
    public Result<Object> queryExamineDetailed(@RequestParam(value = "peopleFamilyId", defaultValue = "", required = false) String peopleFamilyId,
                                               @RequestParam(value = "type", defaultValue = "", required = false) Integer type) {
        if (peopleFamilyId == null || peopleFamilyId.equals("")) {
            return Result.fail("家族成员id不能为空");
        }
        if (type == null) {
            return Result.fail("审核类型不能为空");
        }
        Object familyVO = examineService.queryExamine(peopleFamilyId, type);
        return Result.success(familyVO);
    }

    /**
     * 审核通过或驳回
     *
     * @param id          审核id
     * @param type        审核类型
     * @param status      审核状态
     * @param examineUser 审核人（登录人）
     */
    @PostMapping(value = "/examineAdoptAndReject")
    public Result<String> examineAdoptAndReject(@RequestParam(value = "id", defaultValue = "", required = false) String id,
                                                @RequestParam(value = "type", defaultValue = "", required = false) Integer type,
                                                @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
                                                @RequestParam(value = "examineUser", defaultValue = "", required = false) String examineUser) {
        if (id == null || id.equals("")) {
            return Result.fail("审核id不能为空");
        }
        if (type == null) {
            return Result.fail("审核类型不能为空");
        }
        if (status == null) {
            return Result.fail("审核状态不能为空");
        }
        //审核人为登录人
        if (examineUser == null || examineUser.equals("")) {
            return Result.fail("审核人不能为空");
        }
        String message = examineService.adoptAndReject(id, type, status, examineUser);
        return Result.success(message);
    }

    /**
     * 户主查询提交的审核记录
     *
     * @param loginName 登录人姓名
     */
    @PostMapping(value = "/houseExamineHistory")
    public Result<List<Examine>> houseExamineHistory(@RequestParam(value = "loginName", defaultValue = "", required = false) String loginName) {
        if (loginName == null || loginName.equals("")) {
            return Result.fail("登录人不能为空");
        }
        List<Examine> examines = examineService.houseExamineHistory(loginName);
        return Result.success(examines);
    }
}
