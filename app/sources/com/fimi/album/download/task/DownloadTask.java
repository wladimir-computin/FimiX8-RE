package com.fimi.album.download.task;

import android.support.graphics.drawable.PathInterpolatorCompat;
import com.fimi.album.download.entity.FileInfo;
import com.fimi.album.download.interfaces.OnDownloadListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask implements Runnable {
    private int finished = 0;
    private FileInfo info;
    private OnDownloadListener listener;

    public DownloadTask(FileInfo info, OnDownloadListener listener) {
        this.info = info;
        this.listener = listener;
        info.setDownloading(true);
    }

    public void run() {
        startDownload();
    }

    public void startBreakPoint() {
        getLength();
        download();
    }

    private void startDownload() {
        File dir = new File(this.info.getPath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(this.info.getPath(), this.info.getDownloadFileName());
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(this.info.getUrl()).openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            CopyStream(is, os);
            save(this.info);
            os.close();
            conn.disconnect();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        if (this.info.isDownloadFinish()) {
            this.listener.onSuccess(this.info);
        } else {
            this.listener.onFailure(this.info);
        }
    }

    public void CopyStream(InputStream is, OutputStream os) {
        try {
            byte[] buffer = new byte[4096];
            do {
                int len = is.read(buffer);
                if (len != -1) {
                    os.write(buffer, 0, len);
                    this.finished += len;
                    this.info.setFinished(this.finished);
                    this.listener.onProgress(this.info, (long) this.info.getFinished(), (long) this.info.getLength());
                } else {
                    this.info.setDownloadFinish(true);
                    this.info.setDownloading(false);
                    return;
                }
            } while (!this.info.isStop());
            this.info.setDownloading(false);
        } catch (Exception e) {
        }
    }

    public boolean save(FileInfo info) {
        return new File(info.getPath(), info.getDownloadFileName()).renameTo(new File(info.getPath(), info.getFileName()));
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:58:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0143 A:{SYNTHETIC, Splitter:B:37:0x0143} */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x014f  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0154 A:{SYNTHETIC, Splitter:B:45:0x0154} */
    private void download() {
        /*
        r19 = this;
        r9 = 0;
        r15 = 0;
        r18 = new java.net.URL;	 Catch:{ Exception -> 0x012d }
        r0 = r19;
        r2 = r0.info;	 Catch:{ Exception -> 0x012d }
        r2 = r2.getUrl();	 Catch:{ Exception -> 0x012d }
        r0 = r18;
        r0.<init>(r2);	 Catch:{ Exception -> 0x012d }
        r2 = r18.openConnection();	 Catch:{ Exception -> 0x012d }
        r0 = r2;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x012d }
        r9 = r0;
        r2 = "GET";
        r9.setRequestMethod(r2);	 Catch:{ Exception -> 0x012d }
        r2 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;
        r9.setConnectTimeout(r2);	 Catch:{ Exception -> 0x012d }
        r0 = r19;
        r2 = r0.info;	 Catch:{ Exception -> 0x012d }
        r17 = r2.getFinished();	 Catch:{ Exception -> 0x012d }
        r2 = "Range";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x012d }
        r3.<init>();	 Catch:{ Exception -> 0x012d }
        r4 = "bytes=";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x012d }
        r0 = r17;
        r3 = r3.append(r0);	 Catch:{ Exception -> 0x012d }
        r4 = "-";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x012d }
        r0 = r19;
        r4 = r0.info;	 Catch:{ Exception -> 0x012d }
        r4 = r4.getLength();	 Catch:{ Exception -> 0x012d }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x012d }
        r3 = r3.toString();	 Catch:{ Exception -> 0x012d }
        r9.setRequestProperty(r2, r3);	 Catch:{ Exception -> 0x012d }
        r12 = new java.io.File;	 Catch:{ Exception -> 0x012d }
        r0 = r19;
        r2 = r0.info;	 Catch:{ Exception -> 0x012d }
        r2 = r2.getPath();	 Catch:{ Exception -> 0x012d }
        r0 = r19;
        r3 = r0.info;	 Catch:{ Exception -> 0x012d }
        r3 = r3.getDownloadFileName();	 Catch:{ Exception -> 0x012d }
        r12.<init>(r2, r3);	 Catch:{ Exception -> 0x012d }
        r16 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x012d }
        r2 = "rwd";
        r0 = r16;
        r0.<init>(r12, r2);	 Catch:{ Exception -> 0x012d }
        r0 = r17;
        r2 = (long) r0;
        r0 = r16;
        r0.seek(r2);	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r2 = r0.finished;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r3 = r0.info;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r3 = r3.getFinished();	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r2 = r2 + r3;
        r0 = r19;
        r0.finished = r2;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r10 = r9.getResponseCode();	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r2 = 206; // 0xce float:2.89E-43 double:1.02E-321;
        if (r10 != r2) goto L_0x0119;
    L_0x0096:
        r13 = r9.getInputStream();	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r2 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r8 = new byte[r2];	 Catch:{ Exception -> 0x0161, all -> 0x015d }
    L_0x009e:
        r14 = r13.read(r8);	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r2 = -1;
        if (r14 == r2) goto L_0x0103;
    L_0x00a5:
        r2 = 0;
        r0 = r16;
        r0.write(r8, r2, r14);	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r2 = r0.finished;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r2 = r2 + r14;
        r0 = r19;
        r0.finished = r2;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r2 = r0.info;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r3 = r0.finished;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r2.setFinished(r3);	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r2 = r0.listener;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r3 = r0.info;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r4 = r0.info;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r4 = r4.getFinished();	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r4 = (long) r4;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r6 = r0.info;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r6 = r6.getLength();	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r6 = (long) r6;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r2.onProgress(r3, r4, r6);	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r2 = r0.info;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r2 = r2.isStop();	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        if (r2 == 0) goto L_0x009e;
    L_0x00e6:
        r0 = r19;
        r2 = r0.info;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r3 = 0;
        r2.setDownloading(r3);	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r13.close();	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        if (r9 == 0) goto L_0x00f6;
    L_0x00f3:
        r9.disconnect();
    L_0x00f6:
        if (r16 == 0) goto L_0x00fb;
    L_0x00f8:
        r16.close();	 Catch:{ IOException -> 0x00fe }
    L_0x00fb:
        r15 = r16;
    L_0x00fd:
        return;
    L_0x00fe:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x00fb;
    L_0x0103:
        r0 = r19;
        r2 = r0.info;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r3 = 0;
        r2.setDownloading(r3);	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r2 = r0.listener;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r0 = r19;
        r3 = r0.info;	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r2.onSuccess(r3);	 Catch:{ Exception -> 0x0161, all -> 0x015d }
        r13.close();	 Catch:{ Exception -> 0x0161, all -> 0x015d }
    L_0x0119:
        if (r9 == 0) goto L_0x011e;
    L_0x011b:
        r9.disconnect();
    L_0x011e:
        if (r16 == 0) goto L_0x0123;
    L_0x0120:
        r16.close();	 Catch:{ IOException -> 0x0126 }
    L_0x0123:
        r15 = r16;
        goto L_0x00fd;
    L_0x0126:
        r11 = move-exception;
        r11.printStackTrace();
        r15 = r16;
        goto L_0x00fd;
    L_0x012d:
        r11 = move-exception;
    L_0x012e:
        r11.printStackTrace();	 Catch:{ all -> 0x014c }
        r0 = r19;
        r2 = r0.listener;	 Catch:{ all -> 0x014c }
        r0 = r19;
        r3 = r0.info;	 Catch:{ all -> 0x014c }
        r2.onFailure(r3);	 Catch:{ all -> 0x014c }
        if (r9 == 0) goto L_0x0141;
    L_0x013e:
        r9.disconnect();
    L_0x0141:
        if (r15 == 0) goto L_0x00fd;
    L_0x0143:
        r15.close();	 Catch:{ IOException -> 0x0147 }
        goto L_0x00fd;
    L_0x0147:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x00fd;
    L_0x014c:
        r2 = move-exception;
    L_0x014d:
        if (r9 == 0) goto L_0x0152;
    L_0x014f:
        r9.disconnect();
    L_0x0152:
        if (r15 == 0) goto L_0x0157;
    L_0x0154:
        r15.close();	 Catch:{ IOException -> 0x0158 }
    L_0x0157:
        throw r2;
    L_0x0158:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x0157;
    L_0x015d:
        r2 = move-exception;
        r15 = r16;
        goto L_0x014d;
    L_0x0161:
        r11 = move-exception;
        r15 = r16;
        goto L_0x012e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.album.download.task.DownloadTask.download():void");
    }

    private void getLength() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(this.info.getUrl()).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(PathInterpolatorCompat.MAX_NUM_POINTS);
            int length = -1;
            if (connection.getResponseCode() == 200) {
                length = connection.getContentLength();
            }
            if (length > 0) {
                File dir = new File(this.info.getPath());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                this.info.setLength(length);
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
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
