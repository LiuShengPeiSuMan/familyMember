package com.liushengpei.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushengpei.dao.*;
import com.liushengpei.pojo.*;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;
import com.liushengpei.service.IFamilyService;
import com.liushengpei.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.liushengpei.util.ConstantToolUtil.EMAIL_CHECK;
import static com.liushengpei.util.ConstantToolUtil.PHONE_CHECK;

/**
 * 家庭成员管理
 */
@Service
public class FamilyServiceImpl implements IFamilyService {

    @Autowired
    private FamilyMemberDao familyMemberDao;
    @Autowired
    private FamilyBriefIntroductionDao introductionDao;
    @Autowired
    private ExamineDao examineDao;
    @Autowired
    private PeopleHouseDao peopleHouseDao;
    @Autowired
    private HouseSituationDao houseSituationDao;
    @Autowired
    private FamilyMemberImageDao memberImageDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 添加家庭成员(户主添加)
     */
    @Transactional
    @Override
    public String addFamily(FamilyMemberUtilVO familyMembervo) {
        //查询户主id
        String houseId = houseSituationDao.queryHouseId(familyMembervo.getLoginName());
        //判断此成员是否添加过
        Integer integer1 = familyMemberDao.queryName(familyMembervo.getName());
        if (integer1 > 0) {
            return "此成员以添加过，请重新添加！";
        }
        String familyMemberId = UUID.randomUUID().toString().substring(0, 32);
        String user = familyMembervo.getLoginName();
        //添加家族成员
        FamilyMember familyMember = new FamilyMember();
        familyMember.setId(familyMemberId);
        familyMember.setName(familyMembervo.getName());
        familyMember.setAge(familyMembervo.getAge());
        familyMember.setSex(familyMembervo.getSex());
        familyMember.setHomeAddress(familyMembervo.getHomeAddress());
        familyMember.setDateOfBirth(familyMembervo.getDateOfBirth());
        familyMember.setDateOfDeath(familyMembervo.getDateOfDeath());
        familyMember.setMarriedOfNot(familyMembervo.getMarriedOfNot());
        familyMember.setEducation(familyMembervo.getEducation());
        familyMember.setWork(familyMembervo.getWork());
        familyMember.setWorkAddress(familyMembervo.getWorkAddress());
        //校验电话和邮箱格式是否正确
        String phone = familyMembervo.getPhone();
        String email = familyMembervo.getEmail();
        if (!phone.matches(PHONE_CHECK)) {
            return "电话格式不正确";
        }
        if (!email.matches(EMAIL_CHECK)) {
            return "邮箱格式不正确";
        }
        familyMember.setPhone(phone);
        familyMember.setEmail(email);
        familyMember.setCreateTime(new Date());
        familyMember.setCreateUser(user);
        familyMember.setUpdateUser(user);
        familyMember.setUpdateTime(new Date());
        familyMember.setDelFlag(0);
        familyMemberDao.addMember(familyMember);
        //添加家庭成员简介
        FamilyBriefIntroduction introduction = new FamilyBriefIntroduction();
        introduction.setId(UUID.randomUUID().toString().substring(0, 32));
        introduction.setFamilyPeopleId(familyMemberId);
        //户主id
        introduction.setHouseId(houseId);
        introduction.setName(familyMembervo.getName());
        introduction.setAge(familyMembervo.getAge());
        introduction.setSex(familyMembervo.getSex());
        introduction.setPhone(phone);
        //是否已婚
        introduction.setIsMarry(familyMember.getMarriedOfNot());
        introduction.setCreateTime(new Date());
        introduction.setCreateUser(user);
        introduction.setUpdateTime(new Date());
        introduction.setUpdateUser(user);
        introduction.setDelFlag(0);
        introductionDao.addFamilyBriefIntroduction(introduction);
        //添加审核
        Examine examine = new Examine();
        examine.setId(UUID.randomUUID().toString().substring(0, 32));
        examine.setBabyOrpeopleId(familyMemberId);
        examine.setHouseId(houseId);
        //添加家庭成员审核
        examine.setExamineType(1);
        //待审核
        examine.setExamineStatus(0);
        //原由
        examine.setReason(familyMembervo.getReason());
        //审核人
        examine.setExamineUser(null);
        examine.setExamineTime(new Date());
        examine.setSubmitUser(user);
        examine.setCreateTime(new Date());
        examine.setDelFlag(0);
        examineDao.addExamine(examine);
        //添加家族成员与户主关系
        PeopleHouse peopleHouse = new PeopleHouse();
        peopleHouse.setId(UUID.randomUUID().toString().substring(0, 32));
        peopleHouse.setHouseholderId(houseId);
        peopleHouse.setName(familyMembervo.getName());
        peopleHouse.setFamilyPeopleId(familyMemberId);
        //家庭成员与户主关系
        peopleHouse.setRelationship(familyMembervo.getRelationship());
        peopleHouse.setCreateTime(new Date());
        peopleHouse.setCreateUser(user);
        peopleHouse.setUpdateTime(new Date());
        peopleHouse.setUpdateUser(user);
        peopleHouse.setDelFlag(0);
        peopleHouseDao.addPeopleHouse(peopleHouse);
        //修改家庭总人口数
        //Integer number = houseSituationDao.peopleCount(familyMembervo.getLoginId());
//        if (number != null) {
//            Map<String, Object> parameter = new HashMap<>();
//            parameter.put("updateTime", new Date());
//            parameter.put("updateUser", familyMembervo.getLoginName());
//            parameter.put("id", familyMembervo.getLoginId());
//            houseSituationDao.addCountPeople(parameter);
//        }
        //添加头像数据
        FamilyMemberImage image = new FamilyMemberImage();
        //将图片转换为二进制
        MultipartFile file = familyMembervo.getFile();
        if (file != null) {
            image.setId(UUID.randomUUID().toString().substring(0, 32));
            image.setFamilyMemberId(familyMemberId);
            try {
                byte[] bytes = ImageUtil.imageByteArray(file);
                image.setImage(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setCreateTime(new Date());
            image.setCreateUser(familyMembervo.getLoginName());
            image.setUpdateTime(new Date());
            image.setUpdateUser(familyMembervo.getLoginName());
            image.setDelFlag(0);
            memberImageDao.addImage(image);
        }
        return "添加成功,待族长审核！";
    }

    /**
     * 删除家庭成员
     */
    @Transactional
    @Override
    public String deleteFamily(String id, String reason) {

        //判断之前此成员有没有提交过删除
        Integer queryNum = examineDao.queryNum(id);
        if (queryNum > 0) {
            return "删除此成员已提交，请耐心等待族长审核！";
        }
        //获取户主id
        String houseId = peopleHouseDao.queryHouseId(id);
        /**
         * 添加一条审核记录（通过后删除）
         * */
        Examine examine = new Examine();
        examine.setId(UUID.randomUUID().toString().substring(0, 32));
        examine.setHouseId(houseId);
        examine.setBabyOrpeopleId(id);
        //审核类型删除家庭成员
        examine.setExamineType(2);
        //删除原由
        examine.setReason(reason);
        examine.setExamineStatus(0);
        examine.setExamineUser(null);
        examine.setExamineTime(new Date());
        //户主id查询户主名字
        String name = houseSituationDao.queryName(houseId);
        if (name != null || name.equals("")) {
            examine.setSubmitUser(name);
        } else {
            examine.setSubmitUser("admin");
        }
        examine.setCreateTime(new Date());
        examine.setDelFlag(0);
        examineDao.addExamine(examine);
        return "删除成功，等待族长审核！";
    }

    /**
     * 查询家挺成员
     */
    @Override
    public List<FamilyBriefIntroduction> queryAllFamily(String huZhuId, String name) {
        //查询审核通过后的家族成员
        List<FamilyBriefIntroduction> familyList = introductionDao.queryAllFamily(huZhuId);
        //查询登录人本人
        FamilyBriefIntroduction introduction = introductionDao.queryOneself(name);
        familyList.add(introduction);
        return familyList;
    }

    /**
     * 修改家庭成员
     */
    @Transactional
    @Override
    public String updateFamily(FamilyMemberUtilVO memberUtilVO) {
        //修改家庭成员简介
        Map<String, Object> params = new HashMap<>();
        //判断电话和邮箱格式是否正确
        String phone = memberUtilVO.getPhone();
        String email = memberUtilVO.getEmail();
        if (!phone.matches(PHONE_CHECK)) {
            return "电话号格式不正确";
        }
        if (!email.matches(EMAIL_CHECK)) {
            return "邮箱格式不正确";
        }
        params.put("phone", phone);
        params.put("isMarry", memberUtilVO.getMarriedOfNot());
        params.put("updateTime", new Date());
        params.put("updateUser", memberUtilVO.getLoginName());
        String fbiid = introductionDao.queryFamilyPeopleId(memberUtilVO.getId());
        params.put("fbiid", fbiid);
        introductionDao.updateFamily(params);
        //修改家庭详细信息
        Map<String, Object> data = new HashMap<>();
        data.put("homeAddress", memberUtilVO.getHomeAddress());
        data.put("dateOfDeath", memberUtilVO.getDateOfDeath());
        data.put("marriedOfNot", memberUtilVO.getMarriedOfNot());
        data.put("education", memberUtilVO.getEducation());
        data.put("work", memberUtilVO.getWork());
        data.put("workAddress", memberUtilVO.getWorkAddress());
        data.put("phone", phone);
        data.put("email", email);
        data.put("updateTime", new Date());
        data.put("updateUser", memberUtilVO.getLoginName());
        data.put("id", memberUtilVO.getId());
        familyMemberDao.updateFamilyMember(data);
        return "修改信息成功";
    }

    /**
     * 查询登录成员所属户主id
     */
    @Override
    public String queryHouseId(String name) {
        String queryHouseIdByName = peopleHouseDao.queryHouseIdByName(name);
        return queryHouseIdByName;
    }
}
