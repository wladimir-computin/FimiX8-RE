package com.fimi.x8sdk.listener;

import com.fimi.kernel.dataparser.usb.CmdResult;

public interface HandleCallBackListener {

    public interface CallBack {
        void onComplete(CmdResult cmdResult);
    }

    public interface CallBackWithParam<T> {
        void onError(CmdResult cmdResult);

        void onResult(T t);
    }
}
