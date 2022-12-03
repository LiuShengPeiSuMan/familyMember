package com.liushengpei.dao;

import com.liushengpei.pojo.BabySituation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BabySituationDao {

    /**
     * 查询本年度出生成员
     */
    List<BabySituation> queryBabyOrYear();

    /**
     * 添加出生成员
     */
    Integer insertBaby(BabySituation situation);

    /**
     * 查询有没有重复添加出生成员
     */
    Integer babyCount(String name);

    /**
     * 查询出生成员信息
     */
    BabySituation queryBaby(String id);
}
