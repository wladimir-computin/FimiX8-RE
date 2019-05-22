package com.fimi.x8sdk.connect.datatype;

import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.SeqCache;
import com.fimi.kernel.connect.interfaces.IRetransmissionHandle;
import com.fimi.kernel.connect.interfaces.ITimerSendQueueHandle;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.fmlink4.Parser4;
import com.fimi.x8sdk.command.X8BaseCmd.X8S_Module;
import com.fimi.x8sdk.connect.ConnectStatusManager;
import com.fimi.x8sdk.dataparser.AckAccurateLanding;
import com.fimi.x8sdk.dataparser.AckAccurateLandingState;
import com.fimi.x8sdk.dataparser.AckAiFollowGetEnableBack;
import com.fimi.x8sdk.dataparser.AckAiFollowGetSpeed;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.AckAiSurrounds;
import com.fimi.x8sdk.dataparser.AckCameraInterestMetering;
import com.fimi.x8sdk.dataparser.AckCameraPhotoMode;
import com.fimi.x8sdk.dataparser.AckCameraVideoMode;
import com.fimi.x8sdk.dataparser.AckCheckIMUException;
import com.fimi.x8sdk.dataparser.AckCloudCali;
import com.fimi.x8sdk.dataparser.AckCloudCaliState;
import com.fimi.x8sdk.dataparser.AckCloudParams;
import com.fimi.x8sdk.dataparser.AckFiveKeyDefine;
import com.fimi.x8sdk.dataparser.AckGetAiFollowMode;
import com.fimi.x8sdk.dataparser.AckGetAiLinePoint;
import com.fimi.x8sdk.dataparser.AckGetAiLinePointsAction;
import com.fimi.x8sdk.dataparser.AckGetAiPoint;
import com.fimi.x8sdk.dataparser.AckGetAiSurroundPoint;
import com.fimi.x8sdk.dataparser.AckGetApMode;
import com.fimi.x8sdk.dataparser.AckGetCaliState;
import com.fimi.x8sdk.dataparser.AckGetFcParam;
import com.fimi.x8sdk.dataparser.AckGetGimbalGain;
import com.fimi.x8sdk.dataparser.AckGetHorizontalAdjust;
import com.fimi.x8sdk.dataparser.AckGetIMUInfo;
import com.fimi.x8sdk.dataparser.AckGetLostAction;
import com.fimi.x8sdk.dataparser.AckGetLowPowerOpt;
import com.fimi.x8sdk.dataparser.AckGetOpticFlow;
import com.fimi.x8sdk.dataparser.AckGetPilotMode;
import com.fimi.x8sdk.dataparser.AckGetPitchSpeed;
import com.fimi.x8sdk.dataparser.AckGetRcMode;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;
import com.fimi.x8sdk.dataparser.AckGetSensitivity;
import com.fimi.x8sdk.dataparser.AckGetSportMode;
import com.fimi.x8sdk.dataparser.AckNoFlyNormal;
import com.fimi.x8sdk.dataparser.AckNormalCmds;
import com.fimi.x8sdk.dataparser.AckPanoramaPhotographType;
import com.fimi.x8sdk.dataparser.AckRcCalibrationState;
import com.fimi.x8sdk.dataparser.AckRightRoller;
import com.fimi.x8sdk.dataparser.AckSetCloudParams;
import com.fimi.x8sdk.dataparser.AckSetFcParam;
import com.fimi.x8sdk.dataparser.AckSetLostAction;
import com.fimi.x8sdk.dataparser.AckSetRetHeight;
import com.fimi.x8sdk.dataparser.AckStartRecord;
import com.fimi.x8sdk.dataparser.AckStopRecord;
import com.fimi.x8sdk.dataparser.AckSyncTime;
import com.fimi.x8sdk.dataparser.AckTFCarddCap;
import com.fimi.x8sdk.dataparser.AckTakeOffAndLand;
import com.fimi.x8sdk.dataparser.AckTakePhoto;
import com.fimi.x8sdk.dataparser.AckUpdateCurrentProgress;
import com.fimi.x8sdk.dataparser.AckUpdateRequest;
import com.fimi.x8sdk.dataparser.AckUpdateRequestPutFile;
import com.fimi.x8sdk.dataparser.AckUpdateSystemStatus;
import com.fimi.x8sdk.dataparser.AckVcSetRectF;
import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.dataparser.AutoAiFollowErrorCode;
import com.fimi.x8sdk.dataparser.AutoAiSurroundState;
import com.fimi.x8sdk.dataparser.AutoBlackBox30;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.dataparser.AutoFcBattery;
import com.fimi.x8sdk.dataparser.AutoFcErrCode;
import com.fimi.x8sdk.dataparser.AutoFcHeart;
import com.fimi.x8sdk.dataparser.AutoFcSignalState;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.dataparser.AutoFixedwingState;
import com.fimi.x8sdk.dataparser.AutoGimbalState;
import com.fimi.x8sdk.dataparser.AutoHomeInfo;
import com.fimi.x8sdk.dataparser.AutoNavigationState;
import com.fimi.x8sdk.dataparser.AutoNfzState;
import com.fimi.x8sdk.dataparser.AutoNotifyFwFile;
import com.fimi.x8sdk.dataparser.AutoRCMatchRt;
import com.fimi.x8sdk.dataparser.AutoRcState;
import com.fimi.x8sdk.dataparser.AutoRelayHeart;
import com.fimi.x8sdk.dataparser.AutoVcTracking;
import com.fimi.x8sdk.dataparser.cmd.AckGetAutoHome;
import com.fimi.x8sdk.entity.X8AppSettingLog;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.process.FcErrProcess;
import com.fimi.x8sdk.process.RelayProcess;
import com.fimi.x8sdk.rtp.X8Rtp;

public class FmLinkDataChanel implements IDataChanel {
    boolean isResponse = false;
    private SeqCache mSeqCache = new SeqCache();
    private Parser4 p = new Parser4();
    IRetransmissionHandle retransmissionHandle;
    private ConnectStatusManager statusManager = new ConnectStatusManager();
    ITimerSendQueueHandle timerSendQueueHandle;

    public void forwardData(byte[] buffer) {
        if (buffer != null && buffer.length > 0 && buffer != null && buffer.length > 0) {
            for (byte b : buffer) {
                LinkPacket4 packet = this.p.unPacket(b & 255);
                if (packet != null) {
                    onRequestCmd(packet);
                    this.statusManager.onDataRecieved(packet);
                }
            }
        }
    }

    private void onRequestCmd(LinkPacket4 linkPacket4) {
        if (this.retransmissionHandle != null && this.timerSendQueueHandle != null) {
            boolean isModuleCv;
            StateManager.getInstance().getRelayState().updateLastRlHeartTime();
            int msgId = linkPacket4.getPayLoad4().getMsgId() & 255;
            int groupId = linkPacket4.getPayLoad4().getGroupId() & 255;
            if (groupId != 3 || 87 == msgId || 1 == msgId) {
            }
            this.isResponse = false;
            boolean isModuleCamera;
            if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_CAMERA.ordinal() || !this.mSeqCache.isExist(linkPacket4.getHeader4().getSeq())) {
                isModuleCamera = false;
            } else {
                isModuleCamera = true;
            }
            if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_CV.ordinal() || !this.mSeqCache.isExist(linkPacket4.getHeader4().getSeq())) {
                isModuleCv = false;
            } else {
                isModuleCv = true;
            }
            if (!isModuleCv || !isModuleCamera) {
                if (linkPacket4.getHeader4().getSrcId() != X8S_Module.MODULE_CV.ordinal()) {
                    if (this.retransmissionHandle.removeFromListByCmdIDLinkPacket4(groupId, msgId, linkPacket4.getHeader4().getSeq(), linkPacket4)) {
                        this.mSeqCache.add2SeqCache(linkPacket4.getHeader4().getSeq());
                    }
                    this.isResponse = true;
                } else if (linkPacket4.getHeader4().getSeq() == (short) 0) {
                    this.isResponse = true;
                } else {
                    this.isResponse = this.retransmissionHandle.removeFromListByCmdIDLinkPacket4(groupId, msgId, linkPacket4.getHeader4().getSeq(), linkPacket4);
                }
                if (!this.isResponse) {
                    return;
                }
                if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_CAMERA.ordinal()) {
                    fromCameraCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_REPEATER_RC.ordinal()) {
                    fromRelayCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_REPEATER_VEHICLE.ordinal()) {
                    fromFcRelayCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_FC.ordinal()) {
                    StateManager.getInstance().getX8Drone().updateLastFcHeartTime();
                    fromFcCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_NFZ.ordinal()) {
                    fromNFZCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_CV.ordinal()) {
                    fromVcCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_ESC.ordinal()) {
                    fromEscCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_GIMBAL.ordinal()) {
                    fromGimbalCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_BATTERY.ordinal()) {
                    fromBatteryCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_RC.ordinal()) {
                    fromRcCmd(msgId, groupId, linkPacket4);
                } else if (linkPacket4.getHeader4().getSrcId() == X8S_Module.MODULE_ULTRASONIC.ordinal()) {
                    fromUltrasonicCmd(msgId, groupId, linkPacket4);
                }
            }
        }
    }

    private void fromFcRelayCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 16) {
            switch (msgId) {
                case 177:
                    AckVersion ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    return;
                default:
                    return;
            }
        }
    }

    private void fromRcCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId != 16) {
            if (groupId == 14) {
                switch (msgId) {
                    case 2:
                        AutoRCMatchRt autoRCMatchRt = new AutoRCMatchRt();
                        autoRCMatchRt.unPacket(linkPacket4);
                        StateManager.getInstance().getRcMatchState().setMatchRt(autoRCMatchRt);
                        break;
                }
            }
        }
        switch (msgId) {
            case 177:
                AckVersion ackVersion = new AckVersion();
                ackVersion.unPacket(linkPacket4);
                notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                break;
        }
        if (groupId == 11) {
            switch (msgId) {
                case 4:
                    AutoRcState rcs = new AutoRcState();
                    rcs.unPacket(linkPacket4);
                    StateManager.getInstance().getErrorCodeState().setErrorCodeRcs(rcs.getErroCode());
                    return;
                case 14:
                case 15:
                    AckRcCalibrationState ackRcCibartionState = new AckRcCalibrationState();
                    ackRcCibartionState.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackRcCibartionState, linkPacket4);
                    return;
                case 16:
                    AckFiveKeyDefine ackFiveKeyDefine = new AckFiveKeyDefine();
                    ackFiveKeyDefine.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackFiveKeyDefine, linkPacket4);
                    return;
                case 17:
                    AckNormalCmds normalCmd = new AckNormalCmds();
                    normalCmd.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, normalCmd, linkPacket4);
                    return;
                case 18:
                    AckGetRcMode rcMode = new AckGetRcMode();
                    rcMode.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, rcMode, linkPacket4);
                    return;
                case 19:
                    AckRightRoller rightRoller = new AckRightRoller();
                    rightRoller.unPacket(linkPacket4);
                    StateManager.getInstance().setAckRightRoller(rightRoller);
                    return;
                default:
                    return;
            }
        }
    }

    private void fromBatteryCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 16) {
            switch (msgId) {
                case 177:
                    AckVersion ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    return;
                default:
                    return;
            }
        }
    }

    private void fromUltrasonicCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 16) {
            switch (msgId) {
                case 177:
                    AckVersion ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    return;
                default:
                    return;
            }
        }
    }

    private void fromGimbalCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 16) {
            switch (msgId) {
                case 177:
                    AckVersion ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    return;
                default:
                    return;
            }
        } else if (groupId == 9) {
            switch (msgId) {
                case 1:
                    AutoGimbalState autoGimbalState = new AutoGimbalState();
                    autoGimbalState.unPacket(linkPacket4);
                    StateManager.getInstance().getGimbalState().setAutoGimbalState(autoGimbalState);
                    return;
                case 6:
                    AckNormalCmds ackPosrate = new AckNormalCmds();
                    ackPosrate.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackPosrate, linkPacket4);
                    return;
                case 30:
                    AckNormalCmds ackNormalCmds1 = new AckNormalCmds();
                    ackNormalCmds1.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackNormalCmds1, linkPacket4);
                    return;
                case 31:
                    AckGetGimbalGain getGimbalGain = new AckGetGimbalGain();
                    getGimbalGain.unPacket(linkPacket4);
                    HostLogBack.getInstance().writeLog("getGimbalGain==" + getGimbalGain.toString());
                    notityX8Message(groupId, msgId, getGimbalGain, linkPacket4);
                    return;
                case 40:
                    ILinkMessage normalCmds = new AckNormalCmds();
                    normalCmds.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, normalCmds, linkPacket4);
                    return;
                case 41:
                    ILinkMessage pitchSpeed = new AckGetPitchSpeed();
                    pitchSpeed.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, pitchSpeed, linkPacket4);
                    return;
                case 42:
                    AckNormalCmds ac = new AckNormalCmds();
                    ac.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ac, linkPacket4);
                    return;
                case 43:
                    ILinkMessage horizontalAdjust = new AckGetHorizontalAdjust();
                    horizontalAdjust.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, horizontalAdjust, linkPacket4);
                    return;
                case 44:
                    AckCloudCali ackCloudCali = new AckCloudCali();
                    ackCloudCali.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackCloudCali, linkPacket4);
                    return;
                case 45:
                    AckCloudCaliState cloudCaliState = new AckCloudCaliState();
                    cloudCaliState.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, cloudCaliState, linkPacket4);
                    return;
                case 47:
                    AckNormalCmds ackNormalCmds = new AckNormalCmds();
                    ackNormalCmds.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackNormalCmds, linkPacket4);
                    return;
                case 105:
                    AckSetCloudParams ackSetCloudParams = new AckSetCloudParams();
                    ackSetCloudParams.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackSetCloudParams, linkPacket4);
                    return;
                case 106:
                    AckCloudParams ackCloudParams = new AckCloudParams();
                    ackCloudParams.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackCloudParams, linkPacket4);
                    return;
                default:
                    return;
            }
        }
    }

    private void fromEscCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 16) {
            switch (msgId) {
                case 177:
                    AckVersion ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    return;
                default:
                    return;
            }
        }
    }

    private void fromVcCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 15) {
            switch (msgId) {
                case 3:
                    AckVcSetRectF ackVcSetRectF = new AckVcSetRectF();
                    ackVcSetRectF.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVcSetRectF, linkPacket4);
                    break;
                case 4:
                    AutoVcTracking vcTracking = new AutoVcTracking();
                    vcTracking.unPacket(linkPacket4);
                    StateManager.getInstance().onTracking(vcTracking);
                    break;
                case 16:
                    AckNormalCmds setFpvMode = new AckNormalCmds();
                    setFpvMode.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, setFpvMode, linkPacket4);
                    break;
            }
        }
        if (groupId == 16) {
            switch (msgId) {
                case 177:
                    AckVersion ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    return;
                default:
                    return;
            }
        }
    }

    private void fromNFZCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 17) {
            switch (msgId) {
                case 1:
                    AckNoFlyNormal flyNormal = new AckNoFlyNormal();
                    flyNormal.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, flyNormal, linkPacket4);
                    StateManager.getInstance().getNfzState().setAckNoFlyNormal(flyNormal);
                    break;
                case 2:
                    AckNoFlyNormal flyNormal2 = new AckNoFlyNormal();
                    flyNormal2.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, flyNormal2, linkPacket4);
                    break;
                case 3:
                    AutoNfzState nfzs = new AutoNfzState();
                    nfzs.unPacket(linkPacket4);
                    StateManager.getInstance().getErrorCodeState().setNfzErrorCode(nfzs.getState());
                    break;
            }
        }
        if (groupId == 16) {
            switch (msgId) {
                case 177:
                    AckVersion ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    return;
                default:
                    return;
            }
        }
    }

    private void fromFcCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 16) {
            switch (msgId) {
                case 177:
                    ILinkMessage ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    if (X8Rtp.simulationTest) {
                        StateManager.getInstance().getVersionState().setModuleFcAckVersion(ackVersion);
                        StateManager.getInstance().getVersionState().setModuleRepeaterRcVersion(ackVersion);
                        AutoRelayHeart relayHeart = new AutoRelayHeart();
                        relayHeart.setStatus((short) 2045);
                        StateManager.getInstance().getCamera().setCameraStatus(relayHeart.getStatus());
                        StateManager.getInstance().getRelayState().setRelayHeart(relayHeart);
                        RelayProcess.getRelayProcess().setRelayHeart(relayHeart);
                        break;
                    }
                    break;
            }
        }
        if (StateManager.getInstance().getX8Drone().isConnect()) {
            ILinkMessage ackNormalCmds;
            if (groupId == 4) {
                switch (msgId) {
                    case 1:
                        ILinkMessage d = new AckNormalCmds();
                        d.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, d, linkPacket4);
                        break;
                    case 2:
                        ILinkMessage afcParam = new AckGetPilotMode();
                        afcParam.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, afcParam, linkPacket4);
                        StateManager.getInstance().getX8Drone().setPilotMode(afcParam.getPilotMode());
                        break;
                    case 3:
                        ILinkMessage common1 = new AckNormalCmds();
                        common1.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, common1, linkPacket4);
                        break;
                    case 4:
                        ILinkMessage getSportMode = new AckGetSportMode();
                        getSportMode.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, getSportMode, linkPacket4);
                        break;
                    case 5:
                        ILinkMessage param = new AckSetFcParam();
                        param.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, param, linkPacket4);
                        break;
                    case 6:
                        ILinkMessage fcParam = new AckGetFcParam();
                        fcParam.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, fcParam, linkPacket4);
                        break;
                    case 8:
                        ILinkMessage setRetHeight = new AckSetRetHeight();
                        setRetHeight.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, setRetHeight, linkPacket4);
                        break;
                    case 9:
                        ILinkMessage retHeight = new AckGetRetHeight();
                        retHeight.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, retHeight, linkPacket4);
                        StateManager.getInstance().getX8Drone().setReturnHomeHight(retHeight.getHeight());
                        break;
                    case 10:
                        ILinkMessage setEnableBack = new AckNormalCmds();
                        setEnableBack.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, setEnableBack, linkPacket4);
                        break;
                    case 11:
                        ILinkMessage getEnableBack = new AckAiFollowGetEnableBack();
                        getEnableBack.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, getEnableBack, linkPacket4);
                        StateManager.getInstance().getX8Drone().setFollowReturn(getEnableBack.getEnable());
                        break;
                    case 12:
                        ILinkMessage setLostAction = new AckSetLostAction();
                        setLostAction.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, setLostAction, linkPacket4);
                        break;
                    case 13:
                        ILinkMessage getLostAction = new AckGetLostAction();
                        getLostAction.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, getLostAction, linkPacket4);
                        X8AppSettingLog.lostAction = getLostAction.getAction();
                        break;
                    case 14:
                        ILinkMessage setCmds = new AckNormalCmds();
                        setCmds.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, setCmds, linkPacket4);
                        break;
                    case 15:
                        ILinkMessage getOpticFlow = new AckGetOpticFlow();
                        getOpticFlow.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, getOpticFlow, linkPacket4);
                        break;
                    case 21:
                        ILinkMessage apMode = new AckGetApMode();
                        apMode.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, apMode, linkPacket4);
                        break;
                    case 23:
                        ILinkMessage lowPowerOpt = new AckGetLowPowerOpt();
                        lowPowerOpt.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, lowPowerOpt, linkPacket4);
                        break;
                    case 24:
                        ILinkMessage setCmd = new AckNormalCmds();
                        setCmd.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, setCmd, linkPacket4);
                        break;
                    case 25:
                        ILinkMessage respCmd6 = new AckNormalCmds();
                        respCmd6.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, respCmd6, linkPacket4);
                        break;
                    case 26:
                        ILinkMessage respCmd7 = new AckGetSensitivity();
                        respCmd7.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, respCmd7, linkPacket4);
                        X8AppSettingLog.setExp(respCmd7.getPitchPercent(), respCmd7.getRollPercent(), respCmd7.getThroPercent(), respCmd7.getYawPercent());
                        break;
                    case 33:
                        ILinkMessage respCmd4 = new AckNormalCmds();
                        respCmd4.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, respCmd4, linkPacket4);
                        break;
                    case 34:
                        ILinkMessage respCmd5 = new AckGetSensitivity();
                        respCmd5.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, respCmd5, linkPacket4);
                        X8AppSettingLog.setYawTrip(respCmd5.getPitchPercent(), respCmd5.getRollPercent(), respCmd5.getThroPercent(), respCmd5.getYawPercent());
                        break;
                    case 35:
                        ILinkMessage respCmd2 = new AckNormalCmds();
                        respCmd2.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, respCmd2, linkPacket4);
                        break;
                    case 36:
                        ILinkMessage respCmd3 = new AckGetSensitivity();
                        respCmd3.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, respCmd3, linkPacket4);
                        X8AppSettingLog.setFb(respCmd3.getPitchPercent(), respCmd3.getRollPercent(), respCmd3.getThroPercent(), respCmd3.getYawPercent());
                        StateManager.getInstance().getX8Drone().setFcBrakeSenssity(respCmd3.getRollPercent());
                        break;
                    case 37:
                        ILinkMessage respCmd = new AckNormalCmds();
                        respCmd.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, respCmd, linkPacket4);
                        break;
                    case 38:
                        ILinkMessage respCmd1 = new AckGetSensitivity();
                        respCmd1.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, respCmd1, linkPacket4);
                        X8AppSettingLog.setFs(respCmd1.getPitchPercent(), respCmd1.getRollPercent(), respCmd1.getThroPercent(), respCmd1.getYawPercent());
                        StateManager.getInstance().getX8Drone().setFcYAWSenssity(respCmd1.getYawPercent());
                        break;
                    case 39:
                        ILinkMessage setAutoHome = new AckNormalCmds();
                        setAutoHome.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, setAutoHome, linkPacket4);
                        break;
                    case 40:
                        ILinkMessage ackGetAutoHome = new AckGetAutoHome();
                        ackGetAutoHome.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackGetAutoHome, linkPacket4);
                        X8AppSettingLog.noChangeFollowRP(ackGetAutoHome.getState() == 1);
                        break;
                    case 41:
                        ILinkMessage enableTripod = new AckNormalCmds();
                        enableTripod.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, enableTripod, linkPacket4);
                        break;
                    case 42:
                        ILinkMessage disenableTripod = new AckNormalCmds();
                        disenableTripod.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, disenableTripod, linkPacket4);
                        break;
                    case 43:
                        ILinkMessage enableAerailShot = new AckNormalCmds();
                        enableAerailShot.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, enableAerailShot, linkPacket4);
                        break;
                    case 44:
                        ILinkMessage disenableAerailShot = new AckNormalCmds();
                        disenableAerailShot.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, disenableAerailShot, linkPacket4);
                        break;
                    case 45:
                        ILinkMessage enableHeadingFree = new AckNormalCmds();
                        enableHeadingFree.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, enableHeadingFree, linkPacket4);
                        break;
                    case 46:
                        ILinkMessage disenableHeadingFree = new AckNormalCmds();
                        disenableHeadingFree.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, disenableHeadingFree, linkPacket4);
                        break;
                    case 47:
                        ILinkMessage enableFixwing = new AckNormalCmds();
                        enableFixwing.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, enableFixwing, linkPacket4);
                        break;
                    case 48:
                        AckNormalCmds DisenableFixwing = new AckNormalCmds();
                        DisenableFixwing.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, DisenableFixwing, linkPacket4);
                        break;
                    case 49:
                        AutoFixedwingState mAutoFixedwingState = new AutoFixedwingState();
                        mAutoFixedwingState.unPacket(linkPacket4);
                        StateManager.getInstance().getX8Drone().setAutoFixedwingState(mAutoFixedwingState);
                        break;
                    case 50:
                        ILinkMessage updateHeadingFree = new AckNormalCmds();
                        updateHeadingFree.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, updateHeadingFree, linkPacket4);
                        break;
                    case 51:
                        ILinkMessage enableAccurate = new AckNormalCmds();
                        enableAccurate.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, enableAccurate, linkPacket4);
                        StateManager.getInstance().getX8Drone().setAccurateLanding(1);
                        break;
                    case 52:
                        ILinkMessage disenableAccurate = new AckNormalCmds();
                        disenableAccurate.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, disenableAccurate, linkPacket4);
                        StateManager.getInstance().getX8Drone().setAccurateLanding(0);
                        break;
                    case 53:
                        ILinkMessage getAccurateLanding = new AckAccurateLanding();
                        getAccurateLanding.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, getAccurateLanding, linkPacket4);
                        StateManager.getInstance().getX8Drone().setAccurateLanding(getAccurateLanding.getState());
                        break;
                    case 54:
                        ackNormalCmds = new AckNormalCmds();
                        ackNormalCmds.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackNormalCmds, linkPacket4);
                        break;
                    case 137:
                        ILinkMessage common = new AckNormalCmds();
                        common.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, common, linkPacket4);
                        break;
                }
            }
            if (groupId == 3) {
                switch (msgId) {
                    case 1:
                        AutoNavigationState mAutoNavigationState = new AutoNavigationState();
                        mAutoNavigationState.unPacket(linkPacket4);
                        StateManager.getInstance().getX8Drone().setANavigationState(mAutoNavigationState);
                        break;
                    case 16:
                        ILinkMessage ackTakeOffAndLand = new AckTakeOffAndLand();
                        ackTakeOffAndLand.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackTakeOffAndLand, linkPacket4);
                        break;
                    case 19:
                        ILinkMessage ackTakeoffExit = new AckNormalCmds();
                        ackTakeoffExit.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackTakeoffExit, linkPacket4);
                        break;
                    case 21:
                        ILinkMessage takeOffAndLand = new AckTakeOffAndLand();
                        takeOffAndLand.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, takeOffAndLand, linkPacket4);
                        break;
                    case 24:
                        ILinkMessage ackLandExit = new AckNormalCmds();
                        ackLandExit.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackLandExit, linkPacket4);
                        break;
                    case 26:
                        ILinkMessage ackReturnHomeExcute = new AckNormalCmds();
                        ackReturnHomeExcute.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackReturnHomeExcute, linkPacket4);
                        break;
                    case 29:
                        ILinkMessage ackReturnHomeExite = new AckNormalCmds();
                        ackReturnHomeExite.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackReturnHomeExite, linkPacket4);
                        break;
                    case 32:
                        ILinkMessage ackLineExcute = new AckNormalCmds();
                        ackLineExcute.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackLineExcute, linkPacket4);
                        break;
                    case 35:
                        ILinkMessage ackLineExite = new AckNormalCmds();
                        ackLineExite.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackLineExite, linkPacket4);
                        break;
                    case 36:
                        ILinkMessage ackLineSetPoints = new AckNormalCmds();
                        ackLineSetPoints.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackLineSetPoints, linkPacket4);
                        break;
                    case 37:
                        ILinkMessage ackSetPointsAction = new AckNormalCmds();
                        ackSetPointsAction.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackSetPointsAction, linkPacket4);
                        break;
                    case 38:
                        ILinkMessage ackGetAiLinePoint = new AckGetAiLinePoint();
                        ackGetAiLinePoint.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackGetAiLinePoint, linkPacket4);
                        break;
                    case 39:
                        ILinkMessage ackgetPointsAction = new AckGetAiLinePointsAction();
                        ackgetPointsAction.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackgetPointsAction, linkPacket4);
                        break;
                    case 48:
                        ILinkMessage ackPoint2PointExcute = new AckNormalCmds();
                        ackPoint2PointExcute.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackPoint2PointExcute, linkPacket4);
                        break;
                    case 51:
                        ILinkMessage ackSetP2PExite = new AckNormalCmds();
                        ackSetP2PExite.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackSetP2PExite, linkPacket4);
                        break;
                    case 52:
                        ILinkMessage ackPoint2Point = new AckNormalCmds();
                        ackPoint2Point.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackPoint2Point, linkPacket4);
                        break;
                    case 53:
                        ILinkMessage mAckGetAiPoint = new AckGetAiPoint();
                        mAckGetAiPoint.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, mAckGetAiPoint, linkPacket4);
                        break;
                    case 56:
                        AckNormalCmds ackAutoPhotoExcute = new AckNormalCmds();
                        ackAutoPhotoExcute.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackAutoPhotoExcute, linkPacket4);
                        break;
                    case 59:
                        ILinkMessage ackAutoPhotoExite = new AckNormalCmds();
                        ackAutoPhotoExite.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackAutoPhotoExite, linkPacket4);
                        break;
                    case 60:
                        ILinkMessage ackAutoPhotoValue = new AckNormalCmds();
                        ackAutoPhotoValue.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackAutoPhotoValue, linkPacket4);
                        break;
                    case 64:
                        ILinkMessage ackSurrondExcute = new AckNormalCmds();
                        ackSurrondExcute.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackSurrondExcute, linkPacket4);
                        break;
                    case 67:
                        ILinkMessage ackkSurroundExit = new AckNormalCmds();
                        ackkSurroundExit.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackkSurroundExit, linkPacket4);
                        break;
                    case 68:
                        ILinkMessage ackSurroundCircleDot = new AckNormalCmds();
                        ackSurroundCircleDot.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackSurroundCircleDot, linkPacket4);
                        break;
                    case 69:
                        ILinkMessage ackGetAiSurroundPoint = new AckGetAiSurroundPoint();
                        ackGetAiSurroundPoint.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackGetAiSurroundPoint, linkPacket4);
                        break;
                    case 70:
                        ILinkMessage ackSurroundSpeed = new AckNormalCmds();
                        ackSurroundSpeed.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackSurroundSpeed, linkPacket4);
                        break;
                    case 71:
                        AckAiSurrounds acGgetSurroundSpeed = new AckAiSurrounds();
                        acGgetSurroundSpeed.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, acGgetSurroundSpeed, linkPacket4);
                        break;
                    case 72:
                        ILinkMessage ackSurroundOrientation = new AckNormalCmds();
                        ackSurroundOrientation.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackSurroundOrientation, linkPacket4);
                        break;
                    case 73:
                        ILinkMessage ackGetSurroundOrientation = new AckAiSurrounds();
                        ackGetSurroundOrientation.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackGetSurroundOrientation, linkPacket4);
                        break;
                    case 74:
                        AutoAiSurroundState mAutoAiSurroundState = new AutoAiSurroundState();
                        mAutoAiSurroundState.unPacket(linkPacket4);
                        StateManager.getInstance().getX8Drone().setAutoAiSurroundState(mAutoAiSurroundState);
                        break;
                    case 80:
                        ILinkMessage mAckAiFollowExcute = new AckNormalCmds();
                        mAckAiFollowExcute.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, mAckAiFollowExcute, linkPacket4);
                        break;
                    case 83:
                        ILinkMessage mAckAiFollowExit = new AckNormalCmds();
                        mAckAiFollowExit.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, mAckAiFollowExit, linkPacket4);
                        break;
                    case 84:
                        ILinkMessage mAckAiFollowStanby = new AckNormalCmds();
                        mAckAiFollowStanby.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, mAckAiFollowStanby, linkPacket4);
                        break;
                    case 85:
                        ILinkMessage aiModle = new AckNormalCmds();
                        aiModle.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, aiModle, linkPacket4);
                        break;
                    case 86:
                        ILinkMessage ackGetAiFollowMode = new AckGetAiFollowMode();
                        ackGetAiFollowMode.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackGetAiFollowMode, linkPacket4);
                        break;
                    case 87:
                        AutoAiFollowErrorCode mAutoAiFollowErrorCode = new AutoAiFollowErrorCode();
                        mAutoAiFollowErrorCode.unPacket(linkPacket4);
                        FcErrProcess.getFcErrProcess().setVcErrorCode(mAutoAiFollowErrorCode.getAiFollowErrorCode());
                        break;
                    case 88:
                        ILinkMessage ackSetFollowSpeed = new AckNormalCmds();
                        ackSetFollowSpeed.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackSetFollowSpeed, linkPacket4);
                        break;
                    case 89:
                        ILinkMessage ackGetFollowSpeed = new AckAiFollowGetSpeed();
                        ackGetFollowSpeed.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackGetFollowSpeed, linkPacket4);
                        break;
                    case 90:
                        ILinkMessage ackSetHomePoint = new AckNormalCmds();
                        ackSetHomePoint.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackSetHomePoint, linkPacket4);
                        break;
                    case 96:
                        ILinkMessage vcOpen = new AckNormalCmds();
                        vcOpen.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, vcOpen, linkPacket4);
                        break;
                    case 97:
                        ILinkMessage vcClodse = new AckNormalCmds();
                        vcClodse.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, vcClodse, linkPacket4);
                        break;
                    case 98:
                        ILinkMessage vcNotityFc = new AckNormalCmds();
                        vcNotityFc.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, vcNotityFc, linkPacket4);
                        break;
                    case 99:
                        ILinkMessage ackScrewStart = new AckNormalCmds();
                        ackScrewStart.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackScrewStart, linkPacket4);
                        break;
                    case 100:
                        ILinkMessage ackScrewPause = new AckNormalCmds();
                        ackScrewPause.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackScrewPause, linkPacket4);
                        break;
                    case 101:
                        ILinkMessage ackScrewResume = new AckNormalCmds();
                        ackScrewResume.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackScrewResume, linkPacket4);
                        break;
                    case 102:
                        ILinkMessage ackScrewExite = new AckNormalCmds();
                        ackScrewExite.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackScrewExite, linkPacket4);
                        break;
                    case 103:
                        ILinkMessage etScrewPrameter = new AckNormalCmds();
                        etScrewPrameter.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, etScrewPrameter, linkPacket4);
                        break;
                    case 104:
                        ILinkMessage getScrewPrameter = new AckAiScrewPrameter();
                        getScrewPrameter.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, getScrewPrameter, linkPacket4);
                        break;
                    case 105:
                        ackNormalCmds = new AckNormalCmds();
                        ackNormalCmds.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackNormalCmds, linkPacket4);
                        break;
                    case 106:
                        AckPanoramaPhotographType ackPanoramaPhotographType = new AckPanoramaPhotographType();
                        ackPanoramaPhotographType.unPacket(linkPacket4);
                        StateManager.getInstance().onPanoramicInformation(ackPanoramaPhotographType);
                        break;
                    case 108:
                        AckAccurateLandingState landingState = new AckAccurateLandingState();
                        landingState.unPacket(linkPacket4);
                        StateManager.getInstance().getX8Drone().setAccurateLandingCheckObj(landingState.getBit2());
                        break;
                    case 111:
                        ILinkMessage ackNormalCmdsStop = new AckNormalCmds();
                        ackNormalCmdsStop.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackNormalCmdsStop, linkPacket4);
                        break;
                }
            }
            if (groupId == 12) {
                switch (msgId) {
                    case 1:
                        ILinkMessage fcHeart = new AutoFcHeart();
                        fcHeart.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, fcHeart, linkPacket4);
                        StateManager.getInstance().getX8Drone().setFcHeart(fcHeart);
                        StateManager.getInstance().getX8Drone().setCtrlType(fcHeart.getCtrlType());
                        StateManager.getInstance().getX8Drone().setCtrlMode(fcHeart.getCtrlModel());
                        StateManager.getInstance().getErrorCodeState().setErroCodeAtcAndMtc(fcHeart.getTakeOffCap(), fcHeart.getAutoTakeOffCap());
                        return;
                    case 2:
                        ILinkMessage sportState = new AutoFcSportState();
                        sportState.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, sportState, linkPacket4);
                        StateManager.getInstance().getX8Drone().setFcSportState(sportState);
                        StateManager.getInstance().getX8Drone().setSportStateValue(sportState.getDeviceLongitude(), sportState.getDeviceLatitude(), sportState.getHeight(), sportState.getDeviceAngle());
                        return;
                    case 3:
                        ILinkMessage signalState = new AutoFcSignalState();
                        signalState.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, signalState, linkPacket4);
                        StateManager.getInstance().getX8Drone().setFcSingal(signalState);
                        return;
                    case 4:
                        ILinkMessage fcErrCode = new AutoFcErrCode();
                        fcErrCode.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, fcErrCode, linkPacket4);
                        StateManager.getInstance().getErrorCodeState().setErrorCode((AutoFcErrCode) fcErrCode);
                        return;
                    case 5:
                        ILinkMessage battery = new AutoFcBattery();
                        battery.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, battery, linkPacket4);
                        StateManager.getInstance().getX8Drone().setFcBatterState(battery);
                        StateManager.getInstance().onBatterProcess();
                        return;
                    case 6:
                        ILinkMessage homeInfo = new AutoHomeInfo();
                        homeInfo.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, homeInfo, linkPacket4);
                        StateManager.getInstance();
                        StateManager.getInstance().getX8Drone().setHomeInfo(homeInfo);
                        return;
                    case 7:
                        ILinkMessage ackGetIMUInfo = new AckGetIMUInfo();
                        ackGetIMUInfo.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackGetIMUInfo, linkPacket4);
                        return;
                    default:
                        return;
                }
            } else if (groupId == 13) {
                switch (msgId) {
                    case 5:
                        ILinkMessage ackCali = new AckNormalCmds();
                        ackCali.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackCali, linkPacket4);
                        return;
                    case 6:
                        ILinkMessage ackGetCaliState = new AckGetCaliState();
                        ackGetCaliState.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackGetCaliState, linkPacket4);
                        return;
                    case 8:
                        ILinkMessage checkIMUException = new AckCheckIMUException();
                        checkIMUException.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, checkIMUException, linkPacket4);
                        return;
                    default:
                        return;
                }
            } else if (groupId == 8) {
                switch (msgId) {
                    case 4:
                        ILinkMessage ackSyncTime = new AckSyncTime();
                        ackSyncTime.unPacket(linkPacket4);
                        notityX8Message(groupId, msgId, ackSyncTime, linkPacket4);
                        return;
                    default:
                        return;
                }
            } else if (groupId == 10) {
                new AutoBlackBox30().unPacket(linkPacket4);
            }
        }
    }

    public void setRetransmissionHandle(IRetransmissionHandle retransmissionHandle) {
        this.retransmissionHandle = retransmissionHandle;
    }

    public void setTimerSendQueueHandle(ITimerSendQueueHandle timerSendQueueHandle) {
        this.timerSendQueueHandle = timerSendQueueHandle;
    }

    private void fromRelayCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 14) {
            switch (msgId) {
                case 5:
                    AutoRelayHeart relayHeart = new AutoRelayHeart();
                    relayHeart.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, relayHeart, linkPacket4);
                    StateManager.getInstance().getCamera().setCameraStatus(relayHeart.getStatus());
                    StateManager.getInstance().getRelayState().setRelayHeart(relayHeart);
                    RelayProcess.getRelayProcess().setRelayHeart(relayHeart);
                    break;
            }
        }
        if (groupId == 16) {
            switch (msgId) {
                case 177:
                    AckVersion ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    break;
            }
        }
        if (groupId == 14) {
            switch (msgId) {
                case 10:
                    AckNormalCmds normalCmd = new AckNormalCmds();
                    normalCmd.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, normalCmd, linkPacket4);
                    break;
                case 15:
                    AckNormalCmds normalCmdRestart = new AckNormalCmds();
                    normalCmdRestart.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, normalCmdRestart, linkPacket4);
                    break;
            }
        }
        if (!StateManager.getInstance().getRelayState().isConnect()) {
        }
    }

    private void fromCameraCmd(int msgId, int groupId, LinkPacket4 linkPacket4) {
        if (groupId == 16) {
            switch (msgId) {
                case 2:
                    ILinkMessage requestUpdate = new AckUpdateRequest();
                    requestUpdate.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, requestUpdate, linkPacket4);
                    break;
                case 3:
                    AckUpdateRequestPutFile ackUpdateRequestPutFile = new AckUpdateRequestPutFile();
                    ackUpdateRequestPutFile.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackUpdateRequestPutFile, linkPacket4);
                    break;
                case 4:
                    ILinkMessage autoNotifyFwFile = new AutoNotifyFwFile();
                    autoNotifyFwFile.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, autoNotifyFwFile, linkPacket4);
                    break;
                case 5:
                    ILinkMessage systemStatus = new AckUpdateSystemStatus();
                    systemStatus.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, systemStatus, linkPacket4);
                    break;
                case 6:
                    AckUpdateCurrentProgress ackUpdateCurrentProgress = new AckUpdateCurrentProgress();
                    ackUpdateCurrentProgress.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackUpdateCurrentProgress, linkPacket4);
                    break;
                case 177:
                    AckVersion ackVersion = new AckVersion();
                    ackVersion.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackVersion, linkPacket4);
                    break;
            }
        }
        if (groupId == 2) {
            switch (msgId) {
                case 2:
                    AckStartRecord ackStartRecord = new AckStartRecord();
                    ackStartRecord.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackStartRecord, linkPacket4);
                    return;
                case 3:
                    AckStopRecord ackStopRecord = new AckStopRecord();
                    ackStopRecord.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackStopRecord, linkPacket4);
                    return;
                case 4:
                    AckTakePhoto ackTakePhoto = new AckTakePhoto();
                    ackTakePhoto.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackTakePhoto, linkPacket4);
                    return;
                case 8:
                    AckTFCarddCap ackTFCarddCap = new AckTFCarddCap();
                    ackTFCarddCap.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackTFCarddCap, linkPacket4);
                    return;
                case 10:
                    AckCameraPhotoMode ackCameraPhotoMode = new AckCameraPhotoMode();
                    ackCameraPhotoMode.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackCameraPhotoMode, linkPacket4);
                    return;
                case 11:
                    AckCameraVideoMode ackCameraVideoMode = new AckCameraVideoMode();
                    ackCameraVideoMode.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackCameraVideoMode, linkPacket4);
                    return;
                case 12:
                    AckCameraInterestMetering ackCameraInterestMetering = new AckCameraInterestMetering();
                    ackCameraInterestMetering.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, ackCameraInterestMetering, linkPacket4);
                    return;
                case 21:
                    AutoCameraStateADV autoCameraStateADV = new AutoCameraStateADV();
                    autoCameraStateADV.unPacket(linkPacket4);
                    notityX8Message(groupId, msgId, autoCameraStateADV, linkPacket4);
                    StateManager.getInstance().getCamera().setAutoCameraStateADV(autoCameraStateADV);
                    return;
                default:
                    return;
            }
        }
    }

    public void notityX8Message(int groupId, int msgId, ILinkMessage msg, LinkPacket4 packet) {
        msg.setUiCallBack(packet.getUiCallBack());
        if (packet.getPersonalDataCallBack() == null) {
            NoticeManager.getInstance().onDataCallBack(groupId, msgId, msg);
        } else {
            NoticeManager.getInstance().onPersonalDataCallBack(groupId, msgId, msg, packet.getPersonalDataCallBack());
        }
    }
}
