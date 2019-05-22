package com.fimi.album.interfaces;

public interface IMediaFileDownloadObserverable {
    void addObserver(IMediaFileDownloadObserver iMediaFileDownloadObserver);

    void notityObserver(int i, int i2);
}
