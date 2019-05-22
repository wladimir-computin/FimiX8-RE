package com.fimi.album.download.interfaces;

import com.fimi.album.entity.MediaModel;

public interface OnDownloadListener {
    void onFailure(Object obj);

    void onProgress(Object obj, long j, long j2);

    void onStop(MediaModel mediaModel);

    void onSuccess(Object obj);
}
