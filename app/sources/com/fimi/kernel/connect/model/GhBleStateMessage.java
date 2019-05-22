package com.fimi.kernel.connect.model;

import com.fimi.kernel.dataparser.GhMessage;
import com.fimi.kernel.dataparser.milink.LinkPacket;

public class GhBleStateMessage extends GhMessage {
    private byte bleState;
    private byte systemState;

    public byte getSystemState() {
        return this.systemState;
    }

    public void setSystemState(byte systemState) {
        this.systemState = systemState;
    }

    public byte getBleState() {
        return this.bleState;
    }

    public void setBleState(byte bleState) {
        this.bleState = bleState;
    }

    public void fillPayload(LinkPacket packet) {
        super.fillPayload(packet);
        fillPayloadCommon(packet);
        packet.payload.putByte(getSystemState());
        packet.payload.putByte(getBleState());
    }
}
