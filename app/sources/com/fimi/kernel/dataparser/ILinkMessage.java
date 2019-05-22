package com.fimi.kernel.dataparser;

import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import java.io.Serializable;

public abstract class ILinkMessage implements Serializable {
    public abstract UiCallBackListener getUiCallBack();

    public abstract void setUiCallBack(UiCallBackListener uiCallBackListener);

    public int getMsgRpt() {
        return 0;
    }
}
