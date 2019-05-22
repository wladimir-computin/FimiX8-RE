package com.fimi.network.oauth2;

public abstract class CallBackListner {
    public abstract void onSuccess(Object obj);

    public void onFailed(String errMsg) {
    }
}
