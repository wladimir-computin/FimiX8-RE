package com.fimi.app.x8s.map.manager.gaode;

import android.content.Context;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.map.view.gaode.GaoDeMapCustomMarkerView;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.util.GpsCorrect;
import java.util.ArrayList;
import java.util.List;

public class GaoDeMapAiPoint2PointManager extends AbsAiPoint2PointManager implements OnMapClickListener {
    private AMap aMap;
    private Context context;
    boolean isRuning;
    List<LatLng> latLngs = new ArrayList();
    private Circle limitCircle;
    private GaodeMapLocationManager mGaodeMapLocationManager;
    MapPointLatLng mp;
    private IX8MarkerListener point2PointMarkerSelectListener;
    private Marker pointMarker;
    private Polyline polyline;

    public void clearPoint2PointMarker() {
        clearPointMark();
    }

    public void setMarkerByDevice(double latitude, double logitude, int altitude) {
        if (this.mGaodeMapLocationManager.getHomeLocation() != null) {
            addPointLatLng(new LatLng(latitude, logitude), 0.0f, this.mGaodeMapLocationManager.getDevLocation());
            setMarkerViewInfo(((float) altitude) / 10.0f);
        }
    }

    public void changeLine() {
    }

    public void setRunning() {
        this.isRuning = true;
    }

    public void calcDistance() {
    }

    public GaoDeMapAiPoint2PointManager(Context context, AMap aMap, GaodeMapLocationManager mGaodeMapLocationManager) {
        this.context = context;
        this.aMap = aMap;
        this.mGaodeMapLocationManager = mGaodeMapLocationManager;
    }

    private void setOnMarkerListener() {
        this.aMap.setOnMapClickListener(this);
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
            this.pointMarker = this.aMap.addMarker(new MarkerOptions().position(latLng).icon(new GaoDeMapCustomMarkerView().createCustomMarkerViewForP2P(this.context, R.drawable.x8_img_ai_follow_point2, this.mp.altitude, this.mp.nPos)).anchor(0.5f, 0.9f).draggable(false));
            this.pointMarker.setObject(this.mp);
        } else {
            this.pointMarker.setPosition(latLng);
        }
        drawPointLine(deviceLocation);
        this.mp.distance = distance;
        if (this.point2PointMarkerSelectListener != null) {
            this.point2PointMarkerSelectListener.onMarkerSelect(true, this.mp.altitude, this.mp, false);
        }
    }

    private void clearPointMark() {
        this.isRuning = false;
        if (this.pointMarker != null) {
            this.pointMarker.remove();
            this.pointMarker = null;
        }
        clearMarker();
    }

    public MapPointLatLng getMapPointLatLng() {
        if (this.pointMarker != null) {
            FLatLng mFlatlng = GpsCorrect.Mars_To_Earth0(this.pointMarker.getPosition().latitude, this.pointMarker.getPosition().longitude);
            this.mp.longitude = mFlatlng.longitude;
            this.mp.latitude = mFlatlng.latitude;
        }
        return this.mp;
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
                polylineOptions.setDottedLine(true);
                polylineOptions.color(this.context.getResources().getColor(R.color.x8_drone_inface_line)).zIndex(50.0f);
                polylineOptions.width(10.0f);
                if (this.polyline != null) {
                    this.polyline.remove();
                }
                this.polyline = this.aMap.addPolyline(polylineOptions);
            }
            this.polyline.setPoints(this.latLngs);
        }
    }

    public void changeDeviceLocation(LatLng latLngDevice) {
        if (!this.isRuning) {
            drawPointLine(latLngDevice);
        }
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
    }

    public void setMarkerViewInfo(float height) {
        if (this.pointMarker != null) {
            int res;
            this.mp.altitude = height;
            GaoDeMapCustomMarkerView gdCustemMarkerView = new GaoDeMapCustomMarkerView();
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
        this.aMap.setOnMapClickListener(null);
        this.aMap.setOnMarkerClickListener(null);
    }

    public void resetMapEvent() {
        this.aMap.setOnMapClickListener(null);
        this.aMap.setOnMarkerClickListener(null);
    }

    public void drawAiLimit(double lat, double lng, double radiu) {
        if (this.limitCircle == null) {
            this.limitCircle = this.aMap.addCircle(new CircleOptions().center(new LatLng(lat, lng)).radius(radiu).strokeColor(this.strokeColor).fillColor(this.fillColor).strokeWidth((float) this.strokeWidth));
            return;
        }
        this.limitCircle.setCenter(new LatLng(lat, lng));
    }

    public void setPoint2PointMarkerSelectListener(IX8MarkerListener point2PointMarkerSelectListener) {
        this.point2PointMarkerSelectListener = point2PointMarkerSelectListener;
    }

    public void onMapClick(LatLng latLng) {
        onMapClickForAiP2P(latLng);
    }

    public void onMapClickForAiP2P(LatLng latLng) {
        if (this.mGaodeMapLocationManager.getHomeLocation() != null) {
            float distance = AMapUtils.calculateLineDistance(latLng, this.mGaodeMapLocationManager.getHomeLocation());
            if (10.0f <= distance && distance <= 500.0f) {
                addPointLatLng(latLng, distance, this.mGaodeMapLocationManager.getDevLocation());
            } else if (distance < 10.0f) {
                ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far1), new Object[]{X8NumberUtil.getDistanceNumberString(10.0f, 0, true)}), 0);
            } else if (distance > 500.0f) {
                ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far), new Object[]{X8NumberUtil.getDistanceNumberString(500.0f, 0, true)}), 0);
            }
        }
    }
}
