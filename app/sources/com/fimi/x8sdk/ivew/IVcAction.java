package com.fimi.x8sdk.ivew;

import com.fimi.kernel.dataparser.usb.UiCallBackListener;

public interface IVcAction {
    void setVcFpvLostSeq(UiCallBackListener uiCallBackListener, int i);

    void setVcFpvMode(UiCallBackListener uiCallBackListener, int i);

    void setVcRectF(UiCallBackListener uiCallBackListener, int i, int i2, int i3, int i4, int i5);
}
