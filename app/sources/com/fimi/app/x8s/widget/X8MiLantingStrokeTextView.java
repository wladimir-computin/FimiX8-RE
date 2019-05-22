package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.fimi.kernel.utils.FontUtil;

@SuppressLint({"AppCompatCustomView"})
public class X8MiLantingStrokeTextView extends TextView {
    private TextView borderText = null;

    public X8MiLantingStrokeTextView(Context context) {
        super(context);
        this.borderText = new TextView(context);
        init(context);
    }

    public X8MiLantingStrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.borderText = new TextView(context, attrs);
        init(context);
    }

    public X8MiLantingStrokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.borderText = new TextView(context, attrs, defStyle);
        init(context);
    }

    public void init(Context mContext) {
        TextPaint tp1 = this.borderText.getPaint();
        tp1.setStrokeWidth(1.0f);
        tp1.setStyle(Style.STROKE);
        this.borderText.setTextColor(855638016);
        this.borderText.setGravity(getGravity());
        FontUtil.changeFontLanTing(mContext.getAssets(), this.borderText, this);
    }

    public void setLayoutParams(LayoutParams params) {
        super.setLayoutParams(params);
        this.borderText.setLayoutParams(params);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = this.borderText.getText();
        if (tt == null || !tt.equals(getText())) {
            this.borderText.setText(getText());
            postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.borderText.layout(left, top, right, bottom);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        this.borderText.draw(canvas);
        super.onDraw(canvas);
    }
}
