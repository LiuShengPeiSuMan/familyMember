package com.liushengpei.service.impl;

import com.liushengpei.dao.FamilyBriefIntroductionDao;
import com.liushengpei.dao.PeopleHouseDao;
import com.liushengpei.pojo.FamilyBriefIntroduction;
import com.liushengpei.pojo.PeopleHouse;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 对位提供接口逻辑
 */
@Service
public class ForeignServiceImpl implements IForeignService {

    @Autowired
    private FamilyBriefIntroductionDao introductionDao;

    @Autowired
    private PeopleHouseDao houseDao;

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
     * 删除家庭成员简介
     */
    @Override
    public String delIntroduction(Map<String, Object> params) {
        params.put("updateTime", new Date());
        Integer num = introductionDao.delIntroduction(params);
        if (num > 0) {
            return "删除成功";
        }
        return "删除失败";
    }

    /**
     * 删除家族成员与户主关系
     */
    @Override
    public String delPeopleHouse(Map<String, Object> params) {
        params.put("updateTime", new Date());
        Integer num = houseDao.delPeopleHouse(params);
        if (num > 0) {
            return "删除成功";
        }
        return "删除失败";
    }

    /**
     * 查询户主id
     */
    @Override
    public String queryHouseId(String id) {
        String houseId = houseDao.selectHouseId(id);
        return houseId;
    }
}
