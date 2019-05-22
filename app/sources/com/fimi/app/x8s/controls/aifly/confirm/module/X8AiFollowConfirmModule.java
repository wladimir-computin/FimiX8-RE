package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiFollowConfirmUi;

public class X8AiFollowConfirmModule extends X8BaseModule {
    private X8AiFollowConfirmUi mUi;

    public void init(Activity activity, View rootView) {
        this.mUi = new X8AiFollowConfirmUi(activity, rootView);
    }

    public void setListener(X8MainAiFlyController mX8MainAiFlyController) {
        this.mUi.setX8MainAiFlyController(mX8MainAiFlyController);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mUi.setFcHeart(isInSky, isLowPower);
    }
}
