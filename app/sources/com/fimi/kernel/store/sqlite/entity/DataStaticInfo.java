package com.fimi.kernel.store.sqlite.entity;

public class DataStaticInfo {
    private String currentTime;
    private byte deviceType;
    private String flyDistance;
    private String flyHeight;
    private String flyTime;
    public Long id;
    private String latitude;
    private String longitude;
    private String mcuVersion;
    private String sysVersion;
    private byte type;
    private String useTime;
    private String userId;

    public byte getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(byte deviceType) {
        this.deviceType = deviceType;
    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getUseTime() {
        return this.useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFlyTime() {
        return this.flyTime;
    }

    public void setFlyTime(String flyTime) {
        this.flyTime = flyTime;
    }

    public String getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFlyHeight() {
        return this.flyHeight;
    }

    public void setFlyHeight(String flyHeight) {
        this.flyHeight = flyHeight;
    }

    public String getFlyDistance() {
        return this.flyDistance;
    }

    public void setFlyDistance(String flyDistance) {
        this.flyDistance = flyDistance;
    }

    public String getMcuVersion() {
        return this.mcuVersion;
    }

    public void setMcuVersion(String mcuVersion) {
        this.mcuVersion = mcuVersion;
    }

    public String getSysVersion() {
        return this.sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public DataStaticInfo(Long id, String currentTime, String flyTime, String userId, String useTime, byte type, byte deviceType, String sysVersion, String mcuVersion, String flyDistance, String flyHeight, String longitude, String latitude) {
        this.id = id;
        this.currentTime = currentTime;
        this.flyTime = flyTime;
        this.userId = userId;
        this.useTime = useTime;
        this.type = type;
        this.deviceType = deviceType;
        this.sysVersion = sysVersion;
        this.mcuVersion = mcuVersion;
        this.flyDistance = flyDistance;
        this.flyHeight = flyHeight;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
