package com.example.familymembermanagement.pojo;

/**
 * 修改家庭成员所传递参数
 */
public class UpdateFamily {
    private String id;
    private String phone;
    private String loginName;
    private String homeAddress;
    private Integer marriedOfNot;
    private Integer education;
    private String work;
    private String workAddress;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
