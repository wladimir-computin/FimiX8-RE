package com.fimi.kernel.percent;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.fimi.kernel.percent.PercentLayoutHelper.PercentLayoutInfo;
import com.fimi.kernel.percent.PercentLayoutHelper.PercentLayoutParams;

public class PercentLinearLayout extends LinearLayout {
    private static final String TAG = "PercentLinearLayout";
    private PercentLayoutHelper mPercentLayoutHelper = new PercentLayoutHelper(this);

    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams implements PercentLayoutParams {
        private PercentLayoutInfo mPercentLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
        }

        public PercentLayoutInfo getPercentLayoutInfo() {
            return this.mPercentLayoutInfo;
        }

        /* Access modifiers changed, original: protected */
        public void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    public PercentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int tmpHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        int tmpWidthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getMode(widthMeasureSpec));
        Log.d(TAG, MeasureSpec.toString(heightMeasureSpec));
        if (heightMode == 0 && getParent() != null && (getParent() instanceof ScrollView)) {
            int baseHeight;
            Context context = getContext();
            if (context instanceof Activity) {
                int measuredHeight = ((Activity) context).findViewById(16908290).getMeasuredHeight();
                baseHeight = measuredHeight;
                Log.d(TAG, "measuredHeight = " + measuredHeight);
            } else {
                baseHeight = getScreenHeight();
                Log.d(TAG, "scHeight = " + baseHeight);
            }
            tmpHeightMeasureSpec = MeasureSpec.makeMeasureSpec(baseHeight, heightMode);
        }
        this.mPercentLayoutHelper.adjustChildren(tmpWidthMeasureSpec, tmpHeightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mPercentLayoutHelper.handleMeasuredStateTooSmall()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService("window");
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mPercentLayoutHelper.restoreOriginalParams();
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
}
