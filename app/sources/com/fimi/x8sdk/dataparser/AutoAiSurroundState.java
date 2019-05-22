package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AutoAiSurroundState extends X8BaseMessage {
    private int altitude;
    private int radius;
    private int speed;
    private int states;
    private int yawMode;

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAltitude() {
        return this.altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getYawMode() {
        return this.yawMode;
    }

    public void setYawMode(int yawMode) {
        this.yawMode = yawMode;
    }

    public int getStates() {
        return this.states;
    }

    public void setStates(int states) {
        this.states = states;
    }

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.radius = packet.getPayLoad4().getShort();
        this.speed = packet.getPayLoad4().getShort();
        this.altitude = packet.getPayLoad4().getShort();
        this.yawMode = packet.getPayLoad4().getByte();
        this.states = packet.getPayLoad4().getByte();
    }

    public String toString() {
        return "{radius=" + this.radius + ", speed=" + this.speed + ", altitude=" + this.altitude + ", yawMode=" + this.yawMode + ", states=" + this.states + CoreConstants.CURLY_RIGHT;
    }
}
