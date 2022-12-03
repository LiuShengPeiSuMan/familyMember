package com.liushengpei.service;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.pojo.UpdateFamilyDetailed;
import util.domain.FamilyMember;
import util.domain.FamilyVO;

import java.util.List;
import java.util.Map;

/**
 * 家庭成员
 */
public interface IFamilyService {

    /**
     * 添加家挺成员
     */
    String addToFamily(FamilyVO familyVO);

    /**
     * 查询全部家庭成员
     */
    List<FamilyBriefIntroduction> allFamily(String loginName);

    /**
     * 查询家庭成员详细信息
     */
    FamilyMember familyDetailed(String familyPeopleId);

    /**
     * 修改家庭成员基本信息
     */
    String updateFamily(UpdateFamilyDetailed detailed);

    /**
     * 删除家庭成员
     */
    String deleteFamily(String familyPeopleId,String loginName,String reason);
}
