package com.fimi.app.x8s.anim;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.List;

public class YoYo {
    public static final float CENTER_PIVOT = Float.MAX_VALUE;
    private static final long DURATION = 1000;
    public static final int INFINITE = -1;
    private static final long NO_DELAY = 0;
    private BaseViewAnimator animator;
    private List<AnimatorListener> callbacks;
    private long delay;
    private long duration;
    private Interpolator interpolator;
    private float pivotX;
    private float pivotY;
    private boolean repeat;
    private int repeatMode;
    private int repeatTimes;
    private View target;

    private static class EmptyAnimatorListener implements AnimatorListener {
        private EmptyAnimatorListener() {
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }

    public static final class AnimationComposer {
        private BaseViewAnimator animator;
        private List<AnimatorListener> callbacks = new ArrayList();
        private long delay = 0;
        private long duration = 1000;
        private Interpolator interpolator;
        private float pivotX = Float.MAX_VALUE;
        private float pivotY = Float.MAX_VALUE;
        private boolean repeat = false;
        private int repeatMode = 1;
        private int repeatTimes = 0;
        private View target;

        public AnimationComposer(BaseViewAnimator animator) {
            this.animator = animator;
        }

        public AnimationComposer duration(long duration) {
            this.duration = duration;
            return this;
        }

        public AnimationComposer delay(long delay) {
            this.delay = delay;
            return this;
        }

        public AnimationComposer interpolate(Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public AnimationComposer pivot(float pivotX, float pivotY) {
            this.pivotX = pivotX;
            this.pivotY = pivotY;
            return this;
        }

        public AnimationComposer pivotX(float pivotX) {
            this.pivotX = pivotX;
            return this;
        }

        public AnimationComposer pivotY(float pivotY) {
            this.pivotY = pivotY;
            return this;
        }

        public AnimationComposer repeat(int times) {
            if (times < -1) {
                throw new RuntimeException("Can not be less than -1, -1 is infinite loop");
            }
            this.repeat = times != 0;
            this.repeatTimes = times;
            return this;
        }

        public AnimationComposer repeatMode(int mode) {
            this.repeatMode = mode;
            return this;
        }

        public AnimationComposer withListener(AnimatorListener listener) {
            this.callbacks.add(listener);
            return this;
        }

        public AnimationComposer onStart(final AnimatorCallback callback) {
            this.callbacks.add(new EmptyAnimatorListener() {
                public void onAnimationStart(Animator animation) {
                    callback.call(animation);
                }
            });
            return this;
        }

        public AnimationComposer onEnd(final AnimatorCallback callback) {
            this.callbacks.add(new EmptyAnimatorListener() {
                public void onAnimationEnd(Animator animation) {
                    callback.call(animation);
                }
            });
            return this;
        }

        public AnimationComposer onCancel(final AnimatorCallback callback) {
            this.callbacks.add(new EmptyAnimatorListener() {
                public void onAnimationCancel(Animator animation) {
                    callback.call(animation);
                }
            });
            return this;
        }

        public AnimationComposer onRepeat(final AnimatorCallback callback) {
            this.callbacks.add(new EmptyAnimatorListener() {
                public void onAnimationRepeat(Animator animation) {
                    callback.call(animation);
                }
            });
            return this;
        }

        public YoYoString playOn(View target) {
            this.target = target;
            return new YoYoString(new YoYo(this).play(), this.target);
        }
    }

    public interface AnimatorCallback {
        void call(Animator animator);
    }

    public static final class YoYoString {
        private BaseViewAnimator animator;
        private View target;

        private YoYoString(BaseViewAnimator animator, View target) {
            this.target = target;
            this.animator = animator;
        }

        public boolean isStarted() {
            return this.animator.isStarted();
        }

        public boolean isRunning() {
            return this.animator.isRunning();
        }

        public void stop() {
            stop(true);
        }

        public void stop(boolean reset) {
            this.animator.cancel();
            if (reset) {
                this.animator.reset(this.target);
            }
        }
    }

    private YoYo(AnimationComposer animationComposer) {
        this.animator = animationComposer.animator;
        this.duration = animationComposer.duration;
        this.delay = animationComposer.delay;
        this.repeat = animationComposer.repeat;
        this.repeatTimes = animationComposer.repeatTimes;
        this.repeatMode = animationComposer.repeatMode;
        this.interpolator = animationComposer.interpolator;
        this.pivotX = animationComposer.pivotX;
        this.pivotY = animationComposer.pivotY;
        this.callbacks = animationComposer.callbacks;
        this.target = animationComposer.target;
    }

    public static AnimationComposer with(BaseViewAnimator animator) {
        return new AnimationComposer(animator);
    }

    private BaseViewAnimator play() {
        this.animator.setTarget(this.target);
        if (this.pivotX == Float.MAX_VALUE) {
            ViewCompat.setPivotX(this.target, ((float) this.target.getMeasuredWidth()) / 2.0f);
        } else {
            this.target.setPivotX(this.pivotX);
        }
        if (this.pivotY == Float.MAX_VALUE) {
            ViewCompat.setPivotY(this.target, ((float) this.target.getMeasuredHeight()) / 2.0f);
        } else {
            this.target.setPivotY(this.pivotY);
        }
        this.animator.setDuration(this.duration).setRepeatTimes(this.repeatTimes).setRepeatMode(this.repeatMode).setInterpolator(this.interpolator).setStartDelay(this.delay);
        if (this.callbacks.size() > 0) {
            for (AnimatorListener callback : this.callbacks) {
                this.animator.addAnimatorListener(callback);
            }
        }
        this.animator.animate();
        return this.animator;
    }
}
