package com.liushengpei.dao;

import com.liushengpei.pojo.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知
 */
@Mapper
public interface NoticeDao {

    /**
     * 添加通知
     */
    Integer addNotice(Notice notice);
}
