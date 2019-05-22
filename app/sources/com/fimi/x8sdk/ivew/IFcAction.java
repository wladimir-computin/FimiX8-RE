package com.fimi.x8sdk.ivew;

import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.cmd.CmdAiAutoPhoto;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePoints;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction;
import com.fimi.x8sdk.entity.GpsInfoCmd;

public interface IFcAction {
    void getAiFollowModle(UiCallBackListener uiCallBackListener);

    void getAiFollowPoint(UiCallBackListener uiCallBackListener);

    void getAiFollowSpeed(UiCallBackListener uiCallBackListener);

    void getAiLinePoint(int i, UiCallBackListener uiCallBackListener);

    void getAiLinePointValue(int i, UiCallBackListener uiCallBackListener);

    void getAiSurroundCiclePoint(UiCallBackListener uiCallBackListener);

    void getAiSurroundOrientation(UiCallBackListener uiCallBackListener);

    void getAiSurroundSpeed(UiCallBackListener uiCallBackListener);

    void getFwVersion(byte b, byte b2, UiCallBackListener uiCallBackListener);

    void getScrewPrameter(UiCallBackListener uiCallBackListener);

    void land(UiCallBackListener uiCallBackListener);

    void landExit(UiCallBackListener uiCallBackListener);

    void setAiAutoPhotoExcute(UiCallBackListener uiCallBackListener);

    void setAiAutoPhotoExit(UiCallBackListener uiCallBackListener);

    void setAiAutoPhotoValue(CmdAiAutoPhoto cmdAiAutoPhoto, UiCallBackListener uiCallBackListener);

    void setAiFollowModle(int i, UiCallBackListener uiCallBackListener);

    void setAiFollowPoint2Point(double d, double d2, int i, int i2, UiCallBackListener uiCallBackListener);

    void setAiFollowPoint2PointExcute(UiCallBackListener uiCallBackListener);

    void setAiFollowPoint2PointExite(UiCallBackListener uiCallBackListener);

    void setAiFollowSpeed(int i, UiCallBackListener uiCallBackListener);

    void setAiLineExcute(UiCallBackListener uiCallBackListener);

    void setAiLineExite(UiCallBackListener uiCallBackListener);

    void setAiLinePoints(CmdAiLinePoints cmdAiLinePoints, UiCallBackListener uiCallBackListener);

    void setAiLinePointsAction(CmdAiLinePointsAction cmdAiLinePointsAction, UiCallBackListener uiCallBackListener);

    void setAiRetureHome(UiCallBackListener uiCallBackListener);

    void setAiRetureHomeExite(UiCallBackListener uiCallBackListener);

    void setAiSurroundCiclePoint(double d, double d2, float f, double d3, double d4, float f2, int i, UiCallBackListener uiCallBackListener);

    void setAiSurroundExcute(UiCallBackListener uiCallBackListener);

    void setAiSurroundExite(UiCallBackListener uiCallBackListener);

    void setAiSurroundOrientation(int i, UiCallBackListener uiCallBackListener);

    void setAiSurroundSpeed(int i, UiCallBackListener uiCallBackListener);

    void setAiVcClose(UiCallBackListener uiCallBackListener);

    void setAiVcNotityFc(UiCallBackListener uiCallBackListener);

    void setAiVcOpen(UiCallBackListener uiCallBackListener);

    void setFollowExcute(UiCallBackListener uiCallBackListener);

    void setFollowExit(UiCallBackListener uiCallBackListener);

    void setFollowStandby(UiCallBackListener uiCallBackListener);

    void setGpsInfo(GpsInfoCmd gpsInfoCmd);

    void setHomePoint(float f, double d, double d2, int i, float f2, UiCallBackListener uiCallBackListener);

    void setPressureInfo(float f, float f2);

    void setScrewExite(UiCallBackListener uiCallBackListener);

    void setScrewPause(UiCallBackListener uiCallBackListener);

    void setScrewPrameter(AckAiScrewPrameter ackAiScrewPrameter, UiCallBackListener uiCallBackListener);

    void setScrewResume(UiCallBackListener uiCallBackListener);

    void setScrewStart(UiCallBackListener uiCallBackListener);

    void syncTime(UiCallBackListener uiCallBackListener);

    void sysCtrlMode2AiVc(UiCallBackListener uiCallBackListener, int i);

    void takeOff(UiCallBackListener uiCallBackListener);

    void takeOffExit(UiCallBackListener uiCallBackListener);
}
