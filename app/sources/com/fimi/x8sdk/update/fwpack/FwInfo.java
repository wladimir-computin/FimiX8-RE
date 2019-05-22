package com.fimi.x8sdk.update.fwpack;

import java.io.Serializable;

public class FwInfo implements Serializable {
    public static final String UPDATE_RESULT_FAILED = "1";
    public static final String UPDATE_RESULT_SUCCESS = "0";
    private String errorCode;
    private byte fwType;
    private byte modelId;
    private int schedule;
    private short softwareVer;
    private byte stepVer;
    private String sysName;
    private byte typeId;
    private String updateResult;

    public byte getModelId() {
        return this.modelId;
    }

    public void setModelId(byte modelId) {
        this.modelId = modelId;
    }

    public byte getTypeId() {
        return this.typeId;
    }

    public void setTypeId(byte typeId) {
        this.typeId = typeId;
    }

    public byte getFwType() {
        return this.fwType;
    }

    public void setFwType(byte fwType) {
        this.fwType = fwType;
    }

    public short getSoftwareVer() {
        return this.softwareVer;
    }

    public void setSoftwareVer(short softwareVer) {
        this.softwareVer = softwareVer;
    }

    public byte getStepVer() {
        return this.stepVer;
    }

    public void setStepVer(byte stepVer) {
        this.stepVer = stepVer;
    }

    public String getUpdateResult() {
        return this.updateResult;
    }

    public String getSysName() {
        return this.sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public void setUpdateResult(String updateResult) {
        this.updateResult = updateResult;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getSchedule() {
        return this.schedule;
    }

    public void setSchedule(int schedule) {
        this.schedule = schedule;
    }

    public String toString() {
        return "FwInfo [modelId=" + this.modelId + ", typeId=" + this.typeId + ", fwType=" + this.fwType + ", softwareVer=" + this.softwareVer + ", stepVer=" + this.stepVer + ", updateResult=" + this.updateResult + "]";
    }
}
