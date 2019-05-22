package com.fimi.x8sdk.ivew;

import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.listener.CallBackParamListener;

public interface ICamAction {
    void endRecord(UiCallBackListener uiCallBackListener);

    void getTFCardState(CallBackParamListener callBackParamListener);

    void setInterestMetering(byte b, UiCallBackListener uiCallBackListener);

    void startRecord(UiCallBackListener uiCallBackListener);

    void switchPhotoMode(UiCallBackListener uiCallBackListener);

    void swithVideoMode(UiCallBackListener uiCallBackListener);

    void takePhoto(UiCallBackListener uiCallBackListener);
}
