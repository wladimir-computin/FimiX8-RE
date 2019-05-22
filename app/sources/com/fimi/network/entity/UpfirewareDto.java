package com.fimi.network.entity;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.utils.I18NUtil;

public class UpfirewareDto extends BaseModel {
    public static final String FW_DOWN_FAILED = "1";
    public static final String FW_DOWN_SUCCESS = "0";
    public static final String UPDATE_RESULT_FAILED = "1";
    public static final String UPDATE_RESULT_SUCCESS = "0";
    public static final String UPGRADE_FORCE = "2";
    public static final String UPGRADE_FORCE_IGNORE_VERSION = "1";
    public static final String UPGRADE_UNFORCE = "0";
    private String downResult;
    private int endVersion;
    private String fileEncode;
    private long fileSize;
    private String fileUrl;
    private String forceOta;
    private String forceSign;
    private long logicVersion;
    private int model;
    private Long pushFireType;
    private int startVersion;
    private String status;
    private FwContenti18N sysNameI18N;
    private int type;
    private FwContenti18N updateContentI18N;
    private String updateResult;
    private String userVersion;

    public FwContenti18N getSysNameI18N() {
        return this.sysNameI18N;
    }

    public void setSysNameI18N(FwContenti18N sysNameI18N) {
        this.sysNameI18N = sysNameI18N;
    }

    public void setUpdateContentI18N(FwContenti18N updateContentI18N) {
        this.updateContentI18N = updateContentI18N;
    }

    public FwContenti18N getUpdateContentI18N() {
        return this.updateContentI18N;
    }

    public int getStartVersion() {
        return this.startVersion;
    }

    public void setStartVersion(int startVersion) {
        this.startVersion = startVersion;
    }

    public int getEndVersion() {
        return this.endVersion;
    }

    public void setEndVersion(int endVersion) {
        this.endVersion = endVersion;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSysName() {
        return I18NUtil.getI18NStrin(this.sysNameI18N);
    }

    public String getUpdateContent() {
        return I18NUtil.getI18NStrin(this.updateContentI18N);
    }

    public long getLogicVersion() {
        return this.logicVersion;
    }

    public void setLogicVersion(long logicVersion) {
        this.logicVersion = logicVersion;
    }

    public String getUserVersion() {
        return this.userVersion;
    }

    public void setUserVersion(String userVersion) {
        this.userVersion = userVersion;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileEncode() {
        return this.fileEncode;
    }

    public void setFileEncode(String fileEncode) {
        this.fileEncode = fileEncode;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getModel() {
        return this.model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public Long getPushFireType() {
        return this.pushFireType;
    }

    public void setPushFireType(Long pushFireType) {
        this.pushFireType = pushFireType;
    }

    public String getForceSign() {
        return this.forceSign;
    }

    public void setForceSign(String forceSign) {
        this.forceSign = forceSign;
    }

    public String getUpdateResult() {
        return this.updateResult;
    }

    public void setUpdateResult(String updateResult) {
        this.updateResult = updateResult;
    }

    public String getDownResult() {
        return this.downResult;
    }

    public void setDownResult(String downResult) {
        this.downResult = downResult;
    }

    public String getForceOta() {
        return this.forceOta;
    }

    public void setForceOta(String forceOta) {
        this.forceOta = forceOta;
    }

    public String toString() {
        return "UpfirewareDto{type=" + this.type + ", sysNameI18N='" + this.sysNameI18N.toString() + CoreConstants.SINGLE_QUOTE_CHAR + ", updateContentI18N='" + this.updateContentI18N.toString() + CoreConstants.SINGLE_QUOTE_CHAR + ", logicVersion=" + this.logicVersion + ", userVersion='" + this.userVersion + CoreConstants.SINGLE_QUOTE_CHAR + ", fileUrl='" + this.fileUrl + CoreConstants.SINGLE_QUOTE_CHAR + ", fileSize=" + this.fileSize + ", fileEncode='" + this.fileEncode + CoreConstants.SINGLE_QUOTE_CHAR + ", status='" + this.status + CoreConstants.SINGLE_QUOTE_CHAR + ", model=" + this.model + ", pushFireType=" + this.pushFireType + ", forceSign='" + this.forceSign + CoreConstants.SINGLE_QUOTE_CHAR + ", startVersion=" + this.startVersion + ", endVersion=" + this.endVersion + ", updateResult='" + this.updateResult + CoreConstants.SINGLE_QUOTE_CHAR + ", downResult='" + this.downResult + CoreConstants.SINGLE_QUOTE_CHAR + ", forceOta='" + this.forceOta + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }
}
