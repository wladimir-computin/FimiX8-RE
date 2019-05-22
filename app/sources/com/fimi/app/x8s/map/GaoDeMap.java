package com.fimi.app.x8s.map;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.fimi.app.x8s.enums.NoFlyZoneEnum;
import com.fimi.app.x8s.interfaces.IFimiOnSnapshotReady;
import com.fimi.app.x8s.interfaces.IX8AiItemMapListener;
import com.fimi.app.x8s.map.interfaces.AbsFimiMap;
import com.fimi.app.x8s.map.manager.gaode.GaoDeMapAiLineManager;
import com.fimi.app.x8s.map.manager.gaode.GaoDeMapAiPoint2PointManager;
import com.fimi.app.x8s.map.manager.gaode.GaoDeMapAiSurroundManager;
import com.fimi.app.x8s.map.manager.gaode.GaodeMapLocationManager;
import com.fimi.app.x8s.map.model.FimiPoint;
import com.fimi.app.x8s.map.model.GaoDeMapNoFlyZone;
import com.fimi.app.x8s.map.model.MapPoint;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.dataparser.AutoHomeInfo;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.util.GpsCorrect;
import java.util.ArrayList;
import java.util.List;

public class GaoDeMap extends AbsFimiMap {
    public static final LatLng BEIJING = new LatLng(39.885936156645116d, 116.44093986600636d);
    public static final LatLng BEIJING1 = new LatLng(39.63005008474942d, 116.179156601429d);
    private AMap aMap;
    private float currentZoom = 0.0f;
    private Polyline droneFlyLine;
    private boolean isInit;
    List<LatLng> latLngs = new ArrayList();
    private GaodeMapLocationManager mAMapLocationManager;
    private GaoDeMapAiLineManager mGaoDeMapAiLineManager;
    private GaoDeMapAiPoint2PointManager mGaoDeMapAiPoint2PointManager;
    private GaoDeMapAiSurroundManager mGaoDeMapAiSurroundManager;
    private UiSettings mUiSettings;
    private IX8AiItemMapListener mX8AiItemMapListener;
    private TextureMapView mapView;
    private GaoDeMapNoFlyZone noFlyZone;

    public void setmX8AiItemMapListener(IX8AiItemMapListener mX8AiItemMapListener) {
        this.mX8AiItemMapListener = mX8AiItemMapListener;
    }

    public boolean isMapInit() {
        return this.isInit;
    }

    public LatLng getGaoDeLatlng(double lat, double lng) {
        return new LatLng(lat, lng);
    }

    public GaoDeMapAiLineManager getAiLineManager() {
        return this.mGaoDeMapAiLineManager;
    }

    public GaoDeMapAiSurroundManager getAiSurroundManager() {
        return this.mGaoDeMapAiSurroundManager;
    }

    public GaoDeMapAiPoint2PointManager getAiPoint2PointManager() {
        return this.mGaoDeMapAiPoint2PointManager;
    }

    public boolean hasHomeInfo() {
        if (!this.isInit || this.mAMapLocationManager == null || this.mAMapLocationManager.getHomeLocation() == null) {
            return false;
        }
        return true;
    }

    public float getAccuracy() {
        if (!this.isInit || this.mAMapLocationManager == null) {
            return 0.0f;
        }
        return this.mAMapLocationManager.getAccuracy();
    }

    public void moveCameraByDevice() {
    }

    public double[] getDevicePosition() {
        return new double[0];
    }

    public View getMapView() {
        return this.mapView;
    }

    public AMap getAMap() {
        return this.aMap;
    }

    public void onCreate(View rootView, Bundle savedInstanceState) {
        this.mapView = new TextureMapView(rootView.getContext());
        this.mapView.onCreate(savedInstanceState);
        if (this.aMap == null) {
            this.aMap = this.mapView.getMap();
            this.aMap.setMapType(GlobalConfig.getInstance().getMapStyle() == Constants.X8_GENERAL_MAP_STYLE_NORMAL ? 1 : 2);
            this.mUiSettings = this.aMap.getUiSettings();
            this.mUiSettings.setZoomControlsEnabled(false);
            this.mAMapLocationManager = new GaodeMapLocationManager(this.context, this.aMap);
            this.mGaoDeMapAiPoint2PointManager = new GaoDeMapAiPoint2PointManager(this.context, this.aMap, this.mAMapLocationManager);
            this.mGaoDeMapAiSurroundManager = new GaoDeMapAiSurroundManager(this.context, this.aMap, this.mAMapLocationManager);
            this.mGaoDeMapAiLineManager = new GaoDeMapAiLineManager(this.context, this.aMap, this.mAMapLocationManager);
            this.noFlyZone = new GaoDeMapNoFlyZone(this.aMap);
            this.isInit = true;
        }
    }

    public void setUpMap() {
        this.mAMapLocationManager.setUpMap();
    }

    public void onResume() {
        this.mapView.onResume();
        if (this.mUiSettings != null) {
            this.mUiSettings.setZoomControlsEnabled(false);
        }
        this.mAMapLocationManager.onResume();
    }

    public void onPause() {
        this.mapView.onPause();
        this.mAMapLocationManager.onPause();
    }

    public void onSaveInstanceState(Bundle outState) {
        this.mapView.onSaveInstanceState(outState);
    }

    public void onDestroy() {
        this.mapView.onDestroy();
        this.mAMapLocationManager.onDestroy();
    }

    public float getZoom() {
        float f = this.aMap.getCameraPosition().zoom;
        this.currentZoom = f;
        return f;
    }

    public void changeGaodeCamera(CameraUpdate update) {
        this.aMap.moveCamera(update);
    }

    public void changeGoogleCamera(com.google.android.gms.maps.CameraUpdate update) {
    }

    public void switchMapStyle(int mapStyle) {
        if (mapStyle == Constants.X8_GENERAL_MAP_STYLE_NORMAL) {
            this.aMap.setMapType(1);
        } else if (mapStyle == Constants.X8_GENERAL_MAP_STYLE_SATELLITE) {
            this.aMap.setMapType(2);
        }
    }

    public void setHomeLocation(double latitude, double longitude) {
        addHomeLocation(new LatLng(latitude, longitude));
    }

    public void drawNoFlightZone(MapPoint points) {
        LatLng center = points.getCenter();
        LatLng A1 = points.getA1();
        LatLng A2 = points.getA2();
        LatLng C1 = points.getC1();
        LatLng C2 = points.getC2();
        LatLng B1 = points.getB1();
        LatLng B2 = points.getB2();
        LatLng D1 = points.getD1();
        LatLng D2 = points.getD2();
        if (points.getType() == NoFlyZoneEnum.CANDY) {
            this.noFlyZone.drawCandyNoFlyZone(new LatLng[]{center, D1, B1, C1, A1, A2, C2, B2, D2});
        } else if (points.getType() == NoFlyZoneEnum.CIRCLE) {
            this.noFlyZone.drawCircleNoFlyZone(center, points.getRadius());
        } else if (points.getType() == NoFlyZoneEnum.IRREGULAR) {
            List<LatLng> list = points.getLatLngs();
            LatLng[] lats = new LatLng[list.size()];
            list.toArray(lats);
            this.noFlyZone.drawIrregularNoFlyZone(lats, points.isNoFly());
        }
    }

    public void clearNoFlightZone() {
        this.noFlyZone.clearNoFlightZone();
    }

    public void onSensorChanged(float degree) {
        this.mAMapLocationManager.onSensorChanged(degree);
    }

    public void addHomeLocation(LatLng latLng) {
        this.mAMapLocationManager.addHomeLocation(latLng);
        if (this.mX8AiItemMapListener != null) {
            switch (this.mX8AiItemMapListener.getCurrentItem()) {
                case AI_POINT_TO_POINT:
                    this.mGaoDeMapAiPoint2PointManager.drawAiLimit(latLng.latitude, latLng.longitude, 500.0d);
                    return;
                case AI_LINE:
                    this.mGaoDeMapAiLineManager.drawAiLimit(latLng.latitude, latLng.longitude, 1000.0d);
                    return;
                default:
                    return;
            }
        }
    }

    public void addDeviceLocation(double latitude, double logitude) {
        this.mAMapLocationManager.addDeviceLocation(new LatLng(latitude, logitude));
        this.mAMapLocationManager.drawFlyLine();
        if (StateManager.getInstance().getX8Drone().getCtrlMode() == 4) {
            this.mGaoDeMapAiPoint2PointManager.changeDeviceLocation(new LatLng(latitude, logitude));
        }
    }

    public void chaneDeviceAngle(float angle) {
        this.mAMapLocationManager.chaneDeviceAngle(angle);
    }

    public void addFlyPolyline(double latitude, double logitude) {
        if (StateManager.getInstance().getX8Drone().isInSky()) {
            this.mAMapLocationManager.addFlyPolyLine(latitude, logitude);
        } else {
            this.mAMapLocationManager.clearFlyPolyLine();
        }
    }

    public void defaultMapValue() {
        this.mAMapLocationManager.clearMarker();
    }

    public void animateCamer() {
        if (this.mAMapLocationManager != null) {
            this.mAMapLocationManager.animatePersonLocation();
        }
    }

    public FimiPoint toScreenLocation(double lat, double lng) {
        FimiPoint p = new FimiPoint();
        Point mPoint = this.aMap.getProjection().toScreenLocation(new LatLng(lat, lng));
        if (mPoint != null) {
            p.x = mPoint.x;
            p.y = mPoint.y;
        }
        return p;
    }

    public MapPointLatLng getDeviceLatlng() {
        MapPointLatLng p = new MapPointLatLng();
        LatLng latlng = this.mAMapLocationManager.getHomeLocation();
        if (latlng != null) {
            p.latitude = latlng.latitude;
            p.longitude = latlng.longitude;
        }
        return p;
    }

    public void onLocationEvnent() {
        AutoHomeInfo homeInfo = StateManager.getInstance().getX8Drone().getHomeInfo();
        this.aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(homeInfo.getFLatLng().latitude, homeInfo.getFLatLng().longitude), 15.5f));
    }

    public double[] getManLatLng() {
        double[] latLng = new double[2];
        LatLng ll = this.mAMapLocationManager.getManLocation();
        FLatLng fLatLng = GpsCorrect.Mars_To_Earth0(ll.latitude, ll.longitude);
        latLng[0] = fLatLng.latitude;
        latLng[1] = fLatLng.longitude;
        return latLng;
    }

    public void snapshot(IFimiOnSnapshotReady callback) {
    }
}
