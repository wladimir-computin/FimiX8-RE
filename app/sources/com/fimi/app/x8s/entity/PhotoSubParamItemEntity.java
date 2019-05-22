package com.fimi.app.x8s.entity;

import ch.qos.logback.core.CoreConstants;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class PhotoSubParamItemEntity implements Serializable {
    private boolean iselected = false;
    private LinkedHashMap<String, String> optionMap = new LinkedHashMap();
    private List<String> options;
    private String paramKey;
    private String paramValue;
    private String titleName;

    public String getTitleName() {
        return this.titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
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

    public List<String> getOptions() {
        return this.options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public LinkedHashMap<String, String> getOptionMap() {
        return this.optionMap;
    }

    public void setOptionMap(LinkedHashMap<String, String> optionMap) {
        this.optionMap = optionMap;
    }

    public boolean isIselected() {
        return this.iselected;
    }

    public void setIselected(boolean iselected) {
        this.iselected = iselected;
    }

    public String toString() {
        return "PhotoSubParamItemEntity{paramKey='" + this.paramKey + CoreConstants.SINGLE_QUOTE_CHAR + ", paramValue='" + this.paramValue + CoreConstants.SINGLE_QUOTE_CHAR + ", options=" + this.options + ", titleName='" + this.titleName + CoreConstants.SINGLE_QUOTE_CHAR + ", optionMap=" + this.optionMap.toString() + ", iselected=" + this.iselected + CoreConstants.CURLY_RIGHT;
    }
}
