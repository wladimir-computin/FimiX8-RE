package com.fimi.x8sdk.map;

import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.map.amap.AMapShowInfo;
import com.fimi.x8sdk.map.googlemap.GoogleMapShowInfo;

public class MapProxy {
    public static MapProxy proxy = null;
    IShowInfo showInfo = null;

    public MapProxy() {
        GlobalConfig globalConfig = GlobalConfig.getInstance();
        if (globalConfig.getMapType() == null) {
            throw new IllegalArgumentException("maptype is not null");
        }
        switch (globalConfig.getMapType()) {
            case AMap:
                this.showInfo = new AMapShowInfo();
                return;
            case GoogleMap:
                this.showInfo = new GoogleMapShowInfo();
                return;
            default:
                return;
        }
    }

    public static MapProxy getInstance() {
        if (proxy == null) {
            synchronized (MapProxy.class) {
                if (proxy == null) {
                    proxy = new MapProxy();
                }
            }
        }
        return proxy;
    }

    public FLatLng getHomePosition(double longitude, double latitude) {
        return this.showInfo.getHomePosition(longitude, longitude);
    }
}
