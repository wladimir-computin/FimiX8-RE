package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckAiFollowGetEnableBack extends X8BaseMessage {
    private int enable;

    public int getEnable() {
        return this.enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.enable = packet.getPayLoad4().getByte();
    }
}
