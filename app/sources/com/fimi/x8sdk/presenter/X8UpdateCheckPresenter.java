package com.fimi.x8sdk.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.fimi.host.HostConstants;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.R;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.command.FwUpdateCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckUpdateRequest;
import com.fimi.x8sdk.dataparser.AckUpdateSystemStatus;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.ivew.IUpdateCheckAction;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.update.UpdateUtil;
import java.util.Timer;
import java.util.TimerTask;

public class X8UpdateCheckPresenter extends BasePresenter {
    private static final int CHECK_UPDATE = 1;
    private static final int CHECK_UPDATE_ERR = 2;
    private AckUpdateRequest ackUpdateRequest;
    private AckUpdateSystemStatus ackUpdateSystemStatus;
    private Timer checkTimer = new Timer();
    private Context context;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            boolean z = true;
            if (msg.what == 2) {
                IUpdateCheckAction access$300 = X8UpdateCheckPresenter.this.iUpdateCheckAction;
                if (msg.arg1 != 0) {
                    z = false;
                }
                access$300.showIsUpdate(z, msg.arg1);
            } else if (msg.what == 1) {
                X8UpdateCheckPresenter.this.iUpdateCheckAction.checkUpdate();
            }
            super.handleMessage(msg);
        }
    };
    private boolean haveLockMotor = false;
    private IUpdateCheckAction iUpdateCheckAction;
    private UpdateCheckState updateCheckState = UpdateCheckState.updateInit;
    private byte[] updateStates = new byte[]{(byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12, (byte) 13};

    private enum UpdateCheckState {
        updateInit,
        readyUpgrade,
        updating,
        upgradeEnd,
        notUpgrade
    }

    public X8UpdateCheckPresenter() {
        addNoticeListener();
        intCheckUpdateStatus();
    }

    public void setIUpdateCheckAction(Context context, IUpdateCheckAction iUpdateCheckAction) {
        this.context = context;
        this.iUpdateCheckAction = iUpdateCheckAction;
    }

    public void intCheckUpdateStatus() {
        if (this.checkTimer == null) {
            this.checkTimer = new Timer();
        }
        this.checkTimer.schedule(new TimerTask() {
            public void run() {
                if (X8UpdateCheckPresenter.this.updateCheckState != UpdateCheckState.updating) {
                    X8UpdateCheckPresenter.this.queryCurSystemStatus();
                    X8UpdateCheckPresenter.this.checkUpdateVersion();
                }
            }
        }, 0, 2000);
    }

    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    /* Access modifiers changed, original: protected */
    public void reponseCmd(boolean isAck, int group, int msgId, ILinkMessage packet, BaseCommand bcd) {
        super.reponseCmd(isAck, group, msgId, packet, bcd);
        if (group != 16) {
            return;
        }
        if (msgId == 5) {
            if (this.updateCheckState == UpdateCheckState.updateInit) {
                this.ackUpdateSystemStatus = (AckUpdateSystemStatus) packet;
                checkCameraState();
            }
        } else if (msgId == 2) {
            this.ackUpdateRequest = (AckUpdateRequest) packet;
            if (this.ackUpdateRequest == null) {
                return;
            }
            if (SPStoreManager.getInstance().getInt(HostConstants.SP_KEY_UPDATE_CHECK, 2) == 2 || UpdateUtil.isForceUpdate()) {
                this.handler.sendEmptyMessage(1);
            }
        }
    }

    private void checkCameraState() {
        int status = this.ackUpdateSystemStatus.getStatus();
        if (status == 0) {
            if (this.haveLockMotor) {
                setPresenterLockMotor(0);
            }
            this.updateCheckState = UpdateCheckState.readyUpgrade;
            requestStartUpdate(null);
        } else if (checkUpdatingState(status)) {
            if (!this.haveLockMotor) {
                setPresenterLockMotor(1);
            }
            this.updateCheckState = UpdateCheckState.updating;
        } else {
            this.updateCheckState = UpdateCheckState.upgradeEnd;
        }
    }

    private void checkUpdateVersion() {
        AutoCameraStateADV stateADV = StateManager.getInstance().getCamera().getAutoCameraStateADV();
        int resId = 0;
        if (!StateManager.getInstance().getX8Drone().isConnect()) {
            resId = R.string.x8_update_err_connect;
        } else if (StateManager.getInstance().getX8Drone().isInSky()) {
            resId = R.string.x8_update_err_insky;
        } else if (StateManager.getInstance().getCamera().getToken() <= 0) {
            resId = R.string.x8_update_err_a12ununited;
        } else if (this.updateCheckState == UpdateCheckState.updating) {
            resId = R.string.x8_update_err_updating;
            this.updateCheckState = UpdateCheckState.upgradeEnd;
        } else if (stateADV != null && stateADV.getInfo() == 3) {
            resId = R.string.x8_error_code_update_3;
        } else if (!(this.ackUpdateRequest == null || this.ackUpdateRequest.isResultSucceed())) {
            resId = UpdateUtil.getErrorCodeString(this.context, this.ackUpdateRequest.getMsgRpt());
        }
        Message msg = new Message();
        msg.what = 2;
        msg.arg1 = resId;
        this.handler.sendMessage(msg);
    }

    public void requestStartUpdate(UiCallBackListener callBackListener) {
        sendCmd(new FwUpdateCollection(this, callBackListener).requestStartUpdate());
    }

    public void queryCurSystemStatus() {
        this.updateCheckState = UpdateCheckState.updateInit;
        sendCmd(new FwUpdateCollection().queryCurSystemStatus());
    }

    public void setPresenterLockMotor(final int lock) {
        sendCmd(new FcCollection(this, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess()) {
                    return;
                }
                if (lock == 0) {
                    X8UpdateCheckPresenter.this.haveLockMotor = false;
                } else {
                    X8UpdateCheckPresenter.this.haveLockMotor = true;
                }
            }
        }).setLockMotor(lock));
    }

    private boolean checkUpdatingState(int updateState) {
        boolean isUpdateState = false;
        for (byte b : this.updateStates) {
            if (updateState == b) {
                return true;
            }
            isUpdateState = false;
        }
        return isUpdateState;
    }

    public void removeNoticeList() {
    }
}
