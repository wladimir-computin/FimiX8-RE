package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.IX8GimbalHorizontalTrimListener;
import com.fimi.app.x8s.widget.X8DoubleWaySeekBar.OnSeekProgressListener;
import com.fimi.kernel.percent.PercentRelativeLayout;

public class X8HorizontalTrimView extends RelativeLayout implements OnClickListener {
    private ImageButton btnAdd;
    private ImageButton btnReduce;
    private int currValue = 0;
    private boolean isReady = false;
    private IX8GimbalHorizontalTrimListener listener;
    private PercentRelativeLayout root_layout;
    private X8DoubleWaySeekBar seekBar;
    private TextView tvBubbleTop;

    public X8HorizontalTrimView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.x8_gimbal_horizontal_trim_layout, null);
        this.root_layout = (PercentRelativeLayout) view.findViewById(R.id.root_layout);
        addView(view, new LayoutParams(-1, -1));
        this.seekBar = (X8DoubleWaySeekBar) view.findViewById(R.id.seek_bar);
        this.tvBubbleTop = (TextView) view.findViewById(R.id.tv_bubble_top);
        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        this.btnReduce = (ImageButton) view.findViewById(R.id.btn_gimbal_reduce_left);
        this.btnAdd = (ImageButton) view.findViewById(R.id.btn_gimbal_add_right);
        this.btnReduce.setOnClickListener(this);
        this.btnAdd.setOnClickListener(this);
        this.seekBar.setOnSeekProgressListener(new OnSeekProgressListener() {
            @SuppressLint({"StringFormatMatches"})
            public void onSeekProgress(int progress) {
                X8HorizontalTrimView.this.currValue = progress;
                if (progress <= 0) {
                    X8HorizontalTrimView.this.tvBubbleTop.setText(String.valueOf(String.format(context.getString(R.string.x8_cloud_minus_angle), new Object[]{Float.valueOf(((float) progress) / 10.0f)})));
                    return;
                }
                X8HorizontalTrimView.this.tvBubbleTop.setText(String.valueOf(String.format(context.getString(R.string.x8_cloud_angle), new Object[]{Float.valueOf(((float) progress) / 10.0f)})));
            }

            public void onPointerPositionChanged(int x, int y) {
                X8HorizontalTrimView.this.tvBubbleTop.setX((float) (x - (X8HorizontalTrimView.this.tvBubbleTop.getWidth() / 2)));
            }

            public void onSizeChanged() {
                X8HorizontalTrimView.this.seekBar.setProgress((float) X8HorizontalTrimView.this.currValue);
                X8HorizontalTrimView.this.isReady = true;
            }
        });
    }

    public void setEnabled(boolean enabled) {
        this.btnReduce.setEnabled(enabled);
        this.btnAdd.setEnabled(enabled);
        this.seekBar.setEnabled(enabled);
        updateViewEnable(enabled, this.root_layout);
    }

    public void updateViewEnable(boolean enable, ViewGroup... parent) {
        if (parent != null && parent.length > 0) {
            for (ViewGroup group : parent) {
                int len = group.getChildCount();
                for (int j = 0; j < len; j++) {
                    View subView = group.getChildAt(j);
                    if (subView instanceof ViewGroup) {
                        updateViewEnable(enable, (ViewGroup) subView);
                    } else {
                        subView.setEnabled(enable);
                        if (enable) {
                            subView.setAlpha(1.0f);
                        } else {
                            subView.setAlpha(0.6f);
                        }
                    }
                }
            }
        }
    }

    public void setCurrValue(float currValue) {
        if (this.isReady) {
            this.seekBar.setProgress(currValue);
        }
    }

    public float getCurrValue() {
        return ((float) this.currValue) / 10.0f;
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_sure) {
            if (this.listener != null) {
                this.listener.onSettingReady((float) this.currValue);
            }
        } else if (i == R.id.btn_gimbal_reduce_left) {
            setCurrValue(((float) this.currValue) - 1.1f);
        } else if (i == R.id.btn_gimbal_add_right) {
            setCurrValue(((float) this.currValue) + 1.1f);
        }
    }

    public void setListener(IX8GimbalHorizontalTrimListener listener) {
        this.listener = listener;
    }
}
