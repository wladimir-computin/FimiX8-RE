package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.host.HostLogBack;
import com.fimi.x8sdk.update.fwpack.ByteHexHelper;
import java.util.Arrays;

public class MediaFileDownLoadPacket {
    private byte cmdType;
    private int crcFlag;
    private short errorCode;
    private String fileName;
    private int msgLen;
    private short nameLen;
    private int offSet;
    private byte[] playData;
    private short playloadSize;
    private int resultFlag;

    public void unPacket(byte[] data) {
        this.cmdType = data[0];
        this.msgLen = getInt(1, data);
        this.errorCode = getShort(5, data);
        this.offSet = getInt(7, data);
        this.resultFlag = data[11];
        this.crcFlag = getShort(12, data);
        this.nameLen = getShort(14, data);
        byte[] nameArray = new byte[this.nameLen];
        try {
            System.arraycopy(data, 16, nameArray, 0, nameArray.length);
            this.fileName = new String(nameArray);
            this.playloadSize = getShort(nameArray.length + 16, data);
            this.playData = new byte[this.playloadSize];
            System.arraycopy(data, nameArray.length + 18, this.playData, 0, this.playData.length);
        } catch (Exception e) {
            HostLogBack.getInstance().writeLog("Alanqiu ======Exception unPacket:" + ByteHexHelper.bytesToHexString(data));
            e.printStackTrace();
        }
    }

    public int getInt(int index, byte[] data) {
        return (((0 | ((data[index + 3] & 255) << 24)) | ((data[index + 2] & 255) << 16)) | ((data[index + 1] & 255) << 8)) | (data[index + 0] & 255);
    }

    public short getShort(int index, byte[] data) {
        return (short) ((data[index + 0] & 255) | ((short) (((data[index + 1] & 255) << 8) | (short) 0)));
    }

    public byte getCmdType() {
        return this.cmdType;
    }

    public void setCmdType(byte cmdType) {
        this.cmdType = cmdType;
    }

    public int getMsgLen() {
        return this.msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }

    public short getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public int getOffSet() {
        return this.offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public short getNameLen() {
        return this.nameLen;
    }

    public void setNameLen(short nameLen) {
        this.nameLen = nameLen;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public short getPlayloadSize() {
        return this.playloadSize;
    }

    public void setPlayloadSize(short playloadSize) {
        this.playloadSize = playloadSize;
    }

    public byte[] getPlayData() {
        return this.playData;
    }

    public void setPlayData(byte[] playData) {
        this.playData = playData;
    }

    public int getResultFlag() {
        return this.resultFlag;
    }

    public boolean isFinished() {
        return this.resultFlag == 1;
    }

    public void setResultFlag(int resultFlag) {
        this.resultFlag = resultFlag;
    }

    public int getCrcFlag() {
        return this.crcFlag;
    }

    public void setCrcFlag(int crcFlag) {
        this.crcFlag = crcFlag;
    }

    public String toString() {
        return "MediaFileDownLoadPacket{cmdType=" + this.cmdType + ", msgLen=" + this.msgLen + ", errorCode=" + this.errorCode + ", offSet=" + this.offSet + ", resultFlag=" + this.resultFlag + ", crcFlag=" + this.crcFlag + ", nameLen=" + this.nameLen + ", fileName='" + this.fileName + CoreConstants.SINGLE_QUOTE_CHAR + ", playloadSize=" + this.playloadSize + ", playData=" + Arrays.toString(this.playData) + CoreConstants.CURLY_RIGHT;
    }
}
