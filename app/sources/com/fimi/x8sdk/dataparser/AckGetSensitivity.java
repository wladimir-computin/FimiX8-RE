package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetSensitivity extends X8BaseMessage {
    int pitchPercent;
    int rollPercent;
    int throPercent;
    int yawPercent;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.rollPercent = packet.getPayLoad4().getByte();
        this.pitchPercent = packet.getPayLoad4().getByte();
        this.yawPercent = packet.getPayLoad4().getByte();
        this.throPercent = packet.getPayLoad4().getByte();
    }

    public int getRollPercent() {
        return this.rollPercent;
    }

    public void setRollPercent(int rollPercent) {
        this.rollPercent = rollPercent;
    }

    public int getPitchPercent() {
        return this.pitchPercent;
    }

    public void setPitchPercent(int pitchPercent) {
        this.pitchPercent = pitchPercent;
    }

    public int getYawPercent() {
        return this.yawPercent;
    }

    public void setYawPercent(int yawPercent) {
        this.yawPercent = yawPercent;
    }

    public int getThroPercent() {
        return this.throPercent;
    }

    public void setThroPercent(int throPercent) {
        this.throPercent = throPercent;
    }

    public String toString() {
        return "AckGetSensitivity{rollPercent=" + this.rollPercent + ", pitchPercent=" + this.pitchPercent + ", yawPercent=" + this.yawPercent + ", throPercent=" + this.throPercent + CoreConstants.CURLY_RIGHT;
    }
}
