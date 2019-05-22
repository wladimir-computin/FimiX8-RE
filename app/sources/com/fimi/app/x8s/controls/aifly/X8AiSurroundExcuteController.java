package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.X8AiTrackController.OnAiTrackControllerListener;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiSurroundToPointExcuteConfirmModule;
import com.fimi.app.x8s.enums.X8AiSuroundState;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiSurroundExcuteControllerListener;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8FollowSpeedContainerView;
import com.fimi.app.x8s.widget.X8FollowSpeedContainerView.OnSendSpeedListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AckAiSurrounds;
import com.fimi.x8sdk.dataparser.AckGetAiSurroundPoint;
import com.fimi.x8sdk.dataparser.AckNormalCmds;
import com.fimi.x8sdk.dataparser.AutoAiSurroundState;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AiSurroundExcuteController extends AbsX8AiController implements OnClickListener, onDialogButtonClickListener, OnAiTrackControllerListener, OnSendSpeedListener {
    private static int MIN = 0;
    protected int MAX_WIDTH;
    private X8sMainActivity activity;
    private View blank;
    private X8DoubleCustomDialog dialog;
    private FcManager fcManager;
    private View flagSmall;
    private ImageView imgBack;
    private ImageView imgSuroundBg;
    private ImageView imgVcToggle;
    private boolean isChangRadius;
    private boolean isDraw;
    private boolean isGetPoint;
    private boolean isGetSpeed;
    protected boolean isNextShow;
    private boolean isSetCircle;
    private double lastLat;
    private double lastLog;
    private double lat;
    private double lat1;
    private IX8AiSurroundExcuteControllerListener listener;
    private double log;
    private double log1;
    private IX8NextViewListener mIX8NextViewListener = new IX8NextViewListener() {
        public void onBackClick() {
            X8AiSurroundExcuteController.this.closeNextUiFromNext(true);
        }

        public void onExcuteClick() {
            X8AiSurroundExcuteController.this.closeNextUi(false);
            X8AiSurroundExcuteController.this.lat = X8AiSurroundExcuteController.this.lastLat;
            X8AiSurroundExcuteController.this.log = X8AiSurroundExcuteController.this.lastLog;
            X8AiSurroundExcuteController.this.isDraw = true;
            X8AiSurroundExcuteController.this.isGetPoint = true;
            X8AiSurroundExcuteController.this.isGetSpeed = true;
            X8AiSurroundExcuteController.this.imgSuroundBg.setVisibility(8);
            X8AiSurroundExcuteController.this.mX8AiSuroundState = X8AiSuroundState.RUNNING;
            X8AiSurroundExcuteController.this.tvTip.setVisibility(8);
            X8AiSurroundExcuteController.this.vSpeed.setVisibility(0);
            X8AiSurroundExcuteController.this.openVcToggle();
            X8AiSurroundExcuteController.this.listener.onSurroundRunning();
        }

        public void onSaveClick() {
        }
    };
    private TextView mTvRadius;
    private X8AiSuroundState mX8AiSuroundState = X8AiSuroundState.IDLE;
    private X8AiSurroundToPointExcuteConfirmModule mX8AiSurroundToPointExcuteConfirmModule;
    private View nextRootView;
    private float r;
    private float radius = 50.0f;
    private int speed;
    private int timeSend = 0;
    private TextView tvP2PTip;
    private TextView tvPoint;
    private X8AiTipWithCloseView tvTip;
    private View vRadiusBg;
    private X8FollowSpeedContainerView vSpeed;
    protected int width = X8Application.ANIMATION_WIDTH;

    public X8AiSurroundExcuteController(X8sMainActivity activity, View rootView, X8AiSuroundState state) {
        super(rootView);
        this.activity = activity;
        this.mX8AiSuroundState = state;
    }

    public void setListener(IX8AiSurroundExcuteControllerListener listener) {
        this.listener = listener;
    }

    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    public void initActions() {
        if (this.handleView != null) {
            this.tvPoint.setOnClickListener(this);
            this.imgBack.setOnClickListener(this);
            this.blank.setOnClickListener(this);
            this.imgVcToggle.setOnClickListener(this);
        }
    }

    public void defaultVal() {
    }

    public void showExitDialog() {
        if (this.dialog == null) {
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_ai_fly_surround_to_point), this.rootView.getContext().getString(R.string.x8_ai_fly_surround_eixte), this);
        }
        this.dialog.show();
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            if (this.mX8AiSuroundState == X8AiSuroundState.RUNNING) {
                showExitDialog();
            } else {
                closeSurround();
            }
        } else if (id == R.id.img_ai_set_dot) {
            if (this.mX8AiSuroundState == X8AiSuroundState.IDLE) {
                setCirclePoint();
            } else if (this.mX8AiSuroundState == X8AiSuroundState.SET_CIRCLE_POINT) {
                String s;
                if (this.radius > 500.0f) {
                    s = X8NumberUtil.getDistanceNumberString(500.0f, 0, true);
                    X8ToastUtil.showToast(this.activity, String.format(this.activity.getString(R.string.x8_ai_surround_radius_tip2), new Object[]{s}), 0);
                } else if (this.radius < 5.0f) {
                    s = X8NumberUtil.getDistanceNumberString(5.0f, 0, true);
                    X8ToastUtil.showToast(this.activity, String.format(this.activity.getString(R.string.x8_ai_surround_radius_tip1), new Object[]{s}), 0);
                } else {
                    setTakeOffPoint();
                }
            } else if (this.mX8AiSuroundState != X8AiSuroundState.SET_TAKE_OFF_POINT && this.mX8AiSuroundState == X8AiSuroundState.SET_PARAMETER) {
                openNextUi();
                this.vRadiusBg.setVisibility(8);
            }
        } else if (id == R.id.x8_main_ai_ai_surround_next_blank) {
            closeNextUiFromNext(true);
        } else if (id == R.id.img_vc_targgle) {
            if (this.imgVcToggle.isSelected()) {
                setAiVcClose();
            } else {
                setAiVcOpen();
            }
        } else if (id != R.id.rl_flag_small) {
        } else {
            if (this.tvP2PTip.getVisibility() == 0) {
                this.tvP2PTip.setVisibility(8);
            } else {
                this.tvP2PTip.setVisibility(0);
            }
        }
    }

    public void openUi() {
        this.isShow = true;
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_ai_surround_layout, (ViewGroup) this.rootView, true);
        this.tvPoint = (TextView) this.handleView.findViewById(R.id.img_ai_set_dot);
        this.imgBack = (ImageView) this.handleView.findViewById(R.id.img_ai_follow_back);
        this.tvP2PTip = (TextView) this.handleView.findViewById(R.id.img_ai_p2p_tip);
        this.mTvRadius = (TextView) this.handleView.findViewById(R.id.tv_ai_radius);
        this.imgSuroundBg = (ImageView) this.handleView.findViewById(R.id.img_ai_suround_bg);
        this.vRadiusBg = this.handleView.findViewById(R.id.rl_x8_ai_surround_radius);
        this.imgVcToggle = (ImageView) this.handleView.findViewById(R.id.img_vc_targgle);
        this.tvTip = (X8AiTipWithCloseView) this.handleView.findViewById(R.id.v_content_tip);
        this.tvTip.setTipText(this.handleView.getContext().getString(R.string.x8_ai_surround_select_point));
        this.vSpeed = (X8FollowSpeedContainerView) this.handleView.findViewById(R.id.v_surround_speed);
        this.vSpeed.setMaxMin(100, MIN, 1);
        this.vSpeed.setOnSendSpeedListener(this);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.flagSmall.setOnClickListener(this);
        this.nextRootView = this.rootView.findViewById(R.id.x8_main_ai_surround_next_content);
        this.blank = this.rootView.findViewById(R.id.x8_main_ai_ai_surround_next_blank);
        this.mX8AiSurroundToPointExcuteConfirmModule = new X8AiSurroundToPointExcuteConfirmModule();
        initActions();
        this.activity.getmX8AiTrackController().setOnAiTrackControllerListener(this);
        if (this.mX8AiSuroundState != X8AiSuroundState.IDLE) {
            this.isDraw = false;
            this.imgSuroundBg.setVisibility(8);
            this.tvPoint.setVisibility(8);
            this.mTvRadius.setVisibility(8);
            this.tvTip.setVisibility(8);
        } else {
            this.vRadiusBg.setVisibility(8);
            this.tvTip.showTip();
            this.isDraw = true;
        }
        if (this.mX8AiSuroundState == X8AiSuroundState.RUNNING) {
            openVcToggle();
            this.listener.onSurroundRunning();
        }
        super.openUi();
    }

    public void closeUi() {
        this.mX8AiSuroundState = X8AiSuroundState.IDLE;
        this.isShow = false;
        this.activity.getmX8AiTrackController().closeUi();
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().clearSurroundMarker();
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().resetMapEvent();
        setAiVcClose();
        super.closeUi();
    }

    public void openNextUi() {
        this.nextRootView.setVisibility(0);
        this.blank.setVisibility(0);
        closeIconByNextUi();
        this.mX8AiSurroundToPointExcuteConfirmModule.init(this.activity, this.nextRootView, this.radius);
        if (this.mX8AiSurroundToPointExcuteConfirmModule != null) {
            this.mX8AiSurroundToPointExcuteConfirmModule.setListener(this.mIX8NextViewListener, this.fcManager, this);
        }
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", new float[]{(float) this.width, 0.0f});
            animatorY.setDuration(300);
            animatorY.start();
        }
    }

    public void closeNextUi(final boolean b) {
        this.blank.setVisibility(8);
        if (this.isNextShow) {
            this.isNextShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.nextRootView, "translationX", new float[]{0.0f, (float) this.width});
            translationRight.setDuration(300);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8AiSurroundExcuteController.this.nextRootView.setVisibility(8);
                    ((ViewGroup) X8AiSurroundExcuteController.this.nextRootView).removeAllViews();
                    X8AiSurroundExcuteController.this.imgBack.setVisibility(0);
                    X8AiSurroundExcuteController.this.flagSmall.setVisibility(0);
                    if (b) {
                        X8AiSurroundExcuteController.this.tvPoint.setVisibility(0);
                    }
                }
            });
        }
    }

    public void setFcManager(FcManager fcManager) {
        this.fcManager = fcManager;
    }

    public void setCirclePoint() {
        this.lastLog = StateManager.getInstance().getX8Drone().getLongitude();
        this.lastLat = StateManager.getInstance().getX8Drone().getLatitude();
        float alt = StateManager.getInstance().getX8Drone().getHeight();
        this.tvPoint.setText(this.activity.getString(R.string.x8_ai_fly_follow_surround_set_takeoff_point));
        this.tvTip.setTipText(this.activity.getString(R.string.x8_ai_surround_select_point2));
        this.mTvRadius.setText(String.format(this.activity.getString(R.string.x8_ai_surround_radius), new Object[]{X8NumberUtil.getDistanceNumberString(0.0f, 0, true)}));
        this.vRadiusBg.setVisibility(0);
        this.mX8AiSuroundState = X8AiSuroundState.SET_CIRCLE_POINT;
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().setAiSurroundMark(this.lastLat, this.lastLog);
    }

    private void setTakeOffPoint() {
        if (this.isSetCircle) {
            this.vRadiusBg.setVisibility(8);
            this.tvPoint.setVisibility(8);
            this.imgBack.setVisibility(8);
            openNextUi();
            return;
        }
        double log = StateManager.getInstance().getX8Drone().getLongitude();
        double lat = StateManager.getInstance().getX8Drone().getLatitude();
        float alt = StateManager.getInstance().getX8Drone().getHeight();
        if (alt >= 5.0f) {
            this.mX8AiSuroundState = X8AiSuroundState.SET_TAKE_OFF_POINT;
            this.fcManager.setAiSurroundCiclePoint(this.lastLog, this.lastLat, alt, log, lat, alt, 1, new UiCallBackListener<AckNormalCmds>() {
                public void onComplete(CmdResult cmdResult, AckNormalCmds o) {
                    if (cmdResult.isSuccess()) {
                        X8AiSurroundExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().setAiSurroundCircle(X8AiSurroundExcuteController.this.lastLat, X8AiSurroundExcuteController.this.lastLog, X8AiSurroundExcuteController.this.radius);
                        X8AiSurroundExcuteController.this.openNextUi();
                        X8AiSurroundExcuteController.this.vRadiusBg.setVisibility(8);
                        X8AiSurroundExcuteController.this.tvPoint.setVisibility(8);
                        X8AiSurroundExcuteController.this.imgBack.setVisibility(8);
                        X8AiSurroundExcuteController.this.isSetCircle = true;
                        return;
                    }
                    X8AiSurroundExcuteController.this.mX8AiSuroundState = X8AiSuroundState.SET_CIRCLE_POINT;
                }
            });
            return;
        }
        X8ToastUtil.showToast(this.activity, String.format(this.activity.getString(R.string.height_tip), new Object[]{X8NumberUtil.getDistanceNumberString(5.0f, 0, true)}), 0);
    }

    public float getCurrentDistance() {
        return this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().getSurroundRadius(this.lastLog, this.lastLat, StateManager.getInstance().getX8Drone().getLongitude(), StateManager.getInstance().getX8Drone().getLatitude());
    }

    public void setDeviceRadius() {
        this.radius = getCurrentDistance();
        int r = Math.round(this.radius);
        this.mTvRadius.setText(String.format(this.activity.getString(R.string.x8_ai_surround_radius), new Object[]{X8NumberUtil.getDistanceNumberString((float) r, 0, true)}));
    }

    public void cancleByModeChange(int mode) {
        boolean z = true;
        if (mode != 1) {
            z = false;
        }
        onTaskComplete(z);
    }

    public void onTaskComplete(boolean showText) {
        closeSurround();
        if (this.listener != null) {
            this.listener.onSurroundComplete(showText);
        }
    }

    public void ononDroneDisconnectedTaskComplete() {
        closeSurround();
        if (this.listener != null) {
            this.listener.onSurroundComplete(false);
        }
    }

    public void taskExit() {
        onTaskComplete(false);
    }

    public void sendExiteCmd() {
        this.fcManager.setAiSurroundExite(new UiCallBackListener<AckNormalCmds>() {
            public void onComplete(CmdResult cmdResult, AckNormalCmds o) {
                if (cmdResult.isSuccess()) {
                    X8AiSurroundExcuteController.this.taskExit();
                }
            }
        });
    }

    private void closeNextUiFromNext(boolean b) {
        closeNextUi(b);
        this.vRadiusBg.setVisibility(0);
        this.tvPoint.setText(this.activity.getString(R.string.x8_ai_fly_follow_surround_mext));
        this.mX8AiSuroundState = X8AiSuroundState.SET_PARAMETER;
    }

    private void closeSurround() {
        closeUi();
        if (this.listener != null) {
            this.listener.onSurroundBackClick();
        }
    }

    public void getPoint() {
        this.fcManager.getAiSurroundCiclePoint(new UiCallBackListener<AckGetAiSurroundPoint>() {
            public void onComplete(CmdResult cmdResult, AckGetAiSurroundPoint ackGetAiSurroundPoint) {
                if (cmdResult.isSuccess()) {
                    X8AiSurroundExcuteController.this.lat = ackGetAiSurroundPoint.getDeviceLatitude();
                    X8AiSurroundExcuteController.this.log = ackGetAiSurroundPoint.getDeviceLongitude();
                    X8AiSurroundExcuteController.this.lat1 = ackGetAiSurroundPoint.getDeviceLatitudeTakeoff();
                    X8AiSurroundExcuteController.this.log1 = ackGetAiSurroundPoint.getDeviceLongitudeTakeoff();
                    X8AiSurroundExcuteController.this.r = ((float) StateManager.getInstance().getX8Drone().getAiSurroundState().getRadius()) / 10.0f;
                    if (X8AiSurroundExcuteController.this.r == 0.0f) {
                    }
                    int max = (int) (Math.sqrt(((double) Math.round(X8AiSurroundExcuteController.this.r)) * 1.5d) * 10.0d);
                    if (max > 100) {
                        max = 100;
                    }
                    X8AiSurroundExcuteController.this.vSpeed.setMaxMin(max, X8AiSurroundExcuteController.MIN, 1);
                    X8AiSurroundExcuteController.this.isGetPoint = true;
                    return;
                }
                X8AiSurroundExcuteController.this.isGetPoint = false;
            }
        });
    }

    public void switchMapVideo(boolean sightFlag) {
        if (this.handleView != null && this.isShow) {
            if (this.mX8AiSuroundState != X8AiSuroundState.RUNNING) {
                this.imgSuroundBg.setVisibility(sightFlag ? 8 : 0);
            } else {
                this.imgSuroundBg.setVisibility(8);
            }
            if (this.mX8AiSuroundState != X8AiSuroundState.RUNNING) {
                return;
            }
            if (sightFlag) {
                this.imgVcToggle.setVisibility(8);
            } else {
                this.imgVcToggle.setVisibility(0);
            }
        }
    }

    public void onLeft() {
    }

    public void onRight() {
        if (this.mX8AiSuroundState == X8AiSuroundState.RUNNING) {
            sendExiteCmd();
        }
    }

    public void onDroneConnected(boolean b) {
        if (!this.isShow) {
            return;
        }
        if (b) {
            sysAiVcCtrlMode();
        } else {
            ononDroneDisconnectedTaskComplete();
        }
    }

    public void setAiVcNotityFc() {
        this.fcManager.setAiVcNotityFc(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                }
            }
        });
    }

    public void setAiVcOpen() {
        this.fcManager.setAiVcOpen(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiSurroundExcuteController.this.imgVcToggle.setSelected(true);
                    X8AiSurroundExcuteController.this.activity.getmX8AiTrackController().openUi();
                }
            }
        });
    }

    public void setAiVcClose() {
        this.fcManager.setAiVcClose(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiSurroundExcuteController.this.imgVcToggle.setSelected(false);
                    X8AiSurroundExcuteController.this.activity.getmX8AiTrackController().closeUi();
                }
            }
        });
    }

    public void onChangeGoLocation(float left, float right, float top, float bottom, int w, int h) {
    }

    public void setGoEnabled(boolean b) {
        if (b) {
            setAiVcNotityFc();
        }
    }

    public void onTouchActionDown() {
    }

    public void onTouchActionUp() {
    }

    public void onTracking() {
        this.imgVcToggle.setEnabled(true);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        this.vSpeed.setSpeed2(speed);
    }

    public void getSpeed() {
        this.fcManager.getAiSurroundSpeed(new UiCallBackListener<AckAiSurrounds>() {
            public void onComplete(CmdResult cmdResult, AckAiSurrounds o) {
                if (cmdResult.isSuccess()) {
                    X8AiSurroundExcuteController.this.speed = o.getSpeed();
                    X8AiSurroundExcuteController.this.vSpeed.setSpeed2(X8AiSurroundExcuteController.this.speed);
                    X8AiSurroundExcuteController.this.isGetSpeed = true;
                    return;
                }
                X8AiSurroundExcuteController.this.isGetSpeed = false;
            }
        });
    }

    public void onSendSpeed(int speed) {
        this.fcManager.setAiSurroundSpeed(speed, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                }
            }
        });
    }

    public void switchUnityEvent() {
        if (this.isShow) {
            this.vSpeed.switchUnity();
        }
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void setSpeedMax(int speedMax) {
        this.vSpeed.setMaxMin(speedMax, MIN, 1);
    }

    public void showSportState(AutoFcSportState state) {
        if (this.mX8AiSuroundState != X8AiSuroundState.RUNNING) {
            setDeviceRadius();
        } else if (!this.isDraw && this.mX8AiSuroundState == X8AiSuroundState.RUNNING) {
            if (!this.isGetPoint) {
                getPoint();
            }
            if (!this.isGetSpeed) {
                getSpeed();
            }
            if (this.isGetPoint && this.isGetSpeed) {
                this.isDraw = true;
                this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().setAiSurroundMark(this.lat, this.log);
                this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().setAiSurroundCircle(this.lat, this.log, this.r);
                this.vSpeed.setVisibility(0);
            }
        } else if (this.isDraw && this.isGetPoint && this.isGetSpeed && this.mX8AiSuroundState == X8AiSuroundState.RUNNING) {
            AutoAiSurroundState runState = StateManager.getInstance().getX8Drone().getAiSurroundState();
            if (runState == null) {
                return;
            }
            if (runState.getStates() != 0) {
                this.isChangRadius = true;
                this.speed = runState.getSpeed();
                this.r = ((float) runState.getRadius()) / 10.0f;
            } else if (this.isChangRadius) {
                this.isChangRadius = false;
                this.speed = runState.getSpeed();
                this.r = ((float) runState.getRadius()) / 10.0f;
                this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().reSetAiSurroundCircle(this.lat, this.log, this.r);
                int max = (int) (Math.sqrt(((double) Math.round(this.r)) * 1.5d) * 10.0d);
                if (max > 100) {
                    max = 100;
                }
                this.vSpeed.setMaxMin(max, MIN, 1);
                if (max < Math.abs(this.speed)) {
                    if (this.speed > 0) {
                        this.speed = max;
                    } else {
                        this.speed = -max;
                    }
                }
                this.vSpeed.setSpeed2(this.speed);
            }
        }
    }

    public void openVcToggle() {
        if (this.activity.getmMapVideoController().isFullVideo()) {
            this.imgVcToggle.setVisibility(0);
        } else {
            this.imgVcToggle.setVisibility(8);
        }
    }

    public void closeIconByNextUi() {
        this.tvPoint.setVisibility(8);
        this.imgBack.setVisibility(8);
        this.flagSmall.setVisibility(8);
    }

    public void sysAiVcCtrlMode() {
        if (this.mX8AiSuroundState != X8AiSuroundState.IDLE && this.mX8AiSuroundState != X8AiSuroundState.SET_CIRCLE_POINT && this.mX8AiSuroundState != X8AiSuroundState.SET_TAKE_OFF_POINT && this.mX8AiSuroundState != X8AiSuroundState.SET_PARAMETER) {
            return;
        }
        if (this.timeSend == 0) {
            this.timeSend = 1;
            this.activity.getFcManager().sysCtrlMode2AiVc(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                }
            }, X8Task.VCM_INTEREST_POINT.ordinal());
            return;
        }
        this.timeSend = 0;
    }
}
