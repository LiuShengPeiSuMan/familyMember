package com.liushengpei.service.impl;

import com.liushengpei.dao.HouseDao;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Integer addFamilyPeopleNum(Map<String,Object> params) {
        Integer count = houseDao.updatePeopleNum(params);
        return count;
    }
}
