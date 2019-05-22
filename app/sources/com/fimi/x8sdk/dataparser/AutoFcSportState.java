package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.util.GpsCorrect;

public class AutoFcSportState extends X8BaseMessage {
    int downVelocity;
    FLatLng fLatLng = new FLatLng();
    int groupSpeed;
    int headingAngle;
    float height;
    float homeDistance;
    double latitude;
    double longitude;
    int pitchAngle;
    int reserve1;
    int reserve2;
    int rollAngle;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.longitude = packet.getPayLoad4().getDouble().doubleValue();
        this.latitude = packet.getPayLoad4().getDouble().doubleValue();
        this.fLatLng = GpsCorrect.Earth_To_Mars(this.latitude, this.longitude);
        this.height = packet.getPayLoad4().getFloat();
        this.groupSpeed = packet.getPayLoad4().getShort();
        this.downVelocity = packet.getPayLoad4().getShort();
        this.rollAngle = packet.getPayLoad4().getShort();
        this.pitchAngle = packet.getPayLoad4().getShort();
        this.headingAngle = packet.getPayLoad4().getShort();
        this.reserve1 = packet.getPayLoad4().getByte();
        this.reserve2 = packet.getPayLoad4().getByte();
        this.homeDistance = packet.getPayLoad4().getFloat();
    }

    public double getLongitude() {
        return this.fLatLng.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.fLatLng.latitude;
    }

    public double getDeviceLongitude() {
        return this.longitude;
    }

    public double getDeviceLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getGroupSpeed() {
        return this.groupSpeed;
    }

    public void setGroupSpeed(int groupSpeed) {
        this.groupSpeed = groupSpeed;
    }

    public int getDownVelocity() {
        return this.downVelocity;
    }

    public void setDownVelocity(int downVelocity) {
        this.downVelocity = downVelocity;
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

    public int getHeadingAngle() {
        return this.headingAngle;
    }

    public float getDeviceAngle() {
        return ((float) this.headingAngle) / 10.0f;
    }

    public void setHeadingAngle(int headingAngle) {
        this.headingAngle = headingAngle;
    }

    public int getReserve1() {
        return this.reserve1;
    }

    public void setReserve1(int reserve1) {
        this.reserve1 = reserve1;
    }

    public int getReserve2() {
        return this.reserve2;
    }

    public void setReserve2(int reserve2) {
        this.reserve2 = reserve2;
    }

    public float getHomeDistance() {
        return this.homeDistance;
    }

    public void setHomeDistance(float homeDistance) {
        this.homeDistance = homeDistance;
    }

    public String toString() {
        return "AutoFcSportState{longitude=" + this.longitude + ", latitude=" + this.latitude + ", height=" + this.height + ", groupSpeed=" + this.groupSpeed + ", downVelocity=" + this.downVelocity + ", rollAngle=" + this.rollAngle + ", pitchAngle=" + this.pitchAngle + ", headingAngle=" + this.headingAngle + ", reserve1=" + this.reserve1 + ", reserve2=" + this.reserve2 + ", homeDistance=" + this.homeDistance + ", fLatLng=" + this.fLatLng + CoreConstants.CURLY_RIGHT;
    }
}
