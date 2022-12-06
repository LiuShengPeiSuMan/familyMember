package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.NoticeDao;
import com.liushengpei.pojo.Notice;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对外提供接口
 */
@Service
public class ForeignServiceImpl implements IForeignService {

    @Autowired
    private NoticeDao noticeDao;

    /**
     * 查询当天所有通知
     */
    @Override
    public List<Notice> noticeList() {
        List<Notice> notices = noticeDao.queryNoticeToday();
        return notices;
    }
}
