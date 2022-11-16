package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.*;
import com.liushengpei.pojo.*;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;
import com.liushengpei.service.IHouseService;
import com.liushengpei.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.liushengpei.util.ConstantToolUtil.*;

/**
 * 户主管理
 */
@Service
public class HouseServiceImpl implements IHouseService {

    @Autowired
    private HouseSituationDao houseSituationDao;
    @Autowired
    private PeopleHouseDao peopleHouseDao;
    @Autowired
    private FamilyMemberDao familyMemberDao;
    @Autowired
    private UserLoginDao loginDao;
    @Autowired
    private FamilyBriefIntroductionDao introductionDao;

    /**
     * 查询全部户主
     */
    @Override
    public List<HouseSituation> queryAllHouse(String name) {
        List<HouseSituation> houseSituations = houseSituationDao.queryAllHouse(name);
        return houseSituations;
    }

    /**
     * 查询户主所有家族成员
     */
    @Override
    public List<PeopleHouse> queryAllPeopleHouse(String id) {
        //查询审核通过的家庭成员
        List<PeopleHouse> peopleHouses = peopleHouseDao.queryAllPeopleHouse(id);
        //查询户主
        PeopleHouse peopleHouse = peopleHouseDao.queryHuzhu(id);
        peopleHouses.add(peopleHouse);
        return peopleHouses;
    }

    /**
     * 查询家庭成员详细信息
     */
    @Override
    public FamilyMember queryFamilyDetailed(String familyPeopleId) {
        FamilyMember familyMember = familyMemberDao.peopleDetailedById(familyPeopleId);
        return familyMember;
    }

    /**
     * 添加户主（族长添加）
     */
    @Transactional
    @Override
    public String addHouse(FamilyMemberUtilVO memberUtilVO) {
        String houseHolderID = null;
        String phone = memberUtilVO.getPhone();
        String email = memberUtilVO.getEmail();
        if (!phone.matches(PHONE_CHECK)) {
            return "电话号格式不正确";
        }
        if (!email.matches(EMAIL_CHECK)) {
            return "邮箱格式不正确";
        }
        //判断有没有添加过此户主
        Integer name = familyMemberDao.queryName(memberUtilVO.getName());
        if (name > 0) {
            return "此成员已存在，请从新添加";
        }
        //添加家庭成员
        FamilyMember familyMember = new FamilyMember();
        String id = UUID.randomUUID().toString().substring(0, 32);
        familyMember.setId(id);
        familyMember.setName(memberUtilVO.getName());
        //年龄
        familyMember.setAge(memberUtilVO.getAge());
        familyMember.setSex(memberUtilVO.getSex());
        familyMember.setHomeAddress(memberUtilVO.getHomeAddress());
        familyMember.setDateOfBirth(memberUtilVO.getDateOfBirth());
        familyMember.setDateOfDeath(null);
        familyMember.setMarriedOfNot(memberUtilVO.getMarriedOfNot());
        familyMember.setEducation(memberUtilVO.getEducation());
        familyMember.setWorkAddress(memberUtilVO.getWorkAddress());
        familyMember.setWork(memberUtilVO.getWork());
        familyMember.setPhone(phone);
        familyMember.setEmail(email);
        familyMember.setCreateTime(new Date());
        familyMember.setCreateUser(memberUtilVO.getLoginName());
        familyMember.setUpdateTime(new Date());
        familyMember.setUpdateUser(memberUtilVO.getLoginName());
        familyMember.setDelFlag(0);
        familyMemberDao.addMember(familyMember);
        //添加户主
        HouseSituation situation = new HouseSituation();
        //户主id
        houseHolderID = UUID.randomUUID().toString().substring(0, 32);
        situation.setId(houseHolderID);
        situation.setFamilyPeopleId(id);
        situation.setName(memberUtilVO.getName());
        situation.setSex(memberUtilVO.getSex());
        situation.setHouseNumber(memberUtilVO.getHouseNumber());
        situation.setPeopleNumber(1);
        //死亡人口
        situation.setDeadNumber(0);
        situation.setCreateTime(new Date());
        situation.setCreateUser(memberUtilVO.getLoginName());
        situation.setUpdateTime(new Date());
        situation.setUpdateUser(memberUtilVO.getLoginName());
        situation.setDelFlag(0);
        houseSituationDao.addHouse(situation);
        //默认给户主添加登录权限
        UserLogin login = new UserLogin();
        login.setId(UUID.randomUUID().toString().substring(0, 32));
        //汉字转拼音
        String account = ToolUtil.toPinYin(memberUtilVO.getName());
        login.setAccount(HUZHU + account);
        String pwd = PASSWORD;
        login.setPassword(pwd);
        login.setLoginEmail(email);
        login.setNickname(memberUtilVO.getName());
        //角色(户主)
        login.setRole(2);
        login.setCreateTime(new Date());
        login.setCreateUser(memberUtilVO.getLoginName());
        login.setUpdateTime(new Date());
        login.setUpdateUser(memberUtilVO.getLoginName());
        login.setDelFlag(0);
        loginDao.addUser(login);
        //添加户主与家庭成员关系表
        PeopleHouse peopleHouse = new PeopleHouse();
        peopleHouse.setId(UUID.randomUUID().toString().substring(0, 32));
        peopleHouse.setHouseholderId(houseHolderID);
        peopleHouse.setFamilyPeopleId(id);
        peopleHouse.setName(memberUtilVO.getName());
        peopleHouse.setRelationship("户主");
        peopleHouse.setCreateTime(new Date());
        peopleHouse.setCreateUser(memberUtilVO.getLoginName());
        peopleHouse.setUpdateTime(new Date());
        peopleHouse.setUpdateUser(memberUtilVO.getLoginName());
        peopleHouse.setDelFlag(0);
        peopleHouseDao.addPeopleHouse(peopleHouse);
        //添加成员简介
        FamilyBriefIntroduction introduction = new FamilyBriefIntroduction();
        introduction.setId(UUID.randomUUID().toString().substring(0, 32));
        introduction.setFamilyPeopleId(id);
        //户主id
        introduction.setHouseId(houseHolderID);
        introduction.setName(memberUtilVO.getName());
        introduction.setAge(memberUtilVO.getAge());
        introduction.setSex(memberUtilVO.getSex());
        introduction.setPhone(phone);
        //是否已婚
        introduction.setIsMarry(familyMember.getMarriedOfNot());
        introduction.setCreateTime(new Date());
        introduction.setCreateUser(memberUtilVO.getLoginName());
        introduction.setUpdateTime(new Date());
        introduction.setUpdateUser(memberUtilVO.getLoginName());
        introduction.setDelFlag(0);
        introductionDao.addFamilyBriefIntroduction(introduction);
        return "添加户主成功";
    }


}
