package com.fimi.app.x8s.tools;

import com.google.android.gms.maps.model.LatLng;

public class X8MapCalcAngle {
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        return (toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude);
    }

    private float getSpecialAngle(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.latitude - fromPoint.latitude > 0.0d) {
            return 90.0f;
        }
        return 180.0f;
    }

    private float toSceenAngle(double angle, LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude - fromPoint.longitude >= 0.0d) {
            return (float) ((int) Math.round(90.0d - angle));
        }
        return (float) ((int) Math.round(180.0d + (90.0d - angle)));
    }

    public float getAngle(LatLng fromPoint, LatLng toPoint) {
        double angle;
        double k = getSlope(fromPoint, toPoint);
        if (k != Double.MAX_VALUE) {
            angle = (Math.atan(k) * 180.0d) / 3.141592653589793d;
        } else {
            angle = (double) getSpecialAngle(fromPoint, toPoint);
        }
        return (float) ((double) toSceenAngle(angle, fromPoint, toPoint));
    }

    public LatLng[] getLineLatLngInterval(LatLng fromPoint, LatLng toPoint, int count) {
        if (count < 2) {
            return null;
        }
        double k = getSlope(fromPoint, toPoint);
        LatLng[] latLng = new LatLng[(count - 1)];
        double d;
        int i;
        if (k != Double.MAX_VALUE) {
            d = (toPoint.longitude - fromPoint.longitude) / ((double) count);
            for (i = 1; i < count; i++) {
                double lng = fromPoint.longitude + (((double) i) * d);
                latLng[i - 1] = new LatLng(fromPoint.latitude - ((fromPoint.longitude - lng) * k), lng);
            }
            return latLng;
        }
        d = (toPoint.latitude - fromPoint.latitude) / ((double) count);
        for (i = 1; i < count; i++) {
            latLng[i - 1] = new LatLng(fromPoint.latitude + (((double) i) * d), toPoint.longitude);
        }
        return latLng;
    }

    public float[] getAnlgesByRoration(float angle1, float angle2, int roration) {
        angle1 = (angle1 + 360.0f) % 360.0f;
        angle2 = (angle2 + 360.0f) % 360.0f;
        float tempAngle;
        if (roration == 1) {
            tempAngle = angle2 - angle1;
            if (tempAngle < 0.0f) {
                tempAngle += 360.0f;
            }
            tempAngle /= 3.0f;
            angle1 = (angle1 + tempAngle) % 360.0f;
            angle2 = (((tempAngle * 2.0f) / 3.0f) + angle1) % 360.0f;
        } else if (roration == 2) {
            tempAngle = angle1 - angle2;
            if (tempAngle < 0.0f) {
                tempAngle += 360.0f;
            }
            tempAngle /= 3.0f;
            angle2 = (angle2 + tempAngle) % 360.0f;
            angle1 = (((tempAngle * 2.0f) / 3.0f) + angle2) % 360.0f;
        } else {
            tempAngle = Math.abs(angle2 - angle1);
            if (tempAngle > 180.0f) {
                tempAngle = 360.0f - tempAngle;
            }
            tempAngle /= 3.0f;
            if (angle2 > angle1) {
                angle1 = (angle1 + tempAngle) % 360.0f;
                angle2 = (((tempAngle * 2.0f) / 3.0f) + angle1) % 360.0f;
            } else if (Math.abs(angle2 - angle1) > 180.0f) {
                angle1 = (angle1 + tempAngle) % 360.0f;
                angle2 = (((tempAngle * 2.0f) / 3.0f) + angle1) % 360.0f;
            } else {
                angle2 = (angle2 + tempAngle) % 360.0f;
                angle1 = (((tempAngle * 2.0f) / 3.0f) + angle2) % 360.0f;
            }
        }
        return new float[]{switchScreenAngle2DroneAngle(angle1), switchScreenAngle2DroneAngle(angle2)};
    }

    public float switchScreenAngle2DroneAngle(float angle) {
        if (0.0f > angle || angle > 180.0f) {
            return angle - 360.0f;
        }
        return angle;
    }

    public float getAngle2(LatLng fromPoint, LatLng toPoint) {
        double angle;
        double k = getSlope(fromPoint, toPoint);
        if (k != Double.MAX_VALUE) {
            angle = (Math.atan(k) * 180.0d) / 3.141592653589793d;
        } else {
            angle = (double) getSpecialAngle(fromPoint, toPoint);
        }
        if (toPoint.longitude - fromPoint.longitude < 0.0d) {
            angle = 180.0d - angle;
        } else if (angle > 0.0d) {
            angle = 360.0d - angle;
        } else {
            angle = -angle;
        }
        return (float) angle;
    }
}
