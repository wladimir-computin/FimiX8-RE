package com.fimi.album.interfaces;

import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.IDateHandler;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public interface ICameraDeviceDispater<T extends MediaModel> {
    void forCameraFolder();

    CopyOnWriteArrayList<T> getCameraDataList();

    CopyOnWriteArrayList<T> getCameraDataNoHeadList();

    LinkedHashMap<String, CopyOnWriteArrayList<T>> getCameraDateHash();

    long getPhotoCount();

    long getVideoCount();

    boolean isLoadCompleteSuccess();

    void reDefaultList();

    void setLoadCompleteSuccess(boolean z);

    void setPhotoCount(long j);

    void setVideoCount(long j);

    void setmIDateHandler(IDateHandler iDateHandler);
}
