package com.fimi.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;
import com.fimi.sdk.R;
import java.lang.reflect.Field;

@SuppressLint({"AppCompatCustomView"})
public class StrokeTextView extends TextView {
    int mInnerColor;
    int mOuterColor;
    TextPaint m_TextPaint = getPaint();
    private boolean m_bDrawSideLine = true;

    public StrokeTextView(Context context, int outerColor, int innnerColor) {
        super(context);
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        this.mInnerColor = a.getColor(R.styleable.StrokeTextView_innnerColor, ViewCompat.MEASURED_SIZE_MASK);
        this.mOuterColor = a.getColor(R.styleable.StrokeTextView_outerColor, 1275068416);
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle, int outerColor, int innnerColor) {
        super(context, attrs, defStyle);
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.m_bDrawSideLine) {
            setTextColorUseReflection(this.mOuterColor);
            this.m_TextPaint.setStrokeWidth(3.0f);
            this.m_TextPaint.setStyle(Style.STROKE);
            this.m_TextPaint.setFakeBoldText(false);
            super.onDraw(canvas);
            setTextColorUseReflection(this.mInnerColor);
            this.m_TextPaint.setStrokeWidth(0.0f);
            this.m_TextPaint.setStyle(Style.STROKE);
            this.m_TextPaint.setFakeBoldText(false);
        }
        super.onDraw(canvas);
    }

    private void setTextColorUseReflection(int color) {
        try {
            Field textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, Integer.valueOf(color));
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        this.m_TextPaint.setColor(color);
    }
}
