package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.util.GpsCorrect;

public class AckGetAiPoint extends X8BaseMessage {
    private int altitude;
    private float distance;
    private FLatLng fLatLng;
    private boolean isSelect;
    private double latitude;
    private double longitude;
    private int speed;
    private int yaw;

    public int getYaw() {
        return this.yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public FLatLng getfLatLng() {
        return this.fLatLng;
    }

    public void setfLatLng(FLatLng fLatLng) {
        this.fLatLng = fLatLng;
    }

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.longitude = packet.getPayLoad4().getDouble().doubleValue();
        this.latitude = packet.getPayLoad4().getDouble().doubleValue();
        this.fLatLng = GpsCorrect.Earth_To_Mars(this.latitude, this.longitude);
        this.altitude = packet.getPayLoad4().getShort() & 65535;
        this.yaw = packet.getPayLoad4().getShort() & 65535;
        this.speed = packet.getPayLoad4().getByte() & 255;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getAltitude() {
        return this.altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }
}
