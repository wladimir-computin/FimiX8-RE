package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.fimi.app.x8s.R;
import com.fimi.kernel.utils.DensityUtil;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8SeekBarView extends View {
    private int bgColor;
    private float cicleR;
    private float cicleX;
    private float cicleY;
    private float downX;
    private float downY;
    private float dpLineH = 1.33f;
    private float dpMaginW = 30.0f;
    private float dpR = 0.667f;
    private float dpThumpW = 15.33f;
    private float dph = 40.0f;
    private float endX;
    private float endY;
    private int h = 0;
    private boolean isInnerClick;
    private int lineH;
    private SlideChangeListener listener;
    private float locationX;
    private Paint mPaint;
    private float maginW;
    private int maxProgress = 100;
    private int progress;
    private int progressColor;
    private float r = 1.33f;
    private float startX;
    private float startY;
    private int w = 0;

    public interface SlideChangeListener {
        void onProgress(X8SeekBarView x8SeekBarView, int i);

        void onStart(X8SeekBarView x8SeekBarView, int i);

        void onStop(X8SeekBarView x8SeekBarView, int i);
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public X8SeekBarView(Context context) {
        super(context);
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.listener != null) {
            this.listener.onProgress(this, progress);
        }
        invalidate();
    }

    public X8SeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public X8SeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.bgColor = getContext().getResources().getColor(R.color.white_15);
        this.progressColor = getContext().getResources().getColor(R.color.white_60);
    }

    public void onDraw(Canvas canvas) {
        if (this.w == 0) {
            this.w = getMeasuredWidth();
        }
        if (this.h != 0) {
            this.mPaint.setColor(this.bgColor);
            this.startX = this.maginW / 2.0f;
            this.endX = ((float) this.w) - (this.maginW / 2.0f);
            this.startY = (((float) this.h) / 2.0f) - (((float) this.lineH) / 2.0f);
            this.endY = (((float) this.h) / 2.0f) + (((float) this.lineH) / 2.0f);
            canvas.drawRoundRect(new RectF(this.startX, this.startY, this.endX, this.endY), this.r, this.r, this.mPaint);
            this.cicleX = this.startX + (((((float) this.w) - this.maginW) * ((float) this.progress)) / ((float) this.maxProgress));
            this.cicleY = (float) ((int) (((float) this.h) / 2.0f));
            this.mPaint.setColor(this.progressColor);
            canvas.drawRoundRect(new RectF(this.startX, this.startY, this.cicleX, this.endY), this.r, this.r, this.mPaint);
            this.mPaint.setColor(-1);
            canvas.drawCircle(this.cicleX, this.cicleY, this.cicleR, this.mPaint);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.h = DensityUtil.dip2px(getContext(), this.dph);
        this.maginW = (float) DensityUtil.dip2px(getContext(), this.dpMaginW);
        this.lineH = DensityUtil.dip2px(getContext(), this.dpLineH);
        this.r = (float) DensityUtil.dip2px(getContext(), this.dpR);
        this.cicleR = ((float) DensityUtil.dip2px(getContext(), this.dpThumpW)) / 2.0f;
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), this.h);
    }

    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
            case NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE /*1073741824*/:
                return specSize;
            case 0:
                return size;
            default:
                return result;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                getParent().requestDisallowInterceptTouchEvent(true);
                this.isInnerClick = isInnerMthum(event);
                if (this.isInnerClick && this.listener != null) {
                    this.listener.onStart(this, this.progress);
                }
                this.downX = event.getX();
                this.downY = event.getY();
                break;
            case 1:
                getParent().requestDisallowInterceptTouchEvent(false);
                if (this.isInnerClick && this.listener != null) {
                    this.listener.onStop(this, this.progress);
                    break;
                }
            case 2:
                if (this.isInnerClick) {
                    this.locationX = event.getX();
                    fixLocationX();
                    this.progress = Math.round(((this.locationX - this.startX) / (((float) this.w) - this.maginW)) * ((float) this.maxProgress));
                    if (this.listener != null) {
                        this.listener.onProgress(this, this.progress);
                    }
                    this.downY = event.getY();
                    this.downX = event.getX();
                    invalidate();
                    break;
                }
                break;
        }
        return true;
    }

    private boolean isInnerMthum(MotionEvent event) {
        if (event.getX() < 0.0f || event.getX() > ((float) this.w) || event.getY() < 0.0f || event.getY() > ((float) this.h)) {
            return false;
        }
        return true;
    }

    private void fixLocationX() {
        if (this.locationX < this.startX) {
            this.locationX = this.startX;
        } else if (this.locationX > this.endX) {
            this.locationX = this.endX;
        }
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setOnSlideChangeListener(SlideChangeListener l) {
        this.listener = l;
    }
}
