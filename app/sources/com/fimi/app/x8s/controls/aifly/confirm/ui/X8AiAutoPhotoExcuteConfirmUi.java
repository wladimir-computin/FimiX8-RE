package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.controls.aifly.X8AiAutoPhototExcuteController;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8SeekBarView;
import com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.widget.SwitchButton;
import com.fimi.widget.SwitchButton.OnSwitchListener;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.cmd.CmdAiAutoPhoto;

public class X8AiAutoPhotoExcuteConfirmUi implements OnClickListener, OnSwitchListener {
    private float DISTANCE_DEFALOUT = (30.0f - this.DISTANCE_MIN);
    private float DISTANCE_MAX = 300.0f;
    private int DISTANCE_MAX_PROGRESS = ((int) ((this.DISTANCE_MAX - this.DISTANCE_MIN) * 10.0f));
    private float DISTANCE_MIN = 1.0f;
    private float SPEED_DEFALOUT = (3.0f - this.SPEED_MIN);
    private float SPEED_MAX = 10.0f;
    private int SPEED_MAX_PROGRESS = ((int) ((this.SPEED_MAX - this.SPEED_MIN) * 10.0f));
    private float SPEED_MIN = 1.0f;
    private int angle;
    private View btnOk;
    private View contentView;
    private SlideChangeListener distanceListener = new SlideChangeListener() {
        public void onStart(X8SeekBarView slideView, int progress) {
        }

        public void onProgress(X8SeekBarView slideView, int progress) {
            X8AiAutoPhotoExcuteConfirmUi.this.tvDistance.setText(X8NumberUtil.getDistanceNumberString(X8AiAutoPhotoExcuteConfirmUi.this.DISTANCE_MIN + (((float) progress) / 10.0f), 1, true));
            X8AiAutoPhotoExcuteConfirmUi.this.setValue(X8AiAutoPhotoExcuteConfirmUi.this.angle, X8AiAutoPhotoExcuteConfirmUi.this.item);
        }

        public void onStop(X8SeekBarView slideView, int progress) {
        }
    };
    private FcManager fcManager;
    private View imgReturn;
    private int item;
    private IX8NextViewListener listener;
    private X8MainAiFlyController mX8MainAiFlyController;
    private X8SeekBarView sbDistance;
    private X8SeekBarView sbSpeed;
    private SlideChangeListener speedListener = new SlideChangeListener() {
        public void onStart(X8SeekBarView slideView, int progress) {
        }

        public void onProgress(X8SeekBarView slideView, int progress) {
            X8AiAutoPhotoExcuteConfirmUi.this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString(X8AiAutoPhotoExcuteConfirmUi.this.SPEED_MIN + (((float) progress) / 10.0f), 1, true));
            X8AiAutoPhotoExcuteConfirmUi.this.setValue(X8AiAutoPhotoExcuteConfirmUi.this.angle, X8AiAutoPhotoExcuteConfirmUi.this.item);
        }

        public void onStop(X8SeekBarView slideView, int progress) {
        }
    };
    private SwitchButton swbAutoReturn;
    private TextView tvContent;
    private TextView tvDistance;
    private TextView tvSpeed;
    private TextView tvTime;
    private TextView tvTitle;
    private View vDistanceMinus;
    private View vDistancePlus;
    private View vSpeedMinus;
    private View vSpeedPlus;
    private X8AiAutoPhototExcuteController x8AiAutoPhototExcuteController;

    public X8AiAutoPhotoExcuteConfirmUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_auto_photo_excute_confirm_layout, (ViewGroup) parent, true);
        initViews(this.contentView);
        initActions();
    }

    public void initViews(View rootView) {
        this.imgReturn = rootView.findViewById(R.id.img_ai_return);
        this.btnOk = rootView.findViewById(R.id.btn_ai_confirm_ok);
        this.tvTitle = (TextView) rootView.findViewById(R.id.tv_ai_title);
        this.tvContent = (TextView) rootView.findViewById(R.id.tv_ai_next_content1);
        this.tvTime = (TextView) rootView.findViewById(R.id.tv_ai_time);
        this.tvSpeed = (TextView) rootView.findViewById(R.id.tv_ai_speed);
        this.tvDistance = (TextView) rootView.findViewById(R.id.tv_ai_distance);
        this.vSpeedMinus = rootView.findViewById(R.id.rl_speed_minus);
        this.vSpeedPlus = rootView.findViewById(R.id.rl_speed_plus);
        this.sbSpeed = (X8SeekBarView) rootView.findViewById(R.id.sb_speed);
        this.sbSpeed.setMaxProgress(this.SPEED_MAX_PROGRESS);
        this.vDistanceMinus = rootView.findViewById(R.id.rl_distance_minus);
        this.vDistancePlus = rootView.findViewById(R.id.rl_distance_plus);
        this.sbDistance = (X8SeekBarView) rootView.findViewById(R.id.sb_distance);
        this.sbDistance.setMaxProgress(this.DISTANCE_MAX_PROGRESS);
        this.swbAutoReturn = (SwitchButton) rootView.findViewById(R.id.swb_ai_auto_return);
        this.swbAutoReturn.setEnabled(true);
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.swbAutoReturn.setOnSwitchListener(this);
        this.vSpeedMinus.setOnClickListener(this);
        this.vSpeedPlus.setOnClickListener(this);
        this.sbSpeed.setOnSlideChangeListener(this.speedListener);
        this.vDistanceMinus.setOnClickListener(this);
        this.vDistancePlus.setOnClickListener(this);
        this.sbDistance.setOnSlideChangeListener(this.distanceListener);
        this.sbSpeed.setProgress((int) (this.SPEED_DEFALOUT * 10.0f));
        this.sbDistance.setProgress((int) (this.DISTANCE_DEFALOUT * 10.0f));
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id != R.id.img_ai_return) {
            int s;
            if (id == R.id.btn_ai_confirm_ok) {
                setAiAutoPhotoValueCmd();
            } else if (id == R.id.rl_speed_minus) {
                if (this.sbSpeed.getProgress() != 0) {
                    s = this.sbSpeed.getProgress() - 10;
                    if (s < 0) {
                        s = 0;
                    }
                    this.sbSpeed.setProgress(s);
                }
            } else if (id == R.id.rl_speed_plus) {
                if (this.sbSpeed.getProgress() != this.SPEED_MAX_PROGRESS) {
                    s = this.sbSpeed.getProgress() + 10;
                    if (s > this.SPEED_MAX_PROGRESS) {
                        s = this.SPEED_MAX_PROGRESS;
                    }
                    this.sbSpeed.setProgress(s);
                }
            } else if (id == R.id.rl_distance_minus) {
                if (this.sbDistance.getProgress() != 0) {
                    s = this.sbDistance.getProgress() - 10;
                    if (s < 0) {
                        s = 0;
                    }
                    this.sbDistance.setProgress(s);
                }
            } else if (id == R.id.rl_distance_plus) {
                s = this.sbDistance.getProgress() + 10;
                if (s > this.DISTANCE_MAX_PROGRESS) {
                    s = this.DISTANCE_MAX_PROGRESS;
                }
                if (this.sbDistance.getProgress() != this.DISTANCE_MAX_PROGRESS) {
                    this.sbDistance.setProgress(s);
                }
            }
        }
    }

    public void setListener(IX8NextViewListener listener, FcManager fcManager, X8AiAutoPhototExcuteController mX8AiAutoPhototExcuteController) {
        this.listener = listener;
        this.fcManager = fcManager;
        this.x8AiAutoPhototExcuteController = mX8AiAutoPhototExcuteController;
    }

    public void setValue(int angle, int item) {
        float speed;
        float distance;
        if (item == 0) {
            this.angle = Math.abs(angle);
            this.item = item;
            String angleStr = "" + NumberUtil.decimalPointStr(((double) angle) / 100.0d, 1);
            this.tvContent.setText(String.format(this.contentView.getResources().getString(R.string.x8_ai_auto_photo_tip4), new Object[]{"" + angleStr}));
            speed = this.SPEED_MIN + (((float) this.sbSpeed.getProgress()) / 10.0f);
            distance = this.DISTANCE_MIN + (((float) this.sbDistance.getProgress()) / 10.0f);
            this.tvTime.setText("" + String.format("%.2f", new Object[]{Float.valueOf(distance / speed)}) + "S");
            this.tvTitle.setText(this.contentView.getResources().getString(R.string.x8_ai_auto_photo_title));
            return;
        }
        this.angle = Math.abs(9000);
        this.item = item;
        this.tvContent.setText(this.contentView.getResources().getString(R.string.x8_ai_auto_photo_vertical_next_tip1));
        speed = this.SPEED_MIN + (((float) this.sbSpeed.getProgress()) / 10.0f);
        distance = this.DISTANCE_MIN + (((float) this.sbDistance.getProgress()) / 10.0f);
        this.tvTime.setText("" + String.format("%.2f", new Object[]{Float.valueOf(distance / speed)}) + "S");
        this.tvTitle.setText(this.contentView.getResources().getString(R.string.x8_ai_auto_photo_vertical_title));
    }

    public void onSwitch(View view, boolean on) {
        if (on) {
            this.swbAutoReturn.setSwitchState(false);
        } else {
            this.swbAutoReturn.setSwitchState(true);
        }
    }

    public void setAiAutoPhotoValueCmd() {
        int i = 1;
        CmdAiAutoPhoto cmd = new CmdAiAutoPhoto();
        cmd.speed = (int) ((this.SPEED_MIN * 10.0f) + ((float) this.sbSpeed.getProgress()));
        cmd.config = this.swbAutoReturn.getToggleOn() ? 1 : 0;
        cmd.angle = this.angle;
        if (this.item != 0) {
            i = 0;
        }
        cmd.mode = i;
        cmd.routeLength = (int) ((this.DISTANCE_MIN * 10.0f) + ((float) this.sbDistance.getProgress()));
        this.fcManager.setAiAutoPhotoValue(cmd, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiAutoPhotoExcuteConfirmUi.this.startAiAutoPhoto();
                }
            }
        });
    }

    public void startAiAutoPhoto() {
        this.fcManager.setAiAutoPhotoExcute(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiAutoPhotoExcuteConfirmUi.this.listener.onExcuteClick();
                }
            }
        });
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        if (isInSky && isLowPower) {
            this.btnOk.setEnabled(true);
        } else {
            this.btnOk.setEnabled(false);
        }
    }
}
