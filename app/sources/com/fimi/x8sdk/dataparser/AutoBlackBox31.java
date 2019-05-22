package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.X8FcLogManager;

public class AutoBlackBox31 extends X8BaseMessage {
    public void unPacket(LinkPacket4 packet) {
        X8FcLogManager.getInstance().fcLogWrite(getPayloadData(packet));
    }
}
