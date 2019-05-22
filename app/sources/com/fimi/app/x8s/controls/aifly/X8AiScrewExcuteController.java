package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiScrewNextModule;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.interfaces.IX8ScrewListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.AckAiSurrounds;
import com.fimi.x8sdk.dataparser.AckGetAiSurroundPoint;
import com.fimi.x8sdk.dataparser.AckNormalCmds;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AiScrewExcuteController extends AbsX8AiController implements OnClickListener, onDialogButtonClickListener {
    protected int MAX_WIDTH;
    private X8sMainActivity activity;
    private View blank;
    private double currentLat;
    private double currentLog;
    private boolean cw;
    private X8DoubleCustomDialog dialog;
    private int distance;
    private View flagSmall;
    private float height;
    private ImageView imgBack;
    private ImageView imgSuroundBg;
    private boolean isDraw;
    private boolean isGetDistance;
    private boolean isGetPoint;
    private boolean isGetSpeed;
    private boolean isNextShow;
    protected boolean isShow;
    private double lastLat;
    private double lastLog;
    private IX8ScrewListener listener;
    private FcCtrlManager mFcCtrlManager;
    private FcManager mFcManager;
    private IX8NextViewListener mIX8NextViewListener = new IX8NextViewListener() {
        public void onBackClick() {
            X8AiScrewExcuteController.this.closeNextUi(true);
        }

        public void onExcuteClick() {
            X8AiScrewExcuteController.this.closeNextUi(false);
            X8AiScrewExcuteController.this.imgSuroundBg.setVisibility(8);
            X8AiScrewExcuteController.this.vRadiusBg.setVisibility(8);
            X8AiScrewExcuteController.this.tvTip.setVisibility(8);
            X8AiScrewExcuteController.this.state = ScrewState.RUNNING;
            X8AiScrewExcuteController.this.listener.onAiScrewRunning();
        }

        public void onSaveClick() {
        }
    };
    private TextView mTvRadius;
    private X8AiScrewNextModule mX8AiScrewNextModule;
    private View nextRootView;
    private String prex;
    private int r;
    private float radius;
    private ScrewState state = ScrewState.IDLE;
    private int timeSend = 0;
    private TextView tvP2PTip;
    private TextView tvPoint;
    private X8AiTipWithCloseView tvTip;
    private View vRadiusBg;
    protected int width = X8Application.ANIMATION_WIDTH;

    public enum ScrewState {
        IDLE,
        SETDOT,
        SETRADIUS,
        RUNNING
    }

    public double getLastLng() {
        return this.lastLog;
    }

    public double getLastLat() {
        return this.lastLat;
    }

    public void onLeft() {
    }

    public void onRight() {
        if (this.state == ScrewState.RUNNING) {
            sendExiteCmd();
        }
    }

    public X8AiScrewExcuteController(X8sMainActivity activity, View rootView, ScrewState state) {
        super(rootView);
        this.activity = activity;
        this.state = state;
    }

    public void setListener(IX8ScrewListener listener) {
        this.listener = listener;
    }

    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            if (this.state != ScrewState.RUNNING) {
                closeScrewing();
            } else {
                showExitDialog();
            }
        } else if (id == R.id.img_ai_set_dot) {
            if (this.state == ScrewState.IDLE) {
                setCirclePoint();
            } else if (this.state == ScrewState.SETDOT) {
                String s;
                if (this.radius >= 200.0f) {
                    s = X8NumberUtil.getDistanceNumberString(200.0f, 0, false);
                    X8ToastUtil.showToast(this.activity, String.format(this.activity.getString(R.string.x8_ai_surround_radius_tip2), new Object[]{s}), 0);
                } else if (this.radius < 5.0f) {
                    s = X8NumberUtil.getDistanceNumberString(5.0f, 0, false);
                    X8ToastUtil.showToast(this.activity, String.format(this.activity.getString(R.string.x8_ai_surround_radius_tip1), new Object[]{s}), 0);
                } else if (this.height < 3.0f) {
                    X8ToastUtil.showToast(this.activity, String.format(this.activity.getString(R.string.height_tip), new Object[]{X8NumberUtil.getDistanceNumberString(3.0f, 0, true)}), 0);
                } else {
                    sendCircleCmd();
                }
            } else if (this.state == ScrewState.SETRADIUS) {
                openNextUi();
            }
        } else if (id == R.id.main_ai_ai_screw_next_blank) {
            closeNextUi(true);
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
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_ai_screw_layout, (ViewGroup) this.rootView, true);
        this.imgSuroundBg = (ImageView) this.handleView.findViewById(R.id.img_ai_suround_bg);
        this.imgBack = (ImageView) this.handleView.findViewById(R.id.img_ai_follow_back);
        this.tvP2PTip = (TextView) this.handleView.findViewById(R.id.img_ai_p2p_tip);
        this.tvPoint = (TextView) this.handleView.findViewById(R.id.img_ai_set_dot);
        this.vRadiusBg = this.handleView.findViewById(R.id.rl_x8_ai_surround_radius);
        this.mTvRadius = (TextView) this.handleView.findViewById(R.id.tv_ai_radius);
        this.tvTip = (X8AiTipWithCloseView) this.handleView.findViewById(R.id.v_content_tip);
        this.tvTip.setTipText(this.handleView.getContext().getString(R.string.x8_ai_surround_select_point));
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.flagSmall.setOnClickListener(this);
        if (this.state != ScrewState.IDLE) {
            this.imgSuroundBg.setVisibility(8);
            this.tvPoint.setVisibility(8);
            this.mTvRadius.setVisibility(8);
            this.tvTip.setVisibility(8);
        }
        this.prex = this.activity.getString(R.string.x8_ai_fly_screw_hight_distance);
        this.nextRootView = this.rootView.findViewById(R.id.x8_main_ai_screw_next_content);
        this.blank = this.rootView.findViewById(R.id.main_ai_ai_screw_next_blank);
        this.mX8AiScrewNextModule = new X8AiScrewNextModule();
        this.imgBack.setOnClickListener(this);
        this.tvPoint.setOnClickListener(this);
        this.blank.setOnClickListener(this);
        if (this.state == ScrewState.RUNNING) {
            this.listener.onAiScrewRunning();
        }
        super.openUi();
    }

    public void closeUi() {
        this.isShow = false;
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().clearSurroundMarker();
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().resetMapEvent();
        super.closeUi();
    }

    public void openNextUi() {
        this.nextRootView.setVisibility(0);
        this.blank.setVisibility(0);
        closeIconByNextUi();
        this.mX8AiScrewNextModule.init(this.activity, this.nextRootView);
        if (this.mX8AiScrewNextModule != null) {
            this.mX8AiScrewNextModule.setListener(this.mIX8NextViewListener, this.mFcManager, this, this.radius, this.height);
        }
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", new float[]{(float) this.width, 0.0f});
            animatorY.setDuration(300);
            animatorY.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                public void onAnimationStart(Animator animation) {
                }
            });
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
                    X8AiScrewExcuteController.this.nextRootView.setVisibility(8);
                    ((ViewGroup) X8AiScrewExcuteController.this.nextRootView).removeAllViews();
                    X8AiScrewExcuteController.this.imgBack.setVisibility(0);
                    X8AiScrewExcuteController.this.flagSmall.setVisibility(0);
                    if (b) {
                        X8AiScrewExcuteController.this.tvPoint.setVisibility(0);
                    }
                }
            });
        }
    }

    public void setFcManager(FcCtrlManager mFcCtrlManager, FcManager mFcManager) {
        this.mFcCtrlManager = mFcCtrlManager;
        this.mFcManager = mFcManager;
    }

    public boolean isShow() {
        if (StateManager.getInstance().getX8Drone().getCtrlMode() == 4) {
            return false;
        }
        return this.isShow;
    }

    public void showExitDialog() {
        String t = "";
        String m = "";
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_ai_fixedwing_exite_title), this.rootView.getContext().getString(R.string.x8_ai_fly_screw_exte), this);
        this.dialog.show();
    }

    public void taskExit() {
        onTaskComplete(false);
    }

    public void cancleByModeChange(int mode) {
        boolean z = true;
        if (mode != 1) {
            z = false;
        }
        onTaskComplete(z);
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

    private void onTaskComplete(boolean showText) {
        closeScrewing();
        if (this.listener != null) {
            this.listener.onAiScrewComplete(showText);
        }
    }

    public void ononDroneDisconnectedTaskComplete() {
        closeScrewing();
        if (this.listener != null) {
            this.listener.onAiScrewComplete(false);
        }
    }

    private void closeScrewing() {
        closeUi();
        if (this.listener != null) {
            this.listener.onAiScrewBackClick();
        }
    }

    public void sendExiteCmd() {
        this.mFcManager.setScrewExite(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.taskExit();
                }
            }
        });
    }

    public void switchMapVideo(boolean sightFlag) {
        int i = 8;
        if (this.handleView == null || !this.isShow) {
            return;
        }
        if (this.state != ScrewState.RUNNING) {
            ImageView imageView = this.imgSuroundBg;
            if (!sightFlag) {
                i = 0;
            }
            imageView.setVisibility(i);
            return;
        }
        this.imgSuroundBg.setVisibility(8);
    }

    public void showSportState(AutoFcSportState state1) {
        if (!(this.nextRootView == null || this.mX8AiScrewNextModule == null)) {
            if (((ViewGroup) this.nextRootView).getChildCount() > 0) {
                this.mX8AiScrewNextModule.showSportState(state1);
            }
            if (this.state == ScrewState.SETDOT) {
                this.height = state1.getHeight();
                int intH = (int) this.height;
                this.radius = getCurrentDistance();
                int intD = (int) this.radius;
                String h = "";
                String d = "";
                if (intH >= 3) {
                    h = "<font color='#ffffffff'>" + X8NumberUtil.getDistanceNumberString((float) intH, 0, false) + "</font>";
                } else {
                    h = "<font color='#F22121'>" + X8NumberUtil.getDistanceNumberString((float) intH, 0, false) + "</font>";
                }
                if (intD > 5) {
                    d = "<font color='#ffffffff'>" + X8NumberUtil.getDistanceNumberString((float) intD, 0, false) + "</font>";
                } else {
                    d = "<font color='#F22121'>" + X8NumberUtil.getDistanceNumberString((float) intD, 0, false) + "</font>";
                }
                if (this.vRadiusBg.getVisibility() != 0) {
                    this.vRadiusBg.setVisibility(0);
                }
                this.mTvRadius.setText(Html.fromHtml(String.format(this.prex, new Object[]{h, d})));
            }
        }
        getParmeter();
    }

    public void setCirclePoint() {
        this.lastLog = StateManager.getInstance().getX8Drone().getLongitude();
        this.lastLat = StateManager.getInstance().getX8Drone().getLatitude();
        this.tvPoint.setText(this.activity.getString(R.string.x8_ai_fly_follow_surround_set_takeoff_point));
        String prex1 = this.rootView.getContext().getString(R.string.x8_ai_fly_screw_tip5);
        String str1 = X8NumberUtil.getDistanceNumberString(3.0f, 1, false);
        String str2 = X8NumberUtil.getDistanceNumberString(5.0f, 1, false);
        String str3 = X8NumberUtil.getDistanceNumberString(200.0f, 1, false);
        this.tvTip.setTipText(String.format(prex1, new Object[]{str1, str2, str3}));
        this.state = ScrewState.SETDOT;
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().setAiSurroundMark(this.lastLat, this.lastLog);
    }

    public float getCurrentDistance() {
        return this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().getSurroundRadius(this.lastLog, this.lastLat, StateManager.getInstance().getX8Drone().getLongitude(), StateManager.getInstance().getX8Drone().getLatitude());
    }

    public void drawScrew(boolean b, int radius, int max) {
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().addPolylinescircle(b, this.lastLat, this.lastLog, this.currentLat, this.currentLog, radius, max);
        this.isDraw = true;
    }

    private void sendCircleCmd() {
        this.currentLog = StateManager.getInstance().getX8Drone().getLongitude();
        this.currentLat = StateManager.getInstance().getX8Drone().getLatitude();
        this.mFcManager.setAiSurroundCiclePoint(getLastLng(), getLastLat(), this.height, this.currentLog, this.currentLat, this.height, 2, new UiCallBackListener<AckNormalCmds>() {
            public void onComplete(CmdResult cmdResult, AckNormalCmds o) {
                if (cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.state = ScrewState.SETRADIUS;
                    X8AiScrewExcuteController.this.openNextUi();
                }
            }
        });
    }

    public void getParmeter() {
        if (!this.isDraw && this.state == ScrewState.RUNNING) {
            if (!this.isGetPoint) {
                getPoint();
            }
            if (!this.isGetDistance) {
                getMaxDistance();
            }
            if (!this.isGetSpeed) {
                getSpeed();
            }
            if (this.isGetPoint && this.isGetDistance && this.isGetSpeed) {
                drawScrew(this.cw, this.r, this.distance);
            }
        }
    }

    public void getPoint() {
        this.mFcManager.getAiSurroundCiclePoint(new UiCallBackListener<AckGetAiSurroundPoint>() {
            public void onComplete(CmdResult cmdResult, AckGetAiSurroundPoint ackGetAiSurroundPoint) {
                if (cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.lastLat = ackGetAiSurroundPoint.getDeviceLatitude();
                    X8AiScrewExcuteController.this.lastLog = ackGetAiSurroundPoint.getDeviceLongitude();
                    X8AiScrewExcuteController.this.currentLat = ackGetAiSurroundPoint.getDeviceLatitudeTakeoff();
                    X8AiScrewExcuteController.this.currentLog = ackGetAiSurroundPoint.getDeviceLongitudeTakeoff();
                    X8AiScrewExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().setAiSurroundMark(X8AiScrewExcuteController.this.lastLat, X8AiScrewExcuteController.this.lastLog);
                    X8AiScrewExcuteController.this.r = Math.round(X8AiScrewExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().getSurroundRadius(X8AiScrewExcuteController.this.lastLat, X8AiScrewExcuteController.this.lastLog, X8AiScrewExcuteController.this.currentLat, X8AiScrewExcuteController.this.currentLog));
                    X8AiScrewExcuteController.this.isGetPoint = true;
                    return;
                }
                X8AiScrewExcuteController.this.isGetPoint = false;
            }
        });
    }

    public void getMaxDistance() {
        this.mFcManager.getScrewPrameter(new UiCallBackListener<AckAiScrewPrameter>() {
            public void onComplete(CmdResult cmdResult, AckAiScrewPrameter ackAiScrewPrameter) {
                if (cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.isGetDistance = true;
                    X8AiScrewExcuteController.this.distance = ackAiScrewPrameter.getDistance();
                    return;
                }
                X8AiScrewExcuteController.this.isGetDistance = false;
            }
        });
    }

    public void getSpeed() {
        this.mFcManager.getAiSurroundSpeed(new UiCallBackListener<AckAiSurrounds>() {
            public void onComplete(CmdResult cmdResult, AckAiSurrounds o) {
                if (cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.isGetSpeed = true;
                    o.getSpeed();
                    if (o.getSpeed() > 0) {
                        X8AiScrewExcuteController.this.cw = true;
                        return;
                    } else {
                        X8AiScrewExcuteController.this.cw = false;
                        return;
                    }
                }
                X8AiScrewExcuteController.this.isGetSpeed = false;
            }
        });
    }

    public void closeIconByNextUi() {
        this.tvPoint.setVisibility(8);
        this.imgBack.setVisibility(8);
        this.flagSmall.setVisibility(8);
    }

    public void sysAiVcCtrlMode() {
        if (this.state == ScrewState.RUNNING) {
            return;
        }
        if (this.timeSend == 0) {
            this.timeSend = 1;
            this.activity.getFcManager().sysCtrlMode2AiVc(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                }
            }, X8Task.VCM_SPIRAL.ordinal());
            return;
        }
        this.timeSend = 0;
    }
}
