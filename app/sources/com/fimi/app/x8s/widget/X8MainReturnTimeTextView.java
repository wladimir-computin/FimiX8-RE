package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.fimi.app.x8s.R;

public class X8MainReturnTimeTextView extends View {
    private static final String TAG = "X8MainReturnTimeTextVie";
    private float fontSize = 0.0f;
    private Paint mPaint;
    private Paint mPaintStrock;
    private Paint mPaintText;
    private String mStrTime = "00:00";
    private int percent = 100;
    private float photoWidth = 0.0f;

    public X8MainReturnTimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.X8MainReturnTimeTextView, 0, 0);
        this.photoWidth = typedArray.getDimension(R.styleable.X8MainReturnTimeTextView_width, 0.0f);
        this.fontSize = typedArray.getDimension(R.styleable.X8MainReturnTimeTextView_fontSize, 0.0f);
        typedArray.recycle();
        this.mPaint = new Paint();
        this.mPaint.setColor(getResources().getColor(R.color.x8_main_return_time_bg));
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setAntiAlias(true);
        this.mPaintStrock = new Paint();
        this.mPaintStrock.setColor(getResources().getColor(R.color.black_70));
        this.mPaintStrock.setStrokeWidth(1.0f);
        this.mPaintStrock.setStyle(Style.STROKE);
        this.mPaintStrock.setAntiAlias(true);
        this.mPaint.setAntiAlias(true);
        this.mPaintText = new Paint();
        this.mPaintText.setTextSize(this.fontSize);
        this.mPaintText.setColor(getResources().getColor(R.color.black_70));
        this.mPaintText.setAntiAlias(false);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int position = (getWidth() * this.percent) / 100;
        if (((float) position) + this.photoWidth > ((float) getWidth())) {
            position = (int) (((float) getWidth()) - this.photoWidth);
        }
        canvas.drawRoundRect(new RectF((float) position, 0.0f, ((float) position) + this.photoWidth, (float) getHeight()), 2.0f, 2.0f, this.mPaint);
        canvas.drawRoundRect(new RectF((float) position, 0.0f, ((float) position) + this.photoWidth, (float) (getHeight() - 1)), 2.0f, 2.0f, this.mPaintStrock);
        float Textx = (((float) position) + (this.photoWidth / 2.0f)) - (this.mPaintText.measureText(this.mStrTime) / 2.0f);
        FontMetrics fontMetrics = this.mPaintText.getFontMetrics();
        canvas.drawText(this.mStrTime, Textx, ((float) (getHeight() / 2)) + (((fontMetrics.descent - fontMetrics.ascent) / 2.0f) - fontMetrics.descent), this.mPaintText);
    }

    public void setPercent(int percent) {
        if (this.percent != percent) {
            this.percent = percent;
            invalidate();
        }
    }

    public void setStrTime(String strTime) {
        this.mStrTime = strTime;
        invalidate();
    }
}
