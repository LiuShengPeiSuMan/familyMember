package com.liushengpei.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 户主
 */
public class HouseSituation implements Serializable {
    private static final long serialVersionUID = 458073728L;

    //户主id
    private String id;
    //户主在家族成员里的id
    private String familyPeopleId;
    //户主姓名
    private String name;
    //户主性别
    private Integer sex;
    //门牌号
    private Integer houseNumber;
    //家庭总人口
    private Integer peopleNumber;
    //死亡人口
    private Integer deadNumber;
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

    public String getFamilyPeopleId() {
        return familyPeopleId;
    }

    public void setFamilyPeopleId(String familyPeopleId) {
        this.familyPeopleId = familyPeopleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public Integer getDeadNumber() {
        return deadNumber;
    }

    public void setDeadNumber(Integer deadNumber) {
        this.deadNumber = deadNumber;
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
