package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.*;
import com.liushengpei.pojo.*;
import com.liushengpei.service.IExamineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 审核
 */
@SuppressWarnings("all")
@Service
public class ExamineServiceImpl implements IExamineService {

    @Autowired
    private ExamineDao examineDao;
    @Autowired
    private BabySituationDao babySituationDao;
    @Autowired
    private FamilyMemberDao familyMemberDao;
    @Autowired
    private FamilyBriefIntroductionDao introductionDao;
    @Autowired
    private PeopleHouseDao peopleHouseDao;
    @Autowired
    private HouseSituationDao situationDao;

    /**
     * 查询审核数据
     */
    @Override
    public List<Examine> examineAll() {
        List<Examine> examines = examineDao.selectAll();
        return examines;
    }

    /**
     * 详细信息
     */
    @Override
    public Object detailedData(String id, Integer type) {

        //新生儿
        if (type == 0) {
            BabySituation babySituation = babySituationDao.detailedBabyById(id);
            return babySituation;
        }
        //家族成员详细信息
        FamilyMember familyMember = familyMemberDao.peopleDetailedById(id);
        return familyMember;
    }

    /**
     * 审核结果
     */
    @Transactional
    @Override
    public Integer updateResult(Integer status, String id, Integer type, String examineUser) {
        String peopleOrBabyId = examineDao.queryBabyOrPeopleId(id);
        if (peopleOrBabyId == null || peopleOrBabyId.equals("")) {
            return 0;
        }
        //驳回
        if (status == 2) {
            switch (type) {
                case 0: //添加新生儿
                    //审核驳回
                    Map<String, Object> params = new HashMap<>();
                    params.put("examineStatus", status);
                    params.put("examineTime", new Date());
                    params.put("examineUser", examineUser);
                    params.put("id", id);
                    examineDao.updateRsult(params);
                    break;
                case 1: //添加家族成员
                    //删除家族成员详细信息
                    familyMemberDao.delFamilyMember02(peopleOrBabyId);
                    //删除家庭成员简介
                    introductionDao.delDelFlag(peopleOrBabyId);
                    //删除家庭成员与户主关系
                    peopleHouseDao.delPeopleHouse02(peopleOrBabyId);
                    //审核驳回
                    Map<String, Object> addpeople = new HashMap<>();
                    addpeople.put("examineStatus", status);
                    addpeople.put("examineTime", new Date());
                    addpeople.put("examineUser", examineUser);
                    addpeople.put("id", id);
                    examineDao.updateRsult(addpeople);
                    break;
                case 2: //删除家族成员
                    examineStatus(status, id, peopleOrBabyId);
                    break;
            }
        } else {    //通过
            switch (type) {
                case 0: //新生儿
                    //审核通过
                    Map<String, Object> baby = new HashMap<>();
                    baby.put("examineStatus", status);
                    baby.put("examineTime", new Date());
                    baby.put("examineUser", examineUser);
                    baby.put("id", id);
                    examineDao.updateRsult(baby);
                    break;
                case 1: //添加家庭成员
                    FamilyMember familyMember = familyMemberDao.peopleDetailedById(peopleOrBabyId);
                    //查询户主id
                    String huZhuId = peopleHouseDao.queryHouseId(peopleOrBabyId);
                    //添加家庭成员数量
                    Map<String, Object> parameter = new HashMap<>();
                    parameter.put("updateTime", new Date());
                    parameter.put("updateUser", familyMember.getName());
                    parameter.put("id", huZhuId);
                    situationDao.addCountPeople(parameter);
                    //审核通过
                    Map<String, Object> params = new HashMap<>();
                    params.put("examineStatus", status);
                    params.put("examineTime", new Date());
                    params.put("examineUser", examineUser);
                    params.put("id", id);
                    examineDao.updateRsult(params);
                    break;
                case 2: //删除家庭成员
                    examineStatus(status, id, peopleOrBabyId);
                    break;
            }
        }
        return 1;
    }

    /**
     * 审核记录
     */
    @Override
    public List<Examine> Record(Integer status) {
        List<Examine> examinerecord = examineDao.examinerecord(status);
        return examinerecord;
    }

    /**
     * 删除历史记录
     */
    @Override
    public String delExamine(List<String> ids) {
        Integer integer = examineDao.delExamine(ids);
        if (integer > 0) {
            return "删除成功";
        }
        return "删除失败";
    }

    //删除
    public void examineStatus(Integer status, String id, String peopleOrBabyId) {
        //审核通过
        if (status == 1) {
            //修改审核记录
            Map<String, Object> params = new HashMap<>();
            params.put("examineStatus", status);
            params.put("examineTime", new Date());
            params.put("id", id);
            examineDao.updateRsult(params);
            //修改家庭总人口数
            FamilyMember onefamily = familyMemberDao.peopleDetailedById(peopleOrBabyId);
            if (onefamily != null) {
                Map<String, Object> parameter = new HashMap<>();
                parameter.put("updateTime", new Date());
                parameter.put("updateUser", onefamily.getName());
                parameter.put("id", peopleOrBabyId);
                situationDao.updateCountPeople(parameter);
            }
            //删除家庭成员简介表
            introductionDao.deleteDelFlag(peopleOrBabyId);
            //删除家庭成员详细信息
            familyMemberDao.delFamilyMember(peopleOrBabyId);
            //删除家庭成员与户主关系
            peopleHouseDao.delPeopleHouse(peopleOrBabyId);
        } else { //驳回
            //修改审核记录
            Map<String, Object> params = new HashMap<>();
            params.put("examineStatus", status);
            params.put("examineTime", new Date());
            params.put("id", id);
            examineDao.updateRsult(params);
        }
    }

}
