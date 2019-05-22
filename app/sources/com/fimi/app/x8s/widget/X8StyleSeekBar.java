package com.fimi.app.x8s.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class X8StyleSeekBar extends AppCompatSeekBar {
    public X8StyleSeekBar(Context context) {
        super(context);
    }

    public X8StyleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public X8StyleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
