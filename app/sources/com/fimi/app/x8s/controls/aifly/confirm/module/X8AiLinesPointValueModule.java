package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.controls.aifly.X8AiLineExcuteController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLinesPointValueUi;
import com.fimi.app.x8s.map.model.MapPointLatLng;

public class X8AiLinesPointValueModule extends X8BaseModule {
    private X8AiLinesPointValueUi mUi;

    public void init(Activity activity, View rootView, int type, MapPointLatLng mpl, X8MapVideoController mapVideoController, X8AiLineExcuteController controller) {
        this.mUi = new X8AiLinesPointValueUi(activity, rootView, type, mpl, mapVideoController, controller);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mUi.setFcHeart(isInSky, isLowPower);
    }
}
