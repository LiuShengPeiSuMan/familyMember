package com.example.familymembermanagement.pojo;

import java.io.Serializable;

/**
 * 添加出生成员所需参数
 */
public class AddBaby implements Serializable {
    private static final Long serialVersionUID = 101010L;

    private String loginId;
    private String loginName;
    private String babyName;
    private Integer babySex;
    private Integer babyHealthy;
    private String babyMother;
    private String babyFather;
    private String babyDateOfBirth;
    private String reason;

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

    public String getBabyDateOfBirth() {
        return babyDateOfBirth;
    }

    public void setBabyDateOfBirth(String babyDateOfBirth) {
        this.babyDateOfBirth = babyDateOfBirth;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
