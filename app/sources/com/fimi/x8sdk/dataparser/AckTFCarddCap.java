package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckTFCarddCap extends X8BaseMessage {
    private String tfCardCap;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        byte[] payLoad = packet.getPayLoad4().getPayloadData();
        byte[] tfcap = new byte[(payLoad.length - 4)];
        System.arraycopy(payLoad, 4, tfcap, 0, tfcap.length);
        this.tfCardCap = new String(tfcap);
    }

    public String getTfCardCap() {
        return this.tfCardCap;
    }

    public void setTfCardCap(String tfCardCap) {
        this.tfCardCap = tfCardCap;
    }

    public String toString() {
        return "AckTFCarddCap{tfCardCap='" + this.tfCardCap + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }
}
