package com.fimi.app.x8s.controls.aifly;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiFixedwingListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AutoFixedwingState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AiFixedwingExcuteController extends AbsX8AiController implements OnClickListener, onDialogButtonClickListener {
    protected int MAX_WIDTH;
    private Activity activity;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private ImageView imgBack;
    protected boolean isShow;
    private IX8AiFixedwingListener listener;
    private FcCtrlManager mFcCtrlManager;
    private X8AiTipWithCloseView mTipBgView;
    int speedState = 0;
    private TextView tvFlag;
    protected int width = X8Application.ANIMATION_WIDTH;

    public void onLeft() {
    }

    public void onRight() {
        setTypeDisEnable();
    }

    public X8AiFixedwingExcuteController(Activity activity, View rootView) {
        super(rootView);
        this.activity = activity;
    }

    public void setListener(IX8AiFixedwingListener listener) {
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
            showExitDialog();
        } else if (id != R.id.rl_flag_small) {
        } else {
            if (this.tvFlag.getVisibility() == 0) {
                this.tvFlag.setVisibility(8);
            } else {
                this.tvFlag.setVisibility(0);
            }
        }
    }

    public void openUi() {
        this.isShow = true;
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_ai_fixedwing_excute_layout, (ViewGroup) this.rootView, true);
        this.mTipBgView = (X8AiTipWithCloseView) this.handleView.findViewById(R.id.v_content_tip);
        this.imgBack = (ImageView) this.handleView.findViewById(R.id.img_ai_follow_back);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.tvFlag = (TextView) this.handleView.findViewById(R.id.tv_task_tip);
        this.listener.onFixedwingRunning();
        this.imgBack.setOnClickListener(this);
        this.flagSmall.setOnClickListener(this);
        super.openUi();
    }

    public void closeUi() {
        this.isShow = false;
        super.closeUi();
    }

    public void setFcManager(FcCtrlManager fcManager) {
        this.mFcCtrlManager = fcManager;
    }

    public boolean isShow() {
        if (StateManager.getInstance().getX8Drone().getCtrlMode() == 13) {
            return false;
        }
        return this.isShow;
    }

    public void showExitDialog() {
        String t = "";
        String m = "";
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_ai_fixedwing_exite_title), this.rootView.getContext().getString(R.string.x8_ai_fixedwing_exite_tip), this);
        this.dialog.show();
    }

    public void taskExit() {
        onTaskComplete();
    }

    public void cancleByModeChange() {
        onTaskComplete();
    }

    public void onDroneConnected(boolean b) {
        if (this.isShow) {
            if (!b) {
                ononDroneDisconnectedTaskComplete();
            }
            AutoFixedwingState state = StateManager.getInstance().getX8Drone().getAutoFixedwingState();
            if (state == null) {
                return;
            }
            if (state.getFixedwingphase() == 0) {
                this.speedState = 1;
                if (!this.mTipBgView.isClose()) {
                    String h1 = X8NumberUtil.getDistanceNumberString(5.0f, 0, false);
                    String vs1 = X8NumberUtil.getSpeedNumberString(3.0f, 0, false);
                    this.mTipBgView.setTipText(String.format(this.handleView.getContext().getString(R.string.x8_ai_fixedwing_speed_tip1), new Object[]{vs1, h1}));
                    this.mTipBgView.showTip();
                }
            } else if (this.speedState == 1) {
                this.speedState = 2;
                this.mTipBgView.setTipText(this.handleView.getContext().getString(R.string.x8_ai_fixedwing_speed_tip3));
                this.mTipBgView.showTip();
            } else if (!this.mTipBgView.isClose()) {
                this.mTipBgView.setTipText(this.handleView.getContext().getString(R.string.x8_ai_fixedwing_speed_tip3));
                this.mTipBgView.showTip();
            }
        }
    }

    private void onTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onFixedwingComplete(true);
        }
    }

    public void ononDroneDisconnectedTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onFixedwingComplete(false);
        }
    }

    private void closeFixedwing() {
        closeUi();
        if (this.listener != null) {
            this.listener.onFixedwingBackClick();
        }
    }

    public void setTypeDisEnable() {
        this.mFcCtrlManager.setDisenableFixwing(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiFixedwingExcuteController.this.taskExit();
                }
            }
        });
    }
}
