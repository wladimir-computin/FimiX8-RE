package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiPoint2PointExcuteConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8BaseModule;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8Point2PointExcuteListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.x8sdk.controller.FcManager;

public class X8MainAiFollowConfirmController extends AbsX8MenuBoxControllers implements OnClickListener {
    private Activity activity;
    private View blank;
    private X8BaseModule currentModule;
    private FcManager fcManager;
    private IX8Point2PointExcuteListener listener;
    private X8AiPoint2PointExcuteConfirmModule mX8AiFollowPoint2PointExcuteConfirmModule;
    private View mainLayout;

    public X8MainAiFollowConfirmController(View rootView) {
        super(rootView);
    }

    public void setAcitivity(Activity acitivity) {
        this.activity = acitivity;
    }

    public void initViews(View rootView) {
        this.mainLayout = rootView.findViewById(R.id.x8_main_ai_follow_confirm_main_layout);
        this.blank = rootView.findViewById(R.id.x8_main_ai_follow_confirm_main_layout_content_blank);
        this.contentView = rootView.findViewById(R.id.x8_main_ai_follow_confirm_main_layout_content);
        this.mX8AiFollowPoint2PointExcuteConfirmModule = new X8AiPoint2PointExcuteConfirmModule();
        this.currentModule = this.mX8AiFollowPoint2PointExcuteConfirmModule;
    }

    public void initActions() {
        this.blank.setOnClickListener(this);
    }

    public void defaultVal() {
    }

    public void onClick(View v) {
        if (v.getId() == R.id.x8_main_ai_follow_confirm_main_layout_content_blank) {
            closeAiUi();
        }
    }

    public void openUi() {
        this.mainLayout.setVisibility(0);
        this.blank.setVisibility(0);
        this.currentModule.init(this.activity, this.contentView);
        if (!this.isShow) {
            Log.i("zdy", "showAiUi...........");
            this.isShow = true;
            if (this.width == 0) {
                this.contentView.setAlpha(0.0f);
                this.contentView.post(new Runnable() {
                    public void run() {
                        X8MainAiFollowConfirmController.this.contentView.setAlpha(1.0f);
                        X8MainAiFollowConfirmController.this.MAX_WIDTH = X8MainAiFollowConfirmController.this.mainLayout.getWidth();
                        X8MainAiFollowConfirmController.this.width = X8MainAiFollowConfirmController.this.contentView.getWidth();
                        int h = X8MainAiFollowConfirmController.this.contentView.getHeight();
                        ObjectAnimator animatorY = ObjectAnimator.ofFloat(X8MainAiFollowConfirmController.this.contentView, "translationX", new float[]{(float) X8MainAiFollowConfirmController.this.width, 0.0f});
                        animatorY.setDuration(300);
                        animatorY.start();
                    }
                });
                return;
            }
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.contentView, "translationX", new float[]{(float) this.width, 0.0f});
            animatorY.setDuration(300);
            animatorY.start();
        }
    }

    public void closeAiUi() {
        this.blank.setVisibility(8);
        if (this.isShow) {
            Log.i("zdy", "closeAiUi...........");
            this.isShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.contentView, "translationX", new float[]{0.0f, (float) this.width});
            translationRight.setDuration(300);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8MainAiFollowConfirmController.this.mainLayout.setVisibility(4);
                    ((ViewGroup) X8MainAiFollowConfirmController.this.contentView).removeAllViews();
                }
            });
        }
    }

    public void setMapPoint(MapPointLatLng mapPoint) {
        this.mX8AiFollowPoint2PointExcuteConfirmModule.setMapPoint(mapPoint);
    }

    public void setPoint2PointExcuteListener(IX8Point2PointExcuteListener listener, FcManager fcManager) {
        this.listener = listener;
        this.fcManager = fcManager;
    }
}
