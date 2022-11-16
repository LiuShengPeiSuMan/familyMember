package com.example.familymembermanagement.pojo.returndata;

import com.example.familymembermanagement.pojo.FamilyMember;
import com.example.familymembermanagement.pojo.Notice;

import java.util.List;

/**
 * 通知和家族成员
 */
public class NoticeAndBirthday {

    //家族成员
    private List<FamilyMember> birthday;
    //通知
    private List<Notice> natice;

    public List<FamilyMember> getBirthday() {
        return birthday;
    }

    public void setBirthday(List<FamilyMember> birthday) {
        this.birthday = birthday;
    }

    public List<Notice> getNatice() {
        return natice;
    }

    public void setNatice(List<Notice> natice) {
        this.natice = natice;
    }
}
