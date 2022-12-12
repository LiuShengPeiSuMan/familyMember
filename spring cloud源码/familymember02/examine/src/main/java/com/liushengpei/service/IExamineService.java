package com.liushengpei.service;

import com.liushengpei.pojo.Examine;
import util.domain.FamilyVO;

import java.util.List;

/**
 * 审核管理
 */
public interface IExamineService {

    /**
     * 查询所有待审核记录
     */
    List<Examine> queryAllExamine();

    /**
     * 查询提交的审核详细信息
     */
    Object queryExamine(String id, Integer type);

    /**
     * 通过或驳回
     */
    String adoptAndReject(String id, Integer type, Integer status, String examineUser);

    /**
     * 查询每个户主本年度提交的审核记录
     */
    List<Examine> houseExamineHistory(String loginName);
}
