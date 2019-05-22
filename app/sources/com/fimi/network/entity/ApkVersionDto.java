package com.fimi.network.entity;

public class ApkVersionDto extends BaseModel {
    private String fileEncode;
    private String fimiID;
    private String newVersion;
    private String pkgName;
    private String sysname;
    private String updcontents;
    private String url;

    public String getUpdcontents() {
        return this.updcontents;
    }

    public void setUpdcontents(String updcontents) {
        this.updcontents = updcontents;
    }

    public String getFimiID() {
        return this.fimiID;
    }

    public void setFimiID(String fimiID) {
        this.fimiID = fimiID;
    }

    public String getFileEncode() {
        return this.fileEncode;
    }

    public void setFileEncode(String fileEncode) {
        this.fileEncode = fileEncode;
    }

    public String getSysname() {
        return this.sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNewVersion() {
        return this.newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
}
