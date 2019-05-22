package com.fimi.kernel.connect;

import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.connect.usb.LinkMsgType;
import com.fimi.kernel.dataparser.milink.LinkMessage;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;

public abstract class BaseCommand extends LinkMessage {
    public String camKey;
    private byte[] cmdData;
    private long createTime;
    private int currentSendNum = 0;
    private byte[] encryptData;
    public int fileOffset;
    private boolean isAddToSendQueue = true;
    private boolean isTimerCmd;
    private JsonUiCallBackListener jsonUiCallBackListener;
    private long lastSendTime;
    LinkMsgType linkMsgType = LinkMsgType.FmLink4;
    private LinkPacket linkPacket;
    private int msgSeq;
    private int outTime = GLMapStaticValue.ANIMATION_FLUENT_TIME;
    private IPersonalDataCallBack personalDataCallBack;
    private byte[] rawCmdData;
    private int reSendNum = 5;
    private UiCallBackListener uiCallBack;
    private byte[] unEncryptData;
    private int usbCmdKey;

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isAddToSendQueue() {
        return this.isAddToSendQueue;
    }

    public void setAddToSendQueue(boolean addToSendQueue) {
        this.isAddToSendQueue = addToSendQueue;
    }

    public int getCurrentSendNum() {
        return this.currentSendNum;
    }

    public void setCurrentSendNum(int currentSendNum) {
        this.currentSendNum = currentSendNum;
    }

    public boolean isTimerCmd() {
        return this.isTimerCmd;
    }

    public void setTimerCmd(boolean timerCmd) {
        this.isTimerCmd = timerCmd;
    }

    public byte[] getUnEncryptData() {
        return this.unEncryptData;
    }

    public void fillUnEncryptData(LinkPacket packet) {
        if (packet.payload != null) {
            packet.payload.putBytes(this.unEncryptData);
        }
    }

    public void setUnEncryptData(byte[] unEncryptData) {
        this.unEncryptData = unEncryptData;
    }

    public byte[] getCmdData() {
        return this.cmdData;
    }

    public void setCmdData(byte[] cmdData) {
        this.cmdData = cmdData;
    }

    public int getOutTime() {
        return this.outTime;
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public int getMsgSeq() {
        return this.msgSeq;
    }

    public void setMsgSeq(int msgSeq) {
        this.msgSeq = msgSeq;
    }

    public int getReSendNum() {
        return this.reSendNum;
    }

    public void setReSendNum(int reSendNum) {
        this.reSendNum = reSendNum;
    }

    public LinkPacket getLinkPacket() {
        return this.linkPacket;
    }

    public void setLinkPacket(LinkPacket linkPacket) {
        this.linkPacket = linkPacket;
    }

    public long getLastSendTime() {
        return this.lastSendTime;
    }

    public void setLastSendTime(long lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    public void fillPayload(LinkPacket packet) {
        if (this.encryptData != null && packet.payload != null) {
            packet.payload.putBytes(this.encryptData);
        }
    }

    public byte[] getEncryptData() {
        return this.encryptData;
    }

    public void setEncryptData(byte[] encryptData) {
        this.encryptData = encryptData;
    }

    public byte[] getRawCmdData() {
        return this.rawCmdData;
    }

    public void setRawCmdData(byte[] rawCmdData) {
        this.rawCmdData = rawCmdData;
    }

    public IPersonalDataCallBack getPersonalDataCallBack() {
        return this.personalDataCallBack;
    }

    public void setPersonalDataCallBack(IPersonalDataCallBack personalDataCallBack) {
        this.personalDataCallBack = personalDataCallBack;
    }

    public void setUiCallBack(UiCallBackListener uiCallBack) {
        this.uiCallBack = uiCallBack;
    }

    public UiCallBackListener getUiCallBack() {
        return this.uiCallBack;
    }

    public String getCamKey() {
        return this.camKey;
    }

    public void setCamKey(String camKey) {
        this.camKey = camKey;
    }

    public JsonUiCallBackListener getJsonUiCallBackListener() {
        return this.jsonUiCallBackListener;
    }

    public void setJsonUiCallBackListener(JsonUiCallBackListener jsonUiCallBackListener) {
        this.jsonUiCallBackListener = jsonUiCallBackListener;
    }

    public int getFileOffset() {
        return this.fileOffset;
    }

    public void setFileOffset(int fileOffset) {
        this.fileOffset = fileOffset;
    }

    public int getUsbCmdKey() {
        return this.usbCmdKey;
    }

    public void setUsbCmdKey(int usbCmdKey) {
        this.usbCmdKey = usbCmdKey;
    }

    public LinkMsgType getLinkMsgType() {
        return this.linkMsgType;
    }

    public void setLinkMsgType(LinkMsgType linkMsgType) {
        this.linkMsgType = linkMsgType;
    }
}
