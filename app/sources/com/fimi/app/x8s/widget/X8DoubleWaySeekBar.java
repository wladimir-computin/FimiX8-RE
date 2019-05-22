package com.fimi.app.x8s.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.fimi.app.x8s.R;

public class X8DoubleWaySeekBar extends View {
    private int mBgBottom;
    private int mBgTop;
    private Paint mCenterPaint;
    private float mCurrentLeftOffset = 0.0f;
    private int mHalfDrawableWidth;
    private int mHeight;
    private boolean mIsOnDrag;
    private float mLastX;
    private OnSeekProgressListener mListener;
    private Paint mNormalPaint;
    private float mPointerBottom;
    private Drawable mPointerDrawable;
    private float mPointerLeft;
    private Paint mPointerPaint;
    private float mPointerRight;
    private float mPointerTop;
    private Paint mProgressPaint;
    private int mRoundSize;
    private int mSeekBarLeft;
    private int mSeekBarRight;
    private int mViewMiddleXPos;
    private int mWidth;
    private float total_len;

    public interface OnSeekProgressListener {
        void onPointerPositionChanged(int i, int i2);

        void onSeekProgress(int i);

        void onSizeChanged();
    }

    public X8DoubleWaySeekBar(Context context) {
        super(context);
        init(null);
    }

    public X8DoubleWaySeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public X8DoubleWaySeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int progressColor = Color.parseColor("#FF4081");
        int backgroundColor = Color.parseColor("#BBBBBB");
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.doubleWaySeekBar);
            this.mPointerDrawable = a.getDrawable(R.styleable.doubleWaySeekBar_pointerBackground);
            this.mHalfDrawableWidth = this.mPointerDrawable.getIntrinsicWidth() / 2;
            progressColor = a.getColor(R.styleable.doubleWaySeekBar_progressColor, Color.parseColor("#FF4081"));
            backgroundColor = a.getColor(R.styleable.doubleWaySeekBar_backgroundColor, Color.parseColor("#BBBBBB"));
            a.recycle();
        }
        this.mNormalPaint = new Paint();
        this.mNormalPaint.setColor(backgroundColor);
        this.mPointerPaint = new Paint();
        this.mPointerPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.mProgressPaint = new Paint();
        this.mProgressPaint.setColor(progressColor);
        this.mCenterPaint = new Paint();
        this.mCenterPaint.setColor(-1);
        this.mCenterPaint.setStyle(Style.FILL);
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        this.mSeekBarLeft = this.mHalfDrawableWidth;
        this.mSeekBarRight = this.mWidth - this.mHalfDrawableWidth;
        this.mBgTop = 15;
        this.mBgBottom = this.mHeight - 15;
        this.mRoundSize = this.mHeight / 2;
        this.mViewMiddleXPos = this.mWidth / 2;
        this.total_len = (float) (this.mViewMiddleXPos - this.mHalfDrawableWidth);
        this.mPointerLeft = (float) this.mViewMiddleXPos;
        this.mLastX = this.mPointerLeft;
        calculatePointerRect();
        if (this.mListener != null) {
            this.mListener.onSizeChanged();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF();
        rectF.left = (float) this.mSeekBarLeft;
        rectF.right = (float) this.mSeekBarRight;
        rectF.top = (float) this.mBgTop;
        rectF.bottom = (float) this.mBgBottom;
        canvas.drawRoundRect(rectF, (float) this.mRoundSize, (float) this.mRoundSize, this.mNormalPaint);
        if (this.mPointerRight < ((float) this.mViewMiddleXPos)) {
            canvas.drawRect(this.mPointerRight - ((float) this.mHalfDrawableWidth), (float) this.mBgTop, (float) this.mViewMiddleXPos, (float) this.mBgBottom, this.mProgressPaint);
        }
        if (this.mPointerLeft > ((float) this.mViewMiddleXPos)) {
            Canvas canvas2 = canvas;
            canvas2.drawRect((float) this.mViewMiddleXPos, (float) this.mBgTop, ((float) this.mHalfDrawableWidth) + this.mPointerLeft, (float) this.mBgBottom, this.mProgressPaint);
        }
        RectF rectF1 = new RectF();
        rectF1.left = (float) ((this.mSeekBarLeft + ((this.mSeekBarRight - this.mSeekBarLeft) / 2)) - 2);
        rectF1.right = (float) ((this.mSeekBarRight - ((this.mSeekBarRight - this.mSeekBarLeft) / 2)) + 2);
        rectF1.top = (float) (this.mBgTop - 2);
        rectF1.bottom = (float) (this.mBgBottom + 2);
        canvas.drawRect(rectF1, this.mCenterPaint);
        Rect rect = new Rect();
        rect.left = (int) this.mPointerLeft;
        rect.top = (int) this.mPointerTop;
        rect.right = (int) this.mPointerRight;
        rect.bottom = (int) this.mPointerBottom;
        this.mPointerDrawable.setBounds(rect);
        this.mPointerDrawable.draw(canvas);
        if (this.mListener != null) {
            this.mListener.onPointerPositionChanged((((int) getX()) + rect.left) + this.mHalfDrawableWidth, (int) getY());
            callbackProgress();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                return handleDownEvent(event);
            case 1:
            case 3:
                return handleUpEvent(event);
            case 2:
                return handleMoveEvent(event);
            default:
                return false;
        }
    }

    private boolean handleUpEvent(MotionEvent event) {
        if (!this.mIsOnDrag) {
            return false;
        }
        this.mIsOnDrag = false;
        return true;
    }

    private boolean handleMoveEvent(MotionEvent event) {
        float x = event.getX();
        if (!this.mIsOnDrag) {
            return false;
        }
        this.mCurrentLeftOffset = x - this.mLastX;
        calculatePointerRect();
        if (this.mPointerLeft <= 0.0f) {
            this.mPointerLeft = 0.0f;
            this.mPointerRight = this.mPointerLeft + ((float) this.mPointerDrawable.getIntrinsicWidth());
        }
        if (this.mPointerLeft >= ((float) (this.mWidth - (this.mHalfDrawableWidth * 2)))) {
            this.mPointerLeft = (float) (this.mWidth - (this.mHalfDrawableWidth * 2));
            this.mPointerRight = this.mPointerLeft + ((float) this.mPointerDrawable.getIntrinsicWidth());
        }
        invalidate();
        this.mLastX = x;
        return true;
    }

    public void setProgress(float percent) {
        if (((double) percent) >= -100.1d && ((double) percent) <= 100.1d) {
            this.mPointerLeft = this.total_len + ((this.total_len / 100.0f) * percent);
            this.mPointerRight = this.mPointerLeft + ((float) (this.mHalfDrawableWidth * 2));
            invalidate();
        }
    }

    private void callbackProgress() {
        int percent = (int) (((this.mPointerLeft - this.total_len) * 100.0f) / this.total_len);
        callbackProgressInternal(percent);
        Log.i("ljh", "percent ： " + percent + " left : " + this.mPointerLeft);
    }

    private void callbackProgressInternal(int progress) {
        if (this.mListener != null) {
            this.mListener.onSeekProgress(progress);
        }
    }

    private boolean handleDownEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x < this.mPointerLeft - 30.0f || x > this.mPointerRight + 30.0f || y < this.mPointerTop || y > this.mPointerBottom) {
            return false;
        }
        this.mIsOnDrag = true;
        this.mLastX = x;
        return true;
    }

    private void calculatePointerRect() {
        float pointerLeft = getPointerLeft(this.mCurrentLeftOffset);
        float pointerRight = pointerLeft + ((float) this.mPointerDrawable.getIntrinsicWidth());
        this.mPointerLeft = pointerLeft;
        this.mPointerRight = pointerRight;
        this.mPointerTop = 0.0f;
        this.mPointerBottom = (float) this.mHeight;
    }

    public void resetSeekBar() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[]{this.mPointerLeft, (float) (this.mViewMiddleXPos - this.mHalfDrawableWidth)});
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                X8DoubleWaySeekBar.this.mPointerLeft = ((Float) animation.getAnimatedValue()).floatValue();
                X8DoubleWaySeekBar.this.mPointerRight = X8DoubleWaySeekBar.this.mPointerLeft + ((float) X8DoubleWaySeekBar.this.mPointerDrawable.getIntrinsicWidth());
                X8DoubleWaySeekBar.this.invalidate();
            }
        });
        valueAnimator.start();
    }

    private float getPointerLeft(float offset) {
        return this.mPointerLeft + offset;
    }

    public void setOnSeekProgressListener(OnSeekProgressListener listener) {
        this.mListener = listener;
    }
}
