package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetLostAction extends X8BaseMessage {
    int action;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.action = packet.getPayLoad4().getByte();
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
