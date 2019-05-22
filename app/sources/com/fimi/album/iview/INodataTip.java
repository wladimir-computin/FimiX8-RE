package com.fimi.album.iview;

import com.fimi.album.entity.MediaModel;

public interface INodataTip {
    void noDataTipCallback(boolean z);

    void notifyAddCallback(MediaModel mediaModel);
}
