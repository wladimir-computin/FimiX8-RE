package com.fimi.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.fimi.kernel.utils.AbViewUtil;
import com.fimi.sdk.R;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class SwitchButton extends View {
    private int borderColor = Color.parseColor("#ffffff");
    private int borderWidth = 1;
    private float centerY;
    private boolean defaultAnimate = true;
    private float endX;
    private OnSwitchListener listener;
    private int offSpotColor = Color.parseColor("#bcbcbd");
    private int onColor = 0;
    private int onSpotColor = Color.parseColor("#ff5400");
    private Paint paint;
    private float radius;
    private RectF rect = new RectF();
    private int spotColor = Color.parseColor("#bcbcbd");
    private float spotMaxX;
    private float spotMinX;
    private int spotSize;
    private float spotX;
    private Spring spring;
    SimpleSpringListener springListener = new SimpleSpringListener() {
        public void onSpringUpdate(Spring spring) {
            SwitchButton.this.calculateEffect(spring.getCurrentValue());
        }
    };
    private SpringSystem springSystem;
    private float startX;
    private boolean toggleOn = false;

    public interface OnSwitchListener {
        void onSwitch(View view, boolean z);
    }

    private SwitchButton(Context context) {
        super(context);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.spring.removeListener(this.springListener);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.spring.addListener(this.springListener);
    }

    public void setup(AttributeSet attrs) {
        this.paint = new Paint(1);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeCap(Cap.ROUND);
        this.springSystem = SpringSystem.create();
        this.spring = this.springSystem.createSpring();
        this.spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(50.0d, 7.0d));
        setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                SwitchButton.this.onViewSwitch();
            }
        });
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        this.onSpotColor = typedArray.getColor(R.styleable.SwitchButton_onColor, this.onSpotColor);
        this.offSpotColor = typedArray.getColor(R.styleable.SwitchButton_spotColor, this.offSpotColor);
        this.toggleOn = typedArray.getBoolean(R.styleable.SwitchButton_onToggle, this.toggleOn);
        this.spotColor = this.offSpotColor;
        this.borderWidth = typedArray.getDimensionPixelSize(R.styleable.SwitchButton_borderWidth, (int) AbViewUtil.dip2px(getContext(), (float) this.borderWidth));
        this.defaultAnimate = typedArray.getBoolean(R.styleable.SwitchButton_animate, this.defaultAnimate);
        typedArray.recycle();
    }

    public void setSwitchState(boolean state) {
        setSwitchState(state, true);
    }

    public void setSwitchState(boolean state, boolean animate) {
        this.toggleOn = state;
        takeEffect(animate);
    }

    private void onViewSwitch() {
        if (this.listener != null) {
            this.listener.onSwitch(this, this.toggleOn);
        }
    }

    public void onSwitch(boolean isFlag) {
        this.toggleOn = isFlag;
        takeEffect(true);
        setSwitchState(this.toggleOn);
    }

    private void takeEffect(boolean animate) {
        double d = 1.0d;
        if (animate) {
            Spring spring = this.spring;
            if (!this.toggleOn) {
                d = 0.0d;
            }
            spring.setEndValue(d);
            return;
        }
        double d2;
        Spring spring2 = this.spring;
        if (this.toggleOn) {
            d2 = 1.0d;
        } else {
            d2 = 0.0d;
        }
        spring2.setCurrentValue(d2);
        if (!this.toggleOn) {
            d = 0.0d;
        }
        calculateEffect(d);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Resources r = Resources.getSystem();
        if (widthMode == 0 || widthMode == Integer.MIN_VALUE) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) TypedValue.applyDimension(1, 50.0f, r.getDisplayMetrics()), NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE);
        }
        if (heightSize == 0 || heightSize == Integer.MIN_VALUE) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) TypedValue.applyDimension(1, 30.0f, r.getDisplayMetrics()), NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean getToggleOn() {
        return this.toggleOn;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getWidth();
        int height = getHeight();
        this.radius = ((float) Math.min(width, height)) * 0.5f;
        this.centerY = this.radius;
        this.startX = this.radius;
        this.endX = ((float) width) - this.radius;
        this.spotMinX = this.startX + ((float) this.borderWidth);
        this.spotMaxX = this.endX - ((float) this.borderWidth);
        this.spotSize = height - (this.borderWidth * 4);
        this.spotX = this.toggleOn ? this.spotMaxX : this.spotMinX;
    }

    private int clamp(int value, int low, int high) {
        return Math.min(Math.max(value, low), high);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.paint.setStyle(Style.STROKE);
        this.paint.setAntiAlias(true);
        float padding = AbViewUtil.dip2px(getContext(), 0.5f);
        this.rect.set(padding, padding, ((float) getWidth()) - padding, ((float) getHeight()) - padding);
        this.paint.setColor(603979775);
        this.paint.setStrokeWidth(AbViewUtil.dip2px(getContext(), 0.7f));
        canvas.drawRoundRect(this.rect, this.radius, this.radius, this.paint);
        this.paint.setStyle(Style.FILL);
        this.paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
        this.rect.set((this.spotX - 1.0f) - this.radius, this.centerY - this.radius, (this.spotX + 1.1f) + this.radius, this.centerY + this.radius);
        this.paint.setColor(0);
        canvas.drawRoundRect(this.rect, this.radius, this.radius, this.paint);
        float spotR = ((float) this.spotSize) * 0.45f;
        this.rect.set(this.spotX - spotR, this.centerY - spotR, this.spotX + spotR, this.centerY + spotR);
        this.paint.setColor(this.spotColor);
        canvas.drawRoundRect(this.rect, spotR, spotR, this.paint);
    }

    private void calculateEffect(double value) {
        this.spotX = (float) SpringUtil.mapValueFromRangeToRange(value, 0.0d, 1.0d, (double) this.spotMinX, (double) this.spotMaxX);
        float mapOffLineWidth = (float) SpringUtil.mapValueFromRangeToRange(1.0d - value, 0.0d, 1.0d, 10.0d, (double) this.spotSize);
        int spotFB = Color.blue(this.onSpotColor);
        int spotFR = Color.red(this.onSpotColor);
        int spotFG = Color.green(this.onSpotColor);
        int spotTB = Color.blue(this.offSpotColor);
        int spotTR = Color.red(this.offSpotColor);
        int spotSR = (int) SpringUtil.mapValueFromRangeToRange(1.0d - value, 0.0d, 1.0d, (double) spotFR, (double) spotTR);
        int spotSG = (int) SpringUtil.mapValueFromRangeToRange(1.0d - value, 0.0d, 1.0d, (double) spotFG, (double) Color.green(this.offSpotColor));
        int spotSB = clamp((int) SpringUtil.mapValueFromRangeToRange(1.0d - value, 0.0d, 1.0d, (double) spotFB, (double) spotTB), 0, 255);
        this.spotColor = Color.rgb(clamp(spotSR, 0, 255), clamp(spotSG, 0, 255), spotSB);
        postInvalidate();
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.listener = onSwitchListener;
    }

    public boolean isAnimate() {
        return this.defaultAnimate;
    }

    public void setAnimate(boolean animate) {
        this.defaultAnimate = animate;
    }
}
