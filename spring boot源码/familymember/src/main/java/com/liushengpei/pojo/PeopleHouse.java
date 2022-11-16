package com.liushengpei.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 家庭成员与户主关系
 */
public class PeopleHouse implements Serializable {

    private static final long serialVersionUID = 250123498768L;

    //主键id
    private String id;
    //户主id
    private String householderId;
    //家庭成员id
    private String familyPeopleId;
    //家庭成员姓名
    private String name;
    //家庭成员与户主关系
    private String relationship;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHouseholderId() {
        return householderId;
    }

    public void setHouseholderId(String householderId) {
        this.householderId = householderId;
    }

    public String getFamilyPeopleId() {
        return familyPeopleId;
    }

    public void setFamilyPeopleId(String familyPeopleId) {
        this.familyPeopleId = familyPeopleId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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
