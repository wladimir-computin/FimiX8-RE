package com.fimi.kernel.store.sqlite.entity;

public class X8AiLinePointLatlngInfo {
    private int altitude;
    private int altitudePOI;
    private int gimbalMode;
    private int gimbalPitch;
    public Long id;
    private double latitude;
    private double latitudePOI;
    public long lineId;
    private double longitude;
    private double longitudePOI;
    private int missionFinishAction;
    private int number;
    private int pointActionCmd;
    private int rCLostAction;
    private int roration;
    private int speed;
    private int totalnumber;
    private int trajectoryMode;
    private float yaw;
    private int yawMode;

    public long getLineId() {
        return this.lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getAltitude() {
        return this.altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
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

    public int getYawMode() {
        return this.yawMode;
    }

    public void setYawMode(int yawMode) {
        this.yawMode = yawMode;
    }

    public int getGimbalMode() {
        return this.gimbalMode;
    }

    public void setGimbalMode(int gimbalMode) {
        this.gimbalMode = gimbalMode;
    }

    public int getTrajectoryMode() {
        return this.trajectoryMode;
    }

    public void setTrajectoryMode(int trajectoryMode) {
        this.trajectoryMode = trajectoryMode;
    }

    public int getMissionFinishAction() {
        return this.missionFinishAction;
    }

    public void setMissionFinishAction(int missionFinishAction) {
        this.missionFinishAction = missionFinishAction;
    }

    public int getrCLostAction() {
        return this.rCLostAction;
    }

    public void setrCLostAction(int rCLostAction) {
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

    public int getAltitudePOI() {
        return this.altitudePOI;
    }

    public void setAltitudePOI(int altitudePOI) {
        this.altitudePOI = altitudePOI;
    }

    public int getRCLostAction() {
        return this.rCLostAction;
    }

    public void setRCLostAction(int rCLostAction) {
        this.rCLostAction = rCLostAction;
    }

    public int getRoration() {
        return this.roration;
    }

    public void setRoration(int roration) {
        this.roration = roration;
    }

    public int getPointActionCmd() {
        return this.pointActionCmd;
    }

    public void setPointActionCmd(int pointActionCmd) {
        this.pointActionCmd = pointActionCmd;
    }

    public X8AiLinePointLatlngInfo(Long id, int number, int totalnumber, double longitude, double latitude, int altitude, float yaw, int gimbalPitch, int speed, int yawMode, int gimbalMode, int trajectoryMode, int missionFinishAction, int rCLostAction, double longitudePOI, double latitudePOI, int altitudePOI, long lineId, int pointActionCmd, int roration) {
        this.id = id;
        this.number = number;
        this.totalnumber = totalnumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.yaw = yaw;
        this.gimbalPitch = gimbalPitch;
        this.speed = speed;
        this.yawMode = yawMode;
        this.gimbalMode = gimbalMode;
        this.trajectoryMode = trajectoryMode;
        this.missionFinishAction = missionFinishAction;
        this.rCLostAction = rCLostAction;
        this.longitudePOI = longitudePOI;
        this.latitudePOI = latitudePOI;
        this.altitudePOI = altitudePOI;
        this.lineId = lineId;
        this.pointActionCmd = pointActionCmd;
        this.roration = roration;
    }
}
