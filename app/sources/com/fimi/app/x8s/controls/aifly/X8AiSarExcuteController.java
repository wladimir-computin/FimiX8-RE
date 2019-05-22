package com.fimi.app.x8s.controls.aifly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiSarListener;
import com.fimi.app.x8s.manager.X8ScreenShotManager;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8VerticalSeekBarValueLayout;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.modulestate.X8CameraSettings;

public class X8AiSarExcuteController extends AbsX8AiController implements OnClickListener, onDialogButtonClickListener {
    protected int MAX_WIDTH;
    private X8sMainActivity activity;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private ImageView imgBack;
    private ImageView imgShot;
    private ImageView imgSwith;
    protected boolean isShow;
    private IX8AiSarListener listener;
    private FcCtrlManager mFcCtrlManager;
    private X8VerticalSeekBarValueLayout sbLayout;
    private TextView tvFlag;
    private TextView tvLatlng;
    private TextView tvTime;
    protected int width = X8Application.ANIMATION_WIDTH;

    public void onLeft() {
    }

    public void onRight() {
        taskExit();
    }

    public X8AiSarExcuteController(X8sMainActivity activity, View rootView) {
        super(rootView);
        this.activity = activity;
    }

    public void setListener(IX8AiSarListener listener) {
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
        } else if (id == R.id.img_ai_map_switch) {
            if (GlobalConfig.getInstance().getMapStyle() == Constants.X8_GENERAL_MAP_STYLE_NORMAL) {
                GlobalConfig.getInstance().setMapStyle(Constants.X8_GENERAL_MAP_STYLE_SATELLITE);
                this.activity.getmMapVideoController().getFimiMap().switchMapStyle(Constants.X8_GENERAL_MAP_STYLE_SATELLITE);
                return;
            }
            GlobalConfig.getInstance().setMapStyle(Constants.X8_GENERAL_MAP_STYLE_NORMAL);
            this.activity.getmMapVideoController().getFimiMap().switchMapStyle(Constants.X8_GENERAL_MAP_STYLE_NORMAL);
        } else if (id == R.id.img_ai_screen_shot) {
            if (!X8ScreenShotManager.isBusy) {
                new X8ScreenShotManager().starThread(this.activity);
            }
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
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_ai_sar_excute_layout, (ViewGroup) this.rootView, true);
        this.tvLatlng = (TextView) this.handleView.findViewById(R.id.tv_latlng);
        this.tvTime = (TextView) this.handleView.findViewById(R.id.tv_time);
        this.imgBack = (ImageView) this.handleView.findViewById(R.id.img_ai_follow_back);
        this.imgSwith = (ImageView) this.handleView.findViewById(R.id.img_ai_map_switch);
        this.imgShot = (ImageView) this.handleView.findViewById(R.id.img_ai_screen_shot);
        this.sbLayout = (X8VerticalSeekBarValueLayout) this.handleView.findViewById(R.id.sb_switch_focus);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.tvFlag = (TextView) this.handleView.findViewById(R.id.tv_task_tip);
        this.flagSmall.setOnClickListener(this);
        this.listener.onAiSarRunning();
        this.imgBack.setOnClickListener(this);
        this.imgSwith.setOnClickListener(this);
        this.imgShot.setOnClickListener(this);
        if (this.activity.getmMapVideoController().isFullVideo()) {
            this.sbLayout.setVisibility(0);
            this.imgSwith.setVisibility(8);
        } else {
            this.imgSwith.setVisibility(0);
            this.sbLayout.setVisibility(8);
        }
        this.sbLayout.setMinMax(X8CameraSettings.getMinMaxFocuse(), this.activity.getCameraManager());
        this.sbLayout.setProgress(X8CameraSettings.getFocuse());
        super.openUi();
    }

    public void changeProcessByRc(boolean b) {
        if (this.sbLayout != null) {
            this.sbLayout.changeProcess(b);
        }
    }

    public void closeUi() {
        this.isShow = false;
        super.closeUi();
    }

    public void setFcManager(FcCtrlManager fcManager) {
        this.mFcCtrlManager = fcManager;
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
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_ai_fixedwing_exite_title), this.rootView.getContext().getString(R.string.x8_ai_fly_sar_exite_tip), this);
        this.dialog.show();
    }

    public void taskExit() {
        onTaskComplete(true);
    }

    public void cancleByModeChange() {
        onTaskComplete(false);
    }

    public void onDroneConnected(boolean b) {
        if (this.isShow) {
            if (!b) {
                ononDroneDisconnectedTaskComplete();
            }
            if (b && this.tvTime != null) {
                double[] position = this.activity.getmMapVideoController().getFimiMap().getDevicePosition();
                if (position != null) {
                    this.tvLatlng.setText("" + position[0] + "," + position[1]);
                    this.tvTime.setText(DateUtil.getStringByFormat(System.currentTimeMillis(), "yyyyMMdd HH:mm:ss"));
                }
            }
        }
    }

    private void onTaskComplete(boolean b) {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onAiSarComplete(b);
        }
    }

    public void ononDroneDisconnectedTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onAiSarComplete(false);
        }
    }

    private void closeFixedwing() {
        closeUi();
        if (this.listener != null) {
            this.listener.onAiSarBackClick();
        }
    }

    public void setTypeEnable() {
        this.mFcCtrlManager.setEnableTripod(0, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiSarExcuteController.this.taskExit();
                }
            }
        });
    }

    public void switchMapVideo(boolean sightFlag) {
        if (this.handleView == null || !this.isShow) {
            return;
        }
        if (sightFlag) {
            this.imgSwith.setVisibility(0);
            this.sbLayout.setVisibility(8);
            return;
        }
        this.sbLayout.setVisibility(0);
        this.imgSwith.setVisibility(8);
    }

    public void setd() {
    }
}
