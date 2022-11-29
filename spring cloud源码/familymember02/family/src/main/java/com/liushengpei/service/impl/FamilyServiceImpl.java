package com.liushengpei.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushengpei.dao.FamilyBriefIntroductionDao;
import com.liushengpei.dao.PeopleHouseDao;
import com.liushengpei.feign.ExamineFeign;
import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.service.IFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.domain.Examine;
import util.domain.FamilyMember;
import util.domain.FamilyVO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    //缓存
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 添加家庭成员
     */
    @Override
    public String addFamily(FamilyBriefIntroduction introduction) {
        Integer integer = introductionDao.addFamily(introduction);
        if (integer > 0) {
            return "添加成功";
        }
        return "添加失败";
    }


    /**
     * 添加家族成员与户主关系
     */
    @Override
    public String addPeopleHouse(PeopleHouse house) {
        Integer integer = houseDao.addPeopleHouse(house);
        if (integer > 0) {
            return "添加成功";
        }
        return "添加失败";
    }

    /**
     * 查询户主所有家庭成员
     */
    @Override
    public List<PeopleHouse> queryFamilyAll(String houseId) {
        List<PeopleHouse> peopleHouses = houseDao.queryFamilyAll(houseId);
        return peopleHouses;
    }

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
     * 修改家庭成员基本信息
     */
    @Override
    public String updateFamily(FamilyMember member) {


        return null;
    }
}
