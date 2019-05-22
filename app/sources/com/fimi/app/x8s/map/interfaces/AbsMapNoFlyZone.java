package com.fimi.app.x8s.map.interfaces;

import android.graphics.Color;
import com.fimi.app.x8s.tools.GpsPointTools;

public abstract class AbsMapNoFlyZone {
    protected int fillColor = Color.argb(128, 255, 36, 0);
    protected int fillColorHeightLimit = Color.argb(179, 204, 204, 204);
    protected GpsPointTools mGpsPointTools = new GpsPointTools();
    protected int strokeColor = Color.argb(99, 255, 79, 0);
}
