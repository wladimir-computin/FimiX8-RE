package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.entity.X8RestSystemParamResult;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.controller.AllSettingManager;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.X8GimbalManager;

public class X8RestSystemController implements Callback {
    FcCtrlManager fcCtrlManager;
    X8GimbalManager gimbalManager;
    Context mContext;
    Handler mHandler = new Handler(this);
    OnRestSystemListener onRestSystemListener;
    X8RestSystemParamResult restSystemParamResult;
    X8DoubleCustomDialog x8DoubleCustomDialog;

    public interface OnRestSystemListener {
        void onFinish();

        void onStart();
    }

    public X8RestSystemController(Context mContext, X8GimbalManager gimbalManager, FcCtrlManager fcCtrlManager, OnRestSystemListener restSystemListener) {
        this.fcCtrlManager = fcCtrlManager;
        this.restSystemParamResult = new X8RestSystemParamResult();
        this.onRestSystemListener = restSystemListener;
        this.mContext = mContext;
        this.gimbalManager = gimbalManager;
    }

    public void showRestParamDialog() {
        if (this.x8DoubleCustomDialog == null) {
            this.x8DoubleCustomDialog = new X8DoubleCustomDialog(this.mContext, this.mContext.getString(R.string.x8_general_version_rest_paramters), this.mContext.getString(R.string.x8_general_rest_paramters_content), this.mContext.getString(R.string.x8_general_rest), new onDialogButtonClickListener() {
                public void onLeft() {
                }

                public void onRight() {
                    X8RestSystemController.this.resetALLSystem();
                }
            });
        }
        this.x8DoubleCustomDialog.show();
    }

    private void resetALLSystem() {
        this.restSystemParamResult.init();
        this.onRestSystemListener.onStart();
        resetAppParams();
        restRCSystemParams();
        restFcSystemParams();
        resetGimablSystem();
        this.mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    private void restRCSystemParams() {
        this.fcCtrlManager.setRcCtrlMode(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8RestSystemController.this.restSystemParamResult.setRcResult(true);
                }
            }
        }, (byte) 1);
    }

    private void resetAppParams() {
        GlobalConfig.getInstance().setLowReturn(true);
        GlobalConfig.getInstance().setLowLanding(true);
        restFiveKey();
        SPStoreManager.getInstance().saveInt(Constants.X8_MAP_STYLE, Constants.X8_GENERAL_MAP_STYLE_NORMAL);
        GlobalConfig.getInstance().setMapStyle(Constants.X8_GENERAL_MAP_STYLE_NORMAL);
        SPStoreManager.getInstance().saveBoolean(Constants.X8_UNITY_OPTION, true);
        GlobalConfig.getInstance().setShowmMtric(true);
        this.restSystemParamResult.setAppResult(true);
    }

    private void restFiveKey() {
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_UP_KEY, 0);
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_DOWN_KEY, 1);
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_LEFT_KEY, 2);
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_RIGHT_KEY, 3);
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_CENTRE_KEY, 4);
    }

    private void resetGimablSystem() {
        this.gimbalManager.resetGCParams(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8RestSystemController.this.restSystemParamResult.setGcResult(true);
                }
            }
        });
    }

    private void restFcSystemParams() {
        this.fcCtrlManager.restSystemParams(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8RestSystemController.this.restSystemParamResult.setFcResult(true);
                }
            }
        });
    }

    public boolean handleMessage(Message message) {
        if (this.restSystemParamResult.isAppResult() && this.restSystemParamResult.isFcResult() && this.restSystemParamResult.isRcResult()) {
            X8ToastUtil.showToast(this.mContext, this.mContext.getString(R.string.x8_general_rest_paramters_success), 0);
        } else {
            X8ToastUtil.showToast(this.mContext, this.mContext.getString(R.string.x8_general_rest_paramters_fail), 0);
        }
        AllSettingManager.getInstance().getAllSetting();
        this.onRestSystemListener.onFinish();
        return false;
    }
}
