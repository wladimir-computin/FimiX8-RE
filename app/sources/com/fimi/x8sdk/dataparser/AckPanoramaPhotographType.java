package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckPanoramaPhotographType extends X8BaseMessage {
    private byte currentNum;
    private byte mode;
    private byte totalNum;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.mode = packet.getPayLoad4().getByte();
        this.currentNum = packet.getPayLoad4().getByte();
        this.totalNum = packet.getPayLoad4().getByte();
    }

    public byte getMode() {
        return this.mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public byte getCurrentNum() {
        return this.currentNum;
    }

    public void setCurrentNum(byte currentNum) {
        this.currentNum = currentNum;
    }

    public byte getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(byte totalNum) {
        this.totalNum = totalNum;
    }

    public String toString() {
        return "AckPanoramaPhotographType{mode=" + this.mode + ", currentNum=" + this.currentNum + ", totalNum=" + this.totalNum + CoreConstants.CURLY_RIGHT;
    }
}
