package com.liushengpei.dao;

import com.liushengpei.pojo.Examine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExamineDao {

    /**
     * 查询待审核数据
     */
    List<Examine> selectAll();

    /**
     * 添加审核记录
     */
    Integer addExamine(Examine examine);

    /**
     * 修改审核结果
     */
    Integer updateRsult(Map<String, Object> params);

    /**
     * 查询家族成员或者babyid
     */
    String queryBabyOrPeopleId(String id);

    /**
     * 查看审核记录
     */
    List<Examine> examinerecord(Integer examineStatus);

    /**
     * 批量删除历史记录
     */
    Integer delExamine(List<String> ids);

    /**
     * 查询此成员之前有没有提交删除申请
     */
    Integer queryNum(String id);

    /**
     * 根据名称查询提交记录
     */
    List<Examine> queryAllByName(String name);

}
