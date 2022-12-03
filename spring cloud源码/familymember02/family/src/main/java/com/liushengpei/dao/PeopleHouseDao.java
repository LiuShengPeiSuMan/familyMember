package com.liushengpei.dao;

import com.liushengpei.pojo.PeopleHouse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PeopleHouseDao {

    /**
     * 添加家庭成员与户主关系
     */
    Integer addPeopleHouse(PeopleHouse peopleHouse);

    /**
     * 查询户主所有家庭成员
     */
    List<PeopleHouse> queryFamilyAll(String householderId);

    /**
     * 查询户主id
     */
    String queryHouseIdByName(String name);

    /**
     * 删除家族成员与户主关系
     */
    Integer delPeopleHouse(Map<String, Object> params);

    /**
     * 查询户主id
     */
    String selectHouseId(String familyPeopleId);
}
