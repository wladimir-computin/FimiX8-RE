package com.fimi.app.x8s.controls.fcsettting;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.cmdsenum.X8Cali.CaliCmd;
import com.fimi.x8sdk.cmdsenum.X8Cali.CaliMode;
import com.fimi.x8sdk.cmdsenum.X8Cali.CaliStepStatus;
import com.fimi.x8sdk.cmdsenum.X8Cali.CaliType;
import com.fimi.x8sdk.cmdsenum.X8Cali.SensorType;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetCaliState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8DroneCalibrationController extends AbsX8MenuBoxControllers implements OnClickListener, onDialogButtonClickListener {
    private Button btnResultConfirm;
    private Button btnStart;
    private X8DoubleCustomDialog dialog;
    private FcCtrlManager fcCtrlManager;
    private ImageView imgFlag;
    private ImageView imgResult;
    private ImageView imgReturn;
    private IX8CalibrationListener listener;
    private GaliStete mGaliStete = GaliStete.IDLE;
    public Handler mTimeHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            X8DroneCalibrationController.this.getCalibrationState();
            sendEmptyMessageDelayed(0, 200);
        }
    };
    private View rlCalibration;
    private View rlResult;
    private TextView tvResultTip;
    private TextView tvResultTip1;
    private TextView tvTip;
    private TextView tvTip1;
    private View vVideo;

    /* renamed from: com.fimi.app.x8s.controls.fcsettting.X8DroneCalibrationController$8 */
    static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8DroneCalibrationController$GaliStete = new int[GaliStete.values().length];

        static {
            try {
                $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8DroneCalibrationController$GaliStete[GaliStete.WAITSTART.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8DroneCalibrationController$GaliStete[GaliStete.HORIZONTAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8DroneCalibrationController$GaliStete[GaliStete.NEXT_STEP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8DroneCalibrationController$GaliStete[GaliStete.VERTICAL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8DroneCalibrationController$GaliStete[GaliStete.NEXT_END.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum GaliStete {
        IDLE,
        WAITSTART,
        HORIZONTAL,
        NEXT_STEP,
        VERTICAL,
        NEXT_END,
        SUCCESS,
        FAILED,
        INTERRUPT,
        DISCONNECT_FAILED
    }

    public void onLeft() {
    }

    public void onRight() {
        if (this.mGaliStete == GaliStete.IDLE || this.mGaliStete == GaliStete.DISCONNECT_FAILED) {
            closeItem();
            return;
        }
        this.mGaliStete = GaliStete.INTERRUPT;
        quitGalibration();
    }

    public X8DroneCalibrationController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.contentView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.x8_main_fc_item_drone_calibration, (ViewGroup) rootView, true);
        this.rlCalibration = this.contentView.findViewById(R.id.rl_drone_calibartion_content);
        this.rlResult = this.contentView.findViewById(R.id.rl_drone_calibartion_result);
        this.imgReturn = (ImageView) this.contentView.findViewById(R.id.img_return);
        this.tvTip = (TextView) this.contentView.findViewById(R.id.tv_tip);
        this.tvTip1 = (TextView) this.contentView.findViewById(R.id.tv_tip1);
        this.vVideo = this.contentView.findViewById(R.id.v_video);
        this.btnStart = (Button) this.contentView.findViewById(R.id.btn_compass_calibration);
        this.imgResult = (ImageView) this.contentView.findViewById(R.id.img_result);
        this.tvResultTip = (TextView) this.contentView.findViewById(R.id.tv_result_tip);
        this.tvResultTip1 = (TextView) this.contentView.findViewById(R.id.tv_result_tip1);
        this.btnResultConfirm = (Button) this.contentView.findViewById(R.id.btn_compass_result_confirm);
        this.imgFlag = (ImageView) rootView.findViewById(R.id.img_camp_flag);
        this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(rootView.getContext(), R.drawable.x8_img_camp_enable));
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
        this.btnStart.setOnClickListener(this);
        this.btnResultConfirm.setOnClickListener(this);
    }

    public void onDroneConnected(boolean b) {
        if (!this.isShow) {
            return;
        }
        if (b) {
            getDroneState();
            if (this.isInSky) {
                this.btnStart.setEnabled(false);
                this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_img_camp_disable));
                this.tvTip.setText(this.rootView.getContext().getString(R.string.x8_compass_calibartion_insky_tip));
                return;
            } else if (this.mGaliStete == GaliStete.IDLE || this.mGaliStete == GaliStete.SUCCESS || this.mGaliStete == GaliStete.FAILED || this.mGaliStete == GaliStete.DISCONNECT_FAILED) {
                this.btnStart.setEnabled(true);
                this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_img_camp_enable));
                this.tvTip.setText(this.rootView.getContext().getString(R.string.x8_compass_calibartion_tip));
                return;
            } else {
                return;
            }
        }
        this.btnStart.setEnabled(false);
        this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_img_camp_disable));
        if (this.mGaliStete != GaliStete.IDLE) {
            GaliStete galiStete = this.mGaliStete;
            GaliStete galiStete2 = this.mGaliStete;
            if (galiStete != GaliStete.DISCONNECT_FAILED) {
                onFailedByDisconnect();
            }
        }
    }

    public void defaultVal() {
        this.rlCalibration.setVisibility(0);
        this.rlCalibration.setEnabled(false);
        this.rlResult.setVisibility(8);
        this.tvTip.setText(getString(R.string.x8_compass_calibartion_horizontal));
        this.tvTip1.setVisibility(0);
        this.btnStart.setVisibility(8);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_return) {
            closeItem();
        } else if (id == R.id.btn_compass_calibration) {
            startCalibration();
        } else if (id == R.id.btn_compass_result_confirm) {
            restart();
        }
    }

    public void restart() {
        this.tvTip.setText(getString(R.string.x8_compass_calibartion_tip));
        this.tvTip1.setVisibility(8);
        this.btnStart.setVisibility(0);
        this.rlCalibration.setVisibility(0);
        this.rlResult.setVisibility(8);
    }

    public void showItem() {
        this.isShow = true;
        this.tvTip.setText(getString(R.string.x8_compass_calibartion_tip));
        this.tvTip1.setVisibility(8);
        this.btnStart.setVisibility(0);
        this.contentView.setVisibility(0);
        getDroneState();
        onDroneConnected(this.isConect);
        this.listener.onOpen();
    }

    public void closeItem() {
        if (this.mGaliStete == GaliStete.IDLE || this.mGaliStete == GaliStete.DISCONNECT_FAILED) {
            this.isShow = false;
            this.contentView.setVisibility(8);
            defaultVal();
            this.mTimeHandler.removeMessages(0);
            this.listener.onClose();
            this.listener.onCalibrationReturn();
            StateManager.getInstance().getX8Drone().setOutTime(1500);
            return;
        }
        if (this.dialog == null) {
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getResources().getString(R.string.x8_fc_item_compass_calibration), this.rootView.getResources().getString(R.string.x8_fc_item_compass_calibration_exit), this);
        }
        this.dialog.show();
    }

    public void setCalibrationListener(IX8CalibrationListener listener) {
        this.listener = listener;
    }

    public void switch2Vertical() {
        this.tvTip.setText(getString(R.string.x8_compass_calibartion_vertical));
        this.tvTip1.setText(getString(R.string.x8_compass_calibartion_vertical_tip));
        this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_img_camp_v));
    }

    public void switch2Horizontal() {
        this.rlCalibration.setVisibility(0);
        this.rlResult.setVisibility(8);
        this.tvTip.setText(getString(R.string.x8_compass_calibartion_horizontal));
        this.tvTip1.setText(getString(R.string.x8_compass_calibartion_horizontal_tip));
        this.tvTip1.setVisibility(0);
        this.btnStart.setVisibility(8);
        this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_img_camp_h));
    }

    public void onResultFaild() {
        this.rlCalibration.setVisibility(8);
        this.rlResult.setVisibility(0);
        this.imgResult.setImageResource(R.drawable.x8_img_drone_calibartion_failed);
        this.tvResultTip.setText(getString(R.string.x8_compass_result_failed));
        this.tvResultTip1.setText(getString(R.string.x8_compass_result_failed_tip));
        this.btnResultConfirm.setText(getString(R.string.x8_compass_reuslt_failed_confirm));
        StateManager.getInstance().getX8Drone().setOutTime(1500);
    }

    public void onResultSuccess() {
        this.rlCalibration.setVisibility(8);
        this.rlResult.setVisibility(0);
        this.imgResult.setImageResource(R.drawable.x8_img_drone_calibartion_success);
        this.tvResultTip.setText(getString(R.string.x8_compass_result_success));
        this.tvResultTip1.setText(getString(R.string.x8_compass_result_success_tip));
        this.btnResultConfirm.setText(getString(R.string.x8_compass_reuslt_success_confirm));
        StateManager.getInstance().getX8Drone().setOutTime(1500);
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void startCalibration() {
        this.mGaliStete = GaliStete.WAITSTART;
        this.fcCtrlManager.setCalibrationStart(CaliType.CALI_MAG.ordinal(), CaliCmd.CALI_CMD_START.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8DroneCalibrationController.this.mTimeHandler.sendEmptyMessage(0);
                    StateManager.getInstance().getX8Drone().setOutTime(5000);
                    X8DroneCalibrationController.this.listener.onCalibrationStart();
                    return;
                }
                X8DroneCalibrationController.this.mGaliStete = GaliStete.IDLE;
            }
        });
    }

    public void nextStepCalibration() {
        this.fcCtrlManager.setCalibrationStart(CaliType.CALI_MAG.ordinal(), CaliCmd.CALI_CMD_NEXT_STEP.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8DroneCalibrationController.this.mGaliStete = GaliStete.NEXT_STEP;
                }
            }
        });
    }

    public void endStepCalibration() {
        this.fcCtrlManager.setCalibrationStart(CaliType.CALI_MAG.ordinal(), CaliCmd.CALI_CMD_NEXT_STEP.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8DroneCalibrationController.this.mGaliStete = GaliStete.NEXT_END;
                }
            }
        });
    }

    public void quitGalibration() {
        this.fcCtrlManager.setCalibrationStart(CaliType.CALI_MAG.ordinal(), CaliCmd.CALI_CMD_QUIT.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    if (X8DroneCalibrationController.this.mGaliStete == GaliStete.INTERRUPT) {
                        X8DroneCalibrationController.this.mGaliStete = GaliStete.IDLE;
                        X8DroneCalibrationController.this.closeItem();
                        return;
                    }
                    X8DroneCalibrationController.this.mGaliStete = GaliStete.IDLE;
                } else if (X8DroneCalibrationController.this.mGaliStete == GaliStete.INTERRUPT) {
                    X8DroneCalibrationController.this.mGaliStete = GaliStete.IDLE;
                    X8DroneCalibrationController.this.closeItem();
                } else {
                    X8DroneCalibrationController.this.mGaliStete = GaliStete.IDLE;
                }
            }
        });
    }

    public void completeGalibration() {
        this.fcCtrlManager.setCalibrationStart(CaliType.CALI_MAG.ordinal(), CaliCmd.CALI_CMD_ALL_DONE.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess()) {
                    return;
                }
                if (X8DroneCalibrationController.this.mGaliStete == GaliStete.INTERRUPT) {
                    X8DroneCalibrationController.this.mGaliStete = GaliStete.IDLE;
                    X8DroneCalibrationController.this.closeItem();
                    return;
                }
                X8DroneCalibrationController.this.mGaliStete = GaliStete.IDLE;
            }
        });
    }

    public void getCalibrationState() {
        this.fcCtrlManager.getCalibrationState(SensorType.MAGM.ordinal(), CaliType.CALI_MAG.ordinal(), new UiCallBackListener<AckGetCaliState>() {
            public void onComplete(CmdResult cmdResult, AckGetCaliState o) {
                if (cmdResult.isSuccess()) {
                    switch (AnonymousClass8.$SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8DroneCalibrationController$GaliStete[X8DroneCalibrationController.this.mGaliStete.ordinal()]) {
                        case 1:
                            if (o.getCaliStep() == 1) {
                                X8DroneCalibrationController.this.mGaliStete = GaliStete.HORIZONTAL;
                                X8DroneCalibrationController.this.switch2Horizontal();
                                return;
                            }
                            return;
                        case 2:
                            if (o.getCaliStep() == 1 && o.getStatus() == CaliStepStatus.CALI_STA_SAMPLE_DONE.ordinal()) {
                                X8DroneCalibrationController.this.nextStepCalibration();
                            }
                            if (o.getStatus() == CaliStepStatus.CALI_STA_ERR.ordinal()) {
                                X8DroneCalibrationController.this.onFailed();
                                return;
                            }
                            return;
                        case 3:
                            if (o.getCaliStep() == 2) {
                                X8DroneCalibrationController.this.mGaliStete = GaliStete.VERTICAL;
                                X8DroneCalibrationController.this.switch2Vertical();
                                return;
                            }
                            return;
                        case 4:
                            if (o.getCaliStep() == 2 || (o.getCaliStep() & 255) == 130) {
                                if (o.getStatus() == CaliStepStatus.CALI_STA_SAMPLE_DONE.ordinal()) {
                                    X8DroneCalibrationController.this.endStepCalibration();
                                } else if (o.getStatus() == CaliStepStatus.CALI_STA_DONE.ordinal()) {
                                    X8DroneCalibrationController.this.onSuccess();
                                }
                            }
                            if (o.getStatus() == CaliStepStatus.CALI_STA_ERR.ordinal()) {
                                X8DroneCalibrationController.this.onFailed();
                                return;
                            }
                            return;
                        case 5:
                            if (o.getStatus() == CaliStepStatus.CALI_STA_DONE.ordinal()) {
                                X8DroneCalibrationController.this.onSuccess();
                                return;
                            } else if (o.getStatus() == CaliStepStatus.CALI_STA_ERR.ordinal()) {
                                X8DroneCalibrationController.this.onFailed();
                                return;
                            } else {
                                return;
                            }
                        default:
                            return;
                    }
                }
            }
        });
    }

    public void onFailed() {
        this.mGaliStete = GaliStete.FAILED;
        quitGalibration();
        this.mTimeHandler.removeMessages(0);
        onResultFaild();
    }

    public void onFailedByDisconnect() {
        this.mGaliStete = GaliStete.DISCONNECT_FAILED;
        this.mTimeHandler.removeMessages(0);
        onResultFaild();
    }

    public void onSuccess() {
        this.mGaliStete = GaliStete.SUCCESS;
        completeGalibration();
        this.mTimeHandler.removeMessages(0);
        onResultSuccess();
    }

    public boolean isCalibrationing() {
        if (this.mGaliStete == GaliStete.IDLE || this.mGaliStete == GaliStete.SUCCESS || this.mGaliStete == GaliStete.FAILED || this.mGaliStete == GaliStete.DISCONNECT_FAILED) {
            return false;
        }
        closeItem();
        return true;
    }

    public String getString(int id) {
        return this.rootView.getContext().getResources().getString(id);
    }
}
