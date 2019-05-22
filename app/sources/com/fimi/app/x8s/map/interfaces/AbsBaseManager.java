package com.fimi.app.x8s.map.interfaces;

import android.graphics.Color;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.tools.X8MapCalcAngle;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.PatternItem;
import java.util.Arrays;
import java.util.List;

public abstract class AbsBaseManager {
    private static Dash DASH = new Dash((float) PATTERN_DASH_LENGTH_PX);
    private static Gap GAP = new Gap((float) PATTERN_GAP_LENGTH_PX);
    public static final int MAX_DISTANCE = 1000;
    public static List<PatternItem> PATTERN_DASHED = Arrays.asList(new PatternItem[]{DASH, GAP});
    private static int PATTERN_DASH_LENGTH_PX = 50;
    private static int PATTERN_GAP_LENGTH_PX = 20;
    protected int fillColor = Color.argb(0, 0, 0, 0);
    protected int lineDefaultColor = R.color.x8_ai_line_default;
    protected int lineRunColor = R.color.x8_ai_line_run;
    protected int lineRunningColor = R.color.x8_ai_line_runing;
    protected X8MapCalcAngle mapCalcAngle = new X8MapCalcAngle();
    private boolean onMapClickValid;
    private boolean onMarkerClickValid = true;
    protected int strokeColor = Color.argb(99, 255, 79, 0);
    protected int strokeWidth = 5;

    public abstract void drawAiLimit(double d, double d2, double d3);

    public abstract float getLineAngleByMapBealing(float f);

    public abstract void removeMapClickListener();

    public abstract void resetMapEvent();

    public abstract void setMarkerViewInfo(float f);

    public abstract void setOnMapClickListener();

    public boolean isOnMarkerClickValid() {
        return this.onMarkerClickValid;
    }

    public void setOnMarkerClickValid(boolean onMarkerClickValid) {
        this.onMarkerClickValid = onMarkerClickValid;
    }

    public boolean isOnMapClickValid() {
        return this.onMapClickValid;
    }

    public void setOnMapClickValid(boolean onMapClickValid) {
        this.onMapClickValid = onMapClickValid;
    }
}
