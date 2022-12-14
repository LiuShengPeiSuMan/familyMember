package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.ExamineDao;
import com.liushengpei.pojo.Examine;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 对外开放的接口
 */
@Service
public class ForeignServiceImpl implements IForeignService {

    @Autowired
    private ExamineDao examineDao;

    /**
     * 添加审核记录
     */
    @Override
    public Integer addExamine(Examine examine) {
        Integer integer = examineDao.addExamine(examine);
        return integer;
    }

    /**
     * 判断删除家族成员有没有重复删除同一个人
     */
    @Override
    public Integer countPeopleExamine(String familyPeopleId) {
        Integer count = examineDao.countExamine(familyPeopleId);
        return count;
    }
}
