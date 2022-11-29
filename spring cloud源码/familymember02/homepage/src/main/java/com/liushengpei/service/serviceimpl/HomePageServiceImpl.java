package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.NoticeDao;
import com.liushengpei.pojo.Notice;
import com.liushengpei.service.IHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 首页
 */
@Service
public class HomePageServiceImpl implements IHomePageService {

    @Autowired
    private NoticeDao noticeDao;

    /**
     * 添加通知
     */
    @Override
    public String addNotice(Integer type, String text) {
        //添加通知
        Notice notice = new Notice();
        notice.setId(UUID.randomUUID().toString().substring(0, 32));
        notice.setNoticeType(type);
        notice.setNoticeContent(text);
        notice.setNoticeTime(new Date());
        notice.setDelFlag(0);
        Integer integer = noticeDao.addNotice(notice);
        if (integer > 0) {
            return "添加成功";
        }
        return "添加失败";
    }

    /**
     * 查询首页数据
     */
    @Override
    public Map<String, Object> queryData() {
        return null;
    }
}
