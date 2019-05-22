package com.fimi.album.biz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.entity.MediaModel.recordType;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.IDateHandler;
import com.fimi.album.iview.IHandlerCallback;
import com.fimi.kernel.Constants;
import com.fimi.kernel.utils.DateFormater;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FolderDispater<T extends MediaModel> implements IHandlerCallback {
    public static final String ORIGINAL_PATH = "original_path";
    public static final String TAG = FolderDispater.class.getName();
    public static final String THUMBNAIL_PATH = "thumbnail_path";
    private LinkedHashMap<String, CopyOnWriteArrayList<T>> dataHash = new LinkedHashMap();
    private LinkedHashMap<String, CopyOnWriteArrayList<T>> dataHashPhoto = new LinkedHashMap();
    private LinkedHashMap<String, CopyOnWriteArrayList<T>> dataHashVideo = new LinkedHashMap();
    private String defaultFormatPattern = "yyyy.MM.dd HH:mm:ss";
    public boolean isHadForEachFolder;
    public boolean isHadForEachFolderPhoto;
    public boolean isHadForEachFolderVideo;
    private CopyOnWriteArrayList<T> localDataList = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<T> localDataNoHeadList = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<T> localPhotoDataList = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<T> localPhotoDataNoHeadList = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<T> localVideoDataList = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<T> localVideoDataNoHeadList = new CopyOnWriteArrayList();
    private IDateHandler mIDateHandler;
    private SuffixUtils mSuffixUtils = SuffixUtils.obtain();
    private Handler otherHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
    private long photoCount = 0;
    private long videoCount = 0;

    private enum State {
        ALL,
        PHOTO,
        VIDEO
    }

    public void forEachFolder(String folderPath) {
        if (!this.isHadForEachFolder && !this.otherHandler.hasMessages(3)) {
            Message mMessage = new Message();
            mMessage.what = 3;
            mMessage.obj = folderPath;
            this.otherHandler.sendMessage(mMessage);
        }
    }

    public void forEachFolderPhoto(String folderPath, String thumbnailPath) {
        if (!this.isHadForEachFolderPhoto && !this.otherHandler.hasMessages(3)) {
            Message mMessage = new Message();
            mMessage.what = 12;
            Bundle bundle = new Bundle();
            bundle.putString(ORIGINAL_PATH, folderPath);
            bundle.putString(THUMBNAIL_PATH, thumbnailPath);
            mMessage.setData(bundle);
            this.otherHandler.sendMessage(mMessage);
        }
    }

    public void forEachFolderVideo(String folderPath, String thumbnailPath) {
        if (!this.isHadForEachFolderVideo && !this.otherHandler.hasMessages(3)) {
            Message mMessage = new Message();
            mMessage.what = 13;
            Bundle bundle = new Bundle();
            bundle.putString(ORIGINAL_PATH, folderPath);
            bundle.putString(THUMBNAIL_PATH, thumbnailPath);
            mMessage.setData(bundle);
            this.otherHandler.sendMessage(mMessage);
        }
    }

    public boolean handleMessage(Message message) {
        Bundle bundle;
        if (message.what == 3) {
            this.photoCount = 0;
            this.videoCount = 0;
            reallyHandlerFolderFile((String) message.obj);
        } else if (message.what == 12) {
            this.photoCount = 0;
            bundle = message.getData();
            reallyHandlerFolderFile(bundle.getString(ORIGINAL_PATH), bundle.getString(THUMBNAIL_PATH), State.PHOTO);
        } else if (message.what == 13) {
            this.videoCount = 0;
            bundle = message.getData();
            reallyHandlerFolderFile(bundle.getString(ORIGINAL_PATH), bundle.getString(THUMBNAIL_PATH), State.VIDEO);
        }
        return true;
    }

    public void reallyHandlerFolderFile(String path) {
        this.localDataList.clear();
        List<T> cacheList = new ArrayList();
        List<T> cacheListPhoto = new ArrayList();
        List<T> cacheListVideo = new ArrayList();
        File floder = new File(path);
        if (floder.exists()) {
            String filePath;
            MediaModel mMediaModel;
            LinkedList<File> list = new LinkedList();
            File[] files = floder.listFiles();
            this.videoCount = 0;
            this.photoCount = 0;
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add(file);
                } else {
                    filePath = file.getAbsolutePath();
                    mMediaModel = new MediaModel();
                    mMediaModel.setFileSize(file.length());
                    mMediaModel.setCreateDate(file.lastModified());
                    mMediaModel.setFormatDate(DateFormater.dateString(file.lastModified(), this.defaultFormatPattern));
                    mMediaModel.setName(file.getName());
                    mMediaModel.setFileLocalPath(filePath);
                    if (filePath.contains(".")) {
                        if (this.mSuffixUtils.judgeFileType(filePath)) {
                            mMediaModel.setVideo(true);
                            this.videoCount++;
                            cacheListVideo.add(mMediaModel);
                            mMediaModel.setDownLoadOriginalFile(true);
                            mMediaModel.setThumLocalFilePath(filePath.replace("orgin", "thum").replace(this.mSuffixUtils.fileFormatThm, this.mSuffixUtils.fileFormatJpg));
                        } else {
                            mMediaModel.setVideo(false);
                            this.photoCount++;
                            cacheListPhoto.add(mMediaModel);
                        }
                        cacheList.add(mMediaModel);
                    }
                }
            }
            while (!list.isEmpty()) {
                for (File file2 : ((File) list.removeFirst()).listFiles()) {
                    if (file2.isDirectory()) {
                        list.add(file2);
                    } else {
                        filePath = file2.getAbsolutePath();
                        mMediaModel = new MediaModel();
                        mMediaModel.setFileSize(file2.length());
                        mMediaModel.setCreateDate(file2.lastModified());
                        mMediaModel.setFormatDate(DateFormater.dateString(file2.lastModified(), this.defaultFormatPattern));
                        mMediaModel.setName(file2.getName());
                        mMediaModel.setFileLocalPath(filePath);
                        if (this.mSuffixUtils.judgeFileType(filePath)) {
                            mMediaModel.setVideo(true);
                        } else {
                            mMediaModel.setVideo(false);
                        }
                        cacheList.add(mMediaModel);
                    }
                }
            }
            Collections.sort(cacheList, DateComparator.createDateComparator());
            this.localDataNoHeadList.addAll(cacheList);
            addHeadModelBean(cacheList, this.localDataList, this.dataHash);
            Collections.sort(cacheListPhoto, DateComparator.createDateComparator());
            this.localPhotoDataNoHeadList.addAll(cacheListPhoto);
            addHeadModelBean(cacheListPhoto, this.localPhotoDataList, this.dataHashPhoto);
            Collections.sort(cacheListVideo, DateComparator.createDateComparator());
            this.localVideoDataNoHeadList.addAll(cacheListVideo);
            addHeadModelBean(cacheListVideo, this.localVideoDataList, this.dataHashVideo);
            if (this.mIDateHandler != null) {
                this.mIDateHandler.loadDateComplete(false, true);
                this.isHadForEachFolder = true;
            }
        } else if (this.mIDateHandler != null) {
            this.mIDateHandler.loadDateComplete(false, false);
        }
    }

    public void reallyHandlerFolderFile(String path, String thumbnailPath, State state) {
        List<T> cacheList = new ArrayList();
        List<T> cacheListPhoto = new ArrayList();
        List<T> cacheListVideo = new ArrayList();
        File floder = new File(path);
        if (floder.exists()) {
            String filePath;
            MediaModel mMediaModel;
            String thumbnailVedeoPath;
            LinkedList<File> list = new LinkedList();
            for (File file : floder.listFiles()) {
                if (file.isDirectory()) {
                    list.add(file);
                } else {
                    filePath = file.getAbsolutePath();
                    mMediaModel = new MediaModel();
                    mMediaModel.setFileSize(file.length());
                    mMediaModel.setCreateDate(file.lastModified());
                    mMediaModel.setFormatDate(DateFormater.dateString(file.lastModified(), this.defaultFormatPattern));
                    mMediaModel.setName(file.getName());
                    mMediaModel.setFileLocalPath(filePath);
                    if (filePath.contains(".")) {
                        if (this.mSuffixUtils.judgeFileType(filePath)) {
                            mMediaModel.setVideo(true);
                            if (mMediaModel.getName().contains("delay_mode")) {
                                mMediaModel.setVideoType(recordType.delay_record);
                            } else if (mMediaModel.getName().contains("dynamic_mode")) {
                                mMediaModel.setVideoType(recordType.dynamic_delay_record);
                            }
                            thumbnailVedeoPath = thumbnailPath + file.getName().replace(Constants.VIDEO_FILE_SUFFIX, ".jpg");
                            if (new File(thumbnailVedeoPath.replace("//", "/").replace("////", "/")).exists()) {
                                mMediaModel.setThumLocalFilePath(thumbnailVedeoPath);
                            }
                            if (state != State.PHOTO) {
                                this.videoCount++;
                            }
                            cacheListVideo.add(mMediaModel);
                        } else {
                            mMediaModel.setVideo(false);
                            if (state != State.VIDEO) {
                                this.photoCount++;
                            }
                            cacheListPhoto.add(mMediaModel);
                        }
                        cacheList.add(mMediaModel);
                    }
                }
            }
            while (!list.isEmpty()) {
                for (File file2 : ((File) list.removeFirst()).listFiles()) {
                    if (file2.isDirectory()) {
                        list.add(file2);
                    } else {
                        filePath = file2.getAbsolutePath();
                        mMediaModel = new MediaModel();
                        mMediaModel.setFileSize(file2.length());
                        mMediaModel.setCreateDate(file2.lastModified());
                        mMediaModel.setFormatDate(DateFormater.dateString(file2.lastModified(), this.defaultFormatPattern));
                        mMediaModel.setName(file2.getName());
                        mMediaModel.setFileLocalPath(filePath);
                        if (this.mSuffixUtils.judgeFileType(filePath)) {
                            mMediaModel.setVideo(true);
                            thumbnailVedeoPath = thumbnailPath + file2.getName().replace(Constants.VIDEO_FILE_SUFFIX, ".jpg");
                            if (new File(thumbnailVedeoPath.replace("//", "/").replace("////", "/")).exists()) {
                                mMediaModel.setThumLocalFilePath(thumbnailVedeoPath);
                            }
                            this.videoCount++;
                            cacheListVideo.add(mMediaModel);
                        } else {
                            mMediaModel.setVideo(false);
                            this.photoCount++;
                            cacheListPhoto.add(mMediaModel);
                        }
                        cacheList.add(mMediaModel);
                    }
                }
            }
            if (state == State.ALL) {
                Collections.sort(cacheList, DateComparator.createDateComparator());
                this.localDataNoHeadList.addAll(cacheList);
                addHeadModelBean(cacheList, this.localDataList, this.dataHash);
            } else if (state == State.PHOTO) {
                Collections.sort(cacheListPhoto, DateComparator.createDateComparator());
                this.localPhotoDataNoHeadList.addAll(cacheListPhoto);
                addHeadModelBean(cacheListPhoto, this.localPhotoDataList, this.dataHashPhoto);
            } else {
                Collections.sort(cacheListVideo, DateComparator.createDateComparator());
                this.localVideoDataNoHeadList.addAll(cacheListVideo);
                addHeadModelBean(cacheListVideo, this.localVideoDataList, this.dataHashVideo);
            }
            if (this.mIDateHandler != null && state == State.ALL) {
                this.mIDateHandler.loadDateComplete(false, true);
                this.isHadForEachFolder = true;
            }
            if (this.mIDateHandler != null && state == State.PHOTO) {
                this.mIDateHandler.loadDateComplete(true, true);
                this.isHadForEachFolderPhoto = true;
            }
            if (this.mIDateHandler != null && state == State.VIDEO) {
                this.mIDateHandler.loadDateComplete(false, true);
                this.isHadForEachFolderVideo = true;
            }
        } else if (this.mIDateHandler != null) {
            this.mIDateHandler.loadDateComplete(false, false);
        }
    }

    private void addHeadModelBean(List<T> cacheList, CopyOnWriteArrayList<T> saveList, HashMap<String, CopyOnWriteArrayList<T>> saveHash) {
        String cacheFormateDate = null;
        CopyOnWriteArrayList<T> tempList = null;
        boolean isOneTime = false;
        if (cacheList.size() > 0) {
            MediaModel headViewModel = new MediaModel();
            headViewModel.setHeadView(true);
            saveList.add(headViewModel);
        }
        for (T mediaModel : cacheList) {
            String lastModifyDate = mediaModel.getFormatDate().split(" ")[0];
            if (cacheFormateDate == null || !lastModifyDate.equals(cacheFormateDate)) {
                if (tempList != null) {
                    saveHash.put(cacheFormateDate, tempList);
                    saveList.addAll(tempList);
                    isOneTime = false;
                }
                tempList = new CopyOnWriteArrayList();
                MediaModel headModel = new MediaModel();
                headModel.setFormatDate(lastModifyDate);
                headModel.setCategory(true);
                tempList.add(headModel);
                cacheFormateDate = lastModifyDate;
                if (!isOneTime) {
                    isOneTime = true;
                }
            }
            tempList.add(mediaModel);
        }
        if (isOneTime && tempList != null) {
            saveHash.put(cacheFormateDate, tempList);
            saveList.addAll(tempList);
        }
    }

    public void reDefaultList() {
        this.videoCount = 0;
        this.photoCount = 0;
        this.localDataList.clear();
        this.localDataNoHeadList.clear();
        this.dataHash.clear();
        this.isHadForEachFolder = false;
    }

    public void rePhotoDefaultList() {
        this.photoCount = 0;
        this.localPhotoDataList.clear();
        this.localPhotoDataNoHeadList.clear();
        this.dataHashPhoto.clear();
        this.isHadForEachFolderPhoto = false;
    }

    public void reVideoDefaultList() {
        this.videoCount = 0;
        this.localVideoDataList.clear();
        this.localVideoDataNoHeadList.clear();
        this.dataHashVideo.clear();
        this.isHadForEachFolderVideo = false;
    }

    public boolean isHadForEachFolder() {
        return this.isHadForEachFolder;
    }

    public void setHadForEachFolder(boolean hadForEachFolder) {
        this.isHadForEachFolder = hadForEachFolder;
    }

    public CopyOnWriteArrayList<T> getLocalDataList() {
        return this.localDataList;
    }

    public CopyOnWriteArrayList<T> getLocalDataNoHeadList() {
        return this.localDataNoHeadList;
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getDataHash() {
        return this.dataHash;
    }

    public CopyOnWriteArrayList<T> getLocalPhotoDataList() {
        return this.localPhotoDataList;
    }

    public CopyOnWriteArrayList<T> getLocalVideoDataList() {
        return this.localVideoDataList;
    }

    public CopyOnWriteArrayList<T> getLocalPhotoDataNoHeadList() {
        return this.localPhotoDataNoHeadList;
    }

    public CopyOnWriteArrayList<T> getLocalVideoDataNoHeadList() {
        return this.localVideoDataNoHeadList;
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getDataHashPhoto() {
        return this.dataHashPhoto;
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getDataHashVideo() {
        return this.dataHashVideo;
    }

    public void setmIDateHandler(IDateHandler mIDateHandler) {
        this.mIDateHandler = mIDateHandler;
    }

    public long getVideoCount() {
        return this.videoCount;
    }

    public void setVideoCount(long videoCount) {
        this.videoCount = videoCount;
    }

    public long getPhotoCount() {
        return this.photoCount;
    }

    public void setPhotoCount(long photoCount) {
        this.photoCount = photoCount;
    }

    public void removeCallBack() {
    }
}
