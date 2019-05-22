package com.fimi.x8sdk.ivew;

import com.fimi.kernel.dataparser.usb.UiCallBackListener;

public interface IGbAciton {
    void getGcGain(UiCallBackListener uiCallBackListener);

    void getGcParams(UiCallBackListener uiCallBackListener);

    void getHorizontalAdjust(UiCallBackListener uiCallBackListener);

    void getPitchSpeed(UiCallBackListener uiCallBackListener);

    void restGcSystemParams(UiCallBackListener uiCallBackListener);

    void setAiAutoPhotoPitchAngle(int i, UiCallBackListener uiCallBackListener);

    void setGcGain(int i, UiCallBackListener uiCallBackListener);

    void setGcParams(int i, float f, UiCallBackListener uiCallBackListener);

    void setHorizontalAdjust(float f, UiCallBackListener uiCallBackListener);

    void setPitchSpeed(int i, UiCallBackListener uiCallBackListener);
}
