package com.fimi.app.x8s.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.DisplayMetrics;
import android.view.View;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.IFimiShotResult;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.widget.X8ToastUtil;
import com.twitter.sdk.android.core.internal.scribe.SyndicatedSdkImpressionEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class X8ScreenShotManager {
    public static boolean isBusy;
    boolean isFpvShotSuccess;
    boolean isMapShotSuccess;
    private X8ShotAsyncTask mFpvShotTask;
    private X8ShotAsyncTask mMapShotTask;

    public static String saveScreenBitmap(Activity activity) {
        Bitmap bitmap = screenShot(activity);
        File file = new File(DirectoryPath.getX8LocalSar() + "/" + DateUtil.getStringByFormat(System.currentTimeMillis(), DateUtil.dateFormatYYMMDDHHMMSS) + ".jpeg");
        String s = "";
        try {
            if (!file.exists()) {
                if (file.getParentFile().exists()) {
                    file.createNewFile();
                } else {
                    file.getParentFile().mkdirs();
                }
            }
            if (save(bitmap, file, CompressFormat.JPEG, true)) {
                X8ToastUtil.showToast(activity, activity.getString(R.string.x8_ai_fly_sar_save_pic_tip), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static boolean save(Bitmap src, File file, CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return false;
        }
        try {
            boolean ret = src.compress(format, 100, new BufferedOutputStream(new FileOutputStream(file)));
            if (!recycle || src.isRecycled()) {
                return ret;
            }
            src.recycle();
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap screenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = getStatusBarHeight(activity);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, 0, (int) getDeviceDisplaySize(activity)[0], (int) getDeviceDisplaySize(activity)[1]);
        view.destroyDrawingCache();
        return ret;
    }

    public static float[] getDeviceDisplaySize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return new float[]{(float) width, (float) height};
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", SyndicatedSdkImpressionEvent.CLIENT_NAME);
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public void starThread(final X8sMainActivity activity) {
        isBusy = true;
        this.mMapShotTask = new X8ShotAsyncTask(activity, new IFimiShotResult() {
            public void onShotResult(Bitmap btp) {
                activity.getmMapVideoController().setMapShot(btp);
                X8ScreenShotManager.this.isMapShotSuccess = true;
            }
        }, 0);
        this.mMapShotTask.execute(new String[]{""});
        this.mFpvShotTask = new X8ShotAsyncTask(activity, new IFimiShotResult() {
            public void onShotResult(Bitmap btp) {
                activity.getmMapVideoController().setFpvShot(btp);
                X8ScreenShotManager.this.isFpvShotSuccess = true;
                X8ScreenShotManager.this.isShotSave(activity);
            }
        }, 1);
        this.mFpvShotTask.execute(new String[]{""});
    }

    public void isShotSave(X8sMainActivity activity) {
        saveScreenBitmap(activity);
        activity.getmMapVideoController().clearShotBitmap();
        this.mFpvShotTask.recycleBitmap();
        this.mMapShotTask.recycleBitmap();
        isBusy = false;
    }
}
