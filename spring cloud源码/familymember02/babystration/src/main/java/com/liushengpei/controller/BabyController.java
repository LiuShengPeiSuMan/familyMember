package com.liushengpei.controller;

import com.liushengpei.pojo.BabySituation;
import com.liushengpei.service.IBabyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.domain.FamilyVO;
import util.resultutil.Result;

import java.util.List;

/**
 * 出生成员管理
 */
@RestController
@RequestMapping(value = "baby")
public class BabyController {

    @Autowired
    private IBabyService babyService;

    /**
     * 查询本年度所有出生成员
     */
    @PostMapping(value = "/queryBabyYear")
    public Result<List<BabySituation>> queryBabyYear() {
        List<BabySituation> babySituations = babyService.queryBaby();
        return Result.success(babySituations);
    }

    /**
     * 添加出生成员
     *
     * @param familyVO 出生成员信息
     */
    @PostMapping(value = "/addBaby")
    public Result<String> addBaby(@RequestBody FamilyVO familyVO) {
        if (familyVO == null) {
            return Result.fail("添加出生成员参数不能为空");
        }
        String addBaby = babyService.addBaby(familyVO);
        return Result.success(addBaby);
    }

    /**
     * 查询出生成员详细信息
     *
     * @param id 出生成员id
     */
    @PostMapping(value = "/queryBabyDetailed")
    public Result<BabySituation> queryBabyDetailed(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return Result.fail("出生成员参数不能为空");
        }
        BabySituation babySituation = babyService.queryBabyDetailed(id);
        return Result.success(babySituation);
    }
}
