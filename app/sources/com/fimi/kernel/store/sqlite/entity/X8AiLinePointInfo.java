package com.fimi.kernel.store.sqlite.entity;

import ch.qos.logback.core.CoreConstants;

public class X8AiLinePointInfo {
    private int autoRecord;
    private int disconnectType;
    private float distance;
    private int excuteEnd;
    public Long id;
    private int isCurve;
    private String locality;
    private int mapType;
    private String name = "";
    private int runByMapOrVedio;
    private int saveFlag;
    private int speed;
    private long time;
    private int type;

    public int getRunByMapOrVedio() {
        return this.runByMapOrVedio;
    }

    public void setRunByMapOrVedio(int runByMapOrVedio) {
        this.runByMapOrVedio = runByMapOrVedio;
    }

    public int getMapType() {
        return this.mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSaveFlag() {
        return this.saveFlag;
    }

    public void setSaveFlag(int saveFlag) {
        this.saveFlag = saveFlag;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getIsCurve() {
        return this.isCurve;
    }

    public void setIsCurve(int isCurve) {
        this.isCurve = isCurve;
    }

    public X8AiLinePointInfo(Long id, long time, String name, int type, int speed, int saveFlag, float distance, int isCurve, int mapType, int runByMapOrVedio, int disconnectType, int excuteEnd, int autoRecord, String locality) {
        this.id = id;
        this.time = time;
        this.name = name;
        this.type = type;
        this.speed = speed;
        this.saveFlag = saveFlag;
        this.distance = distance;
        this.isCurve = isCurve;
        this.mapType = mapType;
        this.runByMapOrVedio = runByMapOrVedio;
        this.disconnectType = disconnectType;
        this.excuteEnd = excuteEnd;
        this.autoRecord = autoRecord;
        this.locality = locality;
    }

    public String toString() {
        return "X8AiLinePointInfo{id=" + this.id + ", time=" + this.time + ", name='" + this.name + CoreConstants.SINGLE_QUOTE_CHAR + ", type=" + this.type + ", speed=" + this.speed + ", saveFlag=" + this.saveFlag + ", distance=" + this.distance + ", isCurve=" + this.isCurve + ", mapType=" + this.mapType + ", runByMapOrVedio=" + this.runByMapOrVedio + CoreConstants.CURLY_RIGHT;
    }

    public int getAutoRecord() {
        return this.autoRecord;
    }

    public void setAutoRecord(int autoRecord) {
        this.autoRecord = autoRecord;
    }

    public int getExcuteEnd() {
        return this.excuteEnd;
    }

    public void setExcuteEnd(int excuteEnd) {
        this.excuteEnd = excuteEnd;
    }

    public int getDisconnectType() {
        return this.disconnectType;
    }

    public void setDisconnectType(int disconnectType) {
        this.disconnectType = disconnectType;
    }

    public String getLocality() {
        return this.locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}
