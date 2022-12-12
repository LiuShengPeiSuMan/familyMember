package com.liushengpei.service.impl;

import com.liushengpei.dao.FamilyMemberDao;
import com.liushengpei.feign.FamilyFeign;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.service.IFamilyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 家族成员管理
 */
@Service
public class FamilyMemberServiceImpl implements IFamilyMemberService {

    @Autowired
    private FamilyMemberDao memberDao;
    @Autowired
    private FamilyFeign familyFeign;

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

    /**
     * 定时添加年龄
     */
    @Override
    public void addAge() {
        //查询当天过生日的成员
        List<FamilyMember> familyMemberList = memberDao.selectNowDay();
        if (!familyMemberList.isEmpty()) {
            for (FamilyMember f : familyMemberList) {
                //更新家族成员年龄
                Map<String, Object> params = new HashMap<>();
                params.put("id", f.getId());
                params.put("updateTime", new Date());
                memberDao.updateAge(params);
                //更新家族成员简介年龄
                familyFeign.updateFamilyAge(f.getId());
            }
        }
    }
}
