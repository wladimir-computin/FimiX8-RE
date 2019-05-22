package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiGravitationExcuteConfirmModule;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiGravitationExcuteControllerListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;

public class X8AiGravitationExcuteController extends AbsX8AiController implements OnClickListener, onDialogButtonClickListener {
    private final X8sMainActivity activity;
    private X8DoubleCustomDialog dialog;
    private ImageView imgBack;
    private ImageView imgNext;
    protected boolean isNextShow;
    private X8AiGravitationExcuteConfirmModule mExcuteConfirmModule;
    private View mFlagBottom;
    private IX8AiGravitationExcuteControllerListener mIX8AiGravitationExcuteControllerListener;
    private View mNextBlank;
    private View mNextContent;
    private X8AiTipWithCloseView tvTip;
    private int width;

    public X8AiGravitationExcuteController(X8sMainActivity activity, View rootView) {
        super(rootView);
        this.activity = activity;
    }

    public void setListener(IX8AiGravitationExcuteControllerListener listener) {
        this.mIX8AiGravitationExcuteControllerListener = listener;
    }

    public void initViews(View rootView) {
    }

    public void initActions() {
        if (this.handleView != null) {
            this.imgBack.setOnClickListener(this);
            this.imgNext.setOnClickListener(this);
            this.mNextBlank.setOnClickListener(this);
        }
    }

    public void defaultVal() {
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void openUi() {
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_ai_gravitation_layout, (ViewGroup) this.rootView, true);
        this.imgNext = (ImageView) this.handleView.findViewById(R.id.img_ai_gravitation_follow_next);
        this.imgBack = (ImageView) this.handleView.findViewById(R.id.img_ai_gravitation_follow_back);
        this.tvTip = (X8AiTipWithCloseView) this.handleView.findViewById(R.id.v_gravitation_content_tip);
        this.mNextBlank = this.handleView.findViewById(R.id.x8_main_ai_gravitation_next_blank);
        this.mNextContent = this.handleView.findViewById(R.id.x8_main_ai_gravitation_next_content);
        this.mFlagBottom = this.handleView.findViewById(R.id.rl_flag_gravitation_bottom);
        this.tvTip.setTipText(this.rootView.getContext().getString(R.string.x8_ai_fly_gravitation_tip4));
        this.mExcuteConfirmModule = new X8AiGravitationExcuteConfirmModule();
        initActions();
        super.openUi();
    }

    public void closeUi() {
        super.closeUi();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.img_ai_gravitation_follow_next) {
            openNextUi();
        } else if (id == R.id.img_ai_gravitation_follow_back) {
            showExitDialog();
        } else if (id == R.id.x8_main_ai_gravitation_next_blank) {
            closeNextUi();
        }
    }

    private void closeNextUi() {
        if (this.isNextShow) {
            this.isNextShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.mNextContent, "translationX", new float[]{0.0f, (float) this.width});
            translationRight.setDuration(300);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8AiGravitationExcuteController.this.mNextContent.setVisibility(8);
                    X8AiGravitationExcuteController.this.mNextBlank.setVisibility(8);
                    ((ViewGroup) X8AiGravitationExcuteController.this.mNextContent).removeAllViews();
                    X8AiGravitationExcuteController.this.imgBack.setVisibility(0);
                    X8AiGravitationExcuteController.this.tvTip.setVisibility(0);
                    X8AiGravitationExcuteController.this.mFlagBottom.setVisibility(0);
                    X8AiGravitationExcuteController.this.imgNext.setVisibility(0);
                }
            });
        }
    }

    private void openNextUi() {
        this.mNextBlank.setVisibility(0);
        this.mNextContent.setVisibility(0);
        this.tvTip.setVisibility(8);
        this.mFlagBottom.setVisibility(8);
        this.imgNext.setVisibility(8);
        this.mExcuteConfirmModule.init(this.activity, this.mNextContent);
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.mNextContent, "translationX", new float[]{(float) this.width, 0.0f});
            animatorY.setDuration(300);
            animatorY.start();
        }
    }

    public void showExitDialog() {
        if (this.dialog == null) {
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_ai_fly_gravitation_dialog_title), this.rootView.getContext().getString(R.string.x8_ai_fly_gravitation_dialog_tip), this);
        }
        this.dialog.show();
    }

    public void onLeft() {
    }

    public void onRight() {
        closeUi();
        closeAiGravitation();
    }

    private void closeAiGravitation() {
        if (this.mIX8AiGravitationExcuteControllerListener != null) {
            this.mIX8AiGravitationExcuteControllerListener.onAiGravitationBackClick();
        }
    }
}
