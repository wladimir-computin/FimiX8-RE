package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckTakePhoto extends X8BaseMessage {
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
    }
}
