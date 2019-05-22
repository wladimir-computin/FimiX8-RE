package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetPilotMode extends X8BaseMessage {
    int pilotMode;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.pilotMode = packet.getPayLoad4().getByte();
    }

    public int getPilotMode() {
        return this.pilotMode;
    }

    public void setPilotMode(int pilotMode) {
        this.pilotMode = pilotMode;
    }

    public String toString() {
        return "AckGetFcParam{, pilotMode=" + this.pilotMode + CoreConstants.CURLY_RIGHT;
    }
}
