package com.fimi.album.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class MediaDownloadProgressView extends View {
    private static final int[] SECTION_COLORS = new int[]{-16711936, InputDeviceCompat.SOURCE_ANY, SupportMenu.CATEGORY_MASK};
    private int backColor = 0;
    private float currentCount;
    private int frontColor = 0;
    private int mHeight;
    private Paint mPaint;
    private int mWidth;
    private float maxCount;

    public MediaDownloadProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MediaDownloadProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MediaDownloadProgressView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        int round = this.mHeight / 2;
        if (this.backColor == 0) {
            this.mPaint.setColor(771751935);
        } else {
            this.mPaint.setColor(this.backColor);
        }
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, (float) this.mWidth, (float) this.mHeight), (float) round, (float) round, this.mPaint);
        RectF rectProgressBg = new RectF(0.0f, 0.0f, ((float) this.mWidth) * (this.currentCount / this.maxCount), (float) this.mHeight);
        if (this.frontColor == 0) {
            this.mPaint.setColor(1895825407);
        } else {
            this.mPaint.setColor(this.frontColor);
        }
        canvas.drawRoundRect(rectProgressBg, (float) round, (float) round, this.mPaint);
    }

    private int dipToPx(int dip) {
        return (int) ((((float) (dip >= 0 ? 1 : -1)) * 0.5f) + (((float) dip) * getContext().getResources().getDisplayMetrics().density));
    }

    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    public void setCurrentCount(float currentCount) {
        if (currentCount > this.maxCount) {
            currentCount = this.maxCount;
        }
        this.currentCount = currentCount;
        invalidate();
    }

    public float getMaxCount() {
        return this.maxCount;
    }

    public float getCurrentCount() {
        return this.currentCount;
    }

    public void setFrontColor(int frontColor) {
        this.frontColor = frontColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE || widthSpecMode == Integer.MIN_VALUE) {
            this.mWidth = widthSpecSize;
        } else {
            this.mWidth = 0;
        }
        if (heightSpecMode == Integer.MIN_VALUE || heightSpecMode == 0) {
            this.mHeight = dipToPx(15);
        } else {
            this.mHeight = heightSpecSize;
        }
        setMeasuredDimension(this.mWidth, this.mHeight);
    }
}
