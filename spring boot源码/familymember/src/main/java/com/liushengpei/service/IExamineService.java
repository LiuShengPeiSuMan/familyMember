package com.liushengpei.service;

import com.liushengpei.pojo.Examine;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;

import java.util.List;

/**
 * 审核
 */
public interface IExamineService {
    /**
     * 查询审核数据
     */
    List<Examine> examineAll();

    /**
     * 查询详细数据
     * */
    Object detailedData(String id,Integer type);

    /**
     * 修改审核结果
     * */
    Integer updateResult(Integer status,String id,Integer type,String examineUser);

    /**
     * 审核记录
     * */
    List<Examine> Record(Integer status);

    /**
     * 删除历史记录
     * */
    String delExamine(List<String> ids);
}
