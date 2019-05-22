package com.fimi.player;

import com.fimi.player.IMediaPlayer.MediaQualityListener;
import com.fimi.player.IMediaPlayer.OnBufferingUpdateListener;
import com.fimi.player.IMediaPlayer.OnCompletionListener;
import com.fimi.player.IMediaPlayer.OnErrorListener;
import com.fimi.player.IMediaPlayer.OnInfoListener;
import com.fimi.player.IMediaPlayer.OnLiveVideoListener;
import com.fimi.player.IMediaPlayer.OnPreparedListener;
import com.fimi.player.IMediaPlayer.OnSeekCompleteListener;
import com.fimi.player.IMediaPlayer.OnVideoSizeChangedListener;

public abstract class AbstractMediaPlayer implements IMediaPlayer {
    private MediaQualityListener mMediaQualityListener;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnLiveVideoListener mOnLiveVideoListener;
    private OnPreparedListener mOnPreparedListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    public void setMediaQualityListener(MediaQualityListener listener) {
        this.mMediaQualityListener = listener;
    }

    public final void setOnPreparedListener(OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public final void setOnCompletionListener(OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public final void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        this.mOnBufferingUpdateListener = listener;
    }

    public final void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        this.mOnSeekCompleteListener = listener;
    }

    public final void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    public void showMediaQuality(int arg1, int arg2) {
        if (this.mMediaQualityListener != null) {
            this.mMediaQualityListener.showMediaQuality(arg1, arg2);
        }
    }

    public final void setmMediaQualityListener(MediaQualityListener listener) {
        this.mMediaQualityListener = listener;
    }

    public final void setOnErrorListener(OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public final void setOnInfoListener(OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    public void resetListeners() {
        this.mOnPreparedListener = null;
        this.mOnBufferingUpdateListener = null;
        this.mOnCompletionListener = null;
        this.mOnSeekCompleteListener = null;
        this.mOnVideoSizeChangedListener = null;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
        this.mOnLiveVideoListener = null;
    }

    /* Access modifiers changed, original: protected|final */
    public final void notifyOnPrepared() {
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onPrepared(this);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void notifyOnCompletion(int code) {
        if (this.mOnCompletionListener != null) {
            this.mOnCompletionListener.onCompletion(this, code);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void notifyOnBufferingUpdate(int percent) {
        if (this.mOnBufferingUpdateListener != null) {
            this.mOnBufferingUpdateListener.onBufferingUpdate(this, percent);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void notifyOnSeekComplete() {
        if (this.mOnSeekCompleteListener != null) {
            this.mOnSeekCompleteListener.onSeekComplete(this);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void notifyOnVideoSizeChanged(int width, int height, int sarNum, int sarDen) {
        if (this.mOnVideoSizeChangedListener != null) {
            this.mOnVideoSizeChangedListener.onVideoSizeChanged(this, width, height, sarNum, sarDen);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean notifyOnError(int what, int extra) {
        if (this.mOnErrorListener != null) {
            return this.mOnErrorListener.onError(this, what, extra);
        }
        return false;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean notifyOnInfo(int what, int extra) {
        if (this.mOnInfoListener != null) {
            return this.mOnInfoListener.onInfo(this, what, extra);
        }
        return false;
    }

    public void setOnLiveVideoListener(OnLiveVideoListener listener) {
        this.mOnLiveVideoListener = listener;
    }

    public void notifyRtmpStatusCBOnLiveVideoListener(int type, int status1, int status2) {
        if (this.mOnLiveVideoListener != null) {
            this.mOnLiveVideoListener.onRtmpStatusCB(type, status1, status2);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void notifyOnStartStream() {
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onStartStream();
        }
    }
}
