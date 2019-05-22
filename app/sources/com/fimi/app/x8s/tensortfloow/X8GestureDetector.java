package com.fimi.app.x8s.tensortfloow;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import com.fimi.app.x8s.interfaces.IX8GestureListener;

public class X8GestureDetector {
    protected float FLIP_DISTANCE = 50.0f;
    boolean isOnFling = false;
    GestureDetector mDetector;
    private IX8GestureListener x8GestureListener;

    public X8GestureDetector(Context activity) {
        this.mDetector = new GestureDetector(activity, new OnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            public void onShowPress(MotionEvent e) {
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            public void onLongPress(MotionEvent e) {
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null && e2 == null) {
                    return false;
                }
                if (e1.getX() - e2.getX() > X8GestureDetector.this.FLIP_DISTANCE) {
                    if (X8GestureDetector.this.x8GestureListener != null) {
                        X8GestureDetector.this.x8GestureListener.onFlingResult(1);
                    }
                    X8GestureDetector.this.isOnFling = true;
                    return true;
                } else if (e2.getX() - e1.getX() > X8GestureDetector.this.FLIP_DISTANCE) {
                    if (X8GestureDetector.this.x8GestureListener != null) {
                        X8GestureDetector.this.x8GestureListener.onFlingResult(3);
                    }
                    X8GestureDetector.this.isOnFling = true;
                    return true;
                } else if (e1.getY() - e2.getY() > X8GestureDetector.this.FLIP_DISTANCE) {
                    if (X8GestureDetector.this.x8GestureListener != null) {
                        X8GestureDetector.this.x8GestureListener.onFlingResult(2);
                    }
                    X8GestureDetector.this.isOnFling = true;
                    return true;
                } else if (e2.getY() - e1.getY() <= X8GestureDetector.this.FLIP_DISTANCE) {
                    return false;
                } else {
                    if (X8GestureDetector.this.x8GestureListener != null) {
                        X8GestureDetector.this.x8GestureListener.onFlingResult(4);
                    }
                    X8GestureDetector.this.isOnFling = true;
                    return true;
                }
            }

            public boolean onDown(MotionEvent e) {
                return false;
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean b = this.mDetector.onTouchEvent(event);
        if (event.getAction() == 1) {
            if (!this.isOnFling) {
                this.x8GestureListener.onInterestMetering(event.getX(), event.getY());
            }
            this.isOnFling = false;
        } else if (event.getAction() == 0) {
        }
        return b;
    }

    public void setX8GestureListener(IX8GestureListener x8GestureListener) {
        this.x8GestureListener = x8GestureListener;
    }

    public void setDistance(int distance) {
        this.FLIP_DISTANCE = (float) distance;
    }
}
