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
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AiPoint2PointExcuteConfirmUi implements OnClickListener, OnSeekBarChangeListener {
    private float DEFAULE_SPEED = 2.0f;
    private float MAX = 10.0f;
    private int MAX_PROGRESS = ((int) ((this.MAX - this.MIN) * 10.0f));
    private float MIN = 1.0f;
    private float alt;
    private Button btnGo;
    private View contentView;
    private int distance = 0;
    private FcManager fcManager;
    private float height;
    private ImageView imgBack;
    private IX8NextViewListener listener;
    private MapPointLatLng mapPoint;
    private SeekBar sbSeekBar;
    private TextView tvDistance;
    private TextView tvSpeed;
    private TextView tvTime;
    private View vMinus;
    private View vPlus;

    public void setListener(IX8NextViewListener listener, FcManager fcManager) {
        this.listener = listener;
        this.fcManager = fcManager;
    }

    public X8AiPoint2PointExcuteConfirmUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_point_to_point__excute_confirm_layout, (ViewGroup) parent, true);
        initView(this.contentView);
        initAction();
    }

    public void initView(View rootView) {
        this.imgBack = (ImageView) rootView.findViewById(R.id.img_ai_follow_return);
        this.tvDistance = (TextView) rootView.findViewById(R.id.tv_ai_follow_distance);
        this.tvTime = (TextView) rootView.findViewById(R.id.tv_ai_follow_time);
        this.tvSpeed = (TextView) rootView.findViewById(R.id.tv_ai_follow_speed);
        this.vMinus = rootView.findViewById(R.id.rl_minus);
        this.sbSeekBar = (SeekBar) rootView.findViewById(R.id.sb_value);
        this.vPlus = rootView.findViewById(R.id.rl_plus);
        this.btnGo = (Button) rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.sbSeekBar.setMax(this.MAX_PROGRESS);
    }

    public void initAction() {
        this.imgBack.setOnClickListener(this);
        this.tvDistance.setOnClickListener(this);
        this.tvTime.setOnClickListener(this);
        this.tvSpeed.setOnClickListener(this);
        this.vMinus.setOnClickListener(this);
        this.sbSeekBar.setOnSeekBarChangeListener(this);
        this.vPlus.setOnClickListener(this);
        this.btnGo.setOnClickListener(this);
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
        }
    }

    public void setMapPoint(MapPointLatLng mapPoint) {
        this.mapPoint = mapPoint;
        setDistance(Math.round(mapPoint.distance));
        this.alt = StateManager.getInstance().getX8Drone().getHeight();
        this.height = mapPoint.altitude;
        setSpeed(this.DEFAULE_SPEED);
        setViewValue();
    }

    public void setDistance(int distance) {
        this.distance = distance;
        this.tvDistance.setText(X8NumberUtil.getDistanceNumberString((float) distance, 0, true));
    }

    private void setSpeed(float speed) {
        this.sbSeekBar.setProgress((int) (10.0f * speed));
    }

    public void setViewValue() {
        float temp;
        boolean isUp;
        float speed = this.MIN + (((float) this.sbSeekBar.getProgress()) / 10.0f);
        String s = X8NumberUtil.getSpeedNumberString(speed, 1, true);
        if (this.height - this.alt >= 0.0f) {
            temp = this.height - this.alt;
            isUp = true;
        } else {
            temp = this.alt - this.height;
            isUp = false;
        }
        double radians = Math.toRadians((double) ((float) Math.toDegrees(Math.atan((double) (temp / ((float) this.distance))))));
        float sh = ((float) Math.cos(radians)) * speed;
        float sv = ((float) Math.sin(radians)) * speed;
        if (isUp) {
            if (sv > 4.0f) {
                sv = 4.0f;
            }
        } else if (sv > 3.0f) {
            sv = 3.0f;
        }
        int tv = Math.round(temp / sv);
        int th = Math.round(((float) this.distance) / sh);
        int time = th;
        if (tv > th) {
            time = tv;
        }
        this.tvSpeed.setText(s);
        this.tvTime.setText("" + time + "S");
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float temp;
        boolean isUp;
        float speed = this.MIN + (((float) progress) / 10.0f);
        String s = X8NumberUtil.getSpeedNumberString(speed, 1, true);
        if (this.height - this.alt >= 0.0f) {
            temp = this.height - this.alt;
            isUp = true;
        } else {
            temp = this.alt - this.height;
            isUp = false;
        }
        double radians = Math.toRadians((double) ((float) Math.toDegrees(Math.atan((double) (temp / ((float) this.distance))))));
        float sh = ((float) Math.cos(radians)) * speed;
        float sv = ((float) Math.sin(radians)) * speed;
        if (isUp) {
            if (sv > 4.0f) {
                sv = 4.0f;
            }
        } else if (sv > 3.0f) {
            sv = 3.0f;
        }
        int tv = Math.round(temp / sv);
        int th = Math.round(((float) this.distance) / sh);
        int time = th;
        if (tv > th) {
            time = tv;
        }
        this.tvSpeed.setText(s);
        this.tvTime.setText("" + time + "S");
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onGoClick() {
        this.fcManager.setAiFollowPoint2Point(this.mapPoint.longitude, this.mapPoint.latitude, ((int) this.mapPoint.altitude) * 10, (int) ((this.MIN + (((float) this.sbSeekBar.getProgress()) / 10.0f)) * 10.0f), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiPoint2PointExcuteConfirmUi.this.startExcute();
                }
            }
        });
    }

    public void startExcute() {
        this.fcManager.setAiFollowPoint2PointExcute(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiPoint2PointExcuteConfirmUi.this.listener.onExcuteClick();
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
