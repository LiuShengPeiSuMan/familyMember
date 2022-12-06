package com.liushengpei.controller;

import com.liushengpei.pojo.Notice;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 对外提供接口
 */
@RestController
@RequestMapping(value = "/foreign")
public class ForeignController {

    @Autowired
    private IForeignService foreignService;

    /**
     * 查询当天所有数据
     */
    @PostMapping(value = "/noticeList")
    public List<Notice> noticeList() {
        List<Notice> list = foreignService.noticeList();
        return list;
    }
}
