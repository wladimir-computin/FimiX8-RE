package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetAiFollowMode extends X8BaseMessage {
    private int mode;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.mode = packet.getPayLoad4().getByte();
    }

    public int getMode() {
        return this.mode;
    }
}
