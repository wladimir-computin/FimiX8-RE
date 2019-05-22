package com.fimi.kernel.network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import com.fimi.kernel.network.okhttp.exception.OkHttpException;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class CommonUrlCallback implements Callback {
    protected final int NETWORK_ERROR = -1;
    private boolean isArray = false;
    private boolean isGetCode = true;
    private Class<?> mClass;
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;

    public CommonUrlCallback(DisposeDataHandle handle, boolean isGetCode) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.isArray = handle.isArray;
        this.isGetCode = isGetCode;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    public void onFailure(Call call, final IOException e) {
        this.mDeliveryHandler.post(new Runnable() {
            public void run() {
                CommonUrlCallback.this.mListener.onFailure(new OkHttpException(-1, e));
            }
        });
    }

    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        final HttpUrl url = response.request().url();
        this.mDeliveryHandler.post(new Runnable() {
            public void run() {
                if (CommonUrlCallback.this.isGetCode) {
                    CommonUrlCallback.this.handleResponse(url);
                } else {
                    CommonUrlCallback.this.handleBodyResponse(result);
                }
            }
        });
    }

    private void handleResponse(HttpUrl url) {
        if (url != null) {
            this.mListener.onSuccess(url);
        } else {
            this.mListener.onFailure(null);
        }
    }

    private void handleBodyResponse(String body) {
        if (body != null) {
            this.mListener.onSuccess(body);
        } else {
            this.mListener.onFailure(null);
        }
    }
}
