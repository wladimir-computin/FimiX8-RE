package com.fimi.app.x8s.entity;

public class X8RestSystemParamResult {
    private boolean appResult;
    private boolean fcResult;
    private boolean gcResult;
    private boolean rcResult;

    public boolean isAppResult() {
        return this.appResult;
    }

    public void setAppResult(boolean appResult) {
        this.appResult = appResult;
    }

    public boolean isFcResult() {
        return this.fcResult;
    }

    public void setFcResult(boolean fcResult) {
        this.fcResult = fcResult;
    }

    public boolean isRcResult() {
        return this.rcResult;
    }

    public void setRcResult(boolean rcResult) {
        this.rcResult = rcResult;
    }

    public boolean isGcResult() {
        return this.gcResult;
    }

    public void setGcResult(boolean gcResult) {
        this.gcResult = gcResult;
    }

    public void init() {
        this.appResult = false;
        this.fcResult = false;
        this.rcResult = false;
        this.gcResult = false;
    }
}
