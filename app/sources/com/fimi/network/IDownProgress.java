package com.fimi.network;

import com.fimi.network.DownFwService.DownState;

public interface IDownProgress {
    void onProgress(DownState downState, int i, String str);
}
