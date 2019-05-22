package com.fimi.x8sdk.ivew;

import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;

public interface ICamJsonAction {
    void defaltSystem(JsonUiCallBackListener jsonUiCallBackListener);

    void deleteOnlineFile(String str, JsonUiCallBackListener jsonUiCallBackListener);

    void formatTFCard(JsonUiCallBackListener jsonUiCallBackListener);

    void getCameraAwb();

    void getCameraEV();

    void getCameraFocuse(JsonUiCallBackListener jsonUiCallBackListener);

    void getCameraFocuseValues(JsonUiCallBackListener jsonUiCallBackListener);

    void getCameraISO();

    void getCameraIsoOptions();

    void getCameraKeyOptions(String str);

    void getCameraKeyOptions(String str, JsonUiCallBackListener jsonUiCallBackListener);

    void getCameraShutter();

    void getCameraShutterOptions();

    void getCurAllParams(JsonUiCallBackListener jsonUiCallBackListener);

    void getCurCameraParams(String str, JsonUiCallBackListener jsonUiCallBackListener);

    void setCameraDeControl(String str, JsonUiCallBackListener jsonUiCallBackListener);

    void setCameraEV(String str);

    void setCameraFocuse(String str, JsonUiCallBackListener jsonUiCallBackListener);

    void setCameraIso(String str);

    void setCameraKeyParams(String str, String str2, JsonUiCallBackListener jsonUiCallBackListener);

    void setCameraShutterTime(String str);

    void setCameraSys(String str);

    void setInterestMetering(String str);

    void setPhotoFormat(String str);

    void setPhotoSize(String str);

    void setVideoResolution(String str);

    void startSession();
}
