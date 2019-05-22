package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetRcMode extends X8BaseMessage {
    int mode;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.mode = packet.getPayLoad4().getByte();
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String toString() {
        return "AckGetRcMode{mode=" + this.mode + CoreConstants.CURLY_RIGHT;
    }
}
