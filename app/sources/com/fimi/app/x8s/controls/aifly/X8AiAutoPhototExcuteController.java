package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.X8AiTrackController.OnAiTrackControllerListener;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiAutoPhotoExcuteConfirmModule;
import com.fimi.app.x8s.enums.X8AiAutoPhotoState;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiAutoPhotoExcuteControllerListener;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.modulestate.GimbalState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AiAutoPhototExcuteController extends AbsX8AiController implements OnClickListener, onDialogButtonClickListener, OnAiTrackControllerListener {
    private X8sMainActivity activity;
    private View blank;
    private int count = 3;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private ImageView imgAutoBg;
    private ImageView imgBack;
    private ImageView imgCloud;
    private ImageView imgNext;
    protected boolean isNextShow;
    protected boolean isShow;
    private IX8AiAutoPhotoExcuteControllerListener listener;
    private View mAngle;
    private IX8NextViewListener mIX8NextViewListener = new IX8NextViewListener() {
        public void onBackClick() {
            X8AiAutoPhototExcuteController.this.closeNextUi(true);
        }

        public void onExcuteClick() {
            X8AiAutoPhototExcuteController.this.closeNextUi(false);
            X8AiAutoPhototExcuteController.this.imgAutoBg.setVisibility(8);
            X8AiAutoPhototExcuteController.this.mAngle.setVisibility(8);
            X8AiAutoPhototExcuteController.this.tvTip.setVisibility(8);
            X8AiAutoPhototExcuteController.this.count = 3;
            X8AiAutoPhototExcuteController.this.state = X8AiAutoPhotoState.RUNNING;
            X8AiAutoPhototExcuteController.this.mTimeHandler.sendEmptyMessage(1);
            X8AiAutoPhototExcuteController.this.tvTime.setVisibility(0);
            X8AiAutoPhototExcuteController.this.listener.onAutoPhotoRunning();
        }

        public void onSaveClick() {
        }
    };
    private TimeHandler mTimeHandler = new TimeHandler(this, null);
    private X8AiAutoPhotoExcuteConfirmModule mX8AiAutoPhotoExcuteConfirmModule;
    private View nextRootView;
    private int pitchAngle;
    private X8AiAutoPhotoState state = X8AiAutoPhotoState.IDLE;
    private int timeSend = 0;
    private TextView tvCloud;
    private TextView tvP2PTip;
    private TextView tvTime;
    private X8AiTipWithCloseView tvTip;
    private int type;
    protected int width = X8Application.ANIMATION_WIDTH;

    private class TimeHandler extends Handler {
        private TimeHandler() {
        }

        /* synthetic */ TimeHandler(X8AiAutoPhototExcuteController x0, AnonymousClass1 x1) {
            this();
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (X8AiAutoPhototExcuteController.this.count >= 0) {
                        if (X8AiAutoPhototExcuteController.this.count != 0) {
                            X8AiAutoPhototExcuteController.this.tvTime.setText("" + X8AiAutoPhototExcuteController.this.count = X8AiAutoPhototExcuteController.this.count - 1);
                        } else {
                            X8AiAutoPhototExcuteController.this.count = X8AiAutoPhototExcuteController.this.count - 1;
                            X8AiAutoPhototExcuteController.this.tvTime.setText("GO");
                        }
                        sendEmptyMessageDelayed(1, 1000);
                        return;
                    }
                    X8AiAutoPhototExcuteController.this.tvTime.setVisibility(8);
                    return;
                default:
                    return;
            }
        }
    }

    public X8AiAutoPhototExcuteController(X8sMainActivity activity, View rootView, X8AiAutoPhotoState state, int type) {
        super(rootView);
        this.activity = activity;
        this.state = state;
        this.type = type;
    }

    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    public void initActions() {
        if (this.handleView != null) {
            this.imgNext.setOnClickListener(this);
            this.imgBack.setOnClickListener(this);
            this.blank.setOnClickListener(this);
        }
    }

    public void defaultVal() {
    }

    public void onLeft() {
    }

    public void onRight() {
        if (this.state == X8AiAutoPhotoState.RUNNING) {
            stopAutoPhoto();
        }
    }

    public void showExitDialog() {
        if (this.dialog == null) {
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_ai_fly_auto_photo), this.rootView.getContext().getString(R.string.x8_ai_auto_photo_exite), this);
        }
        this.dialog.show();
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            if (this.state == X8AiAutoPhotoState.RUNNING) {
                showExitDialog();
                return;
            }
            closeUi();
            this.listener.onAutoPhotoBackClick();
        } else if (id == R.id.img_ai_follow_next) {
            openNextUi();
        } else if (id == R.id.x8_main_ai_auto_photo_next_blank) {
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
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_ai_auto_photo_layout, (ViewGroup) this.rootView, true);
        this.tvP2PTip = (TextView) this.handleView.findViewById(R.id.img_ai_p2p_tip);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.flagSmall.setOnClickListener(this);
        this.imgNext = (ImageView) this.handleView.findViewById(R.id.img_ai_follow_next);
        this.imgBack = (ImageView) this.handleView.findViewById(R.id.img_ai_follow_back);
        this.tvTip = (X8AiTipWithCloseView) this.handleView.findViewById(R.id.v_content_tip);
        this.imgAutoBg = (ImageView) this.handleView.findViewById(R.id.img_ai_auto_photo_bg);
        this.mAngle = this.handleView.findViewById(R.id.rl_angle);
        this.imgCloud = (ImageView) this.handleView.findViewById(R.id.img_cloud);
        this.tvCloud = (TextView) this.handleView.findViewById(R.id.tv_cloud);
        this.tvTime = (TextView) this.handleView.findViewById(R.id.img_ai_time);
        this.nextRootView = this.rootView.findViewById(R.id.x8_main_ai_auto_photo_next_content);
        this.blank = this.rootView.findViewById(R.id.x8_main_ai_auto_photo_next_blank);
        this.mX8AiAutoPhotoExcuteConfirmModule = new X8AiAutoPhotoExcuteConfirmModule();
        if (this.state == X8AiAutoPhotoState.IDLE) {
            this.imgNext.setEnabled(false);
            String s;
            if (this.type == 0) {
                s = this.rootView.getContext().getString(R.string.x8_ai_auto_photo_tip5);
                if (this.activity.getmMapVideoController().isFullVideo()) {
                    this.imgAutoBg.setVisibility(0);
                } else {
                    this.imgAutoBg.setVisibility(8);
                }
                this.tvTip.setTipText(s);
            } else {
                s = this.rootView.getContext().getString(R.string.x8_ai_auto_photo_tip5);
                this.imgAutoBg.setVisibility(8);
                this.tvTip.setTipText(s);
            }
        } else if (this.state == X8AiAutoPhotoState.RUNNING) {
            this.imgNext.setVisibility(8);
            this.imgAutoBg.setVisibility(8);
            this.mAngle.setVisibility(8);
            this.tvTip.setVisibility(8);
            if (this.listener != null) {
                this.listener.onAutoPhotoRunning();
            }
        }
        this.activity.getmX8AiTrackController().setOnAiTrackControllerListener(this);
        initActions();
        this.activity.getmX8AiTrackController().openUi();
        super.openUi();
    }

    public void closeUi() {
        this.isShow = false;
        this.activity.getmX8AiTrackController().closeUi();
        this.state = X8AiAutoPhotoState.IDLE;
        this.mTimeHandler.removeMessages(0);
        this.mTimeHandler.removeMessages(1);
        setAiVcClose();
        super.closeUi();
    }

    public void openNextUi() {
        this.nextRootView.setVisibility(0);
        this.blank.setVisibility(0);
        closeIconByNextUi();
        this.mX8AiAutoPhotoExcuteConfirmModule.init(this.activity, this.nextRootView);
        this.mX8AiAutoPhotoExcuteConfirmModule.setListener(this.mIX8NextViewListener, this.activity.getFcManager(), this);
        this.mX8AiAutoPhotoExcuteConfirmModule.setValue(this.pitchAngle, this.type);
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
                    X8AiAutoPhototExcuteController.this.nextRootView.setVisibility(8);
                    ((ViewGroup) X8AiAutoPhototExcuteController.this.nextRootView).removeAllViews();
                    X8AiAutoPhototExcuteController.this.imgBack.setVisibility(0);
                    X8AiAutoPhototExcuteController.this.flagSmall.setVisibility(0);
                    if (b) {
                        X8AiAutoPhototExcuteController.this.imgNext.setVisibility(0);
                    }
                }
            });
        }
    }

    public void setListener(IX8AiAutoPhotoExcuteControllerListener listener) {
        this.listener = listener;
    }

    public void showGimbalState() {
        GimbalState state = StateManager.getInstance().getGimbalState();
        if (state != null && this.handleView != null && this.isShow) {
            this.pitchAngle = state.getPitchAngle();
            this.tvCloud.setText(NumberUtil.decimalPointStr(((double) this.pitchAngle) / 100.0d, 1) + "°");
        }
    }

    public void taskExit() {
        onTaskComplete(false);
    }

    public void stopAutoPhoto() {
        this.state = X8AiAutoPhotoState.STOP;
        this.activity.getFcManager().setAiAutoPhotoExit(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiAutoPhototExcuteController.this.taskExit();
                }
            }
        });
    }

    public void setAiVcClose() {
        this.activity.getFcManager().setAiVcClose(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiAutoPhototExcuteController.this.activity.getmX8AiTrackController().closeUi();
                }
            }
        });
    }

    public void setAiVcNotityFc() {
        this.activity.getFcManager().setAiVcNotityFc(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                }
            }
        });
    }

    public void onChangeGoLocation(float left, float right, float top, float bottom, int w, int h) {
    }

    public void setGoEnabled(boolean b) {
        if (b) {
            this.imgNext.setEnabled(true);
            setAiVcNotityFc();
        }
    }

    public void onTouchActionDown() {
    }

    public void onTouchActionUp() {
    }

    public void onTracking() {
    }

    public void onDroneConnected(boolean b) {
        if (!this.isShow) {
            return;
        }
        if (b) {
            showGimbalState();
            sysAiVcCtrlMode();
            return;
        }
        onDisconnectTaskComplete();
    }

    public void cancleByModeChange(int taskMode) {
        boolean z = true;
        if (taskMode != 1) {
            z = false;
        }
        onTaskComplete(z);
    }

    private void onTaskComplete(boolean b) {
        closeAutoPhoto();
        if (this.listener != null) {
            this.listener.onAutoPhotoComplete(b);
        }
    }

    private void onDisconnectTaskComplete() {
        closeAutoPhoto();
        if (this.listener != null) {
            this.listener.onAutoPhotoComplete(false);
        }
    }

    private void closeAutoPhoto() {
        closeUi();
        if (this.listener != null) {
            this.listener.onAutoPhotoBackClick();
        }
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void switchMapVideo(boolean isVideo) {
        if (this.state != X8AiAutoPhotoState.IDLE || this.type != 0) {
            return;
        }
        if (isVideo) {
            this.imgAutoBg.setVisibility(8);
        } else {
            this.imgAutoBg.setVisibility(0);
        }
    }

    public void closeIconByNextUi() {
        this.imgNext.setVisibility(8);
        this.imgBack.setVisibility(8);
        this.flagSmall.setVisibility(8);
    }

    public void sysAiVcCtrlMode() {
        if (this.state != X8AiAutoPhotoState.IDLE) {
            return;
        }
        if (this.timeSend == 0) {
            this.timeSend = 1;
            this.activity.getFcManager().sysCtrlMode2AiVc(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                }
            }, X8Task.VCM_SELFIE.ordinal());
            return;
        }
        this.timeSend = 0;
    }
}
