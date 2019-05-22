package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiReturnConfirmUi;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AutoFcSportState;

public class X8AiReturnConfirmModule extends X8BaseModule {
    private X8AiReturnConfirmUi mUi;

    public void init(Activity activity, View rootView) {
        this.mUi = new X8AiReturnConfirmUi(activity, rootView);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
    }

    public void setListener(X8MainAiFlyController mX8MainAiFlyController, FcCtrlManager mFcCtrlManager) {
        this.mUi.setX8MainAiFlyController(mX8MainAiFlyController, mFcCtrlManager);
    }

    public void showSportState(AutoFcSportState state) {
        this.mUi.showSportState(state);
    }
}
