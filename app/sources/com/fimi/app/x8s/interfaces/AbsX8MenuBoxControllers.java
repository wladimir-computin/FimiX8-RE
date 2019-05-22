package com.fimi.app.x8s.interfaces;

import android.view.View;
import android.view.ViewGroup;

public abstract class AbsX8MenuBoxControllers extends AbsX8Controllers {
    protected int MAX_WIDTH;
    protected View contentView;
    protected int width;

    public AbsX8MenuBoxControllers(View rootView) {
        super(rootView);
    }

    public void unMountError(boolean unMount) {
    }

    public void updateViewEnable(boolean enable, ViewGroup... parent) {
        if (parent != null && parent.length > 0) {
            for (ViewGroup group : parent) {
                int len = group.getChildCount();
                for (int j = 0; j < len; j++) {
                    View subView = group.getChildAt(j);
                    if (subView instanceof ViewGroup) {
                        updateViewEnable(enable, (ViewGroup) subView);
                    } else {
                        subView.setEnabled(enable);
                        if (enable) {
                            subView.setAlpha(1.0f);
                        } else {
                            subView.setAlpha(0.6f);
                        }
                    }
                }
            }
        }
    }

    public boolean isRunningTask() {
        return false;
    }

    public boolean onClickBackKey() {
        return false;
    }
}
