package com.liushengpei.service;

import com.liushengpei.pojo.BabySituation;

import java.util.Date;
import java.util.List;

public interface IForeignService {

    /**
     * 查询出生成员信息
     */
    BabySituation queryBabySituation(String id);

    /**
     * 添加出生成员
     */
    String addBaby(BabySituation situation);

    /**
     * 查询本年度数据
     */
    List<Date> yearData();
}
