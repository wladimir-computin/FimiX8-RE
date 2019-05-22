package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckAiScrewPrameter extends X8BaseMessage {
    private int RTHTostart;
    private int ciclePeriod;
    private int distance;
    private int pev2;
    private int rev1;
    private int rev3;
    private int vertSpeed;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.distance = packet.getPayLoad4().getShort() / 10;
        this.ciclePeriod = packet.getPayLoad4().getByte();
        this.RTHTostart = packet.getPayLoad4().getByte();
        this.rev1 = packet.getPayLoad4().getByte();
        this.pev2 = packet.getPayLoad4().getByte();
        this.rev3 = packet.getPayLoad4().getByte();
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getVertSpeed() {
        return this.vertSpeed;
    }

    public void setVertSpeed(int vertSpeed) {
        this.vertSpeed = vertSpeed;
    }

    public int getRTHTostart() {
        return this.RTHTostart;
    }

    public void setRTHTostart(int RTHTostart) {
        this.RTHTostart = RTHTostart;
    }

    public int getRev1() {
        return this.rev1;
    }

    public void setRev1(int rev1) {
        this.rev1 = rev1;
    }

    public int getPev2() {
        return this.pev2;
    }

    public void setPev2(int pev2) {
        this.pev2 = pev2;
    }

    public int getRev3() {
        return this.rev3;
    }

    public void setRev3(int rev3) {
        this.rev3 = rev3;
    }

    public int getCiclePeriod() {
        return this.ciclePeriod;
    }

    public void setCiclePeriod(int ciclePeriod) {
        this.ciclePeriod = ciclePeriod;
    }

    public String toString() {
        return "AckAiScrewPrameter{distance=" + this.distance + ", ciclePeriod=" + this.ciclePeriod + ", vertSpeed=" + this.vertSpeed + ", RTHTostart=" + this.RTHTostart + ", rev1=" + this.rev1 + ", pev2=" + this.pev2 + ", rev3=" + this.rev3 + CoreConstants.CURLY_RIGHT;
    }
}
