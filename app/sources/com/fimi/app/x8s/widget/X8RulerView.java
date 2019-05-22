package com.fimi.app.x8s.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.fimi.app.x8s.R;
import com.xiaomi.account.openauth.XiaomiOAuthConstants;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8RulerView extends View {
    private float curScaleValue = 0.0f;
    private Bitmap endBmp;
    private boolean firstInit = true;
    private float fixScale;
    private int height;
    private boolean isEnable = true;
    float lastMoveX;
    private int mMaximumVelocity;
    private VelocityTracker mVelocityTracker;
    private Bitmap maxRuler;
    private float max_ruler_len;
    private Bitmap minRuler;
    private float min_ruler_len;
    float moveX;
    private Paint paint;
    private Map<Float, Float> pointMap = new HashMap();
    private ArrayList<Float> points = new ArrayList();
    float preTime;
    private Bitmap resultBmp;
    private int rulerHeight = 50;
    RulerListener rulerListener;
    private int rulerTopGap = (this.rulerHeight / 4);
    private int scaleGap = 14;
    private int scaleNum = 60;
    float tempX;
    private ValueAnimator valueAnimator;
    private int width;
    int xVelocity;

    public interface RulerListener {
        void updateRuler(float f);
    }

    public X8RulerView(Context context) {
        super(context);
        init(context);
    }

    public X8RulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public X8RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.resultBmp, (float) ((this.width - this.resultBmp.getWidth()) / 2), 0.0f, this.paint);
        if (this.lastMoveX == 0.0f) {
            this.lastMoveX = this.fixScale;
        }
        canvas.translate(this.lastMoveX, 0.0f);
        for (int i = 0; i <= 6; i++) {
            if (i < 6) {
                canvas.drawBitmap(this.minRuler, (((float) (i * 2)) * this.min_ruler_len) + (((float) i) * this.max_ruler_len), (float) ((this.height - this.rulerHeight) / 2), this.paint);
                canvas.drawBitmap(this.maxRuler, (((float) ((i * 2) + 1)) * this.min_ruler_len) + (((float) i) * this.max_ruler_len), (float) ((this.height - this.rulerHeight) / 2), this.paint);
                canvas.drawBitmap(this.minRuler, (((float) ((i * 2) + 1)) * this.min_ruler_len) + (((float) (i + 1)) * this.max_ruler_len), (float) ((this.height - this.rulerHeight) / 2), this.paint);
            } else if (i == 6) {
                canvas.drawBitmap(this.endBmp, (((float) (i * 2)) * this.min_ruler_len) + (((float) i) * this.max_ruler_len), (float) ((this.height - this.rulerHeight) / 2), this.paint);
            }
        }
        canvas.save();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightModule = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (heightModule) {
            case Integer.MIN_VALUE:
                this.height = ((this.rulerHeight + (this.rulerTopGap * 2)) + getPaddingTop()) + getPaddingBottom();
                break;
            case 0:
            case NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE /*1073741824*/:
                this.height = (getPaddingTop() + heightSize) + getPaddingBottom();
                break;
        }
        this.width = (getPaddingLeft() + widthSize) + getPaddingRight();
        this.fixScale = (float) ((this.width - (this.scaleGap * this.scaleNum)) / 2);
        if (this.pointMap.size() <= 0) {
            for (int i = 0; i <= 6; i++) {
                if (i < 6) {
                    float key = (((float) ((this.scaleGap * this.scaleNum) / 2)) + this.fixScale) - ((((float) (i * 2)) * this.min_ruler_len) + (((float) i) * this.max_ruler_len));
                    this.pointMap.put(Float.valueOf(key), Float.valueOf(keep2point(((((float) (i * 2)) * 0.3f) + (((float) i) * 0.4f)) - 1.5f)));
                    float key3 = (((float) ((this.scaleGap * this.scaleNum) / 2)) + this.fixScale) - ((((float) ((i * 2) + 1)) * this.min_ruler_len) + (((float) i) * this.max_ruler_len));
                    this.pointMap.put(Float.valueOf(key3), Float.valueOf(keep2point(((((float) ((i * 2) + 1)) * 0.3f) - 1.5f) + (((float) i) * 0.4f))));
                    float key4 = (((float) ((this.scaleGap * this.scaleNum) / 2)) + this.fixScale) - ((((float) ((i * 2) + 1)) * this.min_ruler_len) + (((float) (i + 1)) * this.max_ruler_len));
                    this.pointMap.put(Float.valueOf(key4), Float.valueOf(keep2point(((((float) ((i * 2) + 1)) * 0.3f) - 1.5f) + (((float) (i + 1)) * 0.4f))));
                    this.points.add(Float.valueOf(key));
                    this.points.add(Float.valueOf(key3));
                    this.points.add(Float.valueOf(key4));
                } else if (i == 6) {
                    float key5 = (((float) ((this.scaleGap * this.scaleNum) / 2)) + this.fixScale) - ((((float) (i * 2)) * this.min_ruler_len) + (((float) i) * this.max_ruler_len));
                    this.pointMap.put(Float.valueOf(key5), Float.valueOf(keep2point(((((float) (i * 2)) * 0.3f) - 1.5f) + (((float) i) * 0.4f))));
                    this.points.add(Float.valueOf(key5));
                }
            }
        }
        setMeasuredDimension(this.width, this.height);
        if (this.firstInit) {
            this.firstInit = false;
            setCurScaleValue(this.curScaleValue);
        }
    }

    private float keep2point(float value) {
        return new BigDecimal((double) value).setScale(2, 4).floatValue();
    }

    private void init(Context context) {
        this.maxRuler = BitmapFactory.decodeResource(context.getResources(), R.drawable.x8_ev_max_value);
        this.minRuler = BitmapFactory.decodeResource(context.getResources(), R.drawable.x8_ev_min_value);
        this.resultBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.x8_ev_result_value);
        this.endBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.x8_ev_end_icon);
        this.paint = new Paint(-1);
        this.min_ruler_len = (float) this.minRuler.getWidth();
        this.max_ruler_len = (float) this.maxRuler.getWidth();
        this.rulerHeight = this.maxRuler.getHeight();
        this.mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.valueAnimator = new ValueAnimator();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.isEnable) {
            obtainVelocityTracker(event);
            switch (event.getAction()) {
                case 0:
                    if (this.valueAnimator != null && this.valueAnimator.isRunning()) {
                        this.valueAnimator.end();
                        this.valueAnimator.cancel();
                    }
                    this.tempX = event.getX();
                    break;
                case 1:
                    checkRulerValue();
                    break;
                case 2:
                    this.lastMoveX += (event.getX() - this.tempX) / 30.0f;
                    if (this.lastMoveX >= ((float) (this.width / 2))) {
                        this.lastMoveX = (float) (this.width / 2);
                    } else if (this.lastMoveX < ((float) (-this.width)) + this.fixScale) {
                        this.lastMoveX = ((float) (-this.width)) + this.fixScale;
                    }
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(GLMapStaticValue.ANIMATION_FLUENT_TIME, (float) this.mMaximumVelocity);
                    this.xVelocity = (int) velocityTracker.getXVelocity();
                    autoVelocityScroll(this.xVelocity);
                    invalidate();
                    break;
            }
        }
        return true;
    }

    private VelocityTracker obtainVelocityTracker(MotionEvent event) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
        this.mVelocityTracker.addMovement(event);
        return this.mVelocityTracker;
    }

    private void recycleVelocity() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void autoVelocityScroll(final int xVelocity) {
        if (Math.abs(xVelocity) >= 1500 && !this.valueAnimator.isRunning()) {
            this.valueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration((long) Math.abs(XiaomiOAuthConstants.SCOPE_MI_CLOUD_CONTACT));
            this.valueAnimator.setInterpolator(new DecelerateInterpolator());
            this.valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float t = ((Float) X8RulerView.this.valueAnimator.getAnimatedValue()).floatValue();
                    X8RulerView.this.moveX = (t - X8RulerView.this.preTime) * ((float) xVelocity);
                    if (t != X8RulerView.this.preTime) {
                        X8RulerView.this.preTime = t;
                    }
                    if (X8RulerView.this.preTime != 0.0f) {
                        X8RulerView x8RulerView = X8RulerView.this;
                        x8RulerView.lastMoveX += X8RulerView.this.moveX;
                        if (X8RulerView.this.lastMoveX >= ((float) (X8RulerView.this.width / 2))) {
                            X8RulerView.this.lastMoveX = (float) (X8RulerView.this.width / 2);
                        } else if (X8RulerView.this.lastMoveX < ((float) (-X8RulerView.this.width)) + X8RulerView.this.fixScale) {
                            X8RulerView.this.lastMoveX = ((float) (-X8RulerView.this.width)) + X8RulerView.this.fixScale;
                        }
                        X8RulerView.this.invalidate();
                    }
                }
            });
            this.valueAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    X8RulerView.this.checkRulerValue();
                    X8RulerView.this.recycleVelocity();
                }
            });
            this.valueAnimator.start();
        }
    }

    private void checkRulerValue() {
        int len = this.points.size();
        for (int j = 0; j < len; j++) {
            if (j < len - 1) {
                float minValue;
                float preValue = ((Float) this.points.get(j)).floatValue();
                float nextValue = ((Float) this.points.get(j + 1)).floatValue();
                float maxValue;
                if (preValue > nextValue) {
                    maxValue = preValue;
                } else {
                    maxValue = nextValue;
                }
                if (preValue < nextValue) {
                    minValue = preValue;
                } else {
                    minValue = nextValue;
                }
                if (this.lastMoveX >= minValue && this.lastMoveX <= maxValue) {
                    if (Math.abs(preValue - this.lastMoveX) < Math.abs(nextValue - this.lastMoveX)) {
                        this.lastMoveX = preValue;
                    } else {
                        this.lastMoveX = nextValue;
                    }
                    this.curScaleValue = ((Float) this.pointMap.get(Float.valueOf(this.lastMoveX))).floatValue();
                    if (this.rulerListener != null) {
                        this.rulerListener.updateRuler(this.curScaleValue);
                    }
                    invalidate();
                }
            } else if (j == len - 1) {
                this.lastMoveX = ((Float) this.points.get(len - 1)).floatValue();
                this.curScaleValue = ((Float) this.pointMap.get(Float.valueOf(this.lastMoveX))).floatValue();
                if (this.rulerListener != null) {
                    this.rulerListener.updateRuler(this.curScaleValue);
                }
                invalidate();
            }
        }
        invalidate();
    }

    public void setCurScaleValue(float curScaleValue) {
        this.curScaleValue = curScaleValue;
        if (this.pointMap.size() > 0) {
            for (Entry entry : this.pointMap.entrySet()) {
                float value = ((Float) entry.getValue()).floatValue();
                float key = ((Float) entry.getKey()).floatValue();
                if (value == curScaleValue) {
                    this.lastMoveX = key;
                    invalidate();
                    return;
                }
            }
        }
    }

    public void setRulerListener(RulerListener rulerListener) {
        this.rulerListener = rulerListener;
    }

    public void setEnable(boolean enable) {
        this.isEnable = enable;
        if (this.paint != null) {
            if (enable) {
                this.paint.setColor(getResources().getColor(R.color.white_100));
            } else {
                this.paint.setColor(getResources().getColor(R.color.white_30));
            }
        }
        invalidate();
    }
}
