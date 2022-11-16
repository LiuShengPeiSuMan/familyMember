package com.liushengpei.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知表
 * */
public class Notice implements Serializable {

    private static final long serialVersionUID = 4580731172087625L;

    //主键id
    private String id;
    //通知类型
    private Integer noticeType;
    //通知内容
    private String noticeContent;
    //通知时间
    private Date noticeTime;
    //删除标记
    private Integer delFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
