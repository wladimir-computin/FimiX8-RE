package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.widget.X8CustomSeekBar.onSeekValueSetListener;

public class X8PlusMinusSeekBar extends RelativeLayout implements OnClickListener, OnSeekBarChangeListener {
    private int curValue = 10;
    private int defaultValue = 0;
    private View imgReset;
    private onSeekValueSetListener listener;
    private SeekBar mSeekBar;
    private onSeekValueSetListener onSeekChangedListener;
    private View rlMinus;
    private View rlPlus;
    private int seekBarMax = 100;
    private int seekBarMin = 10;

    public void setListener(onSeekValueSetListener listener) {
        this.listener = listener;
    }

    public X8PlusMinusSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.x8_plus_minus_seekbar_layout, this, true);
        this.mSeekBar = (SeekBar) findViewById(R.id.sb_value);
        this.rlMinus = findViewById(R.id.rl_minus);
        this.rlPlus = findViewById(R.id.rl_plus);
        this.imgReset = findViewById(R.id.img_reset);
        this.rlMinus.setOnClickListener(this);
        this.rlPlus.setOnClickListener(this);
        this.imgReset.setOnClickListener(this);
        this.mSeekBar.setMax(this.seekBarMax - this.seekBarMin);
        this.mSeekBar.setOnSeekBarChangeListener(this);
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
        if (i == R.id.rl_plus) {
            if (this.mSeekBar.getProgress() != this.mSeekBar.getMax()) {
                this.mSeekBar.setProgress(this.mSeekBar.getProgress() + 1);
            }
        } else if (i == R.id.rl_minus) {
            if (this.mSeekBar.getProgress() != 0) {
                this.mSeekBar.setProgress(this.mSeekBar.getProgress() - 1);
            }
        } else if (i == R.id.img_reset) {
            this.mSeekBar.setProgress(this.defaultValue);
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.curValue = this.seekBarMin + progress;
        if (this.listener != null) {
            this.listener.onSeekValueSet(getId(), this.curValue);
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public int getProgress() {
        return this.curValue;
    }
}
