package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ProgressBar;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8GimbalSettingListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8ValueSeakBarView;
import com.fimi.app.x8s.widget.X8ValueSeakBarView.OnProgressConfirmListener;
import com.fimi.app.x8s.widget.X8ValueSeakBarWithTip;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.dataparser.AckGetGimbalGain;
import com.fimi.x8sdk.dataparser.AckGetPitchSpeed;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8GimbalItemController extends AbsX8Controllers implements OnClickListener {
    private Button btnGimbalCalibration;
    private Button btnHorizontalTrim;
    Button btnRestParams;
    private FcCtrlManager fcCtrlManager;
    private X8GimbalManager gimbalManager;
    private boolean isConnected = false;
    private boolean isRequested = false;
    private IX8GimbalSettingListener listener;
    private Context mContext;
    ProgressBar pbRestsystemLoading;
    private View rlFcItem;
    private X8ValueSeakBarView sbPitchSpeed;
    private ViewStub stubFcItem;
    X8ValueSeakBarWithTip vsbGimbalGain;
    X8DoubleCustomDialog x8DoubleCustomDialog;

    public X8GimbalItemController(View rootView) {
        super(rootView);
        this.mContext = rootView.getContext();
    }

    public void initViews(View rootView) {
        this.stubFcItem = (ViewStub) rootView.findViewById(R.id.stub_gimbal_item);
    }

    public void initActions() {
        if (this.gimbalManager != null && this.sbPitchSpeed != null) {
            this.sbPitchSpeed.setConfirmListener(new OnProgressConfirmListener() {
                public void onConfirm(float value) {
                    if (X8GimbalItemController.this.gimbalManager != null && StateManager.getInstance().getX8Drone().isConnect()) {
                        X8GimbalItemController.this.gimbalManager.setPitchSpeed((int) X8GimbalItemController.this.sbPitchSpeed.getCurrentValue(), new UiCallBackListener() {
                            public void onComplete(CmdResult cmdResult, Object o) {
                                if (o != null) {
                                    ILinkMessage packet = (ILinkMessage) o;
                                    if (packet != null && packet.getMsgRpt() != 16) {
                                        X8GimbalItemController.this.sbPitchSpeed.setImbConfirmEnable(false);
                                    }
                                }
                            }
                        });
                    }
                }
            });
            this.vsbGimbalGain.setConfirmListener(new X8ValueSeakBarWithTip.OnProgressConfirmListener() {
                public void onConfirm(float value) {
                    if (StateManager.getInstance().getX8Drone().isConnect()) {
                        X8GimbalItemController.this.gimbalManager.setGcGain((int) X8GimbalItemController.this.vsbGimbalGain.getCurrentValue(), new UiCallBackListener() {
                            public void onComplete(CmdResult cmdResult, Object o) {
                                if (cmdResult.isSuccess()) {
                                    X8GimbalItemController.this.vsbGimbalGain.setImbConfirmEnable(false);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public void defaultVal() {
    }

    public void onDroneConnected(boolean b) {
        if (this.isConnected != b) {
            this.isConnected = b;
            if (this.isShow && this.rlFcItem != null) {
                setViewEnabled(b);
                if (b && !this.isRequested) {
                    requestValue();
                    this.isRequested = true;
                }
            }
        }
    }

    public void showItem() {
        if (this.rlFcItem == null) {
            View view = this.stubFcItem.inflate();
            this.rlFcItem = view.findViewById(R.id.x8_rl_main_gimbal_item);
            this.btnGimbalCalibration = (Button) view.findViewById(R.id.btn_gimbal_calibration);
            this.btnHorizontalTrim = (Button) view.findViewById(R.id.btn_horizontal_trim);
            this.sbPitchSpeed = (X8ValueSeakBarView) view.findViewById(R.id.vsb_pitching_speed_limit);
            this.vsbGimbalGain = (X8ValueSeakBarWithTip) view.findViewById(R.id.vsb_gimbal_gain);
            this.btnRestParams = (Button) view.findViewById(R.id.btn_rest_params);
            this.pbRestsystemLoading = (ProgressBar) view.findViewById(R.id.pb_restsystem_loading);
            this.btnRestParams.setOnClickListener(this);
            this.btnGimbalCalibration.setOnClickListener(this);
            this.btnHorizontalTrim.setOnClickListener(this);
            requestValue();
            initActions();
        }
        this.rlFcItem.setVisibility(0);
        this.isShow = true;
    }

    private void requestValue() {
        boolean isConnect = StateManager.getInstance().getX8Drone().isConnect();
        setViewEnabled(isConnect);
        if (isConnect && this.gimbalManager != null) {
            this.gimbalManager.getPitchSpeed(new UiCallBackListener<AckGetPitchSpeed>() {
                public void onComplete(CmdResult cmdResult, AckGetPitchSpeed obj) {
                    if (cmdResult.isSuccess()) {
                        X8GimbalItemController.this.sbPitchSpeed.setProgress(obj.getSpeed());
                        X8GimbalItemController.this.sbPitchSpeed.setImbConfirmEnable(false);
                    }
                }
            });
            this.gimbalManager.getGcGain(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                        X8GimbalItemController.this.vsbGimbalGain.setProgress(((AckGetGimbalGain) o).getData());
                        X8GimbalItemController.this.vsbGimbalGain.setImbConfirmEnable(false);
                    }
                }
            });
        }
    }

    public void setViewEnabled(boolean isEnabled) {
        if (this.rlFcItem != null) {
            float f;
            this.btnHorizontalTrim.setEnabled(isEnabled);
            this.sbPitchSpeed.setViewEnable(isEnabled);
            this.vsbGimbalGain.setViewEnable(isEnabled);
            boolean isOngroud = StateManager.getInstance().getX8Drone().isOnGround();
            Button button = this.btnRestParams;
            boolean z = isOngroud && isEnabled;
            button.setEnabled(z);
            button = this.btnRestParams;
            if (isOngroud && isEnabled) {
                f = 1.0f;
            } else {
                f = 0.4f;
            }
            button.setAlpha(f);
            if (isEnabled) {
                this.btnHorizontalTrim.setAlpha(1.0f);
            } else {
                this.btnHorizontalTrim.setAlpha(0.4f);
            }
        }
    }

    public void closeItem() {
        if (this.rlFcItem != null) {
            this.rlFcItem.setVisibility(8);
            this.isShow = false;
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_gimbal_calibration && this.listener != null) {
            this.listener.onGimbalCalibrationClick();
        }
        if (i == R.id.btn_horizontal_trim && this.listener != null) {
            this.listener.onHorizontalTrimClick();
        }
        if (i == R.id.btn_rest_params) {
            showRestParamDialog();
        }
    }

    public void setListener(IX8GimbalSettingListener listener) {
        this.listener = listener;
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setGimbalManager(X8GimbalManager gimbalManager) {
        this.gimbalManager = gimbalManager;
    }

    public void showRestParamDialog() {
        if (this.x8DoubleCustomDialog == null) {
            this.x8DoubleCustomDialog = new X8DoubleCustomDialog(this.mContext, this.mContext.getString(R.string.x8_gimbal_setting_gimbal_reset_params), this.mContext.getString(R.string.x8_gimbale_settting_rest_params_content), this.mContext.getString(R.string.x8_general_rest), new onDialogButtonClickListener() {
                public void onLeft() {
                }

                public void onRight() {
                    X8GimbalItemController.this.resetGimbalSystemParams();
                }
            });
        }
        this.x8DoubleCustomDialog.show();
    }

    private void resetGimbalSystemParams() {
        this.gimbalManager.resetGCParams(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8ToastUtil.showToast(X8GimbalItemController.this.mContext, X8GimbalItemController.this.getString(R.string.x8_gimbale_settting_rest_params_result_success), 0);
                } else {
                    X8ToastUtil.showToast(X8GimbalItemController.this.mContext, X8GimbalItemController.this.getString(R.string.x8_gimbale_settting_rest_params_result_failed), 0);
                }
                X8GimbalItemController.this.requestValue();
            }
        });
    }

    public boolean onClickBackKey() {
        return false;
    }
}
