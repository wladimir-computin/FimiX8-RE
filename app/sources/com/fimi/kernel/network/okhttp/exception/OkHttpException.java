package com.fimi.kernel.network.okhttp.exception;

public class OkHttpException extends Exception {
    private static final long serialVersionUID = 1;
    private int ecode;
    private Object emsg;

    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return this.ecode;
    }

    public Object getEmsg() {
        return this.emsg;
    }
}
