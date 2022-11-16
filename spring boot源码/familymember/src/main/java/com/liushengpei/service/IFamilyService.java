package com.liushengpei.service;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;

import java.util.List;

/**
 * 家庭成员管理
 * */
public interface IFamilyService {

    /**
     * 添加家庭成员
     * */
    String addFamily(FamilyMemberUtilVO familyMembervo);

    /**
     * 删除家庭成员
     * */
    String deleteFamily(String id,String reason);

    /**
     * 查询家族成员
     * */
    List<FamilyBriefIntroduction> queryAllFamily(String huZhuId,String name);

    /**
     * 修改家庭成员
     * */
    String updateFamily(FamilyMemberUtilVO memberUtilVO);

    /**
     * 查询登录成员所属户主id
     */
    String queryHouseId(String name);
}
