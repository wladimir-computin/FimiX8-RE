package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.util.GpsCorrect;

public class AckGetAiSurroundPoint extends X8BaseMessage {
    private float altitude;
    private float altitudeTakeoff;
    private FLatLng fLatLng;
    private FLatLng fLatLngTakeoff;
    private double latitude;
    private double latitudeTakeoff;
    private double longitude;
    private double longitudeTakeoff;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.longitude = packet.getPayLoad4().getDouble().doubleValue();
        this.latitude = packet.getPayLoad4().getDouble().doubleValue();
        this.fLatLng = GpsCorrect.Earth_To_Mars(this.latitude, this.longitude);
        this.altitude = (float) (packet.getPayLoad4().getShort() & 65535);
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        this.longitudeTakeoff = packet.getPayLoad4().getDouble().doubleValue();
        this.latitudeTakeoff = packet.getPayLoad4().getDouble().doubleValue();
        this.fLatLngTakeoff = GpsCorrect.Earth_To_Mars(this.latitudeTakeoff, this.longitudeTakeoff);
        this.altitudeTakeoff = (float) (packet.getPayLoad4().getShort() & 65535);
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
    }

    public double getDeviceLongitude() {
        return this.longitude;
    }

    public double getDeviceLatitude() {
        return this.latitude;
    }

    public double getDeviceLongitudeTakeoff() {
        return this.longitudeTakeoff;
    }

    public double getDeviceLatitudeTakeoff() {
        return this.latitudeTakeoff;
    }

    public double getLongitude() {
        return this.fLatLng.longitude;
    }

    public double getLatitude() {
        return this.fLatLng.latitude;
    }

    public double getLongitudeTakeoff() {
        return this.fLatLngTakeoff.longitude;
    }

    public double getLatitudeTakeoff() {
        return this.fLatLngTakeoff.latitude;
    }

    public String toString() {
        return "AckGetAiSurroundPoint{longitude=" + this.longitude + ", latitude=" + this.latitude + ", altitude=" + this.altitude + ", fLatLng=" + this.fLatLng + ", longitudeTakeoff=" + this.longitudeTakeoff + ", latitudeTakeoff=" + this.latitudeTakeoff + ", altitudeTakeoff=" + this.altitudeTakeoff + ", fLatLngTakeoff=" + this.fLatLngTakeoff + CoreConstants.CURLY_RIGHT;
    }
}
