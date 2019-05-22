package com.fimi.app.x8s.controls.aifly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.TcpClient;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.X8AiTrackController.OnAiTrackControllerListener;
import com.fimi.app.x8s.enums.X8AiFollowState;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiFollowExcuteListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiFollowModeItemView;
import com.fimi.app.x8s.widget.X8AiFollowModeItemView.OnModeSelectListner;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8FollowSpeedContainerView;
import com.fimi.app.x8s.widget.X8FollowSpeedContainerView.OnSendSpeedListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.percent.PercentRelativeLayout.LayoutParams;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.dataparser.AckAiFollowGetSpeed;
import com.fimi.x8sdk.dataparser.AckGetAiFollowMode;

public class X8AiFollowExcuteController extends AbsX8AiController implements OnClickListener, OnAiTrackControllerListener, onDialogButtonClickListener, OnModeSelectListner, OnSendSpeedListener {
    private X8sMainActivity activity;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private int goHeight;
    private int goWidth;
    private ImageView imgBack;
    private ImageView imgGo;
    private ImageView imgSmall;
    private boolean isGetMode;
    private boolean isGetSpeed;
    private boolean isShowGo = false;
    private boolean isTou;
    private IX8AiFollowExcuteListener listener;
    private X8AiFollowState mX8AiFollowState = X8AiFollowState.IDLE;
    private int timeSend = 0;
    private TextView tvTitle;
    private int type;
    private X8AiFollowModeItemView vModeImtes;
    private X8FollowSpeedContainerView vSpeedContainer;

    public X8AiFollowExcuteController(X8sMainActivity activity, View rootView, X8AiFollowState state, int type) {
        super(rootView);
        this.mX8AiFollowState = state;
        this.type = type;
        this.activity = activity;
    }

    public void setX8AiFollowExcuteListener(IX8AiFollowExcuteListener listener) {
        this.listener = listener;
    }

    public void initViews(View rootView) {
    }

    public void initActions() {
        if (this.handleView != null) {
            this.imgGo.setOnClickListener(this);
            this.imgBack.setOnClickListener(this);
        }
    }

    public void initViewStubViews(View view) {
        this.imgGo = (ImageView) view.findViewById(R.id.img_ai_follow_go);
        this.imgBack = (ImageView) view.findViewById(R.id.img_ai_follow_back);
        this.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        this.vModeImtes = (X8AiFollowModeItemView) view.findViewById(R.id.v_mode_item);
        this.vSpeedContainer = (X8FollowSpeedContainerView) view.findViewById(R.id.v_lock_mode_speed);
        this.vModeImtes.setListener(this);
        this.vSpeedContainer.setOnSendSpeedListener(this);
    }

    public void setTitle() {
        String s = "";
        int res = 0;
        switch (this.type) {
            case 0:
                s = getString(R.string.x8_ai_fly_follow_normal);
                res = R.drawable.x8_img_ai_follow_normal1_small;
                break;
            case 1:
                s = getString(R.string.x8_ai_fly_follow_parallel);
                res = R.drawable.x8_img_ai_follow_parallel1_small;
                break;
            case 2:
                s = getString(R.string.x8_ai_fly_follow_lockup);
                res = R.drawable.x8_img_ai_follow_lockup1_small;
                break;
        }
        this.vModeImtes.findIndexByMode(this.type);
        this.tvTitle.setText(s);
        this.imgSmall.setBackgroundResource(res);
    }

    public void openUi() {
        this.isShow = true;
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_ai_follow_excute_layout, (ViewGroup) this.rootView, true);
        this.imgSmall = (ImageView) this.handleView.findViewById(R.id.img_ai_flag_small);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.flagSmall.setOnClickListener(this);
        initViewStubViews(this.handleView);
        initActions();
        if (this.mX8AiFollowState == X8AiFollowState.IDLE) {
            this.vModeImtes.setVisibility(8);
            this.vSpeedContainer.setVisibility(8);
            this.vModeImtes.findIndexByMode(this.type);
            setTitle();
            this.mX8AiFollowState = X8AiFollowState.OEPNVIEW;
            this.isGetMode = true;
        } else if (this.mX8AiFollowState == X8AiFollowState.RUNNING) {
            this.listener.onAiFollowRunning();
            this.isShowGo = true;
        }
        this.activity.getmX8AiTrackController().setOnAiTrackControllerListener(this);
        this.activity.getmX8AiTrackController().openUi();
        super.openUi();
    }

    public void closeUi() {
        super.closeUi();
        this.activity.getmX8AiTrackController().closeUi();
        this.mX8AiFollowState = X8AiFollowState.IDLE;
    }

    public void defaultVal() {
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_go) {
            onGoClick();
        } else if (id == R.id.img_ai_follow_back) {
            showExitDialog();
        } else if (id != R.id.rl_flag_small) {
        } else {
            if (this.tvTitle.getVisibility() == 0) {
                this.tvTitle.setVisibility(8);
            } else {
                this.tvTitle.setVisibility(0);
            }
        }
    }

    public void onTouchActionDown() {
        this.isTou = false;
        this.imgGo.setVisibility(4);
    }

    public void onTouchActionUp() {
        this.isTou = true;
        setAiVcNotityFc();
    }

    public void onChangeGoLocation(float left, float right, float top, float bottom, int w, int h) {
        if (!this.isShowGo && right - left > 5.0f) {
            int l;
            int t;
            if (this.goWidth == 0) {
                this.goWidth = this.imgGo.getWidth();
                this.goHeight = this.imgGo.getHeight();
            }
            LayoutParams lp = new LayoutParams(this.imgGo.getLayoutParams());
            boolean inside = false;
            if (((float) this.goWidth) <= left) {
                l = (int) (left - ((float) this.goWidth));
            } else if (Math.ceil((double) right) + ((double) this.goWidth) <= ((double) w)) {
                l = (int) Math.ceil((double) right);
            } else {
                l = (int) Math.ceil((double) left);
                inside = true;
            }
            if (bottom <= ((float) this.goHeight)) {
                t = 0;
            } else if (Math.ceil((double) top) + ((double) this.goHeight) >= ((double) h)) {
                t = h - this.goHeight;
            } else if (inside) {
                t = (int) (bottom - ((float) this.goHeight));
            } else {
                t = (int) ((((bottom - top) / 2.0f) + top) - (((float) this.goHeight) / 2.0f));
            }
            lp.setMargins(l, t, 0, 0);
            this.imgGo.setLayoutParams(lp);
            if (this.mX8AiFollowState != X8AiFollowState.IDLE) {
                this.imgGo.setVisibility(0);
            }
        } else if (left == right && top == bottom && top == right && right == 0.0f) {
            this.imgGo.setVisibility(4);
        }
    }

    public void setGoEnabled(boolean b) {
    }

    public void setAiVcNotityFc() {
        this.activity.getFcManager().setAiVcNotityFc(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                TcpClient.getIntance().sendLog("----->...setAiVcNotityFc..." + cmdResult.getErrDes());
                if (cmdResult.isSuccess()) {
                }
            }
        });
    }

    public void onTracking() {
        if (this.isShow && this.isTou && this.isShowGo && this.imgGo.getVisibility() == 0) {
            this.imgGo.setVisibility(4);
        }
    }

    public void onGoClick() {
        setModle(this.type);
    }

    public void doFollow() {
        this.activity.getFcManager().setFollowExcute(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiFollowExcuteController.this.listener.onAiFollowRunning();
                    X8AiFollowExcuteController.this.activity.getmMapVideoController().resetShow();
                    X8AiFollowExcuteController.this.mX8AiFollowState = X8AiFollowState.RUNNING;
                    X8AiFollowExcuteController.this.isShowGo = true;
                    TcpClient.getIntance().sendLog("..setFollowExcute.. " + X8AiFollowExcuteController.this.isShowGo + cmdResult.getErrDes());
                    return;
                }
                TcpClient.getIntance().sendLog("..onGoClick.. " + X8AiFollowExcuteController.this.isShowGo + cmdResult.getErrDes());
            }
        });
    }

    public void setModle(final int type) {
        this.activity.getFcManager().setAiFollowModle(type, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                TcpClient.getIntance().sendLog("..setModle.. " + cmdResult.getErrDes() + " mode=" + type);
                if (cmdResult.isSuccess()) {
                    X8AiFollowExcuteController.this.doFollow();
                }
            }
        });
    }

    public void getFollowMode() {
        this.activity.getFcManager().getAiFollowModle(new UiCallBackListener<AckGetAiFollowMode>() {
            public void onComplete(CmdResult cmdResult, AckGetAiFollowMode ackGetAiFollowMode) {
                if (cmdResult.isSuccess()) {
                    X8AiFollowExcuteController.this.isGetMode = true;
                    TcpClient.getIntance().sendLog("..getFollowMode.. " + cmdResult.getErrDes() + " mode=" + ackGetAiFollowMode.getMode());
                    X8AiFollowExcuteController.this.type = ackGetAiFollowMode.getMode();
                    X8AiFollowExcuteController.this.setTitle();
                    X8AiFollowExcuteController.this.vModeImtes.findIndexByMode(X8AiFollowExcuteController.this.type);
                    return;
                }
                TcpClient.getIntance().sendLog(" ..getFollowMode..  " + cmdResult.getErrDes());
            }
        });
    }

    public void getFollowSpeed() {
        this.activity.getFcManager().getAiFollowSpeed(new UiCallBackListener<AckAiFollowGetSpeed>() {
            public void onComplete(CmdResult cmdResult, AckAiFollowGetSpeed o) {
                if (cmdResult.isSuccess()) {
                    X8AiFollowExcuteController.this.isGetSpeed = true;
                    TcpClient.getIntance().sendLog("..getFollowSpeed.. " + cmdResult.getErrDes() + " speed=" + o.getSpeed());
                    X8AiFollowExcuteController.this.vSpeedContainer.setSpeed(o.getSpeed());
                }
            }
        });
    }

    public void cancleByModeChange(int mode) {
        boolean z = true;
        if (mode != 1) {
            z = false;
        }
        onTaskComplete(z);
        setAiVcCloseByTaskModeChange();
    }

    public void onTaskComplete(boolean b) {
        closeFollow();
        String s = "";
        switch (this.type) {
            case 0:
                s = String.format(getString(R.string.x8_ai_done), new Object[]{getString(R.string.x8_ai_fly_follow_normal)});
                break;
            case 1:
                s = String.format(getString(R.string.x8_ai_done), new Object[]{getString(R.string.x8_ai_fly_follow_parallel)});
                break;
            case 2:
                s = String.format(getString(R.string.x8_ai_done), new Object[]{getString(R.string.x8_ai_fly_follow_lockup)});
                break;
        }
        if (this.listener != null) {
            this.listener.onComplete(s, b);
        }
    }

    private void closeFollow() {
        closeUi();
        if (this.listener != null) {
            this.listener.onAiFollowExcuteBackClick();
        }
    }

    public void onDisconnectTaskComplete() {
        closeFollow();
        String s = "";
        switch (this.type) {
            case 0:
                s = String.format(getString(R.string.x8_ai_done), new Object[]{getString(R.string.x8_ai_fly_follow_normal)});
                break;
            case 1:
                s = String.format(getString(R.string.x8_ai_done), new Object[]{getString(R.string.x8_ai_fly_follow_parallel)});
                break;
            case 2:
                s = String.format(getString(R.string.x8_ai_done), new Object[]{getString(R.string.x8_ai_fly_follow_lockup)});
                break;
        }
        if (this.listener != null) {
            this.listener.onComplete(s, false);
        }
    }

    public void showExitDialog() {
        String t = "";
        String m = getString(R.string.x8_ai_fly_follow_exit_msg);
        switch (this.type) {
            case 0:
                t = getString(R.string.x8_ai_fly_follow_normal);
                break;
            case 1:
                t = getString(R.string.x8_ai_fly_follow_parallel);
                break;
            case 2:
                t = getString(R.string.x8_ai_fly_follow_lockup);
                break;
        }
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        this.dialog.show();
    }

    public void onLeft() {
    }

    public void onRight() {
        sendExiteCmd();
    }

    public void taskExit() {
        onTaskComplete(false);
    }

    public void sendExiteCmd() {
        this.activity.getFcManager().setAiVcClose(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    TcpClient.getIntance().sendLog("setAiVcClose success  " + cmdResult.getErrDes() + X8AiFollowExcuteController.this.mX8AiFollowState);
                    if (X8AiFollowExcuteController.this.mX8AiFollowState != X8AiFollowState.RUNNING) {
                        X8AiFollowExcuteController.this.taskExit();
                        return;
                    }
                    return;
                }
                TcpClient.getIntance().sendLog("setAiVcClose error" + cmdResult.getErrDes());
            }
        });
    }

    public void setAiVcCloseByTaskModeChange() {
        this.activity.getFcManager().setAiVcClose(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                TcpClient.getIntance().sendLog("setAiVcCloseByTaskModeChange   " + cmdResult.getErrDes());
            }
        });
    }

    public void setFollowExit(int mode) {
        this.activity.getFcManager().setFollowExit(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    TcpClient.getIntance().sendLog("退出跟随 success  " + cmdResult.getErrDes());
                } else {
                    TcpClient.getIntance().sendLog("退出跟随失败  " + cmdResult.getErrDes());
                }
            }
        });
    }

    public void onDroneConnected(boolean b) {
        if (!this.isShow) {
            return;
        }
        if (b) {
            if (this.mX8AiFollowState == X8AiFollowState.RUNNING) {
                if (!this.isGetMode) {
                    getFollowMode();
                } else if (this.vModeImtes.getVisibility() != 0) {
                    this.vModeImtes.setVisibility(0);
                }
                if (this.isGetSpeed && this.isGetMode && this.type == 2 && this.vSpeedContainer.getVisibility() != 0) {
                    this.vSpeedContainer.setVisibility(0);
                }
            } else {
                sysAiVcCtrlMode();
            }
            if (!this.isGetSpeed) {
                getFollowSpeed();
                return;
            }
            return;
        }
        onDisconnectTaskComplete();
    }

    public void onModeSelect(int mode) {
        TcpClient.getIntance().sendLog("onModeSelect success  " + mode);
        setFollowExit(mode);
    }

    public void onSendSpeed(int speed) {
        setFollowSpeed(speed);
    }

    public void setFollowSpeed(final int speed) {
        this.activity.getFcManager().setAiFollowSpeed(speed, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                TcpClient.getIntance().sendLog("..setFollowSpeed.. " + cmdResult.getErrDes() + " speed=" + speed);
                if (cmdResult.isSuccess()) {
                }
            }
        });
    }

    public void switchUnityEvent() {
        if (this.isShow) {
            this.vSpeedContainer.switchUnity();
        }
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void sysAiVcCtrlMode() {
        if (this.mX8AiFollowState == X8AiFollowState.RUNNING) {
            return;
        }
        if (this.timeSend == 0) {
            this.timeSend = 1;
            this.activity.getFcManager().sysCtrlMode2AiVc(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                }
            }, X8Task.VCM_FOLLOW.ordinal());
            return;
        }
        this.timeSend = 0;
    }
}
