package com.example.familymembermanagement.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 新生儿
 */
public class BabySituation implements Serializable {

    private static final Long serialVersionUID = 1L;

    //主键id
    private String id;
    //婴儿姓名
    private String name;
    //性别
    private Integer sex;
    //是否健康
    private Integer healthy;
    //母亲
    private String mother;
    //父亲
    private String father;
    //出生日期
    private String dateOfBirth;
    //创建时间
    private String createTime;
    //创建人
    private String createUser;
    //更新时间
    private String updateTime;
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

    public Integer getHealthy() {
        return healthy;
    }

    public void setHealthy(Integer healthy) {
        this.healthy = healthy;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
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
