package com.fimi.kernel.store.sqlite.entity;

public class GH2DataStaticInfo {
    public String createTime;
    public int gimbalVersion;
    public int handleVersion;
    public Long id;
    public double latitude;
    public double longitude;
    public String productModel;
    public double useTime;

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getUseTime() {
        return this.useTime;
    }

    public void setUseTime(double useTime) {
        this.useTime = useTime;
    }

    public int getGimbalVersion() {
        return this.gimbalVersion;
    }

    public void setGimbalVersion(int gimbalVersion) {
        this.gimbalVersion = gimbalVersion;
    }

    public int getHandleVersion() {
        return this.handleVersion;
    }

    public void setHandleVersion(int handleVersion) {
        this.handleVersion = handleVersion;
    }

    public String getProductModel() {
        return this.productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GH2DataStaticInfo(Long id, String productModel, int handleVersion, int gimbalVersion, double useTime, double longitude, double latitude, String createTime) {
        this.id = id;
        this.productModel = productModel;
        this.handleVersion = handleVersion;
        this.gimbalVersion = gimbalVersion;
        this.useTime = useTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createTime = createTime;
    }
}
