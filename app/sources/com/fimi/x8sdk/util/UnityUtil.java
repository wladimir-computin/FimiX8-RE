package com.fimi.x8sdk.util;

public class UnityUtil {
    public static float meterToFoot(float meter) {
        return 3.2808f * meter;
    }

    public static float footToMeter(float foot) {
        return 0.3048f * foot;
    }

    public static float msToMph(float v) {
        return 2.2369f * v;
    }
}
