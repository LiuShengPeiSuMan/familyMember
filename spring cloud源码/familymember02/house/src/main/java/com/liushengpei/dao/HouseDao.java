package com.liushengpei.dao;

import com.liushengpei.pojo.HouseSituation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HouseDao {

    /**
     * 添加户主
     */
    Integer addHouse(HouseSituation houseSituation);

    /**
     * 查询户主
     */
    Integer queryHouse(String familyPeopleId);

    /**
     * 查询全部户主
     */
    List<HouseSituation> queryHoueses();

    /**
     * 添加户主人口数量
     */
    Integer updatePeopleNum(Map<String, Object> params);
}
