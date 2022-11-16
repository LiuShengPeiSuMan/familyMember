package com.liushengpei.service;

import com.github.pagehelper.PageInfo;
import com.liushengpei.pojo.FamilyMember;

import java.util.List;

/**
 * 全部成员管理
 */
public interface IMemberService {

    /**
     * 查询所有家庭成员
     */
    List<FamilyMember> allMember(String name);

    /**
     * 分页查询
     */
    PageInfo<FamilyMember> pageMember(Integer num, Integer size);

    /**
     * 查询家族成员详细信息
     */
    FamilyMember detailed(String id);

    /**
     * 定时怎加年龄
     */
    void addAge();
}
