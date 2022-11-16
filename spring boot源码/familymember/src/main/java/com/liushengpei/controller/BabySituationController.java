package com.liushengpei.controller;

import com.liushengpei.pojo.BabySituation;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;
import com.liushengpei.service.IBabySituationService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 新生儿
 */
@RestController
@RequestMapping(value = "/babySituation")
public class BabySituationController {

    @Autowired
    private IBabySituationService babySituationService;

    /**
     * 添加新生儿
     */
    @PostMapping(value = "/addNewBaby")
    public Result<String> addNewBaby(@RequestBody FamilyMemberUtilVO memberUtilVO) {
        if (memberUtilVO == null) {
            return Result.fail("参数不能为空");
        }
        String s = babySituationService.addBaby(memberUtilVO);
        return Result.success(s);
    }

    /**
     * 查询本年度新生儿
     */
    @PostMapping(value = "/yearBaby")
    public Result<List<BabySituation>> yearBaby() {
        List<BabySituation> babySituations = babySituationService.yearBaby();
        return Result.success(babySituations);
    }

    /**
     * 查询出生成员详细信息
     */
    @PostMapping(value = "/babyDetailed")
    public Result<BabySituation> babyDetailed(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return Result.fail("参数不能为空");
        }
        BabySituation babySituation = babySituationService.detailedBaby(id);
        return Result.success(babySituation);
    }
}
