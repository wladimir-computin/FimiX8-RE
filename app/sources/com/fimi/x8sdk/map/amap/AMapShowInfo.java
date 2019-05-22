package com.fimi.x8sdk.map.amap;

import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.map.IShowInfo;
import com.fimi.x8sdk.util.GpsCorrect;

public class AMapShowInfo implements IShowInfo {
    public FLatLng getHomePosition(double longitude, double latitude) {
        if (GlobalConfig.getInstance().isRectification()) {
            return GpsCorrect.Earth_To_Mars(latitude, longitude);
        }
        return new FLatLng(latitude, longitude);
    }
}
