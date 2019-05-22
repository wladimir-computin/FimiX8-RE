package com.fimi.app.x8s.entity;

import java.io.Serializable;

public class PhotoParamItemEntity implements Serializable {
    private String nickName;
    private String nickParam;
    private String paramKey;
    private String paramValue;

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getParamKey() {
        return this.paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return this.paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getNickParam() {
        return this.nickParam;
    }

    public void setNickParam(String nickParam) {
        this.nickParam = nickParam;
    }
}
