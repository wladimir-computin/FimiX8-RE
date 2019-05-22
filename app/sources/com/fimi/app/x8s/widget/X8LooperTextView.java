package com.fimi.app.x8s.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.fimi.app.x8s.anim.FlashAnimator;
import com.fimi.app.x8s.anim.YoYo;
import java.util.List;

public class X8LooperTextView extends FrameLayout {
    private final int ANIM_DELAYED_MILLIONS = PathInterpolatorCompat.MAX_NUM_POINTS;
    private final int ANIM_DURATION = 1000;
    private final String DEFAULT_TEXT_COLOR = "#FFFFFF";
    private final int DEFAULT_TEXT_SIZE = 14;
    private final String TIP_PREFIX = "";
    private Animation anim_in;
    private Animation anim_out;
    private int curTipIndex = 0;
    private long lastTimeMillis;
    private Drawable leftDrawable;
    private boolean needFlash = false;
    private List<String> tipList;
    private TextView tv_tip_in;
    private TextView tv_tip_out;

    public X8LooperTextView(Context context) {
        super(context);
        initTipFrame();
        initAnimation();
    }

    public X8LooperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTipFrame();
        initAnimation();
    }

    public X8LooperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTipFrame();
        initAnimation();
    }

    public void setDrawableLeft(int resId) {
        this.leftDrawable = loadDrawable(resId);
    }

    private void initTipFrame() {
        this.tv_tip_out = newTextView();
        this.tv_tip_in = newTextView();
        addView(this.tv_tip_in);
        addView(this.tv_tip_out);
    }

    private TextView newTextView() {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(-1, -1, 16));
        textView.setCompoundDrawablePadding(10);
        textView.setPadding(10, 0, 0, 0);
        textView.setGravity(16);
        textView.setLines(2);
        textView.setEllipsize(TruncateAt.END);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(1, 14.0f);
        return textView;
    }

    private Drawable loadDrawable(int ResId) {
        Drawable drawable = getResources().getDrawable(ResId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth() - 10, drawable.getMinimumHeight() - 10);
        return drawable;
    }

    private void initAnimation() {
        this.anim_out = newAnimation(0.0f, -1.0f);
        this.anim_in = newAnimation(1.0f, 0.0f);
        this.anim_in.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                X8LooperTextView.this.updateTipAndPlayAnimationWithCheck();
            }
        });
    }

    private Animation newAnimation(float fromYValue, float toYValue) {
        Animation anim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, fromYValue, 1, toYValue);
        anim.setDuration(1000);
        anim.setStartOffset(3000);
        anim.setInterpolator(new DecelerateInterpolator());
        return anim;
    }

    private void updateTipAndPlayAnimationWithCheck() {
        if (System.currentTimeMillis() - this.lastTimeMillis >= 1000) {
            this.lastTimeMillis = System.currentTimeMillis();
            updateTipAndPlayAnimation();
        }
    }

    private void updateTipAndPlayAnimation() {
        if (this.curTipIndex % 2 == 0) {
            updateTip(this.tv_tip_out);
            this.tv_tip_in.startAnimation(this.anim_out);
            this.tv_tip_out.startAnimation(this.anim_in);
            bringChildToFront(this.tv_tip_in);
        } else {
            updateTip(this.tv_tip_in);
            this.tv_tip_out.startAnimation(this.anim_out);
            this.tv_tip_in.startAnimation(this.anim_in);
            bringChildToFront(this.tv_tip_out);
        }
        if (this.needFlash) {
            setFlashAnimator(this);
        }
    }

    public void needFlash(boolean needFlash) {
        this.needFlash = needFlash;
    }

    private void updateTip(TextView tipView) {
        if (this.leftDrawable != null) {
            tipView.setCompoundDrawables(this.leftDrawable, null, null, null);
        }
        String tip = getNextTip();
        if (!TextUtils.isEmpty(tip)) {
            tipView.setText(tip + "");
        }
    }

    private String getNextTip() {
        if (isListEmpty(this.tipList)) {
            return null;
        }
        List list = this.tipList;
        int i = this.curTipIndex;
        this.curTipIndex = i + 1;
        return (String) list.get(i % this.tipList.size());
    }

    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public void setTipList(List<String> tipList) {
        this.tipList = tipList;
        this.curTipIndex = 0;
        updateTip(this.tv_tip_out);
        updateTipAndPlayAnimation();
    }

    private void setFlashAnimator(View view) {
        YoYo.with(new FlashAnimator()).duration(1200).repeat(1).pivot(Float.MAX_VALUE, Float.MAX_VALUE).interpolate(new AccelerateDecelerateInterpolator()).withListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        }).playOn(view);
    }
}
