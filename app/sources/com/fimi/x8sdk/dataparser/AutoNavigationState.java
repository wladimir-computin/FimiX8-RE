package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class AutoNavigationState extends X8BaseMessage {
    private int apStatus;
    private int naviTaskSta;
    private int taskMode;
    private int wpNUM;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.taskMode = packet.getPayLoad4().getByte() & 255;
        this.naviTaskSta = packet.getPayLoad4().getByte() & 255;
        this.apStatus = packet.getPayLoad4().getByte() & 255;
        this.wpNUM = packet.getPayLoad4().getShort() & 65535;
    }

    public int getApStatus() {
        return this.apStatus;
    }

    public void setApStatus(int apStatus) {
        this.apStatus = apStatus;
    }

    public int getTaskMode() {
        return this.taskMode;
    }

    public void setTaskMode(int taskMode) {
        this.taskMode = taskMode;
    }

    public int getNaviTaskSta() {
        return this.naviTaskSta;
    }

    public void setNaviTaskSta(int naviTaskSta) {
        this.naviTaskSta = naviTaskSta;
    }

    public int getWpNUM() {
        return this.wpNUM;
    }

    public void setWpNUM(int wpNUM) {
        this.wpNUM = wpNUM;
    }
}
