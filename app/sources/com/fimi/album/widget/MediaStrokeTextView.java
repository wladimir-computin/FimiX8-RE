package com.fimi.album.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.fimi.kernel.utils.FontUtil;

public class MediaStrokeTextView extends TextView {
    private TextView borderText = null;

    public MediaStrokeTextView(Context context) {
        super(context);
        this.borderText = new TextView(context);
        FontUtil.changeDINAlernateBold(context.getAssets(), this.borderText, this);
        init();
    }

    public MediaStrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.borderText = new TextView(context, attrs);
        FontUtil.changeDINAlernateBold(context.getAssets(), this.borderText, this);
        init();
    }

    public MediaStrokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.borderText = new TextView(context, attrs, defStyle);
        FontUtil.changeDINAlernateBold(context.getAssets(), this.borderText, this);
        init();
    }

    public void init() {
        TextPaint tp1 = this.borderText.getPaint();
        tp1.setStrokeWidth(1.0f);
        tp1.setStyle(Style.STROKE);
        this.borderText.setTextColor(1275068416);
        this.borderText.setGravity(getGravity());
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
