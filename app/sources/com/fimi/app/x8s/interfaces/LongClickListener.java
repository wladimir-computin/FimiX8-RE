package com.fimi.app.x8s.interfaces;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class LongClickListener implements OnTouchListener {
    private Handler handler = new Handler();
    private boolean isClickDown = false;
    private Runnable longClickRunnable = new Runnable() {
        public void run() {
            if (LongClickListener.this.isClickDown) {
                LongClickListener.this.handler.postDelayed(this, 50);
                LongClickListener.this.longClickCallback(LongClickListener.this.viewId);
            }
        }
    };
    private int viewId;

    public abstract void longClickCallback(int i);

    public abstract void onFingerUp(int i);

    public boolean onTouch(View v, MotionEvent event) {
        this.viewId = v.getId();
        switch (event.getAction()) {
            case 0:
                this.isClickDown = true;
                this.handler.postDelayed(this.longClickRunnable, 500);
                break;
            case 1:
                this.isClickDown = false;
                this.handler.removeCallbacks(this.longClickRunnable);
                onFingerUp(this.viewId);
                break;
        }
        return false;
    }
}
