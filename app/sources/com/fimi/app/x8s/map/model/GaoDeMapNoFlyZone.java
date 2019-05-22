package com.fimi.app.x8s.map.model;

import android.support.v4.internal.view.SupportMenu;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BaseHoleOptions;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleHoleOptions;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonHoleOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.fimi.app.x8s.map.interfaces.AbsMapNoFlyZone;
import java.util.ArrayList;
import java.util.List;

public class GaoDeMapNoFlyZone extends AbsMapNoFlyZone {
    private AMap aMap;
    private List<Circle> circleList = new ArrayList();
    private List<Polygon> polygonList = new ArrayList();

    public GaoDeMapNoFlyZone(AMap aMap) {
        this.aMap = aMap;
    }

    public void drawCandyNoFlyZone(LatLng[] lats) {
        int i;
        PolygonOptions options = new PolygonOptions();
        List<LatLng> latLngsNofly = new ArrayList();
        LatLng O = lats[0];
        LatLng D1 = lats[1];
        LatLng B1 = lats[2];
        LatLng C1 = lats[3];
        LatLng A1 = lats[4];
        LatLng A2 = lats[5];
        LatLng C2 = lats[6];
        LatLng B2 = lats[7];
        LatLng D2 = lats[8];
        double[] sysPoint = this.mGpsPointTools.getSymmetryPoint(A1.latitude, A1.longitude, O.latitude, O.longitude);
        LatLng latLng = new LatLng(sysPoint[0], sysPoint[1]);
        sysPoint = this.mGpsPointTools.getSymmetryPoint(C1.latitude, C1.longitude, O.latitude, O.longitude);
        latLng = new LatLng(sysPoint[0], sysPoint[1]);
        sysPoint = this.mGpsPointTools.getSymmetryPoint(B1.latitude, B1.longitude, O.latitude, O.longitude);
        latLng = new LatLng(sysPoint[0], sysPoint[1]);
        sysPoint = this.mGpsPointTools.getSymmetryPoint(B2.latitude, B2.longitude, O.latitude, O.longitude);
        latLng = new LatLng(sysPoint[0], sysPoint[1]);
        sysPoint = this.mGpsPointTools.getSymmetryPoint(C2.latitude, C2.longitude, O.latitude, O.longitude);
        latLng = new LatLng(sysPoint[0], sysPoint[1]);
        sysPoint = this.mGpsPointTools.getSymmetryPoint(A2.latitude, A2.longitude, O.latitude, O.longitude);
        latLng = new LatLng(sysPoint[0], sysPoint[1]);
        sysPoint = this.mGpsPointTools.getSymmetryPoint(D1.latitude, D1.longitude, O.latitude, O.longitude);
        latLng = new LatLng(sysPoint[0], sysPoint[1]);
        sysPoint = this.mGpsPointTools.getSymmetryPoint(D2.latitude, D2.longitude, O.latitude, O.longitude);
        latLng = new LatLng(sysPoint[0], sysPoint[1]);
        latLngsNofly.add(A1);
        latLngsNofly.add(A2);
        double[][] pointArcs = this.mGpsPointTools.gpsPointDrawArc(C2.latitude, C2.longitude, B2.latitude, B2.longitude, O.latitude, O.longitude);
        for (i = 0; i < pointArcs.length; i++) {
            latLngsNofly.add(new LatLng(pointArcs[i][0], pointArcs[i][1]));
        }
        pointArcs = this.mGpsPointTools.gpsPointDrawArc(latLng.latitude, latLng.longitude, latLng.latitude, latLng.longitude, O.latitude, O.longitude);
        for (i = 0; i < pointArcs.length; i++) {
            latLngsNofly.add(new LatLng(pointArcs[i][0], pointArcs[i][1]));
        }
        latLngsNofly.add(latLng);
        latLngsNofly.add(latLng);
        pointArcs = this.mGpsPointTools.gpsPointDrawArc(latLng.latitude, latLng.longitude, latLng.latitude, latLng.longitude, O.latitude, O.longitude);
        for (i = 0; i < pointArcs.length; i++) {
            latLngsNofly.add(new LatLng(pointArcs[i][0], pointArcs[i][1]));
        }
        pointArcs = this.mGpsPointTools.gpsPointDrawArc(B1.latitude, B1.longitude, C1.latitude, C1.longitude, O.latitude, O.longitude);
        for (i = 0; i < pointArcs.length; i++) {
            latLngsNofly.add(new LatLng(pointArcs[i][0], pointArcs[i][1]));
        }
        options.strokeWidth(0.0f).strokeColor(this.strokeColor).fillColor(this.fillColor);
        options.addAll(latLngsNofly);
        List<LatLng> latLngs = new ArrayList();
        latLngs.add(D1);
        latLngs.add(D2);
        latLngs.add(latLng);
        latLngs.add(latLng);
        Polygon polygonLimiHight = this.aMap.addPolygon(new PolygonOptions().addAll(latLngs).fillColor(this.fillColorHeightLimit).strokeColor(SupportMenu.CATEGORY_MASK).strokeWidth(0.0f));
        this.polygonList.add(polygonLimiHight);
        PolygonHoleOptions holeOptions = new PolygonHoleOptions();
        holeOptions.addAll(latLngsNofly);
        polygonLimiHight.setHoleOptions(null);
        List<BaseHoleOptions> list = new ArrayList();
        list.add(holeOptions);
        polygonLimiHight.setHoleOptions(list);
        this.polygonList.add(this.aMap.addPolygon(options));
    }

    public void drawCircleNoFlyZone(LatLng lats, int radius) {
        Circle circleLimitHight = this.aMap.addCircle(new CircleOptions().center(lats).radius((double) (radius + 1000)).strokeColor(this.strokeColor).fillColor(this.fillColorHeightLimit).strokeWidth(0.0f));
        this.circleList.add(circleLimitHight);
        CircleHoleOptions circleHoleOptions = new CircleHoleOptions();
        circleHoleOptions.center(lats).radius((double) radius);
        circleLimitHight.setHoleOptions(null);
        List<BaseHoleOptions> list = new ArrayList();
        list.add(circleHoleOptions);
        circleLimitHight.setHoleOptions(list);
        this.circleList.add(this.aMap.addCircle(new CircleOptions().center(lats).radius((double) radius).strokeColor(this.fillColorHeightLimit).fillColor(this.fillColor)));
    }

    public void drawIrregularNoFlyZone(LatLng[] lats, boolean isNoFly) {
        PolygonOptions options = new PolygonOptions();
        options.strokeWidth(0.0f).strokeColor(this.strokeColor);
        if (isNoFly) {
            options.fillColor(this.fillColor);
        } else {
            options.fillColor(this.fillColorHeightLimit);
        }
        for (LatLng add : lats) {
            options.add(add);
        }
        this.polygonList.add(this.aMap.addPolygon(options));
    }

    public void clearNoFlightZone() {
        if (this.polygonList != null) {
            for (Polygon polygon : this.polygonList) {
                polygon.remove();
            }
            this.polygonList.clear();
        }
        if (this.circleList != null) {
            for (Circle circle : this.circleList) {
                circle.remove();
            }
            this.circleList.clear();
        }
    }
}
