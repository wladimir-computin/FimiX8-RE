package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckFiveKeyDefine extends X8BaseMessage {
    byte adckeyAction;
    byte adckeyIndex;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.adckeyIndex = packet.getPayLoad4().getByte();
        this.adckeyAction = packet.getPayLoad4().getByte();
    }

    public byte getAdckeyIndex() {
        return this.adckeyIndex;
    }

    public void setAdckeyIndex(byte adckeyIndex) {
        this.adckeyIndex = adckeyIndex;
    }

    public byte getAdckeyAction() {
        return this.adckeyAction;
    }

    public void setAdckeyAction(byte adckeyAction) {
        this.adckeyAction = adckeyAction;
    }

    public String toString() {
        return "AckFiveKeyDefine{adckeyIndex=" + this.adckeyIndex + ", adckeyAction=" + this.adckeyAction + CoreConstants.CURLY_RIGHT;
    }
}
