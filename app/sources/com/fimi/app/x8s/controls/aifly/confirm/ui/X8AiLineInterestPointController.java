package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class X8AiLineInterestPointController {
    private RelativeLayout group;
    private OnInterestTouchUp listener;
    private TextView textView;
    private ImageView tmpView;
    private ImageView view;

    public interface OnInterestTouchUp {
        void onUp(int i, int i2);
    }

    public void setInterestEnable(boolean interestEnable) {
        this.view.setEnabled(interestEnable);
    }

    public void setListener(OnInterestTouchUp listener) {
        this.listener = listener;
    }

    public X8AiLineInterestPointController(RelativeLayout group, ImageView view, TextView textView) {
        this.group = group;
        this.view = view;
        this.textView = textView;
        addTouchEvent();
    }

    public void addTouchEvent() {
        this.view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    X8AiLineInterestPointController.this.tmpView = new ImageView(X8AiLineInterestPointController.this.group.getContext());
                    X8AiLineInterestPointController.this.tmpView.setBackgroundResource(R.drawable.x8_img_ai_line_inreterst_max2);
                    LayoutParams layoutParams = new LayoutParams(-2, -2);
                    layoutParams.setMargins(X8AiLineInterestPointController.this.view.getLeft(), X8AiLineInterestPointController.this.view.getTop(), 0, 0);
                    X8AiLineInterestPointController.this.group.addView(X8AiLineInterestPointController.this.tmpView, layoutParams);
                } else if (event.getAction() == 2) {
                    LayoutParams lp = (LayoutParams) X8AiLineInterestPointController.this.tmpView.getLayoutParams();
                    lp.setMargins((X8AiLineInterestPointController.this.view.getLeft() + ((int) event.getX())) - (X8AiLineInterestPointController.this.tmpView.getWidth() / 2), (X8AiLineInterestPointController.this.view.getTop() + ((int) event.getY())) - ((int) (1.5f * ((float) X8AiLineInterestPointController.this.tmpView.getHeight()))), 0, 0);
                    X8AiLineInterestPointController.this.tmpView.setLayoutParams(lp);
                } else if (event.getAction() == 1) {
                    X8AiLineInterestPointController.this.listener.onUp(X8AiLineInterestPointController.this.view.getLeft() + ((int) event.getX()), X8AiLineInterestPointController.this.view.getTop() + ((int) event.getY()));
                    X8AiLineInterestPointController.this.group.removeView(X8AiLineInterestPointController.this.tmpView);
                }
                return true;
            }
        });
    }

    public void showView(boolean b) {
        if (this.view != null && this.textView != null) {
            if (b) {
                this.view.setVisibility(0);
                this.textView.setVisibility(0);
                return;
            }
            this.view.setVisibility(8);
            this.textView.setVisibility(8);
        }
    }
}
