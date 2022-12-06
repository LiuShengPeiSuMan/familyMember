package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.NoticeDao;
import com.liushengpei.feign.FamilyMemberFeign;
import com.liushengpei.pojo.Notice;
import com.liushengpei.service.IHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.domain.FamilyMember;

import java.util.*;

/**
 * 首页
 */
@Service
public class HomePageServiceImpl implements IHomePageService {

    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private FamilyMemberFeign memberFeign;

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
        Map<String, Object> map = new HashMap<>();
        //审核数据
        List<Notice> notices = noticeDao.queryNoticeToday();
        if (notices.isEmpty()) {
            map.put("notice", null);
        } else {
            map.put("notice", notices);
        }
        //家族成员生日
        List<FamilyMember> dateOfBirth = memberFeign.queryDateOfBirth();
        if (dateOfBirth.isEmpty()) {
            map.put("dateofbirth", null);
        } else {
            map.put("dateofbirth", dateOfBirth);
        }
        return map;
    }
}
