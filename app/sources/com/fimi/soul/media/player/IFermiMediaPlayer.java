package com.fimi.soul.media.player;

import android.view.SurfaceView;
import android.widget.SeekBar;

public interface IFermiMediaPlayer {

    public enum FermiPlyaerState {
        Playing,
        Pause,
        Stop,
        Inited,
        Unknow
    }

    void addOnPlayerStateChangedListener(OnPlayerStateChangedListener onPlayerStateChangedListener);

    long getCurrentPosition();

    long getDuration();

    int getPosition();

    boolean isAutoPlay();

    boolean isPlaying();

    void pause();

    void play();

    void prepare();

    void seekTo(long j) throws IllegalStateException;

    void setAutoPlay(boolean z);

    void setMediaUri(String str);

    void setMediaUri(String str, String str2, String str3);

    void setOnMediaSizeChangedListener(OnMediaSizeChangedListener onMediaSizeChangedListener);

    void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener);

    void setPlayPosition(int i);

    void setSeekBar(SeekBar seekBar);

    void setSurfaceView(SurfaceView surfaceView);

    void stop();
}
