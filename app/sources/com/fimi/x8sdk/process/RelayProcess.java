package com.fimi.x8sdk.process;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.host.HostConstants;
import com.fimi.host.LocalFwEntity;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.session.JsonListener;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.BitUtil;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.command.X8BaseCmd.X8S_Module;
import com.fimi.x8sdk.connect.DeviceMonitorThread;
import com.fimi.x8sdk.controller.AllSettingManager;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AckCamJsonInfo;
import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.dataparser.AutoRelayHeart;
import com.fimi.x8sdk.listener.RelayHeartListener;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.modulestate.VersionState;
import com.fimi.x8sdk.rtp.X8Rtp;

public class RelayProcess implements JsonListener {
    private static RelayProcess relayProcess = new RelayProcess();
    private int MAXFW = 11;
    private CameraManager cameraManager;
    private int countFw = 0;
    private long curTime;
    private FcManager fcManager;
    private boolean getSetting;
    private boolean isShowUpdateView = true;
    private FcCtrlManager mFcCtrlManager;
    DeviceMonitorThread monitorThread;
    AutoRelayHeart relayHeart;

    public boolean isShowUpdateView() {
        return this.isShowUpdateView;
    }

    public void setShowUpdateView(boolean showUpdateView) {
        this.isShowUpdateView = showUpdateView;
    }

    public static RelayProcess getRelayProcess() {
        return relayProcess;
    }

    public void registerListener(RelayHeartListener listener) {
        this.monitorThread = new DeviceMonitorThread();
        this.monitorThread.start();
        NoticeManager.getInstance().addJsonListener(this);
    }

    public void removeListener(RelayHeartListener listener) {
        if (this.monitorThread != null) {
            this.monitorThread.exit();
        }
        NoticeManager.getInstance().removeJsonListener(this);
    }

    public void setRelayHeart(AutoRelayHeart relayHeart) {
        boolean isQuestToken;
        this.relayHeart = relayHeart;
        int isConnect = BitUtil.getBitByByte(relayHeart.getStatus(), 2);
        int token = StateManager.getInstance().getCamera().getToken();
        if (isConnect <= 0 || token > 0) {
            isQuestToken = false;
        } else {
            isQuestToken = true;
        }
        if (isQuestToken && SystemClock.uptimeMillis() - this.curTime > 2000) {
            this.curTime = SystemClock.uptimeMillis();
            SessionManager.getInstance().sendCmd(new CameraJsonCollection().startSession());
        }
        if (isConnect <= 0) {
            StateManager.getInstance().getCamera().setToken(-1);
        }
        if (StateManager.getInstance().getX8Drone().getVersion() == null) {
            getAllVersion();
            this.getSetting = false;
        }
        getAllSetting();
    }

    public AutoRelayHeart getRelayHeart() {
        return this.relayHeart;
    }

    public void onProcess(int msgId, JSONObject json) {
        if (json != null) {
            AckCamJsonInfo jsonInfo = (AckCamJsonInfo) JSON.parseObject(json.toJSONString(), AckCamJsonInfo.class);
            int retVal = jsonInfo.getRval();
            String type = jsonInfo.getType();
            String param = jsonInfo.getParam();
            if (retVal != 0) {
                return;
            }
            if (msgId == 257) {
                int token = Integer.valueOf(param).intValue();
                StateManager.getInstance().getCamera().setToken(token);
                StateManager.getInstance().setCameraToken(token);
                return;
            }
            if (msgId == 2) {
            }
        }
    }

    public void sendCmd(BaseCommand cmd) {
        if (cmd != null) {
            SessionManager.getInstance().sendCmd(cmd);
        }
    }

    public void getAllVersion() {
        if (this.fcManager != null) {
            this.countFw = 0;
            if (!X8Rtp.simulationTest) {
                this.fcManager.getFwVersion((byte) X8S_Module.MODULE_FC.ordinal(), (byte) 0, new UiCallBackListener<AckVersion>() {
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleFcAckVersion(o);
                        }
                        RelayProcess.this.onGetVersionResult();
                    }
                });
            }
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_REPEATER_RC.ordinal(), (byte) 11, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleRepeaterRcVersion(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_CAMERA.ordinal(), (byte) 4, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleCameraVersion(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_CV.ordinal(), (byte) 9, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleCvVersion(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_RC.ordinal(), (byte) 1, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleRcVersion(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_REPEATER_VEHICLE.ordinal(), (byte) 12, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleRepeaterVehicleVersion(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_ESC.ordinal(), (byte) 14, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleEscVersion(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_GIMBAL.ordinal(), (byte) 3, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleGimbalVersion(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_BATTERY.ordinal(), (byte) 5, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleBatteryVersion(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_NFZ.ordinal(), (byte) 10, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleNfzVersion(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
            this.fcManager.getFwVersion((byte) X8S_Module.MODULE_ULTRASONIC.ordinal(), (byte) 13, new UiCallBackListener<AckVersion>() {
                public void onComplete(CmdResult cmdResult, AckVersion o) {
                    if (cmdResult.isSuccess()) {
                        StateManager.getInstance().getVersionState().setModuleUltrasonic(o);
                    }
                    RelayProcess.this.onGetVersionResult();
                }
            });
        }
    }

    public void onGetVersionResult() {
        this.countFw++;
        if (this.countFw == this.MAXFW) {
            if (this.isShowUpdateView && StateManager.getInstance().getVersionState().getModuleFcAckVersion() != null) {
                checkVersion();
                this.isShowUpdateView = false;
            }
            VersionState versionState = StateManager.getInstance().getVersionState();
            if (versionState.getModuleFcAckVersion() != null) {
                HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleFcAckVersion().getType(), versionState.getModuleFcAckVersion().getModel(), (long) versionState.getModuleFcAckVersion().getSoftVersion(), ""));
                if (versionState.getModuleRcVersion() != null) {
                    HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleRcVersion().getType(), versionState.getModuleRcVersion().getModel(), (long) versionState.getModuleRcVersion().getSoftVersion(), ""));
                    if (versionState.getModuleCvVersion() != null) {
                        HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleCvVersion().getType(), versionState.getModuleCvVersion().getModel(), (long) versionState.getModuleCvVersion().getSoftVersion(), ""));
                        if (versionState.getModuleRepeaterRcVersion() != null) {
                            HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleRepeaterRcVersion().getType(), versionState.getModuleRepeaterRcVersion().getModel(), (long) versionState.getModuleRepeaterRcVersion().getSoftVersion(), ""));
                            if (versionState.getModuleRepeaterVehicleVersion() != null) {
                                HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleRepeaterVehicleVersion().getType(), versionState.getModuleRepeaterVehicleVersion().getModel(), (long) versionState.getModuleRepeaterVehicleVersion().getSoftVersion(), ""));
                                if (versionState.getModuleEscVersion() != null) {
                                    HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleEscVersion().getType(), versionState.getModuleEscVersion().getModel(), (long) versionState.getModuleEscVersion().getSoftVersion(), ""));
                                    if (versionState.getModuleGimbalVersion() != null) {
                                        HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleGimbalVersion().getType(), versionState.getModuleGimbalVersion().getModel(), (long) versionState.getModuleGimbalVersion().getSoftVersion(), ""));
                                        if (versionState.getModuleBatteryVersion() != null) {
                                            HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleBatteryVersion().getType(), versionState.getModuleBatteryVersion().getModel(), (long) versionState.getModuleBatteryVersion().getSoftVersion(), ""));
                                            if (versionState.getModuleNfzVersion() != null) {
                                                HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleNfzVersion().getType(), versionState.getModuleNfzVersion().getModel(), (long) versionState.getModuleNfzVersion().getSoftVersion(), ""));
                                                if (versionState.getModuleCameraVersion() != null) {
                                                    HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleCameraVersion().getType(), versionState.getModuleCameraVersion().getModel(), (long) versionState.getModuleCameraVersion().getSoftVersion(), ""));
                                                    if (versionState.getModuleUltrasonic() != null) {
                                                        HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleUltrasonic().getType(), versionState.getModuleUltrasonic().getModel(), (long) versionState.getModuleUltrasonic().getSoftVersion(), ""));
                                                        SessionManager.getInstance().onDeviveState(1);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setFcManager(FcManager fcManager, FcCtrlManager mFcCtrlManager, CameraManager cameraManager) {
        this.fcManager = fcManager;
        this.mFcCtrlManager = mFcCtrlManager;
        this.cameraManager = cameraManager;
    }

    public void checkVersion() {
    }

    public void getAllSetting() {
        if (!this.getSetting) {
            AllSettingManager.getInstance().getAllSetting();
            this.getSetting = true;
        }
    }
}
