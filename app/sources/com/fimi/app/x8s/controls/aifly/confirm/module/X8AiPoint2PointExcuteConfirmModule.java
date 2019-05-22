package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiPoint2PointExcuteConfirmUi;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.x8sdk.controller.FcManager;

public class X8AiPoint2PointExcuteConfirmModule extends X8BaseModule {
    private X8AiPoint2PointExcuteConfirmUi mUi;

    public void init(Activity activity, View rootView) {
        this.mUi = new X8AiPoint2PointExcuteConfirmUi(activity, rootView);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mUi.setFcHeart(isInSky, isLowPower);
    }

    public void setMapPoint(MapPointLatLng mapPoint) {
        this.mUi.setMapPoint(mapPoint);
    }

    public void setListener(IX8NextViewListener listener, FcManager fcManager) {
        this.mUi.setListener(listener, fcManager);
    }
}
