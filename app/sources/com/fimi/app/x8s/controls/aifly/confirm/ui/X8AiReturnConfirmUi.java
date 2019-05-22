package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AiReturnConfirmUi implements OnClickListener, OnSeekBarChangeListener {
    private int MAX = 0;
    private int MIN = 0;
    private int accuracy = 10;
    private View btnOk;
    private View contentView;
    private ImageView imgFlag;
    private View imgReturn;
    private X8MainAiFlyController listener;
    private FcCtrlManager mFcCtrlManager;
    private SeekBar mSeekBar;
    private X8MainAiFlyController mX8MainAiFlyController;
    private String prex;
    private String prex2;
    int res = 0;
    private View rlMinus;
    private View rlPlus;
    private float seekBarMax = (120.0f * ((float) this.accuracy));
    private float seekBarMin = (30.0f * ((float) this.accuracy));
    float temp = 0.0f;
    int tmpRes = 0;
    private TextView tvCuurentHeight;
    private TextView tvHeight;

    public X8AiReturnConfirmUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_return_layout, (ViewGroup) parent, true);
        this.MAX = (int) (this.seekBarMax - this.seekBarMin);
        initViews(this.contentView);
        initActions();
    }

    public void setX8MainAiFlyController(X8MainAiFlyController mX8MainAiFlyController, FcCtrlManager mFcCtrlManager) {
        this.mX8MainAiFlyController = mX8MainAiFlyController;
        this.mFcCtrlManager = mFcCtrlManager;
        getHeight();
    }

    public void initViews(View rootView) {
        this.imgReturn = rootView.findViewById(R.id.img_ai_follow_return);
        this.btnOk = rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.prex = rootView.getContext().getString(R.string.x8_ai_fly_return_home_tip);
        this.prex2 = rootView.getContext().getString(R.string.x8_ai_fly_return_home_tip2);
        this.tvCuurentHeight = (TextView) rootView.findViewById(R.id.tv_ai_follow_confirm_title1);
        this.tvHeight = (TextView) rootView.findViewById(R.id.tv_limit_height);
        this.rlMinus = rootView.findViewById(R.id.rl_minus);
        this.rlPlus = rootView.findViewById(R.id.rl_plus);
        this.mSeekBar = (SeekBar) rootView.findViewById(R.id.sb_value);
        this.mSeekBar.setMax(this.MAX);
        this.imgFlag = (ImageView) rootView.findViewById(R.id.img_ai_return_flag);
        AutoFcSportState state = StateManager.getInstance().getX8Drone().getFcSportState();
        if (state != null) {
            showSportState(state);
        }
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.rlMinus.setOnClickListener(this);
        this.rlPlus.setOnClickListener(this);
        this.mSeekBar.setOnSeekBarChangeListener(this);
    }

    public void getHeight() {
        setProgress((int) (StateManager.getInstance().getX8Drone().getReturnHomeHight() * ((float) this.accuracy)));
        this.mFcCtrlManager.getReturnHomeHeight(new UiCallBackListener<AckGetRetHeight>() {
            public void onComplete(CmdResult cmdResult, AckGetRetHeight aFloat) {
            }
        });
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_return) {
            this.mX8MainAiFlyController.onCloseConfirmUi();
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            onComfirnClick();
        } else if (id == R.id.rl_minus) {
            if (this.mSeekBar.getProgress() != this.MIN) {
                this.mSeekBar.setProgress(this.mSeekBar.getProgress() - (this.accuracy * 1));
                setHeightLimit();
            }
        } else if (id == R.id.rl_plus && this.mSeekBar.getProgress() != this.MAX) {
            this.mSeekBar.setProgress(this.mSeekBar.getProgress() + (this.accuracy * 1));
            setHeightLimit();
        }
    }

    public void setProgress(int progress) {
        this.mSeekBar.setProgress((int) (((float) progress) - this.seekBarMin));
        this.tvHeight.setText(this.prex2 + X8NumberUtil.getDistanceNumberString((((float) progress) * 1.0f) / ((float) this.accuracy), 1, true));
    }

    public void setProgress1(float progress) {
        this.tvHeight.setText(this.prex2 + X8NumberUtil.getDistanceNumberString((progress + this.seekBarMin) / ((float) this.accuracy), 1, true));
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setProgress1((float) progress);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        setHeightLimit();
    }

    public void onComfirnClick() {
        this.mX8MainAiFlyController.onRetureHomeClick();
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        if (isInSky && isLowPower) {
            this.btnOk.setEnabled(true);
        } else {
            this.btnOk.setEnabled(false);
        }
    }

    public void showSportState(AutoFcSportState state) {
        float h = state.getHeight();
        if (state.getHomeDistance() <= 10.0f) {
            if (h <= 3.0f) {
                this.temp = 3.0f;
                this.tmpRes = R.drawable.x8_img_ai_return_1;
            } else {
                this.temp = h;
                this.tmpRes = R.drawable.x8_img_ai_return_2;
            }
        } else if (h <= StateManager.getInstance().getX8Drone().getReturnHomeHight()) {
            this.temp = StateManager.getInstance().getX8Drone().getReturnHomeHight();
            this.tmpRes = R.drawable.x8_img_ai_return_3;
        } else {
            this.temp = h;
            this.tmpRes = R.drawable.x8_img_ai_return_4;
        }
        this.tvCuurentHeight.setText(String.format(this.prex, new Object[]{X8NumberUtil.getDistanceNumberString(this.temp, 1, true)}));
        if (this.tmpRes != 0 && this.tmpRes != this.res) {
            this.res = this.tmpRes;
            this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.contentView.getContext(), this.res));
        }
    }

    public void setHeightLimit() {
        final float h = (((float) this.mSeekBar.getProgress()) + this.seekBarMin) / ((float) this.accuracy);
        this.mFcCtrlManager.setReturnHome(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    StateManager.getInstance().getX8Drone().setReturnHomeHight(h);
                } else {
                    X8AiReturnConfirmUi.this.setProgress((int) (StateManager.getInstance().getX8Drone().getReturnHomeHight() * ((float) X8AiReturnConfirmUi.this.accuracy)));
                }
            }
        }, h);
    }
}
