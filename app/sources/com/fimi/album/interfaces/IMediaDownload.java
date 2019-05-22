package com.fimi.album.interfaces;

import com.fimi.album.entity.MediaModel;

public interface IMediaDownload {
    void addData(MediaModel mediaModel);

    void startDownload();

    void stopDownload();
}
