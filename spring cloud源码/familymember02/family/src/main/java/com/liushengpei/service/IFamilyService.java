package com.liushengpei.service;

import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;
import util.domain.FamilyMember;
import util.domain.FamilyVO;

import java.util.List;

/**
 * 家庭成员
 */
public interface IFamilyService {

    /**
     * 添加家庭成语
     */
    String addFamily(FamilyBriefIntroduction introduction);

    /**
     * 添加家族成员与户主关系
     */
    String addPeopleHouse(PeopleHouse house);

    /**
     * 查询所有户主家庭成员
     */
    List<PeopleHouse> queryFamilyAll(String houseId);

    /**
     * 添加家挺成员
     */
    String addToFamily(FamilyVO familyVO);

    /**
     * 修改家庭成员基本信息
     */
    String updateFamily(FamilyMember member);
}
