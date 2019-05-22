package com.fimi.x8sdk.map.googlemap;

import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.map.IShowInfo;

public class GoogleMapShowInfo implements IShowInfo {
    public FLatLng getHomePosition(double longitude, double latitude) {
        return new FLatLng(latitude, longitude);
    }
}
