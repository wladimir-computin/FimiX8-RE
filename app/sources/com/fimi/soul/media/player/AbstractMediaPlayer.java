package com.fimi.soul.media.player;

import com.fimi.soul.media.player.IMediaPlayer.OnBufferingUpdateListener;
import com.fimi.soul.media.player.IMediaPlayer.OnCompletionListener;
import com.fimi.soul.media.player.IMediaPlayer.OnErrorListener;
import com.fimi.soul.media.player.IMediaPlayer.OnInfoListener;
import com.fimi.soul.media.player.IMediaPlayer.OnPreparedListener;
import com.fimi.soul.media.player.IMediaPlayer.OnSeekCompleteListener;
import com.fimi.soul.media.player.IMediaPlayer.OnVideoSizeChangedListener;

public abstract class AbstractMediaPlayer implements IMediaPlayer {
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPreparedListener mOnPreparedListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangedListener;

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
    }

    /* Access modifiers changed, original: protected|final */
    public final void notifyOnPrepared() {
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onPrepared(this);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void notifyOnCompletion() {
        if (this.mOnCompletionListener != null) {
            this.mOnCompletionListener.onCompletion(this);
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
}
