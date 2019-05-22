package com.fimi.app.x8s.entity;

import ch.qos.logback.core.CoreConstants;
import com.fimi.app.x8s.enums.X8ErrorCodeEnum;

public class X8ErrorCode {
    private int bgId;
    private String detail;
    private boolean isShow;
    private boolean isSpeak;
    private X8ErrorCodeEnum level;
    private String speakStr;
    private String title;
    private int titleImgId;

    public boolean isShow() {
        return this.isShow;
    }

    public void setShow(boolean show) {
        this.isShow = show;
    }

    public boolean isSpeak() {
        return this.isSpeak;
    }

    public void setSpeak(boolean speak) {
        this.isSpeak = speak;
    }

    public int getBgId() {
        return this.bgId;
    }

    public void setBgId(int bgId) {
        this.bgId = bgId;
    }

    public int getTitleImgId() {
        return this.titleImgId;
    }

    public void setTitleImgId(int titleImgId) {
        this.titleImgId = titleImgId;
    }

    public X8ErrorCodeEnum getLevel() {
        return this.level;
    }

    public void setLevel(X8ErrorCodeEnum level) {
        this.level = level;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSpeakStr() {
        return this.speakStr;
    }

    public void setSpeakStr(String speakStr) {
        this.speakStr = speakStr;
    }

    public String toString() {
        return "X8ErrorCode{level=" + this.level + ", title='" + this.title + CoreConstants.SINGLE_QUOTE_CHAR + ", detail='" + this.detail + CoreConstants.SINGLE_QUOTE_CHAR + ", bgId=" + this.bgId + ", titleImgId=" + this.titleImgId + ", speakStr='" + this.speakStr + CoreConstants.SINGLE_QUOTE_CHAR + ", isShow=" + this.isShow + CoreConstants.CURLY_RIGHT;
    }
}
