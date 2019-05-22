package com.fimi.album.presenter;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.IVideoPlayer;

public class VideoPlayerPresneter implements Callback {
    private static final int TIME = 1;
    private IVideoPlayer mIVideoPlayer;
    private Handler mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);

    public VideoPlayerPresneter(IVideoPlayer mIVideoPlayer) {
        this.mIVideoPlayer = mIVideoPlayer;
    }

    public void startPlay() {
        this.mainHandler.sendEmptyMessage(1);
    }

    public void removeCallBack() {
        this.mainHandler.removeCallbacksAndMessages(null);
    }

    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            if (this.mIVideoPlayer != null) {
                this.mIVideoPlayer.timeFunction();
            }
            this.mainHandler.sendEmptyMessageDelayed(1, 1000);
        }
        return true;
    }
}
