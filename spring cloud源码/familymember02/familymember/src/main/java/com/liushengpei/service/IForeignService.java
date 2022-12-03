package com.liushengpei.service;

import com.liushengpei.pojo.FamilyMember;

import java.util.Map;

/**
 * 对外提供接口
 */
public interface IForeignService {

    /**
     * 修改家庭成员信息
     */
    String updateFamilyMember(Map<String, Object> params);

    /**
     * 添加家族成员
     */
    Integer addFamilyMember(FamilyMember member);

    /**
     * 查询id
     */
    String queryId(String name);

    /**
     * 查询家族成员详细信息
     */
    FamilyMember queryFamilyMember(String id);

    /**
     * 删除家族成员
     */
    String delFamilyMember(Map<String, Object> params);
}
