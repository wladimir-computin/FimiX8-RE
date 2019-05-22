package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLinesConfirmUi;

public class X8AiLinesConfirmModule extends X8BaseModule {
    private X8AiLinesConfirmUi mUi;

    public void init(Activity activity, View rootView) {
        this.mUi = new X8AiLinesConfirmUi(activity, rootView);
    }

    public void setFcHeart(boolean isInSky, boolean isLowpower) {
        this.mUi.setFcHeart(isInSky, isLowpower);
    }

    public void setListener(X8MainAiFlyController mX8MainAiFlyController) {
        this.mUi.setX8MainAiFlyController(mX8MainAiFlyController);
    }
}
