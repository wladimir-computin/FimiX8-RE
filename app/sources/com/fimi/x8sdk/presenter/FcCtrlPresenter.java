package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckGetFcParam;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;
import com.fimi.x8sdk.ivew.IFcCtrlAction;

public class FcCtrlPresenter extends BasePresenter implements IFcCtrlAction {
    public FcCtrlPresenter() {
        addNoticeListener();
    }

    public void setReturnHeight(UiCallBackListener retHomeListener, float height) {
        sendCmd(new FcCollection(this, retHomeListener).setReturnHeight(height));
    }

    public void reqReturnHeight(UiCallBackListener<AckGetRetHeight> reqRetHome) {
        sendCmd(new FcCollection(this, reqRetHome).getReturnHeight());
    }

    public void setLostAction(UiCallBackListener listener, int action) {
        sendCmd(new FcCollection(this, listener).setLostAction((byte) action));
    }

    public void getLostAction(UiCallBackListener callBackListenerWithParam) {
        sendCmd(new FcCollection(this, callBackListenerWithParam).getLostAction());
    }

    public void setPilotMode(UiCallBackListener listener, byte mode) {
        sendCmd(new FcCollection(this, listener).setPilotMode(mode));
    }

    public void getPilotMode(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getPilotMode());
    }

    public void setFcParam(UiCallBackListener callBackListener, int paramIndex, float paramData) {
        sendCmd(new FcCollection(this, callBackListener).setFcParam((byte) paramIndex, paramData));
    }

    public void getFcParam(UiCallBackListener<Float> listenerWithParam, int paramIndex) {
        sendCmd(new FcCollection(this, listenerWithParam).getFcParam((byte) paramIndex));
    }

    public void setGpsSpeedParam(UiCallBackListener callBackListener, float paramData) {
        sendCmd(new FcCollection(this, callBackListener).setFcParam((byte) 3, paramData));
    }

    public void getGpsSpeedParam(UiCallBackListener<AckGetFcParam> listenerWithParam) {
        sendCmd(new FcCollection(this, listenerWithParam).getFcParam((byte) 3));
    }

    public void setFlyDistanceParam(UiCallBackListener callBackListener, float paramData) {
        sendCmd(new FcCollection(this, callBackListener).setFcParam((byte) 7, paramData));
    }

    public void getFlyDistanceParam(UiCallBackListener<AckGetFcParam> listenerWithParam) {
        sendCmd(new FcCollection(this, listenerWithParam).getFcParam((byte) 7));
    }

    public void setCalibrationStart(int type, int cmd, int mode, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setCalibrationStart(type, cmd, mode));
    }

    public void getCalibrationState(int sensorType, int type, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getCalibrationState(sensorType, type));
    }

    public void getAircrftCalibrationState(int sensorType, int type, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAircrftCalibrationState(sensorType, type));
    }

    public void setAircrftCalibrationStart(int type, int cmd, int mode, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAircrftCalibrationStart(type, cmd, mode));
    }

    public void setApMode(byte mode, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setApMode(mode));
    }

    public void setApModeRestart(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setApModeRestart());
    }

    public void getApMode(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getApMode());
    }

    public void onSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
    }

    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
    }

    public void onPersonalDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    public void onPersonalSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        reponseCmd(false, groupId, msgId, null, bcd);
    }

    /* Access modifiers changed, original: protected */
    public void reponseCmd(boolean isAck, int groupId, int msgId, ILinkMessage packet, BaseCommand bcd) {
        if (groupId == 4) {
            onNormalResponseWithParamForFCCTRL(isAck, packet, bcd);
        } else if (groupId == 13) {
            onNormalResponseWithParamForMaintenance(isAck, packet, bcd);
        } else if (groupId == 12) {
            onNormalResponseWithParamForTelemetry(isAck, packet, bcd);
        } else if (groupId == 9) {
            onNormalResponseWithParamForGimbal(isAck, packet, bcd);
        } else if (groupId == 11) {
            onNormalResponseWithParamWithRcCTRL(isAck, packet, bcd);
        } else if (groupId == 3) {
            onNormalResponse(isAck, packet, bcd);
        } else if (groupId == 14) {
            onNormalResponse(isAck, packet, bcd);
        }
    }

    public void getIUMInfo(int imuType, UiCallBackListener callBackListener) {
        sendCmd(new FcCollection(this, callBackListener).getIMUInfoCmd(imuType));
    }

    public void cloudCalibration(int status, UiCallBackListener callBackListener) {
        sendCmd(new FcCollection(this, callBackListener).setCloudCalibrationCmd(status));
    }

    public void checkCloudCalibrationProgress(UiCallBackListener callBackListener) {
        sendCmd(new FcCollection(this, callBackListener).checkCloudCalibrationCmd());
    }

    public void setLowPowerOperation(UiCallBackListener listener, int lowPowerValue, int seriousLowPowerValue, int lowPowerOpt, int seriousLowPowerOpt) {
        sendCmd(new FcCollection(this, listener).setLowPowerOperation(lowPowerValue, seriousLowPowerValue, lowPowerOpt, seriousLowPowerOpt));
    }

    public void getLowPowerOperation(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getLowPowerOperation());
    }

    public void rcMatchCodeOrNot(int codeType) {
        sendCmd(new FcCollection(this, null).RCMatchOrCancelCode(codeType));
    }

    public void setOpticFlow(UiCallBackListener listener, boolean isOpen) {
        sendCmd(new FcCollection(this, listener).setOpticFlow(isOpen));
    }

    public void getOpticFlow(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getOpticFlow());
    }

    public void setAttitudeSensitivity(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        sendCmd(new FcCollection(this, listener).setAttitudeSensitivity(rollPercent, pitchPercent));
    }

    public void setYawSensitivity(UiCallBackListener listener, int yawPercent) {
        sendCmd(new FcCollection(this, listener).setYawSensitivity(yawPercent));
    }

    public void getSensitivity(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getSensitivity());
    }

    public void setBrakeSens(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        sendCmd(new FcCollection(this, listener).setBrakeSens(rollPercent, pitchPercent));
    }

    public void getBrakeSens(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getBrakeSens());
    }

    public void setYawTrip(UiCallBackListener listener, int yawValue) {
        sendCmd(new FcCollection(this, listener).setYawTrip(yawValue));
    }

    public void getYawTrip(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getYawTrip());
    }

    public void setUpDownRockerExp(UiCallBackListener listener, int throttlePercent) {
        sendCmd(new FcCollection(this, listener).setUpDownRockerExp(throttlePercent));
    }

    public void setLeftRightRockerExp(UiCallBackListener listener, int yawPercent) {
        sendCmd(new FcCollection(this, listener).setLeftRightRockerExp(yawPercent));
    }

    public void setGoBackRockerExp(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        sendCmd(new FcCollection(this, listener).setGoBackRockerExp(rollPercent, pitchPercent));
    }

    public void getRockerExp(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getRockerExp());
    }

    public void setRcCtrlMode(UiCallBackListener listener, byte mode) {
        sendCmd(new FcCollection(this, listener).setCtrlMode(mode));
    }

    public void getRcCtrlMode(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getCtrlMode());
    }

    public void checkMatchCodeProgress(UiCallBackListener callBackListener) {
        sendCmd(new FcCollection(this, callBackListener).checkMatchCodeProgress());
    }

    public void rcCalibration(int cmdTyp, UiCallBackListener callBackListener) {
        sendCmd(new FcCollection(this, callBackListener).rcCalibration(cmdTyp));
    }

    public void checkRcCilabration(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).checkRCCalibration());
    }

    public void checkIMUException(int sensorType, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).checkIMUException(sensorType));
    }

    public void restSystemParams(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).restSystemParams());
    }

    public void setSportMode(UiCallBackListener uiCallBackListener, int enable) {
        sendCmd(new FcCollection(this, uiCallBackListener).setSportMode(enable));
    }

    public void getSportMode(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).getSportMode());
    }

    public void setAutoHomePoint(int enable, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).setAutoHomePoint(enable));
    }

    public void getAutoHomePoint(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).getAutoHomePoint());
    }

    public void setEnableTripod(int enable, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).setEnableTripod(enable));
    }

    public void setEnableAerailShot(int enable, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).setEnableAerailShot(enable));
    }

    public void setEnableFixwing(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setEnableFixwing());
    }

    public void setDisenableFixwing(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setDisenableFixwing());
    }

    public void setEnableHeadingFree(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setEnableHeadingFree());
    }

    public void setDisenableHeadingFree(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setDisenableHeadingFree());
    }

    public void setAiFollowEnableBack(int value, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowEnableBack(value));
    }

    public void getAiFollowEnableBack(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAiFollowEnableBack());
    }

    public void setFlyHeight(UiCallBackListener listener, float paramData) {
        sendCmd(new FcCollection(this, listener).setFcParam((byte) 5, paramData));
    }

    public void getFlyHeight(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getFcParam((byte) 5));
    }

    public void openCheckIMU(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).checkIMUInfoCmd());
    }

    public void openAccurateLanding(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAccurateLanding(1));
    }

    public void closeAccurateLanding(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAccurateLanding(0));
    }

    public void getAccurateLanding(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAccurateLanding());
    }

    public void setUpdateHeadingFree(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setUpdateHeadingFree());
    }

    public void setPanoramaPhotographType(UiCallBackListener listener, int panoramaPhotographType) {
        sendCmd(new FcCollection(this, listener).setPanoramaPhotographType(panoramaPhotographType));
    }

    public void setPanoramaPhotographState(UiCallBackListener listener, byte panoramaPhotographState) {
        sendCmd(new FcCollection(this, listener).setPanoramaPhotographState(panoramaPhotographState));
    }
}
