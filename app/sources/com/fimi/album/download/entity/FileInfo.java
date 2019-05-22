package com.fimi.album.download.entity;

public class FileInfo {
    private boolean downloading;
    private String fileName;
    private int finished;
    private boolean isDownloadFinish;
    private boolean isStop = false;
    private int length;
    private String path;
    private String url;

    public boolean isDownloading() {
        return this.downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDownloadFinish(boolean isDownloadFinish) {
        this.isDownloadFinish = isDownloadFinish;
    }

    public boolean isDownloadFinish() {
        return this.isDownloadFinish;
    }

    public String getDownloadFileName() {
        return String.valueOf(this.url.hashCode());
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return this.finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public boolean isStop() {
        return this.isStop;
    }

    public void setStop(boolean stop) {
        this.isStop = stop;
    }
}
