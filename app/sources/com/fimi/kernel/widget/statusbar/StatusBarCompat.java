package com.fimi.kernel.widget.statusbar;

import android.app.Activity;
import android.os.Build.VERSION;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;

public class StatusBarCompat {
    private static int calculateStatusBarColor(int color, int alpha) {
        float a = 1.0f - (((float) alpha) / 255.0f);
        return ((ViewCompat.MEASURED_STATE_MASK | (((int) (((double) (((float) ((color >> 16) & 255)) * a)) + 0.5d)) << 16)) | (((int) (((double) (((float) ((color >> 8) & 255)) * a)) + 0.5d)) << 8)) | ((int) (((double) (((float) (color & 255)) * a)) + 0.5d));
    }

    public static void setStatusBarColor(Activity activity, int statusColor, int alpha) {
        setStatusBarColor(activity, calculateStatusBarColor(statusColor, alpha));
    }

    public static void setStatusBarColor(Activity activity, int statusColor) {
        if (VERSION.SDK_INT >= 21) {
            StatusBarCompatLollipop.setStatusBarColor(activity, statusColor);
        } else if (VERSION.SDK_INT >= 19) {
            StatusBarCompatKitKat.setStatusBarColor(activity, statusColor);
        }
    }

    public static void translucentStatusBar(Activity activity) {
        translucentStatusBar(activity, false);
    }

    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        if (VERSION.SDK_INT >= 21) {
            StatusBarCompatLollipop.translucentStatusBar(activity, hideStatusBarBackground);
        } else if (VERSION.SDK_INT >= 19) {
            StatusBarCompatKitKat.translucentStatusBar(activity);
        }
    }

    public static void setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusColor) {
        if (VERSION.SDK_INT >= 21) {
            StatusBarCompatLollipop.setStatusBarColorForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        } else if (VERSION.SDK_INT >= 19) {
            StatusBarCompatKitKat.setStatusBarColorForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        }
    }
}
