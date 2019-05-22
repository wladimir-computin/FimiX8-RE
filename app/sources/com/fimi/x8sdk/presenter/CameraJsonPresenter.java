package com.fimi.x8sdk.presenter;

import com.alibaba.fastjson.JSONObject;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.session.JsonListener;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.connect.JsonNoticeManager;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.ivew.ICamJsonAction;

public class CameraJsonPresenter extends BasePresenter implements ICamJsonAction, JsonListener {
    public CameraJsonPresenter() {
        addNoticeListener((JsonListener) this);
    }

    public void startSession() {
        sendCmd(new CameraJsonCollection().startSession());
    }

    public void setCameraSys(String param) {
        sendCmd(new CameraJsonCollection().setCamera(param));
    }

    public void setVideoResolution(String param) {
        sendCmd(new CameraJsonCollection().setVideoResolution(param));
    }

    public void setPhotoSize(String param) {
        sendCmd(new CameraJsonCollection().setPhotoSize(param));
    }

    public void setPhotoFormat(String param) {
        sendCmd(new CameraJsonCollection().setPhotoFormat(param));
    }

    public void formatTFCard(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().formatTFCard(callBackListener));
    }

    public void defaltSystem(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().defaultSystem(callBackListener));
    }

    public void getCameraEV() {
        sendCmd(new CameraJsonCollection().getCameraEV());
    }

    public void getCameraShutter() {
        sendCmd(new CameraJsonCollection().getCameraShutter());
    }

    public void setCameraDeControl(String param, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().setCameraDeControl(param, callBackListener));
    }

    public void getCameraISO() {
        sendCmd(new CameraJsonCollection().getCameraISO());
    }

    public void getCameraIsoOptions() {
        sendCmd(new CameraJsonCollection().getCameraIsoOptions());
    }

    public void getCameraAwb() {
        sendCmd(new CameraJsonCollection().getCameraAWB());
    }

    public void onSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        super.onSendTimeOut(groupId, msgId, bcd);
        JsonNoticeManager.getNoticeManager().sendOutTime();
    }

    public void onProcess(int msgId, JSONObject respJson) {
        JsonNoticeManager.getNoticeManager().onProcess(msgId, respJson);
    }

    public void removeLisnters() {
        removeNoticeListener();
        NoticeManager.getInstance().removeJsonListener(this);
    }

    public void setCameraIso(String param) {
        if (!X8CameraParamsValue.getInstance().getCurParamsJson().getIso().trim().equalsIgnoreCase(param.trim())) {
            sendCmd(new CameraJsonCollection().setCameraIso(param));
        }
    }

    public void setCameraShutterTime(String shutterTime) {
        if (!X8CameraParamsValue.getInstance().getCurParamsJson().getShutter_time().trim().equalsIgnoreCase(shutterTime.trim())) {
            sendCmd(new CameraJsonCollection().setCameraShutterTime(shutterTime));
        }
    }

    public void getCameraShutterOptions() {
        sendCmd(new CameraJsonCollection().getCameraShutterOptions());
    }

    public void getCurAllParams(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCameraCurParams(callBackListener));
    }

    public void setCameraEV(String param) {
        if (!X8CameraParamsValue.getInstance().getCurParamsJson().getAe_bias().trim().equalsIgnoreCase(param.trim())) {
            sendCmd(new CameraJsonCollection().setCameraEV(param));
        }
    }

    public void getCameraKeyOptions(String param) {
        sendCmd(new CameraJsonCollection().getCameraKeyOptions(param));
    }

    public void getCameraKeyOptions(String param, JsonUiCallBackListener listener) {
        sendCmd(new CameraJsonCollection().getCameraKeyOptions(param, listener));
    }

    public void setCameraKeyParams(String paramValue, String paramKey, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().setCameraKeyParam(paramValue, paramKey, callBackListener));
    }

    public void getCurCameraParams(String paramKey, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCurCameraParams(paramKey, callBackListener));
    }

    public void setCameraFocuse(String param, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().setCameraFocuse(param, callBackListener));
    }

    public void getCameraFocuse(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCameraFocuse(callBackListener));
    }

    public void getCameraFocuseValues(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCameraFocuseValues(callBackListener));
    }

    public void deleteOnlineFile(String path, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().deleteFile(path, callBackListener));
    }

    public void setInterestMetering(String param) {
        sendCmd(new CameraJsonCollection().setInterestMetering(param));
    }
}
