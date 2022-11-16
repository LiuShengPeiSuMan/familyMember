package com.liushengpei.pojo;

import java.io.Serializable;

/**
 * 邮件实体类
 */
public class EmailSendParam implements Serializable {
    private static final long serialVersionUID = 458073098117262568L;

    //登录邮箱
    private String loginEmail;
    //收件人地址
    private String to;
    //邮箱标题
    private String subject;
    //邮箱内容
    private String text;
    //附件路径
    private String filePath;
    //静态资源唯一id
    private String rscId;
    //静态资源路径
    private String rscPath;

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRscId() {
        return rscId;
    }

    public void setRscId(String rscId) {
        this.rscId = rscId;
    }

    public String getRscPath() {
        return rscPath;
    }

    public void setRscPath(String rscPath) {
        this.rscPath = rscPath;
    }
}
