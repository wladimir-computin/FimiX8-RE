package com.fimi.app.x8s.map.manager.gaode;

import android.content.Context;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.map.interfaces.AbsAiSurroundManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.map.view.gaode.GaoDeMapCustomMarkerView;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.util.GpsCorrect;
import java.util.ArrayList;
import java.util.List;

public class GaoDeMapAiSurroundManager extends AbsAiSurroundManager {
    private AMap aMap;
    private Context context;
    List<LatLng> latLngs = new ArrayList();
    private Circle limitCircle;
    private GaodeMapLocationManager mGaodeMapLocationManager;
    MapPointLatLng mp;
    private Marker pointMarker;
    private Polyline polyline;

    public GaoDeMapAiSurroundManager(Context context, AMap aMap, GaodeMapLocationManager mGaodeMapLocationManager) {
        this.context = context;
        this.aMap = aMap;
        this.mGaodeMapLocationManager = mGaodeMapLocationManager;
    }

    public void setAiSurroundMark(double latitude, double logitude) {
        FLatLng fLatLng = GpsCorrect.Earth_To_Mars(latitude, logitude);
        LatLng latLng = new LatLng(fLatLng.latitude, fLatLng.longitude);
        if (this.pointMarker == null) {
            BitmapDescriptor mBitmapDescriptor = new GaoDeMapCustomMarkerView().createCustomMarkerView(this.context, R.drawable.x8_img_ai_follow_point2);
            this.mp = new MapPointLatLng();
            this.pointMarker = this.aMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(true));
            this.pointMarker.setObject(this.mp);
            return;
        }
        this.pointMarker.setPosition(latLng);
    }

    public void setAiSurroundCircle(double latitude, double logitude, float radius) {
        FLatLng fLatLng = GpsCorrect.Earth_To_Mars(latitude, logitude);
        LatLng lats = new LatLng(fLatLng.latitude, fLatLng.longitude);
        drawAiLimit(lats.latitude, lats.longitude, (double) radius);
    }

    public void addPolylinescircle(boolean cw, double lat, double lng, double lat1, double lng2, int radius, int maxRadius) {
    }

    public void reSetAiSurroundCircle(double latitude, double logitude, float radius) {
    }

    public void addPolylinescircle(LatLng centerpoint, float radius) {
        PolylineOptions options = new PolylineOptions();
        double phase = 6.283185307179586d / ((double) 360);
        for (int i = 0; i < 360; i++) {
            double newlng = centerpoint.longitude + ((((double) radius) * Math.cos(((double) i) * phase)) / (((Math.cos((centerpoint.latitude * 3.141592653589793d) / 180.0d) * 6371000.79d) * 3.141592653589793d) / 180.0d));
            options.add(new LatLng(centerpoint.latitude + ((((double) radius) * Math.sin(((double) i) * phase)) / ((3.141592653589793d * 6371000.79d) / 180.0d)), newlng));
        }
        options.color(this.context.getResources().getColor(R.color.x8_drone_inface_line));
        this.polyline = this.aMap.addPolyline(options.width(10.0f).useGradient(true).setDottedLine(true));
    }

    public void clearSurroundMarker() {
        if (this.pointMarker != null) {
            this.pointMarker.remove();
            this.pointMarker = null;
        }
        if (this.polyline != null) {
            this.polyline.remove();
            this.polyline = null;
        }
        if (this.limitCircle != null) {
            this.limitCircle.remove();
            this.limitCircle = null;
        }
        if (this.latLngs != null) {
            this.latLngs.clear();
        }
        this.mp = null;
    }

    public float getSurroundRadius(double lastLogitude, double lastLatitude, double currentLogitude, double currentLatitude) {
        FLatLng last = GpsCorrect.Earth_To_Mars(lastLatitude, lastLogitude);
        FLatLng currrent = GpsCorrect.Earth_To_Mars(currentLatitude, currentLogitude);
        return AMapUtils.calculateLineDistance(new LatLng(last.latitude, last.longitude), new LatLng(currrent.latitude, currrent.longitude));
    }

    public float getLineAngleByMapBealing(float angle) {
        return 0.0f;
    }

    public void setOnMapClickListener() {
    }

    public void removeMapClickListener() {
    }

    public void resetMapEvent() {
    }

    public void drawAiLimit(double lat, double lng, double radiu) {
        if (this.limitCircle == null) {
            this.limitCircle = this.aMap.addCircle(new CircleOptions().center(new LatLng(lat, lng)).radius(radiu).strokeColor(this.strokeColor).fillColor(this.fillColor).strokeWidth((float) this.strokeWidth));
            return;
        }
        this.limitCircle.setCenter(new LatLng(lat, lng));
    }
}
