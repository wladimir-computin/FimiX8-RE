package com.fimi.album.entity;

import ch.qos.logback.core.CoreConstants;
import java.io.Serializable;
import java.util.concurrent.Future;

public class MediaModel implements Serializable, Cloneable {
    private long createDate;
    private String downLoadOriginalPath;
    private String downLoadSmallPath;
    private String downloadName;
    private boolean downloading;
    private String fileLocalPath;
    private long fileSize;
    private String fileUrl;
    private String formatDate;
    private boolean isCategory;
    private boolean isDownLoadOriginalFile;
    private boolean isDownLoadSmallFile;
    private boolean isDownLoadThum;
    private boolean isDownloadFail;
    private boolean isDownloadFinish;
    private boolean isHeadView = false;
    private boolean isLoadThulm;
    private boolean isSelect;
    private volatile boolean isStop = false;
    private boolean isThumDownloadFinish;
    private boolean isThumStop;
    private boolean isVideo;
    private int itemPosition;
    private String localFileDir;
    private String localThumFileDir;
    private String md5;
    private String name;
    private int progress;
    Future<?> taskFutrue;
    private boolean thumDownloading;
    private String thumFileUrl;
    private String thumLocalFilePath;
    private String thumName;
    private long thumSize;
    private long thumTotal;
    private long total;
    private String videoDuration;
    private recordType videoType = recordType.normal_record;

    public enum recordType {
        normal_record,
        delay_record,
        dynamic_delay_record
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean isHeadView() {
        return this.isHeadView;
    }

    public void setHeadView(boolean headView) {
        this.isHeadView = headView;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getFormatDate() {
        return this.formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public String getFileLocalPath() {
        return this.fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    public String getThumFileUrl() {
        return this.thumFileUrl;
    }

    public void setThumFileUrl(String thumFileUrl) {
        this.thumFileUrl = thumFileUrl;
    }

    public boolean isVideo() {
        return this.isVideo;
    }

    public void setVideo(boolean video) {
        this.isVideo = video;
    }

    public String getDownLoadOriginalPath() {
        return this.downLoadOriginalPath;
    }

    public void setDownLoadOriginalPath(String downLoadOriginalPath) {
        this.downLoadOriginalPath = downLoadOriginalPath;
    }

    public String getDownLoadSmallPath() {
        return this.downLoadSmallPath;
    }

    public void setDownLoadSmallPath(String downLoadSmallPath) {
        this.downLoadSmallPath = downLoadSmallPath;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isDownLoadSmallFile() {
        return this.isDownLoadSmallFile;
    }

    public void setDownLoadSmallFile(boolean downLoadSmallFile) {
        this.isDownLoadSmallFile = downLoadSmallFile;
    }

    public boolean isCategory() {
        return this.isCategory;
    }

    public void setCategory(boolean category) {
        this.isCategory = category;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
    }

    public String getVideoDuration() {
        return this.videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public boolean isLoadThulm() {
        return this.isLoadThulm;
    }

    public void setLoadThulm(boolean loadThulm) {
        this.isLoadThulm = loadThulm;
    }

    public String getThumLocalFilePath() {
        return this.thumLocalFilePath;
    }

    public void setThumLocalFilePath(String thumLocalFilePath) {
        this.thumLocalFilePath = thumLocalFilePath;
    }

    public boolean isDownLoadThum() {
        return this.isDownLoadThum;
    }

    public void setDownLoadThum(boolean downLoadThum) {
        this.isDownLoadThum = downLoadThum;
    }

    public boolean isDownloadFail() {
        return this.isDownloadFail;
    }

    public void setDownloadFail(boolean downloadFail) {
        this.isDownloadFail = downloadFail;
    }

    public boolean isDownLoadOriginalFile() {
        return this.isDownLoadOriginalFile;
    }

    public void setDownLoadOriginalFile(boolean downLoadOriginalFile) {
        this.isDownLoadOriginalFile = downLoadOriginalFile;
    }

    public recordType getVideoType() {
        return this.videoType;
    }

    public void setVideoType(recordType videoType) {
        this.videoType = videoType;
    }

    public long getThumSize() {
        return this.thumSize;
    }

    public void setThumSize(long thumSize) {
        this.thumSize = thumSize;
    }

    public boolean isStop() {
        return this.isStop;
    }

    public long getThumTotal() {
        return this.thumTotal;
    }

    public void setThumTotal(long thumTotal) {
        this.thumTotal = thumTotal;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public boolean isThumDownloading() {
        return this.thumDownloading;
    }

    public void setThumDownloading(boolean thumDownloading) {
        this.thumDownloading = thumDownloading;
    }

    public boolean isThumStop() {
        return this.isThumStop;
    }

    public boolean isThumDownloadFinish() {
        return this.isThumDownloadFinish;
    }

    public void setThumDownloadFinish(boolean thumDownloadFinish) {
        this.isThumDownloadFinish = thumDownloadFinish;
    }

    public void setThumStop(boolean thumStop) {
        this.isThumStop = thumStop;
    }

    public boolean isDownloadFinish() {
        return this.isDownloadFinish;
    }

    public void setDownloadFinish(boolean isDownloadFinish) {
        this.isDownloadFinish = isDownloadFinish;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getDownloadName() {
        return this.downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    public boolean isDownloading() {
        return this.downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getThumName() {
        return this.thumName;
    }

    public void setThumName(String thumName) {
        this.thumName = thumName;
    }

    public String getLocalFileDir() {
        return this.localFileDir;
    }

    public void setLocalFileDir(String localFileDir) {
        this.localFileDir = localFileDir;
    }

    public String getLocalThumFileDir() {
        return this.localThumFileDir;
    }

    public void setLocalThumFileDir(String localThumFileDir) {
        this.localThumFileDir = localThumFileDir;
    }

    public int getItemPosition() {
        return this.itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public Future<?> getTaskFutrue() {
        return this.taskFutrue;
    }

    public void setTaskFutrue(Future<?> taskFutrue) {
        this.taskFutrue = taskFutrue;
    }

    public void stopTask() {
        if (this.taskFutrue != null) {
            this.taskFutrue.cancel(true);
        }
        this.taskFutrue = null;
    }

    public String toString() {
        return "MediaModel{name='" + this.name + CoreConstants.SINGLE_QUOTE_CHAR + ", createDate=" + this.createDate + ", formatDate='" + this.formatDate + CoreConstants.SINGLE_QUOTE_CHAR + ", fileLocalPath='" + this.fileLocalPath + CoreConstants.SINGLE_QUOTE_CHAR + ", thumLocalFilePath='" + this.thumLocalFilePath + CoreConstants.SINGLE_QUOTE_CHAR + ", fileUrl='" + this.fileUrl + CoreConstants.SINGLE_QUOTE_CHAR + ", thumFileUrl='" + this.thumFileUrl + CoreConstants.SINGLE_QUOTE_CHAR + ", isVideo=" + this.isVideo + ", downLoadOriginalPath='" + this.downLoadOriginalPath + CoreConstants.SINGLE_QUOTE_CHAR + ", downLoadSmallPath='" + this.downLoadSmallPath + CoreConstants.SINGLE_QUOTE_CHAR + ", fileSize=" + this.fileSize + ", isDownLoadOriginalFile=" + this.isDownLoadOriginalFile + ", isDownloadFail=" + this.isDownloadFail + ", isDownLoadSmallFile=" + this.isDownLoadSmallFile + ", isDownLoadThum=" + this.isDownLoadThum + ", isCategory=" + this.isCategory + ", isSelect=" + this.isSelect + ", videoDuration='" + this.videoDuration + CoreConstants.SINGLE_QUOTE_CHAR + ", isLoadThulm=" + this.isLoadThulm + ", isHeadView=" + this.isHeadView + ", itemPosition=" + this.itemPosition + ", md5='" + this.md5 + CoreConstants.SINGLE_QUOTE_CHAR + ", downloading=" + this.downloading + ", progress=" + this.progress + ", downloadName='" + this.downloadName + CoreConstants.SINGLE_QUOTE_CHAR + ", isStop=" + this.isStop + ", total=" + this.total + ", isDownloadFinish=" + this.isDownloadFinish + ", thumSize=" + this.thumSize + ", thumName='" + this.thumName + CoreConstants.SINGLE_QUOTE_CHAR + ", localFileDir='" + this.localFileDir + CoreConstants.SINGLE_QUOTE_CHAR + ", localThumFileDir='" + this.localThumFileDir + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }

    public MediaModel clone() {
        MediaModel mediaModel = null;
        try {
            return (MediaModel) super.clone();
        } catch (Exception e) {
            return mediaModel;
        }
    }
}
