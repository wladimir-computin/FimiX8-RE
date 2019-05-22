package com.fimi.app.x8s.interfaces;

import android.graphics.Rect;
import com.fimi.app.x8s.map.model.MapPointLatLng;

public interface IX8MarkerListener {
    Rect getDeletePosition();

    int getOration();

    void onInterestSizeEnable(boolean z);

    void onMarkerSelect(boolean z, float f, MapPointLatLng mapPointLatLng, boolean z2);

    void onMarkerSizeChange(int i);

    void onRunIndex(int i, int i2);
}
