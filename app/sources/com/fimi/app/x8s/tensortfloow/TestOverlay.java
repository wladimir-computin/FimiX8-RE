package com.fimi.app.x8s.tensortfloow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"WrongCall"})
public class TestOverlay extends View {
    private final String TAG = TestOverlay.class.getSimpleName();
    private boolean enableCustomTestOverlay = false;
    private int endX = 0;
    private int endY = 0;
    private boolean isLost = true;
    private boolean isTracking = false;
    private TestOverlayListener listener = null;
    private int lostColor = SupportMenu.CATEGORY_MASK;
    private List<RectF> mRectF = new ArrayList();
    RectF rf = new RectF();
    private int selectedColor = -15935891;
    private int startX = 0;
    private int startY = 0;
    private int viewH = 0;
    private int viewW = 0;
    private int x1 = 0;
    private int x2 = 0;
    private int y1 = 0;
    private int y2 = 0;

    public interface TestOverlayListener {
        void onDraw(Canvas canvas, Rect rect, boolean z);

        void onTouchActionDown();

        void onTouchActionUp(int i, int i2, int i3, int i4);
    }

    public TestOverlay(Context context) {
        super(context);
    }

    public TestOverlay(Context context, int w, int h) {
        super(context);
        this.viewW = w;
        this.viewH = h;
    }

    public TestOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.rf != null) {
            Paint p = new Paint();
            p.setColor(this.lostColor);
            p.setStyle(Style.STROKE);
            canvas.drawRect(this.rf.left, this.rf.top, this.rf.right, this.rf.bottom, p);
        }
    }

    public void addRect(RectF r) {
        this.mRectF.add(r);
    }

    public void clear() {
        this.mRectF.clear();
    }

    public void onTracking(RectF r) {
        this.rf.left = r.left;
        this.rf.top = r.top;
        this.rf.right = r.right;
        this.rf.bottom = r.bottom;
    }
}
