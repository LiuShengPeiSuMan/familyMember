package com.liushengpei.controller;

import com.liushengpei.pojo.Notice;
import com.liushengpei.service.IHomeService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 首页
 */
@RestController
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    private IHomeService homeService;

    /**
     * 查询当天发布的通知和生日
     */
    @PostMapping(value = "/homeData")
    public Result<Map<String, Object>> homeData() {
        Map<String, Object> map = homeService.nowDayNotice();
        return Result.success(map);
    }

}
