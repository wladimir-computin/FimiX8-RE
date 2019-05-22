package com.fimi.kernel.utils;

import android.os.Handler;

public class TimerUtil {
    private int _interval;
    private Runnable _tickHandler;
    private Runnable delegate;
    private Handler handler = new Handler();
    public boolean starting;
    public boolean ticking;

    public int getInterval() {
        return this._interval;
    }

    public void setInterval(int delay) {
        this._interval = delay;
    }

    public boolean getIsTicking() {
        return this.ticking;
    }

    public TimerUtil(int interval) {
        this._interval = interval;
    }

    public TimerUtil(int interval, Runnable onTickHandler) {
        this._interval = interval;
        setOnTickHandler(onTickHandler);
    }

    public void start(int interval, Runnable onTickHandler) {
        this.starting = true;
        if (!this.ticking) {
            this._interval = interval;
            setOnTickHandler(onTickHandler);
            this.handler.postDelayed(this.delegate, (long) this._interval);
            this.ticking = true;
        }
    }

    public void restart() {
        this.handler.removeCallbacks(this.delegate);
        this.ticking = false;
        this.handler.postDelayed(this.delegate, (long) this._interval);
        this.ticking = true;
    }

    public void start() {
        if (!this.ticking) {
            this.handler.postDelayed(this.delegate, (long) this._interval);
            this.ticking = true;
        }
    }

    public void stop() {
        this.handler.removeCallbacks(this.delegate);
        this.ticking = false;
    }

    public void setOnTickHandler(Runnable onTickHandler) {
        if (onTickHandler != null) {
            this._tickHandler = onTickHandler;
            this.delegate = new Runnable() {
                public void run() {
                    if (TimerUtil.this._tickHandler != null) {
                        TimerUtil.this._tickHandler.run();
                        TimerUtil.this.handler.postDelayed(TimerUtil.this.delegate, (long) TimerUtil.this._interval);
                    }
                }
            };
        }
    }

    public void remove() {
        this.handler.removeCallbacks(this._tickHandler);
        this.handler.removeCallbacks(this.delegate);
    }
}
