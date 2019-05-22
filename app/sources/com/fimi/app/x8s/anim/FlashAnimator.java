package com.fimi.app.x8s.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public class FlashAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{1.0f, 0.0f, 1.0f, 0.0f, 1.0f})});
    }
}
