package com.fimi.album.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class HackyViewPager extends ViewPager {
    private static final String TAG = "HackyViewPager";
    private boolean scrollble = true;

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (this.scrollble) {
                return super.onInterceptTouchEvent(ev);
            }
            return false;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "hacky viewpager error1");
            return false;
        } catch (ArrayIndexOutOfBoundsException e2) {
            Log.e(TAG, "hacky viewpager error2");
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.scrollble) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
}
