package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.fimi.app.x8s.R;

public class X8AircrftCalibrationIndicatorView extends LinearLayout {
    private LinearLayout container;
    private Context context;
    private int selected = 0;
    private boolean[] stepStatus = new boolean[]{false, false, false, false, false, false};

    public X8AircrftCalibrationIndicatorView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public X8AircrftCalibrationIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.x8_aircraft_calibration_indcator_layout, this);
        setOrientation(0);
        setGravity(16);
        this.container = (LinearLayout) findViewById(R.id.ll_aircrft_calibration_indicator);
        draw();
    }

    public void setSelected(int selected) {
        this.selected = selected;
        stopFlick();
        if (selected >= 0 && selected <= 5 && this.container != null) {
            startFlick(this.container.getChildAt(selected));
        }
        if (selected == 6 && this.container != null) {
            startFlick(this.container);
        }
    }

    public void setStepStatus(boolean[] stepStatus, int status) {
        this.stepStatus = stepStatus;
        if (status >= 0 && status <= 5) {
            this.stepStatus[status] = true;
        }
        stopFlick();
        this.container.removeAllViews();
        draw();
    }

    public void draw() {
        for (boolean z : this.stepStatus) {
            ImageView imageview = new ImageView(this.context);
            imageview.setLayoutParams(new LayoutParams(100, 100));
            imageview.setPadding(20, 0, 20, 0);
            if (z) {
                imageview.setImageDrawable(getResources().getDrawable(R.drawable.x8_aircraft_calibration_indcator_focus));
            } else {
                imageview.setImageDrawable(getResources().getDrawable(R.drawable.x8_aircraft_calibration_indcator_normal));
            }
            this.container.addView(imageview);
        }
    }

    private void startFlick(View view) {
        if (view != null) {
            Animation alphaAnimation = new AlphaAnimation(1.0f, 0.2f);
            alphaAnimation.setDuration(500);
            alphaAnimation.setInterpolator(new LinearInterpolator());
            alphaAnimation.setRepeatCount(-1);
            alphaAnimation.setRepeatMode(2);
            view.startAnimation(alphaAnimation);
        }
    }

    public void stopFlick() {
        if (this.container != null) {
            this.container.clearAnimation();
            for (int i = 0; i < this.container.getChildCount(); i++) {
                this.container.getChildAt(i).clearAnimation();
            }
        }
    }
}
