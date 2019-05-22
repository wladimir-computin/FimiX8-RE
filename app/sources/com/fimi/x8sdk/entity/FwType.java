package com.fimi.x8sdk.entity;

import ch.qos.logback.core.CoreConstants;

public class FwType {
    private int devMouduleId;
    private int devTargetId;
    private int msgModuleId;

    public int getDevTargetId() {
        return this.devTargetId;
    }

    public void setDevTargetId(int devTargetId) {
        this.devTargetId = devTargetId;
    }

    public int getDevMouduleId() {
        return this.devMouduleId;
    }

    public void setDevMouduleId(int devMouduleId) {
        this.devMouduleId = devMouduleId;
    }

    public int getMsgModuleId() {
        return this.msgModuleId;
    }

    public void setMsgModuleId(int msgModuleId) {
        this.msgModuleId = msgModuleId;
    }

    public String toString() {
        return "FwType{devTargetId=" + this.devTargetId + ", devMouduleId=" + this.devMouduleId + ", msgModuleId=" + this.msgModuleId + CoreConstants.CURLY_RIGHT;
    }
}
