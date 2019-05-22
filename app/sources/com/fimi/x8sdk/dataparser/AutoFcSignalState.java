package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.cmdsenum.X8GpsNumState;
import com.fimi.x8sdk.cmdsenum.X8HandleSignalState;

public class AutoFcSignalState extends X8BaseMessage {
    public static final int REMOTESIGN_LOW = 30;
    public static final int REMOTESIGN_MID = 80;
    int gpsHorizontal;
    int gpsPosition;
    int gpsSpeed;
    int gpsVertical;
    int magnetic;
    int rfsignal;
    int rptsRec;
    int satelliteNumber;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.satelliteNumber = packet.getPayLoad4().getByte();
        this.gpsVertical = packet.getPayLoad4().getByte();
        this.gpsHorizontal = packet.getPayLoad4().getByte();
        this.gpsPosition = packet.getPayLoad4().getByte();
        this.gpsSpeed = packet.getPayLoad4().getByte();
        this.rfsignal = packet.getPayLoad4().getByte();
        this.rptsRec = packet.getPayLoad4().getByte();
        this.magnetic = packet.getPayLoad4().getByte();
    }

    public X8GpsNumState getSatelliteState() {
        if (this.satelliteNumber <= 6) {
            return X8GpsNumState.LOW;
        }
        if (this.satelliteNumber <= 12 && this.satelliteNumber >= 7) {
            return X8GpsNumState.MIDDLE;
        }
        if (this.satelliteNumber > 13) {
            return X8GpsNumState.STRONG;
        }
        return X8GpsNumState.STRONG;
    }

    public int getSatelliteNumber() {
        return this.satelliteNumber;
    }

    public void setSatelliteNumber(int satelliteNumber) {
        this.satelliteNumber = satelliteNumber;
    }

    public int getGpsVertical() {
        return this.gpsVertical;
    }

    public void setGpsVertical(int gpsVertical) {
        this.gpsVertical = gpsVertical;
    }

    public int getGpsHorizontal() {
        return this.gpsHorizontal;
    }

    public void setGpsHorizontal(int gpsHorizontal) {
        this.gpsHorizontal = gpsHorizontal;
    }

    public int getGpsPosition() {
        return this.gpsPosition;
    }

    public void setGpsPosition(int gpsPosition) {
        this.gpsPosition = gpsPosition;
    }

    public int getGpsSpeed() {
        return this.gpsSpeed;
    }

    public void setGpsSpeed(int gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public int getRfsignal() {
        return this.rfsignal;
    }

    public void setRfsignal(int rfsignal) {
        this.rfsignal = rfsignal;
    }

    public int getRptsRec() {
        return this.rptsRec;
    }

    public void setRptsRec(int rptsRec) {
        this.rptsRec = rptsRec;
    }

    public int getMagnetic() {
        return (int) (Math.abs((((float) this.magnetic) / 10.0f) - 1.0f) * 100.0f);
    }

    public void setMagnetic(int magnetic) {
        this.magnetic = magnetic;
    }

    public boolean isStrong() {
        return this.rptsRec > 80;
    }

    public boolean isMiddle() {
        return this.rptsRec >= 30 && this.rptsRec <= 80;
    }

    public boolean isLow() {
        return this.rptsRec < 30;
    }

    public X8HandleSignalState getX8HandleSignalState() {
        if (isStrong()) {
            return X8HandleSignalState.STRONG;
        }
        if (isMiddle()) {
            return X8HandleSignalState.MIDDLE;
        }
        if (isLow()) {
            return X8HandleSignalState.LOW;
        }
        return X8HandleSignalState.NOSIGNAL;
    }
}
