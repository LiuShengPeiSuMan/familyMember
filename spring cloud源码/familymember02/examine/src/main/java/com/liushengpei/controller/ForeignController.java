package com.liushengpei.controller;

import com.liushengpei.pojo.Examine;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     */
    @PostMapping(value = "/addExamine")
    public Integer addExamine(@RequestBody Examine examine) {
        Integer integer = service.addExamine(examine);
        return integer;
    }
}
