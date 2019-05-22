package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.fimi.app.x8s.R;
import java.text.DecimalFormat;
import java.util.Random;

public class X8PieView extends View {
    private int centerX;
    private int centerY;
    private float circleWidth;
    private int[] colors;
    private Paint dataPaint;
    private Rect dataTextBound;
    private int dataTextColor;
    private float dataTextSize;
    private Paint mArcPaint;
    private String[] names;
    private int[] numbers;
    private float radius;
    private Random random;
    private RectF rectF;
    private int sum;

    public X8PieView(Context context) {
        super(context);
        this.dataTextBound = new Rect();
        this.random = new Random();
        this.dataTextSize = 30.0f;
        this.dataTextColor = SupportMenu.CATEGORY_MASK;
        this.circleWidth = 100.0f;
        init();
    }

    public X8PieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X8PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.dataTextBound = new Rect();
        this.random = new Random();
        this.dataTextSize = 30.0f;
        this.dataTextColor = SupportMenu.CATEGORY_MASK;
        this.circleWidth = 100.0f;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieView);
        this.dataTextSize = typedArray.getDimension(R.styleable.PieView_dataTextSize, this.dataTextSize);
        this.circleWidth = typedArray.getDimension(R.styleable.PieView_circleWidth, this.circleWidth);
        this.dataTextColor = typedArray.getColor(R.styleable.PieView_dataTextColor, this.dataTextColor);
        typedArray.recycle();
        init();
    }

    private void init() {
        this.mArcPaint = new Paint();
        this.mArcPaint.setStrokeWidth(this.circleWidth);
        this.mArcPaint.setAntiAlias(true);
        this.mArcPaint.setStyle(Style.FILL);
        this.dataPaint = new Paint();
        this.dataPaint.setStrokeWidth(2.0f);
        this.dataPaint.setTextSize(this.dataTextSize);
        this.dataPaint.setAntiAlias(true);
        this.dataPaint.setColor(this.dataTextColor);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (measureWidthMode == Integer.MIN_VALUE && measureHeightMode == Integer.MIN_VALUE) {
            setMeasuredDimension(measureWidthSize, measureHeightSize);
        } else if (measureWidthMode == Integer.MIN_VALUE) {
            setMeasuredDimension(measureWidthSize, measureHeightSize);
        } else if (measureHeightMode == Integer.MIN_VALUE) {
            setMeasuredDimension(measureWidthSize, measureHeightSize);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.centerX = getMeasuredWidth() / 2;
        this.centerY = getMeasuredHeight() / 2;
        this.radius = (float) (Math.min(getMeasuredWidth(), getMeasuredHeight()) / 4);
        this.rectF = new RectF(((float) this.centerX) - this.radius, ((float) this.centerY) - this.radius, ((float) this.centerX) + this.radius, ((float) this.centerY) + this.radius);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateAndDraw(canvas);
    }

    private void calculateAndDraw(Canvas canvas) {
        if (this.numbers != null && this.numbers.length != 0) {
            int startAngle = 0;
            for (int i = 0; i < this.numbers.length; i++) {
                float angle;
                float percent = ((float) this.numbers[i]) / ((float) this.sum);
                if (i == this.numbers.length - 1) {
                    angle = (float) (360 - startAngle);
                } else {
                    angle = (float) Math.ceil((double) (360.0f * percent));
                }
                drawArc(canvas, (float) startAngle, angle, this.colors[i], i);
                startAngle = (int) (((float) startAngle) + angle);
                if (this.numbers[i] > 0) {
                    calculatePosition(((float) (startAngle + 90)) - (angle / 2.0f));
                    drawData(canvas, i, percent);
                }
            }
        }
    }

    private float[] calculatePosition(float degree) {
        this.radius = (float) (Math.min(getMeasuredWidth(), getMeasuredHeight()) / 4);
        float startX = ((float) this.centerX) + ((float) (Math.sin(Math.toRadians((double) degree)) * ((double) this.radius)));
        float startY = ((float) this.centerY) - ((float) (Math.cos(Math.toRadians((double) degree)) * ((double) this.radius)));
        return new float[]{startX, startY};
    }

    private void drawData(Canvas canvas, int i, float percent) {
        Path path;
        if (i == 2) {
            path = new Path();
            this.dataPaint.setColor(-1);
            this.dataPaint.setStyle(Style.STROKE);
            path.moveTo(this.rectF.centerX() + (this.radius * 0.6f), this.rectF.centerY() - (0.8f * this.radius));
            path.lineTo(this.rectF.centerX() + this.radius, this.rectF.centerY() - (this.radius * 1.2f));
            path.lineTo(this.rectF.centerX() + (this.radius * 2.0f), this.rectF.centerY() - (this.radius * 1.2f));
            canvas.drawPath(path, this.dataPaint);
            drawPieValue(canvas, this.names[i], this.rectF.centerX() + (this.radius * 1.5f), this.rectF.centerY() - (this.radius * 1.2f), percent);
        } else if (i == 1) {
            path = new Path();
            this.dataPaint.setColor(-1);
            this.dataPaint.setStyle(Style.STROKE);
            path.moveTo(this.rectF.centerX() - (this.radius * 0.6f), this.rectF.centerY() + (this.radius * 0.6f));
            path.lineTo(this.rectF.centerX() - this.radius, this.rectF.centerY() + this.radius);
            path.lineTo(this.rectF.centerX() - (this.radius * 2.0f), this.rectF.centerY() + this.radius);
            canvas.drawPath(path, this.dataPaint);
            drawPieValue(canvas, this.names[i], (-1.5f * this.radius) + this.rectF.centerX(), this.rectF.centerY() + this.radius, percent);
        } else if (i == 0) {
            path = new Path();
            this.dataPaint.setColor(-1);
            this.dataPaint.setStyle(Style.STROKE);
            path.moveTo((this.radius * 0.6f) + this.rectF.centerX(), this.rectF.centerY() + (this.radius * 0.6f));
            path.lineTo(this.radius + this.rectF.centerX(), this.rectF.centerY() + this.radius);
            path.lineTo((this.radius * 2.0f) + this.rectF.centerX(), this.rectF.centerY() + this.radius);
            canvas.drawPath(path, this.dataPaint);
            drawPieValue(canvas, this.names[i], (this.radius * 1.5f) + this.rectF.centerX(), this.rectF.centerY() + this.radius, percent);
        }
    }

    private void drawPieValue(Canvas canvas, String name, float startX, float startY, float percent) {
        this.dataPaint.getTextBounds(name, 0, name.length(), this.dataTextBound);
        canvas.drawText(name, startX - ((float) (this.dataTextBound.width() / 2)), (((float) (this.dataTextBound.height() / 2)) + startY) - 20.0f, this.dataPaint);
        String percentString = new DecimalFormat("0.0").format((double) (100.0f * percent)) + "%";
        this.dataPaint.getTextBounds(percentString, 0, percentString.length(), this.dataTextBound);
        canvas.drawText(percentString, startX - ((float) (this.dataTextBound.width() / 2)), (((float) (this.dataTextBound.height() / 2)) + startY) + 30.0f, this.dataPaint);
    }

    private void drawArc(Canvas canvas, float startAngle, float angle, int color, int index) {
        this.mArcPaint.setColor(color);
        this.radius = (float) ((index * 8) + (Math.min(getMeasuredWidth(), getMeasuredHeight()) / 4));
        this.rectF = new RectF(((float) this.centerX) - this.radius, ((float) this.centerY) - this.radius, ((float) this.centerX) + this.radius, ((float) this.centerY) + this.radius);
        canvas.drawArc(this.rectF, startAngle - 0.5f, angle + 0.5f, true, this.mArcPaint);
    }

    private int randomColor() {
        return Color.rgb(this.random.nextInt(256), this.random.nextInt(256), this.random.nextInt(256));
    }

    public void setData(int[] numbers, String[] names) {
        if (numbers != null && numbers.length != 0 && names != null && names.length != 0 && numbers.length == names.length) {
            this.numbers = numbers;
            this.names = names;
            this.colors = new int[numbers.length];
            for (int i = 0; i < this.numbers.length; i++) {
                this.sum += numbers[i];
                this.colors[i] = randomColor();
            }
            invalidate();
        }
    }
}
