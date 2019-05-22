package com.fimi.app.x8s.map.interfaces;

public abstract class AbsAiSurroundManager extends AbsBaseManager {
    public abstract void addPolylinescircle(boolean z, double d, double d2, double d3, double d4, int i, int i2);

    public abstract void clearSurroundMarker();

    public abstract float getSurroundRadius(double d, double d2, double d3, double d4);

    public abstract void reSetAiSurroundCircle(double d, double d2, float f);

    public abstract void setAiSurroundCircle(double d, double d2, float f);

    public abstract void setAiSurroundMark(double d, double d2);

    public void setMarkerViewInfo(float height) {
    }
}
