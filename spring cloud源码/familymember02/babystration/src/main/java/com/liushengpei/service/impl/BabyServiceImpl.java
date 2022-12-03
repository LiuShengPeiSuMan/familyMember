package com.liushengpei.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushengpei.dao.BabySituationDao;
import com.liushengpei.feign.ExamineFeign;
import com.liushengpei.feign.FamilyMemberFeign;
import com.liushengpei.feign.HouseFeign;
import com.liushengpei.pojo.BabySituation;
import com.liushengpei.service.IBabyService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.domain.Examine;
import util.domain.FamilyVO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static util.constant.ConstantToolUtil.ADD_BABY;
import static util.constant.ConstantToolUtil.CHECK_BABY;

/**
 * 出生成员管理逻辑
 */
@Service
public class BabyServiceImpl implements IBabyService {

    @Autowired
    private BabySituationDao situationDao;
    @Autowired
    private ExamineFeign examineFeign;
    @Autowired
    private FamilyMemberFeign memberFeign;
    @Autowired
    private HouseFeign houseFeign;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 查询本年度出生成员
     */
    @Override
    public List<BabySituation> queryBaby() {
        List<BabySituation> babySituations = situationDao.queryBabyOrYear();
        return babySituations;
    }

    /**
     * 添加出生成员
     */
    @Override
    @GlobalTransactional
    public String addBaby(FamilyVO familyVO) {
        //判断有没有重复添加出生成员
        Object o = redisTemplate.opsForValue().get(CHECK_BABY + familyVO.getBabyName());
        if (o != null) {
            return "此出生成员已添加，待族长审核中！";
        }
        //查询数据库有没有此出生成员
        Integer babyCount = situationDao.babyCount(familyVO.getBabyName());
        if (babyCount > 0) {
            return "此出生成员已添加";
        }
        //查询家族成员id
        String familyMemberId = memberFeign.queryId(familyVO.getLoginName());
        //查询户主id
        String houseId = houseFeign.queryHouseId(familyMemberId);
        //添加出生成员
        String babyId = UUID.randomUUID().toString().substring(0, 32);
        BabySituation situation = new BabySituation();
        situation.setId(babyId);
        situation.setName(familyVO.getBabyName());
        situation.setSex(familyVO.getBabySex());
        situation.setHealthy(familyVO.getBabyHealthy());
        situation.setMother(familyVO.getBabyMother());
        situation.setFather(familyVO.getBabyFather());
        situation.setDateOfBirth(familyVO.getBabyDateOfBirth());
        situation.setCreateTime(new Date());
        situation.setCreateUser(familyVO.getLoginName());
        situation.setUpdateTime(new Date());
        situation.setUpdateUser(familyVO.getLoginName());
        situation.setDelFlag(0);
        //添加到缓存
        String s = null;
        try {
            s = mapper.writeValueAsString(situation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (s != null) {
            redisTemplate.opsForValue().set(ADD_BABY + babyId, s);
            redisTemplate.opsForValue().set(CHECK_BABY + familyVO.getBabyName(), babyId);
        }
        //添加一条审核记录
        Examine examine = new Examine();
        examine.setId(UUID.randomUUID().toString().substring(0, 32));
        examine.setBabyorpeopleId(babyId);
        examine.setHouseId(houseId);
        //审核类型添加出生成员
        examine.setExamineType(0);
        examine.setExamineStatus(0);
        examine.setReason(familyVO.getReason());
        examine.setExamineUser(null);
        examine.setExamineTime(null);
        examine.setSubmitUser(familyVO.getLoginName());
        examine.setCreateTime(new Date());
        examine.setDelFlag(0);
        examineFeign.addExamine(examine);
        return "添加成功,待族长审核!";
    }

    /**
     * 查询出生成员信息
     */
    @Override
    public BabySituation queryBabyDetailed(String id) {
        BabySituation babySituation = situationDao.queryBaby(id);
        return babySituation;
    }
}
