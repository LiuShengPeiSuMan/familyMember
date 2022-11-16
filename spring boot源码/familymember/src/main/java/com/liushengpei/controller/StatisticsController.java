package com.liushengpei.controller;

import com.liushengpei.service.IStatisticsService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 统计
 */
@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {

    @Autowired
    private IStatisticsService statisticsService;

    @PostMapping(value = "/proportion")
    public Result<Map<String, Object>> proportion() {
        Map<String, Object> stringIntegerMap = statisticsService.statisticsData();
        return Result.success(stringIntegerMap);
    }
}
