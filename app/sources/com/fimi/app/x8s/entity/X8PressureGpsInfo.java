package com.fimi.app.x8s.entity;

import ch.qos.logback.core.CoreConstants;

public class X8PressureGpsInfo {
    private static X8PressureGpsInfo instance = new X8PressureGpsInfo();
    private float hPa;
    private boolean hasLocation;
    private boolean hasPressure;
    private float mAltitude;
    private float mBearing;
    private float mHorizontalAccuracyMeters;
    private double mLatitude;
    private double mLongitude;
    private float mSpeed;
    private float mVerticalAccuracyMeters;

    public void clearAll() {
        this.hasPressure = false;
        this.hasLocation = false;
        this.hPa = 0.0f;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mAltitude = 0.0f;
        this.mHorizontalAccuracyMeters = 0.0f;
        this.mVerticalAccuracyMeters = 0.0f;
        this.mSpeed = 0.0f;
        this.mBearing = 0.0f;
    }

    public static X8PressureGpsInfo getInstance() {
        return instance;
    }

    public boolean isHasPressure() {
        return this.hasPressure;
    }

    public void setHasPressure(boolean hasPressure) {
        this.hasPressure = hasPressure;
    }

    public boolean isHasLocation() {
        return this.hasLocation;
    }

    public void setHasLocation(boolean hasLocation) {
        this.hasLocation = hasLocation;
    }

    public float gethPa() {
        return this.hPa;
    }

    public void sethPa(float hPa) {
        this.hPa = hPa;
    }

    public double getmLongitude() {
        return this.mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getmLatitude() {
        return this.mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public float getmAltitude() {
        return this.mAltitude;
    }

    public void setmAltitude(double mAltitude) {
        this.mAltitude = (float) mAltitude;
    }

    public float getmHorizontalAccuracyMeters() {
        return this.mHorizontalAccuracyMeters;
    }

    public void setmHorizontalAccuracyMeters(float mHorizontalAccuracyMeters) {
        this.mHorizontalAccuracyMeters = mHorizontalAccuracyMeters;
    }

    public float getmVerticalAccuracyMeters() {
        return this.mVerticalAccuracyMeters;
    }

    public void setmVerticalAccuracyMeters(float mVerticalAccuracyMeters) {
        this.mVerticalAccuracyMeters = mVerticalAccuracyMeters;
    }

    public float getmSpeed() {
        return this.mSpeed;
    }

    public void setmSpeed(float mSpeed) {
        this.mSpeed = mSpeed;
    }

    public float getmBearing() {
        return this.mBearing;
    }

    public void setmBearing(float mBearing) {
        this.mBearing = mBearing;
    }

    public String toString() {
        return "X8PressureGpsInfo{hasPressure=" + this.hasPressure + ", hasLocation=" + this.hasLocation + ", hPa=" + this.hPa + ", mLongitude=" + this.mLongitude + ", mLatitude=" + this.mLatitude + ", mAltitude=" + this.mAltitude + ", mHorizontalAccuracyMeters=" + this.mHorizontalAccuracyMeters + ", mVerticalAccuracyMeters=" + this.mVerticalAccuracyMeters + ", mSpeed=" + this.mSpeed + ", mBearing=" + this.mBearing + CoreConstants.CURLY_RIGHT;
    }
}
