package com.liushengpei.pojo.domainvo;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 封装参数值
 */
public class FamilyMemberUtilVO implements Serializable {

    private static final long serialVersionUID = 25098768L;

    //户主id
    private String houseId;
    //家庭成员id
    private String familyMemberId;
    //死亡人口数
    private Integer deadNumber;
    //门牌号
    private Integer houseNumber;
    //家庭总人口
    private Integer peopleNumber;
    //文件
    private MultipartFile file;
    //头像
    private byte[] image;
    //审核状态
    private Integer status;
    //审核类型
    private Integer type;
    //登录人id
    private String loginId;
    //登陆人名称
    private String loginName;
    //家庭成员与户主关系
    private String relationship;
    //原由
    private String reason;
    //主键id
    private String id;
    //姓名
    private String name;
    //年龄
    private Integer age;
    //性别
    private Integer sex;
    //家庭住址
    private String homeAddress;
    //出生日期
    private Date dateOfBirth;
    //死亡日期
    private Date dateOfDeath;
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
    //主键id
    private String babyId;
    //婴儿姓名
    private String babyName;
    //性别
    private Integer babySex;
    //是否健康
    private Integer babyHealthy;
    //母亲
    private String babyMother;
    //父亲
    private String babyFather;
    //出生日期
    private Date babyDateOfBirth;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //创建人
    private String createUser;
    //更新人
    private String updateUser;
    //删除标识
    private Integer delFlag;

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(String familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public Integer getDeadNumber() {
        return deadNumber;
    }

    public void setDeadNumber(Integer deadNumber) {
        this.deadNumber = deadNumber;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public Integer getBabySex() {
        return babySex;
    }

    public void setBabySex(Integer babySex) {
        this.babySex = babySex;
    }

    public Integer getBabyHealthy() {
        return babyHealthy;
    }

    public void setBabyHealthy(Integer babyHealthy) {
        this.babyHealthy = babyHealthy;
    }

    public String getBabyMother() {
        return babyMother;
    }

    public void setBabyMother(String babyMother) {
        this.babyMother = babyMother;
    }

    public String getBabyFather() {
        return babyFather;
    }

    public void setBabyFather(String babyFather) {
        this.babyFather = babyFather;
    }

    public Date getBabyDateOfBirth() {
        return babyDateOfBirth;
    }

    public void setBabyDateOfBirth(Date babyDateOfBirth) {
        this.babyDateOfBirth = babyDateOfBirth;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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
