package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.ExamineDao;
import com.liushengpei.pojo.Examine;
import com.liushengpei.service.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 户主提交审核记录
 */
@Service
public class HistoryServiceImpl implements IHistoryService {

    @Autowired
    private ExamineDao examineDao;

    /**
     * 查询审核历史记录
     */
    @Override
    public List<Examine> examineHistory(String name) {
        List<Examine> examines = examineDao.queryAllByName(name);
        return examines;
    }
}
