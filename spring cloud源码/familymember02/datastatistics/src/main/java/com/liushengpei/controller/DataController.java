package com.liushengpei.controller;

import com.liushengpei.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.resultutil.Result;

import java.util.Map;

/**
 * 统计
 */
@RestController
@RequestMapping(value = "/data")
public class DataController {

    @Autowired
    private IDataService dataService;

    /**
     * 统计男女比例，家族成员年龄状况，出生成员每个月状况
     */
    @PostMapping(value = "/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> map = dataService.statisticsData();
        return Result.success(map);
    }
}
