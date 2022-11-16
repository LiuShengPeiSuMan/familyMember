package com.liushengpei.controller;


import com.liushengpei.pojo.Notice;
import com.liushengpei.service.INoticeService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 通知
 */
@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    /**
     * 添加通知
     */
    @PostMapping(value = "/addNotice")
    public Result<String> addNotice(@RequestBody Notice notice) {

        if (notice == null) {
            return Result.fail("添加的通知不能为空");
        }
        //添加通知
        String addNotice = noticeService.addNotice(notice);
        return Result.success(addNotice);
    }

    /**
     * 查询全部通知
     */
    @PostMapping(value = "/conditionAllNotice")
    public Result<List<Notice>> conditionAllNotice(@RequestBody Map<String, Object> params) {
//        if (params==null){
//            return Result.fail("条件不能为空");
//        }
        List<Notice> notices = noticeService.conditionAllNotice(params);
        return Result.success(notices);
    }
}
