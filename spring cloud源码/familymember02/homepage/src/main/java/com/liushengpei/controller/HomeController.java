package com.liushengpei.controller;

import com.liushengpei.service.IHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.resultutil.Result;

import java.util.Map;

/**
 * 首页数据查询
 */
@RestController
@RequestMapping(value = "home")
public class HomeController {

    @Autowired
    private IHomePageService service;

    /**
     * 查询首页数据
     */
    @PostMapping(value = "/queryHomeData")
    public Result<Map<String, Object>> queryHomeData() {
        return Result.success();
    }
}
