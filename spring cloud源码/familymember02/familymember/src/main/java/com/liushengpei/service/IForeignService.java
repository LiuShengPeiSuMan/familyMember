package com.liushengpei.service;

import com.liushengpei.pojo.FamilyMember;

import java.util.List;
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

    /**
     * 查询所有家族成员
     */
    List<FamilyMember> familyMemberAll();

    /**
     * 查询一周之内过生日的人
     */
    List<FamilyMember> queryDateOfBirth();

    /**
     * 查询男女人数
     */
    List<Map<String, Object>> manAndWomanNum();

    /**
     * 查询所有人的年龄
     */
    List<Integer> queryAllAge();


}
