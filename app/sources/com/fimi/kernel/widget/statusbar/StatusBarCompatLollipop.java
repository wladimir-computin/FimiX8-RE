package com.fimi.kernel.widget.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.fimi.kernel.FimiAppContext;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

@TargetApi(21)
public class StatusBarCompatLollipop {
    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        window.clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(statusColor);
        window.getDecorView().setSystemUiVisibility(0);
        View mChildView = ((ViewGroup) window.findViewById(16908290)).getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    return insets;
                }
            });
            ViewCompat.setFitsSystemWindows(mChildView, true);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        window.addFlags(Integer.MIN_VALUE);
        if (hideStatusBarBackground) {
            window.clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            window.setStatusBarColor(0);
            window.getDecorView().setSystemUiVisibility(FimiAppContext.UI_HEIGHT);
        } else {
            window.addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            window.getDecorView().setSystemUiVisibility(0);
        }
        View mChildView = ((ViewGroup) window.findViewById(16908290)).getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    return insets;
                }
            });
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static void setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusColor) {
        Window window = activity.getWindow();
        window.clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(0);
        window.getDecorView().setSystemUiVisibility(0);
        View mChildView = ((ViewGroup) window.findViewById(16908290)).getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    return insets;
                }
            });
            ViewCompat.setFitsSystemWindows(mChildView, true);
            ViewCompat.requestApplyInsets(mChildView);
        }
        ((View) appBarLayout.getParent()).setFitsSystemWindows(true);
        appBarLayout.setFitsSystemWindows(true);
        collapsingToolbarLayout.setFitsSystemWindows(true);
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(true);
        toolbar.setFitsSystemWindows(false);
        collapsingToolbarLayout.setStatusBarScrimColor(statusColor);
    }
}
