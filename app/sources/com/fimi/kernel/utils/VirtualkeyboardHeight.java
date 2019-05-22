package com.fimi.kernel.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class VirtualkeyboardHeight {
    public static int getScreenDPI(Context context) {
        int dpi = 0;
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class.forName("android.view.Display").getMethod("getRealMetrics", new Class[]{DisplayMetrics.class}).invoke(display, new Object[]{displayMetrics});
            return displayMetrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return dpi;
        }
    }

    public static int getScreenHeightDPI(Context context) {
        int dpi = 0;
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class.forName("android.view.Display").getMethod("getRealMetrics", new Class[]{DisplayMetrics.class}).invoke(display, new Object[]{displayMetrics});
            return displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return dpi;
        }
    }

    public static int getBottomStatusHeight(Context context) {
        return getScreenDPI(context) - getScreenHeight(context);
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService("window");
        DisplayMetrics out = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(out);
        return out.widthPixels;
    }

    public static boolean isStandardSize(Context context) {
        float ratio = ((float) getScreenDPI(context)) / ((float) getScreenHeightDPI(context));
        if (ratio > 1.7777778f + 0.1f || ratio <= 1.7777778f - 0.1f) {
            return true;
        }
        return false;
    }
}
