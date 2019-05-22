package com.fimi.x8sdk.entity;

import ch.qos.logback.core.CoreConstants;

public class UpdateCurrentProgressEntity {
    int devModuleID;
    int devTargetID;
    int msgModuleID;
    int result;
    int schedule;
    int stage;

    public int getDevTargetID() {
        return this.devTargetID;
    }

    public void setDevTargetID(int devTargetID) {
        this.devTargetID = devTargetID;
    }

    public int getDevModuleID() {
        return this.devModuleID;
    }

    public void setDevModuleID(int devModuleID) {
        this.devModuleID = devModuleID;
    }

    public int getMsgModuleID() {
        return this.msgModuleID;
    }

    public void setMsgModuleID(int msgModuleID) {
        this.msgModuleID = msgModuleID;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
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

    public String toString() {
        return "UpdateCurrentProgressEntity{devTargetID=" + this.devTargetID + ", devModuleID=" + this.devModuleID + ", msgModuleID=" + this.msgModuleID + ", stage=" + this.stage + ", schedule=" + this.schedule + ", result=" + this.result + CoreConstants.CURLY_RIGHT;
    }
}
