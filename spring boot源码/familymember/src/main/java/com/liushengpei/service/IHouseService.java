package com.liushengpei.service;

import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.HouseSituation;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;

import java.util.List;

/**
 * 户主管理
 */
public interface IHouseService {

    /**
     * 查询全部户主
     */
    List<HouseSituation> queryAllHouse(String name);

    /**
     * 查询户主所有家庭成员
     */
    List<PeopleHouse> queryAllPeopleHouse(String id);

    /**
     * 查询家庭成员详细信息
     */
    FamilyMember queryFamilyDetailed(String familyPeopleId);

    /**
     * 添加户主
     */
    String addHouse(FamilyMemberUtilVO memberUtilVO);


}
