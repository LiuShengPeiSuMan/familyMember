package com.liushengpei.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushengpei.dao.FamilyBriefIntroductionDao;
import com.liushengpei.dao.PeopleHouseDao;
import com.liushengpei.feign.ExamineFeign;
import com.liushengpei.feign.FamilyMemberFeign;
import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.pojo.UpdateFamilyDetailed;
import com.liushengpei.service.IFamilyService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.domain.Examine;
import util.domain.FamilyMember;
import util.domain.FamilyVO;

import java.util.*;

import static util.constant.ConstantToolUtil.*;

/**
 * 家庭成员
 */
@Service
public class FamilyServiceImpl implements IFamilyService {

    @Autowired
    private FamilyBriefIntroductionDao introductionDao;
    @Autowired
    private PeopleHouseDao houseDao;
    @Autowired
    private ExamineFeign examineFeign;
    @Autowired
    private FamilyMemberFeign memberFeign;
    //缓存
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 添加家庭成员
     */
    @Override
    public String addToFamily(FamilyVO familyVO) {
        //校验邮箱
        if (familyVO.getEmail() != null && !familyVO.getEmail().equals("")) {
            if (!familyVO.getEmail().matches(EMAIL_CHECK)) {
                return "邮箱格式不正确";
            }
        }
        //校验电话号码
        if (familyVO.getPhone() != null && !familyVO.getPhone().equals("")) {
            if (!familyVO.getPhone().matches(PHONE_CHECK)) {
                return "电话号码格式不正确";
            }
        }
        //查询缓存有没有此条数据
        Object family = redisTemplate.opsForValue().get(CHECK_FAMILY + familyVO.getName());
        if (family != null) {
            return "此家庭成员已添加,待族长审核中！";
        }
        //查询数据库有没有此家庭成员
        Integer count = introductionDao.queryFamilyByName(familyVO.getName());
        if (count > 0) {
            return "此家族成员已添加";
        }
        //查询户主id
        String houseId = houseDao.queryHouseIdByName(familyVO.getLoginName());
        //家族成员id
        String familyPeopleId = UUID.randomUUID().toString().substring(0, 32);
        //审核id
        String examineId = UUID.randomUUID().toString().substring(0, 32);
        familyVO.setHouseId(houseId);
        familyVO.setFamilyMemberId(familyPeopleId);
        familyVO.setId(examineId);
        //添加一条审核记录
        Examine examine = new Examine();
        examine.setId(examineId);
        examine.setBabyorpeopleId(familyPeopleId);
        examine.setHouseId(houseId);
        //审核类型添加家庭成员
        examine.setExamineType(1);
        examine.setExamineStatus(0);
        examine.setReason(familyVO.getReason());
        examine.setExamineUser("");
        examine.setExamineTime(null);
        examine.setSubmitUser(familyVO.getLoginName());
        examine.setCreateTime(new Date());
        examine.setDelFlag(0);
        examineFeign.addExamine(examine);
        //序列化对象
        String familyvo = null;
        try {
            familyvo = mapper.writeValueAsString(familyVO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //添加到缓存
        if (familyvo != null) {
            redisTemplate.opsForValue().set(ADD_FAMILY + familyPeopleId, familyvo);
            //添加成员校验
            redisTemplate.opsForValue().set(CHECK_FAMILY + familyVO.getName(), familyPeopleId);
        }
        return "添加成功，待族长审核！";
    }

    /**
     * 查询全部家庭成员
     */
    @Override
    public List<FamilyBriefIntroduction> allFamily(String loginName) {
        //根据登录人姓名查询户主id
        String houseId = introductionDao.queryHouseIdByName(loginName);
        //查询所有该户主下的家庭成员
        List<FamilyBriefIntroduction> introductions = introductionDao.familyList(houseId);
        return introductions;
    }

    /**
     * 查询家庭成员详细信息
     */
    @Override
    public FamilyMember familyDetailed(String familyPeopleId) {
        FamilyMember member = memberFeign.queryInformation(familyPeopleId);
        return member;
    }

    /**
     * 修改家庭成员基本信息
     */
    @Override
    @GlobalTransactional
    public String updateFamily(UpdateFamilyDetailed detailed) {
        //修改家庭成员信息简介
        Map<String, Object> params = new HashMap<>();
        params.put("familyPeopleId", detailed.getFamilyPeopleId());
        params.put("isMarry", detailed.getIsMarry());
        params.put("phone", detailed.getPhone());
        params.put("updateTime", new Date());
        params.put("updateUser", detailed.getLoginName());
        introductionDao.updateFamily(params);
        //修改家族成员信息
        Map<String, Object> params02 = new HashMap<>();
        params02.put("homeAddress", detailed.getHomeAddress());
        params02.put("marriedOfNot", detailed.getIsMarry());
        params02.put("education", detailed.getEducation());
        params02.put("work", detailed.getWork());
        params02.put("workAddress", detailed.getWorkAddress());
        params02.put("phone", detailed.getPhone());
        params02.put("email", detailed.getEmail());
        params02.put("updateTime", new Date());
        params02.put("updateUser", detailed.getLoginName());
        params02.put("id", detailed.getFamilyPeopleId());
        memberFeign.updateFamilyMember(params02);
        return "修改成功";
    }

    /**
     * 删除家庭成员
     */
    @Override
    public String deleteFamily(String familyPeopleId, String loginName, String reason) {
        //判断有没有重复提交删除
        Integer count = examineFeign.countPeopleExamine(familyPeopleId);
        if (count > 0) {
            return "此成员已删除，族长审核中！";
        }
        //查询户主id
        String houseId = introductionDao.houseId(familyPeopleId);
        //添加一条审核记录
        Examine examine = new Examine();
        examine.setId(UUID.randomUUID().toString().substring(0, 32));
        examine.setBabyorpeopleId(familyPeopleId);
        examine.setHouseId(houseId);
        //审核类型删除家庭成员
        examine.setExamineType(2);
        examine.setExamineStatus(0);
        examine.setReason(reason);
        examine.setExamineUser(null);
        examine.setExamineTime(null);
        examine.setSubmitUser(loginName);
        examine.setCreateTime(new Date());
        examine.setDelFlag(0);
        examineFeign.addExamine(examine);
        return "删除成功，待族长审核";
    }
}
