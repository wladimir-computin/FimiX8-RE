package com.fimi.app.x8s.media;

import android.view.Surface;

public class H264Decoder {
    private boolean isWorking;
    private H264DecoderThread mH264DecoderThread;
    private H264Player mH264Player;
    private H264StreamParseThread mH264StreamParseThread;

    public H264Player getmH264Player() {
        return this.mH264Player;
    }

    public void stopThread() {
        release();
    }

    public void release() {
        this.isWorking = false;
        if (this.mH264DecoderThread != null) {
            this.mH264DecoderThread.release();
            this.mH264DecoderThread = null;
        }
        if (this.mH264Player != null) {
            this.mH264Player.stop();
        }
    }

    public void startWorkThread(Surface surface, int width, int height, int frameRate, IFrameDataListener listener) {
        this.mH264DecoderThread = new H264DecoderThread(this, listener);
        this.mH264DecoderThread.start();
        this.isWorking = true;
    }

    public void setH264StreamData(byte[] data) {
        if (this.mH264DecoderThread != null && this.isWorking) {
            this.mH264DecoderThread.notityDecode(data);
        }
        if (this.mH264Player != null && this.isWorking) {
            this.mH264Player.addBufferData(data, 0, data.length);
        }
    }

    public void startJniWorkThread(Surface surface, int width, int height, int frameRate, IFrameDataListener listener) {
        if (this.mH264Player == null) {
            this.mH264Player = new H264Player(listener);
        }
        this.mH264Player.setSurface(surface);
        this.mH264DecoderThread = new H264DecoderThread(this.mH264Player, listener);
        this.mH264DecoderThread.start();
        this.isWorking = true;
    }

    public void startJniThread(Surface surface, int width, int height, int frameRate, IFrameDataListener listener) {
        if (this.mH264Player == null) {
            this.mH264Player = new H264Player(listener);
        }
        this.mH264Player.setSurface(surface);
        this.mH264Player.start();
        this.isWorking = true;
    }

    public void onDestroy() {
        if (this.mH264Player != null) {
            this.mH264Player.deInitDecode();
        }
    }

    public void setX8VideoFrameBufferListener(OnX8VideoFrameBufferListener listener) {
        this.mH264Player.setX8VideoFrameBufferListener(listener);
    }

    public void setEnableCallback(int enbale) {
        this.mH264Player.setEnableCallback(enbale);
    }
}
