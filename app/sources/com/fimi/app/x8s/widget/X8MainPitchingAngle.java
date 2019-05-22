package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import ch.qos.logback.core.net.SyslogConstants;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.tools.X8NumberUtil;

public class X8MainPitchingAngle extends View {
    private static final String TAG = "X8MainPitchingAngle";
    private int MAX = SyslogConstants.LOG_CLOCK;
    private int MAXPROGRESS = (this.MAX - this.MIN);
    private int MIN = 5;
    private OnProgressListener listener;
    private int mBgColor;
    private Rect mBounds = new Rect();
    private Paint mPaint;
    private float mPercent = 0.0f;
    private int mProgressColor;
    private float mProgressMarginLeft;
    private float mProgressWidth;
    private final int mProjectionColor;
    private float mRadius;
    private Paint mTextBgPaint;
    private Paint mTextPaint;
    private int rectangleColor;
    private float rectangleHeight;
    private float rectangleLeft;
    private float rectangleWidth;
    private int textColor;
    private float textSize;

    public interface OnProgressListener {
        void onProgressChanage(float f);
    }

    public X8MainPitchingAngle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.X8MainPitchingAngle, 0, 0);
        this.mBgColor = typedArray.getColor(R.styleable.X8MainPitchingAngle_bg_color, ViewCompat.MEASURED_STATE_MASK);
        this.mProjectionColor = typedArray.getColor(R.styleable.X8MainPitchingAngle_progress_projection_color, ViewCompat.MEASURED_STATE_MASK);
        this.mProgressColor = typedArray.getColor(R.styleable.X8MainPitchingAngle_progress_color, ViewCompat.MEASURED_STATE_MASK);
        this.mProgressWidth = typedArray.getDimension(R.styleable.X8MainPitchingAngle_progress_bar_width, 0.0f);
        this.mProgressMarginLeft = typedArray.getDimension(R.styleable.X8MainPitchingAngle_progress_margin_left, 0.0f);
        this.mRadius = typedArray.getDimension(R.styleable.X8MainPitchingAngle_radius, 0.0f);
        this.rectangleLeft = typedArray.getDimension(R.styleable.X8MainPitchingAngle_rectangle_left, 0.0f);
        this.rectangleWidth = typedArray.getDimension(R.styleable.X8MainPitchingAngle_rectangle_width, 0.0f);
        this.rectangleHeight = typedArray.getDimension(R.styleable.X8MainPitchingAngle_rectangle_height, 0.0f);
        this.rectangleColor = typedArray.getColor(R.styleable.X8MainPitchingAngle_rectangle_color, 0);
        this.textSize = typedArray.getDimension(R.styleable.X8MainPitchingAngle_text_size, 0.0f);
        this.textColor = typedArray.getColor(R.styleable.X8MainPitchingAngle_text_color, 0);
        this.mPaint = new Paint();
        this.mPaint.setColor(this.mBgColor);
        this.mPaint.setAntiAlias(true);
        typedArray.recycle();
        this.mTextBgPaint = new Paint();
        this.mTextBgPaint.setAntiAlias(true);
        this.mTextBgPaint.setColor(this.rectangleColor);
        this.mTextPaint = new Paint();
        this.mTextPaint.setTextSize(this.textSize);
        this.mTextPaint.setColor(this.textColor);
        setLayerType(1, null);
    }

    public void setOnProgressListener(OnProgressListener listener) {
        this.listener = listener;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        drawText(canvas);
    }

    private void drawProgress(Canvas canvas) {
        float left = (this.mRadius - this.mProgressWidth) + this.mProgressMarginLeft;
        this.mPaint.setColor(this.mBgColor);
        this.mPaint.setShadowLayer(5.0f, 0.0f, 0.0f, this.mProjectionColor);
        Log.i(TAG, "drawProgress: " + left + "," + this.mProgressWidth);
        canvas.drawRoundRect(new RectF(left / 2.0f, 10.0f, (left / 2.0f) + this.mProgressWidth, (float) (getHeight() - 10)), 3.0f, 3.0f, this.mPaint);
        this.mPaint.setColor(this.mProgressColor);
        this.mPaint.setColor(this.mProgressColor);
        int circularY = (int) ((((float) getHeight()) - (((((float) getHeight()) - this.mRadius) * this.mPercent) / 100.0f)) - (this.mRadius / 2.0f));
        RectF rectFProgress = new RectF(left / 2.0f, (float) circularY, (left / 2.0f) + this.mProgressWidth, (float) (getHeight() - 10));
        this.mPaint.clearShadowLayer();
        canvas.drawRoundRect(rectFProgress, 3.0f, 3.0f, this.mPaint);
        this.mPaint.setShadowLayer(5.0f, 0.0f, 0.0f, this.mProjectionColor);
        canvas.drawCircle((this.mRadius / 2.0f) + (this.mProgressMarginLeft / 2.0f), (float) circularY, this.mRadius / 2.0f, this.mPaint);
    }

    private void drawText(Canvas canvas) {
        float heightValue = getRegulationProgress();
        FontMetrics fontMetrics = this.mTextPaint.getFontMetrics();
        float dy = ((fontMetrics.descent - fontMetrics.ascent) / 2.0f) - fontMetrics.descent;
        int intHeightValue = (int) heightValue;
        String text = X8NumberUtil.getDistanceNumberString((float) intHeightValue, 0, true);
        this.mTextPaint.getTextBounds(text, (int) this.rectangleLeft, text.length(), this.mBounds);
        float textWidth = (float) this.mBounds.width();
        float textHeight = (float) this.mBounds.height();
        RectF rectF = new RectF();
        rectF.left = this.rectangleLeft;
        rectF.top = (((float) (getHeight() / 2)) - (textHeight / 2.0f)) - dy;
        rectF.right = (this.rectangleLeft + textWidth) + (2.0f * dy);
        rectF.bottom = (((float) (getHeight() / 2)) + (textHeight / 2.0f)) + dy;
        canvas.drawRoundRect(rectF, 3.0f, 3.0f, this.mTextBgPaint);
        canvas.drawText(text, this.rectangleLeft, ((float) (getHeight() / 2)) + (textHeight / 2.0f), this.mTextPaint);
        if (this.listener != null) {
            this.listener.onProgressChanage((float) intHeightValue);
        }
    }

    public void setPercent(float percent) {
        if (this.mPercent != percent) {
            this.mPercent = percent;
            invalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 2:
                calculProgress(event.getY());
                break;
        }
        return true;
    }

    private void calculProgress(float y) {
        float progress = 0.0f;
        if (y > 0.0f) {
            if (y >= ((float) getHeight())) {
                progress = 100.0f;
            } else {
                progress = (y / ((float) getHeight())) * 100.0f;
            }
        }
        setPercent(((float) Math.round((100.0f - progress) * 10.0f)) / 10.0f);
    }

    public float getRegulationProgress() {
        return ((float) Math.round((((this.mPercent * ((float) this.MAXPROGRESS)) / 100.0f) + ((float) this.MIN)) * 10.0f)) / 10.0f;
    }

    public void setProcess(float y) {
        setPercent(((float) Math.round((((y - ((float) this.MIN)) * 100.0f) / 115.0f) * 10.0f)) / 10.0f);
    }
}
