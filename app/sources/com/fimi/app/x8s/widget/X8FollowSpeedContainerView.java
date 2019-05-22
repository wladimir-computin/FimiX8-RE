package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8FollowSpeedView.OnChangeListener;

public class X8FollowSpeedContainerView extends RelativeLayout implements OnClickListener, OnChangeListener {
    private int MAX = 40;
    private int MIN = 0;
    private int accuracy = 10;
    private X8FollowSpeedView fsv;
    private ImageView imgAntiClockwise;
    private ImageView imgClockwise;
    private OnSendSpeedListener listener;
    private String prex;
    private int speed;
    private TextView tvSpeed;

    public interface OnSendSpeedListener {
        void onSendSpeed(int i);
    }

    public X8FollowSpeedContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.x8_ai_follow_speed_containt_layout, this, true);
        this.fsv = (X8FollowSpeedView) findViewById(R.id.v_speed);
        this.tvSpeed = (TextView) findViewById(R.id.tv_speed);
        this.imgAntiClockwise = (ImageView) findViewById(R.id.img_anti_clockwise);
        this.imgClockwise = (ImageView) findViewById(R.id.img_clockwise);
        this.fsv.setOnSpeedChangeListener(this);
        this.imgAntiClockwise.setOnClickListener(this);
        this.imgClockwise.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        int s;
        if (id == R.id.img_anti_clockwise) {
            s = this.speed;
            if (s >= 0) {
                s -= this.MIN;
            } else {
                s += this.MIN;
            }
            this.fsv.setLeftClick(s, this.MAX, this.MIN);
        } else if (id == R.id.img_clockwise) {
            s = this.speed;
            if (s >= 0) {
                s -= this.MIN;
            } else {
                s += this.MIN;
            }
            this.fsv.setRightClick(s, this.MAX, this.MIN);
        }
    }

    public void onChange(float percent, boolean isRight) {
        this.speed = (int) ((((float) (this.MAX - this.MIN)) * percent) + ((float) this.MIN));
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString((((float) this.speed) * 1.0f) / 10.0f, 1, true));
        if (!isRight) {
            this.speed = -this.speed;
        }
    }

    public void onSendData() {
        if (this.listener != null) {
            this.listener.onSendSpeed(this.speed * this.accuracy);
        }
    }

    public void setOnSendSpeedListener(OnSendSpeedListener listener) {
        this.listener = listener;
    }

    public void setSpeed(int s) {
        this.fsv.setSpeed(s / 10, this.MAX - this.MIN);
    }

    public void setMaxMin(int max, int min, int accuracy) {
        this.MAX = max;
        this.MIN = min;
        this.accuracy = accuracy;
    }

    public void setSpeed2(int s) {
        if (s >= 0) {
            s -= this.MIN;
        } else {
            s += this.MIN;
        }
        this.fsv.setSpeed(s, this.MAX - this.MIN);
    }

    public void switchUnity() {
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString((((float) this.speed) * 1.0f) / 10.0f, 1, true));
    }
}
