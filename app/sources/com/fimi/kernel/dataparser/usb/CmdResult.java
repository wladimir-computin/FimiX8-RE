package com.fimi.kernel.dataparser.usb;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.FimiAppContext;

public class CmdResult {
    private int descption;
    private int errCode;
    private String errDes;
    public boolean isSuccess;
    private int mMsgRpt;

    public int getmMsgRpt() {
        return this.mMsgRpt;
    }

    public void setmMsgRpt(int mMsgRpt) {
        this.mMsgRpt = mMsgRpt;
    }

    public CmdResult(boolean isSuccess, int descption) {
        this.isSuccess = isSuccess;
        this.descption = descption;
        if (descption > 0) {
            this.errDes = FimiAppContext.getContext().getString(descption);
        }
    }

    public CmdResult(boolean isSuccess, int descption, int mMsgRpt) {
        this.isSuccess = isSuccess;
        this.descption = descption;
        this.mMsgRpt = mMsgRpt;
        if (descption > 0) {
            this.errDes = FimiAppContext.getContext().getString(descption);
        }
    }

    public CmdResult(boolean isSuccess, String errDes) {
        this.isSuccess = isSuccess;
        this.errDes = errDes;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public int getErrCode() {
        return this.errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getDescption() {
        return this.descption;
    }

    public void setDescption(int descption) {
        this.descption = descption;
    }

    public String getErrDes() {
        return this.errDes;
    }

    public void setErrDes(String errDes) {
        this.errDes = errDes;
    }

    public String toString() {
        return "CmdResult{isSuccess=" + this.isSuccess + ", errCode=" + this.errCode + ", descption=" + this.descption + ", errDes='" + this.errDes + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }
}
