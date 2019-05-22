package com.fimi.x8sdk.controller;

import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.ivew.IFiveKeyAction;
import com.fimi.x8sdk.listener.IX8FiveKeyDefine;
import com.fimi.x8sdk.presenter.FiveKeyDefinePresenter;
import com.fimi.x8sdk.presenter.FiveKeyDefinePresenter.ParameterType;
import java.util.List;

public class FiveKeyDefineManager {
    FiveKeyDefinePresenter fiveKeyDefinePresenter;
    IFiveKeyAction iFiveKeyAction;

    public FiveKeyDefineManager(IX8FiveKeyDefine ix8FiveKeyDefine, CameraManager cameraManager) {
        this.iFiveKeyAction = new FiveKeyDefinePresenter(ix8FiveKeyDefine, cameraManager);
    }

    public String setCameraContrast(String paramKey, int param, ParameterType parameterType, JsonUiCallBackListener jsonUiCallBackListener) {
        return this.iFiveKeyAction.setCameraContrast(paramKey, param, parameterType, jsonUiCallBackListener);
    }

    public String setCameraSaturation(String paramKey, int param, ParameterType parameterType, JsonUiCallBackListener jsonUiCallBackListener) {
        return this.iFiveKeyAction.setCameraSaturation(paramKey, param, parameterType, jsonUiCallBackListener);
    }

    public String setFiveKeyCameraKeyParams(String paramKey, List list, String currentMeteringMode, JsonUiCallBackListener jsonUiCallBackListener) {
        return this.iFiveKeyAction.setFiveKeyCameraKeyParams(paramKey, list, currentMeteringMode, jsonUiCallBackListener);
    }

    public void restoreUpDownKey(boolean isRestore) {
        this.iFiveKeyAction.restoreUpDownKey(isRestore);
    }

    public void isSetCameraContrast() {
        this.iFiveKeyAction.isSetCameraContrast();
    }

    public void isSetCameraSaturation() {
        this.iFiveKeyAction.isSetCameraSaturation();
    }
}
