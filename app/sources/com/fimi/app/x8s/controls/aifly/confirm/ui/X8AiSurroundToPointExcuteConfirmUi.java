package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.aifly.X8AiSurroundExcuteController;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AiSurroundToPointExcuteConfirmUi implements OnClickListener, OnSeekBarChangeListener {
    private float DEFAULE_SPEED = 2.0f;
    private float MAX = 8.0f;
    private int MAX_PROGRESS = ((int) ((this.MAX - this.MIN) * 10.0f));
    private float MIN = 1.0f;
    private Button btnGo;
    private View contentView;
    private FcManager fcManager;
    private ImageView imgBack;
    private IX8NextViewListener listener;
    private double perimeter = 0.0d;
    private int r;
    private SeekBar sbSeekBar;
    private TextView tvOrientation1;
    private TextView tvOrientation2;
    private TextView tvRotation1;
    private TextView tvRotation2;
    private TextView tvSpeed;
    private TextView tvTime;
    private View vMinus;
    private View vPlus;
    private X8AiSurroundExcuteController x8AiSurroundExcuteController;

    public void setListener(IX8NextViewListener listener, FcManager fcManager, X8AiSurroundExcuteController x8AiSurroundExcuteController) {
        this.listener = listener;
        this.fcManager = fcManager;
        this.x8AiSurroundExcuteController = x8AiSurroundExcuteController;
    }

    public X8AiSurroundToPointExcuteConfirmUi(Activity activity, View parent, float radius) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_surround_to_point__excute_confirm_layout, (ViewGroup) parent, true);
        this.r = Math.round(radius);
        this.MAX = (float) Math.sqrt(((double) this.r) * 1.5d);
        if (this.MAX > 10.0f) {
            this.MAX = 10.0f;
        }
        this.MAX_PROGRESS = (int) ((this.MAX - this.MIN) * 10.0f);
        initView(this.contentView);
        initAction();
        setRadius(this.r);
    }

    public void initView(View rootView) {
        this.imgBack = (ImageView) rootView.findViewById(R.id.img_ai_follow_return);
        this.tvTime = (TextView) rootView.findViewById(R.id.tv_ai_time);
        this.tvSpeed = (TextView) rootView.findViewById(R.id.tv_ai_follow_speed);
        this.tvRotation1 = (TextView) rootView.findViewById(R.id.tv_ai_rotation1);
        this.tvRotation2 = (TextView) rootView.findViewById(R.id.tv_ai_rotation2);
        this.tvOrientation1 = (TextView) rootView.findViewById(R.id.tv_ai_orientation1);
        this.tvOrientation2 = (TextView) rootView.findViewById(R.id.tv_ai_orientation2);
        this.tvRotation1.setSelected(true);
        this.tvOrientation1.setSelected(true);
        this.vMinus = rootView.findViewById(R.id.rl_minus);
        this.sbSeekBar = (SeekBar) rootView.findViewById(R.id.sb_value);
        this.vPlus = rootView.findViewById(R.id.rl_plus);
        this.btnGo = (Button) rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.sbSeekBar.setMax(this.MAX_PROGRESS);
    }

    public void initAction() {
        this.imgBack.setOnClickListener(this);
        this.tvTime.setOnClickListener(this);
        this.tvSpeed.setOnClickListener(this);
        this.vMinus.setOnClickListener(this);
        this.sbSeekBar.setOnSeekBarChangeListener(this);
        this.vPlus.setOnClickListener(this);
        this.btnGo.setOnClickListener(this);
        this.tvRotation1.setOnClickListener(this);
        this.tvRotation2.setOnClickListener(this);
        this.tvOrientation1.setOnClickListener(this);
        this.tvOrientation2.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_minus) {
            if (this.sbSeekBar.getProgress() != 0) {
                this.sbSeekBar.setProgress(this.sbSeekBar.getProgress() - 10);
            }
        } else if (id == R.id.rl_plus) {
            if (this.sbSeekBar.getProgress() != this.MAX_PROGRESS) {
                this.sbSeekBar.setProgress(this.sbSeekBar.getProgress() + 10);
            }
        } else if (id == R.id.img_ai_follow_return) {
            this.listener.onBackClick();
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            onGoClick();
        } else if (id == R.id.tv_ai_rotation1) {
            this.tvRotation1.setSelected(true);
            this.tvRotation2.setSelected(false);
        } else if (id == R.id.tv_ai_rotation2) {
            this.tvRotation1.setSelected(false);
            this.tvRotation2.setSelected(true);
        } else if (id == R.id.tv_ai_orientation1) {
            this.tvOrientation1.setSelected(true);
            this.tvOrientation2.setSelected(false);
        } else if (id == R.id.tv_ai_orientation2) {
            this.tvOrientation1.setSelected(false);
            this.tvOrientation2.setSelected(true);
        }
    }

    public void setRadius(int radius) {
        setPerimeter(6.283185307179586d * ((double) radius));
        setSpeed(this.DEFAULE_SPEED);
        setViewValue();
    }

    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }

    private void setSpeed(float speed) {
        this.sbSeekBar.setProgress((int) (10.0f * speed));
    }

    public void setViewValue() {
        float speed = this.MIN + (((float) this.sbSeekBar.getProgress()) / 10.0f);
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString(speed, 1, true));
        this.tvTime.setText("" + ((int) Math.round(this.perimeter / ((double) speed))) + "S");
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float speed = this.MIN + (((float) progress) / 10.0f);
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString(speed, 1, true));
        this.tvTime.setText("" + ((int) Math.round(this.perimeter / ((double) speed))) + "S");
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onGoClick() {
        if (StateManager.getInstance().getX8Drone().getHeight() >= 5.0f) {
            setSpeed();
        } else {
            X8ToastUtil.showToast(this.contentView.getContext(), this.contentView.getContext().getString(R.string.height_tip), 0);
        }
    }

    public void startExcute() {
        this.fcManager.setAiSurroundExcute(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiSurroundToPointExcuteConfirmUi.this.x8AiSurroundExcuteController.setSpeedMax((int) (X8AiSurroundToPointExcuteConfirmUi.this.MAX * 10.0f));
                    X8AiSurroundToPointExcuteConfirmUi.this.listener.onExcuteClick();
                    int s = (int) ((X8AiSurroundToPointExcuteConfirmUi.this.MIN + (((float) X8AiSurroundToPointExcuteConfirmUi.this.sbSeekBar.getProgress()) / 10.0f)) * 10.0f);
                    if (!X8AiSurroundToPointExcuteConfirmUi.this.tvRotation1.isSelected() && X8AiSurroundToPointExcuteConfirmUi.this.tvRotation2.isSelected()) {
                        s = 0 - s;
                    }
                    X8AiSurroundToPointExcuteConfirmUi.this.x8AiSurroundExcuteController.setSpeed(s);
                }
            }
        });
    }

    public void setSpeed() {
        int s = (int) ((this.MIN + (((float) this.sbSeekBar.getProgress()) / 10.0f)) * 10.0f);
        if (!this.tvRotation1.isSelected() && this.tvRotation2.isSelected()) {
            s = 0 - s;
        }
        this.fcManager.setAiSurroundSpeed(s, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiSurroundToPointExcuteConfirmUi.this.setOrientation();
                }
            }
        });
    }

    public void setOrientation() {
        int type = 0;
        if (this.tvOrientation1.isSelected()) {
            type = 0;
        } else if (this.tvOrientation2.isSelected()) {
            type = 1;
        }
        this.fcManager.setAiSurroundOrientation(type, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiSurroundToPointExcuteConfirmUi.this.startExcute();
                }
            }
        });
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        if (isInSky && isLowPower) {
            this.btnGo.setEnabled(true);
        } else {
            this.btnGo.setEnabled(false);
        }
    }
}
