package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.LongClickListener;
import com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener;

public class X8CustomSeekBar extends LinearLayout implements OnClickListener, SlideChangeListener {
    private final String TAG = "DDLog";
    private int curValue = 10;
    private onSeekValueSetListener listener;
    private LongClickListener longClickListener = new LongClickListener() {
        public void longClickCallback(int viewId) {
            if (viewId == R.id.rl_add) {
                if (X8CustomSeekBar.this.curValue < X8CustomSeekBar.this.seekBarMax) {
                    X8CustomSeekBar.this.curValue = X8CustomSeekBar.this.curValue + 1;
                    X8CustomSeekBar.this.setProgress(X8CustomSeekBar.this.curValue);
                }
            } else if (viewId == R.id.rl_reduce && X8CustomSeekBar.this.curValue > X8CustomSeekBar.this.seekBarMin) {
                X8CustomSeekBar.this.curValue = X8CustomSeekBar.this.curValue - 1;
                X8CustomSeekBar.this.setProgress(X8CustomSeekBar.this.curValue);
            }
        }

        public void onFingerUp(int viewId) {
            if (X8CustomSeekBar.this.listener != null) {
                X8CustomSeekBar.this.listener.onSeekValueSet(X8CustomSeekBar.this.getId(), X8CustomSeekBar.this.curValue);
            }
        }
    };
    private String name = "name";
    private RelativeLayout rlAdd;
    private RelativeLayout rlReduce;
    private X8SeekBarView seekBar;
    private int seekBarMax = 100;
    private int seekBarMin = 10;
    private TextView tvParam;

    public interface onSeekValueSetListener {
        void onSeekValueSet(int i, int i2);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public X8CustomSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.x8_view_custom_seekbar, null);
        view.setLayoutParams(new LayoutParams(-1, -1));
        addView(view);
        this.rlAdd = (RelativeLayout) view.findViewById(R.id.rl_add);
        this.rlReduce = (RelativeLayout) view.findViewById(R.id.rl_reduce);
        this.rlAdd.setOnClickListener(this);
        this.rlReduce.setOnClickListener(this);
        this.rlAdd.setOnTouchListener(this.longClickListener);
        this.rlReduce.setOnTouchListener(this.longClickListener);
        this.tvParam = (TextView) view.findViewById(R.id.tv_param);
        this.seekBar = (X8SeekBarView) view.findViewById(R.id.sb_value);
        this.seekBar.setMaxProgress(this.seekBarMax - this.seekBarMin);
        this.seekBar.setOnSlideChangeListener(this);
        setProgress(this.curValue);
    }

    public void initData(String name, int seekBarMin, int seekBarMax) {
        this.name = name;
        this.seekBarMin = seekBarMin;
        this.seekBarMax = seekBarMax;
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
        this.seekBar.setProgress(value - this.seekBarMin);
        this.tvParam.setText(this.name + "　" + this.curValue + "%");
    }

    public int getCurValue() {
        return this.curValue;
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rl_add) {
            if (this.curValue < this.seekBarMax) {
                this.curValue++;
                if (this.listener != null) {
                    this.listener.onSeekValueSet(getId(), this.curValue);
                }
            }
        } else if (i == R.id.rl_reduce && this.curValue > this.seekBarMin) {
            this.curValue--;
            if (this.listener != null) {
                this.listener.onSeekValueSet(getId(), this.curValue);
            }
        }
    }

    public void setOnSeekChangedListener(onSeekValueSetListener listener) {
        this.listener = listener;
    }

    public void onStart(X8SeekBarView slideView, int progress) {
    }

    public void onProgress(X8SeekBarView slideView, int progress) {
        this.curValue = this.seekBarMin + progress;
    }

    public void onStop(X8SeekBarView slideView, int progress) {
        if (this.listener != null) {
            this.listener.onSeekValueSet(getId(), this.curValue);
        }
    }
}
