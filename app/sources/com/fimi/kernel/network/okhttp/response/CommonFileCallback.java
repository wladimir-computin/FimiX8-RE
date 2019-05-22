package com.fimi.kernel.network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.fimi.kernel.network.okhttp.exception.OkHttpException;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDownloadListener;
import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommonFileCallback implements Callback {
    private static final int PROGRESS_MESSAGE = 1;
    protected final String EMPTY_MSG = "";
    protected final int IO_ERROR = -2;
    protected final int NETWORK_ERROR = -1;
    DisposeDataHandle dataHandle;
    private Handler mDeliveryHandler;
    private String mFilePath;
    private DisposeDownloadListener mListener;
    private int mProgress;

    public CommonFileCallback(DisposeDataHandle handle) {
        this.mListener = (DisposeDownloadListener) handle.mListener;
        this.mFilePath = handle.mSource;
        this.dataHandle = handle;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        CommonFileCallback.this.mListener.onProgress(msg.arg1, msg.arg2);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public void onFailure(Call call, final IOException ioexception) {
        this.mDeliveryHandler.post(new Runnable() {
            public void run() {
                CommonFileCallback.this.mListener.onFailure(new OkHttpException(-1, ioexception));
            }
        });
    }

    public void onResponse(Call call, Response response) throws IOException {
        final File file = handleResponse(response);
        this.mDeliveryHandler.post(new Runnable() {
            public void run() {
                if (file != null) {
                    CommonFileCallback.this.mListener.onSuccess(file);
                } else {
                    CommonFileCallback.this.mListener.onFailure(new OkHttpException(-2, ""));
                }
            }
        });
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00c7 A:{SYNTHETIC, Splitter:B:27:0x00c7} */
    /* JADX WARNING: Removed duplicated region for block: B:55:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00cc A:{Catch:{ IOException -> 0x00d1 }} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00da A:{SYNTHETIC, Splitter:B:35:0x00da} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00df A:{Catch:{ IOException -> 0x00e3 }} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00da A:{SYNTHETIC, Splitter:B:35:0x00da} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00df A:{Catch:{ IOException -> 0x00e3 }} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00c7 A:{SYNTHETIC, Splitter:B:27:0x00c7} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00cc A:{Catch:{ IOException -> 0x00d1 }} */
    /* JADX WARNING: Removed duplicated region for block: B:55:? A:{SYNTHETIC, RETURN} */
    private java.io.File handleResponse(okhttp3.Response r19) {
        /*
        r18 = this;
        if (r19 != 0) goto L_0x0004;
    L_0x0002:
        r5 = 0;
    L_0x0003:
        return r5;
    L_0x0004:
        r9 = 0;
        r5 = 0;
        r7 = 0;
        r14 = 20480; // 0x5000 float:2.8699E-41 double:1.01185E-319;
        r2 = new byte[r14];
        r3 = 0;
        r0 = r18;
        r14 = r0.mFilePath;	 Catch:{ Exception -> 0x00a7 }
        r0 = r18;
        r0.checkLocalFilePath(r14);	 Catch:{ Exception -> 0x00a7 }
        r6 = new java.io.File;	 Catch:{ Exception -> 0x00a7 }
        r0 = r18;
        r14 = r0.mFilePath;	 Catch:{ Exception -> 0x00a7 }
        r6.<init>(r14);	 Catch:{ Exception -> 0x00a7 }
        r8 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00ef, all -> 0x00e8 }
        r8.<init>(r6);	 Catch:{ Exception -> 0x00ef, all -> 0x00e8 }
        r14 = r19.body();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r9 = r14.byteStream();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r14 = r19.body();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r14 = r14.contentLength();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r12 = (double) r14;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r14 = "moweiru";
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r15.<init>();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r16 = "dataHandle.isStop()";
        r15 = r15.append(r16);	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r0 = r18;
        r0 = r0.dataHandle;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r16 = r0;
        r16 = r16.isStop();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r15 = r15.append(r16);	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r15 = r15.toString();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        com.fimi.kernel.utils.LogUtil.d(r14, r15);	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
    L_0x0056:
        r10 = r9.read(r2);	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r14 = -1;
        if (r10 == r14) goto L_0x008e;
    L_0x005d:
        r14 = 0;
        r8.write(r2, r14, r10);	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r3 = r3 + r10;
        r14 = (double) r3;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r14 = r14 / r12;
        r16 = 4636737291354636288; // 0x4059000000000000 float:0.0 double:100.0;
        r14 = r14 * r16;
        r14 = (int) r14;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r0 = r18;
        r0.mProgress = r14;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r11 = new android.os.Message;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r11.<init>();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r14 = 1;
        r11.what = r14;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r0 = r18;
        r14 = r0.mProgress;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r11.arg1 = r14;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r11.arg2 = r3;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r0 = r18;
        r14 = r0.mDeliveryHandler;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r14.sendMessage(r11);	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r0 = r18;
        r14 = r0.dataHandle;	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        r14 = r14.isStop();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        if (r14 == 0) goto L_0x0056;
    L_0x008e:
        r8.flush();	 Catch:{ Exception -> 0x00f2, all -> 0x00eb }
        if (r8 == 0) goto L_0x0096;
    L_0x0093:
        r8.close();	 Catch:{ IOException -> 0x009f }
    L_0x0096:
        if (r9 == 0) goto L_0x009b;
    L_0x0098:
        r9.close();	 Catch:{ IOException -> 0x009f }
    L_0x009b:
        r7 = r8;
        r5 = r6;
        goto L_0x0003;
    L_0x009f:
        r4 = move-exception;
        r4.printStackTrace();
        r7 = r8;
        r5 = r6;
        goto L_0x0003;
    L_0x00a7:
        r4 = move-exception;
    L_0x00a8:
        r14 = "moweiru";
        r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d7 }
        r15.<init>();	 Catch:{ all -> 0x00d7 }
        r16 = "file exception:";
        r15 = r15.append(r16);	 Catch:{ all -> 0x00d7 }
        r16 = r4.getMessage();	 Catch:{ all -> 0x00d7 }
        r15 = r15.append(r16);	 Catch:{ all -> 0x00d7 }
        r15 = r15.toString();	 Catch:{ all -> 0x00d7 }
        com.fimi.kernel.utils.LogUtil.d(r14, r15);	 Catch:{ all -> 0x00d7 }
        r5 = 0;
        if (r7 == 0) goto L_0x00ca;
    L_0x00c7:
        r7.close();	 Catch:{ IOException -> 0x00d1 }
    L_0x00ca:
        if (r9 == 0) goto L_0x0003;
    L_0x00cc:
        r9.close();	 Catch:{ IOException -> 0x00d1 }
        goto L_0x0003;
    L_0x00d1:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x0003;
    L_0x00d7:
        r14 = move-exception;
    L_0x00d8:
        if (r7 == 0) goto L_0x00dd;
    L_0x00da:
        r7.close();	 Catch:{ IOException -> 0x00e3 }
    L_0x00dd:
        if (r9 == 0) goto L_0x00e2;
    L_0x00df:
        r9.close();	 Catch:{ IOException -> 0x00e3 }
    L_0x00e2:
        throw r14;
    L_0x00e3:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x00e2;
    L_0x00e8:
        r14 = move-exception;
        r5 = r6;
        goto L_0x00d8;
    L_0x00eb:
        r14 = move-exception;
        r7 = r8;
        r5 = r6;
        goto L_0x00d8;
    L_0x00ef:
        r4 = move-exception;
        r5 = r6;
        goto L_0x00a8;
    L_0x00f2:
        r4 = move-exception;
        r7 = r8;
        r5 = r6;
        goto L_0x00a8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.network.okhttp.response.CommonFileCallback.handleResponse(okhttp3.Response):java.io.File");
    }

    private void checkLocalFilePath(String localFilePath) {
        File path = new File(localFilePath.substring(0, localFilePath.lastIndexOf("/") + 1));
        File file = new File(localFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
