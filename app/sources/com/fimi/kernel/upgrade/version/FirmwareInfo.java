package com.fimi.kernel.upgrade.version;

import com.fimi.kernel.upgrade.interfaces.IFirmwareInfo;
import com.fimi.kernel.upgrade.interfaces.IReqFimwareBean;

public class FirmwareInfo implements IFirmwareInfo {
    private String errorInfo;
    private IReqFimwareBean fb;
    private long fileSize;
    private String firmwareName;
    private boolean isForce;
    long logicVersion;
    private String path;
    private byte subTargetId;
    private int sysId;
    private byte targetId;
    private String updateContent;
    private boolean upgradeResult;
    private boolean versionError;

    public boolean isVersionError() {
        return this.versionError;
    }

    public void setVersionError(boolean versionError) {
        this.versionError = versionError;
    }

    public void setLogicVersion(long logicVersion) {
        this.logicVersion = logicVersion;
    }

    public long getLogicVersion() {
        return this.logicVersion;
    }

    public boolean isForce() {
        return this.isForce;
    }

    public void setForce(boolean isForce) {
        this.isForce = isForce;
    }

    public String getUpdateContent() {
        return this.updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getFirmwareName() {
        return this.firmwareName;
    }

    public void setFirmwareName(String firmwareName) {
        this.firmwareName = firmwareName;
    }

    public byte getSubTargetId() {
        return this.subTargetId;
    }

    public void setSubTargetId(byte subTargetId) {
        this.subTargetId = subTargetId;
    }

    public boolean isUpgradeResult() {
        return this.upgradeResult;
    }

    public void setUpgradeResult(boolean upgradeResult) {
        this.upgradeResult = upgradeResult;
    }

    public String getErrorInfo() {
        return this.errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte getTargetId() {
        return this.targetId;
    }

    public void setTargetId(byte targetId) {
        this.targetId = targetId;
    }

    public int getSysId() {
        return this.sysId;
    }

    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    public IReqFimwareBean getFb() {
        return this.fb;
    }

    public void setFb(IReqFimwareBean fb) {
        this.fb = fb;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
