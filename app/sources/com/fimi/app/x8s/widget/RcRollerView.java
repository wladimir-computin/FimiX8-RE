package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.fimi.app.x8s.R;

public class RcRollerView extends View {
    private Bitmap bitmap;
    private int bottom;
    private final int leftDown = 1;
    private final int leftUp = 0;
    private final int maxValue = 512;
    private Paint paint;
    private int right;
    private int rollerType = 0;
    private int totalH;
    private int totalW;
    private PorterDuffXfermode xfermode;

    public RcRollerView(Context context) {
        super(context);
        init();
    }

    public RcRollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.rollerView);
        this.rollerType = array.getInteger(R.styleable.rollerView_viewType, 0);
        this.bitmap = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.rollerView_rollerSrc, 0));
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Style.FILL);
        this.paint.setDither(true);
        this.paint.setFilterBitmap(true);
        this.xfermode = new PorterDuffXfermode(Mode.SRC_IN);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int sc = canvas.saveLayer(0.0f, 0.0f, (float) this.totalW, (float) this.totalH, this.paint, 31);
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, null);
        this.paint.setXfermode(this.xfermode);
        this.paint.setColor(-1);
        if (this.rollerType == 1) {
            RectF rectF = new RectF(0.0f, 0.0f, (float) this.bitmap.getWidth(), (float) this.bottom);
            canvas.rotate(35.0f);
            canvas.drawRect(rectF, this.paint);
        } else {
            canvas.drawRect(new RectF(0.0f, 0.0f, (float) this.right, (float) this.bottom), this.paint);
        }
        this.paint.setXfermode(null);
        canvas.restoreToCount(sc);
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.totalW = w;
        this.totalH = h;
    }

    public void upRollerValue(int rollerValue) {
        if (this.rollerType == 1) {
            if (rollerValue <= 5) {
                this.bottom = 0;
                postInvalidate();
            }
            if (this.bottom <= this.bitmap.getHeight()) {
                this.right = this.bitmap.getWidth();
                this.bottom += (this.bitmap.getHeight() * rollerValue) / 512;
                postInvalidate();
            }
        } else if (this.rollerType == 0) {
            if (rollerValue <= 5) {
                this.right = 0;
                postInvalidate();
            }
            if (this.right <= this.bitmap.getWidth()) {
                this.bottom = this.bitmap.getHeight();
                this.right += (this.bitmap.getWidth() * rollerValue) / 512;
                postInvalidate();
            }
        }
    }

    public void clean() {
        this.right = 0;
        this.bottom = 0;
        invalidate();
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
            resultWidth = this.bitmap.getWidth();
            resultHeight = this.bitmap.getHeight();
        } else if (wSpecMode == Integer.MIN_VALUE) {
            resultWidth = this.bitmap.getWidth();
            resultHeight = hSpecSize;
        } else if (hSpecMode == Integer.MIN_VALUE) {
            resultWidth = wSpecSize;
            resultHeight = this.bitmap.getHeight();
        }
        setMeasuredDimension(resultWidth, resultHeight);
    }
}
