package com.liushengpei.controller;

import com.liushengpei.pojo.BabySituation;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 对外提供接口
 */
@RestController
@RequestMapping(value = "/foreign")
public class ForeignController {

    @Autowired
    private IForeignService foreignService;

    /**
     * 查询出生成员信息
     */
    @PostMapping(value = "/queryBaby")
    public BabySituation queryBaby(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return null;
        }
        BabySituation babySituation = foreignService.queryBabySituation(id);
        return babySituation;
    }

    /**
     * 添加出生成员
     */
    @PostMapping(value = "/addBabySitatus")
    public String addBabySitatus(@RequestBody BabySituation babySituation) {
        if (babySituation == null) {
            return "添加出生成员不能为空";
        }
        String msg = foreignService.addBaby(babySituation);
        return msg;
    }

    /**
     * 查询本年度出生成员每月人数
     */
    @PostMapping(value = "/yearData")
    public List<Date> yearData() {
        List<Date> babyDate = foreignService.yearData();
        return babyDate;
    }
}
