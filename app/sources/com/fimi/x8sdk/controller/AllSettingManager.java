package com.fimi.x8sdk.controller;

import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.dataparser.AckAccurateLanding;
import com.fimi.x8sdk.dataparser.AckAiFollowGetEnableBack;
import com.fimi.x8sdk.dataparser.AckGetFcParam;
import com.fimi.x8sdk.dataparser.AckGetLostAction;
import com.fimi.x8sdk.dataparser.AckGetLowPowerOpt;
import com.fimi.x8sdk.dataparser.AckGetPilotMode;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;
import com.fimi.x8sdk.dataparser.AckGetSensitivity;
import com.fimi.x8sdk.dataparser.AckGetSportMode;
import com.fimi.x8sdk.dataparser.cmd.AckGetAutoHome;
import com.fimi.x8sdk.modulestate.StateManager;

public class AllSettingManager {
    private static final AllSettingManager ourInstance = new AllSettingManager();
    private CameraManager cameraManager;
    private FcManager fcManager;
    private FcCtrlManager mFcCtrlManager;

    public static AllSettingManager getInstance() {
        return ourInstance;
    }

    private AllSettingManager() {
    }

    public void setFcManager(FcManager fcManager, FcCtrlManager mFcCtrlManager, CameraManager cameraManager) {
        this.fcManager = fcManager;
        this.mFcCtrlManager = mFcCtrlManager;
        this.cameraManager = cameraManager;
    }

    public void getAllSetting() {
        if (this.fcManager != null && this.mFcCtrlManager != null) {
            this.mFcCtrlManager.getReturnHomeHeight(new UiCallBackListener<AckGetRetHeight>() {
                public void onComplete(CmdResult cmdResult, AckGetRetHeight aFloat) {
                }
            });
            this.mFcCtrlManager.getPilotMode(new UiCallBackListener<AckGetPilotMode>() {
                public void onComplete(CmdResult cmdResult, AckGetPilotMode obj) {
                }
            });
            this.mFcCtrlManager.getBrakeSens(new UiCallBackListener<AckGetSensitivity>() {
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                }
            });
            this.mFcCtrlManager.getSensitivity(new UiCallBackListener<AckGetSensitivity>() {
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                }
            });
            this.mFcCtrlManager.getAiFollowEnableBack(new UiCallBackListener<AckAiFollowGetEnableBack>() {
                public void onComplete(CmdResult cmdResult, AckAiFollowGetEnableBack obj) {
                }
            });
            this.mFcCtrlManager.getLowPowerOpt(new UiCallBackListener<AckGetLowPowerOpt>() {
                public void onComplete(CmdResult cmdResult, AckGetLowPowerOpt lowPowerOpt) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getX8Drone().setLowPowerValue(lowPowerOpt.getLowPowerValue());
                    }
                }
            });
            this.mFcCtrlManager.getFlyHeight(new UiCallBackListener<AckGetFcParam>() {
                public void onComplete(CmdResult cmdResult, AckGetFcParam obj) {
                    if (cmdResult.isSuccess() && obj.getParamIndex() == 5) {
                        StateManager.getInstance().getX8Drone().setFlyHeight(obj.getParamData());
                    }
                }
            });
            this.mFcCtrlManager.getGpsSpeedParam(new UiCallBackListener<AckGetFcParam>() {
                public void onComplete(CmdResult cmdResult, AckGetFcParam obj) {
                    if (cmdResult.isSuccess() && obj.getParamIndex() == 3) {
                        StateManager.getInstance().getX8Drone().setGpsSpeed(obj.getParamData());
                    }
                }
            });
            this.mFcCtrlManager.getFlyDistanceParam(new UiCallBackListener<AckGetFcParam>() {
                public void onComplete(CmdResult cmdResult, AckGetFcParam obj) {
                    if (cmdResult.isSuccess() && obj.getParamIndex() == 7) {
                        StateManager.getInstance().getX8Drone().setFlyDistance(obj.getParamData());
                    }
                }
            });
            this.mFcCtrlManager.getSportMode(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    AckGetSportMode ackGetSportMode = (AckGetSportMode) o;
                    if (!cmdResult.isSuccess()) {
                        return;
                    }
                    if (ackGetSportMode.getVehicleControlType() == 1) {
                        StateManager.getInstance().getX8Drone().setSportMode(true);
                    } else {
                        StateManager.getInstance().getX8Drone().setSportMode(false);
                    }
                }
            });
            this.mFcCtrlManager.getAccurateLanding(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                }
            });
            this.mFcCtrlManager.getLostAction(new UiCallBackListener<AckGetLostAction>() {
                public void onComplete(CmdResult cmdResult, AckGetLostAction obj) {
                    if (cmdResult.isSuccess()) {
                    }
                }
            });
            this.mFcCtrlManager.getAccurateLanding(new UiCallBackListener<AckAccurateLanding>() {
                public void onComplete(CmdResult cmdResult, AckAccurateLanding ackAccurateLanding) {
                    if (cmdResult.isSuccess()) {
                    }
                }
            });
            this.mFcCtrlManager.getAutoHomePoint(new UiCallBackListener<AckGetAutoHome>() {
                public void onComplete(CmdResult cmdResult, AckGetAutoHome obj) {
                    if (cmdResult.isSuccess()) {
                    }
                }
            });
            this.mFcCtrlManager.getYawTrip(new UiCallBackListener<AckGetSensitivity>() {
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                }
            });
            this.mFcCtrlManager.getRockerExp(new UiCallBackListener<AckGetSensitivity>() {
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                }
            });
            this.mFcCtrlManager.getAiFollowEnableBack(new UiCallBackListener<AckAiFollowGetEnableBack>() {
                public void onComplete(CmdResult cmdResult, AckAiFollowGetEnableBack obj) {
                    if (cmdResult.isSuccess()) {
                    }
                }
            });
        }
    }
}
