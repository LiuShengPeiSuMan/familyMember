package com.liushengpei.dao;

import com.liushengpei.pojo.PeopleHouse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 家庭成员与户主关系
 */
@Mapper
public interface PeopleHouseDao {

    /**
     * 添加家庭成员与户主关系
     */
    Integer addPeopleHouse(PeopleHouse peopleHouse);

    /**
     * 查询户主id
     */
    String queryHouseId(String id);

    /**
     * 删除家庭成员与户主关系
     */
    Integer delPeopleHouse(String id);

    /**
     * 删除家族成员
     */
    Integer delPeopleHouse02(String id);

    /**
     * 查询所有家族成员
     */
    List<PeopleHouse> queryAllPeopleHouse(String householderId);

    /**
     * 查询登录成员所属户主
     */
    String queryHouseIdByName(String name);

    /**
     * 查询户主本人
     */
    PeopleHouse queryHuzhu(String householderId);
}
