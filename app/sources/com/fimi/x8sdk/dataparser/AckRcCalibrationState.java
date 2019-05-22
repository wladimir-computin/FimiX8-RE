package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckRcCalibrationState extends X8BaseMessage {
    private short cmdStatus;
    private int progress;
    private short rc0;
    private short rc1;
    private short rc2;
    private short rc3;
    private short rc4;
    private short rc5;
    private int status;

    public int getProgress() {
        return this.progress;
    }

    public int getStatus() {
        return this.status;
    }

    public short getCmdStatus() {
        return this.cmdStatus;
    }

    public short getRc0() {
        return this.rc0;
    }

    public short getRc1() {
        return this.rc1;
    }

    public short getRc2() {
        return this.rc2;
    }

    public short getRc3() {
        return this.rc3;
    }

    public short getRc4() {
        return this.rc4;
    }

    public short getRc5() {
        return this.rc5;
    }

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.progress = packet.getPayLoad4().getByte();
        this.status = packet.getPayLoad4().getByte();
        this.cmdStatus = packet.getPayLoad4().getShort();
        this.rc0 = packet.getPayLoad4().getShort();
        this.rc1 = packet.getPayLoad4().getShort();
        this.rc2 = packet.getPayLoad4().getShort();
        this.rc3 = packet.getPayLoad4().getShort();
        this.rc4 = packet.getPayLoad4().getShort();
        this.rc5 = packet.getPayLoad4().getShort();
    }

    public String toString() {
        return "AckRcCilbrationState{progress=" + this.progress + ", status=" + this.status + ", cmdStatus=" + this.cmdStatus + ", rc0=" + this.rc0 + ", rc1=" + this.rc1 + ", rc2=" + this.rc2 + ", rc3=" + this.rc3 + ", rc4=" + this.rc4 + ", rc5=" + this.rc5 + CoreConstants.CURLY_RIGHT;
    }
}
