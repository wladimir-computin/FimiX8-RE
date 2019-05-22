package com.fimi.app.x8s.tools;

import java.lang.reflect.Array;

public class GpsPointTools {
    public double getDistance2D(double lat0, double lng0, double lat1, double lng1) {
        double dLat = lat0 - lat1;
        double dLng = lng0 - lng1;
        if (dLng > 180.0d) {
            dLng -= 360.0d;
        } else if (dLng < -180.0d) {
            dLng += 360.0d;
        }
        dLat *= 0.0174532925d;
        dLng *= 0.0174532925d;
        return (2.0d * 6378137.0d) * Math.asin(Math.sqrt(((Math.sin(0.5d * dLng) * Math.sin(0.5d * dLng)) * (Math.cos(0.0174532925d * lat0) * Math.cos(0.0174532925d * lat1))) + (Math.sin(0.5d * dLat) * Math.sin(0.5d * dLat))));
    }

    public double[][] gpsPointDrawArc(double latA, double lngA, double latB, double lngB, double latO, double lngO) {
        double dAngle;
        double[][] PointA_B = (double[][]) Array.newInstance(Double.TYPE, new int[]{30, 2});
        PointA_B[0][0] = latA;
        PointA_B[0][1] = lngA;
        PointA_B[29][0] = latB;
        PointA_B[29][1] = lngB;
        double startAngle = getDirectionAngle(latO, lngO, latA, lngA);
        double endAngle = getDirectionAngle(latO, lngO, latB, lngB);
        double R1 = Math.sqrt(((latO - latA) * (latO - latA)) + ((lngO - lngA) * (lngO - lngA)));
        double R = R1;
        double Rt = (R1 - Math.sqrt(((latO - latB) * (latO - latB)) + ((lngO - lngB) * (lngO - lngB)))) / ((double) 30);
        if (endAngle - startAngle > 0.0d) {
            dAngle = -(((360.0d - endAngle) + startAngle) / ((double) 36));
        } else {
            dAngle = (endAngle - startAngle) / ((double) 36);
        }
        for (int i = 1; i < 29; i++) {
            double tAngle = startAngle + (((double) i) * dAngle);
            if (tAngle > 360.0d) {
                tAngle -= 360.0d;
            }
            PointA_B[i][0] = ((R - (((double) i) * Rt)) * Math.sin((3.141592653589793d * tAngle) / 180.0d)) + latO;
            PointA_B[i][1] = ((R - (((double) i) * Rt)) * Math.cos((3.141592653589793d * tAngle) / 180.0d)) + lngO;
        }
        return PointA_B;
    }

    public double getDirectionAngle(double latA, double lngA, double latB, double lngB) {
        double bearing;
        double A_LngDeg = Math.ceil(lngA);
        double A_lngSec = ((lngA - A_LngDeg) - (Math.ceil((lngA - A_LngDeg) * 60.0d) / 60.0d)) * 3600.0d;
        double A_LatDeg = Math.ceil(latA);
        double A_latSec = ((latA - A_LatDeg) - (Math.ceil((latA - A_LatDeg) * 60.0d) / 60.0d)) * 3600.0d;
        double A_RadLng = (3.141592653589793d * lngA) / 180.0d;
        double A_RadLat = (3.141592653589793d * latA) / 180.0d;
        double A_Ec = 6356725.0d + (((6378137.0d - 6356725.0d) * (90.0d - latA)) / 90.0d);
        double A_Ed = A_Ec * Math.cos(A_RadLat);
        double B_LngDeg = Math.ceil(lngB);
        double B_lngSec = ((lngB - B_LngDeg) - (Math.ceil((lngB - B_LngDeg) * 60.0d) / 60.0d)) * 3600.0d;
        double B_LatDeg = Math.ceil(latB);
        double B_latSec = ((latB - B_LatDeg) - (Math.ceil((latB - B_LatDeg) * 60.0d) / 60.0d)) * 3600.0d;
        double B_RadLat = (3.141592653589793d * latB) / 180.0d;
        double B_Ed = (6356725.0d + (((6378137.0d - 6356725.0d) * (90.0d - latB)) / 90.0d)) * Math.cos(B_RadLat);
        double dLng = lngB - lngA;
        double dLat = latB - latA;
        double angle = (Math.atan(Math.abs(((((3.141592653589793d * lngB) / 180.0d) - A_RadLng) * A_Ed) / ((B_RadLat - A_RadLat) * A_Ec))) * 180.0d) / 3.141592653589793d;
        if (dLng > 0.0d && dLat <= 0.0d) {
            bearing = (90.0d - angle) + 90.0d;
        } else if (dLng <= 0.0d && dLat < 0.0d) {
            bearing = angle + 180.0d;
        } else if (dLng >= 0.0d || dLat < 0.0d) {
            bearing = angle;
        } else {
            bearing = (90.0d - angle) + 270.0d;
        }
        bearing = (360.0d - bearing) + 90.0d;
        if (bearing > 360.0d) {
            return bearing - 360.0d;
        }
        return bearing;
    }

    public double[] getSymmetryPoint(double latA, double lngA, double latO, double lngO) {
        return new double[]{(2.0d * latO) - latA, (2.0d * lngO) - lngA};
    }
}
