package com.liushengpei.service;

import com.liushengpei.pojo.Notice;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通知
 * */
public interface INoticeService {

    /**
     * 添加通知
     * */
    String addNotice(Notice notice);

    /**
     * 查询全部通知
     * */
    List<Notice> conditionAllNotice(Map<String,Object> params);
}
