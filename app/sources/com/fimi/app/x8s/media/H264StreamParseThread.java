package com.fimi.app.x8s.media;

import java.util.concurrent.LinkedBlockingDeque;

public class H264StreamParseThread extends Thread {
    public LinkedBlockingDeque<byte[]> cmdQuene = new LinkedBlockingDeque();
    private boolean isWait;
    private IH264DataListener mH264DataListener;
    private boolean mStopFlag;

    public H264StreamParseThread(IH264DataListener mH264DataListener) {
        this.mH264DataListener = mH264DataListener;
    }

    public void run() {
        while (!this.mStopFlag) {
            if (this.cmdQuene.size() > 0) {
                this.mH264DataListener.onH264Frame((byte[]) this.cmdQuene.poll());
            } else {
                sendSignal();
            }
        }
    }

    public void notityParse(byte[] data) {
        this.cmdQuene.offer(data);
        sendSignal();
    }

    public void sendSignal() {
        synchronized (this.cmdQuene) {
            if (this.isWait) {
                this.isWait = false;
                this.cmdQuene.notify();
            } else {
                this.isWait = true;
                try {
                    this.cmdQuene.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void release() {
        this.mStopFlag = true;
        interrupt();
    }

    public int getFpvSize() {
        return this.cmdQuene.size();
    }
}
