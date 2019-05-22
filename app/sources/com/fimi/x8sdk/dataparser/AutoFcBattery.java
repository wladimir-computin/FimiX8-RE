package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.fmlink4.LinkPayLoad4;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.entity.X8AppSettingLog;

public class AutoFcBattery extends X8BaseMessage {
    private int cc;
    private int cell1Voltage;
    private int cell2Voltage;
    private int cell3Voltage;
    private int cell4Voltage;
    private int currentCapacity;
    private int currents;
    private int landingCapacity;
    private int rcNotUpdateCnt;
    private int remainPercentage;
    private int remainingTime;
    private int reserve;
    private int rhtCapacity;
    private int temperature;
    private int totalCapacity;
    private int uvc;

    public int getLandingCapacity() {
        return this.landingCapacity;
    }

    public void setLandingCapacity(int landingCapacity) {
        this.landingCapacity = landingCapacity;
    }

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        LinkPayLoad4 payLoad4 = packet.getPayLoad4();
        this.cell1Voltage = payLoad4.getByte() & 255;
        this.cell2Voltage = payLoad4.getByte() & 255;
        this.cell3Voltage = payLoad4.getByte() & 255;
        this.cell4Voltage = payLoad4.getByte() & 255;
        this.currentCapacity = payLoad4.getShort();
        this.totalCapacity = payLoad4.getShort();
        this.currents = payLoad4.getShort();
        this.temperature = payLoad4.getShort();
        this.remainingTime = payLoad4.getShort();
        this.remainPercentage = payLoad4.getByte();
        this.uvc = payLoad4.getByte();
        this.rcNotUpdateCnt = payLoad4.getByte();
        this.reserve = payLoad4.getByte();
        this.rhtCapacity = payLoad4.getShort();
        this.landingCapacity = payLoad4.getShort();
        this.cc = payLoad4.getShort();
        X8AppSettingLog.setCc(this.cc);
        X8AppSettingLog.setUvc(this.uvc);
        X8AppSettingLog.setTotalCapacity(this.totalCapacity);
        X8AppSettingLog.setRcNotUpdateCnt(this.rcNotUpdateCnt);
    }

    public int getTotalCapacity() {
        return this.totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public void setCell1Voltage(int cell1Voltage) {
        this.cell1Voltage = cell1Voltage;
    }

    public void setCell2Voltage(int cell2Voltage) {
        this.cell2Voltage = cell2Voltage;
    }

    public void setCell3Voltage(int cell3Voltage) {
        this.cell3Voltage = cell3Voltage;
    }

    public int getCell4Voltage() {
        return this.cell4Voltage;
    }

    public void setCell4Voltage(int cell4Voltage) {
        this.cell4Voltage = cell4Voltage;
    }

    public int getCurrentCapacity() {
        return this.currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public int getCurrents() {
        return this.currents;
    }

    public void setCurrents(int currents) {
        this.currents = currents;
    }

    public float getTemperature() {
        return ((float) this.temperature) / 10.0f;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getUvc() {
        return this.uvc;
    }

    public void setUvc(int uvc) {
        this.uvc = uvc;
    }

    public int getRcNotUpdateCnt() {
        return this.rcNotUpdateCnt;
    }

    public void setRcNotUpdateCnt(int rcNotUpdateCnt) {
        this.rcNotUpdateCnt = rcNotUpdateCnt;
    }

    public int getRemainPercentage() {
        return this.remainPercentage;
    }

    public void setRemainPercentage(int remainPercentage) {
        this.remainPercentage = remainPercentage;
    }

    public double getCell1Voltage() {
        return (((double) this.cell1Voltage) / 100.0d) + 2.0d;
    }

    public double getCell2Voltage() {
        return (((double) this.cell2Voltage) / 100.0d) + 2.0d;
    }

    public double getCell3Voltage() {
        return (((double) this.cell3Voltage) / 100.0d) + 2.0d;
    }

    public int getDisChargeCnt() {
        return this.cc;
    }

    public String getVoltage() {
        return NumberUtil.decimalPointStr((((((double) this.cell1Voltage) / 100.0d) + 2.0d) + ((((double) this.cell2Voltage) / 100.0d) + 2.0d)) + ((((double) this.cell3Voltage) / 100.0d) + 2.0d), 2);
    }

    public int getCc() {
        return this.cc;
    }

    public int getRhtCapacity() {
        return this.rhtCapacity;
    }

    public void setRhtCapacity(int rhtCapacity) {
        this.rhtCapacity = rhtCapacity;
    }

    public String toString() {
        return "AutoFcBattery{cell1Voltage=" + this.cell1Voltage + ", cell2Voltage=" + this.cell2Voltage + ", cell3Voltage=" + this.cell3Voltage + ", cell4Voltage=" + this.cell4Voltage + ", currentCapacity=" + this.currentCapacity + ", totalCapacity=" + this.totalCapacity + ", currents=" + this.currents + ", temperature=" + this.temperature + ", remainingTime=" + this.remainingTime + ", remainPercentage=" + this.remainPercentage + ", uvc=" + this.uvc + ", rcNotUpdateCnt=" + this.rcNotUpdateCnt + ", cc=" + this.cc + CoreConstants.CURLY_RIGHT;
    }
}
