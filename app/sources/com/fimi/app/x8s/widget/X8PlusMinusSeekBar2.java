package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener;

public class X8PlusMinusSeekBar2 extends RelativeLayout implements OnClickListener, SlideChangeListener {
    private int curValue = 10;
    private onSeekValueSetListener listener;
    private X8SeekBarView mSeekBar;
    private View rlMinus;
    private View rlPlus;
    private int seekBarMax = 100;
    private int seekBarMin = 10;

    public interface onSeekValueSetListener {
        void onSeekValueSet(int i, int i2);

        void onStart(int i, int i2);

        void onStop(int i, int i2);
    }

    public void setListener(onSeekValueSetListener listener) {
        this.listener = listener;
    }

    public X8PlusMinusSeekBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.x8_plus_minus_seekbar_layout2, this, true);
        this.mSeekBar = (X8SeekBarView) findViewById(R.id.sb_value);
        this.rlMinus = findViewById(R.id.rl_minus);
        this.rlPlus = findViewById(R.id.rl_plus);
        this.rlMinus.setOnClickListener(this);
        this.rlPlus.setOnClickListener(this);
        this.mSeekBar.setMaxProgress(this.seekBarMax - this.seekBarMin);
        this.mSeekBar.setOnSlideChangeListener(this);
        setProgress(this.curValue);
    }

    public void initData(int seekBarMin, int seekBarMax) {
        this.seekBarMin = seekBarMin;
        this.seekBarMax = seekBarMax;
        this.mSeekBar.setMaxProgress(seekBarMax - seekBarMin);
        setProgress(this.curValue);
    }

    public void setProgress(int value) {
        if (value > this.seekBarMax) {
            value = this.seekBarMax;
        }
        if (value < this.seekBarMin) {
            value = this.seekBarMin;
        }
        this.curValue = value;
        this.mSeekBar.setProgress(value - this.seekBarMin);
    }

    public void onClick(View v) {
        int i = v.getId();
        int s;
        if (i == R.id.rl_plus) {
            if (this.mSeekBar.getProgress() != this.mSeekBar.getMaxProgress()) {
                s = this.mSeekBar.getProgress() + 1;
                if (s > this.mSeekBar.getMaxProgress()) {
                    s = this.mSeekBar.getMaxProgress();
                }
                this.mSeekBar.setProgress(s);
            }
        } else if (i == R.id.rl_minus && this.mSeekBar.getProgress() != 0) {
            s = this.mSeekBar.getProgress() - 1;
            if (s < 0) {
                s = 0;
            }
            this.mSeekBar.setProgress(s);
        }
    }

    public int getProgress() {
        return this.curValue;
    }

    public void onStart(X8SeekBarView slideView, int progress) {
        this.listener.onStart(slideView.getId(), progress);
    }

    public void onProgress(X8SeekBarView slideView, int progress) {
        this.curValue = this.seekBarMin + progress;
        if (this.listener != null) {
            this.listener.onSeekValueSet(getId(), this.curValue);
        }
    }

    public void onStop(X8SeekBarView slideView, int progress) {
        this.listener.onStop(slideView.getId(), progress);
    }
}
