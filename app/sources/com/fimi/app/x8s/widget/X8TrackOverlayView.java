package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.fimi.app.x8s.R;
import com.fimi.kernel.FimiAppContext;

@SuppressLint({"WrongCall"})
public class X8TrackOverlayView extends View {
    final int MARGING_TOP = 20;
    private int MAX_HEIGHT;
    final int MAX_PATH = 104;
    private int MAX_UNSIGNED_SHORT = 65535;
    private int MAX_WIDTH;
    private final String TAG = X8TrackOverlayView.class.getSimpleName();
    private float d;
    private boolean enableCustomOverlay = false;
    private float endX = 0.0f;
    private float endY = 0.0f;
    private String errorMsg = "";
    public boolean hasTouch = false;
    int initialValue = 0;
    private boolean isClean = true;
    private boolean isErrorSelect = false;
    private boolean isLost = false;
    private boolean isTracking = false;
    private int k = 0;
    private OverlayListener listener = null;
    private int lostColor;
    private int lostfillColor;
    private int lostfillErrorColor;
    private Rect mBounds = new Rect();
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            X8TrackOverlayView.this.cleanTrackerRect();
        }
    };
    private Paint p = new Paint();
    private int previewH = FimiAppContext.UI_WIDTH;
    private int previewH2 = 1;
    private int previewW = FimiAppContext.UI_HEIGHT;
    private int previewW2 = 1;
    private RectF r1 = new RectF();
    private RectF r2 = new RectF();
    public int selectedColor;
    private int selectedErrorColor;
    private float startX = 0.0f;
    private float startY = 0.0f;
    private int viewH = 0;
    private int viewW = 0;
    private float x1 = 0.0f;
    private float x2 = 0.0f;
    private float y1 = 0.0f;
    private float y2 = 0.0f;

    public interface OverlayListener {
        boolean isCanSelect();

        void onChangeGoLocation(float f, float f2, float f3, float f4, int i, int i2);

        void onDraw(Canvas canvas, RectF rectF, boolean z);

        void onTouchActionDown();

        void onTouchActionUp(float f, float f2, float f3, float f4, int i, int i2, int i3, int i4);
    }

    public X8TrackOverlayView(Context context) {
        super(context);
        initView();
    }

    public X8TrackOverlayView(Context context, int w, int h) {
        super(context);
        this.viewW = w;
        this.viewH = h;
        initView();
    }

    public X8TrackOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setOverlayListener(OverlayListener listener) {
        this.listener = listener;
    }

    public void setCustomOverlay(boolean flag) {
        this.enableCustomOverlay = flag;
    }

    private void initView() {
        this.selectedColor = getContext().getResources().getColor(R.color.x8_track_select);
        this.lostColor = getContext().getResources().getColor(R.color.x8_track_lost);
        this.lostfillColor = getContext().getResources().getColor(R.color.x8_track_fill_lost);
        this.selectedErrorColor = getContext().getResources().getColor(R.color.x8_track_select_error);
        this.lostfillErrorColor = getContext().getResources().getColor(R.color.x8_track_fill_lost_error);
    }

    public void setTracking(boolean flag) {
        this.isTracking = flag;
    }

    public boolean getTracking() {
        return this.isTracking;
    }

    public void setLost(boolean flag) {
        this.isLost = flag;
    }

    public void setSelectedColor(int color) {
        this.selectedColor = color;
    }

    public void setLostColor(int color) {
        this.lostColor = color;
    }

    public void refreshTrackerRect(float x1, float y1, float x2, float y2) {
        if (!this.hasTouch) {
            this.startX = x1;
            this.startY = y1;
            this.endX = x2;
            this.endY = y2;
            postInvalidate();
        }
    }

    public void cleanTrackerRect() {
        if (!this.hasTouch) {
            this.errorMsg = "";
            float f = (float) this.initialValue;
            this.x1 = f;
            this.startX = f;
            f = (float) this.initialValue;
            this.y1 = f;
            this.startY = f;
            f = (float) this.initialValue;
            this.x2 = f;
            this.endX = f;
            f = (float) this.initialValue;
            this.y2 = f;
            this.endY = f;
            setLostColor(this.selectedColor);
            postInvalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            this.hasTouch = true;
            setTracking(false);
            this.errorMsg = "";
            this.mHandler.removeMessages(0);
            this.lostColor = this.selectedColor;
            float f = (float) this.initialValue;
            this.x1 = f;
            this.startX = f;
            f = (float) this.initialValue;
            this.y1 = f;
            this.startY = f;
            f = (float) this.initialValue;
            this.x2 = f;
            this.endX = f;
            f = (float) this.initialValue;
            this.y2 = f;
            this.endY = f;
            if (this.listener != null) {
                this.listener.onTouchActionDown();
            }
            f = event.getX();
            this.endX = f;
            this.startX = f;
            f = event.getY();
            this.endY = f;
            this.startY = f;
            this.isErrorSelect = false;
            invalidate();
        } else if (event.getAction() == 2) {
            this.endX = event.getX();
            this.endY = event.getY();
            invalidate();
        } else if (event.getAction() == 1) {
            if (this.listener != null) {
                this.listener.onTouchActionUp(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1, (int) this.x1, (int) this.y1, (int) this.x2, (int) this.y2);
            }
            this.hasTouch = false;
        }
        return true;
    }

    public void onTracking(int x, int y, int w, int h) {
        if (!this.hasTouch && this.MAX_WIDTH != 0 && this.MAX_HEIGHT != 0) {
            setTracking(true);
            this.lostColor = this.selectedColor;
            this.x1 = (((float) (this.MAX_WIDTH * x)) * 1.0f) / ((float) this.MAX_UNSIGNED_SHORT);
            this.x2 = this.x1 + ((((float) (this.MAX_WIDTH * w)) * 1.0f) / ((float) this.MAX_UNSIGNED_SHORT));
            this.y1 = (((float) (this.MAX_HEIGHT * y)) * 1.0f) / ((float) this.MAX_UNSIGNED_SHORT);
            this.y2 = this.y1 + ((((float) (this.MAX_HEIGHT * h)) * 1.0f) / ((float) this.MAX_UNSIGNED_SHORT));
            postInvalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (!this.isTracking) {
            changeXY();
            drawRect(canvas);
            onDrawPath(canvas);
        } else if (this.isTracking) {
            drawRect(canvas);
            onDrawPath(canvas);
            drawText(canvas);
            if (this.listener != null) {
                this.listener.onChangeGoLocation(this.x1, this.x2, this.y1, this.y2, getWidth(), getHeight());
            }
        }
    }

    public void changeXY() {
        if (this.startX <= this.endX) {
            this.x1 = this.startX;
            this.x2 = this.endX;
        } else {
            this.x1 = this.endX;
            this.x2 = this.startX;
        }
        if (this.startY <= this.endY) {
            this.y1 = this.startY;
            this.y2 = this.endY;
            return;
        }
        this.y1 = this.endY;
        this.y2 = this.startY;
    }

    public void drawRect(Canvas canvas) {
        if (this.isErrorSelect) {
            this.p.setColor(this.lostfillErrorColor);
        } else {
            this.p.setColor(this.lostfillColor);
        }
        this.p.setStyle(Style.FILL);
        this.p.setStrokeWidth(0.0f);
        this.r1.left = this.x1;
        this.r1.right = this.x2;
        this.r1.top = this.y1;
        this.r1.bottom = this.y2;
        canvas.drawRect(this.r1, this.p);
        if (this.isErrorSelect) {
            this.p.setColor(this.selectedErrorColor);
        } else {
            this.p.setColor(this.lostColor);
        }
        this.p.setStyle(Style.STROKE);
        this.p.setStrokeWidth(3.0f);
        this.p.setTextSize(0.0f);
        this.r2.left = this.x1;
        this.r2.right = this.x2;
        this.r2.top = this.y1;
        this.r2.bottom = this.y2;
        canvas.drawRect(this.r2, this.p);
    }

    public void onDrawPath(Canvas canvas) {
        int i;
        float xd1;
        float xd2;
        float yd1;
        float yd2;
        this.d = (this.x2 - this.x1) / 4.0f;
        if (this.d >= 52.0f) {
            this.d = ((this.x2 - this.x1) - 104.0f) / 2.0f;
        }
        if (this.isErrorSelect) {
            this.p.setColor(this.selectedErrorColor);
        } else {
            this.p.setColor(this.lostColor);
        }
        this.p.setStyle(Style.FILL);
        this.p.setStrokeWidth(0.0f);
        if (this.y2 - this.y1 > 12.0f) {
            this.k = 6;
        } else {
            this.k = ((int) (this.y2 - this.y1)) / 2;
        }
        for (i = 0; i < this.k; i++) {
            xd1 = (this.x1 + this.d) + (((float) i) * 1.5f);
            xd2 = (this.x2 - this.d) - (((float) i) * 1.5f);
            if (xd1 < this.x2 && xd2 > this.x1) {
                Canvas canvas2 = canvas;
                canvas2.drawLine(xd1, ((float) i) + this.y1, xd2, ((float) i) + this.y1, this.p);
            }
        }
        for (i = 0; i < this.k; i++) {
            xd1 = (this.x1 + this.d) + (((float) i) * 1.5f);
            xd2 = (this.x2 - this.d) - (((float) i) * 1.5f);
            if (xd1 < this.x2 && xd2 > this.x1) {
                canvas.drawLine(xd1, this.y2 - ((float) i), xd2, this.y2 - ((float) i), this.p);
            }
        }
        this.d = (this.y2 - this.y1) / 4.0f;
        if (this.d >= 52.0f) {
            this.d = ((this.y2 - this.y1) - 104.0f) / 2.0f;
        }
        if (this.x2 - this.x1 > 12.0f) {
            this.k = 6;
        } else {
            this.k = ((int) (this.x2 - this.x1)) / 2;
        }
        for (i = 0; i < this.k; i++) {
            yd1 = (this.y1 + this.d) + (((float) i) * 1.5f);
            yd2 = (this.y2 - this.d) - (((float) i) * 1.5f);
            if (yd1 < this.y2 && yd2 > this.y1) {
                canvas.drawLine(this.x1 + ((float) i), yd1, this.x1 + ((float) i), yd2, this.p);
            }
        }
        for (i = 0; i < this.k; i++) {
            yd1 = (this.y1 + this.d) + (((float) i) * 1.5f);
            yd2 = (this.y2 - this.d) - (((float) i) * 1.5f);
            if (yd1 < this.y2 && yd2 > this.y1) {
                canvas.drawLine(this.x2 - ((float) i), yd1, this.x2 - ((float) i), yd2, this.p);
            }
        }
    }

    public void setSelectError(boolean b) {
        this.isErrorSelect = b;
    }

    public void drawText(Canvas canvas) {
        if (this.isErrorSelect && this.errorMsg.length() > 0) {
            this.p.setTextSize(21.0f);
            this.p.getTextBounds(this.errorMsg, 0, this.errorMsg.length(), this.mBounds);
            canvas.drawText(this.errorMsg, (this.x1 + ((this.x2 - this.x1) / 2.0f)) - (((float) this.mBounds.width()) / 2.0f), (this.y2 + 20.0f) + ((float) this.mBounds.height()), this.p);
        }
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        this.mHandler.removeMessages(0);
        this.mHandler.sendEmptyMessageDelayed(0, 1500);
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            this.MAX_WIDTH = w;
            this.MAX_HEIGHT = h;
        }
    }

    public int getMaxWidth() {
        return this.MAX_WIDTH;
    }

    public int getMaxHeight() {
        return this.MAX_HEIGHT;
    }
}
