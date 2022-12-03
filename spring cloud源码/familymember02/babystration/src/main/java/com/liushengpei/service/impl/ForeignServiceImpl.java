package com.liushengpei.service.impl;

import com.liushengpei.dao.BabySituationDao;
import com.liushengpei.pojo.BabySituation;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 对外提供接口逻辑
 */
@Service
public class ForeignServiceImpl implements IForeignService {

    @Autowired
    private BabySituationDao situationDao;

    /**
     * 查询出生成员信息
     *
     * @param id 出生成员id
     */
    @Override
    public BabySituation queryBabySituation(String id) {
        BabySituation babySituation = situationDao.queryBaby(id);
        return babySituation;
    }

    /**
     * 添加出生成员
     *
     * @param situation 出生成员信息
     */
    @Override
    public String addBaby(BabySituation situation) {
        Integer integer = situationDao.insertBaby(situation);
        if (integer > 0) {
            return "添加成功";
        }
        return "添加失败";
    }
}
