package com.fimi.x8sdk.entity;

public class CurUpdateFwEntity {
    int devModuleId;
    int devTargetId;
    int msgModuleId;
    int result;
    int schedule;
    int state;

    public int getDevTargetId() {
        return this.devTargetId;
    }

    public void setDevTargetId(int devTargetId) {
        this.devTargetId = devTargetId;
    }

    public int getDevModuleId() {
        return this.devModuleId;
    }

    public void setDevModuleId(int devModuleId) {
        this.devModuleId = devModuleId;
    }

    public int getMsgModuleId() {
        return this.msgModuleId;
    }

    public void setMsgModuleId(int msgModuleId) {
        this.msgModuleId = msgModuleId;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSchedule() {
        return this.schedule;
    }

    public void setSchedule(int schedule) {
        this.schedule = schedule;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
