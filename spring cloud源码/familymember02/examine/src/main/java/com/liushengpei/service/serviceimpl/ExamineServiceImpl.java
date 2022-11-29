package com.liushengpei.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushengpei.dao.ExamineDao;
import com.liushengpei.feign.FamilyFeign;
import com.liushengpei.feign.FamilyMemberFeign;
import com.liushengpei.feign.HouseFeign;
import com.liushengpei.pojo.Examine;
import com.liushengpei.service.IExamineService;
import io.seata.spring.annotation.GlobalTransactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.domain.FamilyBriefIntroduction;
import util.domain.FamilyMember;
import util.domain.FamilyVO;
import util.domain.PeopleHouse;

import java.util.*;

import static util.constant.ConstantToolUtil.ADD_FAMILY;
import static util.constant.ConstantToolUtil.CHECK_FAMILY;

/**
 * 待审核
 */
@Service
public class ExamineServiceImpl implements IExamineService {

    @Autowired
    private ExamineDao examineDao;
    @Autowired
    private FamilyMemberFeign memberFeign;
    @Autowired
    private FamilyFeign familyFeign;
    @Autowired
    private HouseFeign houseFeign;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 查询所有待审核记录
     */
    @Override
    public List<Examine> queryAllExamine() {
        List<Examine> examines = examineDao.queryAllExamine();
        return examines;
    }

    /**
     * 查询审核记录的详细信息
     */
    @Override
    public FamilyVO queryExamine(String id, Integer type) {
        FamilyVO familyVO = null;
        //判断审核类型
        if (type == 0) {   //添加新生儿

        } else if (type == 1) { //添加家庭成员
            //查询缓存
            String familyvo = (String) redisTemplate.opsForValue().get(ADD_FAMILY + id);
            //序列化成对象
            familyVO = JSONObject.parseObject(familyvo, FamilyVO.class);
            System.err.println("======familyvo=======" + familyvo);
        } else if (type == 2) { //删除家庭成员

        }
        return familyVO;
    }

    /**
     * 通过或驳回
     */
    @GlobalTransactional
    @Override
    public String adoptAndReject(String id, Integer type, Integer status, String examineUser) {
        //查询审核信息
        Examine examine = examineDao.examine(id);
        String e = (String) redisTemplate.opsForValue().get(ADD_FAMILY + examine.getBabyorpeopleId());
        //序列化对象
        FamilyVO familyVO = JSONObject.parseObject(e, FamilyVO.class);
        String message = "";
        if (status == 1) {  //审核通过
            if (type == 0) {   //添加新生儿
                message = "审核已通过";
            } else if (type == 1) { //添加家族成员
                //添加家族成员
                FamilyMember member = new FamilyMember();
                member.setId(familyVO.getFamilyMemberId());
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
                //添加家庭成员简介
                FamilyBriefIntroduction introduction = new FamilyBriefIntroduction();
                introduction.setId(UUID.randomUUID().toString().substring(0, 32));
                introduction.setFamilyPeopleId(familyVO.getFamilyMemberId());
                introduction.setHouseId(familyVO.getHouseId());
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
                //添加家庭成员与户主关系
                PeopleHouse house = new PeopleHouse();
                house.setId(UUID.randomUUID().toString().substring(0, 32));
                house.setFamilyPeopleId(familyVO.getFamilyMemberId());
                house.setHouseholderId(familyVO.getHouseId());
                house.setName(familyVO.getName());
                house.setRelationship(familyVO.getRelationship());
                house.setCreateTime(new Date());
                house.setCreateUser(familyVO.getLoginName());
                house.setUpdateTime(new Date());
                house.setUpdateUser(familyVO.getLoginName());
                house.setDelFlag(0);
                familyFeign.addPeopleHouse(house);
                //修改家庭人数
                Map<String, Object> params = new HashMap<>();
                params.put("id", familyVO.getHouseId());
                params.put("updateTime", new Date());
                params.put("updateUser", examineUser);
                houseFeign.addFamilyNumber(params);
                //修改审核
                updateExamine(id, 1, examineUser);
                //删缓存数据
                redisTemplate.delete(ADD_FAMILY + examine.getBabyorpeopleId());
                redisTemplate.delete(CHECK_FAMILY + familyVO.getName());
                message = "审核已通过";
            } else if (type == 2) { //删除家庭成员
                message = "审核已通过";
            }
        } else { //审核驳回
            if (type == 0) {   //添加新生儿
                message = "审核已被驳回";
            } else if (type == 1) { //添加家族成员
                //修改审核结果
                updateExamine(id, 2, examineUser);
                //删缓存数据
                redisTemplate.delete(ADD_FAMILY + examine.getBabyorpeopleId());
                redisTemplate.delete(CHECK_FAMILY + familyVO.getName());
                message = "审核已被驳回";
            } else if (type == 2) { //删除家庭成员
                message = "审核已被驳回";
            }
        }
        return message;
    }

    /**
     * 修改审核结果
     */
    private void updateExamine(String id, Integer status, String examineUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("status", status);
        params.put("time", new Date());
        params.put("examineUser", examineUser);
        examineDao.updateExamine(params);
    }
}
