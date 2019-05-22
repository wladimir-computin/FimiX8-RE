package com.fimi.x8sdk.ivew;

import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.dataparser.AckGetFcParam;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;

public interface IFcCtrlAction {
    void checkCloudCalibrationProgress(UiCallBackListener uiCallBackListener);

    void checkIMUException(int i, UiCallBackListener uiCallBackListener);

    void checkMatchCodeProgress(UiCallBackListener uiCallBackListener);

    void checkRcCilabration(UiCallBackListener uiCallBackListener);

    void closeAccurateLanding(UiCallBackListener uiCallBackListener);

    void cloudCalibration(int i, UiCallBackListener uiCallBackListener);

    void getAccurateLanding(UiCallBackListener uiCallBackListener);

    void getAiFollowEnableBack(UiCallBackListener uiCallBackListener);

    void getAircrftCalibrationState(int i, int i2, UiCallBackListener uiCallBackListener);

    void getApMode(UiCallBackListener uiCallBackListener);

    void getAutoHomePoint(UiCallBackListener uiCallBackListener);

    void getBrakeSens(UiCallBackListener uiCallBackListener);

    void getCalibrationState(int i, int i2, UiCallBackListener uiCallBackListener);

    void getFcParam(UiCallBackListener<Float> uiCallBackListener, int i);

    void getFlyDistanceParam(UiCallBackListener<AckGetFcParam> uiCallBackListener);

    void getFlyHeight(UiCallBackListener uiCallBackListener);

    void getGpsSpeedParam(UiCallBackListener<AckGetFcParam> uiCallBackListener);

    void getIUMInfo(int i, UiCallBackListener uiCallBackListener);

    void getLostAction(UiCallBackListener uiCallBackListener);

    void getLowPowerOperation(UiCallBackListener uiCallBackListener);

    void getOpticFlow(UiCallBackListener uiCallBackListener);

    void getPilotMode(UiCallBackListener uiCallBackListener);

    void getRcCtrlMode(UiCallBackListener uiCallBackListener);

    void getRockerExp(UiCallBackListener uiCallBackListener);

    void getSensitivity(UiCallBackListener uiCallBackListener);

    void getSportMode(UiCallBackListener uiCallBackListener);

    void getYawTrip(UiCallBackListener uiCallBackListener);

    void openAccurateLanding(UiCallBackListener uiCallBackListener);

    void openCheckIMU(UiCallBackListener uiCallBackListener);

    void rcCalibration(int i, UiCallBackListener uiCallBackListener);

    void rcMatchCodeOrNot(int i);

    void reqReturnHeight(UiCallBackListener<AckGetRetHeight> uiCallBackListener);

    void restSystemParams(UiCallBackListener uiCallBackListener);

    void setAiFollowEnableBack(int i, UiCallBackListener uiCallBackListener);

    void setAircrftCalibrationStart(int i, int i2, int i3, UiCallBackListener uiCallBackListener);

    void setApMode(byte b, UiCallBackListener uiCallBackListener);

    void setApModeRestart(UiCallBackListener uiCallBackListener);

    void setAttitudeSensitivity(UiCallBackListener uiCallBackListener, int i, int i2);

    void setAutoHomePoint(int i, UiCallBackListener uiCallBackListener);

    void setBrakeSens(UiCallBackListener uiCallBackListener, int i, int i2);

    void setCalibrationStart(int i, int i2, int i3, UiCallBackListener uiCallBackListener);

    void setDisenableFixwing(UiCallBackListener uiCallBackListener);

    void setDisenableHeadingFree(UiCallBackListener uiCallBackListener);

    void setEnableAerailShot(int i, UiCallBackListener uiCallBackListener);

    void setEnableFixwing(UiCallBackListener uiCallBackListener);

    void setEnableHeadingFree(UiCallBackListener uiCallBackListener);

    void setEnableTripod(int i, UiCallBackListener uiCallBackListener);

    void setFcParam(UiCallBackListener uiCallBackListener, int i, float f);

    void setFlyDistanceParam(UiCallBackListener uiCallBackListener, float f);

    void setFlyHeight(UiCallBackListener uiCallBackListener, float f);

    void setGoBackRockerExp(UiCallBackListener uiCallBackListener, int i, int i2);

    void setGpsSpeedParam(UiCallBackListener uiCallBackListener, float f);

    void setLeftRightRockerExp(UiCallBackListener uiCallBackListener, int i);

    void setLostAction(UiCallBackListener uiCallBackListener, int i);

    void setLowPowerOperation(UiCallBackListener uiCallBackListener, int i, int i2, int i3, int i4);

    void setOpticFlow(UiCallBackListener uiCallBackListener, boolean z);

    void setPanoramaPhotographState(UiCallBackListener uiCallBackListener, byte b);

    void setPanoramaPhotographType(UiCallBackListener uiCallBackListener, int i);

    void setPilotMode(UiCallBackListener uiCallBackListener, byte b);

    void setRcCtrlMode(UiCallBackListener uiCallBackListener, byte b);

    void setReturnHeight(UiCallBackListener uiCallBackListener, float f);

    void setSportMode(UiCallBackListener uiCallBackListener, int i);

    void setUpDownRockerExp(UiCallBackListener uiCallBackListener, int i);

    void setUpdateHeadingFree(UiCallBackListener uiCallBackListener);

    void setYawSensitivity(UiCallBackListener uiCallBackListener, int i);

    void setYawTrip(UiCallBackListener uiCallBackListener, int i);
}
