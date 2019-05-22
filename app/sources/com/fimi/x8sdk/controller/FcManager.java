package com.fimi.x8sdk.controller;

import android.content.Context;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.cmd.CmdAiAutoPhoto;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePoints;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction;
import com.fimi.x8sdk.entity.GpsInfoCmd;
import com.fimi.x8sdk.ivew.IFcAction;
import com.fimi.x8sdk.presenter.FcPresenter;

public class FcManager implements IFcAction {
    IFcAction fcAction = new FcPresenter();

    public void setContext(Context context) {
        ((FcPresenter) this.fcAction).setContext(context);
    }

    public void takeOff(UiCallBackListener listener) {
        this.fcAction.takeOff(listener);
    }

    public void takeOffExit(UiCallBackListener listener) {
        this.fcAction.takeOffExit(listener);
    }

    public void land(UiCallBackListener listener) {
        this.fcAction.land(listener);
    }

    public void landExit(UiCallBackListener listener) {
        this.fcAction.landExit(listener);
    }

    public void setFollowStandby(UiCallBackListener listener) {
        this.fcAction.setFollowStandby(listener);
    }

    public void setFollowExcute(UiCallBackListener listener) {
        this.fcAction.setFollowExcute(listener);
    }

    public void setFollowExit(UiCallBackListener listener) {
        this.fcAction.setFollowExit(listener);
    }

    public void getFwVersion(byte moduleName, byte type, UiCallBackListener listener) {
        this.fcAction.getFwVersion(moduleName, type, listener);
    }

    public void setAiFollowPoint2Point(double longitude, double latitude, int altitude, int speed, UiCallBackListener listener) {
        this.fcAction.setAiFollowPoint2Point(longitude, latitude, altitude, speed, listener);
    }

    public void setAiFollowPoint2PointExcute(UiCallBackListener listener) {
        this.fcAction.setAiFollowPoint2PointExcute(listener);
    }

    public void getAiFollowPoint(UiCallBackListener listener) {
        this.fcAction.getAiFollowPoint(listener);
    }

    public void setAiFollowPoint2PointExite(UiCallBackListener listener) {
        this.fcAction.setAiFollowPoint2PointExite(listener);
    }

    public void setAiSurroundExcute(UiCallBackListener listener) {
        this.fcAction.setAiSurroundExcute(listener);
    }

    public void setAiSurroundExite(UiCallBackListener listener) {
        this.fcAction.setAiSurroundExite(listener);
    }

    public void setAiSurroundCiclePoint(double longitude, double latitude, float altitude, double longitudeTakeoff, double latitudeTakeoff, float altitudeTakeoff, int type, UiCallBackListener listener) {
        this.fcAction.setAiSurroundCiclePoint(longitude, latitude, altitude, longitudeTakeoff, latitudeTakeoff, altitudeTakeoff, type, listener);
    }

    public void getAiSurroundCiclePoint(UiCallBackListener listener) {
        this.fcAction.getAiSurroundCiclePoint(listener);
    }

    public void setAiSurroundSpeed(int value, UiCallBackListener listener) {
        this.fcAction.setAiSurroundSpeed(value, listener);
    }

    public void setAiSurroundOrientation(int value, UiCallBackListener listener) {
        this.fcAction.setAiSurroundOrientation(value, listener);
    }

    public void getAiSurroundSpeed(UiCallBackListener listener) {
        this.fcAction.getAiSurroundSpeed(listener);
    }

    public void getAiSurroundOrientation(UiCallBackListener listener) {
        this.fcAction.getAiSurroundOrientation(listener);
    }

    public void setAiRetureHome(UiCallBackListener listener) {
        this.fcAction.setAiRetureHome(listener);
    }

    public void setAiRetureHomeExite(UiCallBackListener listener) {
        this.fcAction.setAiRetureHomeExite(listener);
    }

    public void setAiLinePoints(CmdAiLinePoints points, UiCallBackListener listener) {
        this.fcAction.setAiLinePoints(points, listener);
    }

    public void setAiLinePointsAction(CmdAiLinePointsAction action, UiCallBackListener listener) {
        this.fcAction.setAiLinePointsAction(action, listener);
    }

    public void setAiLineExcute(UiCallBackListener listener) {
        this.fcAction.setAiLineExcute(listener);
    }

    public void setAiLineExite(UiCallBackListener listener) {
        this.fcAction.setAiLineExite(listener);
    }

    public void getAiLinePoint(int number, UiCallBackListener listener) {
        this.fcAction.getAiLinePoint(number, listener);
    }

    public void getAiLinePointValue(int number, UiCallBackListener listener) {
        this.fcAction.getAiLinePointValue(number, listener);
    }

    public void setAiAutoPhotoValue(CmdAiAutoPhoto cmd, UiCallBackListener listener) {
        this.fcAction.setAiAutoPhotoValue(cmd, listener);
    }

    public void setAiAutoPhotoExcute(UiCallBackListener listener) {
        this.fcAction.setAiAutoPhotoExcute(listener);
    }

    public void setAiAutoPhotoExit(UiCallBackListener listener) {
        this.fcAction.setAiAutoPhotoExit(listener);
    }

    public void syncTime(UiCallBackListener callBackListener) {
        this.fcAction.syncTime(callBackListener);
    }

    public void setAiVcOpen(UiCallBackListener listener) {
        this.fcAction.setAiVcOpen(listener);
    }

    public void setAiVcClose(UiCallBackListener listener) {
        this.fcAction.setAiVcClose(listener);
    }

    public void setAiVcNotityFc(UiCallBackListener listener) {
        this.fcAction.setAiVcNotityFc(listener);
    }

    public void setAiFollowModle(int type, UiCallBackListener listener) {
        this.fcAction.setAiFollowModle(type, listener);
    }

    public void getAiFollowModle(UiCallBackListener listener) {
        this.fcAction.getAiFollowModle(listener);
    }

    public void setAiFollowSpeed(int value, UiCallBackListener listener) {
        this.fcAction.setAiFollowSpeed(value, listener);
    }

    public void getAiFollowSpeed(UiCallBackListener listener) {
        this.fcAction.getAiFollowSpeed(listener);
    }

    public void setHomePoint(float h, double lat, double lng, int mode, float accuracy, UiCallBackListener listener) {
        this.fcAction.setHomePoint(h, lat, lng, mode, accuracy, listener);
    }

    public void setScrewPrameter(AckAiScrewPrameter prameter, UiCallBackListener listener) {
        this.fcAction.setScrewPrameter(prameter, listener);
    }

    public void getScrewPrameter(UiCallBackListener listener) {
        this.fcAction.getScrewPrameter(listener);
    }

    public void setScrewStart(UiCallBackListener listener) {
        this.fcAction.setScrewStart(listener);
    }

    public void setScrewPause(UiCallBackListener listener) {
        this.fcAction.setScrewPause(listener);
    }

    public void setScrewResume(UiCallBackListener listener) {
        this.fcAction.setScrewResume(listener);
    }

    public void setScrewExite(UiCallBackListener listener) {
        this.fcAction.setScrewExite(listener);
    }

    public void sysCtrlMode2AiVc(UiCallBackListener listener, int ctrlMode) {
        this.fcAction.sysCtrlMode2AiVc(listener, ctrlMode);
    }

    public void setPressureInfo(float alt, float hPa) {
        this.fcAction.setPressureInfo(alt, hPa);
    }

    public void setGpsInfo(GpsInfoCmd gps) {
        this.fcAction.setGpsInfo(gps);
    }
}
