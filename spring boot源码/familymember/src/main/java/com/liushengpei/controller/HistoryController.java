package com.liushengpei.controller;

import com.liushengpei.pojo.Examine;
import com.liushengpei.service.IHistoryService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 户主提交审核记录
 */
@RestController
@RequestMapping(value = "/history")
public class HistoryController {

    @Autowired
    private IHistoryService historyService;

    /**
     * 查询提交的记录
     */
    @PostMapping(value = "/queryHistory")
    public Result<List<Examine>> queryHistory(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
        if (name == null || name.equals("")) {
            return Result.fail("参数不能为空");
        }
        List<Examine> examines = historyService.examineHistory(name);
        return Result.success(examines);
    }
}
