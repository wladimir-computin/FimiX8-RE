package com.fimi.kernel.network.okhttp.listener;

public interface DisposeDataListener {
    void onFailure(Object obj);

    void onSuccess(Object obj);
}
