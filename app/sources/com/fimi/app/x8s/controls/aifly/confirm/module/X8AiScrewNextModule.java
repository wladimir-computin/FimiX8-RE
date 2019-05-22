package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.aifly.X8AiScrewExcuteController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiScrewNextUi;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AutoFcSportState;

public class X8AiScrewNextModule extends X8BaseModule {
    private X8AiScrewNextUi mUi;

    public void init(Activity activity, View rootView) {
        this.mUi = new X8AiScrewNextUi(activity, rootView);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
    }

    public void showSportState(AutoFcSportState state) {
        this.mUi.showSportState(state);
    }

    public void setListener(IX8NextViewListener mIX8NextViewListener, FcManager fcManager, X8AiScrewExcuteController x8AiScrewExcuteController, float radius, float height) {
        this.mUi.setListener(mIX8NextViewListener, fcManager, x8AiScrewExcuteController, radius, height);
    }
}
