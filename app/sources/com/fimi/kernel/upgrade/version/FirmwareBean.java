package com.fimi.kernel.upgrade.version;

import com.fimi.kernel.upgrade.interfaces.IReqFimwareBean;

public class FirmwareBean implements IReqFimwareBean {
    private int hardwareVersion;
    private byte[] idA;
    private byte[] idB;
    private byte[] idC;
    private byte[] idD;
    private int model;
    private int softwareVersion;
    private int sysId;
    private int type;
    private String version;

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getSysId() {
        return this.sysId;
    }

    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    public byte getType() {
        return (byte) this.type;
    }

    public int getModel() {
        return this.model;
    }

    public int getHardwareVersion() {
        return this.hardwareVersion;
    }

    public int getSoftwareVersion() {
        return this.softwareVersion;
    }

    public byte[] getIdA() {
        return this.idA;
    }

    public byte[] getIdB() {
        return this.idB;
    }

    public byte[] getIdC() {
        return this.idC;
    }

    public byte[] getIdD() {
        return this.idD;
    }

    public FirmwareBean(int type, int model, int hardwareVersion, int softwareVersion, byte[] idA, byte[] idB, byte[] idC, byte[] idD) {
        this.type = type;
        this.model = model;
        this.hardwareVersion = hardwareVersion;
        this.softwareVersion = softwareVersion;
        this.idA = idA;
        this.idB = idB;
        this.idC = idC;
        if (idD == null) {
            idD = new byte[4];
        }
        this.idD = idD;
    }

    public void device2SysId() {
    }
}
