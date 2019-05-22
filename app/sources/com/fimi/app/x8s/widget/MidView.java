package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.fimi.app.x8s.R;
import com.fimi.kernel.utils.AbViewUtil;
import java.util.ArrayList;
import java.util.Iterator;

public class MidView extends View {
    private Bitmap birmapbg;
    private Canvas canvas;
    float centerX = 50.0f;
    float centerY = 50.0f;
    private boolean clean = false;
    private float endX;
    private float endY;
    boolean joyOkay = false;
    private float margin;
    private float maxLen;
    private final int maxValue = 512;
    Paint paint;
    private float radius = 0.0f;
    private Bitmap ringbg;
    private Bitmap rtBmp;
    ArrayList<clipType> type;

    public enum clipType {
        left,
        top,
        right,
        bottom
    }

    public MidView(Context context) {
        super(context);
    }

    public MidView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        context.obtainStyledAttributes(attrs, R.styleable.midView).recycle();
        this.paint = new Paint();
        this.birmapbg = BitmapFactory.decodeResource(getResources(), R.drawable.x8_mid_view_bg);
        this.ringbg = BitmapFactory.decodeResource(getResources(), R.drawable.x8_samll_calibration_icon);
        this.rtBmp = BitmapFactory.decodeResource(getResources(), R.drawable.x8_rc_joy_success);
        this.radius = (float) (this.ringbg.getWidth() / 2);
        this.maxLen = AbViewUtil.dip2px(context, 23.5f);
        this.margin = AbViewUtil.dip2px(context, 10.0f);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        this.paint.setStrokeWidth(8.0f);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Style.FILL);
        this.paint.setStrokeJoin(Join.ROUND);
        if (this.clean) {
            this.clean = false;
            removeAll(canvas);
            recycle(this.birmapbg, this.ringbg);
            canvas.drawBitmap(this.birmapbg, 0.0f, 0.0f, this.paint);
            this.paint.setColor(getResources().getColor(R.color.white_100));
            canvas.drawBitmap(this.ringbg, this.endX - this.radius, this.endY - this.radius, this.paint);
            return;
        }
        if (this.joyOkay) {
            this.joyOkay = false;
            removeAll(canvas);
            recycle(this.birmapbg, this.ringbg);
            canvas.drawBitmap(this.rtBmp, 0.0f, 0.0f, this.paint);
        } else {
            canvas.drawBitmap(this.birmapbg, 0.0f, 0.0f, this.paint);
            this.paint.setColor(getResources().getColor(R.color.white_100));
            canvas.drawLine(this.centerX, this.centerY, this.centerX, this.endY, this.paint);
            canvas.drawLine(this.centerX, this.centerY, this.endX, this.centerY, this.paint);
            clipPath(this.type);
            canvas.drawBitmap(this.ringbg, this.endX - this.radius, this.endY - this.radius, this.paint);
        }
        recycle(this.birmapbg, this.ringbg, this.rtBmp);
    }

    @NonNull
    private void removeAll(Canvas canvas) {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
    }

    public void recycle(Bitmap... bitmap) {
        for (Bitmap bmp : bitmap) {
            if (bmp != null && bmp.isRecycled()) {
                bmp.recycle();
            }
        }
    }

    public void setFxFy(float x, float y) {
        if (this.centerX != x || this.centerY != y) {
            this.endX = (float) (((double) this.margin) + Math.ceil((double) ((this.maxLen * x) / 512.0f)));
            this.endY = (float) (((double) this.margin) + Math.ceil((double) ((this.maxLen * y) / 512.0f)));
            invalidate();
        }
    }

    public void setType(ArrayList<clipType> typeArray) {
        this.type = typeArray;
        invalidate();
    }

    private void clipPath(ArrayList<clipType> clips) {
        if (clips != null && clips.size() > 0) {
            this.paint.setColor(getResources().getColor(R.color.x8_value_select));
            Iterator it = clips.iterator();
            while (it.hasNext()) {
                clipType mType = (clipType) it.next();
                if (mType == clipType.left) {
                    this.canvas.drawLine(this.centerX, this.centerY, (float) (((double) this.margin) + Math.ceil((double) ((0.0f * this.maxLen) / 512.0f))), this.centerY, this.paint);
                }
                if (mType == clipType.top) {
                    this.canvas.drawLine(this.centerX, this.centerY, this.centerX, (float) (((double) this.margin) + Math.ceil((double) ((0.0f * this.maxLen) / 512.0f))), this.paint);
                }
                if (mType == clipType.right) {
                    this.canvas.drawLine(this.centerX, this.centerY, (float) (((double) this.margin) + Math.ceil((double) ((1024.0f * this.maxLen) / 512.0f))), this.centerY, this.paint);
                }
                if (mType == clipType.bottom) {
                    this.canvas.drawLine(this.centerX, this.centerY, this.centerX, (float) (((double) this.margin) + Math.ceil((double) ((1024.0f * this.maxLen) / 512.0f))), this.paint);
                }
            }
        }
    }

    public void releaseAll() {
        this.endX = this.centerX;
        this.endY = this.centerY;
        this.joyOkay = false;
        this.clean = true;
        postInvalidate();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = wSpecSize;
        int resultHeight = hSpecSize;
        if (wSpecMode == Integer.MIN_VALUE && hSpecMode == Integer.MIN_VALUE) {
            resultWidth = this.birmapbg.getWidth();
            resultHeight = this.birmapbg.getHeight();
        } else if (wSpecMode == Integer.MIN_VALUE) {
            resultWidth = this.birmapbg.getWidth();
            resultHeight = hSpecSize;
        } else if (hSpecMode == Integer.MIN_VALUE) {
            resultWidth = wSpecSize;
            resultHeight = this.birmapbg.getHeight();
        }
        resultWidth = Math.min(resultWidth, wSpecSize);
        resultHeight = Math.min(resultHeight, hSpecSize);
        this.centerX = (float) (this.birmapbg.getWidth() / 2);
        this.centerY = (float) (this.birmapbg.getHeight() / 2);
        this.endX = this.centerX;
        this.endY = this.centerY;
        setMeasuredDimension(resultWidth, resultHeight);
    }

    public void joyFinish() {
        this.joyOkay = true;
        invalidate();
    }
}
