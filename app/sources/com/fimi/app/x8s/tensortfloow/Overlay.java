package com.fimi.app.x8s.tensortfloow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.fimi.kernel.FimiAppContext;

@SuppressLint({"WrongCall"})
public class Overlay extends View {
    private int MAX_HEIGHT;
    private int MAX_WIDTH;
    private final String TAG = Overlay.class.getSimpleName();
    private boolean enableCustomOverlay = false;
    private int endX = 0;
    private int endY = 0;
    private boolean isLost = true;
    private boolean isTracking = false;
    private OverlayListener listener = null;
    private int lostColor = SupportMenu.CATEGORY_MASK;
    private int previewH = FimiAppContext.UI_WIDTH;
    private int previewW = FimiAppContext.UI_HEIGHT;
    private int selectedColor = -15935891;
    private int startX = 0;
    private int startY = 0;
    private int viewH = 0;
    private int viewW = 0;
    private int x1 = 0;
    private int x2 = 0;
    private int y1 = 0;
    private int y2 = 0;

    public interface OverlayListener {
        void onDraw(Canvas canvas, Rect rect, boolean z);

        void onTouchActionDown();

        void onTouchActionUp(int i, int i2, int i3, int i4);
    }

    public Overlay(Context context) {
        super(context);
    }

    public Overlay(Context context, int w, int h) {
        super(context);
        this.viewW = w;
        this.viewH = h;
    }

    public Overlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOverlayListener(OverlayListener listener) {
        this.listener = listener;
    }

    public void setCustomOverlay(boolean flag) {
        this.enableCustomOverlay = flag;
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

    public void refreshTrackerRect(int x1, int y1, int x2, int y2) {
        this.startX = x1;
        this.startY = y1;
        this.endX = x2;
        this.endY = y2;
        postInvalidate();
    }

    public void cleanTrackerRect() {
        this.x1 = 0;
        this.startX = 0;
        this.y1 = 0;
        this.startY = 0;
        this.x2 = 0;
        this.endX = 0;
        this.y2 = 0;
        this.endY = 0;
        postInvalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            this.listener.onTouchActionDown();
            int x = (int) event.getX();
            this.endX = x;
            this.startX = x;
            x = (int) event.getY();
            this.endY = x;
            this.startY = x;
            invalidate();
        } else if (event.getAction() == 2) {
            this.endX = (int) event.getX();
            this.endY = (int) event.getY();
            invalidate();
        } else if (event.getAction() == 1 && this.listener != null) {
            this.listener.onTouchActionUp((this.x1 * this.previewW) / this.MAX_WIDTH, (this.y1 * this.previewH) / this.MAX_HEIGHT, ((this.x2 - this.x1) * this.previewW) / this.MAX_WIDTH, ((this.y2 - this.y1) * this.previewH) / this.MAX_HEIGHT);
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (!this.enableCustomOverlay) {
            Paint p = new Paint();
            if (!this.isTracking) {
                p.setColor(this.lostColor);
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
                } else {
                    this.y1 = this.endY;
                    this.y2 = this.startY;
                }
            } else if (this.isLost) {
                p.setColor(this.lostColor);
            } else {
                p.setColor(this.selectedColor);
            }
            canvas.drawRect((float) this.x1, (float) this.y1, (float) this.x2, (float) (this.y1 + 5), p);
            canvas.drawRect((float) this.x1, (float) this.y2, (float) (this.x2 + 5), (float) (this.y2 + 5), p);
            canvas.drawRect((float) this.x1, (float) this.y1, (float) (this.x1 + 5), (float) this.y2, p);
            canvas.drawRect((float) this.x2, (float) this.y1, (float) (this.x2 + 5), (float) this.y2, p);
        } else if (this.listener != null) {
            if (!this.isTracking) {
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
                } else {
                    this.y1 = this.endY;
                    this.y2 = this.startY;
                }
            }
            this.listener.onDraw(canvas, new Rect(this.x1, this.y1, this.x2, this.y2), this.isLost);
        }
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
