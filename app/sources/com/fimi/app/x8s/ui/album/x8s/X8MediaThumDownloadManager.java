package com.fimi.app.x8s.ui.album.x8s;

import android.os.Handler;
import android.os.Message;
import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.interfaces.IMediaDownload;
import com.fimi.album.interfaces.OnDownloadUiListener;
import com.fimi.host.HostLogBack;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class X8MediaThumDownloadManager implements OnDownloadListener, IMediaDownload {
    private static X8MediaThumDownloadManager x8MediaThumDownloadManager = new X8MediaThumDownloadManager();
    private List<MediaModel> data = new ArrayList();
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private int index;
    public boolean isDownload;
    private Handler mHanler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    X8MediaThumDownloadManager.this.mUiDownloadListener.onProgress((MediaModel) msg.obj, msg.arg1);
                    return;
                case 1:
                    X8MediaThumDownloadManager.this.mUiDownloadListener.onSuccess((MediaModel) msg.obj);
                    HostLogBack.getInstance().writeLog("Alanqiu  ==================handleMessage:" + msg.obj.toString());
                    return;
                case 2:
                    X8MediaThumDownloadManager.this.mUiDownloadListener.onFailure((MediaModel) msg.obj);
                    return;
                default:
                    return;
            }
        }
    };
    private OnDownloadUiListener mUiDownloadListener;

    public static X8MediaThumDownloadManager getInstance() {
        return x8MediaThumDownloadManager;
    }

    public void setOnDownloadUiListener(OnDownloadUiListener mUiDownloadListener) {
        this.mUiDownloadListener = mUiDownloadListener;
    }

    public void addData(MediaModel m) {
        if (!this.data.contains(m)) {
            this.data.add(m);
        }
    }

    public void stopDownload() {
        this.isDownload = false;
        this.index = 0;
        this.data.clear();
    }

    public int getCount() {
        return this.data.size();
    }

    public void startDownload() {
        this.isDownload = true;
        if (this.data.size() > 0 && this.index < this.data.size()) {
            this.executorService.submit(new X8MediaThumDownloadTask((MediaModel) this.data.get(this.index), this));
        }
    }

    public void next() {
        if (this.isDownload) {
            this.index++;
            startDownload();
            HostLogBack.getInstance().writeLog("Alanqiu  ===============next index:" + this.index);
        }
    }

    public void onProgress(Object responseObj, long progrss, long currentLength) {
        int pos = (int) (progrss / (currentLength / 100));
        this.mHanler.obtainMessage(0, (int) progrss, (int) progrss, responseObj).sendToTarget();
    }

    public void onSuccess(Object responseObj) {
        MediaModel model = (MediaModel) responseObj;
        model.setThumDownloading(false);
        this.mHanler.obtainMessage(1, model).sendToTarget();
        HostLogBack.getInstance().writeLog("Alanqiu  ===============next onSuccess:" + this.index);
        next();
    }

    public void onFailure(Object reasonObj) {
        this.mHanler.obtainMessage(2, reasonObj).sendToTarget();
        HostLogBack.getInstance().writeLog("Alanqiu  ===============next onFailure:" + this.index);
        next();
    }

    public void onStop(MediaModel reasonObj) {
    }
}
