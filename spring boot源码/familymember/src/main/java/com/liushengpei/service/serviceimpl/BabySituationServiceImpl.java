package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.BabySituationDao;
import com.liushengpei.dao.ExamineDao;
import com.liushengpei.pojo.BabySituation;
import com.liushengpei.pojo.Examine;
import com.liushengpei.pojo.domainvo.FamilyMemberUtilVO;
import com.liushengpei.service.IBabySituationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 新生儿
 */
@Service
public class BabySituationServiceImpl implements IBabySituationService {

    @Autowired
    private BabySituationDao babySituationDao;
    @Autowired
    private ExamineDao examineDao;

    /**
     * 添加新生儿
     */
    @Transactional
    @Override
    public String addBaby(FamilyMemberUtilVO memberUtilVO) {
        //判断有没有重复添加
        Map<String, String> params = new HashMap<>();
        params.put("name", memberUtilVO.getBabyName());
        params.put("mother", memberUtilVO.getBabyMother());
        params.put("father", memberUtilVO.getBabyFather());
        Integer num = babySituationDao.bmfCount(params);
        if (num > 0) {
            return "此出生成员已添加过,请重新添加";
        }
        //添加新生儿
        BabySituation babySituation = new BabySituation();
        String id = UUID.randomUUID().toString().substring(0, 32);
        babySituation.setId(id);
        babySituation.setName(memberUtilVO.getBabyName());
        babySituation.setSex(memberUtilVO.getBabySex());
        babySituation.setHealthy(memberUtilVO.getBabyHealthy());
        babySituation.setMother(memberUtilVO.getBabyMother());
        babySituation.setFather(memberUtilVO.getBabyFather());
        babySituation.setDateOfBirth(memberUtilVO.getBabyDateOfBirth());
        babySituation.setCreateTime(new Date());
        babySituation.setCreateUser(memberUtilVO.getLoginName());
        babySituation.setUpdateTime(new Date());
        babySituation.setUpdateUser(memberUtilVO.getLoginName());
        babySituation.setDelFlag(0);
        Integer integer = babySituationDao.addBaby(babySituation);
        //添加审核
        Examine examine = new Examine();
        examine.setId(UUID.randomUUID().toString().substring(0, 32));
        //新生儿或家族成员id
        examine.setBabyOrpeopleId(id);
        //户主id
        examine.setHouseId(memberUtilVO.getLoginId());
        //审核类型--新生儿审核
        examine.setExamineType(0);
        examine.setReason(memberUtilVO.getReason());
        //待审核
        examine.setExamineStatus(0);
        //审核人
        examine.setExamineUser(null);
        //审核时间
        examine.setExamineTime(new Date());
        //提交人
        examine.setSubmitUser(memberUtilVO.getLoginName());
        examine.setCreateTime(new Date());
        examine.setDelFlag(0);
        examineDao.addExamine(examine);
        return "添加成功,待族长审核！";
    }

    /**
     * 本年度新生儿
     */
    @Override
    public List<BabySituation> yearBaby() {
        List<BabySituation> babySituations = babySituationDao.yearNewBaby();
        return babySituations;
    }

    /**
     * 查询出生成员详细信息
     */
    @Override
    public BabySituation detailedBaby(String id) {
        BabySituation babySituation = babySituationDao.detailedBabyById(id);
        return babySituation;
    }
}
