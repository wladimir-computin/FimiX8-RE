package com.fimi.kernel.network.okhttp.listener;

public class DisposeDataHandle {
    public static boolean isStop = false;
    public boolean isArray = false;
    public Class<?> mClass = null;
    public DisposeDataListener mListener = null;
    public String mSource = null;

    public DisposeDataHandle(DisposeDataListener listener) {
        this.mListener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz) {
        this.mListener = listener;
        this.mClass = clazz;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz, boolean isArray) {
        this.mListener = listener;
        this.mClass = clazz;
        this.isArray = isArray;
    }

    public DisposeDataHandle(DisposeDataListener listener, String source) {
        this.mListener = listener;
        this.mSource = source;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }
}
