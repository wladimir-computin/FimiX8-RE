package com.fimi.x8sdk.dataparser.cmd;

import ch.qos.logback.core.CoreConstants;

public class CmdAiAutoPhoto {
    public int angle;
    public int config;
    public int mode;
    public int routeLength;
    public int speed;

    public String toString() {
        return "{angle=" + this.angle + ", routeLength=" + this.routeLength + ", speed=" + this.speed + ", config=" + this.config + ", mode=" + this.mode + CoreConstants.CURLY_RIGHT;
    }
}
