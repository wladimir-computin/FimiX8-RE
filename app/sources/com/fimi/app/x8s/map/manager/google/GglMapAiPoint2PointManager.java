package com.fimi.app.x8s.map.manager.google;

import android.content.Context;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.map.view.google.GglMapCustomMarkerView;
import com.fimi.app.x8s.tools.GeoTools;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.util.GpsCorrect;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

public class GglMapAiPoint2PointManager extends AbsAiPoint2PointManager implements OnMapClickListener, OnMarkerClickListener {
    private Context context;
    private GglMapLocationManager gglMapLocationManager;
    private GoogleMap googleMap;
    boolean isFollow;
    List<LatLng> latLngs = new ArrayList();
    private Circle limitCircle;
    private MapPointLatLng mp;
    private IX8MarkerListener point2PointMarkerSelectListener;
    private Marker pointMarker;
    private Polyline polyline;

    public GglMapAiPoint2PointManager(Context context, GoogleMap googleMap, GglMapLocationManager gglMapLocationManager) {
        this.context = context;
        this.googleMap = googleMap;
        this.gglMapLocationManager = gglMapLocationManager;
    }

    public void setMarkerViewInfo(float height) {
        if (this.pointMarker != null) {
            int res;
            this.mp.altitude = height;
            GglMapCustomMarkerView gdCustemMarkerView = new GglMapCustomMarkerView();
            if (this.mp.isSelect) {
                res = R.drawable.x8_img_ai_follow_point;
            } else {
                res = R.drawable.x8_img_ai_follow_point2;
            }
            this.pointMarker.setIcon(gdCustemMarkerView.createCustomMarkerViewForP2P(this.context, res, this.mp.altitude, this.mp.nPos));
        }
    }

    public float getLineAngleByMapBealing(float angle) {
        return 0.0f;
    }

    public void setOnMapClickListener() {
        setOnMarkerListener();
    }

    public void removeMapClickListener() {
        this.googleMap.setOnMapClickListener(null);
        this.googleMap.setOnMarkerClickListener(null);
    }

    public void resetMapEvent() {
        this.googleMap.setOnMapClickListener(null);
        this.googleMap.setOnMarkerClickListener(null);
    }

    public void drawAiLimit(double lat, double lng, double radiu) {
        if (this.limitCircle == null) {
            this.limitCircle = this.googleMap.addCircle(new CircleOptions().center(new LatLng(lat, lng)).radius(radiu).strokeColor(this.strokeColor).fillColor(this.fillColor).strokeWidth((float) this.strokeWidth));
            return;
        }
        this.limitCircle.setCenter(new LatLng(lat, lng));
    }

    public MapPointLatLng getMapPointLatLng() {
        if (this.pointMarker != null) {
            FLatLng mFlatlng = GpsCorrect.Mars_To_Earth0(this.pointMarker.getPosition().latitude, this.pointMarker.getPosition().longitude);
            this.mp.longitude = mFlatlng.longitude;
            this.mp.latitude = mFlatlng.latitude;
        }
        return this.mp;
    }

    public void setPoint2PointMarkerSelectListener(IX8MarkerListener listener) {
        this.point2PointMarkerSelectListener = listener;
    }

    public void clearPoint2PointMarker() {
        clearPointMark();
        this.isFollow = false;
    }

    private void clearPointMark() {
        if (this.pointMarker != null) {
            this.pointMarker.remove();
            this.pointMarker = null;
        }
        clearMarker();
    }

    public void clearMarker() {
        if (this.pointMarker != null) {
            this.pointMarker.remove();
            this.pointMarker = null;
        }
        if (this.limitCircle != null) {
            this.limitCircle.remove();
            this.limitCircle = null;
        }
        if (this.polyline != null) {
            this.polyline.remove();
            this.polyline = null;
        }
        if (this.latLngs != null) {
            this.latLngs.clear();
        }
        this.mp = null;
        this.gglMapLocationManager.clearFlyPolyLine();
    }

    public void setMarkerByDevice(double latitude, double logitude, int altitude) {
        if (this.gglMapLocationManager.getHomeLocation() != null) {
            addPointLatLng(new LatLng(latitude, logitude), 0.0f, this.gglMapLocationManager.getDevLocation());
            setMarkerViewInfo(((float) altitude) / 10.0f);
        }
    }

    public void changeLine() {
        if (this.isFollow && this.polyline != null) {
            changeDeviceLocation(this.gglMapLocationManager.getDevLocation());
        }
    }

    public void setRunning() {
        this.isFollow = false;
    }

    private void setOnMarkerListener() {
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setOnMarkerClickListener(this);
    }

    public void onMapClick(LatLng latLng) {
        onMapClickForAiP2P(latLng);
    }

    public void onMapClickForAiP2P(LatLng latLng) {
        if (this.gglMapLocationManager.getHomeLocation() != null) {
            float distance = (float) GeoTools.getDistance(latLng, this.gglMapLocationManager.getHomeLocation()).valueInMeters();
            if (0.0f <= distance && distance <= 1000.0f) {
                addPointLatLng(latLng, distance, this.gglMapLocationManager.getDevLocation());
            } else if (distance > 1000.0f) {
                ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far), new Object[]{X8NumberUtil.getDistanceNumberString(1000.0f, 0, true)}), 0);
            }
        }
    }

    public void calcDistance() {
        if (this.pointMarker != null) {
            this.mp.distance = (float) GeoTools.getDistance(this.pointMarker.getPosition(), this.gglMapLocationManager.getDevLocation()).valueInMeters();
        }
    }

    public void addPointLatLng(LatLng latLng, float distance, LatLng deviceLocation) {
        if (this.pointMarker == null) {
            this.mp = new MapPointLatLng();
            this.mp.altitude = 5.0f;
            if (StateManager.getInstance().getX8Drone().isConnect()) {
                int h = Math.round(StateManager.getInstance().getX8Drone().getHeight());
                if (h > 5) {
                    this.mp.altitude = (float) h;
                }
            }
            this.pointMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(new GglMapCustomMarkerView().createCustomMarkerViewForP2P(this.context, R.drawable.x8_img_ai_follow_point2, this.mp.altitude, this.mp.nPos)).anchor(0.5f, 0.9f).draggable(false));
            this.pointMarker.setDraggable(true);
            this.pointMarker.setTag(this.mp);
        } else {
            this.pointMarker.setPosition(latLng);
        }
        drawPointLine(deviceLocation);
        this.mp.distance = distance;
        if (this.point2PointMarkerSelectListener != null) {
            this.point2PointMarkerSelectListener.onMarkerSelect(true, this.mp.altitude, this.mp, false);
        }
        this.isFollow = true;
    }

    public void drawPointLine(LatLng latLngDevice) {
        if (this.pointMarker != null) {
            LatLng latLng = this.pointMarker.getPosition();
            this.latLngs.clear();
            this.latLngs.add(latLng);
            this.latLngs.add(latLngDevice);
            if (this.polyline == null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(this.latLngs);
                polylineOptions.color(this.context.getResources().getColor(R.color.x8_drone_inface_line)).zIndex(50.0f);
                polylineOptions.width(4.0f);
                if (this.polyline != null) {
                    this.polyline.remove();
                }
                this.polyline = this.googleMap.addPolyline(polylineOptions);
                this.polyline.setPattern(PATTERN_DASHED);
            }
            this.polyline.setPoints(this.latLngs);
        }
    }

    public void changeDeviceLocation(LatLng latLngDevice) {
        drawPointLine(latLngDevice);
    }

    public boolean onMarkerClick(Marker marker) {
        return true;
    }
}
