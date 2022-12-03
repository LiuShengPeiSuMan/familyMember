package com.liushengpei.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushengpei.dao.ExamineDao;
import com.liushengpei.feign.BabyFeign;
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
import util.domain.*;

import java.util.*;

import static util.constant.ConstantToolUtil.*;

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
    private BabyFeign babyFeign;
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
     *
     * @param id   家族成员id或出生成员id->（babyOrPeopleId）
     * @param type 审核类型
     */
    @Override
    public Object queryExamine(String id, Integer type) {
        //判断审核类型
        if (type == 0) {   //添加新生儿
            //查询出生成员信息
            //BabySituation babySituation = babyFeign.queryBaby(id);
            String baby = (String) redisTemplate.opsForValue().get(ADD_BABY + id);
            BabySituation babySituation = JSONObject.parseObject(baby, BabySituation.class);
            return babySituation;
        } else if (type == 1) { //添加家庭成员
            //查询缓存
            String familyvo = (String) redisTemplate.opsForValue().get(ADD_FAMILY + id);
            //序列化成对象
            FamilyVO familyVO = JSONObject.parseObject(familyvo, FamilyVO.class);
            System.err.println("======familyvo=======" + familyvo);
            return familyVO;
        } else if (type == 2) { //删除家庭成员
            //查询要删除的家庭成员信息
            FamilyMember member = memberFeign.queryInformation(id);
            return member;
        }
        return null;
    }

    /**
     * 通过或驳回
     *
     * @param id          审核id
     * @param type        审核类型（0，添加出生成员，1，添加家庭成员，2，删除家庭成员）
     * @param status      审核状态（0，待审核，1，审核通过，2，审核驳回）
     * @param examineUser 审核人 （登录人姓名，loginName）
     */
    @GlobalTransactional
    @Override
    public String adoptAndReject(String id, Integer type, Integer status, String examineUser) {
        //查询审核信息
        Examine examine = examineDao.examine(id);
        String message = "";
        if (status == 1) {  //审核通过
            if (type == 0) {   //添加新生儿
                //查询出生成员缓存
                String baby = (String) redisTemplate.opsForValue().get(ADD_BABY + examine.getBabyorpeopleId());
                BabySituation babySituation = JSONObject.parseObject(baby, BabySituation.class);
                if (babySituation != null) {
                    //添加出生成员
                    babyFeign.addBabySitatus(babySituation);
                    //删除缓存数据
                    redisTemplate.delete(ADD_BABY + babySituation.getId());
                    redisTemplate.delete(CHECK_BABY + babySituation.getName());
                }
                //修改审核结果
                updateExamine(examine.getId(), 1, examineUser);
                message = "审核已通过";
            } else if (type == 1) { //添加家族成员
                String e = (String) redisTemplate.opsForValue().get(ADD_FAMILY + examine.getBabyorpeopleId());
                //序列化对象
                FamilyVO familyVO = JSONObject.parseObject(e, FamilyVO.class);
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
                //查询家族成员id
                String familyMemberId = examineDao.queryFamilyMemberId(id);
                //查询户主id
                String houseId = familyFeign.houseId(familyMemberId);
                //删除家族成员
                Map<String, Object> delFamilyMember = new HashMap<>();
                delFamilyMember.put("id", familyMemberId);
                delFamilyMember.put("updateUser", examineUser);
                memberFeign.delFamilyMember(delFamilyMember);
                //删除家庭成员简介
                Map<String, Object> delFamily = new HashMap<>();
                delFamily.put("familyPeopleId", familyMemberId);
                delFamily.put("updateUser", examineUser);
                familyFeign.delIntroduction(delFamily);
                //删除家族成员与户主关系
                Map<String, Object> delPeopleHouse = new HashMap<>();
                delPeopleHouse.put("familyPeopleId", familyMemberId);
                delPeopleHouse.put("updateUser", examineUser);
                familyFeign.delPeopleHouse(delPeopleHouse);
                //修改户主家庭人口数（-1）
                Map<String, Object> reduce = new HashMap<>();
                reduce.put("id", houseId);
                reduce.put("updateUser", examineUser);
                houseFeign.reduceFamilyNum(reduce);
                //修改审核结果
                updateExamine(id, 1, examineUser);
                message = "审核已通过";
            }
        } else { //审核驳回
            if (type == 0) {   //添加新生儿
                //查询出生成员缓存
                String baby = (String) redisTemplate.opsForValue().get(ADD_BABY + examine.getBabyorpeopleId());
                BabySituation babySituation = JSONObject.parseObject(baby, BabySituation.class);
                //删除缓存
                redisTemplate.delete(ADD_BABY + examine.getBabyorpeopleId());
                redisTemplate.delete(CHECK_BABY + babySituation.getName());
                //修改审核结果
                updateExamine(examine.getId(), 2, examineUser);
                message = "审核已被驳回";
            } else if (type == 1) { //添加家族成员
                String e = (String) redisTemplate.opsForValue().get(ADD_FAMILY + examine.getBabyorpeopleId());
                //序列化对象
                FamilyVO familyVO = JSONObject.parseObject(e, FamilyVO.class);
                //修改审核结果
                updateExamine(id, 2, examineUser);
                //删缓存数据
                redisTemplate.delete(ADD_FAMILY + examine.getBabyorpeopleId());
                redisTemplate.delete(CHECK_FAMILY + familyVO.getName());
                message = "审核已被驳回";
            } else if (type == 2) { //删除家庭成员
                //修改审核记录
                updateExamine(id, 2, examineUser);
                message = "审核已被驳回";
            }
        }
        return message;
    }

    /**
     * 修改审核结果
     *
     * @param id          审核id
     * @param status      审核状态
     * @param examineUser 审核人，登录人
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
