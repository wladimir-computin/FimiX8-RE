package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;

public class MediaFileInfo {
    private byte cmdType;
    private short error_code;
    private String fileName;
    private int fileSize;
    private int msg_len;
    private short nameLen;

    public void unPacket(byte[] data) {
        this.cmdType = data[0];
        this.msg_len = getInt(1, data);
        this.error_code = getShort(5, data);
        this.fileSize = getInt(7, data);
        this.nameLen = getShort(11, data);
        byte[] nameArray = new byte[this.nameLen];
        System.arraycopy(data, 13, nameArray, 0, this.nameLen);
        this.fileName = new String(nameArray);
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

    public int getMsg_len() {
        return this.msg_len;
    }

    public void setMsg_len(int msg_len) {
        this.msg_len = msg_len;
    }

    public short getError_code() {
        return this.error_code;
    }

    public void setError_code(short error_code) {
        this.error_code = error_code;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
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

    public String toString() {
        return "MediaFileInfo{cmdType=" + this.cmdType + ", msg_len=" + this.msg_len + ", error_code=" + this.error_code + ", fileSize=" + this.fileSize + ", nameLen=" + this.nameLen + ", fileName='" + this.fileName + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }
}
