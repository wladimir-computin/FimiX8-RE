package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.fimi.app.x8s.R;
import com.fimi.kernel.Constants;

public class X8Camera9GridView extends View {
    private int indexStartX;
    private int indexStartY;
    private Paint paint;
    private float screenHeight;
    private float screenWidth;
    private int type = 2;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
        postInvalidate();
    }

    public X8Camera9GridView(Context context) {
        super(context);
        initPaint();
    }

    public X8Camera9GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public X8Camera9GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        this.paint = new Paint();
        this.paint.setColor(getResources().getColor(R.color.white_100));
        this.paint.setAlpha(153);
        this.paint.setStrokeWidth(1.0f);
        this.paint.setAntiAlias(true);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, (float) this.indexStartY, this.screenWidth, (float) this.indexStartY, this.paint);
        canvas.drawLine(0.0f, (float) (this.indexStartY * 2), this.screenWidth, (float) (this.indexStartY * 2), this.paint);
        canvas.drawLine((float) this.indexStartX, 0.0f, (float) this.indexStartX, this.screenHeight, this.paint);
        canvas.drawLine((float) (this.indexStartX * 2), 0.0f, (float) (this.indexStartX * 2), this.screenHeight, this.paint);
        if (this.type == 1) {
            canvas.drawLine(0.0f, 0.0f, this.screenWidth, this.screenHeight, this.paint);
            canvas.drawLine(this.screenWidth, 0.0f, 0.0f, this.screenHeight, this.paint);
            if (Constants.isFactoryApp()) {
                canvas.drawLine(this.screenWidth / 2.0f, 0.0f, this.screenWidth / 2.0f, this.screenHeight, this.paint);
                canvas.drawLine(0.0f, this.screenHeight / 2.0f, this.screenWidth, this.screenHeight / 2.0f, this.paint);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            this.screenWidth = (float) w;
            this.screenHeight = (float) h;
            this.indexStartX = (int) (this.screenWidth / 3.0f);
            this.indexStartY = (int) (this.screenHeight / 3.0f);
        }
    }
}
