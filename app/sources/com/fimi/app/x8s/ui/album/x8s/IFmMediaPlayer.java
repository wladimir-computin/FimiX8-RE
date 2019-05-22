package com.fimi.app.x8s.ui.album.x8s;

public interface IFmMediaPlayer {
    long getCurrentPosition();

    long getDuration();

    boolean isPlaying();

    void onDestroy();

    void pause();

    void seekTo(int i);

    void start();
}
