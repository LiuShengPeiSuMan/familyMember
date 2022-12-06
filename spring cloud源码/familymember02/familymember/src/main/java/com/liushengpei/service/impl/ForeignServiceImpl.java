package com.liushengpei.service.impl;

import com.liushengpei.dao.FamilyMemberDao;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 对外提供接口
 */
@Service
public class ForeignServiceImpl implements IForeignService {

    @Autowired
    private FamilyMemberDao memberDao;

    /**
     * 修改家庭成员信息
     */
    @Override
    public String updateFamilyMember(Map<String, Object> params) {
        //添加日期
        params.put("updateTime", new Date());
        Integer integer = memberDao.updateFamilyMember(params);
        if (integer > 0) {
            return "修改成功";
        }
        return "修改失败";
    }

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
     * 删除家族成员
     */
    @Override
    public String delFamilyMember(Map<String, Object> params) {
        params.put("updateTime", new Date());
        Integer num = memberDao.delFamilyMember(params);
        if (num > 0) {
            return "删除成功";
        }
        return "删除失败";
    }

    /**
     * 查询所有家族成员
     */
    @Override
    public List<FamilyMember> familyMemberAll() {
        List<FamilyMember> familyMemberList = memberDao.familyMemberList();
        return familyMemberList;
    }

    /**
     * 查询一周只能过生日的成员
     */
    @Override
    public List<FamilyMember> queryDateOfBirth() {
        List<FamilyMember> familyMemberList = memberDao.queryDateOfBirth();
        return familyMemberList;
    }
}
