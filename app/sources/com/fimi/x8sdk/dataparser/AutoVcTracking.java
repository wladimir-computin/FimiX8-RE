package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AutoVcTracking extends X8BaseMessage {
    public static final int REMOTESIGN_LOW = 30;
    public static final int REMOTESIGN_MID = 80;
    int classifier;
    int confidence;
    int h;
    int time;
    long trackErrorCode;
    int w;
    int x;
    int y;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.time = packet.getPayLoad4().getInt();
        this.x = packet.getPayLoad4().getShort() & 65535;
        this.y = packet.getPayLoad4().getShort() & 65535;
        this.w = packet.getPayLoad4().getShort() & 65535;
        this.h = packet.getPayLoad4().getShort() & 65535;
        this.classifier = packet.getPayLoad4().getShort() & 65535;
        this.confidence = packet.getPayLoad4().getByte() & 255;
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        this.trackErrorCode = (long) (packet.getPayLoad4().getInt() & -1);
    }

    public static int getRemotesignMid() {
        return 80;
    }

    public static int getRemotesignLow() {
        return 30;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getH() {
        return this.h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return this.w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getConfidence() {
        return this.confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public long getTrackErrorCode() {
        return this.trackErrorCode;
    }

    public void setTrackErrorCode(long trackErrorCode) {
        this.trackErrorCode = trackErrorCode;
    }

    public String toString() {
        return "AutoVcTracking{time=" + this.time + ", x=" + this.x + ", y=" + this.y + ", h=" + this.h + ", w=" + this.w + ", confidence=" + this.confidence + ", trackErrorCode=" + this.trackErrorCode + CoreConstants.CURLY_RIGHT;
    }
}
