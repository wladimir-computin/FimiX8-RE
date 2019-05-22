package com.fimi.app.x8s.controls.fcsettting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckCloudCaliState;
import com.fimi.x8sdk.modulestate.StateManager;
import java.util.Timer;
import java.util.TimerTask;

public class X8CloudCalibrationController extends AbsX8MenuBoxControllers implements OnClickListener {
    private Button btnStart;
    private ProgressBar calibrationBar;
    private final int calibrationError = 9;
    CalibrationProgressListener checkLisenter;
    private CheckTask checkTask;
    private TextView checkTip;
    private View checkView;
    private ImageView cloudView;
    private Context context;
    private GimbalStatus curStatus = null;
    private X8DoubleCustomDialog dialog;
    private final int endDone = 1;
    private FcCtrlManager fcCtrlManager;
    private final int finishDone = 8;
    private GimbalStatus gimbalStatus = GimbalStatus.ideal;
    private View idealView;
    private ImageView imgAnimation;
    private ImageView imgResult;
    private ImageView imgReturn;
    private IX8CalibrationListener ix8CalibrationListener;
    private Button rtBtn;
    private View rtView;
    private final int startDone = 0;
    private Timer timer;
    private TextView tvRt;
    private TextView tvRtTip;
    private TextView tv_progress;
    private TextView tv_subTip;
    private TextView tv_tip;

    private class CalibrationProgressListener implements UiCallBackListener {
        private CalibrationProgressListener() {
        }

        /* synthetic */ CalibrationProgressListener(X8CloudCalibrationController x0, AnonymousClass1 x1) {
            this();
        }

        @SuppressLint({"StringFormatInvalid"})
        public void onComplete(CmdResult cmdResult, Object o) {
            if (cmdResult.isSuccess() && o != null) {
                AckCloudCaliState caliState = (AckCloudCaliState) o;
                if (caliState.getStatus() == 9) {
                    if (X8CloudCalibrationController.this.gimbalStatus == GimbalStatus.doing) {
                        X8CloudCalibrationController.this.gimbalStatus = GimbalStatus.fail;
                        X8CloudCalibrationController.this.showCalibrateFailed(caliState);
                    }
                } else if (caliState.getStatus() == 8) {
                    X8CloudCalibrationController.this.gimbalStatus = GimbalStatus.finish;
                } else {
                    X8CloudCalibrationController.this.showCalibrateProgress(caliState);
                }
                if (X8CloudCalibrationController.this.gimbalStatus != GimbalStatus.fail) {
                    X8CloudCalibrationController.this.showStatusView(X8CloudCalibrationController.this.gimbalStatus);
                }
            }
        }
    }

    class CheckTask extends TimerTask {
        CheckTask() {
        }

        public void run() {
            if (X8CloudCalibrationController.this.fcCtrlManager != null) {
                if (X8CloudCalibrationController.this.checkLisenter == null) {
                    X8CloudCalibrationController.this.checkLisenter = new CalibrationProgressListener(X8CloudCalibrationController.this, null);
                }
                X8CloudCalibrationController.this.fcCtrlManager.checkCloudCalibrationProgress(X8CloudCalibrationController.this.checkLisenter);
            }
        }
    }

    public enum GimbalStatus {
        ideal,
        doing,
        fail,
        conBroken,
        finish
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setIx8CalibrationListener(IX8CalibrationListener ix8CalibrationListener) {
        this.ix8CalibrationListener = ix8CalibrationListener;
    }

    public X8CloudCalibrationController(View rootView) {
        super(rootView);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_start_calibration) {
            startCalibration();
        } else if (i == R.id.btn_rt_confirm) {
            if (this.gimbalStatus == GimbalStatus.fail) {
                this.gimbalStatus = GimbalStatus.ideal;
                showStatusView(this.gimbalStatus);
            } else if (this.gimbalStatus == GimbalStatus.finish) {
                if (this.ix8CalibrationListener != null) {
                    this.ix8CalibrationListener.onCalibrationReturn();
                    closeUi();
                }
            } else if (this.gimbalStatus == GimbalStatus.conBroken) {
                this.gimbalStatus = GimbalStatus.ideal;
                showStatusView(this.gimbalStatus);
            }
        } else if (i == R.id.img_return) {
            breakOutDone();
        }
    }

    private void breakOutDone() {
        if (this.gimbalStatus == GimbalStatus.doing) {
            this.dialog = new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_cloud_gimbal_break_out_title), this.context.getString(R.string.x8_cloud_gimbal_break_out_tip), new onDialogButtonClickListener() {
                public void onLeft() {
                    X8CloudCalibrationController.this.dialog.dismiss();
                }

                public void onRight() {
                    if (X8CloudCalibrationController.this.fcCtrlManager != null) {
                        X8CloudCalibrationController.this.fcCtrlManager.cloudCalibration(1, new UiCallBackListener() {
                            public void onComplete(CmdResult cmdResult, Object o) {
                                if (X8CloudCalibrationController.this.ix8CalibrationListener != null) {
                                    X8CloudCalibrationController.this.ix8CalibrationListener.onCalibrationReturn();
                                    X8CloudCalibrationController.this.closeUi();
                                }
                            }
                        });
                    }
                    X8CloudCalibrationController.this.dialog.dismiss();
                }
            });
            if (this.gimbalStatus == GimbalStatus.doing) {
                this.dialog.show();
            }
        } else if (this.ix8CalibrationListener != null) {
            this.ix8CalibrationListener.onCalibrationReturn();
            closeUi();
        }
    }

    private void startCalibration() {
        if (this.fcCtrlManager != null && this.gimbalStatus == GimbalStatus.ideal) {
            this.fcCtrlManager.cloudCalibration(0, new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                        X8CloudCalibrationController.this.gimbalStatus = GimbalStatus.doing;
                        X8CloudCalibrationController.this.showStatusView(X8CloudCalibrationController.this.gimbalStatus);
                        X8CloudCalibrationController.this.startCheck();
                    }
                }
            });
        }
    }

    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.handleView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.x8_main_camera_item_cloud_calibration, (ViewGroup) rootView, true);
        this.cloudView = (ImageView) this.handleView.findViewById(R.id.cloud_v);
        this.imgReturn = (ImageView) this.handleView.findViewById(R.id.img_return);
        this.btnStart = (Button) this.handleView.findViewById(R.id.btn_start_calibration);
        this.tv_tip = (TextView) this.handleView.findViewById(R.id.tv_tip);
        this.tv_subTip = (TextView) this.handleView.findViewById(R.id.tv_tip1);
        this.imgAnimation = (ImageView) this.handleView.findViewById(R.id.img_animation);
        this.imgAnimation.setBackgroundResource(R.drawable.x8_calibration_animation);
        ((AnimationDrawable) this.imgAnimation.getBackground()).start();
        this.idealView = this.handleView.findViewById(R.id.rl_cloud_calibration_content);
        this.checkView = this.handleView.findViewById(R.id.rl_calibration_progress_layout);
        this.rtView = this.handleView.findViewById(R.id.rl_cloud_calibration_result);
        this.calibrationBar = (ProgressBar) this.handleView.findViewById(R.id.calibration_bar);
        this.tv_progress = (TextView) this.handleView.findViewById(R.id.tv_progress);
        this.checkTip = (TextView) this.handleView.findViewById(R.id.tv_check_tip);
        this.rtBtn = (Button) this.handleView.findViewById(R.id.btn_rt_confirm);
        this.rtBtn.setOnClickListener(this);
        this.tvRtTip = (TextView) this.handleView.findViewById(R.id.tv_result_tip2);
        this.tvRt = (TextView) this.handleView.findViewById(R.id.tv_result_tip);
        this.imgResult = (ImageView) this.handleView.findViewById(R.id.img_result);
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
        this.btnStart.setOnClickListener(this);
    }

    public void defaultVal() {
    }

    public void unMountError(boolean unMount) {
        super.unMountError(unMount);
        if (this.isShow && unMount) {
            this.tv_tip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_error_2));
            this.btnStart.setEnabled(false);
            this.cloudView.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_cloud_no_exits_icon));
        }
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (!this.isShow) {
            return;
        }
        if (StateManager.getInstance().getX8Drone().isInSky() && this.gimbalStatus == GimbalStatus.ideal) {
            if (this.btnStart.isEnabled()) {
                this.tv_tip.setText(getString(R.string.x8_cloud_gimbal_error_3));
                this.btnStart.setEnabled(false);
                this.cloudView.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_cloud_unable_icon));
            }
        } else if (!b) {
            cancelCheck();
            if (this.btnStart.isEnabled()) {
                this.btnStart.setEnabled(false);
            }
            if (this.gimbalStatus == GimbalStatus.ideal) {
                this.cloudView.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_cloud_unable_icon));
                if (this.checkTip != null) {
                    this.tv_tip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_error_1));
                }
            } else if (this.gimbalStatus == GimbalStatus.doing) {
                this.gimbalStatus = GimbalStatus.conBroken;
                showStatusView(this.gimbalStatus);
            }
        } else if (this.gimbalStatus == GimbalStatus.ideal) {
            this.tv_tip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip));
            this.cloudView.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_cloud_normal));
            if (!this.btnStart.isEnabled()) {
                this.btnStart.setEnabled(true);
                this.btnStart.setAlpha(1.0f);
            }
        } else if (this.gimbalStatus != GimbalStatus.doing && this.gimbalStatus == GimbalStatus.conBroken) {
            this.gimbalStatus = GimbalStatus.ideal;
            showStatusView(this.gimbalStatus);
        }
    }

    private void setCheckTip(int color, int text) {
        this.checkTip.setTextColor(this.rootView.getResources().getColor(color));
        this.checkTip.setText(this.context.getResources().getString(text));
    }

    private void showCalibrateProgress(AckCloudCaliState caliState) {
        int progress = caliState.getProgress();
        if (progress > 0) {
            this.calibrationBar.setProgress(progress);
            this.tv_progress.setText(String.format(this.context.getResources().getString(R.string.x8_calibration_progress), new Object[]{String.valueOf(progress) + "%"}));
            this.gimbalStatus = GimbalStatus.doing;
            if (caliState.isTempeOverErr()) {
                setCheckTip(R.color.x8_error_code_type1, R.string.x8_cloud_gimbal_tip_3);
            } else if (caliState.isNeedLeveling123()) {
                setCheckTip(R.color.x8_error_code_type1, R.string.x8_cloud_gimbal_tip_4);
            } else if (caliState.isNeedLeveling4()) {
                setCheckTip(R.color.x8_error_code_type1, R.string.x8_cloud_gimbal_tip_5);
            } else if (caliState.isNeedLeveling5()) {
                setCheckTip(R.color.x8_error_code_type1, R.string.x8_cloud_gimbal_tip_6);
            } else if (caliState.isNeedLeveling10()) {
                setCheckTip(R.color.x8_error_code_type1, R.string.x8_cloud_gimbal_tip_7);
            } else if (caliState.isNeedLeveling11()) {
                setCheckTip(R.color.x8_error_code_type1, R.string.x8_cloud_gimbal_tip_8);
            } else {
                setCheckTip(R.color.white_100, R.string.x8_cloud_gimbal_tip_2);
            }
        }
    }

    private void showCalibrateFailed(AckCloudCaliState caliState) {
        this.checkView.setVisibility(8);
        this.idealView.setVisibility(8);
        this.rtView.setVisibility(0);
        this.imgResult.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_calibration_fail_icon));
        this.tvRt.setText(this.context.getResources().getString(R.string.x8_compass_result_failed));
        if (caliState.isTempeOverErr()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_18));
        } else if (caliState.isNeedLeveling123()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_13));
        } else if (caliState.isNeedLeveling4()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_14));
        } else if (caliState.isNeedLeveling5()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_15));
        } else if (caliState.isNeedLeveling10()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_16));
        } else if (caliState.isNeedLeveling11()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_17));
        } else if (caliState.isNeedLeveling6()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_9));
        } else if (caliState.isNeedLeveling7()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_10));
        } else if (caliState.isNeedLeveling8()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_11));
        } else if (caliState.isNeedLeveling9()) {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_12));
        } else {
            this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_compass_result_failed_tip));
        }
        this.tvRtTip.setVisibility(0);
        this.rtBtn.setText(this.context.getResources().getString(R.string.x8_compass_reuslt_failed_confirm));
    }

    private void startCheck() {
        cancelCheck();
        this.timer = new Timer();
        this.checkTask = new CheckTask();
        this.timer.schedule(this.checkTask, 0, 500);
    }

    private void cancelCheck() {
        if (this.checkTask != null) {
            this.checkTask.cancel();
            this.checkTask = null;
        }
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    public void openUi() {
        super.openUi();
        this.isShow = true;
        getDroneState();
        if (this.isConect) {
            if (this.gimbalStatus == GimbalStatus.ideal) {
                this.tv_tip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip));
                this.cloudView.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_cloud_normal));
            }
            if (!this.btnStart.isEnabled()) {
                this.btnStart.setEnabled(true);
                this.btnStart.setAlpha(1.0f);
                return;
            }
            return;
        }
        if (this.btnStart.isEnabled()) {
            this.btnStart.setEnabled(false);
            this.btnStart.setAlpha(0.6f);
        }
        if (this.gimbalStatus == GimbalStatus.ideal) {
            this.cloudView.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_cloud_unable_icon));
            if (this.checkTip != null) {
                this.tv_tip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_error_1));
            }
        }
    }

    public void closeUi() {
        super.closeUi();
        this.isShow = false;
        this.gimbalStatus = GimbalStatus.ideal;
        showStatusView(this.gimbalStatus);
        cancelCheck();
    }

    private void showStatusView(GimbalStatus status) {
        if (status != this.curStatus) {
            this.curStatus = status;
            this.idealView.setVisibility(0);
            this.checkView.setVisibility(8);
            this.rtView.setVisibility(8);
            if (status == GimbalStatus.ideal) {
                this.tv_tip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_error_1));
                this.cloudView.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_cloud_unable_icon));
                this.btnStart.setEnabled(false);
                this.btnStart.setAlpha(0.6f);
            } else if (status == GimbalStatus.fail) {
            } else {
                if (status == GimbalStatus.finish) {
                    this.idealView.setVisibility(8);
                    this.rtView.setVisibility(0);
                    this.imgResult.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_calibration_success_icon));
                    this.tvRt.setText(this.context.getResources().getString(R.string.x8_compass_result_success));
                    this.tvRtTip.setVisibility(8);
                    this.rtBtn.setText(this.context.getResources().getString(R.string.x8_compass_reuslt_success_confirm));
                } else if (status == GimbalStatus.doing) {
                    this.checkView.setVisibility(0);
                    this.idealView.setVisibility(8);
                    this.checkTip.setTextColor(this.rootView.getResources().getColor(R.color.white_100));
                    this.checkTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_tip_2));
                } else if (status == GimbalStatus.conBroken) {
                    this.idealView.setVisibility(8);
                    this.rtView.setVisibility(0);
                    this.imgResult.setImageDrawable(this.context.getResources().getDrawable(R.drawable.x8_calibration_fail_icon));
                    this.tvRt.setText(this.context.getResources().getString(R.string.x8_compass_result_failed));
                    this.tvRtTip.setVisibility(0);
                    this.tvRtTip.setText(this.context.getResources().getString(R.string.x8_cloud_gimbal_error_4));
                    this.rtBtn.setText(this.context.getResources().getString(R.string.x8_compass_reuslt_failed_confirm));
                }
            }
        }
    }

    public boolean onClickBackKey() {
        breakOutDone();
        return true;
    }

    public boolean isCalibrationing() {
        if (this.gimbalStatus == GimbalStatus.ideal || this.gimbalStatus == GimbalStatus.fail || this.gimbalStatus == GimbalStatus.finish) {
            return false;
        }
        breakOutDone();
        return true;
    }
}
