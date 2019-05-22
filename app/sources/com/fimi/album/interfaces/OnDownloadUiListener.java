package com.fimi.album.interfaces;

import com.fimi.album.entity.MediaModel;

public interface OnDownloadUiListener {
    void onFailure(MediaModel mediaModel);

    void onProgress(MediaModel mediaModel, int i);

    void onStop(MediaModel mediaModel);

    void onSuccess(MediaModel mediaModel);
}
