package com.liushengpei.service.impl;

import com.liushengpei.dao.HouseDao;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 对外提供接口逻辑
 */
@Service
public class ForeignServiceImpl implements IForeignService {

    @Autowired
    private HouseDao houseDao;

    /**
     * 添加户主家庭人口总数量
     */
    @Override
    public Integer addFamilyPeopleNum(Map<String, Object> params) {
        Integer count = houseDao.updatePeopleNum(params);
        return count;
    }

    /**
     * 减少家庭成员数量
     */
    @Override
    public String reduceFamilyNum(Map<String, Object> params) {
        params.put("updateTime", new Date());
        //查询户主id

        Integer num = houseDao.reduceFamilyNum(params);
        if (num > 0) {
            return "减少成功";
        }
        return "减少失败";
    }

    /**
     * 根据家族成员id查询户主id
     */
    @Override
    public String queryHouseId(String familyPeopleId) {
        String houseId = houseDao.queryHouseId(familyPeopleId);
        return houseId;
    }

    /**
     * 根据名称查询户主id
     */
    @Override
    public String queryHouseIdByName(String name) {
        String houseId = houseDao.queryHouseIdByName(name);
        return houseId;
    }
}
