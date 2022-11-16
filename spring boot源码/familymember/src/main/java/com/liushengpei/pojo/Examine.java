package com.liushengpei.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核表
 */
public class Examine implements Serializable {

    private static final long serialVersionUID = 206800L;

    //主键id
    private String id;
    //家族成员id
    private String babyOrpeopleId;
    //户主id
    private String houseId;
    //审核类型
    private Integer examineType;
    //审核状态
    private Integer examineStatus;
    //原由
    private String reason;
    //审核人
    private String examineUser;
    //审核时间
    private Date examineTime;
    //提交审核人
    private String submitUser;
    //创建时间
    private Date createTime;
    //删除标记
    private Integer delFlag;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getExamineTime() {
        return examineTime;
    }

    public void setExamineTime(Date examineTime) {
        this.examineTime = examineTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBabyOrpeopleId() {
        return babyOrpeopleId;
    }

    public void setBabyOrpeopleId(String babyOrpeopleId) {
        this.babyOrpeopleId = babyOrpeopleId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public Integer getExamineType() {
        return examineType;
    }

    public void setExamineType(Integer examineType) {
        this.examineType = examineType;
    }

    public Integer getExamineStatus() {
        return examineStatus;
    }

    public void setExamineStatus(Integer examineStatus) {
        this.examineStatus = examineStatus;
    }

    public String getExamineUser() {
        return examineUser;
    }

    public void setExamineUser(String examineUser) {
        this.examineUser = examineUser;
    }

    public String getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
