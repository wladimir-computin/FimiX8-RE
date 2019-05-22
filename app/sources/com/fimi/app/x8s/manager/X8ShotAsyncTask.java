package com.fimi.app.x8s.manager;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.fimi.app.x8s.interfaces.IFimiFpvShot;
import com.fimi.app.x8s.interfaces.IFimiOnSnapshotReady;
import com.fimi.app.x8s.interfaces.IFimiShotResult;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;

public class X8ShotAsyncTask extends AsyncTask<String, String, Bitmap> {
    private X8sMainActivity activity;
    private Bitmap btp = null;
    private IFimiShotResult callback;
    private int type;

    public X8ShotAsyncTask(X8sMainActivity activity, IFimiShotResult callback, int type) {
        this.activity = activity;
        this.callback = callback;
        this.type = type;
    }

    /* Access modifiers changed, original: protected */
    public void onPreExecute() {
        super.onPreExecute();
    }

    /* Access modifiers changed, original: protected|varargs */
    public Bitmap doInBackground(String... params) {
        if (this.type == 0) {
            this.activity.getmMapVideoController().snapshot(new IFimiOnSnapshotReady() {
                public void onSnapshotReady(Bitmap btp) {
                    X8ShotAsyncTask.this.btp = btp;
                }
            });
        } else {
            this.activity.getmMapVideoController().fpvShot(new IFimiFpvShot() {
                public void onFpvshotReady(Bitmap btp) {
                    X8ShotAsyncTask.this.btp = btp;
                }
            });
        }
        for (int i = 0; i < 75 && this.btp == null; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.btp;
    }

    /* Access modifiers changed, original: protected */
    public void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        this.callback.onShotResult(bitmap);
    }

    public void recycleBitmap() {
        if (this.btp != null && !this.btp.isRecycled()) {
            this.btp.recycle();
        }
    }
}
