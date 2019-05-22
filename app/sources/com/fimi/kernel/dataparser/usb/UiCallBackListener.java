package com.fimi.kernel.dataparser.usb;

public interface UiCallBackListener<T> {
    void onComplete(CmdResult cmdResult, T t);
}
