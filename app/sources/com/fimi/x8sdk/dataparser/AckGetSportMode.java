package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetSportMode extends X8BaseMessage {
    int vehicleControlType;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.vehicleControlType = packet.getPayLoad4().getInt();
    }

    public int getVehicleControlType() {
        return (this.vehicleControlType >> 7) & 31;
    }

    public void setVehicleControlType(byte vehicleControlType) {
        this.vehicleControlType = vehicleControlType;
    }

    public String toString() {
        return "AckGetFcParam{}";
    }
}
