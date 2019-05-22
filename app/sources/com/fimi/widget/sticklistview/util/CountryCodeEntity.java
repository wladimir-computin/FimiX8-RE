package com.fimi.widget.sticklistview.util;

import com.fimi.widget.sticklistview.BaseSortModel;

public class CountryCodeEntity extends BaseSortModel {
    private String code;
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
