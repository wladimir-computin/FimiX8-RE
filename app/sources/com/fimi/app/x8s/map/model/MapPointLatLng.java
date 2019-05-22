package com.fimi.app.x8s.map.model;

import ch.qos.logback.core.CoreConstants;
import java.util.ArrayList;
import java.util.List;

public class MapPointLatLng {
    public int action;
    public float altitude;
    public float angle;
    public float distance;
    public int gimbalPitch;
    public boolean isActionSave;
    public boolean isInrertestPointActive;
    public boolean isIntertestPoint;
    public boolean isMapPoint;
    public boolean isSelect;
    public double latitude;
    public double longitude;
    public MapPointLatLng mInrertestPoint;
    public int nPos;
    public List<MapPointLatLng> pointList = new ArrayList();
    public int roration;
    public float showAngle;
    public int yawMode;

    public MapPointLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void addPoint(MapPointLatLng point) {
        this.pointList.add(point);
    }

    public String toString() {
        return "MapPointLatLng{latitude=" + this.latitude + ", longitude=" + this.longitude + ", isSelect=" + this.isSelect + ", distance=" + this.distance + ", altitude=" + this.altitude + ", angle=" + this.angle + ", showAngle=" + this.showAngle + ", nPos=" + this.nPos + ", isMapPoint=" + this.isMapPoint + ", isIntertestPoint=" + this.isIntertestPoint + ", isInrertestPointActive=" + this.isInrertestPointActive + ", action=" + this.action + ", isActionSave=" + this.isActionSave + ", mInrertestPoint=" + this.mInrertestPoint + ", pointList=" + this.pointList + ", yawMode=" + this.yawMode + ", roration=" + this.roration + CoreConstants.CURLY_RIGHT;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        this.showAngle = angle;
    }
}
