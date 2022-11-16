package com.example.familymembermanagement.pojo;

import java.io.Serializable;

/**
 * 添加家庭成员
 */
public class AddFamily implements Serializable {

    private static final Long serialVersionUID = 10001010L;
    //登录人id
    private String loginId;
    //登录人姓名
    private String loginName;
    //与户主关系
    private String relationship;
    //添加原因
    private String reason;
    //姓名
    private String name;
    //年龄
    private Integer age;
    //性别
    private Integer sex;
    //家庭住址
    private String homeAddress;
    //出生日期
    private String dateOfBirth;
    //死亡日期
    private String dateOfDeath;
    //是否已婚
    private Integer marriedOfNot;
    //学历
    private Integer education;
    //工作
    private String work;
    //工作地址
    private String workAddress;
    //电话
    private String phone;
    //邮箱
    private String email;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public Integer getMarriedOfNot() {
        return marriedOfNot;
    }

    public void setMarriedOfNot(Integer marriedOfNot) {
        this.marriedOfNot = marriedOfNot;
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
