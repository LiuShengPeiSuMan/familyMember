package com.liushengpei.dao;

import com.liushengpei.pojo.FamilyMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 家族成员
 */
@Mapper
public interface FamilyMemberDao {

    /**
     * 添加家族成员
     */
    Integer addFamilyMember(FamilyMember member);

    /**
     * 查询id
     */
    String queryFamilyMemberId(String name);

    /**
     * 查询家族成员详细信息
     */
    FamilyMember queryFamilyMember(String id);

    /**
     * 查询所有家族成员
     */
    List<FamilyMember> familyMemberList();

    /**
     * 修改家庭成员信息
     */
    Integer updateFamilyMember(Map<String, Object> params);

    /**
     * 修改家族成员删除标记
     */
    Integer delFamilyMember(Map<String, Object> params);

    /**
     * 查询一周之内过生日的人
     */
    List<FamilyMember> queryDateOfBirth();
}
