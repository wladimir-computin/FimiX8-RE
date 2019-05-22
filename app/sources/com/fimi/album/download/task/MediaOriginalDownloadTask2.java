package com.fimi.album.download.task;

import android.support.graphics.drawable.PathInterpolatorCompat;
import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaModel;
import com.fimi.kernel.connect.tcp.SocketOption;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaOriginalDownloadTask2 implements Runnable {
    private int finished = 0;
    private OnDownloadListener listener;
    private MediaModel model;

    public MediaOriginalDownloadTask2(MediaModel model, OnDownloadListener listener) {
        this.model = model;
        this.listener = listener;
        model.setTotal(0);
        model.setDownloading(true);
    }

    public void run() {
        startDownload();
    }

    private void startDownload() {
        long len = (long) getLength();
        String path = "";
        String fileName = "";
        String urlPath = "";
        path = this.model.getLocalFileDir();
        urlPath = this.model.getFileUrl();
        this.model.setDownloadName(String.valueOf(urlPath.hashCode()));
        fileName = this.model.getDownloadName();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path, fileName);
        if (file.exists()) {
            this.listener.onSuccess(this.model);
            return;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlPath).openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setRequestProperty("Range", "bytes=0-" + (this.model.getFileSize() - 1));
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            CopyStream(is, os);
            save(this.model);
            os.close();
            conn.disconnect();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        if (this.model.isDownloadFinish()) {
            this.listener.onSuccess(this.model);
        } else {
            this.listener.onFailure(this.model);
        }
    }

    public void CopyStream(InputStream is, OutputStream os) {
        try {
            byte[] buffer = new byte[SocketOption.RECEIVE_BUFFER_SIZE];
            do {
                int len = is.read(buffer);
                if (len != -1) {
                    os.write(buffer, 0, len);
                    this.finished += len;
                    this.model.setTotal((long) this.finished);
                    this.listener.onProgress(this.model, this.model.getTotal(), this.model.getFileSize());
                } else {
                    this.model.setDownloadFinish(true);
                    this.model.setDownloading(false);
                    this.model.setDownLoadOriginalFile(true);
                    return;
                }
            } while (!this.model.isStop());
            this.listener.onStop(this.model);
            this.model.setDownloading(false);
        } catch (Exception e) {
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

    private int getLength() {
        int length = -1;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(this.model.getFileUrl()).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(PathInterpolatorCompat.MAX_NUM_POINTS);
            if (connection.getResponseCode() == 200) {
                length = connection.getContentLength();
            }
            if (length <= 0) {
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return length;
            }
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return length;
        } catch (Exception e22) {
            e22.printStackTrace();
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e222) {
                    e222.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e2222) {
                    e2222.printStackTrace();
                }
            }
        }
    }
}
