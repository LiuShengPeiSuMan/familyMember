package com.liushengpei.service;

import java.util.Map;

/**
 * 对外提供接口逻辑
 */
public interface IForeignService {

    /**
     * 修改户主家庭人数（添加）
     */
    Integer addFamilyPeopleNum(Map<String, Object> params);

    /**
     * 减少家庭成员数量
     */
    String reduceFamilyNum(Map<String, Object> params);

    /**
     * 根据家族成员id查询户主id
     */
    String queryHouseId(String familyPeopleId);
}
