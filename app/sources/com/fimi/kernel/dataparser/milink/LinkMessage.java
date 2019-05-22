package com.fimi.kernel.dataparser.milink;

import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;

public abstract class LinkMessage extends ILinkMessage {
    private byte errorCode;
    private int msgGroudId;
    private int msgId;
    private UiCallBackListener uiCallBack;

    public abstract void fillPayload(LinkPacket linkPacket);

    public abstract void unpack(LinkPayload linkPayload);

    public int getMsgGroudId() {
        return this.msgGroudId;
    }

    public void setMsgGroudId(int msgGroudId) {
        this.msgGroudId = msgGroudId;
    }

    public int getMsgId() {
        return this.msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public byte getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(byte errorCode) {
        this.errorCode = errorCode;
    }

    public void setUiCallBack(UiCallBackListener uiCallBack) {
        this.uiCallBack = uiCallBack;
    }

    public UiCallBackListener getUiCallBack() {
        return this.uiCallBack;
    }
}
