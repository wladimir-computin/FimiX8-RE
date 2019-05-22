package com.fimi.kernel.widget.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;
import com.twitter.sdk.android.core.internal.scribe.SyndicatedSdkImpressionEvent;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

@TargetApi(19)
public class StatusBarCompatKitKat {
    private static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";
    private static final String TAG_MARGIN_ADDED = "marginAdded";

    private static int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", SyndicatedSdkImpressionEvent.CLIENT_NAME);
        if (resId > 0) {
            return context.getResources().getDimensionPixelOffset(resId);
        }
        return 0;
    }

    private static View addFakeStatusBarView(Activity activity, int statusBarColor, int statusBarHeight) {
        ViewGroup mDecorView = (ViewGroup) activity.getWindow().getDecorView();
        View mStatusBarView = new View(activity);
        LayoutParams layoutParams = new LayoutParams(-1, statusBarHeight);
        layoutParams.gravity = 48;
        mStatusBarView.setLayoutParams(layoutParams);
        mStatusBarView.setBackgroundColor(statusBarColor);
        mStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);
        mDecorView.addView(mStatusBarView);
        return mStatusBarView;
    }

    private static void removeFakeStatusBarViewIfExist(Activity activity) {
        ViewGroup mDecorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeView = mDecorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (fakeView != null) {
            mDecorView.removeView(fakeView);
        }
    }

    private static void addMarginTopToContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild != null && !TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            LayoutParams lp = (LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin += statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(TAG_MARGIN_ADDED);
        }
    }

    private static void removeMarginTopOfContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild != null && TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            LayoutParams lp = (LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin -= statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(null);
        }
    }

    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        window.addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
        View mContentChild = ((ViewGroup) window.findViewById(16908290)).getChildAt(0);
        int statusBarHeight = getStatusBarHeight(activity);
        removeFakeStatusBarViewIfExist(activity);
        addFakeStatusBarView(activity, statusColor, statusBarHeight);
        addMarginTopToContentChild(mContentChild, statusBarHeight);
        if (mContentChild != null) {
            ViewCompat.setFitsSystemWindows(mContentChild, false);
        }
    }

    public static void translucentStatusBar(Activity activity) {
        activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
        View mContentChild = ((ViewGroup) activity.findViewById(16908290)).getChildAt(0);
        removeFakeStatusBarViewIfExist(activity);
        removeMarginTopOfContentChild(mContentChild, getStatusBarHeight(activity));
        if (mContentChild != null) {
            ViewCompat.setFitsSystemWindows(mContentChild, false);
        }
    }

    public static void setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusColor) {
        Window window = activity.getWindow();
        window.addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
        View mContentChild = ((ViewGroup) window.findViewById(16908290)).getChildAt(0);
        mContentChild.setFitsSystemWindows(false);
        ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
        toolbar.setFitsSystemWindows(true);
        if (toolbar.getTag() == null) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            lp.height += getStatusBarHeight(activity);
            toolbar.setLayoutParams(lp);
            toolbar.setTag(Boolean.valueOf(true));
        }
        int statusBarHeight = getStatusBarHeight(activity);
        removeFakeStatusBarViewIfExist(activity);
        removeMarginTopOfContentChild(mContentChild, statusBarHeight);
    }
}
