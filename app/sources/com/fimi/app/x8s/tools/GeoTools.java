package com.fimi.app.x8s.tools;

import com.fimi.x8sdk.util.Length;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class GeoTools {
    private static final double RADIUS_OF_EARTH = 6372797.560856d;
    public List<LatLng> waypoints;

    public static Double getAproximatedDistance(LatLng p1, LatLng p2) {
        return Double.valueOf(Math.hypot(p1.latitude - p2.latitude, p1.longitude - p2.longitude));
    }

    public static Double metersTolat(double meters) {
        return Double.valueOf(Math.toDegrees(meters / 6378100.0d));
    }

    public static Double latToMeters(double lat) {
        return Double.valueOf(Math.toRadians(lat) * 6378100.0d);
    }

    public static LatLng newCoordFromBearingAndDistance(LatLng origin, double bearing, double distance) {
        double lat = origin.latitude;
        double lon = origin.longitude;
        double lat1 = Math.toRadians(lat);
        double lon1 = Math.toRadians(lon);
        double brng = Math.toRadians(bearing);
        double dr = distance / RADIUS_OF_EARTH;
        double lat2 = Math.asin((Math.sin(lat1) * Math.cos(dr)) + ((Math.cos(lat1) * Math.sin(dr)) * Math.cos(brng)));
        double lon2 = lon1 + Math.atan2((Math.sin(brng) * Math.sin(dr)) * Math.cos(lat1), Math.cos(dr) - (Math.sin(lat1) * Math.sin(lat2)));
        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }

    public static double getArcInRadians(LatLng from, LatLng to) {
        double latitudeArc = Math.toRadians(from.latitude - to.latitude);
        double longitudeArc = Math.toRadians(from.longitude - to.longitude);
        double latitudeH = Math.sin(0.5d * latitudeArc);
        latitudeH *= latitudeH;
        double lontitudeH = Math.sin(0.5d * longitudeArc);
        return Math.toDegrees(2.0d * Math.asin(Math.sqrt(((Math.cos(Math.toRadians(from.latitude)) * Math.cos(Math.toRadians(to.latitude))) * (lontitudeH * lontitudeH)) + latitudeH)));
    }

    public static Length getDistance(LatLng latLng, LatLng latLng2) {
        return new Length(RADIUS_OF_EARTH * Math.toRadians(getArcInRadians(latLng, latLng2)));
    }

    public static double getHeadingFromCoordinates(LatLng fromLoc, LatLng toLoc) {
        double fLat = Math.toRadians(fromLoc.latitude);
        double fLng = Math.toRadians(fromLoc.longitude);
        double tLat = Math.toRadians(toLoc.latitude);
        double tLng = Math.toRadians(toLoc.longitude);
        double degree = Math.toDegrees(Math.atan2(Math.sin(tLng - fLng) * Math.cos(tLat), (Math.cos(fLat) * Math.sin(tLat)) - ((Math.sin(fLat) * Math.cos(tLat)) * Math.cos(tLng - fLng))));
        return degree >= 0.0d ? degree : degree + 360.0d;
    }

    public static List<Polyline> drawDashedPolyLine(GoogleMap mMap, ArrayList<LatLng> listOfPoints, int color) {
        List<Polyline> tracklines = new ArrayList();
        boolean added = false;
        for (int i = 0; i < listOfPoints.size() - 1; i++) {
            double distance = getConvertedDistance((LatLng) listOfPoints.get(i), (LatLng) listOfPoints.get(i + 1));
            if (distance >= 0.02d) {
                int countOfDivisions = (int) (distance / 0.02d);
                double latdiff = (((LatLng) listOfPoints.get(i + 1)).latitude - ((LatLng) listOfPoints.get(i)).latitude) / ((double) countOfDivisions);
                double lngdiff = (((LatLng) listOfPoints.get(i + 1)).longitude - ((LatLng) listOfPoints.get(i)).longitude) / ((double) countOfDivisions);
                LatLng lastKnowLatLng = new LatLng(((LatLng) listOfPoints.get(i)).latitude, ((LatLng) listOfPoints.get(i)).longitude);
                for (int j = 0; j < countOfDivisions; j++) {
                    LatLng nextLatLng = new LatLng(lastKnowLatLng.latitude + latdiff, lastKnowLatLng.longitude + lngdiff);
                    if (added) {
                        added = false;
                    } else {
                        tracklines.add(mMap.addPolyline(new PolylineOptions().add(lastKnowLatLng).add(nextLatLng).color(color)));
                        added = true;
                    }
                    lastKnowLatLng = nextLatLng;
                }
            } else if (added) {
                added = false;
            } else {
                tracklines.add(mMap.addPolyline(new PolylineOptions().add((LatLng) listOfPoints.get(i)).add((LatLng) listOfPoints.get(i + 1)).color(color)));
                added = true;
            }
        }
        return tracklines;
    }

    private static double getConvertedDistance(LatLng latlng1, LatLng latlng2) {
        return new BigDecimal(getDistance(latlng1, latlng2).valueInMeters()).setScale(3, RoundingMode.DOWN).doubleValue();
    }

    public static float getScale(LatLng latLng, LatLng latLng2, int pix) {
        return ((float) pix) / ((float) getDistance(latLng, latLng2).valueInMeters());
    }
}
