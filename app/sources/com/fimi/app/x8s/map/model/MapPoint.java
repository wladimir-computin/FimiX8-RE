package com.fimi.app.x8s.map.model;

import com.amap.api.maps.model.LatLng;
import com.fimi.app.x8s.enums.NoFlyZoneEnum;
import java.util.ArrayList;
import java.util.List;

public class MapPoint {
    LatLng A1;
    LatLng A2;
    LatLng B1;
    LatLng B2;
    LatLng C1;
    LatLng C2;
    LatLng D1;
    LatLng D2;
    LatLng center;
    private boolean isNoFly;
    List<LatLng> latLngs = new ArrayList();
    private int limitHight;
    int nfzType;
    private MapPointLatLng[] points;
    private int radius;
    private NoFlyZoneEnum type;

    public boolean isNoFly() {
        return this.isNoFly;
    }

    public void setNoFly(boolean noFly) {
        this.isNoFly = noFly;
    }

    public int getNfzType() {
        return this.nfzType;
    }

    public void setNfzType(int nfzType) {
        this.nfzType = nfzType;
    }

    public NoFlyZoneEnum getType() {
        return this.type;
    }

    public void setType(NoFlyZoneEnum type) {
        this.type = type;
    }

    public int getLimitHight() {
        return this.limitHight;
    }

    public void setLimitHight(int limitHight) {
        this.limitHight = limitHight;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public MapPointLatLng[] getPoints() {
        return this.points;
    }

    public void setPoints(MapPointLatLng[] points) {
        this.points = points;
    }

    public LatLng getA1() {
        return this.A1;
    }

    public void setA1(LatLng a1) {
        this.A1 = a1;
    }

    public LatLng getA2() {
        return this.A2;
    }

    public void setA2(LatLng a2) {
        this.A2 = a2;
    }

    public LatLng getC1() {
        return this.C1;
    }

    public void setC1(LatLng c1) {
        this.C1 = c1;
    }

    public LatLng getC2() {
        return this.C2;
    }

    public void setC2(LatLng c2) {
        this.C2 = c2;
    }

    public LatLng getB1() {
        return this.B1;
    }

    public void setB1(LatLng b1) {
        this.B1 = b1;
    }

    public LatLng getB2() {
        return this.B2;
    }

    public void setB2(LatLng b2) {
        this.B2 = b2;
    }

    public LatLng getD1() {
        return this.D1;
    }

    public void setD1(LatLng d1) {
        this.D1 = d1;
    }

    public LatLng getD2() {
        return this.D2;
    }

    public void setD2(LatLng d2) {
        this.D2 = d2;
    }

    public LatLng getCenter() {
        return this.center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public List<LatLng> getLatLngs() {
        return this.latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }
}
