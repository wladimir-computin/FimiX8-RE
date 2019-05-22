package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetGimbalGain extends X8BaseMessage {
    int data;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.data = packet.getPayLoad4().getByte() & 255;
    }

    public int getData() {
        return this.data;
    }

    public String toString() {
        return "AckGetGimbalGain{data=" + this.data + CoreConstants.CURLY_RIGHT;
    }
}
