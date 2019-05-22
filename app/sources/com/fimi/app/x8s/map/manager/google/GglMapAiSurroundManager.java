package com.fimi.app.x8s.map.manager.google;

import android.content.Context;
import android.graphics.Point;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.map.interfaces.AbsAiSurroundManager;
import com.fimi.app.x8s.map.manager.gaode.GaodeMapLocationManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.map.view.google.GglMapCustomMarkerView;
import com.fimi.app.x8s.tools.GeoTools;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.util.GpsCorrect;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import java.util.ArrayList;
import java.util.List;

public class GglMapAiSurroundManager extends AbsAiSurroundManager {
    private Context context;
    private GglMapLocationManager gglMapLocationManager;
    private GoogleMap googleMap;
    List<LatLng> latLngs = new ArrayList();
    private Circle limitCircle;
    private Polyline line;
    private GaodeMapLocationManager mGaodeMapLocationManager;
    MapPointLatLng mp;
    private Marker pointMarker;
    private Polyline polyline;

    public GglMapAiSurroundManager(Context context, GoogleMap googleMap, GglMapLocationManager gglMapLocationManager) {
        this.context = context;
        this.googleMap = googleMap;
        this.gglMapLocationManager = gglMapLocationManager;
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
        if (this.line != null) {
            this.line.remove();
            this.line = null;
        }
        this.mp = null;
    }

    public void setAiSurroundMark(double latitude, double logitude) {
        FLatLng fLatLng = GpsCorrect.Earth_To_Mars(latitude, logitude);
        LatLng latLng = new LatLng(fLatLng.latitude, fLatLng.longitude);
        if (this.pointMarker == null) {
            BitmapDescriptor mBitmapDescriptor = new GglMapCustomMarkerView().createCustomMarkerView(this.context, R.drawable.x8_img_ai_follow_point2);
            this.mp = new MapPointLatLng();
            this.pointMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(true));
            this.pointMarker.setTag(this.mp);
            return;
        }
        this.pointMarker.setPosition(latLng);
    }

    public float getSurroundRadius(double lastLogitude, double lastLatitude, double currentLogitude, double currentLatitude) {
        FLatLng last = GpsCorrect.Earth_To_Mars(lastLatitude, lastLogitude);
        FLatLng currrent = GpsCorrect.Earth_To_Mars(currentLatitude, currentLogitude);
        return (float) GeoTools.getDistance(new LatLng(last.latitude, last.longitude), new LatLng(currrent.latitude, currrent.longitude)).valueInMeters();
    }

    public void setAiSurroundCircle(double latitude, double logitude, float radius) {
        FLatLng fLatLng = GpsCorrect.Earth_To_Mars(latitude, logitude);
        LatLng lats = new LatLng(fLatLng.latitude, fLatLng.longitude);
        drawAiLimit(lats.latitude, lats.longitude, (double) radius);
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
            this.limitCircle = this.googleMap.addCircle(new CircleOptions().center(new LatLng(lat, lng)).radius(radiu).strokeColor(this.strokeColor).fillColor(this.fillColor).strokeWidth((float) this.strokeWidth));
            return;
        }
        this.limitCircle.setCenter(new LatLng(lat, lng));
    }

    public void addPolylinescircle(boolean cw, double lat, double lng, double lat1, double lng2, int radius, int maxRadius) {
        if (this.line != null) {
            this.line.remove();
            this.line = null;
        }
        FLatLng centerpoint = GpsCorrect.Earth_To_Mars(lat, lng);
        FLatLng point = GpsCorrect.Earth_To_Mars(lat1, lng2);
        float angle = this.mapCalcAngle.getAngle2(new LatLng(centerpoint.latitude, centerpoint.longitude), new LatLng(point.latitude, point.longitude));
        PolylineOptions options = new PolylineOptions();
        int time = (int) Math.round((((double) (radius * 2)) * 3.141592653589793d) / 10.0d);
        if (time < 50) {
            time = 50;
        } else if (time > 180) {
            time = 180;
        }
        double temp = (double) ((((float) ((maxRadius + 10) - radius)) * 1.0f) / ((float) time));
        VisibleRegion visibleRegion = this.googleMap.getProjection().getVisibleRegion();
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        float scale = GeoTools.getScale(farLeft, nearRight, this.googleMap.getProjection().toScreenLocation(nearRight).x - this.googleMap.getProjection().toScreenLocation(farLeft).x);
        for (int i = 0; i < 360; i++) {
            float startAngle;
            float t = (float) (((double) scale) * (((double) radius) + (((double) (((float) i) / (360.0f / ((float) time)))) * temp)));
            if (cw) {
                startAngle = ((float) i) + angle;
            } else {
                float cc = angle - ((float) i);
                if (cc < 0.0f) {
                    startAngle = 360.0f + cc;
                } else {
                    startAngle = cc;
                }
            }
            Point p1 = this.googleMap.getProjection().toScreenLocation(new LatLng(centerpoint.latitude, centerpoint.longitude));
            options.add(this.googleMap.getProjection().fromScreenLocation(new Point((int) (((double) p1.x) + (((double) t) * Math.cos((((double) startAngle) * 3.141592653589793d) / 180.0d))), (int) (((double) p1.y) + (((double) t) * Math.sin((((double) startAngle) * 3.141592653589793d) / 180.0d))))));
        }
        this.line = this.googleMap.addPolyline(options.width(3.0f));
        this.line.setColor(this.context.getResources().getColor(R.color.x8_drone_inface_line));
    }

    public void reSetAiSurroundCircle(double latitude, double logitude, float radius) {
        if (this.limitCircle != null) {
            this.limitCircle.remove();
            this.limitCircle = null;
        }
        setAiSurroundCircle(latitude, logitude, radius);
    }
}
