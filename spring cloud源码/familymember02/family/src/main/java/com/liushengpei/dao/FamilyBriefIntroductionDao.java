package com.liushengpei.dao;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 家庭成员简介
 */
@Mapper
public interface FamilyBriefIntroductionDao {

    /**
     * 添加家庭成员简介
     */
    Integer addFamily(FamilyBriefIntroduction briefIntroduction);

    /**
     * 查询家族成员根据名称
     */
    Integer queryFamilyByName(String name);

    /**
     * 修改家庭成员基本信息
     */
    Integer updateFamily(Map<String, Object> params);

    /**
     * 根据登录人姓名查询户主id
     */
    String queryHouseIdByName(String loginName);

    /**
     * 查询所属户主下的所有家庭成员
     */
    List<FamilyBriefIntroduction> familyList(String houseId);

    /**
     * 根据家族成员id查询户主id
     */
    String houseId(String familyPeopleId);

    /**
     * 删除家庭成员简介
     */
    Integer delIntroduction(Map<String, Object> params);

    /**
     * 更新年龄
     */
    Integer updateFamilyAge(Map<String, Object> params);
}
