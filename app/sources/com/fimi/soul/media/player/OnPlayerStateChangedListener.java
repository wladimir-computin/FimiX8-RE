package com.fimi.soul.media.player;

import com.fimi.soul.media.player.IFermiMediaPlayer.FermiPlyaerState;

public interface OnPlayerStateChangedListener {
    void OnPlayerStateChanged(FermiPlyaerState fermiPlyaerState, IFermiMediaPlayer iFermiMediaPlayer);
}
