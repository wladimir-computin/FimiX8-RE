package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckCheckIMUException;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8IMUCheckController {
    private X8DoubleCustomDialog checkIMUDialog;
    AckCheckIMUException checkIMUException1;
    OnCheckIMULisenter checkIMULisenter;
    AckCheckIMUException checkIMUMxception2;
    FcCtrlManager fcCtrlManager;
    Context mContext;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    X8IMUCheckController x8IMUCheckController = X8IMUCheckController.this;
                    x8IMUCheckController.reqestCount++;
                    if (!StateManager.getInstance().getX8Drone().isConnect()) {
                        X8IMUCheckController.this.checkIMULisenter.checkFinish(0);
                        X8IMUCheckController.this.removeCheckIMUMessage();
                        return;
                    } else if (X8IMUCheckController.this.reqestCount >= 65) {
                        X8IMUCheckController.this.checkIMULisenter.checkFinish(0);
                        X8IMUCheckController.this.removeCheckIMUMessage();
                        return;
                    } else {
                        X8IMUCheckController.this.checkIMULisenter.checkProgress();
                        X8IMUCheckController.this.getCheckIMUResult();
                        return;
                    }
                default:
                    return;
            }
        }
    };
    volatile int reqestCount = 0;

    public interface OnCheckIMULisenter {
        void checkFinish(int i);

        void checkProgress();

        void startCheck();
    }

    public X8IMUCheckController(Context mContext, FcCtrlManager fcCtrlManager, OnCheckIMULisenter checkIMULisenter) {
        this.mContext = mContext;
        this.fcCtrlManager = fcCtrlManager;
        this.checkIMULisenter = checkIMULisenter;
    }

    private void startCheckIMUStatus() {
        this.reqestCount = 0;
        this.fcCtrlManager.openCheckIMU(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
            }
        });
        this.checkIMULisenter.startCheck();
        this.mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    public void showImuDialog() {
        if (this.checkIMUDialog == null) {
            this.checkIMUDialog = new X8DoubleCustomDialog(this.mContext, this.mContext.getString(R.string.x8_fc_item_imu_check), this.mContext.getString(R.string.x8_fc_item_imu_dialog), new onDialogButtonClickListener() {
                public void onLeft() {
                }

                public void onRight() {
                    X8IMUCheckController.this.startCheckIMUStatus();
                }
            });
        }
        this.checkIMUDialog.show();
    }

    private void getCheckIMUResult() {
        if (this.checkIMUException1 == null || this.checkIMUException1.getSensorMaintainSta() != 4) {
            this.fcCtrlManager.checkIMUException(1, new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    X8IMUCheckController.this.checkIMUException1 = (AckCheckIMUException) o;
                }
            });
        }
        if (this.checkIMUMxception2 == null || this.checkIMUMxception2.getSensorMaintainSta() != 4) {
            this.fcCtrlManager.checkIMUException(2, new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    X8IMUCheckController.this.checkIMUMxception2 = (AckCheckIMUException) o;
                }
            });
        }
        if (this.checkIMUException1 == null || this.checkIMUException1.getSensorMaintainSta() != 4 || this.checkIMUMxception2 == null || this.checkIMUMxception2.getSensorMaintainSta() != 4) {
            this.mHandler.sendEmptyMessageDelayed(0, 1000);
            return;
        }
        if (this.checkIMUException1.getErrCode() == 0 && this.checkIMUMxception2.getErrCode() == 0) {
            this.checkIMULisenter.checkFinish(1);
        } else {
            this.checkIMULisenter.checkFinish(2);
        }
        this.checkIMUMxception2 = null;
        this.checkIMUException1 = null;
        removeCheckIMUMessage();
    }

    public void stopCheckIMUChck() {
        removeCheckIMUMessage();
    }

    private void removeCheckIMUMessage() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
        }
    }
}
