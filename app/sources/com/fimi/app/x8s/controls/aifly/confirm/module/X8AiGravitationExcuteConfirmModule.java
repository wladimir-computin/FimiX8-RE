package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiGravitationExcuteComfirmUi;

public class X8AiGravitationExcuteConfirmModule extends X8BaseModule {
    private X8AiGravitationExcuteComfirmUi mX8AiGravitationExcuteComfirmUi;

    public void init(Activity activity, View rootView) {
        this.mX8AiGravitationExcuteComfirmUi = new X8AiGravitationExcuteComfirmUi(activity, rootView);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mX8AiGravitationExcuteComfirmUi.setFcHeart(isInSky, isLowPower);
    }
}
