package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import com.fimi.app.x8s.R;
import java.util.ArrayList;
import java.util.List;

public class X8CustomChartView extends View {
    private double MAX_VALUE = 100.0d;
    private double MIN_VALUE = 10.0d;
    final String TAG = "DDLog";
    private Bitmap backgroundBitmap;
    private Paint bitmapPaint;
    private double curValue = 10.0d;
    private List<Integer> dataList = new ArrayList();
    private GestureDetector gestureDetector;
    private boolean isEnable;
    private boolean isValueChanged = false;
    private int[] label = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private float lastData;
    private OnSeekChangedListener listener;
    OnGestureListener onGestureListener = new OnGestureListener() {
        public boolean onDown(MotionEvent e) {
            return true;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (X8CustomChartView.this.isEnable) {
                if (distanceX < ((float) (-X8CustomChartView.this.verticalMinDistance))) {
                    if (X8CustomChartView.this.curValue < X8CustomChartView.this.MAX_VALUE) {
                        X8CustomChartView.this.curValue = X8CustomChartView.this.curValue + 5.0d;
                        X8CustomChartView.this.refreshView(true);
                        X8CustomChartView.this.invalidate();
                    }
                } else if (distanceX > ((float) X8CustomChartView.this.verticalMinDistance)) {
                    if (X8CustomChartView.this.curValue > X8CustomChartView.this.MIN_VALUE) {
                        X8CustomChartView.this.curValue = X8CustomChartView.this.curValue - 5.0d;
                        X8CustomChartView.this.refreshView(true);
                        X8CustomChartView.this.invalidate();
                    }
                } else if (distanceY < ((float) (-X8CustomChartView.this.verticalMinDistance))) {
                    if (X8CustomChartView.this.curValue > X8CustomChartView.this.MIN_VALUE) {
                        X8CustomChartView.this.curValue = X8CustomChartView.this.curValue - 5.0d;
                        X8CustomChartView.this.refreshView(true);
                        X8CustomChartView.this.invalidate();
                    }
                } else if (distanceY > ((float) X8CustomChartView.this.verticalMinDistance) && X8CustomChartView.this.curValue < X8CustomChartView.this.MAX_VALUE) {
                    X8CustomChartView.this.curValue = X8CustomChartView.this.curValue + 5.0d;
                    X8CustomChartView.this.refreshView(true);
                    X8CustomChartView.this.invalidate();
                }
            }
            return true;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    };
    private Paint paintCurve;
    private int verticalMinDistance = 10;
    private int xPoint;
    private int xScale;
    private int yPoint;
    private int yScale;

    public interface OnSeekChangedListener {
        void onFingerUp(int i, double d);

        void onSeekChanged(int i, double d);
    }

    public X8CustomChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public X8CustomChartView(Context context) {
        super(context);
        initData(context);
    }

    private void initData(Context context) {
        refreshView(true);
        this.gestureDetector = new GestureDetector(context, this.onGestureListener);
        this.bitmapPaint = new Paint();
        this.paintCurve = new Paint();
        this.paintCurve.setStyle(Style.STROKE);
        this.paintCurve.setDither(true);
        this.paintCurve.setAntiAlias(true);
        this.paintCurve.setStrokeWidth(3.0f);
        this.paintCurve.setColor(context.getResources().getColor(R.color.x8_fc_all_setting_blue));
        this.paintCurve.setPathEffect(new CornerPathEffect(25.0f));
        this.backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x8_img_exp_setting);
    }

    public void setOnSeekChangeListener(OnSeekChangedListener listener) {
        this.listener = listener;
    }

    public void setCurValue(double curValue) {
        this.curValue = curValue;
    }

    public double getCurValue() {
        return this.curValue;
    }

    public void refreshView(boolean needResponse) {
        if (this.curValue > this.MAX_VALUE) {
            this.curValue = this.MAX_VALUE;
        }
        if (this.curValue < this.MIN_VALUE) {
            this.curValue = this.MIN_VALUE;
        }
        double value = 2.0d + ((this.curValue / (this.MAX_VALUE - this.MIN_VALUE)) * 3.0d);
        this.dataList.clear();
        for (int x : this.label) {
            this.dataList.add(Integer.valueOf((int) Math.pow((double) x, value)));
        }
        this.lastData = (float) ((Integer) this.dataList.get(this.dataList.size() - 1)).intValue();
        invalidate();
        if (needResponse && this.listener != null) {
            this.isValueChanged = true;
            this.listener.onSeekChanged(getId(), this.curValue);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.xPoint = getWidth() / 2;
        this.yPoint = getHeight() / 2;
        this.xScale = (getWidth() / 2) / (this.label.length - 1);
        this.yScale = (getHeight() / 2) / (this.label.length - 1);
        this.backgroundBitmap = Bitmap.createScaledBitmap(this.backgroundBitmap, getWidth(), getHeight(), true);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        drawCurveTop(canvas, this.paintCurve);
        drawCurveBottom(canvas, this.paintCurve);
        canvas.drawBitmap(this.backgroundBitmap, 0.0f, 0.0f, this.bitmapPaint);
    }

    private void drawCurveTop(Canvas canvas, Paint paint) {
        Path path = new Path();
        for (int i = 0; i <= this.label.length - 1; i++) {
            if (i == 0) {
                path.moveTo((float) this.xPoint, getTopY(((Integer) this.dataList.get(0)).intValue()));
            } else {
                path.lineTo((float) (this.xPoint + (this.xScale * i)), getTopY(((Integer) this.dataList.get(i)).intValue()));
            }
            if (i == this.label.length - 1) {
                path.lineTo((float) (this.xPoint + (this.xScale * i)), getTopY(((Integer) this.dataList.get(i)).intValue()));
            }
        }
        canvas.drawPath(path, paint);
    }

    private void drawCurveBottom(Canvas canvas, Paint paint) {
        Path path = new Path();
        for (int i = 0; i <= this.label.length - 1; i++) {
            if (i == 0) {
                path.moveTo((float) this.xPoint, getBottomY(((Integer) this.dataList.get(0)).intValue()));
            } else {
                path.lineTo((float) (this.xPoint - (this.xScale * i)), getBottomY(((Integer) this.dataList.get(i)).intValue()));
            }
            if (i == this.label.length - 1) {
                path.lineTo((float) (this.xPoint - (this.xScale * i)), getBottomY(((Integer) this.dataList.get(i)).intValue()));
            }
        }
        canvas.drawPath(path, paint);
    }

    private float getTopY(int num) {
        return ((float) this.yPoint) - (((float) this.yScale) * ((((float) num) / this.lastData) * ((float) (this.label.length - 1))));
    }

    private float getBottomY(int num) {
        return ((float) this.yPoint) + (((float) this.yScale) * ((((float) num) / this.lastData) * ((float) (this.label.length - 1))));
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        if (this.listener != null && this.isValueChanged && event.getAction() == 1) {
            this.listener.onFingerUp(getId(), this.curValue);
            this.isValueChanged = false;
        }
        return true;
    }

    public boolean isEnable() {
        return this.isEnable;
    }

    public void setEnable(boolean enable) {
        this.isEnable = enable;
    }
}
