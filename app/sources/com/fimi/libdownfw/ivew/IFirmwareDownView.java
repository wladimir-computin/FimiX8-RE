package com.fimi.libdownfw.ivew;

import com.fimi.network.DownFwService.DownState;

public interface IFirmwareDownView {
    void showDownFwProgress(DownState downState, int i, String str);
}
