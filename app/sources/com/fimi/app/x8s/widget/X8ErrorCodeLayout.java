package com.fimi.app.x8s.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import ch.qos.logback.core.net.SyslogConstants;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.anim.FlashAnimator;
import com.fimi.app.x8s.anim.YoYo;
import com.fimi.app.x8s.entity.X8ErrorCode;
import com.fimi.app.x8s.enums.X8ErrorCodeEnum;

public class X8ErrorCodeLayout extends ViewGroup {
    private X8ErrorCode level0 = null;
    private X8ErrorCode level1 = null;

    public X8ErrorCodeLayout(Context context) {
        super(context);
    }

    public X8ErrorCodeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public X8ErrorCodeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint({"NewApi"})
    public X8ErrorCodeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView = getChildAt(0);
        childView.layout(0, 10, childView.getMeasuredWidth(), childView.getMeasuredHeight() + 10);
        View childView2 = getChildAt(1);
        int measureHeight = childView2.getMeasuredHeight();
        int measuredWidth = childView2.getMeasuredWidth();
        if (this.level1 == null) {
            childView2.layout(0, 10, measuredWidth, measureHeight + 10);
        } else {
            childView2.layout(0, measureHeight + 30, measuredWidth, (measureHeight + 30) + measureHeight);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w_size = MeasureSpec.getSize(widthMeasureSpec);
        int h_size = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(w_size, h_size);
    }

    public synchronized void addErrorCode(X8ErrorCode code) {
        View view;
        boolean isAnimation = false;
        if (code.getLevel() == X8ErrorCodeEnum.medium) {
            if (((ViewGroup) getChildAt(1)).getChildCount() == 0) {
                view = getErrorCodeView();
                ((ViewGroup) getChildAt(1)).addView(view);
                isAnimation = true;
            } else {
                view = getChildAt(1);
            }
        } else if (((ViewGroup) getChildAt(0)).getChildCount() == 0) {
            view = getErrorCodeView();
            ((ViewGroup) getChildAt(0)).addView(view);
            isAnimation = true;
        } else {
            view = getChildAt(0);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        ImageView imgBg = (ImageView) view.findViewById(R.id.img_bg);
        ImageView imgArrow = (ImageView) view.findViewById(R.id.iv_arrow1);
        textView.setText(code.getTitle());
        if (code.getLevel() == X8ErrorCodeEnum.medium) {
            this.level0 = code;
            textView.setTextColor(getResources().getColor(R.color.white_100));
        } else {
            this.level1 = code;
            textView.setTextColor(getResources().getColor(R.color.x8_error_code_type1));
            imgBg.setBackgroundResource(R.drawable.x8_error_code_type4);
            imgArrow.setBackgroundResource(R.drawable.x8_error_code_type1_icon);
        }
        if (isAnimation) {
            view.startAnimation(getInAnimation(getContext()));
            if (code.getLevel() != X8ErrorCodeEnum.medium) {
                setFlashAnimator(imgBg);
            }
        }
        if (code.isShow()) {
            view.setVisibility(0);
        } else {
            view.setVisibility(8);
        }
    }

    private View getErrorCodeView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.x8_error_code_item_view, this, false);
        LayoutParams lp = v.getLayoutParams();
        lp.width = getWidth() / 2;
        lp.height = SyslogConstants.LOG_CLOCK;
        v.setLayoutParams(lp);
        return v;
    }

    public static Animation getInAnimation(Context context) {
        return (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.gift_in);
    }

    public void cleanAll() {
        cleanLevel0();
        cleanLevel1();
    }

    public void cleanLevel0() {
        ((ViewGroup) getChildAt(1)).removeAllViews();
        this.level0 = null;
    }

    public void cleanLevel1() {
        ((ViewGroup) getChildAt(0)).removeAllViews();
        this.level1 = null;
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
