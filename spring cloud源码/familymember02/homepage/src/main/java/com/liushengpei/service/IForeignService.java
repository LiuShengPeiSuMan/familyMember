package com.liushengpei.service;

import com.liushengpei.pojo.Notice;

import java.util.List;

/**
 * 对外提供接口
 */
public interface IForeignService {

    /**
     * 查询当天所有通知
     */
    List<Notice> noticeList();
}
