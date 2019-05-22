package com.fimi.app.x8s.ui.album.x8s;

import android.util.Log;
import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.session.MediaDataListener;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.store.sqlite.entity.MediaDownloadInfo;
import com.fimi.x8sdk.command.X8DownLoadCmd;
import com.fimi.x8sdk.command.X8MediaCmd;
import com.fimi.x8sdk.dataparser.MediaFileDownLoadPacket;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8MediaOriginalDownloadTask implements Runnable {
    private MediaDownloadInfo downloadInfo;
    private long finished = 0;
    boolean isAwait = false;
    private OnDownloadListener listener;
    int max_size = 1024;
    MediaDataListener mediaDataListener = new MediaDataListener() {
        public void mediaDataCallBack(byte[] data) {
            if (data != null && data.length > 0 && data[0] == (byte) 1) {
                final MediaFileDownLoadPacket downLoadPacket = new MediaFileDownLoadPacket();
                downLoadPacket.unPacket(data);
                if (X8MediaOriginalDownloadTask.this.model.getDownLoadOriginalPath().equals(downLoadPacket.getFileName()) && X8MediaOriginalDownloadTask.this.finished < X8MediaOriginalDownloadTask.this.model.getFileSize() && X8MediaOriginalDownloadTask.this.model.getFileSize() > 0) {
                    if (X8MediaOriginalDownloadTask.this.finished == ((long) downLoadPacket.getOffSet())) {
                        boolean isComplete;
                        X8MediaOriginalDownloadTask.this.listener.onProgress(X8MediaOriginalDownloadTask.this.model, X8MediaOriginalDownloadTask.this.finished, X8MediaOriginalDownloadTask.this.model.getFileSize());
                        try {
                            X8MediaOriginalDownloadTask.this.finished = X8MediaOriginalDownloadTask.this.finished + ((long) downLoadPacket.getPlayloadSize());
                            X8MediaOriginalDownloadTask.this.randomAccessFile.write(downLoadPacket.getPlayData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.i("felix", "已经下载：" + X8MediaOriginalDownloadTask.this.finished);
                        if (((long) (downLoadPacket.getOffSet() + downLoadPacket.getPlayloadSize())) >= X8MediaOriginalDownloadTask.this.model.getFileSize()) {
                            isComplete = true;
                        } else {
                            isComplete = false;
                        }
                        if (isComplete) {
                            HostLogBack.getInstance().writeLog("Alanqiu  ==================mediaDataCallBack:下载完成");
                            X8MediaOriginalDownloadTask.this.save(X8MediaOriginalDownloadTask.this.model);
                            X8MediaOriginalDownloadTask.this.listener.onSuccess(X8MediaOriginalDownloadTask.this.model);
                            X8MediaOriginalDownloadTask.this.model.setDownloadFinish(true);
                            X8MediaOriginalDownloadTask.this.model.setDownloading(false);
                            X8MediaOriginalDownloadTask.this.model.setDownLoadOriginalFile(true);
                            X8MediaOriginalDownloadTask.this.removeMediaListener();
                            X8MediaOriginalDownloadTask.this.closeWriteStream();
                        } else if (X8MediaOriginalDownloadTask.this.model.isStop()) {
                            HostLogBack.getInstance().writeLog("Alanqiu  ==================mediaDataCallBack:下载stop");
                            X8MediaOriginalDownloadTask.this.listener.onStop(X8MediaOriginalDownloadTask.this.model);
                            X8MediaOriginalDownloadTask.this.removeMediaListener();
                        } else {
                            X8MediaOriginalDownloadTask.this.model.setDownloading(true);
                            X8MediaOriginalDownloadTask.this.model.setDownloadFail(false);
                        }
                    } else if (!X8MediaOriginalDownloadTask.this.isAwait) {
                        X8MediaOriginalDownloadTask.this.isAwait = true;
                        HandlerManager.obtain().getHandlerInMainThread().postDelayed(new Runnable() {
                            public void run() {
                                HostLogBack.getInstance().writeLog("Alanqiu  ==================mediaDataCallBack finished:" + X8MediaOriginalDownloadTask.this.finished + "downLoadPacket.getOffSet():" + downLoadPacket.getOffSet());
                                X8MediaOriginalDownloadTask.this.sendCmd(new X8DownLoadCmd().downMediaFile((int) X8MediaOriginalDownloadTask.this.finished, NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE, X8MediaOriginalDownloadTask.this.model.getDownLoadOriginalPath(), false));
                                X8MediaOriginalDownloadTask.this.isAwait = false;
                            }
                        }, 3000);
                    }
                }
            }
        }
    };
    private MediaModel model;
    RandomAccessFile randomAccessFile = null;

    public X8MediaOriginalDownloadTask(MediaModel model, OnDownloadListener listener) {
        this.model = model;
        this.listener = listener;
        model.setStop(false);
        model.setDownloadFail(false);
        model.setDownloading(true);
        NoticeManager.getInstance().addMediaListener(this.mediaDataListener);
    }

    public void run() {
        startDownload();
    }

    private void startDownload() {
        Log.i("felix", "======开始下载=====");
        String path = this.model.getLocalFileDir();
        String downloadName = String.valueOf(this.model.getFileUrl().hashCode());
        this.model.setDownloadName(downloadName);
        File tempFile = new File(path, downloadName);
        if (tempFile.exists()) {
            this.finished = tempFile.length();
        } else {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.randomAccessFile = new RandomAccessFile(tempFile, "rwd");
            this.randomAccessFile.seek(this.finished);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Log.i("felix", "开始下载位置：" + this.finished);
        HostLogBack.getInstance().writeLog("Alanqiu  ==================startDownload:" + this.model.getDownLoadOriginalPath());
        sendCmd(new X8DownLoadCmd().downMediaFile((int) this.finished, NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE, this.model.getDownLoadOriginalPath(), false));
    }

    private void sendCmd(X8MediaCmd cmd) {
        if (cmd != null) {
            SessionManager.getInstance().sendCmd(cmd);
        }
    }

    public void sendStopDownload() {
        sendCmd(new X8DownLoadCmd().downMediaFile((int) this.finished, (short) this.max_size, this.model.getFileUrl(), true));
        HostLogBack.getInstance().writeLog("Alanqiu  ==================mediaDataCallBack:==停止下载===");
    }

    public void removeMediaListener() {
        if (this.mediaDataListener != null) {
            NoticeManager.getInstance().removeMediaListener(this.mediaDataListener);
        }
    }

    private void askNextPacket() {
        if (this.model.getFileSize() - this.finished > 0) {
            this.max_size = this.model.getFileSize() - this.finished >= ((long) this.max_size) ? this.max_size : (short) ((int) (this.model.getFileSize() - this.finished));
            sendCmd(new X8DownLoadCmd().downMediaFile((int) this.finished, (short) this.max_size, this.model.getFileUrl(), false));
        }
    }

    private boolean save(MediaModel model) {
        String name = "";
        String localPath = "";
        name = model.getName();
        localPath = model.getLocalFileDir();
        File file = new File(localPath, name);
        boolean b = new File(localPath, model.getDownloadName()).renameTo(file);
        model.setFileLocalPath(file.getAbsolutePath());
        return b;
    }

    private void closeWriteStream() {
        if (this.randomAccessFile != null) {
            try {
                this.randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.randomAccessFile = null;
        }
    }
}
