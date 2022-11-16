package com.liushengpei.dao;

import com.liushengpei.pojo.HouseSituation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 户主
 */
@Mapper
public interface HouseSituationDao {

    /**
     * 查询户主姓名
     */
    String queryName(String id);

    /**
     * 修改家庭总人口数
     */
    Integer updateCountPeople(Map<String, Object> params);

    /**
     * 添加家庭总人口数
     */
    Integer addCountPeople(Map<String, Object> params);

    /**
     * 查询户主数量
     */
    Integer peopleCount(String id);

    /**
     * 查询全部户主
     */
    List<HouseSituation> queryAllHouse(String name);

    /**
     * 添加户主
     */
    Integer addHouse(HouseSituation situation);

    /**
     * 查询户主id
     */
    String queryHouseId(String name);
}
