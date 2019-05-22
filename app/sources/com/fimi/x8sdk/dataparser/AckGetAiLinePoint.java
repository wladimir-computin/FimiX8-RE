package com.fimi.x8sdk.dataparser;

import android.support.annotation.NonNull;
import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.util.GpsCorrect;

public class AckGetAiLinePoint extends X8BaseMessage implements Comparable<AckGetAiLinePoint> {
    private int altitude;
    private double altitudePOI;
    private float angle;
    private FLatLng fLatLng;
    private byte gimbalMode;
    private int gimbalPitch;
    private double latitude;
    private double latitudePOI;
    private double longitude;
    private double longitudePOI;
    private byte missionFinishAction;
    private int number;
    private byte rCLostAction;
    private int roration;
    private int speed;
    private int totalnumber;
    private byte trajectoryMode;
    private int yaw;
    private byte yawMode;

    public float getAngle() {
        float f = ((float) this.yaw) / 100.0f;
        this.angle = f;
        return f;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalnumber() {
        return this.totalnumber;
    }

    public void setTotalnumber(int totalnumber) {
        this.totalnumber = totalnumber;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public FLatLng getfLatLng() {
        return this.fLatLng;
    }

    public void setfLatLng(FLatLng fLatLng) {
        this.fLatLng = fLatLng;
    }

    public int getAltitude() {
        return this.altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getYaw() {
        return this.yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public int getGimbalPitch() {
        return this.gimbalPitch;
    }

    public void setGimbalPitch(int gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public byte getYawMode() {
        return this.yawMode;
    }

    public void setYawMode(byte yawMode) {
        this.yawMode = yawMode;
    }

    public byte getGimbalMode() {
        return this.gimbalMode;
    }

    public void setGimbalMode(byte gimbalMode) {
        this.gimbalMode = gimbalMode;
    }

    public byte getTrajectoryMode() {
        return this.trajectoryMode;
    }

    public void setTrajectoryMode(byte trajectoryMode) {
        this.trajectoryMode = trajectoryMode;
    }

    public byte getMissionFinishAction() {
        return this.missionFinishAction;
    }

    public void setMissionFinishAction(byte missionFinishAction) {
        this.missionFinishAction = missionFinishAction;
    }

    public byte getrCLostAction() {
        return this.rCLostAction;
    }

    public void setrCLostAction(byte rCLostAction) {
        this.rCLostAction = rCLostAction;
    }

    public double getLongitudePOI() {
        return this.longitudePOI;
    }

    public void setLongitudePOI(double longitudePOI) {
        this.longitudePOI = longitudePOI;
    }

    public double getLatitudePOI() {
        return this.latitudePOI;
    }

    public void setLatitudePOI(double latitudePOI) {
        this.latitudePOI = latitudePOI;
    }

    public double getAltitudePOI() {
        return this.altitudePOI;
    }

    public void setAltitudePOI(double altitudePOI) {
        this.altitudePOI = altitudePOI;
    }

    public int getRoration() {
        return this.roration;
    }

    public void setRoration(int roration) {
        this.roration = roration;
    }

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.number = packet.getPayLoad4().getByte();
        this.totalnumber = packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        this.longitude = packet.getPayLoad4().getDouble().doubleValue();
        this.latitude = packet.getPayLoad4().getDouble().doubleValue();
        this.fLatLng = GpsCorrect.Earth_To_Mars(this.latitude, this.longitude);
        this.altitude = packet.getPayLoad4().getShort();
        this.altitude /= 10;
        this.yaw = packet.getPayLoad4().getShort();
        this.gimbalPitch = packet.getPayLoad4().getShort();
        this.speed = packet.getPayLoad4().getByte() & 255;
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        this.yawMode = packet.getPayLoad4().getByte();
        this.roration = (byte) ((this.yawMode & 240) >> 4);
        this.yawMode = (byte) (this.yawMode & 15);
        this.gimbalMode = packet.getPayLoad4().getByte();
        this.trajectoryMode = packet.getPayLoad4().getByte();
        this.missionFinishAction = packet.getPayLoad4().getByte();
        this.rCLostAction = packet.getPayLoad4().getByte();
        this.longitudePOI = packet.getPayLoad4().getDouble().doubleValue();
        this.latitudePOI = packet.getPayLoad4().getDouble().doubleValue();
        this.altitudePOI = (double) (packet.getPayLoad4().getShort() / 10);
    }

    public String toString() {
        return "AckGetAiLinePoint{number=" + this.number + ", totalnumber=" + this.totalnumber + ", longitude=" + this.longitude + ", latitude=" + this.latitude + ", fLatLng=" + this.fLatLng + ", altitude=" + this.altitude + ", yaw=" + this.yaw + ", gimbalPitch=" + this.gimbalPitch + ", speed=" + this.speed + ", yawMode=" + this.yawMode + ", gimbalMode=" + this.gimbalMode + ", trajectoryMode=" + this.trajectoryMode + ", missionFinishAction=" + this.missionFinishAction + ", rCLostAction=" + this.rCLostAction + ", longitudePOI=" + this.longitudePOI + ", latitudePOI=" + this.latitudePOI + ", altitudePOI=" + this.altitudePOI + ", angle=" + this.angle + CoreConstants.CURLY_RIGHT;
    }

    public int compareTo(@NonNull AckGetAiLinePoint o) {
        return getNumber() - o.getNumber();
    }

    public boolean hasInterrestPoint() {
        if (this.longitudePOI == 0.0d && this.latitudePOI == 0.0d && this.altitudePOI == 0.0d) {
            return false;
        }
        return true;
    }
}
