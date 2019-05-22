package com.fimi.album.biz;

import com.fimi.album.entity.MediaModel;
import com.fimi.album.interfaces.ICameraDeviceDispater;
import com.fimi.album.iview.IDateHandler;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataManager<T extends MediaModel> {
    private static DataManager mDataManager;
    private ICameraDeviceDispater mCameraDispater;
    private FolderDispater mFolderDispater = new FolderDispater();

    private DataManager() {
    }

    public void setCameraDeviceDispater(ICameraDeviceDispater mCameraDispater) {
        this.mCameraDispater = mCameraDispater;
    }

    public static DataManager obtain() {
        if (mDataManager == null) {
            synchronized (DataManager.class) {
                if (mDataManager == null) {
                    mDataManager = new DataManager();
                }
            }
        }
        return mDataManager;
    }

    public void forEachFolder(String folderPath) {
        this.mFolderDispater.forEachFolder(folderPath);
    }

    public void forEachFolderPhoto(String folderPath, String thumbnailPath) {
        this.mFolderDispater.forEachFolderPhoto(folderPath, thumbnailPath);
    }

    public void forEachFolderVideo(String folderPath, String thumbnailPath) {
        this.mFolderDispater.forEachFolderVideo(folderPath, thumbnailPath);
    }

    public void setIdataImpl(IDateHandler idataImpl) {
        this.mFolderDispater.setmIDateHandler(idataImpl);
        this.mCameraDispater.setmIDateHandler(idataImpl);
    }

    public void setLocalIdataImpl(IDateHandler idataImpl) {
        this.mFolderDispater.setmIDateHandler(idataImpl);
    }

    public CopyOnWriteArrayList<T> getLocalDataList() {
        return this.mFolderDispater.getLocalDataList();
    }

    public CopyOnWriteArrayList<T> getLocalDataNoHeadList() {
        return this.mFolderDispater.getLocalDataNoHeadList();
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getDataHashPhoto() {
        return this.mFolderDispater.getDataHashPhoto();
    }

    public CopyOnWriteArrayList<T> getLocalPhotoDataList() {
        return this.mFolderDispater.getLocalPhotoDataList();
    }

    public CopyOnWriteArrayList<T> getLocalPhotoDataNoHeadList() {
        return this.mFolderDispater.getLocalPhotoDataNoHeadList();
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getDataHashVideo() {
        return this.mFolderDispater.getDataHashVideo();
    }

    public CopyOnWriteArrayList<T> getLocalVideoDataList() {
        return this.mFolderDispater.getLocalVideoDataList();
    }

    public CopyOnWriteArrayList<T> getLocalVideoDataNoHeadList() {
        return this.mFolderDispater.getLocalVideoDataNoHeadList();
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getDataHash() {
        return this.mFolderDispater.getDataHash();
    }

    public void forX9CameraFolder() {
        this.mCameraDispater.forCameraFolder();
    }

    public CopyOnWriteArrayList<T> getX9CameraDataList() {
        return this.mCameraDispater.getCameraDataList();
    }

    public CopyOnWriteArrayList<T> getX9CameraDataNoHeadList() {
        return this.mCameraDispater.getCameraDataNoHeadList();
    }

    public boolean isHadLoadDate() {
        return this.mFolderDispater.isHadForEachFolder();
    }

    public void reLocalDefaultVaribale() {
        this.mFolderDispater.reDefaultList();
    }

    public void reLocalPhotoDefaultVaribale() {
        this.mFolderDispater.rePhotoDefaultList();
    }

    public void reLocalVideoDefaultVaribale() {
        this.mFolderDispater.reVideoDefaultList();
    }

    public void reX9CameraDefaultVaribale() {
        this.mCameraDispater.reDefaultList();
    }

    public void removeCallBack() {
        this.mFolderDispater.removeCallBack();
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getX9CameraDataHash() {
        return this.mCameraDispater.getCameraDateHash();
    }

    public void setLocalVideoCount(long count) {
        this.mFolderDispater.setVideoCount(count);
    }

    public void setLocalPhotoCount(long count) {
        this.mFolderDispater.setPhotoCount(count);
    }

    public long getLocalVideoCount() {
        return this.mFolderDispater.getVideoCount();
    }

    public long getLocalPhotoCount() {
        return this.mFolderDispater.getPhotoCount();
    }

    public void setX9CameraVideoCount(long count) {
        this.mCameraDispater.setVideoCount(count);
    }

    public void setX9CameraPhotoCount(long count) {
        this.mCameraDispater.setPhotoCount(count);
    }

    public long getX9CameraVideoCount() {
        return this.mCameraDispater.getVideoCount();
    }

    public long getX9CameraPhotoCount() {
        return this.mCameraDispater.getPhotoCount();
    }

    public long getX8CameraVideoCount() {
        return this.mCameraDispater.getVideoCount();
    }

    public long getX8CameraPhotoCount() {
        return this.mCameraDispater.getPhotoCount();
    }

    public boolean isX9CameraLoad() {
        return this.mCameraDispater.isLoadCompleteSuccess();
    }

    public boolean isX9LocalLoad() {
        return this.mFolderDispater.isHadForEachFolder();
    }

    public void setLocalLoad(boolean isload) {
        this.mFolderDispater.setHadForEachFolder(isload);
    }

    public void setX9CameralLoad(boolean isload) {
        this.mCameraDispater.setLoadCompleteSuccess(isload);
    }
}
