package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.entity.X8AppSettingLog;

public class AutoRcState extends X8BaseMessage {
    private int erroCode;
    private int state;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.state = packet.getPayLoad4().getByte() & 255;
        this.erroCode = packet.getPayLoad4().getByte() & 255;
        X8AppSettingLog.setRcState(this.state);
        X8AppSettingLog.setRcErrorState(this.erroCode);
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getErroCode() {
        return this.erroCode;
    }

    public void setErroCode(int erroCode) {
        this.erroCode = erroCode;
    }
}
