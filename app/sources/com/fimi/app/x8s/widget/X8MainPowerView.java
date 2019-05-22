package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.fimi.app.x8s.R;

public class X8MainPowerView extends View {
    private static final String TAG = "X8PowerView";
    Bitmap mBitmap;
    private int mBpEmptySource = 0;
    private Paint mPaint;
    private int percent = 67;

    public X8MainPowerView(Context context) {
        super(context);
    }

    public X8MainPowerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mBpEmptySource = getContext().obtainStyledAttributes(attrs, R.styleable.X8MainPower, 0, 0).getResourceId(R.styleable.X8MainPower_image, 0);
        if (this.mBpEmptySource != 0) {
            this.mBitmap = BitmapFactory.decodeResource(getResources(), this.mBpEmptySource);
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(getResources().getColor(17170445));
    }

    public X8MainPowerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, this.mBitmap.getHeight());
    }

    /* Access modifiers changed, original: protected */
    @SuppressLint({"WrongConstant"})
    public void onDraw(Canvas canvas) {
        float src;
        super.onDraw(canvas);
        if (VERSION.SDK_INT <= 19) {
            this.mPaint.setColor(Color.argb(1, 0, 0, 0));
        }
        canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, 31);
        Rect dst = new Rect();
        dst.left = 0;
        dst.top = 0;
        dst.right = getWidth();
        dst.bottom = getHeight();
        canvas.drawBitmap(this.mBitmap, null, dst, null);
        this.mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        if (this.percent == 0) {
            src = 1.0f;
        } else {
            src = (100.0f - (((((float) this.percent) / 100.0f) * 85.0f) + 15.0f)) / 100.0f;
        }
        canvas.drawRect(new Rect((int) (((float) getWidth()) - (((float) getWidth()) * src)), 0, getWidth(), getHeight()), this.mPaint);
        this.mPaint.setXfermode(null);
    }

    public void setPercent(int percent) {
        if (this.percent != percent) {
            this.percent = percent;
            invalidate();
        }
    }
}
