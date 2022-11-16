package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.NoticeDao;
import com.liushengpei.pojo.Notice;
import com.liushengpei.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 通知
 */
@Service
public class NoticeServiceImpl implements INoticeService {

    @Autowired
    private NoticeDao noticeDao;

    /**
     * 添加通知
     */
    @Override
    public String addNotice(Notice notice) {
        notice.setId(UUID.randomUUID().toString().substring(0, 32));
        notice.setNoticeTime(new Date());
        notice.setDelFlag(0);
        //添加通知
        Integer integer = noticeDao.addNotice(notice);
        if (integer == 1) {
            return "添加成功";
        }
        return "添加失败";
    }

    /**
     * 查询全部通知
     * */
    @Override
    public List<Notice> conditionAllNotice(Map<String,Object> params) {
        List<Notice> notices = noticeDao.conditionNotice(params);
        return notices;
    }
}
