package com.fimi.album.download.task;

import android.support.graphics.drawable.PathInterpolatorCompat;
import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaModel;
import com.fimi.kernel.connect.tcp.SocketOption;
import com.fimi.kernel.store.sqlite.entity.MediaDownloadInfo;
import com.fimi.kernel.store.sqlite.helper.MediaDownloadInfoHelper;
import com.fimi.kernel.utils.LogUtil;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaOriginalDownloadTask implements Runnable {
    private MediaDownloadInfo downloadInfo;
    private long finished = 0;
    private OnDownloadListener listener;
    private MediaModel model;

    public MediaOriginalDownloadTask(MediaModel model, OnDownloadListener listener) {
        this.model = model;
        this.listener = listener;
        model.setStop(false);
        model.setDownloadFail(false);
        model.setDownloading(true);
    }

    public void run() {
        startDownload();
    }

    private void startDownload() {
        String path = "";
        String fileName = "";
        String urlPath = "";
        path = this.model.getLocalFileDir();
        this.model.setDownloadName(String.valueOf(this.model.getFileUrl().hashCode()));
        fileName = this.model.getDownloadName();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path, fileName);
        this.downloadInfo = MediaDownloadInfoHelper.getIntance().queryMediaDownloadInfo(this.model.getFileUrl());
        if (this.downloadInfo == null) {
            this.downloadInfo = new MediaDownloadInfo();
            this.downloadInfo.setUrl(this.model.getFileUrl());
            MediaDownloadInfoHelper.getIntance().addMediaDownloadInfo(this.downloadInfo);
        } else if (file.exists()) {
            this.downloadInfo.setEndPos(this.model.getFileSize());
            this.downloadInfo.setStartPos(this.downloadInfo.getCompeleteZize());
        } else {
            MediaDownloadInfoHelper.getIntance().deleteByUrl(this.model.getFileUrl());
            this.downloadInfo = new MediaDownloadInfo();
            this.downloadInfo.setUrl(this.model.getFileUrl());
            MediaDownloadInfoHelper.getIntance().addMediaDownloadInfo(this.downloadInfo);
        }
        this.finished = this.downloadInfo.getStartPos();
        this.downloadInfo.setEndPos(this.model.getFileSize());
        downloadFile(file);
        if (this.model.isDownloadFinish()) {
            MediaDownloadInfoHelper.getIntance().deleteMediaDownloadInfo(this.model.getFileUrl());
            save(this.model);
            this.listener.onSuccess(this.model);
        } else if (this.model.isStop()) {
            this.listener.onStop(this.model);
        } else if (this.model.isDownloadFail()) {
            LogUtil.i("download", "MediaOriginalDownloadTask====startDownload: ");
            this.listener.onFailure(this.model);
        }
    }

    public void downloadFile(File file) {
        Exception e;
        Throwable th;
        if (!this.model.isStop()) {
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            InputStream is = null;
            try {
                connection = (HttpURLConnection) new URL(this.model.getFileUrl()).openConnection();
                connection.setConnectTimeout(PathInterpolatorCompat.MAX_NUM_POINTS);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Range", "bytes=" + this.downloadInfo.getStartPos() + "-");
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "rwd");
                try {
                    randomAccessFile2.seek(this.downloadInfo.getStartPos());
                    is = connection.getInputStream();
                    byte[] buffer = new byte[SocketOption.RECEIVE_BUFFER_SIZE];
                    do {
                        int length = is.read(buffer);
                        if (length != -1) {
                            randomAccessFile2.write(buffer, 0, length);
                            this.finished += (long) length;
                            this.downloadInfo.setCompeleteZize(this.finished);
                            this.model.setTotal(this.finished);
                            this.model.setDownloading(true);
                            this.model.setDownloadFail(false);
                            this.listener.onProgress(this.model, this.model.getTotal(), this.model.getFileSize());
                            MediaDownloadInfoHelper.getIntance().updateMediaDownloadInfo(this.model.getFileUrl(), this.downloadInfo);
                        } else {
                            if (this.finished == this.model.getFileSize()) {
                                this.model.setDownloadFinish(true);
                                this.model.setDownloading(false);
                                this.model.setDownLoadOriginalFile(true);
                            } else {
                                this.model.setDownloadFinish(false);
                                this.model.setDownloadFail(true);
                                this.model.setDownloading(false);
                                this.listener.onFailure(this.model);
                            }
                            try {
                                is.close();
                                randomAccessFile2.close();
                                connection.disconnect();
                                randomAccessFile = randomAccessFile2;
                                return;
                            } catch (Exception e2) {
                                e2.printStackTrace();
                                randomAccessFile = randomAccessFile2;
                                return;
                            }
                        }
                    } while (!this.model.isStop());
                    this.model.setDownloading(false);
                    this.model.setDownloadFinish(false);
                    this.listener.onStop(this.model);
                    try {
                        is.close();
                        randomAccessFile2.close();
                        connection.disconnect();
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                } catch (Exception e3) {
                    e22 = e3;
                    randomAccessFile = randomAccessFile2;
                } catch (Throwable th2) {
                    th = th2;
                    randomAccessFile = randomAccessFile2;
                    try {
                        is.close();
                        randomAccessFile.close();
                        connection.disconnect();
                    } catch (Exception e222) {
                        e222.printStackTrace();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e222 = e4;
                try {
                    this.model.setDownloadFinish(false);
                    this.model.setDownloadFail(true);
                    this.model.setDownloading(false);
                    this.listener.onFailure(this.model);
                    e222.printStackTrace();
                    try {
                        is.close();
                        randomAccessFile.close();
                        connection.disconnect();
                    } catch (Exception e2222) {
                        e2222.printStackTrace();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    is.close();
                    randomAccessFile.close();
                    connection.disconnect();
                    throw th;
                }
            }
        }
    }

    public boolean save(MediaModel model) {
        String name = "";
        String localPath = "";
        name = model.getName();
        localPath = model.getLocalFileDir();
        File file = new File(localPath, name);
        boolean b = new File(localPath, model.getDownloadName()).renameTo(file);
        model.setFileLocalPath(file.getAbsolutePath());
        return b;
    }
}
