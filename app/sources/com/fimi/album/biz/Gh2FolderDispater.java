package com.fimi.album.biz;

import android.os.Handler;
import android.os.Message;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.IDateHandler;
import com.fimi.album.iview.IHandlerCallback;
import com.fimi.kernel.utils.DateFormater;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Gh2FolderDispater<T extends MediaModel> implements IHandlerCallback {
    public static final int PHOTO = 2;
    public static final String TAG = Gh2FolderDispater.class.getName();
    public static final int VIDEO = 1;
    private LinkedHashMap<String, CopyOnWriteArrayList<T>> dataHash = new LinkedHashMap();
    private String defaultFormatPattern = "yyyy.MM.dd HH:mm:ss";
    public boolean isHadForEachFolder;
    private CopyOnWriteArrayList<T> localDataList = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<T> localDataNoHeadList = new CopyOnWriteArrayList();
    private IDateHandler mIDateHandler;
    private SuffixUtils mSuffixUtils = SuffixUtils.obtain();
    private Handler otherHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
    private long photoCount = 0;
    private long videoCount = 0;

    public void forEachFolder(String folderPath) {
        if (!this.isHadForEachFolder && !this.otherHandler.hasMessages(3)) {
            Message mMessage = new Message();
            mMessage.what = 3;
            mMessage.obj = folderPath;
            this.otherHandler.sendMessage(mMessage);
        }
    }

    public boolean handleMessage(Message message) {
        if (message.what == 3) {
            reallyHandlerFolderFile((String) message.obj);
        }
        return true;
    }

    public void reallyHandlerFolderFile(String path) {
        List<T> cacheList = new ArrayList();
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
                        } else {
                            mMediaModel.setVideo(false);
                            this.photoCount++;
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
            if (this.mIDateHandler != null) {
                this.mIDateHandler.loadDateComplete(false, true);
                this.isHadForEachFolder = true;
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
