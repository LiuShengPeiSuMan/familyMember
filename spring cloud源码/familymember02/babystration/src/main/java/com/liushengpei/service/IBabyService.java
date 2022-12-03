package com.liushengpei.service;

import com.liushengpei.pojo.BabySituation;
import util.domain.FamilyVO;

import java.util.List;

public interface IBabyService {

    /**
     * 查询本年度出生成员
     */
    List<BabySituation> queryBaby();

    /**
     * 添加
     */
    String addBaby(FamilyVO familyVO);

    /**
     * 查询出生成员信息
     */
    BabySituation queryBabyDetailed(String id);

}
