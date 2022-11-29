package com.liushengpei.service.impl;

import com.liushengpei.dao.FamilyMemberDao;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.service.IFamilyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 家族成员管理
 */
@Service
public class FamilyMemberServiceImpl implements IFamilyMemberService {

    @Autowired
    private FamilyMemberDao memberDao;

    /**
     * 添加家族成员
     */
    @Override
    public Integer addFamilyMember(FamilyMember member) {
        Integer integer = memberDao.addFamilyMember(member);
        return integer;
    }

    /**
     * 查询id
     */
    @Override
    public String queryId(String name) {
        String familyMemberId = memberDao.queryFamilyMemberId(name);
        if (familyMemberId == null || familyMemberId.equals("")) {
            return null;
        }
        return familyMemberId;
    }

    /**
     * 查询家族成员详细信息
     */
    @Override
    public FamilyMember queryFamilyMember(String id) {
        FamilyMember familyMember = memberDao.queryFamilyMember(id);
        return familyMember;
    }

    /**
     * 查询所有家族成员
     */
    @Override
    public List<FamilyMember> familyMemberList() {
        List<FamilyMember> familyMemberList = memberDao.familyMemberList();
        return familyMemberList;
    }
}
