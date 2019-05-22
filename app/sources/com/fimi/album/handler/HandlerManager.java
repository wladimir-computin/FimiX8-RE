package com.fimi.album.handler;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;

public class HandlerManager {
    public static final String TAG = "otherThreadHandler";
    private static HandlerManager mHandlerManager;

    private HandlerManager() {
    }

    public static HandlerManager obtain() {
        if (mHandlerManager == null) {
            synchronized (HandlerManager.class) {
                if (mHandlerManager == null) {
                    mHandlerManager = new HandlerManager();
                }
            }
        }
        return mHandlerManager;
    }

    public Handler getHandlerInOtherThread(Callback callback) {
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        return new Handler(handlerThread.getLooper(), callback);
    }

    public Handler getHandlerInMainThread(Callback callback) {
        return new Handler(Looper.getMainLooper(), callback);
    }

    public Handler getHandlerInMainThread() {
        return new Handler(Looper.getMainLooper());
    }
}
