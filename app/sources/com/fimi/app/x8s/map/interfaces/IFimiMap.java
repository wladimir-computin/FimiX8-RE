package com.fimi.app.x8s.map.interfaces;

import android.os.Bundle;
import android.view.View;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.fimi.app.x8s.map.model.MapPoint;
import com.google.android.gms.maps.GoogleMap;

public interface IFimiMap {
    public static final String FIMI_GAODE_MAP = "fimi.gaode.map";

    void changeGaodeCamera(CameraUpdate cameraUpdate);

    void changeGoogleCamera(com.google.android.gms.maps.CameraUpdate cameraUpdate);

    void clearNoFlightZone();

    void drawNoFlightZone(MapPoint mapPoint);

    AMap getAMap();

    View getMapView();

    float getZoom();

    GoogleMap googleMap();

    void onCreate(View view, Bundle bundle);

    void onDestroy();

    void onPause();

    void onResume();

    void onSaveInstanceState(Bundle bundle);

    void switchMapStyle(int i);
}
