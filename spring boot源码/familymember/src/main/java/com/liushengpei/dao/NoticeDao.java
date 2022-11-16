package com.liushengpei.dao;

import com.liushengpei.pojo.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通知
 */
@Mapper
public interface NoticeDao {

    /**
     * 查询当天通知
     */
    List<Notice> nowDayNotice();

    /**
     * 发布通知
     */
    Integer addNotice(Notice notice);

    /**
     * 查询具体通知
     */
    List<Notice> conditionNotice(Map<String,Object> params);


}
