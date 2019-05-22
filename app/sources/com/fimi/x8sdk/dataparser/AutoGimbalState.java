package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AutoGimbalState extends X8BaseMessage {
    private int errorCode;
    private int pitchAngle;
    private int rollAngle;
    private int stateCode;
    private int yawAnagle;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.errorCode = packet.getPayLoad4().getShort();
        this.stateCode = packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        this.rollAngle = packet.getPayLoad4().getShort();
        this.pitchAngle = packet.getPayLoad4().getShort();
        this.yawAnagle = packet.getPayLoad4().getShort();
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getRollAngle() {
        return this.rollAngle;
    }

    public void setRollAngle(int rollAngle) {
        this.rollAngle = rollAngle;
    }

    public int getPitchAngle() {
        return this.pitchAngle;
    }

    public void setPitchAngle(int pitchAngle) {
        this.pitchAngle = pitchAngle;
    }

    public int getYawAnagle() {
        return this.yawAnagle;
    }

    public void setYawAnagle(int yawAnagle) {
        this.yawAnagle = yawAnagle;
    }

    public String toString() {
        return "AutoGimbalState{errorCode=" + this.errorCode + ", stateCode=" + this.stateCode + ", rollAngle=" + this.rollAngle + ", pitchAngle=" + this.pitchAngle + ", yawAnagle=" + this.yawAnagle + CoreConstants.CURLY_RIGHT;
    }
}
