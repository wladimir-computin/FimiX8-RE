package com.fimi.album.download.manager;

import android.os.Handler;
import android.os.Message;
import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.download.task.MediaThumDownloadTask;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.interfaces.IMediaDownload;
import com.fimi.album.interfaces.OnDownloadUiListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaThumDownloadManager implements OnDownloadListener, IMediaDownload {
    private int count;
    private List<MediaModel> data = new ArrayList();
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private int index;
    private boolean isDownload;
    private Handler mHanler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    MediaThumDownloadManager.this.mUiDownloadListener.onProgress((MediaModel) msg.obj, msg.arg1);
                    return;
                case 1:
                    MediaThumDownloadManager.this.count = MediaThumDownloadManager.this.count + 1;
                    MediaThumDownloadManager.this.mUiDownloadListener.onSuccess((MediaModel) msg.obj);
                    return;
                case 2:
                    MediaThumDownloadManager.this.mUiDownloadListener.onFailure((MediaModel) msg.obj);
                    return;
                default:
                    return;
            }
        }
    };
    private OnDownloadUiListener mUiDownloadListener;

    public MediaThumDownloadManager(OnDownloadUiListener mUiDownloadListener) {
        this.mUiDownloadListener = mUiDownloadListener;
    }

    public void addData(MediaModel m) {
        if (!this.data.contains(m)) {
            this.data.add(m);
        }
    }

    public void stopDownload() {
        this.isDownload = false;
        this.count = 0;
        this.index = 0;
        this.data.clear();
    }

    public int getCount() {
        return this.data.size();
    }

    public void startDownload() {
        this.isDownload = true;
        if (this.data.size() > 0 && this.count < this.data.size()) {
            this.executorService.submit(new MediaThumDownloadTask((MediaModel) this.data.get(this.index), this));
        }
    }

    public void next() {
        if (this.isDownload) {
            this.index++;
            startDownload();
        }
    }

    public void onProgress(Object responseObj, long progrss, long currentLength) {
        int pos = (int) (progrss / (currentLength / 100));
        this.mHanler.obtainMessage(0, pos, pos, responseObj).sendToTarget();
    }

    public void onSuccess(Object responseObj) {
        this.mHanler.obtainMessage(1, responseObj).sendToTarget();
        next();
    }

    public void onFailure(Object reasonObj) {
        this.mHanler.obtainMessage(2, reasonObj).sendToTarget();
        next();
    }

    public void onStop(MediaModel reasonObj) {
    }
}
