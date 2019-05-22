package com.fimi.network.entity;

import ch.qos.logback.core.CoreConstants;
import java.io.Serializable;

public class NetModel implements Serializable {
    public static final String SUCCESS = "90000";
    private static final long serialVersionUID = 1;
    private Object data;
    private String errCode;
    private String errMsg;
    private boolean success = true;

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String toString() {
        return "NetModel{errCode='" + this.errCode + CoreConstants.SINGLE_QUOTE_CHAR + ", success=" + this.success + ", data=" + this.data + ", errMsg='" + this.errMsg + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }
}
