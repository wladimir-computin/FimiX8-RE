package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class X8FrequencyPoint extends View {
    int colorG;
    int colorR;
    int colorW;
    int colorY;
    private Paint dashPaint;
    int lineW;
    private Paint mPaint;
    int pW;
    private int[] pencent;

    public X8FrequencyPoint(Context context) {
        super(context);
        this.colorR = -909023;
        this.colorY = -17920;
        this.colorG = -13959424;
        this.colorW = -2130706433;
        this.pW = 0;
        this.lineW = 0;
        this.pencent = new int[]{90, 50, 20, 50, 90};
    }

    public X8FrequencyPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.colorR = -909023;
        this.colorY = -17920;
        this.colorG = -13959424;
        this.colorW = -2130706433;
        this.pW = 0;
        this.lineW = 0;
        this.pencent = new int[]{90, 50, 20, 50, 90};
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.STROKE);
        this.lineW = dip2px(getContext(), 1.0f);
        this.mPaint.setStrokeWidth((float) this.lineW);
        this.dashPaint = new Paint();
        this.dashPaint.setStyle(Style.STROKE);
        this.dashPaint.setAntiAlias(true);
        this.dashPaint.setStrokeWidth(1.0f);
        this.dashPaint.setColor(this.colorW);
        this.pW = dip2px(getContext(), 4.0f);
    }

    public X8FrequencyPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.colorR = -909023;
        this.colorY = -17920;
        this.colorG = -13959424;
        this.colorW = -2130706433;
        this.pW = 0;
        this.lineW = 0;
        this.pencent = new int[]{90, 50, 20, 50, 90};
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setColor(this.colorW);
        this.mPaint.setStyle(Style.STROKE);
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), this.mPaint);
        setLayerType(1, null);
        this.dashPaint.setPathEffect(new DashPathEffect(new float[]{10.0f, 5.0f}, 0.0f));
        canvas.drawLine((float) (this.lineW + 0), ((float) getHeight()) / 3.0f, (float) (getWidth() - this.lineW), ((float) getHeight()) / 3.0f, this.dashPaint);
        canvas.drawLine((float) (this.lineW + 0), ((float) (getHeight() * 2)) / 3.0f, (float) (getWidth() - this.lineW), ((float) (getHeight() * 2)) / 3.0f, this.dashPaint);
        int w = getWidth() - (this.lineW * 2);
        int h = getHeight() - (this.lineW * 2);
        for (int i = 1; i < 6; i++) {
            float l = (((float) (w * i)) / 6.0f) - (((float) this.pW) * 0.5f);
            float r = (((float) (w * i)) / 6.0f) + (((float) this.pW) * 0.5f);
            float t = (((float) (this.pencent[i - 1] * h)) / 100.0f) + ((float) this.lineW);
            float b = (float) (getHeight() - this.lineW);
            this.mPaint.setStyle(Style.FILL);
            if (this.pencent[i - 1] >= 66) {
                this.mPaint.setColor(this.colorG);
            } else if (this.pencent[i - 1] >= 33) {
                this.mPaint.setColor(this.colorY);
            } else {
                this.mPaint.setColor(this.colorR);
            }
            canvas.drawRect(new RectF(l, t, r, b), this.mPaint);
        }
    }

    public static int dip2px(Context context, float dp) {
        return (int) (((double) (dp * context.getResources().getDisplayMetrics().density)) + 0.5d);
    }

    public void setPercent(int p) {
        p = 100 - p;
        for (int i = 0; i < this.pencent.length; i++) {
            this.pencent[i] = p;
        }
        postInvalidate();
    }
}
