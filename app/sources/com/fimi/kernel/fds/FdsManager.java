package com.fimi.kernel.fds;

import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FdsManager implements IFdsUploadListener {
    private static final int DOWNLOAD_FAIL = 2;
    private static final int DOWNLOAD_PROGRESS = 0;
    private static final int DOWNLOAD_STOP = 3;
    private static final int DOWNLOAD_SUCCESS = 1;
    private static FdsManager fdsManager = new FdsManager();
    private IFdsCountListener countListener;
    private List<IFdsFileModel> dataAll = new ArrayList();
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Handler mHanler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (FdsManager.this.uiListener != null) {
                switch (msg.what) {
                    case 0:
                        FdsManager.this.uiListener.onProgress((IFdsFileModel) msg.obj, msg.arg1);
                        return;
                    case 1:
                        FdsManager.this.uiListener.onSuccess((IFdsFileModel) msg.obj);
                        return;
                    case 2:
                        FdsManager.this.uiListener.onFailure((IFdsFileModel) msg.obj);
                        return;
                    case 3:
                        FdsManager.this.uiListener.onStop((IFdsFileModel) msg.obj);
                        return;
                    default:
                        return;
                }
            }
        }
    };
    private IFdsUiListener uiListener;

    public static FdsManager getInstance() {
        return fdsManager;
    }

    public void setUiListener(IFdsUiListener uiListener) {
        this.uiListener = uiListener;
    }

    public void setFdsCountListener(IFdsCountListener countListener) {
        this.countListener = countListener;
    }

    public void startDownload(IFdsFileModel model) {
        model.setState(FdsUploadState.WAIT);
        downloadFile(model);
    }

    private void downloadFile(IFdsFileModel model) {
        if (!this.dataAll.contains(model)) {
            model.setTaskFutrue(this.executorService.submit(new FdsUploadTask(model, this)));
            this.dataAll.add(model);
            notityDataSetChange();
        }
    }

    public void stopAll() {
        for (IFdsFileModel model : this.dataAll) {
            if (model.getTaskFutrue() != null) {
                if (model.getRunable() != null) {
                    model.getRunable().stopUpload();
                }
                model.getTaskFutrue().cancel(true);
            }
            model.setState(FdsUploadState.STOP);
            model.setRunable(null);
            model.setTaskFutrue(null);
        }
        this.dataAll.clear();
        notityDataSetChange();
    }

    public boolean hasUpload() {
        if (this.dataAll.size() > 0) {
            return true;
        }
        return false;
    }

    public void remove(Object model) {
        this.dataAll.remove(model);
        notityDataSetChange();
    }

    public void notityDataSetChange() {
        this.countListener.onUploadingCountChange(this.dataAll.size());
    }

    public void onProgress(Object responseObj, long progrss, long currentLength) {
        int p = (int) ((100 * progrss) / currentLength);
        this.mHanler.obtainMessage(0, p, p, responseObj).sendToTarget();
    }

    public void onSuccess(Object responseObj) {
        this.mHanler.obtainMessage(1, responseObj).sendToTarget();
    }

    public void onFailure(Object reasonObj) {
        this.mHanler.obtainMessage(2, reasonObj).sendToTarget();
    }

    public void onStop(Object reasonObj) {
        this.mHanler.obtainMessage(3, reasonObj).sendToTarget();
    }
}
