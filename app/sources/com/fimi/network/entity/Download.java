package com.fimi.network.entity;

import ch.qos.logback.core.CoreConstants;

public class Download {
    private String fileEncode;
    private String forceSign;
    private String modelID;
    private String newVersion;
    private String pushFireType;
    private String status;
    private String sysid;
    private String sysname;
    private String updcontents;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileEncode() {
        return this.fileEncode;
    }

    public void setFileEncode(String fileEncode) {
        this.fileEncode = fileEncode;
    }

    public String getForceSign() {
        return this.forceSign;
    }

    public void setForceSign(String forceSign) {
        this.forceSign = forceSign;
    }

    public String getSysid() {
        return this.sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getNewVersion() {
        return this.newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getPushFireType() {
        return this.pushFireType;
    }

    public void setPushFireType(String pushFireType) {
        this.pushFireType = pushFireType;
    }

    public String getSysname() {
        return this.sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getUpdcontents() {
        return this.updcontents;
    }

    public void setUpdcontents(String updcontents) {
        this.updcontents = updcontents;
    }

    public String getModelID() {
        return this.modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "Download{fileEncode='" + this.fileEncode + CoreConstants.SINGLE_QUOTE_CHAR + ", forceSign='" + this.forceSign + CoreConstants.SINGLE_QUOTE_CHAR + ", sysid='" + this.sysid + CoreConstants.SINGLE_QUOTE_CHAR + ", newVersion='" + this.newVersion + CoreConstants.SINGLE_QUOTE_CHAR + ", pushFireType='" + this.pushFireType + CoreConstants.SINGLE_QUOTE_CHAR + ", sysname='" + this.sysname + CoreConstants.SINGLE_QUOTE_CHAR + ", updcontents='" + this.updcontents + CoreConstants.SINGLE_QUOTE_CHAR + ", modelID='" + this.modelID + CoreConstants.SINGLE_QUOTE_CHAR + ", status='" + this.status + CoreConstants.SINGLE_QUOTE_CHAR + ", url='" + this.url + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }
}
