package com.fimi.app.x8s.map.interfaces;

import android.content.Context;
import com.amap.api.maps.AMap;
import com.fimi.app.x8s.interfaces.IX8AiItemMapListener;
import com.fimi.app.x8s.map.model.FimiPoint;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.google.android.gms.maps.GoogleMap;

public abstract class AbsMap implements IFimiMap {
    public static final float ZOOMDEFAOULT = 15.555f;
    protected Context context;

    public abstract void addDeviceLocation(double d, double d2);

    public abstract void addFlyPolyline(double d, double d2);

    public abstract void animateCamer();

    public abstract void chaneDeviceAngle(float f);

    public abstract void defaultMapValue();

    public abstract float getAccuracy();

    public abstract AbsAiLineManager getAiLineManager();

    public abstract AbsAiPoint2PointManager getAiPoint2PointManager();

    public abstract AbsAiSurroundManager getAiSurroundManager();

    public abstract MapPointLatLng getDeviceLatlng();

    public abstract double[] getDevicePosition();

    public abstract double[] getManLatLng();

    public abstract boolean hasHomeInfo();

    public abstract boolean isMapInit();

    public abstract void moveCameraByDevice();

    public abstract void onLocationEvnent();

    public abstract void onSensorChanged(float f);

    public abstract void setHomeLocation(double d, double d2);

    public abstract void setmX8AiItemMapListener(IX8AiItemMapListener iX8AiItemMapListener);

    public abstract FimiPoint toScreenLocation(double d, double d2);

    public AMap getAMap() {
        return null;
    }

    public GoogleMap googleMap() {
        return null;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUpMap() {
    }
}
