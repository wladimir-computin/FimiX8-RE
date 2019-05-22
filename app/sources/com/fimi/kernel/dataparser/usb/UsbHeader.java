package com.fimi.kernel.dataparser.usb;

import android.util.Log;

public class UsbHeader {
    public static final int MAGIC_START_NUMBER = 174;
    public static final int USB_HEADER_LEN = 5;
    int checkSum;
    byte[] header = new byte[5];
    int len;
    int type;
    int ver;

    public byte[] onPacket() {
        this.checkSum = 0;
        this.header[0] = (byte) -82;
        Log.d("moweiru", "ver:" + this.ver + ";len:" + this.len);
        this.header[1] = (byte) ((this.ver & 15) | ((this.len & 15) << 4));
        this.header[2] = (byte) ((this.len >> 4) & 255);
        this.header[3] = (byte) (this.type & 255);
        for (int j = 0; j < 4; j++) {
            this.checkSum += this.header[j];
        }
        this.header[4] = (byte) (this.checkSum & 255);
        return this.header;
    }

    public int getVer() {
        return this.ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public int getLen() {
        return this.len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCheckSum() {
        return this.checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }
}
