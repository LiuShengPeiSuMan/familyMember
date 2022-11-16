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
    Integer addFamilyBriefIntroduction(FamilyBriefIntroduction introduction);

    /**
     * 删除家庭成员简介
     */
    Integer deleteDelFlag(String id);

    /**
     * 删除家族成员
     */
    Integer delDelFlag(String id);

    /**
     * 查询家庭成员
     */
    List<FamilyBriefIntroduction> queryAllFamily(String houseId);

    /**
     * 修改家庭成员信息
     */
    Integer updateFamily(Map<String, Object> params);

    /**
     * 查询家族成员id
     */
    String queryFamilyPeopleId(String familyPeopleId);

    /**
     * 查询本人
     */
    FamilyBriefIntroduction queryOneself(String name);

    /**
     * 删除家庭成员简介
     */
    Integer updatefbi(String id);
}
