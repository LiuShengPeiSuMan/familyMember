package com.liushengpei.service.impl;

import com.liushengpei.dao.HouseDao;
import com.liushengpei.feign.FamilyFeign;
import com.liushengpei.feign.FamilyMemberFeign;
import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.HouseSituation;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.service.IHouseService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.domain.FamilyVO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 户主管理
 */
@Service
public class HouseServiceImpl implements IHouseService {

    @Autowired
    private HouseDao houseDao;
    @Autowired
    private FamilyFeign familyFeign;
    @Autowired
    private FamilyMemberFeign memberFeign;

    /**
     * 添加户主
     */
    @Override
    @GlobalTransactional
    public String addHouse(FamilyVO familyVO) {
        //判断此户主有没有添加过
        String id = memberFeign.queryId(familyVO.getName());
        System.err.println("====familyPeopleId====" + id);
        //查询户主
        if (id != null && !id.equals("")) {
            Integer integer = houseDao.queryHouse(id);
            if (integer > 0) {
                return "此户主已经添加，请重新添加！";
            }
        }
        String houseId = UUID.randomUUID().toString().substring(0, 32);
        String familyPeopleId = UUID.randomUUID().toString().substring(0, 32);
        //添加户主
        HouseSituation situation = new HouseSituation();
        situation.setId(houseId);
        situation.setFamilyPeopleId(familyPeopleId);
        situation.setName(familyVO.getName());
        situation.setSex(familyVO.getSex());
        situation.setHouseNumber(familyVO.getHouseNumber());
        situation.setPeopleNumber(1);
        situation.setDeadNumber(0);
        situation.setCreateTime(new Date());
        situation.setCreateUser(familyVO.getLoginName());
        situation.setUpdateTime(new Date());
        situation.setUpdateUser(familyVO.getLoginName());
        situation.setDelFlag(0);
        Integer num = houseDao.addHouse(situation);
        //添加家庭成员简介
        FamilyBriefIntroduction introduction = new FamilyBriefIntroduction();
        introduction.setId(UUID.randomUUID().toString().substring(0, 32));
        introduction.setFamilyPeopleId(familyPeopleId);
        introduction.setHouseId(houseId);
        introduction.setName(familyVO.getName());
        introduction.setAge(familyVO.getAge());
        introduction.setSex(familyVO.getSex());
        introduction.setPhone(familyVO.getPhone());
        introduction.setIsMarry(familyVO.getMarriedOfNot());
        introduction.setCreateTime(new Date());
        introduction.setCreateUser(familyVO.getLoginName());
        introduction.setUpdateTime(new Date());
        introduction.setUpdateUser(familyVO.getLoginName());
        introduction.setDelFlag(0);
        familyFeign.addFamily(introduction);
        //添加户主关系
        PeopleHouse house = new PeopleHouse();
        house.setId(UUID.randomUUID().toString().substring(0, 32));
        house.setHouseholderId(houseId);
        house.setFamilyPeopleId(familyPeopleId);
        house.setName(familyVO.getName());
        house.setRelationship("户主");
        house.setCreateTime(new Date());
        house.setCreateUser(familyVO.getLoginName());
        house.setUpdateTime(new Date());
        house.setUpdateUser(familyVO.getLoginName());
        house.setDelFlag(0);
        familyFeign.addPeopleHouse(house);
        //添加家族成员
        FamilyMember member = new FamilyMember();
        member.setId(familyPeopleId);
        member.setName(familyVO.getName());
        member.setAge(familyVO.getAge());
        member.setSex(familyVO.getSex());
        member.setHomeAddress(familyVO.getHomeAddress());
        member.setDateOfBirth(familyVO.getDateOfBirth());
        member.setDateOfDeath(familyVO.getDateOfDeath());
        member.setMarriedOfNot(familyVO.getMarriedOfNot());
        member.setEducation(familyVO.getEducation());
        member.setWork(familyVO.getWork());
        member.setWorkAddress(familyVO.getWorkAddress());
        member.setPhone(familyVO.getPhone());
        member.setEmail(familyVO.getEmail());
        member.setCreateTime(new Date());
        member.setCreateUser(familyVO.getLoginName());
        member.setUpdateTime(new Date());
        member.setUpdateUser(familyVO.getLoginName());
        member.setDelFlag(0);
        memberFeign.addFamilyMember(member);
        return "添加成功";
    }

    /**
     * 查询全部户主
     */
    @Override
    public List<HouseSituation> queryHouses() {
        //全部户主
        List<HouseSituation> houseSituations = houseDao.queryHoueses();
        return houseSituations;
    }

    /**
     * 查询户主所有家庭成员
     */
    @Override
    public List<PeopleHouse> queryFamilyAll(String houseId) {
        List<PeopleHouse> peopleHouses = familyFeign.queryAllFamily(houseId);
        return peopleHouses;
    }

    /**
     * 查询家庭成员详细信息
     */
    @Override
    public FamilyMember queryFamilyMember(String familyPeopleId) {
        FamilyMember member = memberFeign.queryInformation(familyPeopleId);
        return member;
    }
}
