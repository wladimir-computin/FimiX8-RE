package com.fimi.kernel.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;
    private static Toast mTosat;

    public static void showToast(Context context, String content, int duration) {
        if (context != null) {
            if (mTosat != null) {
                mTosat.setText(content);
            } else if (duration == 0) {
                mTosat = Toast.makeText(context, content, 0);
            } else if (duration == 1) {
                mTosat = Toast.makeText(context, content, 1);
            }
            mTosat.show();
        }
    }

    public static void showToast(Context context, int resId, int duration) {
        if (context != null) {
            showToast(context, context.getString(resId), duration);
        }
    }

    public static void cancelTosat() {
        if (mTosat != null) {
            mTosat.cancel();
        }
    }
}
