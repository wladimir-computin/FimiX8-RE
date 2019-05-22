package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.fimi.app.x8s.R;
import org.slf4j.Marker;

public class X8SliderbarView extends View {
    private static final String TAG = "ScaleView";
    private int MAX_VALUE;
    private int MIN_VALUE;
    private float currentX;
    private int duration;
    private boolean isDrag;
    private boolean isInit;
    private Paint mLinePaint;
    private Paint mMaxRoundPaint;
    private Paint mMinRoundPaint;
    private OnScaleViewChangeListener mOnScaleViewChangeListener;
    private Paint mPaint;
    private int mProgress;
    private Bitmap mRoundBmp;
    private Paint mScaleTextPaint;
    private float radius;
    int refreshCount;
    private boolean refreshProgress;
    private int scaleCount;
    private float scaleHeight;
    private float scaleOneWidth;
    private float scaleStrokeWidth;
    private float scaleTextSize;
    private float scaleX;
    private float scaleY;
    private float tranX;

    public interface OnScaleViewChangeListener {
        void onProgressChanged(int i);
    }

    public X8SliderbarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.scaleCount = 0;
        this.scaleHeight = 0.0f;
        this.scaleOneWidth = 0.0f;
        this.scaleStrokeWidth = 0.0f;
        this.scaleTextSize = 0.0f;
        this.scaleX = 0.0f;
        this.scaleY = 0.0f;
        this.duration = 5;
        this.currentX = 0.0f;
        this.tranX = 0.0f;
        this.refreshCount = 0;
        this.isDrag = false;
        this.isInit = false;
        this.refreshProgress = false;
        this.scaleCount = 20;
        this.scaleHeight = (float) dip2px(getContext(), 4.3f);
        this.scaleStrokeWidth = (float) dip2px(getContext(), 2.0f);
        this.scaleTextSize = (float) dip2px(getContext(), 14.0f);
        this.radius = (float) dip2px(getContext(), 1.0f);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(this.scaleStrokeWidth - 2.0f);
        this.mPaint.setColor(Color.parseColor("#dedede"));
        this.mScaleTextPaint = new Paint();
        this.mScaleTextPaint.setAntiAlias(true);
        this.mScaleTextPaint.setColor(Color.parseColor("#ff4c31"));
        this.mScaleTextPaint.setTextSize((float) dip2px(getContext(), 14.0f));
        this.mScaleTextPaint.setTextSize(this.scaleTextSize);
        this.mMaxRoundPaint = new Paint();
        this.mMaxRoundPaint.setAntiAlias(true);
        this.mMinRoundPaint = new Paint();
        this.mMinRoundPaint.setAntiAlias(true);
        this.mMinRoundPaint.setColor(Color.parseColor("#ff4c31"));
        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setStrokeWidth(((this.scaleStrokeWidth - 2.0f) / 2.0f) * 3.0f);
        this.mLinePaint.setColor(Color.parseColor("#ff4c31"));
        this.refreshCount = this.duration;
        this.MAX_VALUE = this.scaleCount / 2;
        this.MIN_VALUE = (-this.scaleCount) / 2;
        this.mRoundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.x8_img_sliderbar_round);
    }

    public static int dip2px(Context context, float dp) {
        return (int) (((double) (dp * context.getResources().getDisplayMetrics().density)) + 0.5d);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!this.isInit) {
            this.currentX = (float) (getMeasuredWidth() / 2);
            this.isInit = true;
        }
        this.scaleOneWidth = ((float) (getMeasuredWidth() - 40)) / (((float) (this.scaleCount + 2)) * 1.0f);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.refreshProgress) {
            this.refreshProgress = false;
            this.currentX = ((float) (getWidth() / 2)) + (((float) this.mProgress) * this.scaleOneWidth);
        }
        drawScale(canvas);
        canvas.drawLine((float) (getWidth() / 2), (float) (getHeight() / 2), this.currentX, (float) (getHeight() / 2), this.mLinePaint);
        canvas.drawCircle((float) (getWidth() / 2), (float) ((getHeight() / 2) - dip2px(getContext(), 5.0f)), this.radius, this.mMinRoundPaint);
        canvas.drawCircle(this.currentX, (float) (getHeight() / 2), this.radius, this.mMaxRoundPaint);
        canvas.drawBitmap(this.mRoundBmp, this.currentX - ((float) (this.mRoundBmp.getWidth() / 2)), (float) ((getHeight() / 2) - (this.mRoundBmp.getHeight() / 2)), this.mMaxRoundPaint);
        calculationProgress(this.currentX);
        if (this.mProgress > 0) {
            canvas.drawText(Marker.ANY_NON_NULL_MARKER + this.mProgress, this.currentX - ((float) (getTextWidth(this.mScaleTextPaint, Marker.ANY_NON_NULL_MARKER + this.mProgress) / 2)), (float) ((getHeight() / 2) - (this.mRoundBmp.getHeight() / 2)), this.mScaleTextPaint);
        } else {
            canvas.drawText(this.mProgress + "", this.currentX - ((float) (getTextWidth(this.mScaleTextPaint, this.mProgress + "") / 2)), (float) ((getHeight() / 2) - (this.mRoundBmp.getHeight() / 2)), this.mScaleTextPaint);
        }
        if (this.isDrag) {
            Log.i(TAG, "onDraw: ");
            invalidate();
            return;
        }
        refresh();
    }

    private void drawScale(Canvas canvas) {
        canvas.drawLine(this.scaleOneWidth + 20.0f, (float) (getHeight() / 2), ((float) (getWidth() - 20)) - this.scaleOneWidth, (float) (getHeight() / 2), this.mPaint);
    }

    public int getTextWidth(Paint mPaint, String str) {
        float iSum = 0.0f;
        if (!(str == null || str.equals(""))) {
            int len = str.length();
            float[] widths = new float[len];
            mPaint.getTextWidths(str, widths);
            for (int i = 0; i < len; i++) {
                iSum = (float) (((double) iSum) + Math.ceil((double) widths[i]));
            }
        }
        return (int) iSum;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                tranlateX(event.getX(), event.getY());
                break;
            case 1:
                this.refreshCount = 20;
                if (event.getX() > ((float) (getWidth() / 2)) + ((this.scaleOneWidth * ((float) this.scaleCount)) / 2.0f)) {
                    this.currentX = ((float) (getWidth() / 2)) + ((this.scaleOneWidth * ((float) this.scaleCount)) / 2.0f);
                } else if (event.getX() < ((float) (getWidth() / 2)) - ((this.scaleOneWidth * ((float) this.scaleCount)) / 2.0f)) {
                    this.currentX = ((float) (getWidth() / 2)) - ((this.scaleOneWidth * ((float) this.scaleCount)) / 2.0f);
                } else {
                    this.currentX = event.getX();
                }
                this.isDrag = false;
                break;
            case 2:
                this.refreshCount = 20;
                if (event.getX() > ((float) (getWidth() / 2)) + ((this.scaleOneWidth * ((float) this.scaleCount)) / 2.0f)) {
                    this.currentX = ((float) (getWidth() / 2)) + ((this.scaleOneWidth * ((float) this.scaleCount)) / 2.0f);
                } else if (event.getX() < ((float) (getWidth() / 2)) - ((this.scaleOneWidth * ((float) this.scaleCount)) / 2.0f)) {
                    this.currentX = ((float) (getWidth() / 2)) - ((this.scaleOneWidth * ((float) this.scaleCount)) / 2.0f);
                } else {
                    this.currentX = event.getX();
                }
                if (!this.isDrag) {
                    this.isDrag = true;
                    invalidate();
                    break;
                }
                break;
            case 3:
                this.refreshCount = 20;
                this.isDrag = false;
                break;
        }
        return true;
    }

    public void calculationProgress(float x) {
        if (x > ((float) (getWidth() / 2))) {
            x += this.scaleOneWidth / 2.0f;
        } else {
            x -= this.scaleOneWidth / 2.0f;
        }
        this.mProgress = (int) ((x - ((float) (getWidth() / 2))) / this.scaleOneWidth);
        if (this.mOnScaleViewChangeListener != null) {
            this.mOnScaleViewChangeListener.onProgressChanged(this.mProgress);
        }
    }

    public void tranlateX(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.refreshCount = 0;
        this.tranX = (this.scaleX - this.currentX) / ((float) this.duration);
        refresh();
    }

    public void refresh() {
        if (this.refreshCount < this.duration) {
            this.refreshCount++;
            this.currentX += this.tranX;
            Log.i(TAG, "refresh: ");
            invalidate();
        }
    }

    public void setOnScaleViewChangeListener(OnScaleViewChangeListener onScaleViewChangeListener) {
        this.mOnScaleViewChangeListener = onScaleViewChangeListener;
    }

    public void setProgress(int progress) {
        this.refreshProgress = true;
        if (progress > this.MAX_VALUE) {
            progress = this.MAX_VALUE;
        }
        if (progress < this.MIN_VALUE) {
            progress = this.MIN_VALUE;
        }
        this.mProgress = progress;
        Log.i(TAG, "setProgress: " + progress + ";" + this.scaleOneWidth + "," + getWidth());
        invalidate();
    }
}
