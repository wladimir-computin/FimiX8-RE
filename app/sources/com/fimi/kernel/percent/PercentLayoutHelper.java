package com.fimi.kernel.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.R;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PercentLayoutHelper {
    private static final String REGEX_PERCENT = "^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)%([wh]?)$";
    private static final String TAG = "PercentLayout";
    private final ViewGroup mHost;

    public interface PercentLayoutParams {
        PercentLayoutInfo getPercentLayoutInfo();
    }

    public static class PercentLayoutInfo {
        public PercentVal bottomMarginPercent;
        public PercentVal endMarginPercent;
        public PercentVal heightPercent;
        public PercentVal leftMarginPercent;
        final MarginLayoutParams mPreservedParams = new MarginLayoutParams(0, 0);
        public PercentVal maxHeightPercent;
        public PercentVal maxWidthPercent;
        public PercentVal minHeightPercent;
        public PercentVal minWidthPercent;
        public PercentVal paddingBottomPercent;
        public PercentVal paddingLeftPercent;
        public PercentVal paddingRightPercent;
        public PercentVal paddingTopPercent;
        public PercentVal rightMarginPercent;
        public PercentVal startMarginPercent;
        public PercentVal textSizePercent;
        public PercentVal topMarginPercent;
        public PercentVal widthPercent;

        public static class PercentVal {
            public boolean isBaseWidth;
            public float percent = -1.0f;

            public PercentVal(float percent, boolean isBaseWidth) {
                this.percent = percent;
                this.isBaseWidth = isBaseWidth;
            }

            public String toString() {
                return "PercentVal{percent=" + this.percent + ", isBaseWidth=" + this.isBaseWidth + CoreConstants.CURLY_RIGHT;
            }
        }

        public void fillLayoutParams(LayoutParams params, int widthHint, int heightHint) {
            this.mPreservedParams.width = params.width;
            this.mPreservedParams.height = params.height;
            if (this.widthPercent != null) {
                params.width = (int) (((float) (this.widthPercent.isBaseWidth ? widthHint : heightHint)) * this.widthPercent.percent);
            }
            if (this.heightPercent != null) {
                int base;
                if (this.heightPercent.isBaseWidth) {
                    base = widthHint;
                } else {
                    base = heightHint;
                }
                params.height = (int) (((float) base) * this.heightPercent.percent);
            }
        }

        public void fillMarginLayoutParams(MarginLayoutParams params, int widthHint, int heightHint) {
            int base;
            fillLayoutParams(params, widthHint, heightHint);
            this.mPreservedParams.leftMargin = params.leftMargin;
            this.mPreservedParams.topMargin = params.topMargin;
            this.mPreservedParams.rightMargin = params.rightMargin;
            this.mPreservedParams.bottomMargin = params.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(this.mPreservedParams, MarginLayoutParamsCompat.getMarginStart(params));
            MarginLayoutParamsCompat.setMarginEnd(this.mPreservedParams, MarginLayoutParamsCompat.getMarginEnd(params));
            if (this.leftMarginPercent != null) {
                params.leftMargin = (int) (((float) (this.leftMarginPercent.isBaseWidth ? widthHint : heightHint)) * this.leftMarginPercent.percent);
            }
            if (this.topMarginPercent != null) {
                if (this.topMarginPercent.isBaseWidth) {
                    base = widthHint;
                } else {
                    base = heightHint;
                }
                params.topMargin = (int) (((float) base) * this.topMarginPercent.percent);
            }
            if (this.rightMarginPercent != null) {
                if (this.rightMarginPercent.isBaseWidth) {
                    base = widthHint;
                } else {
                    base = heightHint;
                }
                params.rightMargin = (int) (((float) base) * this.rightMarginPercent.percent);
            }
            if (this.bottomMarginPercent != null) {
                if (this.bottomMarginPercent.isBaseWidth) {
                    base = widthHint;
                } else {
                    base = heightHint;
                }
                params.bottomMargin = (int) (((float) base) * this.bottomMarginPercent.percent);
            }
            if (this.startMarginPercent != null) {
                if (this.startMarginPercent.isBaseWidth) {
                    base = widthHint;
                } else {
                    base = heightHint;
                }
                MarginLayoutParamsCompat.setMarginStart(params, (int) (((float) base) * this.startMarginPercent.percent));
            }
            if (this.endMarginPercent != null) {
                if (this.endMarginPercent.isBaseWidth) {
                    base = widthHint;
                } else {
                    base = heightHint;
                }
                MarginLayoutParamsCompat.setMarginEnd(params, (int) (((float) base) * this.endMarginPercent.percent));
            }
            if (Log.isLoggable(PercentLayoutHelper.TAG, 3)) {
                Log.d(PercentLayoutHelper.TAG, "after fillMarginLayoutParams: (" + params.width + ", " + params.height + ")");
            }
        }

        public String toString() {
            return "PercentLayoutInfo{widthPercent=" + this.widthPercent + ", heightPercent=" + this.heightPercent + ", leftMarginPercent=" + this.leftMarginPercent + ", topMarginPercent=" + this.topMarginPercent + ", rightMarginPercent=" + this.rightMarginPercent + ", bottomMarginPercent=" + this.bottomMarginPercent + ", startMarginPercent=" + this.startMarginPercent + ", endMarginPercent=" + this.endMarginPercent + ", textSizePercent=" + this.textSizePercent + ", maxWidthPercent=" + this.maxWidthPercent + ", maxHeightPercent=" + this.maxHeightPercent + ", minWidthPercent=" + this.minWidthPercent + ", minHeightPercent=" + this.minHeightPercent + ", paddingLeftPercent=" + this.paddingLeftPercent + ", paddingRightPercent=" + this.paddingRightPercent + ", paddingTopPercent=" + this.paddingTopPercent + ", paddingBottomPercent=" + this.paddingBottomPercent + ", mPreservedParams=" + this.mPreservedParams + CoreConstants.CURLY_RIGHT;
        }

        public void restoreMarginLayoutParams(MarginLayoutParams params) {
            restoreLayoutParams(params);
            params.leftMargin = this.mPreservedParams.leftMargin;
            params.topMargin = this.mPreservedParams.topMargin;
            params.rightMargin = this.mPreservedParams.rightMargin;
            params.bottomMargin = this.mPreservedParams.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(params, MarginLayoutParamsCompat.getMarginStart(this.mPreservedParams));
            MarginLayoutParamsCompat.setMarginEnd(params, MarginLayoutParamsCompat.getMarginEnd(this.mPreservedParams));
        }

        public void restoreLayoutParams(LayoutParams params) {
            params.width = this.mPreservedParams.width;
            params.height = this.mPreservedParams.height;
        }
    }

    public PercentLayoutHelper(ViewGroup host) {
        this.mHost = host;
    }

    public static void fetchWidthAndHeight(LayoutParams params, TypedArray array, int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "adjustChildren: " + this.mHost + " widthMeasureSpec: " + MeasureSpec.toString(widthMeasureSpec) + " heightMeasureSpec: " + MeasureSpec.toString(heightMeasureSpec));
        }
        int widthHint = MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = MeasureSpec.getSize(heightMeasureSpec);
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "widthHint = " + widthHint + " , heightHint = " + heightHint);
        }
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "should adjust " + view + " " + params);
            }
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "using " + info);
                }
                if (info != null) {
                    supportTextSize(widthHint, heightHint, view, info);
                    supportPadding(widthHint, heightHint, view, info);
                    supportMinOrMaxDimesion(widthHint, heightHint, view, info);
                    if (params instanceof MarginLayoutParams) {
                        info.fillMarginLayoutParams((MarginLayoutParams) params, widthHint, heightHint);
                    } else {
                        info.fillLayoutParams(params, widthHint, heightHint);
                    }
                }
            }
        }
    }

    private void supportPadding(int widthHint, int heightHint, View view, PercentLayoutInfo info) {
        int base;
        int left = view.getPaddingLeft();
        int right = view.getPaddingRight();
        int top = view.getPaddingTop();
        int bottom = view.getPaddingBottom();
        PercentVal percentVal = info.paddingLeftPercent;
        if (percentVal != null) {
            left = (int) (((float) (percentVal.isBaseWidth ? widthHint : heightHint)) * percentVal.percent);
        }
        percentVal = info.paddingRightPercent;
        if (percentVal != null) {
            if (percentVal.isBaseWidth) {
                base = widthHint;
            } else {
                base = heightHint;
            }
            right = (int) (((float) base) * percentVal.percent);
        }
        percentVal = info.paddingTopPercent;
        if (percentVal != null) {
            if (percentVal.isBaseWidth) {
                base = widthHint;
            } else {
                base = heightHint;
            }
            top = (int) (((float) base) * percentVal.percent);
        }
        percentVal = info.paddingBottomPercent;
        if (percentVal != null) {
            if (percentVal.isBaseWidth) {
                base = widthHint;
            } else {
                base = heightHint;
            }
            bottom = (int) (((float) base) * percentVal.percent);
        }
        view.setPadding(left, top, right, bottom);
    }

    private void supportMinOrMaxDimesion(int widthHint, int heightHint, View view, PercentLayoutInfo info) {
        try {
            Class clazz = view.getClass();
            invokeMethod("setMaxWidth", widthHint, heightHint, view, clazz, info.maxWidthPercent);
            invokeMethod("setMaxHeight", widthHint, heightHint, view, clazz, info.maxHeightPercent);
            invokeMethod("setMinWidth", widthHint, heightHint, view, clazz, info.minWidthPercent);
            invokeMethod("setMinHeight", widthHint, heightHint, view, clazz, info.minHeightPercent);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    private void invokeMethod(String methodName, int widthHint, int heightHint, View view, Class clazz, PercentVal percentVal) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (percentVal != null) {
            int base;
            Method setMaxWidthMethod = clazz.getMethod(methodName, new Class[]{Integer.TYPE});
            setMaxWidthMethod.setAccessible(true);
            if (percentVal.isBaseWidth) {
                base = widthHint;
            } else {
                base = heightHint;
            }
            setMaxWidthMethod.invoke(view, new Object[]{Integer.valueOf((int) (((float) base) * percentVal.percent))});
        }
    }

    private void supportTextSize(int widthHint, int heightHint, View view, PercentLayoutInfo info) {
        PercentVal textSizePercent = info.textSizePercent;
        if (textSizePercent != null) {
            int base;
            if (textSizePercent.isBaseWidth) {
                base = widthHint;
            } else {
                base = heightHint;
            }
            float textSize = (float) ((int) (((float) base) * textSizePercent.percent));
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(0, textSize);
            }
        }
    }

    public static PercentLayoutInfo getPercentLayoutInfo(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PercentLayout_Layout);
        PercentLayoutInfo info = setPaddingRelatedVal(array, setMinMaxWidthHeightRelatedVal(array, setTextSizeSupportVal(array, setMarginRelatedVal(array, setWidthAndHeightVal(array, null)))));
        Log.d(TAG, "constructed: " + info);
        array.recycle();
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "constructed: " + info);
        }
        return info;
    }

    private static PercentLayoutInfo setWidthAndHeightVal(TypedArray array, PercentLayoutInfo info) {
        PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_widthPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent width: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.widthPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_heightPercent, false);
        if (percentVal == null) {
            return info;
        }
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "percent height: " + percentVal.percent);
        }
        info = checkForInfoExists(info);
        info.heightPercent = percentVal;
        return info;
    }

    private static PercentLayoutInfo setTextSizeSupportVal(TypedArray array, PercentLayoutInfo info) {
        PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_textSizePercent, false);
        if (percentVal == null) {
            return info;
        }
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "percent text size: " + percentVal.percent);
        }
        info = checkForInfoExists(info);
        info.textSizePercent = percentVal;
        return info;
    }

    private static PercentLayoutInfo setMinMaxWidthHeightRelatedVal(TypedArray array, PercentLayoutInfo info) {
        PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_maxWidthPercent, true);
        if (percentVal != null) {
            checkForInfoExists(info);
            info.maxWidthPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_maxHeightPercent, false);
        if (percentVal != null) {
            checkForInfoExists(info);
            info.maxHeightPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_minWidthPercent, true);
        if (percentVal != null) {
            checkForInfoExists(info);
            info.minWidthPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_minHeightPercent, false);
        if (percentVal != null) {
            checkForInfoExists(info);
            info.minHeightPercent = percentVal;
        }
        return info;
    }

    private static PercentLayoutInfo setMarginRelatedVal(TypedArray array, PercentLayoutInfo info) {
        PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.leftMarginPercent = percentVal;
            info.topMarginPercent = percentVal;
            info.rightMarginPercent = percentVal;
            info.bottomMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginLeftPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent left margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.leftMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginTopPercent, false);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent top margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.topMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginRightPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent right margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.rightMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginBottomPercent, false);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent bottom margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.bottomMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginStartPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent start margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.startMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginEndPercent, true);
        if (percentVal == null) {
            return info;
        }
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "percent end margin: " + percentVal.percent);
        }
        info = checkForInfoExists(info);
        info.endMarginPercent = percentVal;
        return info;
    }

    private static PercentLayoutInfo setPaddingRelatedVal(TypedArray array, PercentLayoutInfo info) {
        PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_paddingPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingLeftPercent = percentVal;
            info.paddingRightPercent = percentVal;
            info.paddingBottomPercent = percentVal;
            info.paddingTopPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_paddingLeftPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingLeftPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_paddingRightPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingRightPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_paddingTopPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingTopPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_paddingBottomPercent, true);
        if (percentVal == null) {
            return info;
        }
        info = checkForInfoExists(info);
        info.paddingBottomPercent = percentVal;
        return info;
    }

    private static PercentVal getPercentVal(TypedArray array, int index, boolean baseWidth) {
        return getPercentVal(array.getString(index), baseWidth);
    }

    @NonNull
    private static PercentLayoutInfo checkForInfoExists(PercentLayoutInfo info) {
        if (info != null) {
            return info;
        }
        return new PercentLayoutInfo();
    }

    private static PercentVal getPercentVal(String percentStr, boolean isOnWidth) {
        boolean isBasedWidth = true;
        if (percentStr == null) {
            return null;
        }
        Matcher matcher = Pattern.compile(REGEX_PERCENT).matcher(percentStr);
        if (matcher.matches()) {
            int len = percentStr.length();
            String floatVal = matcher.group(1);
            String lastAlpha = percentStr.substring(len - 1);
            float percent = Float.parseFloat(floatVal) / 100.0f;
            if ((!isOnWidth || lastAlpha.equals("h")) && !lastAlpha.equals("w")) {
                isBasedWidth = false;
            }
            return new PercentVal(percent, isBasedWidth);
        }
        throw new RuntimeException("the value of layout_xxxPercent invalid! ==>" + percentStr);
    }

    public void restoreOriginalParams() {
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "should restore " + view + " " + params);
            }
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "using " + info);
                }
                if (info != null) {
                    if (params instanceof MarginLayoutParams) {
                        info.restoreMarginLayoutParams((MarginLayoutParams) params);
                    } else {
                        info.restoreLayoutParams(params);
                    }
                }
            }
        }
    }

    public boolean handleMeasuredStateTooSmall() {
        boolean needsSecondMeasure = false;
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            LayoutParams params = view.getLayoutParams();
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (info != null) {
                    if (shouldHandleMeasuredWidthTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.width = -2;
                    }
                    if (shouldHandleMeasuredHeightTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.height = -2;
                    }
                }
            }
        }
        return needsSecondMeasure;
    }

    private static boolean shouldHandleMeasuredWidthTooSmall(View view, PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredWidthAndState(view) & ViewCompat.MEASURED_STATE_MASK;
        if (info == null || info.widthPercent == null || info.mPreservedParams == null || state != 16777216 || info.widthPercent.percent < 0.0f || info.mPreservedParams.width != -2) {
            return false;
        }
        return true;
    }

    private static boolean shouldHandleMeasuredHeightTooSmall(View view, PercentLayoutInfo info) {
        if (info == null || info.heightPercent == null || info.mPreservedParams == null || (ViewCompat.getMeasuredHeightAndState(view) & ViewCompat.MEASURED_STATE_MASK) != 16777216 || info.heightPercent.percent < 0.0f || info.mPreservedParams.height != -2) {
            return false;
        }
        return true;
    }
}
