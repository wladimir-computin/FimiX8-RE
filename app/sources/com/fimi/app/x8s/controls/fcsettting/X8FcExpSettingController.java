package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.app.x8s.widget.X8CustomChartView;
import com.fimi.app.x8s.widget.X8CustomChartView.OnSeekChangedListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8FixedEditText;
import com.fimi.app.x8s.widget.X8FixedEditText.OnInputChangedListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetSensitivity;
import com.fimi.x8sdk.entity.X8AppSettingLog;

public class X8FcExpSettingController extends AbsX8MenuBoxControllers implements OnClickListener, OnInputChangedListener, OnSeekChangedListener {
    private final int DEFAULT_VALUE = 50;
    private ImageButton btnReset;
    private PercentRelativeLayout content_layout;
    private X8CustomChartView cvToGoBack;
    private X8CustomChartView cvToLeftRight;
    private X8CustomChartView cvToUpDown;
    private X8FixedEditText edtToGoBack;
    private X8FixedEditText edtToLeftRight;
    private X8FixedEditText edtToUpDown;
    private FcCtrlManager fcCtrlManager;
    private ImageView imgReturn;
    private boolean isRequested = false;
    private IX8CalibrationListener listener;
    private Context mContext;
    private X8DoubleCustomDialog resetDialog;

    public X8FcExpSettingController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.contentView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.x8_main_fc_exp_setting, (ViewGroup) rootView, true);
        this.content_layout = (PercentRelativeLayout) this.contentView.findViewById(R.id.exp_content_layout);
        this.imgReturn = (ImageView) this.contentView.findViewById(R.id.img_return);
        this.btnReset = (ImageButton) this.contentView.findViewById(R.id.btn_reset);
        this.btnReset.setOnClickListener(this);
        this.edtToUpDown = (X8FixedEditText) this.contentView.findViewById(R.id.edt_to_up_down);
        this.edtToLeftRight = (X8FixedEditText) this.contentView.findViewById(R.id.edt_to_left_right);
        this.edtToGoBack = (X8FixedEditText) this.contentView.findViewById(R.id.edt_to_go_back);
        this.edtToUpDown.setFixedText("%");
        this.edtToLeftRight.setFixedText("%");
        this.edtToGoBack.setFixedText("%");
        this.edtToUpDown.setOnInputChangedListener(this);
        this.edtToLeftRight.setOnInputChangedListener(this);
        this.edtToGoBack.setOnInputChangedListener(this);
        this.edtToUpDown.setEnabled(false);
        this.edtToLeftRight.setEnabled(false);
        this.edtToGoBack.setEnabled(false);
        this.cvToUpDown = (X8CustomChartView) this.contentView.findViewById(R.id.cv_to_up_down);
        this.cvToLeftRight = (X8CustomChartView) this.contentView.findViewById(R.id.cv_to_left_right);
        this.cvToGoBack = (X8CustomChartView) this.contentView.findViewById(R.id.cv_to_go_back);
        this.cvToUpDown.setOnSeekChangeListener(this);
        this.cvToLeftRight.setOnSeekChangeListener(this);
        this.cvToGoBack.setOnSeekChangeListener(this);
        this.mContext = this.contentView.getContext();
        initActions();
    }

    public void initActions() {
        if (this.contentView != null) {
            this.imgReturn.setOnClickListener(this);
        }
    }

    public void onDroneConnected(boolean b) {
        if (this.isShow && this.isShow) {
            if (b && !this.isRequested) {
                this.isRequested = true;
                requestDefaultValue();
            }
            if (b) {
                this.btnReset.setEnabled(true);
                this.btnReset.setAlpha(1.0f);
            } else {
                this.btnReset.setEnabled(false);
                this.btnReset.setAlpha(0.6f);
            }
            updateViewEnable(b, this.content_layout);
            if (this.cvToGoBack != null) {
                if (b) {
                    this.cvToGoBack.setAlpha(1.0f);
                    this.cvToGoBack.setEnable(true);
                } else {
                    this.cvToGoBack.setAlpha(0.6f);
                    this.cvToGoBack.setEnable(false);
                }
            }
            if (this.cvToLeftRight != null) {
                if (b) {
                    this.cvToLeftRight.setAlpha(1.0f);
                    this.cvToLeftRight.setEnable(true);
                } else {
                    this.cvToLeftRight.setAlpha(0.6f);
                    this.cvToLeftRight.setEnable(false);
                }
            }
            if (this.cvToUpDown == null) {
                return;
            }
            if (b) {
                this.cvToUpDown.setAlpha(1.0f);
                this.cvToUpDown.setEnable(true);
                return;
            }
            this.cvToUpDown.setAlpha(0.6f);
            this.cvToUpDown.setEnable(false);
        }
    }

    private void requestDefaultValue() {
        if (this.fcCtrlManager != null) {
            this.fcCtrlManager.getRockerExp(new UiCallBackListener<AckGetSensitivity>() {
                public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
                    if (cmdResult.isSuccess()) {
                        X8FcExpSettingController.this.cvToUpDown.setCurValue((double) sensitivity.getThroPercent());
                        X8FcExpSettingController.this.cvToUpDown.refreshView(false);
                        X8FcExpSettingController.this.edtToUpDown.setText("" + sensitivity.getThroPercent());
                        X8FcExpSettingController.this.cvToLeftRight.setCurValue((double) sensitivity.getYawPercent());
                        X8FcExpSettingController.this.cvToLeftRight.refreshView(false);
                        X8FcExpSettingController.this.edtToLeftRight.setText("" + sensitivity.getYawPercent());
                        X8FcExpSettingController.this.cvToGoBack.setCurValue((double) sensitivity.getRollPercent());
                        X8FcExpSettingController.this.cvToGoBack.refreshView(false);
                        X8FcExpSettingController.this.edtToGoBack.setText("" + sensitivity.getRollPercent());
                    }
                }
            });
        }
    }

    public void defaultVal() {
        updateViewEnable(false, this.content_layout);
        this.btnReset.setAlpha(0.6f);
        this.btnReset.setEnabled(false);
    }

    public void showItem() {
        this.isShow = true;
        this.contentView.setVisibility(0);
        getDroneState();
        if (this.isConect) {
            updateViewEnable(true, this.content_layout);
            this.btnReset.setAlpha(1.0f);
            this.btnReset.setEnabled(true);
            return;
        }
        defaultVal();
    }

    public void closeItem() {
        this.isShow = false;
        this.contentView.setVisibility(8);
        defaultVal();
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setCalibrationListener(IX8CalibrationListener listener) {
        this.listener = listener;
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_return) {
            closeItem();
            if (this.listener != null) {
                this.listener.onCalibrationReturn();
            }
        } else if (i == R.id.btn_reset) {
            if (this.resetDialog == null) {
                this.resetDialog = new X8DoubleCustomDialog(this.mContext, this.mContext.getString(R.string.x8_fc_sensitivity_reset_title), this.mContext.getString(R.string.x8_fc_sensitivity_reset_content), new onDialogButtonClickListener() {
                    public void onLeft() {
                    }

                    public void onRight() {
                        X8FcExpSettingController.this.fcCtrlManager.setUpDownRockerExp(new UiCallBackListener<Object>() {
                            public void onComplete(CmdResult cmdResult, Object resp) {
                                if (cmdResult.isSuccess()) {
                                    X8FcExpSettingController.this.cvToUpDown.setCurValue(50.0d);
                                    X8FcExpSettingController.this.cvToUpDown.refreshView(false);
                                    X8FcExpSettingController.this.edtToUpDown.setText("50");
                                }
                            }
                        }, 50);
                        X8FcExpSettingController.this.fcCtrlManager.setLeftRightRockerExp(new UiCallBackListener() {
                            public void onComplete(CmdResult cmdResult, Object o) {
                                if (cmdResult.isSuccess()) {
                                    X8FcExpSettingController.this.cvToLeftRight.setCurValue(50.0d);
                                    X8FcExpSettingController.this.cvToLeftRight.refreshView(false);
                                    X8FcExpSettingController.this.edtToLeftRight.setText("50");
                                }
                            }
                        }, 50);
                        X8FcExpSettingController.this.fcCtrlManager.setGoBackRockerExp(new UiCallBackListener() {
                            public void onComplete(CmdResult cmdResult, Object o) {
                                if (cmdResult.isSuccess()) {
                                    X8FcExpSettingController.this.cvToGoBack.setCurValue(50.0d);
                                    X8FcExpSettingController.this.cvToGoBack.refreshView(false);
                                    X8FcExpSettingController.this.edtToGoBack.setText("50");
                                    X8AppSettingLog.setExp(50, 50, 50, 50);
                                }
                            }
                        }, 50, 50);
                    }
                });
            }
            this.resetDialog.show();
        }
    }

    public void onSeekChanged(int viewId, double value) {
        if (viewId == R.id.cv_to_up_down) {
            this.edtToUpDown.setText("" + ((int) value));
        } else if (viewId == R.id.cv_to_left_right) {
            this.edtToLeftRight.setText("" + ((int) value));
        } else if (viewId == R.id.cv_to_go_back) {
            this.edtToGoBack.setText("" + ((int) value));
        }
    }

    public void onFingerUp(int viewId, final double value) {
        if (viewId == R.id.cv_to_up_down) {
            this.fcCtrlManager.setUpDownRockerExp(new UiCallBackListener<Object>() {
                public void onComplete(CmdResult cmdResult, Object resp) {
                    if (cmdResult.isSuccess()) {
                        X8AppSettingLog.setExp(-1, -1, (int) value, -1);
                    }
                }
            }, (int) value);
        } else if (viewId == R.id.cv_to_left_right) {
            this.fcCtrlManager.setLeftRightRockerExp(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    X8AppSettingLog.setExp(-1, -1, -1, (int) value);
                }
            }, (int) value);
        } else if (viewId == R.id.cv_to_go_back) {
            this.fcCtrlManager.setGoBackRockerExp(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    X8AppSettingLog.setExp((int) value, (int) value, -1, -1);
                }
            }, (int) value, (int) value);
        }
    }

    public void onInputChanged(int viewId, int value) {
        if (10 <= value && value <= 100) {
            if (viewId == R.id.edt_to_up_down) {
                this.cvToUpDown.setCurValue((double) value);
                this.cvToUpDown.refreshView(false);
                this.fcCtrlManager.setUpDownRockerExp(new UiCallBackListener<Object>() {
                    public void onComplete(CmdResult cmdResult, Object resp) {
                        if (cmdResult.isSuccess()) {
                        }
                    }
                }, value);
            } else if (viewId == R.id.edt_to_left_right) {
                this.cvToLeftRight.setCurValue((double) value);
                this.cvToLeftRight.refreshView(false);
                this.fcCtrlManager.setLeftRightRockerExp(new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                    }
                }, value);
            } else if (viewId == R.id.edt_to_go_back) {
                this.cvToGoBack.setCurValue((double) value);
                this.cvToGoBack.refreshView(false);
                this.fcCtrlManager.setGoBackRockerExp(new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                    }
                }, value, value);
            }
        }
    }

    public void onError(EditText v, int errorCode, String errorMsg) {
        X8ToastUtil.showToast(this.mContext, this.contentView.getContext().getString(R.string.x8_fc_exp_error_tip), 0);
        double curValue = 0.0d;
        int i = v.getId();
        if (i == R.id.edt_to_up_down) {
            curValue = this.cvToUpDown.getCurValue();
        } else if (i == R.id.edt_to_left_right) {
            curValue = this.cvToLeftRight.getCurValue();
        } else if (i == R.id.edt_to_go_back) {
            curValue = this.cvToGoBack.getCurValue();
        }
        v.setText(String.valueOf(curValue));
        v.clearFocus();
    }

    public void updateViewEnable(boolean enable, ViewGroup... parent) {
        if (parent != null && parent.length > 0) {
            for (ViewGroup group : parent) {
                int len = group.getChildCount();
                for (int j = 0; j < len; j++) {
                    View subView = group.getChildAt(j);
                    if (subView instanceof ViewGroup) {
                        updateViewEnable(enable, (ViewGroup) subView);
                    } else {
                        if (subView instanceof X8FixedEditText) {
                            subView.setEnabled(false);
                        } else {
                            subView.setEnabled(enable);
                        }
                        if (enable) {
                            subView.setAlpha(1.0f);
                        } else {
                            subView.setAlpha(0.6f);
                        }
                    }
                }
            }
        }
    }
}
