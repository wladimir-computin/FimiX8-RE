package com.fimi.app.x8s.controls.fcsettting;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.widget.X8AircrftCalibrationIndicatorView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.cmdsenum.X8Cali.CaliCmd;
import com.fimi.x8sdk.cmdsenum.X8Cali.CaliMode;
import com.fimi.x8sdk.cmdsenum.X8Cali.CaliStep;
import com.fimi.x8sdk.cmdsenum.X8Cali.CaliStepStatus;
import com.fimi.x8sdk.cmdsenum.X8Cali.CaliType;
import com.fimi.x8sdk.cmdsenum.X8Cali.SensorType;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetCaliState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AircraftCalibrationController extends AbsX8MenuBoxControllers implements onDialogButtonClickListener {
    private final int FIFTH_DIR_STEP = 128;
    private final int FOURTH_DIR_STEP = 4;
    private final int FRIST_DIR_STEP = 32;
    private final int SECOND_DIR_STEP = 8;
    private final int SIXTH_DIR_STEP = 64;
    private final int THRID_DIR_STEP = 16;
    private ImageView back_btn;
    private X8DoubleCustomDialog dialog;
    private FcCtrlManager fcManager;
    private boolean[] indicatorArray = new boolean[]{false, false, false, false, false, false};
    private boolean isCaltAccSixPoint = false;
    private boolean isResetCalit = false;
    private boolean isShowCalibrationStart = false;
    private Button mBtnFailureCalibration;
    private Button mBtnStartCalibration;
    private Button mBtnSuccessCalibration;
    private View mCalibrationDisconnectView;
    private View mCalibrationFailureView;
    private X8AircrftCalibrationIndicatorView mCalibrationIndicator;
    private View mCalibrationPreView;
    private View mCalibrationStepView;
    private View mCalibrationSuccessedView;
    private ViewStub mDisconnectViewStub;
    private ViewStub mFailureViewStub;
    private GaliStete mGaliStete = GaliStete.IDLE;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!StateManager.getInstance().getX8Drone().isConnect()) {
                if (((!X8AircraftCalibrationController.this.isCaltAccSixPoint ? 1 : 0) & (!X8AircraftCalibrationController.this.isSoftInterrupt() ? 1 : 0)) != 0) {
                    X8AircraftCalibrationController.this.handleCalibrationDisconnect();
                    X8AircraftCalibrationController.this.mGaliStete = GaliStete.IDLE;
                    X8AircraftCalibrationController.this.isShowCalibrationStart = true;
                }
            } else if (X8AircraftCalibrationController.this.isShowCalibrationStart) {
                X8AircraftCalibrationController.this.handleCalibrationStart();
                X8AircraftCalibrationController.this.isResetCalit = true;
                X8AircraftCalibrationController.this.isShowCalibrationStart = false;
            }
            X8AircraftCalibrationController.this.extractCalibrationData();
            X8AircraftCalibrationController.this.mHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };
    private ImageView mIvCalibrationStepPicture;
    private ViewStub mPreViewStub;
    private ViewStub mSuccessedViewStub;
    private TextView mTvCalibrationDisconnectionDescribe;
    private TextView mTvCalibrationFailureDescribe;
    private TextView mTvCalibrationStepDescribe;
    private TextView mTvCalibrationTitle;
    private ViewStub mViewStepStub;
    private IX8GeneraModifyModeControllerListener modeControllerListener;

    public enum GaliStete {
        IDLE,
        SEND_CALI_SIX_POINT,
        GET_CALI_SIX_POINT,
        SEND_CAIL_ORTH,
        GET_CAIL_ORTH,
        SEND_RESTART_CALI_SIX_POINT
    }

    public X8AircraftCalibrationController(View rootView) {
        super(rootView);
    }

    public void setModeControllerListener(IX8GeneraModifyModeControllerListener modeControllerListener) {
        this.modeControllerListener = modeControllerListener;
    }

    public void setFcManager(FcCtrlManager fcManager) {
        this.fcManager = fcManager;
    }

    private void extractCalibrationData() {
        switch (this.mGaliStete) {
            case GET_CALI_SIX_POINT:
                getSixPointData();
                return;
            case GET_CAIL_ORTH:
                getHorizonalData();
                return;
            case SEND_CAIL_ORTH:
                startHorizonalCalibration();
                return;
            case SEND_CALI_SIX_POINT:
                startCalibration();
                return;
            case SEND_RESTART_CALI_SIX_POINT:
                reStartCalibration();
                return;
            default:
                return;
        }
    }

    private void showCalibrationStatus(AckGetCaliState ackGetCaliState) {
        int caliStep = ackGetCaliState.getCaliStep() & 255;
        if (ackGetCaliState.getStatus() == CaliStepStatus.CALI_STA_ERR.ordinal()) {
            quitCalibration();
            this.mGaliStete = GaliStete.IDLE;
            handleCalibrationFailure(1);
        }
        switch (caliStep) {
            case 4:
                if (ackGetCaliState.getStatus() != CaliStepStatus.CALI_STA_ERR.ordinal()) {
                    handleCalibrationStep4();
                    showCalibrationIndicator(ackGetCaliState, 5);
                    if (ackGetCaliState.getSecondPointPercent() == 100) {
                        this.mCalibrationIndicator.stopFlick();
                        jumpNextStep();
                        break;
                    }
                }
                quitCalibration();
                this.mGaliStete = GaliStete.IDLE;
                handleCalibrationFailure(6);
                return;
                break;
            case 8:
                if (ackGetCaliState.getStatus() != CaliStepStatus.CALI_STA_ERR.ordinal()) {
                    handleCalibrationStep2();
                    showCalibrationIndicator(ackGetCaliState, 3);
                    if (ackGetCaliState.getFirstPointPercent() == 100) {
                        this.mCalibrationIndicator.stopFlick();
                        jumpNextStep();
                        break;
                    }
                }
                quitCalibration();
                this.mGaliStete = GaliStete.IDLE;
                handleCalibrationFailure(4);
                return;
                break;
            case 16:
                if (ackGetCaliState.getStatus() != CaliStepStatus.CALI_STA_ERR.ordinal()) {
                    handleCalibrationStep3();
                    showCalibrationIndicator(ackGetCaliState, 4);
                    if (ackGetCaliState.getFourthPointPercent() == 100) {
                        this.mCalibrationIndicator.stopFlick();
                        jumpNextStep();
                        break;
                    }
                }
                quitCalibration();
                this.mGaliStete = GaliStete.IDLE;
                handleCalibrationFailure(5);
                return;
                break;
            case 32:
                if (ackGetCaliState.getStatus() != CaliStepStatus.CALI_STA_ERR.ordinal()) {
                    handleCalibrationStep1();
                    showCalibrationIndicator(ackGetCaliState, 2);
                    if (ackGetCaliState.getThridPointPercent() == 100) {
                        this.mCalibrationIndicator.stopFlick();
                        jumpNextStep();
                        break;
                    }
                }
                quitCalibration();
                this.mGaliStete = GaliStete.IDLE;
                handleCalibrationFailure(3);
                return;
                break;
            case 64:
                if (ackGetCaliState.getStatus() != CaliStepStatus.CALI_STA_ERR.ordinal()) {
                    handleCalibrationStep6();
                    showCalibrationIndicator(ackGetCaliState, 0);
                    if (ackGetCaliState.getSixthPointPercent() == 100) {
                        this.mCalibrationIndicator.stopFlick();
                        jumpNextStep();
                        break;
                    }
                }
                quitCalibration();
                this.mGaliStete = GaliStete.IDLE;
                handleCalibrationFailure(1);
                return;
                break;
            case 128:
                if (ackGetCaliState.getStatus() != CaliStepStatus.CALI_STA_ERR.ordinal()) {
                    handleCalibrationStep5();
                    showCalibrationIndicator(ackGetCaliState, 1);
                    if (ackGetCaliState.getFifthPointPercent() == 100) {
                        this.mCalibrationIndicator.stopFlick();
                        jumpNextStep();
                        break;
                    }
                }
                quitCalibration();
                this.mGaliStete = GaliStete.IDLE;
                handleCalibrationFailure(2);
                return;
                break;
        }
        if (ackGetCaliState.getFirstPointPercent() == 100 && ackGetCaliState.getSecondPointPercent() == 100 && ackGetCaliState.getThridPointPercent() == 100 && ackGetCaliState.getFourthPointPercent() == 100 && ackGetCaliState.getFifthPointPercent() == 100 && ackGetCaliState.getSixthPointPercent() == 100) {
            this.isShowCalibrationStart = false;
            jumpNextStep();
            quitCalibration();
            this.mGaliStete = GaliStete.SEND_CAIL_ORTH;
        }
    }

    private void getSixPointData() {
        this.isCaltAccSixPoint = false;
        this.fcManager.getAircrftCalibrationState(SensorType.IMUM.ordinal(), CaliType.CALI_ACC_SIX_POINT.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (o != null) {
                    AckGetCaliState ackGetCaliState = (AckGetCaliState) o;
                    X8AircraftCalibrationController.this.isCaltAccSixPoint = true;
                    X8AircraftCalibrationController.this.showCalibrationStatus(ackGetCaliState);
                }
            }
        });
    }

    private void getHorizonalData() {
        this.fcManager.getAircrftCalibrationState(SensorType.IMUM.ordinal(), CaliType.CALI_IMU_ORTH.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (o != null) {
                    AckGetCaliState ackGetCaliState = (AckGetCaliState) o;
                    if (ackGetCaliState.getStatus() == CaliStepStatus.CALI_STA_ERR.ordinal()) {
                        X8AircraftCalibrationController.this.quitHorizonalCalibration();
                        X8AircraftCalibrationController.this.mGaliStete = GaliStete.IDLE;
                        X8AircraftCalibrationController.this.handleCalibrationFailure(7);
                    } else if (ackGetCaliState.getStatus() == CaliStepStatus.CALI_STA_DONE.ordinal()) {
                        X8AircraftCalibrationController.this.mCalibrationIndicator.stopFlick();
                        X8AircraftCalibrationController.this.quitHorizonalCalibration();
                        X8AircraftCalibrationController.this.mGaliStete = GaliStete.IDLE;
                        X8AircraftCalibrationController.this.handleCalibrationSuccessed();
                    } else if (ackGetCaliState.getCaliStep() == CaliStep.CALI_STEP_ORTH.ordinal()) {
                        X8AircraftCalibrationController.this.mCalibrationIndicator.setSelected(6);
                    }
                }
            }
        });
    }

    private void startCalibration() {
        for (int i = 0; i < this.indicatorArray.length; i++) {
            this.indicatorArray[i] = false;
        }
        this.mCalibrationIndicator.setStepStatus(this.indicatorArray, -1);
        handleCalibrationStep6();
        quitHorizonalCalibration();
        quitCalibration();
        this.fcManager.setAircrftCalibrationStart(CaliType.CALI_ACC_SIX_POINT.ordinal(), CaliCmd.CALI_CMD_START.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess) {
                    X8AircraftCalibrationController.this.mGaliStete = GaliStete.GET_CALI_SIX_POINT;
                    X8AircraftCalibrationController.this.mHandler.sendEmptyMessage(0);
                    StateManager.getInstance().getX8Drone().setOutTime(5000);
                    return;
                }
                X8AircraftCalibrationController.this.quitCalibration();
            }
        });
    }

    private void quitHorizonalCalibration() {
        this.fcManager.setAircrftCalibrationStart(CaliType.CALI_IMU_ORTH.ordinal(), CaliCmd.CALI_CMD_QUIT.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
            }
        });
    }

    private void startHorizonalCalibration() {
        quitCalibration();
        this.fcManager.setAircrftCalibrationStart(CaliType.CALI_IMU_ORTH.ordinal(), CaliCmd.CALI_CMD_START.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess) {
                    X8AircraftCalibrationController.this.mGaliStete = GaliStete.GET_CAIL_ORTH;
                    X8AircraftCalibrationController.this.mHandler.sendEmptyMessage(0);
                    StateManager.getInstance().getX8Drone().setOutTime(5000);
                    return;
                }
                X8AircraftCalibrationController.this.quitHorizonalCalibration();
            }
        });
    }

    private void quitCalibration() {
        this.fcManager.setAircrftCalibrationStart(CaliType.CALI_ACC_SIX_POINT.ordinal(), CaliCmd.CALI_CMD_QUIT.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                X8AircraftCalibrationController.this.isCaltAccSixPoint = false;
            }
        });
    }

    private void reStartCalibration() {
        quitCalibration();
        for (int i = 0; i < this.indicatorArray.length; i++) {
            this.indicatorArray[i] = false;
        }
        this.isResetCalit = false;
        this.mCalibrationIndicator.setStepStatus(this.indicatorArray, -1);
        handleCalibrationStep6();
        this.mGaliStete = GaliStete.SEND_RESTART_CALI_SIX_POINT;
        this.fcManager.setAircrftCalibrationStart(CaliType.CALI_ACC_SIX_POINT.ordinal(), CaliCmd.CALI_CMD_RESTART.ordinal(), CaliMode.CALI_MODE_MANUALLY.ordinal(), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess) {
                    X8AircraftCalibrationController.this.mHandler.sendEmptyMessage(0);
                    X8AircraftCalibrationController.this.mGaliStete = GaliStete.GET_CALI_SIX_POINT;
                    StateManager.getInstance().getX8Drone().setOutTime(5000);
                    return;
                }
                X8AircraftCalibrationController.this.quitCalibration();
                X8AircraftCalibrationController.this.mGaliStete = GaliStete.IDLE;
                X8AircraftCalibrationController.this.handleCalibrationFailure(1);
            }
        });
    }

    public void initViews(View rootView) {
        this.handleView = rootView.findViewById(R.id.x8_rl_main_mdify_calibration_layout);
        this.back_btn = (ImageView) this.handleView.findViewById(R.id.btn_calibration_return);
        this.mTvCalibrationTitle = (TextView) this.handleView.findViewById(R.id.tv_calibration_title);
        this.mPreViewStub = (ViewStub) this.handleView.findViewById(R.id.vs_aircraft_calibration_pre);
        this.mCalibrationPreView = this.mPreViewStub.inflate();
        this.mCalibrationPreView.setVisibility(8);
        this.mDisconnectViewStub = (ViewStub) this.handleView.findViewById(R.id.vs_aircraft_calibration_disconnect);
        this.mCalibrationDisconnectView = this.mDisconnectViewStub.inflate();
        this.mCalibrationDisconnectView.setVisibility(0);
        this.mViewStepStub = (ViewStub) this.handleView.findViewById(R.id.vs_aircraft_calibration_step);
        this.mCalibrationStepView = this.mViewStepStub.inflate();
        this.mCalibrationStepView.setVisibility(8);
        this.mFailureViewStub = (ViewStub) this.handleView.findViewById(R.id.vs_aircraft_calibration_failure);
        this.mCalibrationFailureView = this.mFailureViewStub.inflate();
        this.mCalibrationFailureView.setVisibility(8);
        this.mSuccessedViewStub = (ViewStub) this.handleView.findViewById(R.id.vs_aircraft_calibration_successed);
        this.mCalibrationSuccessedView = this.mSuccessedViewStub.inflate();
        this.mCalibrationSuccessedView.setVisibility(8);
        this.mBtnStartCalibration = (Button) this.handleView.findViewById(R.id.btn_start_calibration_toggle);
        this.mBtnFailureCalibration = (Button) this.handleView.findViewById(R.id.btn_failure_calibration_toggle);
        this.mBtnSuccessCalibration = (Button) this.handleView.findViewById(R.id.btn_success_calibration_toggle);
        this.mIvCalibrationStepPicture = (ImageView) this.handleView.findViewById(R.id.iv_calibration_step_picture);
        this.mTvCalibrationStepDescribe = (TextView) this.handleView.findViewById(R.id.tv_calibration_step1_describe);
        this.mCalibrationIndicator = (X8AircrftCalibrationIndicatorView) this.handleView.findViewById(R.id.view_aircrft_Calibration_Indicator);
        this.mTvCalibrationFailureDescribe = (TextView) this.handleView.findViewById(R.id.tv_calibration_failure_describe);
        this.mTvCalibrationDisconnectionDescribe = (TextView) this.handleView.findViewById(R.id.tv_calibration_disconnection_describe);
    }

    public void initActions() {
        this.back_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (X8AircraftCalibrationController.this.isCaltAccSixPoint) {
                    if (X8AircraftCalibrationController.this.dialog == null) {
                        X8AircraftCalibrationController.this.dialog = new X8DoubleCustomDialog(X8AircraftCalibrationController.this.rootView.getContext(), X8AircraftCalibrationController.this.rootView.getResources().getString(R.string.x8_modify_aircraft_calibration), X8AircraftCalibrationController.this.rootView.getResources().getString(R.string.x8_modify_aircraft_calibration_exit), X8AircraftCalibrationController.this);
                    }
                    X8AircraftCalibrationController.this.dialog.show();
                    return;
                }
                X8AircraftCalibrationController.this.quitItem();
            }
        });
        this.mBtnStartCalibration.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (X8AircraftCalibrationController.this.isResetCalit) {
                    X8AircraftCalibrationController.this.mGaliStete = GaliStete.SEND_RESTART_CALI_SIX_POINT;
                    X8AircraftCalibrationController.this.reStartCalibration();
                    return;
                }
                X8AircraftCalibrationController.this.mGaliStete = GaliStete.SEND_CALI_SIX_POINT;
                X8AircraftCalibrationController.this.startCalibration();
            }
        });
        this.mBtnFailureCalibration.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8AircraftCalibrationController.this.mGaliStete = GaliStete.SEND_RESTART_CALI_SIX_POINT;
                X8AircraftCalibrationController.this.reStartCalibration();
            }
        });
        this.mBtnSuccessCalibration.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8AircraftCalibrationController.this.quitItem();
            }
        });
    }

    public void onLeft() {
    }

    public void onRight() {
        this.mGaliStete = GaliStete.IDLE;
        quitCalibration();
        quitItem();
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (this.isInSky) {
            handleInsky();
        } else if (b) {
            handleCalibrationStart();
        } else {
            handleCalibrationDisconnect();
        }
    }

    public void defaultVal() {
    }

    public void showItem() {
        super.showItem();
        this.handleView.setVisibility(0);
        getDroneState();
        onDroneConnected(this.isConect);
        this.mHandler.sendEmptyMessageDelayed(0, 10);
    }

    private void quitItem() {
        this.handleView.setVisibility(8);
        this.modeControllerListener.returnBack();
        this.modeControllerListener.onClose();
        for (int i = 0; i < this.indicatorArray.length; i++) {
            this.indicatorArray[i] = false;
        }
        this.mCalibrationIndicator.setStepStatus(this.indicatorArray, 0);
        this.mCalibrationIndicator.stopFlick();
        this.isShowCalibrationStart = false;
        this.isCaltAccSixPoint = false;
        this.isResetCalit = false;
        this.mGaliStete = GaliStete.IDLE;
        closeItem();
        StateManager.getInstance().getX8Drone().setOutTime(1500);
    }

    private void handleCalibrationStart() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration));
        this.mCalibrationPreView.setVisibility(0);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(8);
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleCalibrationDisconnect() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(0);
        this.mTvCalibrationDisconnectionDescribe.setText(getIdString(R.string.x8_modify_start_calibration_describe2));
        this.mCalibrationStepView.setVisibility(8);
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleInsky() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(0);
        this.mTvCalibrationDisconnectionDescribe.setText(getIdString(R.string.x8_modify_start_calibration_describe4));
        this.mCalibrationStepView.setVisibility(8);
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleCalibrationStep1() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration3));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(0);
        this.mIvCalibrationStepPicture.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_calibrate_drone_step1));
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleCalibrationStep2() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration4));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(0);
        this.mIvCalibrationStepPicture.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_calibrate_drone_step2));
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleCalibrationStep3() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration5));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(0);
        this.mIvCalibrationStepPicture.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_calibrate_drone_step3));
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleCalibrationStep4() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration6));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(0);
        this.mIvCalibrationStepPicture.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_calibrate_drone_step4));
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleCalibrationStep5() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration2));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(0);
        this.mIvCalibrationStepPicture.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_calibrate_drone_step5));
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleCalibrationStep6() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration1));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(0);
        this.mIvCalibrationStepPicture.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_calibrate_drone_step6));
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleCalibrationHorizonal() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration_horizonal));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(0);
        this.mIvCalibrationStepPicture.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_calibrate_drone_step6));
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(8);
    }

    private void handleCalibrationFailure(int point) {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(8);
        this.mCalibrationFailureView.setVisibility(0);
        this.mCalibrationSuccessedView.setVisibility(8);
        if (point == 1) {
            this.mTvCalibrationFailureDescribe.setText(getIdString(R.string.x8_modify_aircraft_calibration1_failure));
        } else if (point == 2) {
            this.mTvCalibrationFailureDescribe.setText(getIdString(R.string.x8_modify_aircraft_calibration2_failure));
        } else if (point == 3) {
            this.mTvCalibrationFailureDescribe.setText(getIdString(R.string.x8_modify_aircraft_calibration3_failure));
        } else if (point == 4) {
            this.mTvCalibrationFailureDescribe.setText(getIdString(R.string.x8_modify_aircraft_calibration4_failure));
        } else if (point == 5) {
            this.mTvCalibrationFailureDescribe.setText(getIdString(R.string.x8_modify_aircraft_calibration5_failure));
        } else if (point == 6) {
            this.mTvCalibrationFailureDescribe.setText(getIdString(R.string.x8_modify_aircraft_calibration6_failure));
        } else if (point == 7) {
            this.mTvCalibrationFailureDescribe.setText(getIdString(R.string.x8_modify_aircraft_calibration_horizonal_failure));
        } else {
            this.mTvCalibrationFailureDescribe.setText(getIdString(R.string.x8_modify_aircraft_calibration1_failure));
        }
    }

    private void handleCalibrationSuccessed() {
        this.mTvCalibrationTitle.setText(getIdString(R.string.x8_modify_aircraft_calibration));
        this.mCalibrationPreView.setVisibility(8);
        this.mCalibrationDisconnectView.setVisibility(8);
        this.mCalibrationStepView.setVisibility(8);
        this.mCalibrationFailureView.setVisibility(8);
        this.mCalibrationSuccessedView.setVisibility(0);
    }

    private void showCalibrationIndicator(AckGetCaliState ackGetCaliState, int i) {
        boolean z;
        boolean z2 = true;
        this.indicatorArray[0] = ackGetCaliState.getSixthPointPercent() == 100;
        boolean[] zArr = this.indicatorArray;
        if (ackGetCaliState.getFifthPointPercent() == 100) {
            z = true;
        } else {
            z = false;
        }
        zArr[1] = z;
        zArr = this.indicatorArray;
        if (ackGetCaliState.getThridPointPercent() == 100) {
            z = true;
        } else {
            z = false;
        }
        zArr[2] = z;
        zArr = this.indicatorArray;
        if (ackGetCaliState.getFirstPointPercent() == 100) {
            z = true;
        } else {
            z = false;
        }
        zArr[3] = z;
        zArr = this.indicatorArray;
        if (ackGetCaliState.getFourthPointPercent() == 100) {
            z = true;
        } else {
            z = false;
        }
        zArr[4] = z;
        boolean[] zArr2 = this.indicatorArray;
        if (ackGetCaliState.getSecondPointPercent() != 100) {
            z2 = false;
        }
        zArr2[5] = z2;
        this.mCalibrationIndicator.setStepStatus(this.indicatorArray, i);
        this.mCalibrationIndicator.setSelected(i);
    }

    private boolean isSoftInterrupt() {
        int count = 0;
        for (boolean z : this.indicatorArray) {
            if (z) {
                count++;
            }
        }
        if (count >= 4) {
            return true;
        }
        return false;
    }

    private void jumpNextStep() {
        if (!this.indicatorArray[0]) {
            handleCalibrationStep6();
            this.mCalibrationIndicator.setStepStatus(this.indicatorArray, -1);
        } else if (!this.indicatorArray[1]) {
            handleCalibrationStep5();
            this.mCalibrationIndicator.setStepStatus(this.indicatorArray, -1);
        } else if (!this.indicatorArray[2]) {
            handleCalibrationStep1();
            this.mCalibrationIndicator.setStepStatus(this.indicatorArray, -1);
        } else if (!this.indicatorArray[3]) {
            handleCalibrationStep2();
            this.mCalibrationIndicator.setStepStatus(this.indicatorArray, -1);
        } else if (!this.indicatorArray[4]) {
            handleCalibrationStep3();
            this.mCalibrationIndicator.setStepStatus(this.indicatorArray, -1);
        } else if (this.indicatorArray[5]) {
            handleCalibrationHorizonal();
            this.mCalibrationIndicator.setStepStatus(this.indicatorArray, -1);
        } else {
            handleCalibrationStep4();
            this.mCalibrationIndicator.setStepStatus(this.indicatorArray, -1);
        }
    }

    private String getIdString(int id) {
        return this.handleView.getContext().getString(id);
    }
}
