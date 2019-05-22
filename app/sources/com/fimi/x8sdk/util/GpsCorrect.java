package com.fimi.x8sdk.util;

import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.map.MapType;

public class GpsCorrect {
    static final double a = 6378245.0d;
    static final double ee = 0.006693421622965943d;
    static final double pi = 3.141592653589793d;

    public static void transform(double wgLat, double wgLon, double[] latlng) {
        if (outOfChina(wgLat, wgLon)) {
            latlng[0] = wgLat;
            latlng[1] = wgLon;
            return;
        }
        double dLat = transformLat(wgLon - 105.0d, wgLat - 35.0d);
        double dLon = transformLon(wgLon - 105.0d, wgLat - 35.0d);
        double radLat = (wgLat / 180.0d) * pi;
        double magic = Math.sin(radLat);
        magic = 1.0d - ((ee * magic) * magic);
        double sqrtMagic = Math.sqrt(magic);
        dLon = (180.0d * dLon) / (((a / sqrtMagic) * Math.cos(radLat)) * pi);
        latlng[0] = wgLat + ((180.0d * dLat) / ((6335552.717000426d / (magic * sqrtMagic)) * pi));
        latlng[1] = wgLon + dLon;
    }

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004d || lon > 137.8347d || lat < 0.8293d || lat > 55.8271d) {
            return true;
        }
        return false;
    }

    private static double transformLat(double x, double y) {
        return (((((((-100.0d + (2.0d * x)) + (3.0d * y)) + ((0.2d * y) * y)) + ((0.1d * x) * y)) + (0.2d * Math.sqrt(Math.abs(x)))) + ((((20.0d * Math.sin((6.0d * x) * pi)) + (20.0d * Math.sin((2.0d * x) * pi))) * 2.0d) / 3.0d)) + ((((20.0d * Math.sin(pi * y)) + (40.0d * Math.sin((y / 3.0d) * pi))) * 2.0d) / 3.0d)) + ((((160.0d * Math.sin((y / 12.0d) * pi)) + (320.0d * Math.sin((pi * y) / 30.0d))) * 2.0d) / 3.0d);
    }

    private static double transformLon(double x, double y) {
        return (((((((300.0d + x) + (2.0d * y)) + ((0.1d * x) * x)) + ((0.1d * x) * y)) + (0.1d * Math.sqrt(Math.abs(x)))) + ((((20.0d * Math.sin((6.0d * x) * pi)) + (20.0d * Math.sin((2.0d * x) * pi))) * 2.0d) / 3.0d)) + ((((20.0d * Math.sin(pi * x)) + (40.0d * Math.sin((x / 3.0d) * pi))) * 2.0d) / 3.0d)) + ((((150.0d * Math.sin((x / 12.0d) * pi)) + (300.0d * Math.sin((x / 30.0d) * pi))) * 2.0d) / 3.0d);
    }

    public static FLatLng Earth_To_Mars(double srclat, double srclon) {
        if (GlobalConfig.getInstance().getMapType() != MapType.AMap) {
            return new FLatLng(srclat, srclon);
        }
        if (!GlobalConfig.getInstance().isRectification()) {
            return new FLatLng(srclat, srclon);
        }
        double dLat = transformLat(srclon - 105.0d, srclat - 35.0d);
        double dLon = transformLon(srclon - 105.0d, srclat - 35.0d);
        double radLat = (srclat / 180.0d) * pi;
        double magic = Math.sin(radLat);
        magic = 1.0d - ((ee * magic) * magic);
        double sqrtMagic = Math.sqrt(magic);
        return new FLatLng(srclat + ((180.0d * dLat) / ((6335552.717000426d / (magic * sqrtMagic)) * pi)), srclon + ((180.0d * dLon) / (((a / sqrtMagic) * Math.cos(radLat)) * pi)));
    }

    public static FLatLng Mars_To_Earth0(double srclat, double srclon) {
        if (GlobalConfig.getInstance().getMapType() != MapType.AMap) {
            return new FLatLng(srclat, srclon);
        }
        if (!GlobalConfig.getInstance().isRectification()) {
            return new FLatLng(srclat, srclon);
        }
        double dLat = transformLat(srclon - 105.0d, srclat - 35.0d);
        double dLon = transformLon(srclon - 105.0d, srclat - 35.0d);
        double radLat = (srclat / 180.0d) * pi;
        double magic = Math.sin(radLat);
        magic = 1.0d - ((ee * magic) * magic);
        double sqrtMagic = Math.sqrt(magic);
        return new FLatLng(srclat - ((180.0d * dLat) / ((6335552.717000426d / (magic * sqrtMagic)) * pi)), srclon - ((180.0d * dLon) / (((a / sqrtMagic) * Math.cos(radLat)) * pi)));
    }

    static double Get_Distance2(double lng0, double lat0, double lng1, double lat1) {
        float scalelongdown0 = (float) Math.cos((double) ((float) (Math.abs(lat0) * 0.0174532925d)));
        double tmp0 = (double) (((float) (lng0 - lng1)) * scalelongdown0);
        double tmp1 = (double) ((float) (lat0 - lat1));
        return Math.sqrt((((1.0E7d * tmp0) * (1.0E7d * tmp0)) + ((1.0E7d * tmp1) * (1.0E7d * tmp1))) * 1.2392029762268066d);
    }

    public static FLatLng Mars_To_Earth(double precision, double srclat, double srclon) {
        FLatLng tmplLatitudelongitude = new FLatLng();
        FLatLng tmpLatitudelongitude1 = new FLatLng();
        FLatLng dstlatlon = new FLatLng();
        tmpLatitudelongitude1.latitude = srclat;
        tmpLatitudelongitude1.longitude = srclon;
        tmplLatitudelongitude = Mars_To_Earth0(tmpLatitudelongitude1.latitude, tmpLatitudelongitude1.longitude);
        tmpLatitudelongitude1 = Earth_To_Mars(tmplLatitudelongitude.latitude, tmplLatitudelongitude.longitude);
        do {
            double deltelat = srclat - tmpLatitudelongitude1.latitude;
            dstlatlon.longitude = tmplLatitudelongitude.longitude + (srclon - tmpLatitudelongitude1.longitude);
            dstlatlon.latitude = tmplLatitudelongitude.latitude + deltelat;
            tmplLatitudelongitude.longitude = dstlatlon.longitude;
            tmplLatitudelongitude.latitude = dstlatlon.latitude;
            tmpLatitudelongitude1 = Earth_To_Mars(tmplLatitudelongitude.latitude, tmplLatitudelongitude.longitude);
        } while (precision < Get_Distance2(srclon, srclat, tmpLatitudelongitude1.longitude, tmpLatitudelongitude1.latitude));
        return dstlatlon;
    }
}
