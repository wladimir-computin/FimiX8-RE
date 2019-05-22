package com.fimi.app.x8s.map.model;

import com.fimi.app.x8s.map.interfaces.AbsMapNoFlyZone;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.ArrayList;
import java.util.List;

public class GglMapNoFlyZone extends AbsMapNoFlyZone {
    private List<Circle> circleList = new ArrayList();
    private GoogleMap googleMap;
    private List<Polygon> polygonList = new ArrayList();

    public GglMapNoFlyZone(GoogleMap map) {
        this.googleMap = map;
    }

    public void drawCandyNoFlyZone(LatLng[] lats) {
        PolygonOptions options = new PolygonOptions();
    }

    public void drawCircleNoFlyZone(LatLng lats, int radius) {
    }

    public void drawIrregularNoFlyZone(LatLng[] lats, boolean isNoFly) {
    }

    public void clearNoFlightZone() {
    }
}
