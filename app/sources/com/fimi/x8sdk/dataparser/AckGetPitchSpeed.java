package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetPitchSpeed extends X8BaseMessage {
    int speed;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.speed = packet.getPayLoad4().getByte();
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String toString() {
        return "AckGetPitchSpeed{speed=" + this.speed + CoreConstants.CURLY_RIGHT;
    }
}
