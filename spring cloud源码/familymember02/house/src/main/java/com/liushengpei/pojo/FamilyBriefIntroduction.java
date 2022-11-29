package com.liushengpei.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 家庭成员简介
 */
public class FamilyBriefIntroduction implements Serializable {
    private static final Long serialVersionUID = 14005L;

    //主键id
    private String id;
    //家族成员id
    private String familyPeopleId;
    //户主id
    private String houseId;
    //名称
    private String name;
    //年龄
    private Integer age;
    //性别
    private Integer sex;
    //电话
    private String phone;
    //是否已婚
    private Integer isMarry;
    //创建时间
    private Date createTime;
    //创建人
    private String createUser;
    //更新时间
    private Date updateTime;
    //更新人
    private String updateUser;
    //删除标记
    private Integer delFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyPeopleId() {
        return familyPeopleId;
    }

    public void setFamilyPeopleId(String familyPeopleId) {
        this.familyPeopleId = familyPeopleId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getIsMarry() {
        return isMarry;
    }

    public void setIsMarry(Integer isMarry) {
        this.isMarry = isMarry;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
