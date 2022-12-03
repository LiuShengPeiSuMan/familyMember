package com.liushengpei.pojo;

import java.io.Serializable;

/**
 * 修改家庭成员所需参数
 */
public class UpdateFamilyDetailed implements Serializable {
    private static final long serialVersionUID = 250128768L;

    private String familyPeopleId;
    //登录人姓名
    private String loginName;
    //是否已婚
    private Integer isMarry;
    //家庭住址
    private String homeAddress;
    //学历
    private Integer education;
    //工作
    private String work;
    //工作地址
    private String workAddress;
    //电话
    private String phone;
    //电子邮箱
    private String email;

    public String getFamilyPeopleId() {
        return familyPeopleId;
    }

    public void setFamilyPeopleId(String familyPeopleId) {
        this.familyPeopleId = familyPeopleId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getIsMarry() {
        return isMarry;
    }

    public void setIsMarry(Integer isMarry) {
        this.isMarry = isMarry;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
