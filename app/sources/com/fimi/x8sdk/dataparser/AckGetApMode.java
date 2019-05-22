package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetApMode extends X8BaseMessage {
    int apMode;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.apMode = packet.getPayLoad4().getByte();
    }

    public int getApMode() {
        return this.apMode;
    }

    public void setApMode(int apMode) {
        this.apMode = apMode;
    }

    public String toString() {
        return "AckGetApMode{apMode=" + this.apMode + CoreConstants.CURLY_RIGHT;
    }
}
