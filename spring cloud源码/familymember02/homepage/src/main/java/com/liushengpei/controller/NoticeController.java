package com.liushengpei.controller;

import com.liushengpei.service.IHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.resultutil.Result;

@RestController
@RequestMapping(value = "notice")
public class NoticeController {

    @Autowired
    private IHomePageService service;

    /**
     * 添加通知
     *
     * @param type 通知类型
     * @param text 通知内容
     */
    @PostMapping(value = "/addNotice")
    public Result<String> addNotice(@RequestParam(value = "type", defaultValue = "", required = false) Integer type,
                                    @RequestParam(value = "text", defaultValue = "", required = false) String text) {
        if (type == null) {
            return Result.fail("通知类型不能为空");
        }
        if (text == null || text.equals("")) {
            return Result.fail("通知内容不能为空");
        }
        String s = service.addNotice(type, text);
        return Result.success(s);
    }
}
