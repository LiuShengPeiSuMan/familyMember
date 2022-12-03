package com.liushengpei.controller;

import com.liushengpei.pojo.Examine;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 对外开放的接口
 */
@RestController
@RequestMapping(value = "/foreign")
public class ForeignController {

    @Autowired
    private IForeignService service;

    /**
     * 添加审核记录
     *
     * @param examine
     */
    @PostMapping(value = "/addExamine")
    public Integer addExamine(@RequestBody Examine examine) {
        Integer integer = service.addExamine(examine);
        return integer;
    }

    /**
     * 判断删除家族成员有没有重复删除同一个人
     *
     * @param familyPeopleId 家族成员id
     */
    @PostMapping(value = "/countPeopleExamine")
    public Integer countPeopleExamine(@RequestParam(value = "familyPeopleId", defaultValue = "", required = false) String familyPeopleId) {
        Integer integer = service.countPeopleExamine(familyPeopleId);
        return integer;
    }
}
