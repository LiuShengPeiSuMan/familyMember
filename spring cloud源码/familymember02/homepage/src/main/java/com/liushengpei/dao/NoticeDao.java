package com.liushengpei.dao;

import com.liushengpei.pojo.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 通知
 */
@Mapper
public interface NoticeDao {

    /**
     * 添加通知
     */
    Integer addNotice(Notice notice);

    /**
     * 查询当天所有通知
     */
    List<Notice> queryNoticeToday();
}
