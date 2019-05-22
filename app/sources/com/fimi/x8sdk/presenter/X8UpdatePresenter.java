package com.fimi.x8sdk.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IConnectResultListener;
import com.fimi.kernel.connect.model.UpdateDateMessage;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.connect.session.UpdateDateListener;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.milink.ByteArrayToIntArray;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.FileUtil;
import com.fimi.kernel.utils.ThreadUtils;
import com.fimi.x8sdk.R;
import com.fimi.x8sdk.command.FwUpdateCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckUpdateCurrentProgress;
import com.fimi.x8sdk.dataparser.AutoNotifyFwFile;
import com.fimi.x8sdk.entity.UpdateCurrentProgressEntity;
import com.fimi.x8sdk.ivew.IUpdateAction;
import com.fimi.x8sdk.ivew.IX8UpdateProgressView;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.update.fwpack.ByteHexHelper;
import com.fimi.x8sdk.update.fwpack.FirmwareBuildPack;
import com.fimi.x8sdk.update.fwpack.FirmwareBuildPack.MergFileListener;
import com.fimi.x8sdk.update.fwpack.FwInfo;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class X8UpdatePresenter extends BasePresenter implements IUpdateAction, UpdateDateListener {
    private final int CONTINUOUS_MAX_NUMBER = 16;
    private final int EACH_PACKAGE_LEN = 1024;
    private final int UPDATE_STATE_END = 255;
    private final int WAIT_UPDATE_TIMEOUT = 3;
    private final int WHAT_UPDATE_FINISH = 2;
    private final int WHAT_UPDATE_PROGRESS = 1;
    private int aggregateProgress;
    boolean alreadyExist = false;
    private int callbackOffset = 0;
    int cameraConnectedState = -1;
    private int cheackUpdateTimeOut = 0;
    private Timer checkUpdateTimeout = new Timer();
    private Context context;
    private List<UpdateCurrentProgressEntity> currentProgressEntityList;
    private byte[] fileBytes;
    private int fileProgress;
    private String firewareName;
    private List<FwInfo> fwInfoList;
    private volatile List<FwInfo> fwInfos = new ArrayList();
    private int fwNumber;
    @SuppressLint({"HandlerLeak"})
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                X8UpdatePresenter.this.ix8UpdateProgressView.showUpdateProgress(true, msg.arg1, null, X8UpdatePresenter.this.firewareName);
            } else if (msg.what == 2) {
                X8UpdatePresenter.this.checkUpdateTimeout.cancel();
                X8UpdatePresenter.this.updateState = UpdateState.updateEnd;
                X8UpdatePresenter.this.disposeAddedData();
                int i;
                if (X8UpdatePresenter.this.updateFailure) {
                    X8UpdatePresenter.this.ix8UpdateProgressView.showUpdateProgress(false, msg.arg1, null, X8UpdatePresenter.this.firewareName);
                    for (i = 0; i < X8UpdatePresenter.this.fwInfos.size(); i++) {
                        HostLogBack.getInstance().writeLog("Alanqiu  =======updateFailure================= fwInfos.get(i).toString():" + ((FwInfo) X8UpdatePresenter.this.fwInfos.get(i)).toString());
                    }
                    return;
                }
                i = 0;
                while (i < X8UpdatePresenter.this.fwInfos.size() - 1) {
                    int j = X8UpdatePresenter.this.fwInfos.size() - 1;
                    while (j > i) {
                        if (((FwInfo) X8UpdatePresenter.this.fwInfos.get(j)).getTypeId() == ((FwInfo) X8UpdatePresenter.this.fwInfos.get(i)).getTypeId() && ((FwInfo) X8UpdatePresenter.this.fwInfos.get(j)).getModelId() == ((FwInfo) X8UpdatePresenter.this.fwInfos.get(i)).getModelId()) {
                            X8UpdatePresenter.this.fwInfos.remove(j);
                        }
                        j--;
                    }
                    HostLogBack.getInstance().writeLog("Alanqiu  ======================== fwInfos.get(i).toString():" + ((FwInfo) X8UpdatePresenter.this.fwInfos.get(i)).toString());
                    i++;
                }
                X8UpdatePresenter.this.ix8UpdateProgressView.showUpdateProgress(true, msg.arg1, X8UpdatePresenter.this.fwInfos, "");
            } else if (msg.what == 3) {
                X8UpdatePresenter.this.ix8UpdateProgressView.showUpdateProgress(false, msg.arg1, null, X8UpdatePresenter.this.firewareName);
            }
        }
    };
    private boolean hasAccumulate = false;
    boolean isCameraUpdate = false;
    private boolean isLockOffset = false;
    private IX8UpdateProgressView ix8UpdateProgressView;
    public IConnectResultListener mIConnectResultListener = new IConnectResultListener() {
        public void onConnected(String msg) {
            StateManager.getInstance().startUpdateTimer();
        }

        public void onDisconnect(String msg) {
            if (!X8UpdatePresenter.this.containRemoteControl()) {
                X8UpdatePresenter.this.updateFailure = true;
                X8UpdatePresenter.this.firewareName = X8UpdatePresenter.this.context.getString(R.string.x8_update_err_disconnect);
                X8UpdatePresenter.this.updateProgressView(2, 0);
                X8UpdatePresenter.this.updateFileEnd = true;
                X8UpdatePresenter.this.updateThread.interrupt();
            }
        }

        public void onConnectError(String msg) {
        }

        public void onDeviceConnect() {
        }

        public void onDeviceDisConnnect() {
        }
    };
    private int notifyProgress;
    volatile double offset = 0.0d;
    private int packNum = 0;
    private int residueNum;
    private int sendPackageNum;
    boolean startCheckUpdateTimeOut = false;
    private int subPackageNum;
    private long total;
    private boolean updateFailure = false;
    boolean updateFileEnd = false;
    UpdateState updateState = UpdateState.updateInit;
    private Thread updateThread;
    private int updateTimeoutAddTime = 0;
    private int updateTimeoutProgress = 0;
    private boolean waitSend = false;

    enum UpdateState {
        updateInit,
        requestUpdate,
        sendUploadInformation,
        updateFile,
        fileCheckResults,
        updateEnd
    }

    private class X8UpdateRunnable implements Runnable {
        private X8UpdateRunnable() {
        }

        /* synthetic */ X8UpdateRunnable(X8UpdatePresenter x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            try {
                X8UpdatePresenter.this.offset = 0.0d;
                RandomAccessFile randomFile = new RandomAccessFile(new File(FirmwareBuildPack.PKG_UPDATE_FILE), "r");
                X8UpdatePresenter.this.total = (long) ((int) randomFile.length());
                int packageNum = (int) (X8UpdatePresenter.this.total / 1024);
                int packageMod = (int) (X8UpdatePresenter.this.total % 1024);
                X8UpdatePresenter.this.updateState = UpdateState.updateFile;
                while (!X8UpdatePresenter.this.updateFileEnd && !Thread.interrupted()) {
                    if (!X8UpdatePresenter.this.waitSend) {
                        byte[] bytes2;
                        if (X8UpdatePresenter.this.offset <= ((double) X8UpdatePresenter.this.total)) {
                            if (X8UpdatePresenter.this.offset / 1024.0d != ((double) packageNum)) {
                                randomFile.seek((long) X8UpdatePresenter.this.offset);
                                byte[] bytes = new byte[1024];
                                randomFile.read(bytes, 0, 1024);
                                X8UpdatePresenter.this.sendCmd(new FwUpdateCollection().sendFwFileContent((int) X8UpdatePresenter.this.offset, bytes));
                                if (X8UpdatePresenter.this.isLockOffset) {
                                    X8UpdatePresenter.this.isLockOffset = false;
                                } else {
                                    X8UpdatePresenter.this.offset += 1024.0d;
                                    if ((X8UpdatePresenter.this.offset * 50.0d) / ((double) X8UpdatePresenter.this.total) >= ((double) X8UpdatePresenter.this.fileProgress)) {
                                        X8UpdatePresenter.this.fileProgress = (int) ((X8UpdatePresenter.this.offset * 50.0d) / ((double) X8UpdatePresenter.this.total));
                                        X8UpdatePresenter.this.updateProgressView(1, X8UpdatePresenter.this.fileProgress);
                                        HostLogBack.getInstance().writeLog("update uploadFwFile offset 222--" + X8UpdatePresenter.this.offset + "fileProgress:" + X8UpdatePresenter.this.fileProgress + "total:" + X8UpdatePresenter.this.total);
                                    }
                                }
                            } else {
                                bytes2 = new byte[packageMod];
                                randomFile.seek((long) (packageNum * 1024));
                                randomFile.read(bytes2, 0, packageMod);
                                X8UpdatePresenter.this.sendCmd(new FwUpdateCollection().sendFwFileContent((int) X8UpdatePresenter.this.offset, bytes2));
                                if (X8UpdatePresenter.this.isLockOffset) {
                                    X8UpdatePresenter.this.isLockOffset = false;
                                } else {
                                    X8UpdatePresenter.this.offset += (double) packageMod;
                                    if ((X8UpdatePresenter.this.offset * 50.0d) / ((double) X8UpdatePresenter.this.total) >= ((double) X8UpdatePresenter.this.fileProgress)) {
                                        X8UpdatePresenter.this.fileProgress = (int) ((X8UpdatePresenter.this.offset * 50.0d) / ((double) X8UpdatePresenter.this.total));
                                        HostLogBack.getInstance().writeLog("update uploadFwFile end packageMod:" + packageMod + "total:" + X8UpdatePresenter.this.total);
                                        X8UpdatePresenter.this.updateProgressView(1, X8UpdatePresenter.this.fileProgress);
                                    }
                                }
                            }
                            Thread.sleep(2);
                        } else {
                            bytes2 = new byte[packageMod];
                            randomFile.seek((long) (packageNum * 1024));
                            randomFile.read(bytes2, 0, packageMod);
                            X8UpdatePresenter.this.sendCmd(new FwUpdateCollection().sendFwFileContent((int) X8UpdatePresenter.this.offset, bytes2));
                            Thread.sleep(5);
                        }
                    } else {
                        return;
                    }
                }
                if (randomFile != null) {
                    randomFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                X8UpdatePresenter.this.updateThread.interrupt();
            }
        }
    }

    public X8UpdatePresenter(Context context) {
        this.context = context;
        SessionManager.getInstance().add2NoticeList(this.mIConnectResultListener);
        initDate();
    }

    /* Access modifiers changed, original: 0000 */
    public void initDate() {
        addNoticeListener((UpdateDateListener) this);
        checkUpdateOutStatus();
    }

    public void onPersonalDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    public void onPersonalSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        reponseCmd(false, groupId, msgId, null, bcd);
    }

    public void uploadFwFile() {
        this.fwInfos.clear();
        this.updateFileEnd = false;
        this.updateThread = new Thread(new X8UpdateRunnable(this, null));
        this.updateThread.start();
    }

    /* Access modifiers changed, original: protected */
    public void reponseCmd(boolean isAck, int groupId, int msgId, ILinkMessage packet, BaseCommand bcd) {
        if (groupId != 16) {
            return;
        }
        if (msgId == 3) {
            if (this.updateState == UpdateState.requestUpdate) {
                this.updateState = UpdateState.sendUploadInformation;
                HostLogBack.getInstance().writeLog("update MSG_ID_PUT_FILE");
                uploadFwFile();
            }
        } else if (msgId == 4) {
            AutoNotifyFwFile autoNotifyFwFile = (AutoNotifyFwFile) packet;
            FwInfo fwInfo;
            if (autoNotifyFwFile.getNotifyType() == 0) {
                if (autoNotifyFwFile.getResult() != 0) {
                    this.updateFailure = true;
                    this.firewareName = getErrorCodeString(autoNotifyFwFile.getResult());
                    updateProgressView(2, 0);
                }
            } else if (autoNotifyFwFile.getNotifyType() == 1) {
                if (autoNotifyFwFile.getResult() != 0) {
                    this.updateFailure = true;
                    this.firewareName = getErrorCodeString(autoNotifyFwFile.getResult());
                    updateProgressView(2, 0);
                }
            } else if (autoNotifyFwFile.getNotifyType() == 2) {
                if (autoNotifyFwFile.getResult() == 0) {
                    this.fwNumber = autoNotifyFwFile.getFwNumber();
                    this.startCheckUpdateTimeOut = true;
                    return;
                }
                this.updateFailure = true;
                this.firewareName = getErrorCodeString(autoNotifyFwFile.getResult());
                updateProgressView(2, 0);
            } else if (autoNotifyFwFile.getNotifyType() == 3) {
                this.updateState = UpdateState.fileCheckResults;
                if (50 != this.fileProgress) {
                    this.fileProgress = 50;
                }
                HostLogBack.getInstance().writeLog("WHAT_UPDATE_PROGRESS:" + this.notifyProgress + "autoNotifyFwFile.getSchedule():" + autoNotifyFwFile.getSchedule() + "aggregateProgress:" + this.aggregateProgress + "notifyProgress:" + this.notifyProgress + "fwNumber:" + this.fwNumber);
                try {
                    this.notifyProgress = ((autoNotifyFwFile.getSchedule() + this.aggregateProgress) / this.fwNumber) / 2;
                } catch (Exception e) {
                }
                this.firewareName = getSysName((byte) autoNotifyFwFile.getDevModuleId(), (byte) autoNotifyFwFile.getDevTargetId());
                updateProgressView(1, this.fileProgress + this.notifyProgress);
                this.hasAccumulate = false;
            } else if (autoNotifyFwFile.getNotifyType() == 4) {
                if (!this.hasAccumulate) {
                    fwInfo = new FwInfo();
                    fwInfo.setModelId((byte) autoNotifyFwFile.getDevModuleId());
                    fwInfo.setTypeId((byte) autoNotifyFwFile.getDevTargetId());
                    fwInfo.setSchedule(autoNotifyFwFile.getSchedule());
                    fwInfo.setSysName(getSysName(fwInfo.getModelId(), fwInfo.getTypeId()));
                    if (autoNotifyFwFile.getResult() == 0) {
                        this.updateFailure = false;
                        fwInfo.setUpdateResult("0");
                        fwInfo.setSoftwareVer(getSoftwareVer(fwInfo.getModelId(), fwInfo.getTypeId()));
                    } else {
                        fwInfo.setUpdateResult("1");
                        fwInfo.setErrorCode(getErrorCodeString(autoNotifyFwFile.getResult()));
                    }
                    HostLogBack.getInstance().writeLog("autoNotifyFwFile.getResult():" + autoNotifyFwFile.getResult() + "getErrorCodeString(autoNotifyFwFile):" + getErrorCodeString(autoNotifyFwFile.getResult()) + "aggregateProgress:" + this.aggregateProgress);
                    if (!isAdd(fwInfo)) {
                        this.aggregateProgress += 100;
                        this.fwInfos.add(fwInfo);
                    }
                    this.hasAccumulate = true;
                }
            } else if (autoNotifyFwFile.getNotifyType() == 5) {
                if (!this.hasAccumulate) {
                    fwInfo = new FwInfo();
                    fwInfo.setModelId((byte) autoNotifyFwFile.getDevModuleId());
                    fwInfo.setTypeId((byte) autoNotifyFwFile.getDevTargetId());
                    fwInfo.setSchedule(autoNotifyFwFile.getSchedule());
                    fwInfo.setSysName(getSysName(fwInfo.getModelId(), fwInfo.getTypeId()));
                    fwInfo.setUpdateResult("1");
                    if (!isAdd(fwInfo)) {
                        this.aggregateProgress += 100;
                        this.fwInfos.add(fwInfo);
                    }
                    this.hasAccumulate = true;
                    HostLogBack.getInstance().writeLog("NotifyType() == 5:" + this.aggregateProgress);
                }
            } else if (autoNotifyFwFile.getNotifyType() != 6) {
            } else {
                if (autoNotifyFwFile.getResult() == 255 || autoNotifyFwFile.getResult() == 0) {
                    updateProgressView(2, 100);
                    return;
                }
                this.updateFailure = true;
                this.firewareName = getErrorCodeString(autoNotifyFwFile.getResult());
                updateProgressView(2, 0);
            }
        } else if (msgId == 6) {
            AckUpdateCurrentProgress ackUpdateCurrentProgress = (AckUpdateCurrentProgress) packet;
            int deviceNumber = ackUpdateCurrentProgress.getDeviceNumber();
            if (deviceNumber != 0 && this.fwNumber == deviceNumber) {
                if (this.fileProgress == 0) {
                    this.fileProgress = 50;
                }
                this.currentProgressEntityList = ackUpdateCurrentProgress.getUpdateCurrentProgressEntitys();
                for (int i = 0; i < this.currentProgressEntityList.size(); i++) {
                    if ((containCamera(((UpdateCurrentProgressEntity) this.currentProgressEntityList.get(i)).getDevModuleID(), ((UpdateCurrentProgressEntity) this.currentProgressEntityList.get(i)).getDevTargetID()) || containRemoteControl()) && (this.fileProgress + this.notifyProgress == 100 || ((UpdateCurrentProgressEntity) this.currentProgressEntityList.get(this.currentProgressEntityList.size() - 1)).getSchedule() == 100)) {
                        this.handler.postDelayed(new Runnable() {
                            public void run() {
                                X8UpdatePresenter.this.updateFailure = false;
                                HostLogBack.getInstance().writeLog("Alanqiu  ===========MSG_ID_UPDATE_STATUS isCameraUpdate：" + X8UpdatePresenter.this.isCameraUpdate);
                                X8UpdatePresenter.this.updateProgressView(2, 100);
                            }
                        }, 2500);
                    }
                }
                this.hasAccumulate = true;
                HostLogBack.getInstance().writeLog("MSG_ID_UPDATE_STATUS:fwInfoList.size():" + this.fwInfoList.size() + "fwInfos.size():" + this.fwInfos.size() + "ackUpdateCurrentProgress:" + ackUpdateCurrentProgress.toString());
            }
        }
    }

    private void disposeAddedData() {
        if (this.currentProgressEntityList != null) {
            for (int i = 0; i < this.currentProgressEntityList.size(); i++) {
                FwInfo fwInfo = new FwInfo();
                fwInfo.setModelId((byte) ((UpdateCurrentProgressEntity) this.currentProgressEntityList.get(i)).getDevModuleID());
                fwInfo.setTypeId((byte) ((UpdateCurrentProgressEntity) this.currentProgressEntityList.get(i)).getDevTargetID());
                fwInfo.setSchedule(((UpdateCurrentProgressEntity) this.currentProgressEntityList.get(i)).getSchedule());
                fwInfo.setSysName(getSysName(fwInfo.getModelId(), fwInfo.getTypeId()));
                if (((UpdateCurrentProgressEntity) this.currentProgressEntityList.get(i)).getSchedule() != 100) {
                    fwInfo.setUpdateResult("1");
                    if (!isAdd(fwInfo)) {
                        this.aggregateProgress += 100;
                        this.fwInfos.add(fwInfo);
                    }
                } else if (((UpdateCurrentProgressEntity) this.currentProgressEntityList.get(i)).getResult() == 0) {
                    fwInfo.setUpdateResult("0");
                    fwInfo.setSoftwareVer(getSoftwareVer(fwInfo.getModelId(), fwInfo.getTypeId()));
                    if (!isAdd(fwInfo)) {
                        this.fwInfos.add(fwInfo);
                    }
                } else {
                    fwInfo.setUpdateResult("1");
                    if (!isAdd(fwInfo)) {
                        this.fwInfos.add(fwInfo);
                    }
                }
            }
        }
    }

    public void requestStartUpdate(UiCallBackListener callBackListener) {
        sendCmd(new FwUpdateCollection(this, callBackListener).requestStartUpdate());
    }

    public void requestUploadFile() {
        if (this.fileBytes != null) {
            byte[] byteCrc = ByteHexHelper.intToFourHexBytes(ByteArrayToIntArray.CRC32Software(this.fileBytes, this.fileBytes.length));
            sendCmd(new FwUpdateCollection().requestUploadFile(ByteHexHelper.intToFourHexBytes(this.fileBytes.length), byteCrc));
        }
    }

    public void queryCurUpdateStatus(UiCallBackListener callBackListener) {
        sendCmd(new FwUpdateCollection().queryCurUpdateStatus());
    }

    public void firmwareBuildPack(final List<FwInfo> fwInfoList) {
        this.fwInfoList = fwInfoList;
        ThreadUtils.execute(new Runnable() {
            public void run() {
                new FirmwareBuildPack(new MergFileListener() {
                    public void mergResult(int result) {
                        X8UpdatePresenter.this.fileBytes = FileUtil.getFileBytes(FirmwareBuildPack.PKG_UPDATE_FILE);
                        X8UpdatePresenter.this.residueNum = X8UpdatePresenter.this.fileBytes.length % 1024;
                        X8UpdatePresenter.this.subPackageNum = X8UpdatePresenter.this.fileBytes.length / 1024;
                        if (X8UpdatePresenter.this.residueNum == 0) {
                            X8UpdatePresenter.this.packNum = X8UpdatePresenter.this.subPackageNum;
                        } else {
                            X8UpdatePresenter.this.packNum = X8UpdatePresenter.this.subPackageNum + 1;
                        }
                        X8UpdatePresenter.this.requestUploadFile();
                        X8UpdatePresenter.this.updateState = UpdateState.requestUpdate;
                    }
                }, fwInfoList).createUpdatePkg();
            }
        });
    }

    private String getSysName(byte modelId, byte typeId) {
        String sysName = "";
        if (this.fwInfoList == null) {
            return sysName;
        }
        for (FwInfo fwInfo : this.fwInfoList) {
            if (fwInfo.getModelId() == modelId && fwInfo.getTypeId() == typeId) {
                sysName = fwInfo.getSysName();
            }
        }
        return sysName;
    }

    private short getSoftwareVer(byte modelId, byte typeId) {
        short softwareVer = (short) 0;
        if (this.fwInfoList == null) {
            return 0;
        }
        for (FwInfo fwInfo : this.fwInfoList) {
            if (fwInfo.getModelId() == modelId && fwInfo.getTypeId() == typeId) {
                softwareVer = fwInfo.getSoftwareVer();
            }
        }
        return softwareVer;
    }

    private boolean containRemoteControl() {
        boolean tempSteta = false;
        if (this.fwInfoList == null) {
            return false;
        }
        for (FwInfo fwInfo : this.fwInfoList) {
            if (fwInfo.getModelId() == (byte) 3 && fwInfo.getTypeId() == (byte) 1) {
                return true;
            }
            tempSteta = false;
        }
        return tempSteta;
    }

    private boolean containCamera(int modelID, int typeID) {
        if (modelID == 2 && typeID == 4) {
            return true;
        }
        return false;
    }

    public void setOnUpdateProgress(IX8UpdateProgressView ix8UpdateProgressView) {
        this.ix8UpdateProgressView = ix8UpdateProgressView;
    }

    public void onUpdateDateCallBack(UpdateDateMessage updateDateMessage) {
        this.callbackOffset = updateDateMessage.getFileOffset();
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.waitSend = false;
        if (this.updateState == UpdateState.updateFile) {
            HostLogBack.getInstance().writeLog("onUpdateDateCallBack:=========================" + this.callbackOffset + "total:" + this.total + "Rtp:" + updateDateMessage.getFaultMessage());
            if (this.callbackOffset == 0) {
                this.isLockOffset = true;
                this.offset = 0.0d;
            } else if (this.total == ((long) this.callbackOffset)) {
                this.fileProgress = 50;
                updateProgressView(1, this.fileProgress);
                this.updateFileEnd = true;
                this.updateThread.interrupt();
                HostLogBack.getInstance().writeLog("update uploadFwFile end fileProgress:" + this.fileProgress);
            } else if (this.callbackOffset >= 1024 && this.total != ((long) this.callbackOffset)) {
                this.isLockOffset = true;
                this.offset = (double) (this.callbackOffset - 1024);
                HostLogBack.getInstance().writeLog("update uploadFwFile offset:" + this.offset);
            }
        }
    }

    private void updateProgressView(int what, int arg1) {
        if (this.updateState != UpdateState.updateEnd) {
            Message msg = new Message();
            msg.what = what;
            if (100 < arg1) {
                arg1 = 100;
            }
            msg.arg1 = arg1;
            if (msg.arg1 >= 0) {
                this.handler.sendMessage(msg);
            }
        }
    }

    public void checkUpdateOutStatus() {
        if (this.checkUpdateTimeout == null) {
            this.checkUpdateTimeout = new Timer();
        }
        this.checkUpdateTimeout.schedule(new TimerTask() {
            public void run() {
                if (X8UpdatePresenter.this.updateState != UpdateState.updateEnd) {
                    if (X8UpdatePresenter.this.startCheckUpdateTimeOut) {
                        X8UpdatePresenter.this.queryCurUpdateStatus(null);
                        X8UpdatePresenter.this.cheackUpdateTimeOut = X8UpdatePresenter.this.cheackUpdateTimeOut + 1;
                        if (X8UpdatePresenter.this.cheackUpdateTimeOut > 960) {
                            X8UpdatePresenter.this.updateFailure = true;
                            X8UpdatePresenter.this.firewareName = X8UpdatePresenter.this.context.getString(R.string.x8_error_code_update_25);
                            X8UpdatePresenter.this.updateProgressView(2, 0);
                            X8UpdatePresenter.this.startCheckUpdateTimeOut = false;
                        }
                    }
                    if (X8UpdatePresenter.this.fileProgress + X8UpdatePresenter.this.notifyProgress != X8UpdatePresenter.this.updateTimeoutProgress) {
                        X8UpdatePresenter.this.updateTimeoutProgress = X8UpdatePresenter.this.fileProgress + X8UpdatePresenter.this.notifyProgress;
                        X8UpdatePresenter.this.updateTimeoutAddTime = 0;
                        return;
                    }
                    X8UpdatePresenter.this.updateTimeoutAddTime = X8UpdatePresenter.this.updateTimeoutAddTime + 1;
                    if (X8UpdatePresenter.this.updateTimeoutAddTime > 180) {
                        X8UpdatePresenter.this.updateFailure = true;
                        X8UpdatePresenter.this.firewareName = X8UpdatePresenter.this.context.getString(R.string.x8_error_code_update_25);
                        X8UpdatePresenter.this.updateProgressView(2, 0);
                    }
                }
            }
        }, 0, 1000);
    }

    private boolean isAdd(FwInfo fwInfo) {
        for (FwInfo info : this.fwInfos) {
            if (info.getModelId() == fwInfo.getModelId() && info.getTypeId() == fwInfo.getTypeId()) {
                this.alreadyExist = true;
            } else {
                this.alreadyExist = false;
            }
        }
        return this.alreadyExist;
    }

    public String getErrorCodeString(int result) {
        String str = "";
        switch ((byte) result) {
            case (byte) -1:
                return this.context.getString(R.string.x8_error_code_update_255);
            case (byte) 0:
                return this.context.getString(R.string.x8_error_code_update_0);
            case (byte) 1:
                return this.context.getString(R.string.x8_error_code_update_1);
            case (byte) 2:
                return this.context.getString(R.string.x8_error_code_update_2);
            case (byte) 3:
                return this.context.getString(R.string.x8_error_code_update_3);
            case (byte) 4:
                return this.context.getString(R.string.x8_error_code_update_4);
            case (byte) 5:
                return this.context.getString(R.string.x8_error_code_update_5);
            case (byte) 6:
                return this.context.getString(R.string.x8_error_code_update_6);
            case (byte) 7:
                return this.context.getString(R.string.x8_error_code_update_7);
            case (byte) 33:
                return this.context.getString(R.string.x8_error_code_update_21);
            case (byte) 34:
                return this.context.getString(R.string.x8_error_code_update_22);
            case (byte) 35:
                return this.context.getString(R.string.x8_error_code_update_23);
            case (byte) 36:
                return this.context.getString(R.string.x8_error_code_update_24);
            case (byte) 37:
                return this.context.getString(R.string.x8_error_code_update_25);
            case (byte) 38:
                return this.context.getString(R.string.x8_error_code_update_26);
            case (byte) 39:
                return this.context.getString(R.string.x8_error_code_update_27);
            case (byte) 40:
                return this.context.getString(R.string.x8_error_code_update_28);
            case (byte) 41:
                return this.context.getString(R.string.x8_error_code_update_29);
            default:
                return str;
        }
    }

    public void removeNoticeList() {
        SessionManager.getInstance().removeNoticeList(this.mIConnectResultListener);
        removeNoticeListener();
    }
}
