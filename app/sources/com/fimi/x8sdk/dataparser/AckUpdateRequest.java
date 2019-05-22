package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckUpdateRequest extends X8BaseMessage {
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
    }

    public boolean isResultSucceed() {
        return getMsgRpt() == 0;
    }

    public String toString() {
        return "AckUpdateRequest{getMsgRpt():" + getMsgRpt() + CoreConstants.CURLY_RIGHT;
    }
}
