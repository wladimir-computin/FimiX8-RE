package com.fimi.network.entity;

import ch.qos.logback.core.CoreConstants;
import java.util.Date;

public class UserDto extends BaseModel {
    private Date createTime;
    private String email;
    private String fimiId;
    private String name;
    private String nickName;
    private String phone;
    private String status;
    private String thirdId;
    private String userImgUrl;

    public String getFimiId() {
        return this.fimiId;
    }

    public void setFimiId(String fimiId) {
        this.fimiId = fimiId;
    }

    public String getThirdId() {
        return this.thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserImgUrl() {
        return this.userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "UserDto{fimiId='" + this.fimiId + CoreConstants.SINGLE_QUOTE_CHAR + ", thirdId='" + this.thirdId + CoreConstants.SINGLE_QUOTE_CHAR + ", nickName='" + this.nickName + CoreConstants.SINGLE_QUOTE_CHAR + ", name='" + this.name + CoreConstants.SINGLE_QUOTE_CHAR + ", email='" + this.email + CoreConstants.SINGLE_QUOTE_CHAR + ", phone='" + this.phone + CoreConstants.SINGLE_QUOTE_CHAR + ", userImgUrl='" + this.userImgUrl + CoreConstants.SINGLE_QUOTE_CHAR + ", status='" + this.status + CoreConstants.SINGLE_QUOTE_CHAR + ", createTime=" + this.createTime + CoreConstants.CURLY_RIGHT;
    }
}
