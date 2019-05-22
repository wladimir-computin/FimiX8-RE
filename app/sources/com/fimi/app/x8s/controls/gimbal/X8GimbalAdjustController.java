package com.fimi.app.x8s.controls.gimbal;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.IX8MainTopBarListener;
import com.fimi.app.x8s.widget.GimbalAdjustRelayout;
import com.fimi.kernel.Constants;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.dataparser.AckCloudParams;

public class X8GimbalAdjustController {
    Button btnCalibrate;
    Button btnGet;
    Button btnSave;
    IX8MainTopBarListener listener;
    Context mContext;
    X8GimbalManager mX8GimbalManager = new X8GimbalManager();
    GimbalAdjustRelayout relayoutPitch;
    GimbalAdjustRelayout relayoutRoll;
    GimbalAdjustRelayout rlYaw;

    public X8GimbalAdjustController(View rootView) {
        this.btnGet = (Button) rootView.findViewById(R.id.btn_get);
        this.btnSave = (Button) rootView.findViewById(R.id.btn_save);
        this.relayoutPitch = (GimbalAdjustRelayout) rootView.findViewById(R.id.rl_pitch);
        this.relayoutPitch.getTvGimbalModel().setText("Pitch");
        this.relayoutRoll = (GimbalAdjustRelayout) rootView.findViewById(R.id.rl_roll);
        this.relayoutRoll.getTvGimbalModel().setText("Roll");
        this.rlYaw = (GimbalAdjustRelayout) rootView.findViewById(R.id.rl_yaw);
        this.rlYaw.getTvGimbalModel().setText("Yaw");
        this.mContext = rootView.getContext();
        this.btnCalibrate = (Button) rootView.findViewById(R.id.btn_calibrate);
        if (!Constants.isFactoryApp()) {
            rootView.findViewById(R.id.rl_gc_calibrate).setVisibility(8);
        }
        initClickAction();
    }

    public void setListener(IX8MainTopBarListener listener) {
        this.listener = listener;
    }

    private void initClickAction() {
        this.btnCalibrate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (X8GimbalAdjustController.this.listener != null) {
                    X8GimbalAdjustController.this.listener.onGcClick();
                }
            }
        });
        this.btnSave.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8GimbalAdjustController.this.mX8GimbalManager.setGcParams(1, 0.0f, new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8ToastUtil.showToast(X8GimbalAdjustController.this.mContext, "保存云台参数成功", 0);
                        } else {
                            X8ToastUtil.showToast(X8GimbalAdjustController.this.mContext, "保存云台参数失败", 0);
                        }
                    }
                });
            }
        });
        this.btnGet.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8GimbalAdjustController.this.mX8GimbalManager.getGcParams(new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            AckCloudParams params = (AckCloudParams) o;
                            if (params != null) {
                                X8GimbalAdjustController.this.relayoutPitch.getEtxValue().setText(NumberUtil.decimalPointStr(params.getParam1(), 4));
                                X8GimbalAdjustController.this.relayoutRoll.getEtxValue().setText(NumberUtil.decimalPointStr(params.getParam2(), 4));
                                X8GimbalAdjustController.this.rlYaw.getEtxValue().setText(NumberUtil.decimalPointStr(params.getParam3(), 4));
                                return;
                            }
                            X8ToastUtil.showToast(X8GimbalAdjustController.this.mContext, "获取云台参数失败", 0);
                        }
                    }
                });
            }
        });
        this.relayoutPitch.getBtnAdd().setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final float value = (float) (((double) Float.valueOf(X8GimbalAdjustController.this.relayoutPitch.getEtxValue().getText().toString()).floatValue()) + 0.004d);
                X8GimbalAdjustController.this.mX8GimbalManager.setGcParams(2, value, new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8GimbalAdjustController.this.relayoutPitch.getEtxValue().setText(NumberUtil.decimalPointStr((double) value, 4));
                        } else {
                            X8ToastUtil.showToast(X8GimbalAdjustController.this.mContext, "设置云台参数失败", 0);
                        }
                    }
                });
            }
        });
        this.relayoutPitch.getBtnSub().setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final float value = (float) (((double) Float.valueOf(X8GimbalAdjustController.this.relayoutPitch.getEtxValue().getText().toString()).floatValue()) - 0.004d);
                X8GimbalAdjustController.this.mX8GimbalManager.setGcParams(2, value, new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8GimbalAdjustController.this.relayoutPitch.getEtxValue().setText(NumberUtil.decimalPointStr((double) value, 4));
                        } else {
                            X8ToastUtil.showToast(X8GimbalAdjustController.this.mContext, "设置云台参数失败", 0);
                        }
                    }
                });
            }
        });
        this.relayoutRoll.getBtnAdd().setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final float value = (float) (((double) Float.valueOf(X8GimbalAdjustController.this.relayoutRoll.getEtxValue().getText().toString()).floatValue()) + 0.004d);
                X8GimbalAdjustController.this.mX8GimbalManager.setGcParams(4, value, new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8GimbalAdjustController.this.relayoutRoll.getEtxValue().setText(NumberUtil.decimalPointStr((double) value, 4));
                        } else {
                            X8ToastUtil.showToast(X8GimbalAdjustController.this.mContext, "设置云台参数失败", 0);
                        }
                    }
                });
            }
        });
        this.relayoutRoll.getBtnSub().setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final float value = (float) (((double) Float.valueOf(X8GimbalAdjustController.this.relayoutRoll.getEtxValue().getText().toString()).floatValue()) - 0.004d);
                X8GimbalAdjustController.this.mX8GimbalManager.setGcParams(4, value, new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8GimbalAdjustController.this.relayoutRoll.getEtxValue().setText(NumberUtil.decimalPointStr((double) value, 4));
                        } else {
                            X8ToastUtil.showToast(X8GimbalAdjustController.this.mContext, "设置云台参数失败", 0);
                        }
                    }
                });
            }
        });
        this.rlYaw.getBtnAdd().setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final float value = (float) (((double) Float.valueOf(X8GimbalAdjustController.this.rlYaw.getEtxValue().getText().toString()).floatValue()) - 0.004d);
                X8GimbalAdjustController.this.mX8GimbalManager.setGcParams(8, value, new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8GimbalAdjustController.this.rlYaw.getEtxValue().setText(NumberUtil.decimalPointStr((double) value, 4));
                        } else {
                            X8ToastUtil.showToast(X8GimbalAdjustController.this.mContext, "设置云台参数失败", 0);
                        }
                    }
                });
            }
        });
        this.rlYaw.getBtnSub().setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final float value = (float) (((double) Float.valueOf(X8GimbalAdjustController.this.rlYaw.getEtxValue().getText().toString()).floatValue()) + 0.004d);
                X8GimbalAdjustController.this.mX8GimbalManager.setGcParams(8, value, new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8GimbalAdjustController.this.rlYaw.getEtxValue().setText(NumberUtil.decimalPointStr((double) value, 4));
                        } else {
                            X8ToastUtil.showToast(X8GimbalAdjustController.this.mContext, "设置云台参数失败", 0);
                        }
                    }
                });
            }
        });
    }

    public void setmX8GimbalManager(X8GimbalManager mX8GimbalManager) {
        this.mX8GimbalManager = mX8GimbalManager;
    }
}
