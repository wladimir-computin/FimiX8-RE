package com.fimi.app.x8s.map.manager.gaode;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.CancelableCallback;
import com.amap.api.maps.AMap.OnMapTouchListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.tools.LinkedListQueue;
import java.util.ArrayList;
import java.util.List;

public class GaodeMapLocationManager implements LocationSource, AMapLocationListener, OnMapTouchListener {
    private AMap aMap;
    private Context context;
    private Marker deviceMarker;
    LinkedListQueue<LatLng> flyLatLngs = new LinkedListQueue();
    private Polyline flyPolyLine;
    private Marker home;
    List<LatLng> latLngs = new ArrayList();
    Marker locationMarker;
    private float mAccuracy;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient mlocationClient;
    MyCancelCallback myCancelCallback = new MyCancelCallback();
    private Polyline polyline;
    Projection projection;
    boolean useMoveToLocationWithMapMode = true;

    class MyCancelCallback implements CancelableCallback {
        LatLng targetLatlng;

        MyCancelCallback() {
        }

        public void setTargetLatlng(LatLng latlng) {
            this.targetLatlng = latlng;
        }

        public void onFinish() {
            if (GaodeMapLocationManager.this.locationMarker != null && this.targetLatlng != null) {
                GaodeMapLocationManager.this.locationMarker.setPosition(this.targetLatlng);
            }
        }

        public void onCancel() {
            if (GaodeMapLocationManager.this.locationMarker != null && this.targetLatlng != null) {
                GaodeMapLocationManager.this.locationMarker.setPosition(this.targetLatlng);
            }
        }
    }

    public float getAccuracy() {
        return this.mAccuracy;
    }

    public GaodeMapLocationManager(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    public void setUpMap() {
        this.aMap.setLocationSource(this);
        this.aMap.setMyLocationEnabled(true);
        this.aMap.setOnMapTouchListener(this);
        Log.i("zdy", "setUpMap");
    }

    public void onResume() {
        this.useMoveToLocationWithMapMode = true;
        Log.i("zdy", "onResume");
    }

    public void onPause() {
        deactivate();
        this.useMoveToLocationWithMapMode = false;
        Log.i("zdy", "onPause");
    }

    public void onDestroy() {
        if (this.mlocationClient != null) {
            this.mlocationClient.onDestroy();
        }
        Log.i("zdy", "onDestroy");
    }

    public void onLocationChanged(AMapLocation amapLocation) {
        if (this.mListener != null && amapLocation != null) {
            if (amapLocation == null || amapLocation.getErrorCode() != 0) {
                Log.e("AmapErr", "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo());
                return;
            }
            LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
            this.mAccuracy = amapLocation.getAccuracy();
            if (this.locationMarker == null) {
                this.locationMarker = this.aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map)).anchor(0.5f, 0.5f));
                this.aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.5f));
            } else if (this.useMoveToLocationWithMapMode) {
                startMoveLocationAndMap(latLng);
            } else {
                startChangeLocation(latLng);
            }
        }
    }

    private void startChangeLocation(LatLng latLng) {
        if (this.locationMarker != null) {
            LatLng curLatlng = this.locationMarker.getPosition();
            if (curLatlng == null || !curLatlng.equals(latLng)) {
                this.locationMarker.setPosition(latLng);
            }
        }
    }

    private void startMoveLocationAndMap(LatLng latLng) {
        if (this.projection == null) {
            this.projection = this.aMap.getProjection();
        }
        if (!(this.locationMarker == null || this.projection == null)) {
            Point screenPosition = this.aMap.getProjection().toScreenLocation(this.locationMarker.getPosition());
            this.locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
        }
        this.myCancelCallback.setTargetLatlng(latLng);
        this.aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng), 1000, this.myCancelCallback);
    }

    public void onTouch(MotionEvent motionEvent) {
        Log.i("amap", "onTouch 关闭地图和小蓝点一起移动的模式");
        this.useMoveToLocationWithMapMode = false;
        Log.i("zdy", "onTouch");
    }

    public void activate(OnLocationChangedListener listener) {
        this.mListener = listener;
        if (this.mlocationClient == null) {
            Log.i("zdy", "activate");
            this.mlocationClient = new AMapLocationClient(this.context);
            this.mLocationOption = new AMapLocationClientOption();
            this.mlocationClient.setLocationListener(this);
            this.mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            this.mLocationOption.setInterval(2000);
            this.mlocationClient.setLocationOption(this.mLocationOption);
            this.mlocationClient.startLocation();
        }
    }

    public void deactivate() {
        Log.i("zdy", "deactivate");
        this.mListener = null;
        if (this.mlocationClient != null) {
            this.mlocationClient.stopLocation();
            this.mlocationClient.onDestroy();
        }
        this.mlocationClient = null;
    }

    public void onSensorChanged(float degree) {
        if (this.locationMarker != null) {
            float bearing = this.aMap.getCameraPosition().bearing;
            if (degree + bearing > 360.0f) {
                this.locationMarker.setRotateAngle((((-degree) + bearing) - 180.0f) + 90.0f);
            } else {
                this.locationMarker.setRotateAngle((((-degree) + bearing) + 180.0f) + 90.0f);
            }
        }
    }

    public void addHomeLocation(LatLng latLng) {
        if (this.home == null) {
            this.home = this.aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_point)).anchor(0.5f, 1.0f));
        } else {
            this.home.setPosition(latLng);
        }
    }

    public void chaneDeviceAngle(float angle) {
        this.deviceMarker.setRotateAngle((-angle) + this.aMap.getCameraPosition().bearing);
    }

    public void addDeviceLocation(LatLng latLng) {
        if (this.deviceMarker == null) {
            this.deviceMarker = this.aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_fly_handpiece_location)).anchor(0.5f, 0.5f));
        } else {
            this.deviceMarker.setPosition(latLng);
        }
    }

    public void clearMarker() {
        if (this.deviceMarker != null) {
            this.deviceMarker.remove();
            this.deviceMarker = null;
        }
        if (this.home != null) {
            this.home.remove();
            this.home = null;
        }
        if (this.polyline != null) {
            this.polyline.remove();
            this.polyline = null;
        }
        clearFlyPolyLine();
    }

    public void drawFlyLine() {
        if (this.locationMarker != null && this.deviceMarker != null) {
            LatLng latLngDevice = this.deviceMarker.getPosition();
            LatLng latLngMe = this.locationMarker.getPosition();
            this.latLngs.clear();
            this.latLngs.add(latLngDevice);
            this.latLngs.add(latLngMe);
            if (this.polyline == null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(this.latLngs);
                polylineOptions.width(10.0f).setDottedLine(false).geodesic(true).color(Color.argb(255, 1, 1, 1));
                if (this.polyline != null) {
                    this.polyline.remove();
                }
                this.polyline = this.aMap.addPolyline(polylineOptions);
            }
            this.polyline.setPoints(this.latLngs);
        }
    }

    public void addFlyPolyLine(double latitude, double logitude) {
        this.flyLatLngs.add(new LatLng(latitude, logitude));
        List<LatLng> latLngs = new ArrayList(this.flyLatLngs.getLinkedList());
        if (this.flyPolyLine == null) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(latLngs);
            polylineOptions.color(this.context.getResources().getColor(R.color.x8_drone_inface_line)).zIndex(50.0f);
            polylineOptions.width(4.0f);
            this.flyPolyLine = this.aMap.addPolyline(polylineOptions);
        }
        this.flyPolyLine.setPoints(latLngs);
    }

    public void clearFlyPolyLine() {
        if (this.flyPolyLine != null) {
            this.flyPolyLine.remove();
            this.flyPolyLine = null;
        }
        if (this.flyLatLngs != null && this.flyLatLngs.size() > 0) {
            this.flyLatLngs.removeAll();
        }
    }

    public void animatePersonLocation() {
        if (this.locationMarker != null) {
            this.aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.locationMarker.getPosition(), 18.0f));
        }
    }

    public LatLng getHomeLocation() {
        if (this.home != null) {
            return this.home.getPosition();
        }
        return null;
    }

    public LatLng getManLocation() {
        if (this.locationMarker != null) {
            return this.locationMarker.getPosition();
        }
        return null;
    }

    public LatLng getDevLocation() {
        if (this.deviceMarker != null) {
            return this.deviceMarker.getPosition();
        }
        return null;
    }
}
