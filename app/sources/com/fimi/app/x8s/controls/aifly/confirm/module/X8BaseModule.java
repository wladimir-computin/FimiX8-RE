package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;

public abstract class X8BaseModule {
    protected int parentLevel;
    protected View rootView;

    public abstract void setFcHeart(boolean z, boolean z2);

    public int getParentLevel() {
        return this.parentLevel;
    }

    public void setParentLevel(int parentLevel) {
        this.parentLevel = parentLevel;
    }

    public View getRootView() {
        return this.rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public void init(Activity activity, View rootView) {
        this.rootView = rootView;
    }
}
