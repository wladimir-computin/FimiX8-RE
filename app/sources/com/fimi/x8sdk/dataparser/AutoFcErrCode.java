package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AutoFcErrCode extends X8BaseMessage {
    public static long p = (((long) Math.pow(2.0d, 32.0d)) - 1);
    long systemStatusCodA;
    long systemStatusCodB;
    long systemStatusCodC;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.systemStatusCodA = (long) packet.getPayLoad4().getInt();
        if (this.systemStatusCodA < 0) {
            this.systemStatusCodA &= p;
        }
        this.systemStatusCodB = (long) packet.getPayLoad4().getInt();
        if (this.systemStatusCodB < 0) {
            this.systemStatusCodB &= p;
        }
        this.systemStatusCodC = (long) packet.getPayLoad4().getInt();
        if (this.systemStatusCodC < 0) {
            this.systemStatusCodC &= p;
        }
    }

    public long getSystemStatusCodA() {
        return this.systemStatusCodA;
    }

    public void setSystemStatusCodA(int systemStatusCodA) {
        this.systemStatusCodA = (long) systemStatusCodA;
    }

    public long getSystemStatusCodB() {
        return this.systemStatusCodB;
    }

    public void setSystemStatusCodB(int systemStatusCodB) {
        this.systemStatusCodB = (long) systemStatusCodB;
    }

    public long getSystemStatusCodC() {
        return this.systemStatusCodC;
    }

    public void setSystemStatusCodC(int systemStatusCodC) {
        this.systemStatusCodC = (long) systemStatusCodC;
    }

    public String toString() {
        return "AutoFcErrCode{systemStatusCodA=" + this.systemStatusCodA + ", systemStatusCodB=" + this.systemStatusCodB + ", systemStatusCodC=" + this.systemStatusCodC + CoreConstants.CURLY_RIGHT;
    }
}
