package com.fimi.libperson.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.fimi.kernel.utils.AbViewUtil;

public class LargeView extends View {
    private static final String TAG = "LargeView";
    private static final int sHeight = 1920;
    private static final int sWidth = 1080;
    private Bitmap bitmap;
    private boolean bitmapIsCached;
    private Paint bitmapPaint = new Paint();
    private long duration = 20000;
    private boolean isFirst = true;
    private boolean isUp = true;
    private boolean mReady = false;
    private Matrix matrix;
    private float scale;
    private long startTime;
    private PointF vTranslate = new PointF();

    public LargeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.bitmapPaint.setAntiAlias(true);
        this.bitmapPaint.setFilterBitmap(true);
        this.bitmapPaint.setDither(true);
        this.matrix = new Matrix();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        boolean z = true;
        super.onDraw(canvas);
        this.matrix.reset();
        if (this.mReady) {
            boolean finished;
            this.scale = ((float) AbViewUtil.getScreenWidth(getContext())) / (((float) this.bitmap.getWidth()) * 1.0f);
            Log.i(TAG, "onDraw: " + this.bitmap.getHeight() + "," + this.bitmap.getWidth() + "," + AbViewUtil.getScreenWidth(getContext()) + "," + AbViewUtil.getScreenHeight(getContext()));
            if (this.isFirst) {
                this.startTime = System.currentTimeMillis();
                this.isFirst = false;
            }
            long scaleElapsed = System.currentTimeMillis() - this.startTime;
            if (scaleElapsed > this.duration) {
                finished = true;
            } else {
                finished = false;
            }
            float percent = ((float) scaleElapsed) / (((float) this.duration) * 1.0f);
            if (!this.isUp) {
                percent = 1.0f - percent;
            }
            this.vTranslate.y = (-percent) * ((((float) this.bitmap.getHeight()) * this.scale) - ((float) AbViewUtil.getScreenHeight(getContext())));
            Log.i(TAG, "onDraw: " + this.vTranslate.y + ",scale:" + this.scale + ",percent:" + percent);
            if (finished) {
                this.startTime = System.currentTimeMillis();
                if (this.isUp) {
                    z = false;
                }
                this.isUp = z;
            }
            invalidate();
        }
        this.matrix.setScale(this.scale, this.scale);
        this.matrix.postTranslate(0.0f, this.vTranslate.y);
        if (this.bitmap != null) {
            canvas.drawBitmap(this.bitmap, this.matrix, this.bitmapPaint);
        }
    }

    public boolean isReady() {
        return this.mReady;
    }

    public void setReady(boolean ready) {
        this.mReady = ready;
        if (this.mReady) {
            this.startTime = System.currentTimeMillis();
            invalidate();
        }
    }

    public void setRecyle() {
        this.mReady = false;
        if (this.bitmap != null && !this.bitmap.isRecycled()) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
    }

    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (bitmap != null) {
            this.startTime = System.currentTimeMillis();
            this.mReady = true;
            invalidate();
        }
    }
}
