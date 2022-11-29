package com.liushengpei.dao;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import org.apache.ibatis.annotations.Mapper;

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
}
