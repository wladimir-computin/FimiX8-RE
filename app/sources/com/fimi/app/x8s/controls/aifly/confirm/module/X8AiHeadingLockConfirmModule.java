package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiHeadingLockConfirmUi;
import com.fimi.x8sdk.dataparser.AutoFcSportState;

public class X8AiHeadingLockConfirmModule extends X8BaseModule {
    private X8AiHeadingLockConfirmUi mUi;

    public void init(Activity activity, View rootView) {
        this.mUi = new X8AiHeadingLockConfirmUi(activity, rootView);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mUi.setFcHeart(isInSky, isLowPower);
    }

    public void setListener(X8MainAiFlyController mX8MainAiFlyController) {
        this.mUi.setX8MainAiFlyController(mX8MainAiFlyController);
    }

    public void showSportState(AutoFcSportState state) {
        this.mUi.showSportState(state);
    }
}
