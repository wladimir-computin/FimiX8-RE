package com.fimi.kernel.upgrade.interfaces;

public interface IFirmwareInfo {
    String getErrorInfo();

    IReqFimwareBean getFb();

    long getFileSize();

    String getFirmwareName();

    long getLogicVersion();

    String getPath();

    byte getSubTargetId();

    int getSysId();

    byte getTargetId();

    String getUpdateContent();

    boolean isForce();

    boolean isUpgradeResult();

    boolean isVersionError();

    void setErrorInfo(String str);

    void setFb(IReqFimwareBean iReqFimwareBean);

    void setFileSize(long j);

    void setFirmwareName(String str);

    void setForce(boolean z);

    void setLogicVersion(long j);

    void setPath(String str);

    void setSubTargetId(byte b);

    void setSysId(int i);

    void setTargetId(byte b);

    void setUpdateContent(String str);

    void setUpgradeResult(boolean z);

    void setVersionError(boolean z);
}
