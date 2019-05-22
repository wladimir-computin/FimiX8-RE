package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.aifly.X8AiSurroundExcuteController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiSurroundToPointExcuteConfirmUi;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.x8sdk.controller.FcManager;

public class X8AiSurroundToPointExcuteConfirmModule extends X8BaseModule {
    private X8AiSurroundToPointExcuteConfirmUi mUi;

    public void init(Activity activity, View rootView, float radius) {
        this.mUi = new X8AiSurroundToPointExcuteConfirmUi(activity, rootView, radius);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mUi.setFcHeart(isInSky, isLowPower);
    }

    public void setListener(IX8NextViewListener listener, FcManager fcManager, X8AiSurroundExcuteController x8AiSurroundExcuteController) {
        this.mUi.setListener(listener, fcManager, x8AiSurroundExcuteController);
    }
}
