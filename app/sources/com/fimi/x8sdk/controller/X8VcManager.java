package com.fimi.x8sdk.controller;

import android.content.Context;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.ivew.IVcAction;
import com.fimi.x8sdk.presenter.X8VcPresenter;

public class X8VcManager implements IVcAction {
    IVcAction vcAction = new X8VcPresenter();

    public void setContext(Context context) {
        ((X8VcPresenter) this.vcAction).setContext(context);
    }

    public void setVcRectF(UiCallBackListener listener, int x, int y, int w, int h, int classfier) {
        this.vcAction.setVcRectF(listener, x, y, w, h, classfier);
    }

    public void setVcFpvMode(UiCallBackListener listener, int mode) {
        this.vcAction.setVcFpvMode(listener, mode);
    }

    public void setVcFpvLostSeq(UiCallBackListener listener, int seq) {
        this.vcAction.setVcFpvLostSeq(listener, seq);
    }
}
