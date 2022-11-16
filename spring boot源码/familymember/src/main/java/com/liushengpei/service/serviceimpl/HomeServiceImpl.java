package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.FamilyMemberDao;
import com.liushengpei.dao.NoticeDao;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.Notice;
import com.liushengpei.service.IHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 首页
 */
@Service
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private FamilyMemberDao familyMemberDao;

    /**
     * 首页数据查询
     */
    @Override
    public Map<String, Object> nowDayNotice() {
        //保存数据
        Map<String, Object> data = new HashMap<>();
        //当天通知
        List<Notice> notices = noticeDao.nowDayNotice();
        //查询当天生日家族成员
        List<FamilyMember> nowDay = familyMemberDao.selectNowDay();
        //如果当天有数据就返回当天数据，否则返回之后7天之内的第一个家族成员
        if (!nowDay.isEmpty()) {
            data.put("birthday", nowDay);
        } else {
            //查询7天内过生日的家族成员
            List<FamilyMember> familyMemberList = familyMemberDao.peopleBirthday();
            data.put("birthday", familyMemberList);
        }
        data.put("natice", notices);
        return data;
    }

}
