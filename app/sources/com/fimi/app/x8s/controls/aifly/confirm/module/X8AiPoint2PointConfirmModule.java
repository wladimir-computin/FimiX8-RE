package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiPoint2PointConfirmUi;

public class X8AiPoint2PointConfirmModule extends X8BaseModule {
    private X8AiPoint2PointConfirmUi mUi;

    public void init(Activity activity, View rootView) {
        this.mUi = new X8AiPoint2PointConfirmUi(activity, rootView);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mUi.setFcHeart(isInSky, isLowPower);
    }

    public void setListener(X8MainAiFlyController mX8MainAiFlyController) {
        this.mUi.setX8MainAiFlyController(mX8MainAiFlyController);
    }
}
