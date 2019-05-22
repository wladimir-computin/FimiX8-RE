package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.aifly.X8AiLineExcuteController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLinesHistoryUi;

public class X8AiLinesHistoryModule extends X8BaseModule {
    private X8AiLinesHistoryUi mUi;

    public void init(Activity activity, View rootView) {
        this.mUi = new X8AiLinesHistoryUi(activity, rootView);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mUi.setFcHeart(isInSky, isLowPower);
    }

    public void setController(X8AiLineExcuteController controller) {
        this.mUi.setController(controller);
    }
}
