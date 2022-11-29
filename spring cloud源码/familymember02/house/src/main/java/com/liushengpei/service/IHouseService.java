package com.liushengpei.service;

import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.HouseSituation;
import com.liushengpei.pojo.PeopleHouse;
import util.domain.FamilyVO;

import java.util.List;

/**
 * 户主管理
 */
public interface IHouseService {

    /**
     * 添加户主
     */
    String addHouse(FamilyVO familyVO);

    /**
     * 查询全部户主
     */
    List<HouseSituation> queryHouses();

    /**
     * 查询户主所有家庭成员
     */
    List<PeopleHouse> queryFamilyAll(String houseId);

    /**
     * 查询家庭成员详细信息
     */
    FamilyMember queryFamilyMember(String familyPeopleId);
}
