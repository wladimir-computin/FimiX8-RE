package com.fimi.app.x8s.map.interfaces;

import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;

public abstract class AbsAiPoint2PointManager extends AbsBaseManager {
    public abstract void calcDistance();

    public abstract void changeLine();

    public abstract void clearPoint2PointMarker();

    public abstract MapPointLatLng getMapPointLatLng();

    public abstract void setMarkerByDevice(double d, double d2, int i);

    public abstract void setPoint2PointMarkerSelectListener(IX8MarkerListener iX8MarkerListener);

    public abstract void setRunning();
}
