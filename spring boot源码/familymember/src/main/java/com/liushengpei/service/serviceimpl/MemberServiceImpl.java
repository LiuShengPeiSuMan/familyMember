package com.liushengpei.service.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liushengpei.dao.FamilyMemberDao;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 家族成员管理
 */
@Service
public class MemberServiceImpl implements IMemberService {

    @Autowired
    private FamilyMemberDao memberDao;

    /**
     * 查询所有家族成员
     */
    @Override
    public List<FamilyMember> allMember(String name) {
        //查询所有家族成员
        List<FamilyMember> familyMembers = memberDao.selectAll(name);
        return familyMembers;
    }

    /**
     * 分页查询
     */
    @Override
    public PageInfo<FamilyMember> pageMember(Integer num, Integer size) {
        PageHelper.startPage(num, size);
        List<FamilyMember> familyMembers = memberDao.selectAll(null);
        return new PageInfo<>(familyMembers);
    }

    /**
     * 查询家族成员详细信息
     */
    @Override
    public FamilyMember detailed(String id) {
        FamilyMember familyMember = memberDao.peopleDetailedById(id);
        return familyMember;
    }

    /**
     * 定时增加年龄
     */
    @Override
    public void addAge() {
        //查询今天过生日成员
        List<FamilyMember> familyMemberList = memberDao.selectNowDay();
        if (!familyMemberList.isEmpty()) {
            for (FamilyMember f : familyMemberList) {
                //查询具体年龄
                Integer integer = memberDao.queryAgeByName(f.getId());
                //更新年龄
                Map<String, Object> params = new HashMap<>();
                params.put("id", f.getId());
                params.put("age", integer);
                memberDao.updateAge(params);
            }
        }
    }
}
