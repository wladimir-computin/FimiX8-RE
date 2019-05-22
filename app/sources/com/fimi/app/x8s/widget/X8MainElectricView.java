package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import com.fimi.app.x8s.R;
import com.fimi.kernel.utils.DensityUtil;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8MainElectricView extends View {
    private int hightElectric = 0;
    private int lowElectric = 0;
    private Paint mPaint;
    private State mState;
    private int middleElectric = 0;
    private int mostlowElectric = 0;
    private int percent = 100;
    float v;

    public enum State {
        LOW,
        MIDDLE,
        HIGHT
    }

    public X8MainElectricView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.X8MainElectricPower, 0, 0);
        this.lowElectric = typedArray.getColor(R.styleable.X8MainElectricPower_lowElectric, -1);
        this.middleElectric = typedArray.getColor(R.styleable.X8MainElectricPower_middleElectric, -1);
        this.hightElectric = typedArray.getColor(R.styleable.X8MainElectricPower_hightElectric, -1);
        this.mostlowElectric = context.getResources().getColor(R.color.x8_battery_most_low);
        typedArray.recycle();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(-1);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.v = (float) StateManager.getInstance().getX8Drone().getLowPowerValue();
        if (this.v <= 0.0f) {
            this.v = 20.0f;
        }
        this.mPaint.setColor(-1);
        if (this.percent > 50) {
            drawType1(canvas);
        } else if (((float) this.percent) > this.v) {
            drawType2(canvas);
        } else if (this.percent > 15) {
            drawType4(canvas);
        } else if (this.percent > 10) {
            drawType3(canvas);
        } else {
            this.mPaint.setColor(this.mostlowElectric);
            canvas.drawRect(0.0f, 0.0f, (((((float) this.percent) / 100.0f) * ((float) getWidth())) * 1624.0f) / 1920.0f, (float) getHeight(), this.mPaint);
        }
    }

    public void setPercent(int percent) {
        if (this.percent != percent || this.v != ((float) StateManager.getInstance().getX8Drone().getLowPowerValue())) {
            this.percent = percent;
            invalidate();
        }
    }

    public void drawType4(Canvas canvas) {
        this.mPaint.setColor(this.middleElectric);
        canvas.drawRect(0.0f, 0.0f, (((((float) this.percent) / 100.0f) * ((float) getWidth())) * 1624.0f) / 1920.0f, (float) getHeight(), this.mPaint);
        this.mPaint.setColor(this.mostlowElectric);
        canvas.drawRect(0.0f, 0.0f, ((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f, (float) getHeight(), this.mPaint);
        float rightStart = ((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd = rightStart + ((float) DensityUtil.dip2px(getContext(), 2.0f));
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart, 0.0f, rightEnd, (float) getHeight(), this.mPaint);
        this.mPaint.setColor(this.lowElectric);
        rightStart = rightEnd;
        rightEnd = ((0.15f * ((float) getWidth())) * 1624.0f) / 1920.0f;
        canvas.drawRect(rightStart, 0.0f, rightEnd, (float) getHeight(), this.mPaint);
        rightStart = rightEnd;
        rightEnd = rightStart + ((float) DensityUtil.dip2px(getContext(), 2.0f));
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart, 0.0f, rightEnd, (float) getHeight(), this.mPaint);
    }

    public void drawType3(Canvas canvas) {
        this.mPaint.setColor(this.mostlowElectric);
        canvas.drawRect(0.0f, 0.0f, ((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f, (float) getHeight(), this.mPaint);
        float rightStart = ((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd = rightStart + ((float) DensityUtil.dip2px(getContext(), 2.0f));
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart, 0.0f, rightEnd, (float) getHeight(), this.mPaint);
        this.mPaint.setColor(this.lowElectric);
        canvas.drawRect(rightEnd, 0.0f, (((((float) this.percent) / 100.0f) * ((float) getWidth())) * 1624.0f) / 1920.0f, (float) getHeight(), this.mPaint);
    }

    public void drawType2(Canvas canvas) {
        canvas.drawRect(((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f, 0.0f, (((((float) this.percent) / 100.0f) * ((float) getWidth())) * 1624.0f) / 1920.0f, (float) getHeight(), this.mPaint);
        this.mPaint.setColor(this.mostlowElectric);
        canvas.drawRect(0.0f, 0.0f, ((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f, (float) getHeight(), this.mPaint);
        float rightStart = ((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd = rightStart + ((float) DensityUtil.dip2px(getContext(), 2.0f));
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart, 0.0f, rightEnd, (float) getHeight(), this.mPaint);
        rightStart = ((0.15f * ((float) getWidth())) * 1624.0f) / 1920.0f;
        rightEnd = rightStart + ((float) DensityUtil.dip2px(getContext(), 2.0f));
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart, 0.0f, rightEnd, (float) getHeight(), this.mPaint);
        float maxStart = (((0.5f * ((float) getWidth())) * 1624.0f) / 1920.0f) - ((float) DensityUtil.dip2px(getContext(), 2.0f));
        rightStart = (((this.v / 100.0f) * ((float) getWidth())) * 1624.0f) / 1920.0f;
        if (rightStart > maxStart) {
            rightStart = maxStart;
        }
        canvas.drawRect(rightStart, 0.0f, rightStart + ((float) DensityUtil.dip2px(getContext(), 2.0f)), (float) getHeight(), this.mPaint);
    }

    public void drawType1(Canvas canvas) {
        canvas.drawRect(((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f, 0.0f, ((0.5f * ((float) getWidth())) * 1624.0f) / 1920.0f, (float) getHeight(), this.mPaint);
        float rightStart = ((float) getWidth()) - (((0.5f * ((float) getWidth())) * 1624.0f) / 1920.0f);
        canvas.drawRect(rightStart, 0.0f, rightStart + ((((((float) (this.percent - 50)) / 100.0f) * ((float) getWidth())) * 1624.0f) / 1920.0f), (float) getHeight(), this.mPaint);
        this.mPaint.setColor(this.mostlowElectric);
        canvas.drawRect(0.0f, 0.0f, ((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f, (float) getHeight(), this.mPaint);
        rightStart = ((((float) getWidth()) * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd = rightStart + ((float) DensityUtil.dip2px(getContext(), 2.0f));
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart, 0.0f, rightEnd, (float) getHeight(), this.mPaint);
        rightStart = ((0.15f * ((float) getWidth())) * 1624.0f) / 1920.0f;
        rightEnd = rightStart + ((float) DensityUtil.dip2px(getContext(), 2.0f));
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart, 0.0f, rightEnd, (float) getHeight(), this.mPaint);
        float maxStart = (((0.5f * ((float) getWidth())) * 1624.0f) / 1920.0f) - ((float) DensityUtil.dip2px(getContext(), 2.0f));
        rightStart = (((this.v / 100.0f) * ((float) getWidth())) * 1624.0f) / 1920.0f;
        if (rightStart > maxStart) {
            rightStart = maxStart;
        }
        canvas.drawRect(rightStart, 0.0f, rightStart + ((float) DensityUtil.dip2px(getContext(), 2.0f)), (float) getHeight(), this.mPaint);
    }
}
