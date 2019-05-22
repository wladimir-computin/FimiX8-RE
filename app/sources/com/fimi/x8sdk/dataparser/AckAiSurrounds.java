package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckAiSurrounds extends X8BaseMessage {
    private int orientation;
    private int speed;

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        int msgId = getMsgId();
        if (msgId == 71) {
            this.speed = packet.getPayLoad4().getShort();
            packet.getPayLoad4().getByte();
            packet.getPayLoad4().getByte();
        } else if (msgId == 73) {
            this.orientation = packet.getPayLoad4().getByte() & 255;
            packet.getPayLoad4().getByte();
            packet.getPayLoad4().getByte();
            packet.getPayLoad4().getByte();
        }
    }
}
