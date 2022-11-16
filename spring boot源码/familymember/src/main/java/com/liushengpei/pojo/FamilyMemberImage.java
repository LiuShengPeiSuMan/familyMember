package com.liushengpei.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 家族成员头像
 */
public class FamilyMemberImage implements Serializable {

    private static final long serialVersionUID = 568L;

    //主键id
    private String id;
    //家族成员id
    private String familyMemberId;
    //家族成员头像
    private byte[] image;
    //创建时间
    private Date createTime;
    //创建人
    private String createUser;
    //修改时间
    private Date updateTime;
    //修改人
    private String updateUser;
    //删除标记
    private Integer delFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(String familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
