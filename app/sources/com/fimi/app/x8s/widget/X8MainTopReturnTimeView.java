package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.fimi.app.x8s.R;
import com.fimi.kernel.utils.AbViewUtil;

public class X8MainTopReturnTimeView extends View {
    private static final String TAG = "X8TopReturnView";
    private int SPACING;
    private Bitmap mBpEmpty;
    private int mBpEmptySource = 0;
    private Bitmap mBpFull;
    private int mBpFullSource = 0;
    private Bitmap mBpMiddle;
    private int mBpMiddleSource = 0;
    private Paint mPaint;
    private int percent = 0;

    public X8MainTopReturnTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.x8_mask_image, 0, 0);
        this.mBpEmptySource = typedArray.getResourceId(R.styleable.x8_mask_image_empty_image, 0);
        this.mBpMiddleSource = typedArray.getResourceId(R.styleable.x8_mask_image_middle_image, 0);
        this.mBpFullSource = typedArray.getResourceId(R.styleable.x8_mask_image_full_image, 0);
        if (this.mBpEmptySource != 0) {
            this.mBpEmpty = BitmapFactory.decodeResource(getResources(), this.mBpEmptySource);
        }
        if (this.mBpMiddleSource != 0) {
            this.mBpMiddle = BitmapFactory.decodeResource(getResources(), this.mBpMiddleSource);
        }
        if (this.mBpFullSource != 0) {
            this.mBpFull = BitmapFactory.decodeResource(getResources(), this.mBpFullSource);
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(getResources().getColor(17170445));
        this.SPACING = (int) AbViewUtil.dip2px(context, 2.0f);
    }

    /* Access modifiers changed, original: protected */
    @RequiresApi(api = 21)
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mBpEmpty != null) {
        }
        canvas.drawBitmap(this.mBpEmpty, 0.0f, 0.0f, null);
        if (this.percent > 0) {
            drawPercent(canvas, this.percent);
        }
    }

    public void drawPercent(Canvas canvas, int percent) {
        canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, 31);
        if (percent > 100) {
            percent = 100;
        }
        Path path = new Path();
        path.moveTo((float) ((getWidth() - ((getWidth() * (100 - percent)) / 100)) + this.SPACING), 0.0f);
        path.lineTo((float) (getWidth() - ((getWidth() * (100 - percent)) / 100)), (float) getHeight());
        path.lineTo((float) getWidth(), (float) getHeight());
        path.lineTo((float) getWidth(), 0.0f);
        path.close();
        if (percent > 50) {
            canvas.drawBitmap(this.mBpFull, 0.0f, 0.0f, null);
        } else {
            canvas.drawBitmap(this.mBpMiddle, 0.0f, 0.0f, null);
        }
        this.mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawPath(path, this.mPaint);
        this.mPaint.setXfermode(null);
        canvas.restore();
    }

    public void setPercent(int percent) {
        if (this.percent != percent) {
            this.percent = percent;
            invalidate();
        }
    }
}
