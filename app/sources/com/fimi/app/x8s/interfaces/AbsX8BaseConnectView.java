package com.fimi.app.x8s.interfaces;

import android.app.Activity;
import android.view.View;
import com.fimi.x8sdk.modulestate.StateManager;

public class AbsX8BaseConnectView {
    protected boolean isConect = StateManager.getInstance().getX8Drone().isConnect();
    protected boolean isInSky = StateManager.getInstance().getX8Drone().isInSky();
    protected boolean isLowpower = StateManager.getInstance().getErrorCodeState().isLowPower();

    public AbsX8BaseConnectView(Activity activity, View parent) {
    }
}
