package com.fimi.x8sdk.dataparser.cmd;

import ch.qos.logback.core.CoreConstants;

public class CmdAiLinePoints {
    public int altitude;
    public int altitudePIO;
    public float angle;
    public int autoRecord;
    public int compeletEvent;
    public int count;
    public int disconnectEvent;
    public int gimbalPitch;
    public double latitude;
    public double latitudePIO;
    public double longitude;
    public double longitudePIO;
    public int nPos;
    public int orientation;
    public int pioEnbale;
    public int rotation;
    public int speed;

    public String toString() {
        return "CmdAiLinePoints{speed=" + this.speed + ", angle=" + this.angle + ", orientation=" + this.orientation + ", rotation=" + this.rotation + ", latitude=" + this.latitude + ", longitude=" + this.longitude + ", altitude=" + this.altitude + ", count=" + this.count + ", nPos=" + this.nPos + ", latitudePIO=" + this.latitudePIO + ", longitudePIO=" + this.longitudePIO + ", altitudePIO=" + this.altitudePIO + ", disconnectEvent=" + this.disconnectEvent + ", compeletEvent=" + this.compeletEvent + ", autoRecord=" + this.autoRecord + ", pioEnbale=" + this.pioEnbale + ", gimbalPitch=" + this.gimbalPitch + CoreConstants.CURLY_RIGHT;
    }
}
