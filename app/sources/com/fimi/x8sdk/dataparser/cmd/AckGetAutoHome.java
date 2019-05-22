package com.fimi.x8sdk.dataparser.cmd;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.dataparser.X8BaseMessage;

public class AckGetAutoHome extends X8BaseMessage {
    private int state;

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.state = packet.getPayLoad4().getByte();
    }
}
