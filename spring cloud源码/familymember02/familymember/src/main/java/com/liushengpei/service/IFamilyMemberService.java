package com.liushengpei.service;

import com.liushengpei.pojo.FamilyMember;

import java.util.List;

public interface IFamilyMemberService {

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
     * 查询所有家族成员
     */
    List<FamilyMember> familyMemberList();
}
