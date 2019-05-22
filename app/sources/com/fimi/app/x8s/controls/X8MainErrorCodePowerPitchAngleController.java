package com.fimi.app.x8s.controls;

import android.view.View;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.x8sdk.entity.X8ErrorCodeInfo;
import java.util.List;

public class X8MainErrorCodePowerPitchAngleController extends AbsX8Controllers {
    private X8ErrorCodeController mX8ErrorCodeController;

    public X8ErrorCodeController getmX8ErrorCodeController() {
        return this.mX8ErrorCodeController;
    }

    public X8MainErrorCodePowerPitchAngleController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.handleView = rootView.findViewById(R.id.main_left_tools);
        this.mX8ErrorCodeController = new X8ErrorCodeController(rootView);
        if (this.mX8ErrorCodeController != null) {
            this.mX8ErrorCodeController.initSpeak();
        }
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void openUi() {
    }

    public void closeUi() {
    }

    public void onErrorCode(List<X8ErrorCodeInfo> list) {
        if (this.mX8ErrorCodeController != null) {
            this.mX8ErrorCodeController.onErrorCode(list);
        }
    }

    public void onDroneConnected(boolean b) {
        if (this.mX8ErrorCodeController != null) {
            this.mX8ErrorCodeController.onDroneConnected(b);
        }
    }

    public void setErrorViewEnableShow(boolean b) {
    }

    public boolean onClickBackKey() {
        return false;
    }
}
