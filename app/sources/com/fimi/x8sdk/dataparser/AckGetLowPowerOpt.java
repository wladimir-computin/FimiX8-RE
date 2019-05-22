package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetLowPowerOpt extends X8BaseMessage {
    int lowPowerOpt;
    int lowPowerValue;
    int seriousLowPowerOpt;
    int seriousLowPowerValue;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.lowPowerValue = packet.getPayLoad4().getByte();
        this.seriousLowPowerValue = packet.getPayLoad4().getByte();
        this.lowPowerOpt = packet.getPayLoad4().getByte();
        this.seriousLowPowerOpt = packet.getPayLoad4().getByte();
    }

    public int getLowPowerValue() {
        return this.lowPowerValue;
    }

    public void setLowPowerValue(int lowPowerValue) {
        this.lowPowerValue = lowPowerValue;
    }

    public int getSeriousLowPowerValue() {
        return this.seriousLowPowerValue;
    }

    public void setSeriousLowPowerValue(int seriousLowPowerValue) {
        this.seriousLowPowerValue = seriousLowPowerValue;
    }

    public int getLowPowerOpt() {
        return this.lowPowerOpt;
    }

    public void setLowPowerOpt(int lowPowerOpt) {
        this.lowPowerOpt = lowPowerOpt;
    }

    public int getSeriousLowPowerOpt() {
        return this.seriousLowPowerOpt;
    }

    public void setSeriousLowPowerOpt(int seriousLowPowerOpt) {
        this.seriousLowPowerOpt = seriousLowPowerOpt;
    }

    public String toString() {
        return "AckGetLowPowerOpt{lowPowerValue=" + this.lowPowerValue + ", seriousLowPowerValue=" + this.seriousLowPowerValue + ", lowPowerOpt=" + this.lowPowerOpt + ", seriousLowPowerOpt=" + this.seriousLowPowerOpt + CoreConstants.CURLY_RIGHT;
    }
}
