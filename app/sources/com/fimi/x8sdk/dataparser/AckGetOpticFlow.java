package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetOpticFlow extends X8BaseMessage {
    boolean isOpen;

    public void unPacket(LinkPacket4 packet) {
        boolean z = true;
        super.decrypt(packet);
        if (packet.getPayLoad4().getByte() != (byte) 1) {
            z = false;
        }
        this.isOpen = z;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public String toString() {
        return "AckGetOpticFlow{isOpen=" + this.isOpen + CoreConstants.CURLY_RIGHT;
    }
}
