package com.fimi.x8sdk.controller;

import android.content.Context;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.connect.JsonNoticeManager;
import com.fimi.x8sdk.ivew.ICamAction;
import com.fimi.x8sdk.ivew.ICamJsonAction;
import com.fimi.x8sdk.listener.JsonCallBackListener;
import com.fimi.x8sdk.presenter.CameraJsonPresenter;
import com.fimi.x8sdk.presenter.CameraPresenter;

public class CameraManager {
    ICamAction camAction = new CameraPresenter();
    ICamJsonAction jsonAction = new CameraJsonPresenter();

    public void setContext(Context context) {
        ((CameraPresenter) this.camAction).setContext(context);
        ((CameraJsonPresenter) this.jsonAction).setContext(context);
    }

    public void startSession() {
        this.jsonAction.startSession();
    }

    public void registerJsonCallBackListener(JsonCallBackListener listener) {
        JsonNoticeManager.getNoticeManager().addListener(listener);
    }

    public void removeJsonCallBackListener(JsonCallBackListener listener) {
        JsonNoticeManager.getNoticeManager().removeListener(listener);
    }

    public void swithVideoMode(UiCallBackListener swithVideoModeListener) {
        this.camAction.swithVideoMode(swithVideoModeListener);
    }

    public void swithPhotoMode(UiCallBackListener switchPhotoModeListener) {
        this.camAction.switchPhotoMode(switchPhotoModeListener);
    }

    public void takePhoto(UiCallBackListener callBackListener) {
        this.camAction.takePhoto(callBackListener);
    }

    public void startVideo(UiCallBackListener callBackListener) {
        this.camAction.startRecord(callBackListener);
    }

    public void stopVideo(UiCallBackListener callBackListener) {
        this.camAction.endRecord(callBackListener);
    }

    public void setCameraSys(String param) {
        this.jsonAction.setCameraSys(param);
    }

    public void setVideoResolution(String param) {
        this.jsonAction.setVideoResolution(param);
    }

    public void setPhotoSize(String param) {
        this.jsonAction.setPhotoSize(param);
    }

    public void setPhotoFormat(String param) {
        this.jsonAction.setPhotoFormat(param);
    }

    public void formatTFCard(JsonUiCallBackListener callBackListener) {
        this.jsonAction.formatTFCard(callBackListener);
    }

    public void defaltSystem(JsonUiCallBackListener callBackListener) {
        this.jsonAction.defaltSystem(callBackListener);
    }

    public void getCameraEV() {
        this.jsonAction.getCameraEV();
    }

    public void getCameraShutter() {
        this.jsonAction.getCameraShutter();
    }

    public void getCameraShutterOptions() {
        this.jsonAction.getCameraShutterOptions();
    }

    public void getCameraISO() {
        this.jsonAction.getCameraISO();
    }

    public void getCameraIsoOptions() {
        this.jsonAction.getCameraIsoOptions();
    }

    public void getCameraAwb() {
        this.jsonAction.getCameraAwb();
    }

    public void setCameraIsoParams(String params) {
        this.jsonAction.setCameraIso(params);
    }

    public void setCameraShutterParams(String shutterTime) {
        this.jsonAction.setCameraShutterTime(shutterTime);
    }

    public void getCameraCurParams(JsonUiCallBackListener callBackListener) {
        this.jsonAction.getCurAllParams(callBackListener);
    }

    public void setCameraEV(String param) {
        this.jsonAction.setCameraEV(param);
    }

    public void getCameraKeyOptions(String optionKey) {
        this.jsonAction.getCameraKeyOptions(optionKey);
    }

    public void getCameraKeyOptions(String optionKey, JsonUiCallBackListener callBackListener) {
        this.jsonAction.getCameraKeyOptions(optionKey, callBackListener);
    }

    public void setCameraKeyParams(String param, String key, JsonUiCallBackListener callBackListener) {
        this.jsonAction.setCameraKeyParams(param, key, callBackListener);
    }

    public void syncCameraTime(UiCallBackListener callBackListener) {
        this.camAction.startRecord(callBackListener);
    }

    public void getCurCameraParams(String paramKey, JsonUiCallBackListener callBackListener) {
        this.jsonAction.getCurCameraParams(paramKey, callBackListener);
    }

    public void setCameraFocuse(String param, JsonUiCallBackListener callBackListener) {
        this.jsonAction.setCameraFocuse(param, callBackListener);
    }

    public void getCameraFocuse(JsonUiCallBackListener callBackListener) {
        this.jsonAction.getCameraFocuse(callBackListener);
    }

    public void getCameraFocuseValues(JsonUiCallBackListener callBackListener) {
        this.jsonAction.getCameraFocuseValues(callBackListener);
    }

    public void setCameraDeControl(String param, JsonUiCallBackListener callBackListener) {
        this.jsonAction.setCameraDeControl(param, callBackListener);
    }

    public void setInterestMetering(String param) {
        this.jsonAction.setInterestMetering(param);
    }

    public void deleteOnlineFile(String path, JsonUiCallBackListener callBackListener) {
        this.jsonAction.deleteOnlineFile(path, callBackListener);
    }
}
