package com.fimi.x8sdk.controller;

import android.content.Context;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.ivew.IGbAciton;
import com.fimi.x8sdk.presenter.X8GimbalPresenter;

public class X8GimbalManager {
    IGbAciton gbAction = new X8GimbalPresenter();

    public void setContext(Context context) {
        ((X8GimbalPresenter) this.gbAction).setContext(context);
    }

    public void setAiAutoPhotoPitchAngle(int angle, UiCallBackListener listener) {
        this.gbAction.setAiAutoPhotoPitchAngle(angle, listener);
    }

    public void setHorizontalAdjust(float angle, UiCallBackListener listener) {
        this.gbAction.setHorizontalAdjust(angle, listener);
    }

    public void getHorizontalAdjust(UiCallBackListener listener) {
        this.gbAction.getHorizontalAdjust(listener);
    }

    public void setPitchSpeed(int speed, UiCallBackListener listener) {
        this.gbAction.setPitchSpeed(speed, listener);
    }

    public void getPitchSpeed(UiCallBackListener listener) {
        this.gbAction.getPitchSpeed(listener);
    }

    public void resetGCParams(UiCallBackListener listener) {
        this.gbAction.restGcSystemParams(listener);
    }

    public void getGcParams(UiCallBackListener listener) {
        this.gbAction.getGcParams(listener);
    }

    public void setGcParams(int value, float param, UiCallBackListener listener) {
        this.gbAction.setGcParams(value, param, listener);
    }

    public void setGcGain(int value, UiCallBackListener listener) {
        this.gbAction.setGcGain(value, listener);
    }

    public void getGcGain(UiCallBackListener listener) {
        this.gbAction.getGcGain(listener);
    }
}
