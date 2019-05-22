package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.widget.MidView;
import com.fimi.app.x8s.widget.MidView.clipType;
import com.fimi.app.x8s.widget.RcRollerView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckRcCalibrationState;
import com.fimi.x8sdk.modulestate.StateManager;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class X8RCCalibrationController extends AbsX8MenuBoxControllers implements OnClickListener, UiCallBackListener {
    private ImageView backBtn;
    private Button cali_btn;
    private final int centerValue = 512;
    private CheckTask checkTask;
    private Context context;
    private RelativeLayout control_layout;
    RcStatus curStatus;
    private X8DoubleCustomDialog dialog;
    private boolean downRoller;
    private boolean droneOkay = false;
    private TextView errorTip;
    private final int exitCmd = 4;
    private FcCtrlManager fcCtrlManager;
    private IX8CalibrationListener ix8CalibrationListener;
    private final int joyCmd = 2;
    private MidView lefMidView;
    ArrayList<clipType> leftClips = new ArrayList();
    private RcRollerView leftDownRoller;
    private ImageView leftMidBottom;
    private ImageView leftMidLeft;
    private int leftMidResult = 0;
    private ImageView leftMidRight;
    private ImageView leftMidTop;
    private RcRollerView leftUpRoller;
    private final int midCmd = 1;
    private boolean rcConnect = false;
    private RcStatus rcStatus = RcStatus.ideal;
    private RelativeLayout rc_layout;
    ArrayList<clipType> rightClips = new ArrayList();
    private ImageView rightMidBottom;
    private ImageView rightMidLeft;
    private int rightMidResult = 0;
    private ImageView rightMidRight;
    private ImageView rightMidTop;
    private MidView rightMidView;
    private final int rollerCmd = 3;
    private Button rtBtn;
    private ImageView rtImage;
    private TextView rtTip;
    private RelativeLayout rt_layout;
    private Timer timer;
    private TextView tipTV;
    private boolean upRoller;

    class CheckTask extends TimerTask {
        CheckTask() {
        }

        public void run() {
            if (X8RCCalibrationController.this.fcCtrlManager != null) {
                X8RCCalibrationController.this.fcCtrlManager.checkRcCalibrationProgress(X8RCCalibrationController.this);
            }
        }
    }

    private enum RcStatus {
        ideal,
        midModel,
        joyModel,
        rollerModel,
        fail,
        finish,
        conBroken,
        error
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setIx8CalibrationListener(IX8CalibrationListener ix8CalibrationListener) {
        this.ix8CalibrationListener = ix8CalibrationListener;
    }

    public X8RCCalibrationController(View rootView) {
        super(rootView);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_calibration) {
            if (this.fcCtrlManager != null) {
                this.fcCtrlManager.rcCalibration(1, this);
            }
        } else if (i == R.id.img_return) {
            breakOutDone();
        } else if (i == R.id.btn_rt) {
            if (this.rcStatus == RcStatus.finish) {
                if (this.ix8CalibrationListener != null) {
                    this.ix8CalibrationListener.onCalibrationReturn();
                    closeUi();
                }
                if (this.fcCtrlManager != null) {
                    this.fcCtrlManager.rcCalibration(4, this);
                }
            } else if (this.rcStatus == RcStatus.fail) {
                if (this.fcCtrlManager != null) {
                    this.fcCtrlManager.rcCalibration(4, this);
                }
                this.rcStatus = RcStatus.ideal;
                gotoModel(this.rcStatus);
            }
            if (this.rcStatus == RcStatus.conBroken) {
                this.rcStatus = RcStatus.ideal;
                gotoModel(this.rcStatus);
                this.cali_btn.setAlpha(0.6f);
                this.cali_btn.setEnabled(false);
            }
        }
    }

    private void breakOutDone() {
        this.dialog = new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_rc_exit_calibration), this.context.getString(R.string.x8_rc_exit_tip), new onDialogButtonClickListener() {
            public void onLeft() {
                X8RCCalibrationController.this.dialog.dismiss();
            }

            public void onRight() {
                if (X8RCCalibrationController.this.fcCtrlManager != null) {
                    X8RCCalibrationController.this.fcCtrlManager.rcCalibration(4, X8RCCalibrationController.this);
                }
                X8RCCalibrationController.this.dialog.dismiss();
                if (X8RCCalibrationController.this.ix8CalibrationListener != null) {
                    X8RCCalibrationController.this.ix8CalibrationListener.onCalibrationReturn();
                    X8RCCalibrationController.this.closeUi();
                }
            }
        });
        if (this.rcStatus != RcStatus.ideal && this.rcStatus != RcStatus.finish && this.rcStatus != RcStatus.conBroken) {
            this.dialog.show();
        } else if (this.ix8CalibrationListener != null) {
            this.ix8CalibrationListener.onCalibrationReturn();
            closeUi();
        }
    }

    public void onComplete(CmdResult cmdResult, Object o) {
        if (cmdResult.isSuccess() && o != null) {
            AckRcCalibrationState caliState = (AckRcCalibrationState) o;
            if (caliState.getMsgId() != 15) {
                return;
            }
            if (caliState.getStatus() != 7 || this.rcStatus == RcStatus.ideal) {
                int rc1;
                int rc0;
                int rc2;
                int rc3;
                int rc4;
                if (caliState.getProgress() == 1) {
                    if (caliState.getStatus() == 1) {
                        this.fcCtrlManager.rcCalibration(2, this);
                    } else {
                        this.rcStatus = RcStatus.midModel;
                    }
                } else if (caliState.getProgress() == 2) {
                    if (caliState.getStatus() == 2) {
                        this.fcCtrlManager.rcCalibration(3, this);
                    } else {
                        this.rcStatus = RcStatus.joyModel;
                        rc1 = caliState.getRc1();
                        rc0 = caliState.getRc0();
                        rc2 = caliState.getRc2();
                        rc3 = caliState.getRc3();
                        this.lefMidView.setFxFy((float) rc1, (float) rc0);
                        this.rightMidView.setFxFy((float) rc3, (float) rc2);
                    }
                } else if (caliState.getProgress() == 3) {
                    if (caliState.getStatus() != 3) {
                        this.rcStatus = RcStatus.rollerModel;
                        rc4 = caliState.getRc4();
                        if (rc4 > 512 && !this.upRoller) {
                            this.leftUpRoller.upRollerValue(rc4 - 512);
                        } else if (rc4 >= 512 || this.downRoller) {
                            if (!this.upRoller) {
                                this.leftDownRoller.clean();
                            }
                            if (!this.downRoller) {
                                this.leftUpRoller.clean();
                            }
                        } else {
                            this.leftDownRoller.upRollerValue(512 - rc4);
                        }
                    } else if (!(this.rcStatus == RcStatus.finish || this.rcStatus == RcStatus.ideal)) {
                        this.rcStatus = RcStatus.finish;
                    }
                }
                int cmdStatus = caliState.getCmdStatus();
                if (this.rcStatus == RcStatus.joyModel) {
                    if ((cmdStatus & 1) == 1 && !this.leftMidTop.isSelected()) {
                        this.leftMidResult++;
                        this.leftClips.add(clipType.top);
                        this.leftMidTop.setSelected(true);
                    }
                    if (((cmdStatus & 2) >> 1) == 1 && !this.leftMidLeft.isSelected()) {
                        this.leftMidResult++;
                        this.leftClips.add(clipType.left);
                        this.leftMidLeft.setSelected(true);
                    }
                    if (((cmdStatus & 128) >> 7) == 1 && !this.leftMidRight.isSelected()) {
                        this.leftMidResult++;
                        this.leftClips.add(clipType.right);
                        this.leftMidRight.setSelected(true);
                    }
                    if (((cmdStatus & 64) >> 6) == 1 && !this.leftMidBottom.isSelected()) {
                        this.leftMidResult++;
                        this.leftClips.add(clipType.bottom);
                        this.leftMidBottom.setSelected(true);
                    }
                    if (((cmdStatus & 4) >> 2) == 1 && !this.rightMidTop.isSelected()) {
                        this.rightMidResult++;
                        this.rightClips.add(clipType.top);
                        this.rightMidTop.setSelected(true);
                    }
                    if (((cmdStatus & 8) >> 3) == 1 && !this.rightMidLeft.isSelected()) {
                        this.rightMidResult++;
                        this.rightClips.add(clipType.left);
                        this.rightMidLeft.setSelected(true);
                    }
                    if (((cmdStatus & 512) >> 9) == 1 && !this.rightMidRight.isSelected()) {
                        this.rightMidResult++;
                        this.rightClips.add(clipType.right);
                        this.rightMidRight.setSelected(true);
                    }
                    if (((cmdStatus & 256) >> 8) == 1 && !this.rightMidBottom.isSelected()) {
                        this.rightMidResult++;
                        this.rightClips.add(clipType.bottom);
                        this.rightMidBottom.setSelected(true);
                    }
                    this.lefMidView.setType(this.leftClips);
                    this.rightMidView.setType(this.rightClips);
                    if (this.leftMidResult == 4) {
                        this.lefMidView.joyFinish();
                    }
                    if (this.rightMidResult == 4) {
                        this.rightMidView.joyFinish();
                    }
                } else if (this.rcStatus == RcStatus.rollerModel) {
                    if (((cmdStatus & 16) >> 4) == 1) {
                        this.upRoller = true;
                    }
                    if (((cmdStatus & 1024) >> 10) == 1) {
                        this.downRoller = true;
                    }
                }
                if (this.rcStatus == RcStatus.ideal) {
                    rc1 = caliState.getRc1();
                    rc0 = caliState.getRc0();
                    rc2 = caliState.getRc2();
                    rc3 = caliState.getRc3();
                    this.lefMidView.setFxFy((float) rc1, (float) rc0);
                    this.rightMidView.setFxFy((float) rc3, (float) rc2);
                    rc4 = caliState.getRc4();
                    if (rc4 > 512) {
                        this.leftUpRoller.upRollerValue(rc4 - 512);
                    } else if (rc4 < 512) {
                        this.leftDownRoller.upRollerValue(512 - rc4);
                    } else {
                        this.leftDownRoller.clean();
                        this.leftUpRoller.clean();
                    }
                }
                if (!this.droneOkay) {
                    gotoModel(this.rcStatus);
                    return;
                }
                return;
            }
            this.rcStatus = RcStatus.fail;
            gotoModel(this.rcStatus);
        }
    }

    private void gotoModel(RcStatus rcStatus) {
        if (this.curStatus != rcStatus) {
            this.curStatus = rcStatus;
            this.rc_layout.setVisibility(0);
            this.rt_layout.setVisibility(8);
            this.leftMidTop.setVisibility(8);
            this.leftMidBottom.setVisibility(8);
            this.leftMidLeft.setVisibility(8);
            this.leftMidRight.setVisibility(8);
            this.rightMidTop.setVisibility(8);
            this.rightMidRight.setVisibility(8);
            this.rightMidBottom.setVisibility(8);
            this.rightMidLeft.setVisibility(8);
            this.leftDownRoller.setVisibility(8);
            this.leftUpRoller.setVisibility(8);
            this.cali_btn.setVisibility(8);
            this.errorTip.setVisibility(8);
            if (this.curStatus == RcStatus.error) {
                this.control_layout.setBackground(new BitmapDrawable(ImageUtils.getBitmapByPath(this.control_layout.getContext(), R.drawable.x8_rc_unable_bg)));
            } else if (rcStatus == RcStatus.rollerModel) {
                this.control_layout.setBackground(new BitmapDrawable(ImageUtils.getBitmapByPath(this.control_layout.getContext(), R.drawable.x8_rc_roller_bg)));
            } else {
                this.control_layout.setBackground(new BitmapDrawable(ImageUtils.getBitmapByPath(this.control_layout.getContext(), R.drawable.x8_rc_roller_bg)));
            }
            if (rcStatus == RcStatus.ideal) {
                this.leftDownRoller.setVisibility(0);
                this.leftUpRoller.setVisibility(0);
                this.tipTV.setText(getString(R.string.x8_rc_lead_tip));
                this.cali_btn.setEnabled(true);
                this.cali_btn.setAlpha(1.0f);
                this.cali_btn.setVisibility(0);
                this.leftClips.clear();
                this.rightClips.clear();
                this.lefMidView.setType(this.leftClips);
                this.rightMidView.setType(this.rightClips);
                this.rightMidView.releaseAll();
                this.lefMidView.releaseAll();
                this.leftUpRoller.clean();
                this.leftDownRoller.clean();
            } else if (rcStatus == RcStatus.joyModel) {
                this.tipTV.setText(getString(R.string.x8_rc_joy_tip));
                this.leftMidTop.setVisibility(0);
                this.leftMidBottom.setVisibility(0);
                this.leftMidLeft.setVisibility(0);
                this.leftMidRight.setVisibility(0);
                this.lefMidView.setAlpha(1.0f);
                this.rightMidView.setAlpha(1.0f);
                this.rightMidTop.setVisibility(0);
                this.rightMidRight.setVisibility(0);
                this.rightMidBottom.setVisibility(0);
                this.rightMidLeft.setVisibility(0);
            } else if (rcStatus == RcStatus.rollerModel) {
                this.tipTV.setText(getString(R.string.x8_rc_roller_tip));
                this.leftDownRoller.setVisibility(0);
                this.leftUpRoller.setVisibility(0);
                this.leftClips.clear();
                this.rightClips.clear();
                this.lefMidView.setAlpha(0.4f);
                this.rightMidView.setAlpha(0.4f);
                this.lefMidView.setType(this.leftClips);
                this.rightMidView.setType(this.rightClips);
                this.rightMidView.releaseAll();
                this.lefMidView.releaseAll();
            } else if (rcStatus == RcStatus.midModel) {
                this.tipTV.setText(getString(R.string.x8_rc_mid_tip));
                this.lefMidView.setAlpha(1.0f);
                this.rightMidView.setAlpha(1.0f);
            } else if (rcStatus == RcStatus.finish) {
                this.rc_layout.setVisibility(8);
                this.rt_layout.setVisibility(0);
                this.rtImage.setImageResource(R.drawable.x8_calibration_success_icon);
                this.rtTip.setText(getString(R.string.x8_compass_result_success));
                this.rtBtn.setText(R.string.x8_compass_reuslt_success_confirm);
            } else if (rcStatus == RcStatus.fail) {
                this.rt_layout.setVisibility(0);
                this.rc_layout.setVisibility(8);
                this.rtTip.setText(getString(R.string.x8_compass_result_failed));
                this.rtBtn.setText(R.string.x8_compass_reuslt_failed_confirm);
                this.rtImage.setImageResource(R.drawable.x8_calibration_fail_icon);
                this.errorTip.setText(getString(R.string.x8_compass_result_failed_tip));
                this.errorTip.setVisibility(0);
            } else if (rcStatus == RcStatus.conBroken) {
                this.rt_layout.setVisibility(0);
                this.rc_layout.setVisibility(8);
                this.rtImage.setImageResource(R.drawable.x8_calibration_fail_icon);
                this.rtTip.setText(getString(R.string.x8_compass_result_failed));
                this.rtBtn.setText(R.string.x8_compass_reuslt_failed_confirm);
                this.errorTip.setText(getString(R.string.x8_rc_calibration_tip));
                this.errorTip.setVisibility(0);
                this.rtBtn.setEnabled(true);
                this.rtBtn.setAlpha(1.0f);
            } else if (rcStatus == RcStatus.error) {
                this.cali_btn.setVisibility(0);
                this.cali_btn.setEnabled(false);
                this.cali_btn.setAlpha(0.6f);
            }
        }
    }

    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.handleView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.x8_rc_calibration_layout, (ViewGroup) rootView, true);
        this.leftUpRoller = (RcRollerView) this.handleView.findViewById(R.id.left_up);
        this.leftDownRoller = (RcRollerView) this.handleView.findViewById(R.id.left_down);
        this.lefMidView = (MidView) this.handleView.findViewById(R.id.mid_left);
        this.rightMidView = (MidView) this.handleView.findViewById(R.id.mid_right);
        this.lefMidView.setAlpha(0.4f);
        this.rightMidView.setAlpha(0.4f);
        this.cali_btn = (Button) this.handleView.findViewById(R.id.btn_calibration);
        this.cali_btn.setOnClickListener(this);
        this.leftMidTop = (ImageView) this.handleView.findViewById(R.id.left_top_icon);
        this.leftMidBottom = (ImageView) this.handleView.findViewById(R.id.left_bottom_icon);
        this.leftMidLeft = (ImageView) this.handleView.findViewById(R.id.left_left_icon);
        this.leftMidRight = (ImageView) this.handleView.findViewById(R.id.left_right_icon);
        this.rightMidTop = (ImageView) this.handleView.findViewById(R.id.right_top_icon);
        this.rightMidBottom = (ImageView) this.handleView.findViewById(R.id.right_bottom_icon);
        this.rightMidLeft = (ImageView) this.handleView.findViewById(R.id.right_left_icon);
        this.rightMidRight = (ImageView) this.handleView.findViewById(R.id.right_right_icon);
        this.backBtn = (ImageView) this.handleView.findViewById(R.id.img_return);
        this.backBtn.setOnClickListener(this);
        this.tipTV = (TextView) this.handleView.findViewById(R.id.tv_tip);
        this.rt_layout = (RelativeLayout) this.handleView.findViewById(R.id.rl_rc_calibration_result);
        this.errorTip = (TextView) this.handleView.findViewById(R.id.tv_error_tip);
        this.rtTip = (TextView) this.handleView.findViewById(R.id.tv_result_tip);
        this.rtBtn = (Button) this.handleView.findViewById(R.id.btn_rt);
        this.rtBtn.setOnClickListener(this);
        this.rc_layout = (RelativeLayout) this.handleView.findViewById(R.id.rc_calibration_content);
        this.control_layout = (RelativeLayout) this.handleView.findViewById(R.id.control_layout);
        this.leftDownRoller.setOnClickListener(this);
        this.rtImage = (ImageView) rootView.findViewById(R.id.img_result);
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void openUi() {
        super.openUi();
        this.isShow = true;
        this.droneOkay = StateManager.getInstance().getConectState().isConnectDrone();
        if (this.droneOkay) {
            this.tipTV.setText(getString(R.string.x8_rc_plane_connect));
            gotoModel(RcStatus.error);
        } else if (StateManager.getInstance().getConectState().isConnectRelay()) {
            updateViewEnable(true, this.rc_layout, this.rt_layout);
            gotoModel(this.rcStatus);
        } else {
            this.tipTV.setText(getString(R.string.x8_rc_no_connect_tip));
            gotoModel(RcStatus.error);
        }
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (b && StateManager.getInstance().getX8Drone().isInSky()) {
            this.tipTV.setText(getString(R.string.x8_rc_plane_inSky));
            gotoModel(RcStatus.error);
            return;
        }
        if (this.droneOkay != b) {
            this.droneOkay = b;
        }
        if (this.droneOkay) {
            this.tipTV.setText(getString(R.string.x8_rc_plane_connect));
            gotoModel(RcStatus.error);
        } else if (!this.rcConnect) {
        } else {
            if (this.rcStatus == RcStatus.ideal) {
                gotoModel(RcStatus.ideal);
            } else if (this.rcStatus == RcStatus.midModel) {
                gotoModel(RcStatus.midModel);
            } else if (this.rcStatus == RcStatus.rollerModel) {
                gotoModel(RcStatus.rollerModel);
            } else if (this.rcStatus == RcStatus.joyModel) {
                gotoModel(RcStatus.joyModel);
            }
        }
    }

    public void checkRcConnect(boolean isConnect) {
        if (this.rcConnect != isConnect) {
            this.rcConnect = isConnect;
            if (this.isShow && !this.droneOkay) {
                if (isConnect) {
                    updateViewEnable(true, this.rc_layout, this.rt_layout);
                    startCheck();
                    gotoModel(this.rcStatus);
                    return;
                }
                this.droneOkay = false;
                stopTask();
                if (this.rcStatus == RcStatus.ideal || this.rcStatus == RcStatus.finish || this.rcStatus == RcStatus.fail) {
                    this.tipTV.setText(getString(R.string.x8_rc_no_connect_tip));
                    gotoModel(RcStatus.error);
                    return;
                }
                this.rcStatus = RcStatus.conBroken;
                gotoModel(this.rcStatus);
            }
        }
    }

    public void closeUi() {
        super.closeUi();
        this.isShow = false;
        releaseDone();
    }

    private void releaseDone() {
        stopTask();
        this.droneOkay = false;
        this.rcConnect = false;
        this.leftClips.clear();
        this.rightClips.clear();
        this.lefMidView.setType(this.leftClips);
        this.rightMidView.setType(this.rightClips);
        this.rightMidView.releaseAll();
        this.lefMidView.releaseAll();
        this.leftUpRoller.clean();
        this.leftDownRoller.clean();
        this.rcStatus = RcStatus.ideal;
        this.curStatus = null;
        this.leftMidTop.setSelected(false);
        this.leftMidLeft.setSelected(false);
        this.leftMidRight.setSelected(false);
        this.leftMidBottom.setSelected(false);
        this.rightMidTop.setSelected(false);
        this.rightMidLeft.setSelected(false);
        this.rightMidRight.setSelected(false);
        this.rightMidBottom.setSelected(false);
        this.upRoller = false;
        this.downRoller = false;
        this.leftMidResult = 0;
        this.rightMidResult = 0;
        this.cali_btn.setVisibility(0);
        this.rc_layout.setVisibility(0);
        this.rt_layout.setVisibility(8);
        this.errorTip.setVisibility(8);
        gotoModel(this.rcStatus);
        this.cali_btn.setEnabled(false);
        this.cali_btn.setAlpha(0.6f);
    }

    private void stopTask() {
        if (this.checkTask != null) {
            this.checkTask.cancel();
            this.checkTask = null;
        }
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    private void startCheck() {
        stopTask();
        this.timer = new Timer();
        this.checkTask = new CheckTask();
        this.timer.schedule(this.checkTask, 0, 1000);
    }

    public boolean onClickBackKey() {
        return false;
    }

    public boolean isCalibrationing() {
        if (this.rcStatus == RcStatus.ideal || this.rcStatus == RcStatus.fail || this.rcStatus == RcStatus.finish || this.rcStatus == RcStatus.error) {
            return false;
        }
        breakOutDone();
        return true;
    }
}
