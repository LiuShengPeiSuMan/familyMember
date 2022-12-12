package com.liushengpei.dao;

import com.liushengpei.pojo.Examine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExamineDao {

    /**
     * 添加审核记录
     */
    Integer addExamine(Examine examine);

    /**
     * 查询所有待审核记录
     */
    List<Examine> queryAllExamine();

    /**
     * 查询具体信息
     */
    Examine examine(String id);

    /**
     * 修改审核状态
     */
    Integer updateExamine(Map<String, Object> params);

    /**
     * 查询户主id
     */
    String queryFamilyMemberId(String id);

    /**
     * 查询有没有重复
     */
    Integer countExamine(String familyPeopleId);

    /**
     * 查询本年度提交的审核数据
     */
    List<Examine> examineYear(String houseId);
}
