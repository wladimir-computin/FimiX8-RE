package com.fimi.app.x8s.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.amap.api.maps.CameraUpdate;
import com.fimi.app.x8s.enums.NoFlyZoneEnum;
import com.fimi.app.x8s.interfaces.IFimiOnSnapshotReady;
import com.fimi.app.x8s.interfaces.IX8AiItemMapListener;
import com.fimi.app.x8s.map.interfaces.AbsAiLineManager;
import com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager;
import com.fimi.app.x8s.map.interfaces.AbsAiSurroundManager;
import com.fimi.app.x8s.map.interfaces.AbsFimiMap;
import com.fimi.app.x8s.map.manager.google.GglMapAiLineManager;
import com.fimi.app.x8s.map.manager.google.GglMapAiPoint2PointManager;
import com.fimi.app.x8s.map.manager.google.GglMapAiSurroundManager;
import com.fimi.app.x8s.map.manager.google.GglMapLocationManager;
import com.fimi.app.x8s.map.model.FimiPoint;
import com.fimi.app.x8s.map.model.GglMapNoFlyZone;
import com.fimi.app.x8s.map.model.MapPoint;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.dataparser.AutoHomeInfo;
import com.fimi.x8sdk.modulestate.StateManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.twitter.sdk.android.core.internal.scribe.EventsFilesManager;

public class GglMap extends AbsFimiMap implements OnMapReadyCallback {
    LatLng BEIJING = new LatLng(22.63916666d, 113.8108333d);
    private GglMapAiLineManager aiLineManager;
    private GglMapAiPoint2PointManager aiP2PManager;
    private GglMapAiSurroundManager aiSurroundManager;
    private float currentZoom = 0.0f;
    private GglMapLocationManager gglMapLocationManager;
    private GoogleMap googleMap;
    private boolean isInit;
    private IX8AiItemMapListener mX8AiItemMapListener;
    private MapView mapView;
    private GglMapNoFlyZone noFlyZone;

    public void setmX8AiItemMapListener(IX8AiItemMapListener mX8AiItemMapListener) {
        this.mX8AiItemMapListener = mX8AiItemMapListener;
    }

    public boolean isMapInit() {
        return this.isInit;
    }

    public boolean hasHomeInfo() {
        if (!this.isInit || this.gglMapLocationManager == null || this.gglMapLocationManager.getHomeLocation() == null) {
            return false;
        }
        return true;
    }

    public float getAccuracy() {
        if (!this.isInit || this.gglMapLocationManager == null) {
            return 0.0f;
        }
        return this.gglMapLocationManager.getAccuracy();
    }

    public View getMapView() {
        return this.mapView;
    }

    public GoogleMap googleMap() {
        return this.googleMap;
    }

    public void setHomeLocation(double latitude, double longitude) {
        addHomeLocation(new LatLng(latitude, longitude));
    }

    public void onCreate(View rootView, Bundle savedInstanceState) {
        this.mapView = new MapView(rootView.getContext());
        this.mapView.onCreate(savedInstanceState);
        this.mapView.setEnabled(true);
        this.mapView.setClickable(true);
        this.mapView.getMapAsync(this);
    }

    public void onResume() {
        this.mapView.onResume();
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.onStart();
        }
    }

    public void onPause() {
        this.mapView.onPause();
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.onStop();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        this.mapView.onSaveInstanceState(outState);
    }

    public void onDestroy() {
        this.mapView.onDestroy();
    }

    public float getZoom() {
        float f = this.googleMap.getCameraPosition().zoom;
        this.currentZoom = f;
        return f;
    }

    public void changeGaodeCamera(CameraUpdate update) {
    }

    public void switchMapStyle(int mapStyle) {
        if (!this.isInit) {
            return;
        }
        GoogleMap googleMap;
        GoogleMap googleMap2;
        if (mapStyle == Constants.X8_GENERAL_MAP_STYLE_NORMAL) {
            googleMap = this.googleMap;
            googleMap2 = this.googleMap;
            googleMap.setMapType(1);
        } else if (mapStyle == Constants.X8_GENERAL_MAP_STYLE_SATELLITE) {
            googleMap = this.googleMap;
            googleMap2 = this.googleMap;
            googleMap.setMapType(2);
        }
    }

    public void changeGoogleCamera(com.google.android.gms.maps.CameraUpdate update) {
        this.googleMap.moveCamera(update);
    }

    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.noFlyZone = new GglMapNoFlyZone(googleMap);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.BEIJING, 9.49f));
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            this.gglMapLocationManager = new GglMapLocationManager(googleMap, this.context);
            this.aiP2PManager = new GglMapAiPoint2PointManager(this.context, googleMap, this.gglMapLocationManager);
            this.aiSurroundManager = new GglMapAiSurroundManager(this.context, googleMap, this.gglMapLocationManager);
            this.aiLineManager = new GglMapAiLineManager(this.context, googleMap, this.gglMapLocationManager);
            this.gglMapLocationManager.onStart();
            this.isInit = true;
            if (GlobalConfig.getInstance().getMapStyle() == Constants.X8_GENERAL_MAP_STYLE_NORMAL) {
                googleMap.setMapType(1);
            } else if (GlobalConfig.getInstance().getMapStyle() == Constants.X8_GENERAL_MAP_STYLE_SATELLITE) {
                googleMap.setMapType(2);
            }
        }
    }

    public void onSensorChanged(float degree) {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.onSensorChanged(degree);
        }
    }

    public void addDeviceLocation(double latitude, double logitude) {
        LatLng mLatLng = new LatLng(latitude, logitude);
        addDeviceLocation(mLatLng);
        this.gglMapLocationManager.addDeviceLocation(mLatLng);
        this.gglMapLocationManager.drawFlyLine();
    }

    public void chaneDeviceAngle(float angle) {
        this.gglMapLocationManager.chaneDeviceAngle(angle);
    }

    public void addFlyPolyline(double latitude, double logitude) {
        if (StateManager.getInstance().getX8Drone().isInSky()) {
            this.gglMapLocationManager.addFlyPolyLine(latitude, logitude);
        } else {
            this.gglMapLocationManager.clearFlyPolyLine();
        }
    }

    public void defaultMapValue() {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.clearMarker();
        }
    }

    public void animateCamer() {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.animatePersonLocation();
        }
    }

    public AbsAiLineManager getAiLineManager() {
        return this.aiLineManager;
    }

    public AbsAiPoint2PointManager getAiPoint2PointManager() {
        return this.aiP2PManager;
    }

    public AbsAiSurroundManager getAiSurroundManager() {
        return this.aiSurroundManager;
    }

    public FimiPoint toScreenLocation(double lat, double lng) {
        FimiPoint p = new FimiPoint();
        Point mPoint = this.googleMap.getProjection().toScreenLocation(new LatLng(lat, lng));
        if (mPoint != null) {
            p.x = mPoint.x;
            p.y = mPoint.y;
        }
        return p;
    }

    public MapPointLatLng getDeviceLatlng() {
        MapPointLatLng p = new MapPointLatLng();
        LatLng latlng = this.gglMapLocationManager.getDevLocation();
        if (latlng != null) {
            p.latitude = latlng.latitude;
            p.longitude = latlng.longitude;
        }
        return p;
    }

    public void drawNoFlightZone(MapPoint points) {
        if (this.noFlyZone != null) {
            LatLng center = new LatLng(22.63916666d, 113.8108333d);
            LatLng A1 = new LatLng(22.51166667d, 113.92d);
            LatLng A2 = new LatLng(22.48d, 113.8583333d);
            LatLng C1 = new LatLng(22.58260615d, 113.8691167d);
            LatLng C2 = new LatLng(22.5625731d, 113.8271341d);
            LatLng B1 = new LatLng(22.65420346d, 113.8802317d);
            LatLng B2 = new LatLng(22.59746319d, 113.7564348d);
            LatLng D1 = new LatLng(22.50633789d, 113.9952067d);
            LatLng D2 = new LatLng(22.42642201d, 113.8208448d);
            if (points.getType() == NoFlyZoneEnum.CANDY) {
                this.noFlyZone.drawCandyNoFlyZone(new LatLng[]{center, D1, B1, C1, A1, A2, C2, B2, D2});
            } else if (points.getType() == NoFlyZoneEnum.CIRCLE) {
                this.noFlyZone.drawCircleNoFlyZone(center, EventsFilesManager.MAX_BYTE_SIZE_PER_FILE);
            } else if (points.getType() == NoFlyZoneEnum.IRREGULAR) {
                this.noFlyZone.drawIrregularNoFlyZone(new LatLng[]{B1, C1, A1, A2, C2, B2}, points.isNoFly());
            }
            addHomeLocation(center);
            addDeviceLocation(D2);
        }
    }

    public void clearNoFlightZone() {
        if (this.noFlyZone != null) {
            this.noFlyZone.clearNoFlightZone();
        }
    }

    public void addHomeLocation(LatLng latLng) {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.addHomeLocation(latLng);
        }
        if (this.mX8AiItemMapListener != null) {
            switch (this.mX8AiItemMapListener.getCurrentItem()) {
                case AI_POINT_TO_POINT:
                    this.aiP2PManager.drawAiLimit(latLng.latitude, latLng.longitude, 1000.0d);
                    return;
                case AI_LINE:
                    this.aiLineManager.drawAiLimit(latLng.latitude, latLng.longitude, 1000.0d);
                    return;
                default:
                    return;
            }
        }
    }

    public void addDeviceLocation(LatLng latLng) {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.addDeviceLocation(latLng);
            this.gglMapLocationManager.drawFlyLine();
        }
        if (this.aiLineManager != null) {
            this.aiLineManager.changeLine();
        }
        if (this.aiP2PManager != null) {
            this.aiP2PManager.changeLine();
        }
    }

    public void moveCameraByDevice() {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.moveCameraByDevice();
        }
    }

    public double[] getDevicePosition() {
        if (this.gglMapLocationManager != null) {
            return this.gglMapLocationManager.getDevicePosition();
        }
        return null;
    }

    public void onLocationEvnent() {
        AutoHomeInfo homeInfo = StateManager.getInstance().getX8Drone().getHomeInfo();
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(homeInfo.getFLatLng().latitude, homeInfo.getFLatLng().longitude), 15.555f));
    }

    public double[] getManLatLng() {
        double[] latLng = new double[2];
        if (this.gglMapLocationManager == null) {
            return null;
        }
        LatLng ll = this.gglMapLocationManager.getManLocation();
        if (ll == null) {
            return null;
        }
        latLng[0] = ll.latitude;
        latLng[1] = ll.longitude;
        return latLng;
    }

    public void snapshot(final IFimiOnSnapshotReady callBack) {
        this.googleMap.snapshot(new SnapshotReadyCallback() {
            public void onSnapshotReady(Bitmap bitmap) {
                callBack.onSnapshotReady(bitmap);
            }
        });
    }
}
