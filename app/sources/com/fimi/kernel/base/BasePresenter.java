package com.fimi.kernel.base;

public abstract class BasePresenter<T> {
    public T mView;

    public void attachVM(T v) {
        this.mView = v;
    }

    public void detachVM() {
        this.mView = null;
    }
}
