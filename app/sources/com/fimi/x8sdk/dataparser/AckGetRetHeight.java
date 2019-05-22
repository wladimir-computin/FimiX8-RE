package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetRetHeight extends X8BaseMessage {
    float height;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.height = packet.getPayLoad4().getFloat();
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
