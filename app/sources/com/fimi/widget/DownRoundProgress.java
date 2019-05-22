package com.fimi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.View;
import com.fimi.sdk.R;

public class DownRoundProgress extends View {
    public static final int FILL = 1;
    public static final int STROKE = 0;
    private boolean digitalEnbale;
    private Rect mBound;
    private int max;
    private Paint paint;
    private int progress;
    private int roundColor;
    private int roundProgressColor;
    private float roundWidth;
    private float roundWidthPlan;
    private int style;
    private final int textColor;
    private boolean textIsDisplayable;
    private int textSize;

    public DownRoundProgress(Context context) {
        this(context, null);
    }

    public DownRoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownRoundProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.roundWidthPlan = 4.5f;
        this.paint = new Paint();
        this.mBound = new Rect();
        this.textColor = getResources().getColor(R.color.dialog_item_color);
        this.textSize = dip2px(context, 17.0f);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        this.roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, SupportMenu.CATEGORY_MASK);
        this.roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, -16711936);
        this.roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5.0f);
        this.roundWidthPlan = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundProgressWidth, 5.0f);
        this.max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        this.textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        this.style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        mTypedArray.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centre = getWidth() / 2;
        int radius = (int) (((float) centre) - (this.roundWidth / 2.0f));
        this.paint.setColor(this.roundColor);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth(this.roundWidth);
        this.paint.setAntiAlias(true);
        canvas.drawCircle((float) centre, (float) centre, (float) radius, this.paint);
        this.paint.setStrokeWidth(this.roundWidthPlan);
        this.paint.setColor(this.roundProgressColor);
        RectF oval = new RectF((float) (centre - radius), (float) (centre - radius), (float) (centre + radius), (float) (centre + radius));
        switch (this.style) {
            case 0:
                this.paint.setStyle(Style.STROKE);
                canvas.drawArc(oval, 270.0f, (float) ((this.progress * 360) / this.max), false, this.paint);
                break;
            case 1:
                this.paint.setStyle(Style.FILL_AND_STROKE);
                if (this.progress != 0) {
                    canvas.drawArc(oval, 270.0f, (float) ((this.progress * 360) / this.max), true, this.paint);
                    break;
                }
                break;
        }
        if (this.digitalEnbale) {
            this.paint.setAntiAlias(true);
            this.paint.setStyle(Style.FILL);
            String text = "" + getProgress() + "%";
            this.paint.setTextSize((float) this.textSize);
            this.paint.setColor(this.textColor);
            this.paint.getTextBounds(text, 0, text.length(), this.mBound);
            canvas.drawText(text, (oval.width() / 2.0f) - ((float) (this.mBound.width() / 2)), (oval.height() / 2.0f) + ((float) (this.mBound.height() / 2)), this.paint);
        }
    }

    public void enbaleDrawDigital(boolean b) {
        this.digitalEnbale = b;
    }

    public synchronized int getMax() {
        return this.max;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    public synchronized int getProgress() {
        return this.progress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > this.max) {
            progress = this.max;
        }
        if (progress <= this.max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getCricleColor() {
        return this.roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return this.roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public float getRoundWidth() {
        return this.roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public void setProgressTextSize(int size) {
        this.textSize = dip2px(getContext(), (float) size);
    }
}
