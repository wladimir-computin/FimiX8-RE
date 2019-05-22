package com.fimi.x8sdk.controller;

import android.content.Context;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.dataparser.AckGetFcParam;
import com.fimi.x8sdk.dataparser.AckGetLostAction;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;
import com.fimi.x8sdk.ivew.IFcCtrlAction;
import com.fimi.x8sdk.presenter.FcCtrlPresenter;

public class FcCtrlManager {
    IFcCtrlAction fcCtrlAction = new FcCtrlPresenter();

    public void setContext(Context context) {
        ((FcCtrlPresenter) this.fcCtrlAction).setContext(context);
    }

    public void setReturnHome(UiCallBackListener listener, float height) {
        this.fcCtrlAction.setReturnHeight(listener, height);
    }

    public void getReturnHomeHeight(UiCallBackListener<AckGetRetHeight> paramLisnter) {
        this.fcCtrlAction.reqReturnHeight(paramLisnter);
    }

    public void setLostAction(UiCallBackListener listener, int lostAction) {
        this.fcCtrlAction.setLostAction(listener, lostAction);
    }

    public void getLostAction(UiCallBackListener<AckGetLostAction> listenerWithParam) {
        this.fcCtrlAction.getLostAction(listenerWithParam);
    }

    public void setPilotMode(UiCallBackListener listener, byte mode) {
        this.fcCtrlAction.setPilotMode(listener, mode);
    }

    public void getPilotMode(UiCallBackListener listener) {
        this.fcCtrlAction.getPilotMode(listener);
    }

    public void setGpsSpeed(UiCallBackListener listener, float speed) {
        this.fcCtrlAction.setGpsSpeedParam(listener, speed);
    }

    public void getGpsSpeedParam(UiCallBackListener<AckGetFcParam> listenerWithParam) {
        this.fcCtrlAction.getGpsSpeedParam(listenerWithParam);
    }

    public void setFlyDistanceParam(UiCallBackListener callBackListener, float paramData) {
        this.fcCtrlAction.setFlyDistanceParam(callBackListener, paramData);
    }

    public void getFlyDistanceParam(UiCallBackListener<AckGetFcParam> listenerWithParam) {
        this.fcCtrlAction.getFlyDistanceParam(listenerWithParam);
    }

    public void setCalibrationStart(int type, int cmd, int mode, UiCallBackListener listener) {
        this.fcCtrlAction.setCalibrationStart(type, cmd, mode, listener);
    }

    public void getCalibrationState(int sensorType, int type, UiCallBackListener listener) {
        this.fcCtrlAction.getCalibrationState(sensorType, type, listener);
    }

    public void setAircrftCalibrationStart(int type, int cmd, int mode, UiCallBackListener listener) {
        this.fcCtrlAction.setAircrftCalibrationStart(type, cmd, mode, listener);
    }

    public void getAircrftCalibrationState(int sensorType, int type, UiCallBackListener listener) {
        this.fcCtrlAction.getAircrftCalibrationState(sensorType, type, listener);
    }

    public void getIUMInfo(int imuType, UiCallBackListener callBackListener) {
        this.fcCtrlAction.getIUMInfo(imuType, callBackListener);
    }

    public void setApMode(byte mode, UiCallBackListener listener) {
        this.fcCtrlAction.setApMode(mode, listener);
    }

    public void setApModeRestart(UiCallBackListener listener) {
        this.fcCtrlAction.setApModeRestart(listener);
    }

    public void getApMode(UiCallBackListener listener) {
        this.fcCtrlAction.getApMode(listener);
    }

    public void cloudCalibration(int status, UiCallBackListener callBackListener) {
        this.fcCtrlAction.cloudCalibration(status, callBackListener);
    }

    public void checkCloudCalibrationProgress(UiCallBackListener callBackListener) {
        this.fcCtrlAction.checkCloudCalibrationProgress(callBackListener);
    }

    public void rcMatchCodeOrNot(int codeType) {
        this.fcCtrlAction.rcMatchCodeOrNot(codeType);
    }

    public void checkRcMactchProgress(UiCallBackListener callBackListener) {
        this.fcCtrlAction.checkMatchCodeProgress(callBackListener);
    }

    public void rcCalibration(int cmdType, UiCallBackListener callBackListener) {
        this.fcCtrlAction.rcCalibration(cmdType, callBackListener);
    }

    public void checkRcCalibrationProgress(UiCallBackListener uiCallBackListener) {
        this.fcCtrlAction.checkRcCilabration(uiCallBackListener);
    }

    public void setLowPowerOpt(UiCallBackListener listener, int lowPowerValue, int seriousLowPowerValue, int lowPowerOpt, int seriousLowPowerOpt) {
        this.fcCtrlAction.setLowPowerOperation(listener, lowPowerValue, seriousLowPowerValue, lowPowerOpt, seriousLowPowerOpt);
    }

    public void getLowPowerOpt(UiCallBackListener listener) {
        this.fcCtrlAction.getLowPowerOperation(listener);
    }

    public void setOpticFlow(UiCallBackListener listener, boolean isOpen) {
        this.fcCtrlAction.setOpticFlow(listener, isOpen);
    }

    public void getOpticFlow(UiCallBackListener listener) {
        this.fcCtrlAction.getOpticFlow(listener);
    }

    public void setAttitudeSensitivity(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        this.fcCtrlAction.setAttitudeSensitivity(listener, rollPercent, pitchPercent);
    }

    public void setYawSensitivity(UiCallBackListener listener, int yawPercent) {
        this.fcCtrlAction.setYawSensitivity(listener, yawPercent);
    }

    public void getSensitivity(UiCallBackListener listener) {
        this.fcCtrlAction.getSensitivity(listener);
    }

    public void setBrakeSens(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        this.fcCtrlAction.setBrakeSens(listener, rollPercent, pitchPercent);
    }

    public void getBrakeSens(UiCallBackListener listener) {
        this.fcCtrlAction.getBrakeSens(listener);
    }

    public void setYawTrip(UiCallBackListener listener, int yawValue) {
        this.fcCtrlAction.setYawTrip(listener, yawValue);
    }

    public void getYawTrip(UiCallBackListener listener) {
        this.fcCtrlAction.getYawTrip(listener);
    }

    public void setUpDownRockerExp(UiCallBackListener listener, int throttlePercent) {
        this.fcCtrlAction.setUpDownRockerExp(listener, throttlePercent);
    }

    public void setLeftRightRockerExp(UiCallBackListener listener, int yawPercent) {
        this.fcCtrlAction.setLeftRightRockerExp(listener, yawPercent);
    }

    public void setGoBackRockerExp(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        this.fcCtrlAction.setGoBackRockerExp(listener, rollPercent, pitchPercent);
    }

    public void getRockerExp(UiCallBackListener listener) {
        this.fcCtrlAction.getRockerExp(listener);
    }

    public void setRcCtrlMode(UiCallBackListener listener, byte mode) {
        this.fcCtrlAction.setRcCtrlMode(listener, mode);
    }

    public void getRcCtrlMode(UiCallBackListener listener) {
        this.fcCtrlAction.getRcCtrlMode(listener);
    }

    public void checkIMUException(int sensorType, UiCallBackListener uiCallBackListener) {
        this.fcCtrlAction.checkIMUException(sensorType, uiCallBackListener);
    }

    public void openCheckIMU(UiCallBackListener listener) {
        this.fcCtrlAction.openCheckIMU(listener);
    }

    public void restSystemParams(UiCallBackListener uiCallBackListener) {
        this.fcCtrlAction.restSystemParams(uiCallBackListener);
    }

    public void setSportMode(UiCallBackListener uiCallBackListener, int enable) {
        this.fcCtrlAction.setSportMode(uiCallBackListener, enable);
    }

    public void getSportMode(UiCallBackListener uiCallBackListener) {
        this.fcCtrlAction.getSportMode(uiCallBackListener);
    }

    public void setAutoHomePoint(int enable, UiCallBackListener uiCallBackListener) {
        this.fcCtrlAction.setAutoHomePoint(enable, uiCallBackListener);
    }

    public void getAutoHomePoint(UiCallBackListener uiCallBackListener) {
        this.fcCtrlAction.getAutoHomePoint(uiCallBackListener);
    }

    public void setEnableTripod(int enable, UiCallBackListener uiCallBackListener) {
        this.fcCtrlAction.setEnableTripod(enable, uiCallBackListener);
    }

    public void setEnableAerailShot(int enable, UiCallBackListener uiCallBackListener) {
        this.fcCtrlAction.setEnableAerailShot(enable, uiCallBackListener);
    }

    public void setEnableFixwing(UiCallBackListener listener) {
        this.fcCtrlAction.setEnableFixwing(listener);
    }

    public void setDisenableFixwing(UiCallBackListener listener) {
        this.fcCtrlAction.setDisenableFixwing(listener);
    }

    public void setEnableHeadingFree(UiCallBackListener listener) {
        this.fcCtrlAction.setEnableHeadingFree(listener);
    }

    public void setDisenableHeadingFree(UiCallBackListener listener) {
        this.fcCtrlAction.setDisenableHeadingFree(listener);
    }

    public void setAiFollowEnableBack(int value, UiCallBackListener listener) {
        this.fcCtrlAction.setAiFollowEnableBack(value, listener);
    }

    public void getAiFollowEnableBack(UiCallBackListener listener) {
        this.fcCtrlAction.getAiFollowEnableBack(listener);
    }

    public void setFlyHeight(UiCallBackListener listener, float value) {
        this.fcCtrlAction.setFlyHeight(listener, value);
    }

    public void getFlyHeight(UiCallBackListener listener) {
        this.fcCtrlAction.getFlyHeight(listener);
    }

    public void openAccurateLanding(UiCallBackListener listener) {
        this.fcCtrlAction.openAccurateLanding(listener);
    }

    public void closeAccurateLanding(UiCallBackListener listener) {
        this.fcCtrlAction.closeAccurateLanding(listener);
    }

    public void getAccurateLanding(UiCallBackListener listener) {
        this.fcCtrlAction.getAccurateLanding(listener);
    }

    public void setUpdateHeadingFree(UiCallBackListener listener) {
        this.fcCtrlAction.setUpdateHeadingFree(listener);
    }

    public void setPanoramaPhotographType(UiCallBackListener listener, int panoramaPhotographType) {
        this.fcCtrlAction.setPanoramaPhotographType(listener, panoramaPhotographType);
    }

    public void setPanoramaPhotographState(UiCallBackListener listener, byte panoramaPhotographState) {
        this.fcCtrlAction.setPanoramaPhotographState(listener, panoramaPhotographState);
    }
}
