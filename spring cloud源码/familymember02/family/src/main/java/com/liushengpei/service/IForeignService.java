package com.liushengpei.service;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;

import java.util.List;
import java.util.Map;

public interface IForeignService {

    /**
     * 添加家庭成语
     */
    String addFamily(FamilyBriefIntroduction introduction);

    /**
     * 添加家族成员与户主关系
     */
    String addPeopleHouse(PeopleHouse house);

    /**
     * 查询所有户主家庭成员
     */
    List<PeopleHouse> queryFamilyAll(String houseId);

    /**
     * 删除家庭成员简介
     */
    String delIntroduction(Map<String, Object> params);

    /**
     * 删除家族成员与户主关系
     */
    String delPeopleHouse(Map<String, Object> params);

    /**
     * 查询户主id
     */
    String queryHouseId(String id);

    /**
     * 更新家庭成员简介年龄
     */
    Integer updateAge(String id);
}
