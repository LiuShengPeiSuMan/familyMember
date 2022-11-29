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
}
