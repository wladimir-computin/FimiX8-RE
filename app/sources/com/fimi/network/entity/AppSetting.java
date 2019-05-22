package com.fimi.network.entity;

import ch.qos.logback.core.CoreConstants;

public class AppSetting extends BaseModel {
    String key;
    String value;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "AppSetting{key='" + this.key + CoreConstants.SINGLE_QUOTE_CHAR + ", value='" + this.value + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }
}
