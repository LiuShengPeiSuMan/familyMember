package com.liushengpei.service;

import com.liushengpei.pojo.FamilyMember;

import java.util.List;

public interface IFamilyMemberService {


    /**
     * 查询家族成员详细信息
     */
    FamilyMember queryFamilyMember(String id);

    /**
     * 查询所有家族成员
     */
    List<FamilyMember> familyMemberList();


    /**
     * 定时增加年龄
     */
    void addAge();
}
