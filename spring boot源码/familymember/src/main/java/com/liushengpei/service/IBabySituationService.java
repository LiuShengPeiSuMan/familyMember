package com.liushengpei.service;

import com.liushengpei.pojo.BabySituation;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;

import java.util.List;

/**
 * 新生儿
 */
public interface IBabySituationService {

    /**
     * 添加新生儿
     */
    String addBaby(FamilyMemberUtilVO memberUtilVO);

    /**
     * 查询本年度新生儿
     */
    List<BabySituation> yearBaby();

    /**
     * 查询出生成员详细信息
     */
    BabySituation detailedBaby(String id);
}
