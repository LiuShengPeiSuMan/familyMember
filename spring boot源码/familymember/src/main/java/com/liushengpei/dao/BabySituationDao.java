package com.liushengpei.dao;

import com.liushengpei.pojo.BabySituation;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 新生儿
 */
@Mapper
public interface BabySituationDao {

    /**
     * 添加婴儿
     */
    Integer addBaby(BabySituation babySituation);

    /**
     * 查询是否有添加过的新生儿
     */
    Integer bmfCount(Map<String, String> params);

    /**
     * 查询本年度新生儿
     */
    List<BabySituation> yearNewBaby();

    /**
     * 根据id查询新生儿详细信息
     */
    BabySituation detailedBabyById(String id);

    /**
     * 查询本年度数据
     */
    List<Date> yearData();
}
