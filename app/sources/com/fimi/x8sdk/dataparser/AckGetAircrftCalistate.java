package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AckGetAircrftCalistate extends X8BaseMessage {
    private int caliErrorCode;
    private int caliStep;
    private int caliType;
    private int fifthPointPercent;
    private int firstPointPercent;
    private int fourthPointPercent;
    private int secondPointPercent;
    private int sensorType;
    private int sixthPointPercent;
    private int status;
    private int thridPointPercent;

    public int getSensorType() {
        return this.sensorType;
    }

    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    public int getCaliType() {
        return this.caliType;
    }

    public void setCaliType(int caliType) {
        this.caliType = caliType;
    }

    public int getCaliStep() {
        return this.caliStep;
    }

    public void setCaliStep(int caliStep) {
        this.caliStep = caliStep;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCaliErrorCode() {
        return this.caliErrorCode;
    }

    public void setCaliErrorCode(int caliErrorCode) {
        this.caliErrorCode = caliErrorCode;
    }

    public int getFirstPointPercent() {
        return this.firstPointPercent;
    }

    public void setFirstPointPercent(int firstPointPercent) {
        this.firstPointPercent = firstPointPercent;
    }

    public int getSecondPointPercent() {
        return this.secondPointPercent;
    }

    public void setSecondPointPercent(int secondPointPercent) {
        this.secondPointPercent = secondPointPercent;
    }

    public int getThridPointPercent() {
        return this.thridPointPercent;
    }

    public void setThridPointPercent(int thridPointPercent) {
        this.thridPointPercent = thridPointPercent;
    }

    public int getFourthPointPercent() {
        return this.fourthPointPercent;
    }

    public void setFourthPointPercent(int fourthPointPercent) {
        this.fourthPointPercent = fourthPointPercent;
    }

    public int getFifthPointPercent() {
        return this.fifthPointPercent;
    }

    public void setFifthPointPercent(int fifthPointPercent) {
        this.fifthPointPercent = fifthPointPercent;
    }

    public int getSixthPointPercent() {
        return this.sixthPointPercent;
    }

    public void setSixthPointPercent(int sixthPointPercent) {
        this.sixthPointPercent = sixthPointPercent;
    }

    public String toString() {
        return "AckGetAircrftCalistate{sensorType=" + this.sensorType + ", caliType=" + this.caliType + ", caliStep=" + this.caliStep + ", status=" + this.status + ", caliErrorCode=" + this.caliErrorCode + ", firstPointPercent=" + this.firstPointPercent + ", secondPointPercent=" + this.secondPointPercent + ", thridPointPercent=" + this.thridPointPercent + ", fourthPointPercent=" + this.fourthPointPercent + ", fifthPointPercent=" + this.fifthPointPercent + ", sixthPointPercent=" + this.sixthPointPercent + CoreConstants.CURLY_RIGHT;
    }

    public void unPacket(LinkPacket4 packet) {
        this.sensorType = packet.getPayLoad4().getByte();
        this.caliType = packet.getPayLoad4().getByte();
        this.caliStep = packet.getPayLoad4().getByte();
        this.status = packet.getPayLoad4().getByte();
        this.caliErrorCode = packet.getPayLoad4().getInt();
        this.firstPointPercent = packet.getPayLoad4().getByte();
        this.secondPointPercent = packet.getPayLoad4().getByte();
        this.thridPointPercent = packet.getPayLoad4().getByte();
        this.fourthPointPercent = packet.getPayLoad4().getByte();
        this.fifthPointPercent = packet.getPayLoad4().getByte();
        this.sixthPointPercent = packet.getPayLoad4().getByte();
    }
}
