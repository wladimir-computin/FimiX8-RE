package com.fimi.app.x8s.media;

import android.os.Handler;
import android.os.Message;
import android.view.Surface;
import com.fimi.media.FimiMediaPlayer;

public class H264Player {
    private static final int FPS = 60;
    private static final int VIDEO_HEIGHT = 720;
    private static final int VIDEO_WIDTH = 1280;
    private int count;
    private int currentStartIndex;
    private Thread mDecodeThread;
    private FimiMediaPlayer mFimiMediaPlayer;
    private H264StreamParseThread mH264StreamParseThread;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    H264Player.this.countFps();
                    H264Player.this.mHandler.sendEmptyMessageDelayed(0, 1000);
                    return;
                default:
                    return;
            }
        }
    };
    private IFrameDataListener mIFrameDataListener;
    private boolean mStopFlag;

    public H264Player(IFrameDataListener listener) {
        this.mIFrameDataListener = listener;
        this.mFimiMediaPlayer = new FimiMediaPlayer();
        this.mFimiMediaPlayer.enableLog(0);
        this.mFimiMediaPlayer.displayVersion();
        this.mFimiMediaPlayer.initDecoder(1280, 720, 60);
    }

    public void setSurface(Surface surface) {
        this.mFimiMediaPlayer.setSurfaceView(surface);
    }

    public void decodeBuffer(byte[] data, int length) {
        this.mFimiMediaPlayer.decodeBuffer(data, length);
    }

    public void deInitDecode() {
        this.mFimiMediaPlayer.deInitDecode();
    }

    public void start() {
        this.mFimiMediaPlayer.setMediaDecType(0);
        this.mFimiMediaPlayer.setTddMode(1);
        this.mFimiMediaPlayer.start();
        this.count = this.mFimiMediaPlayer.getFpsIndex();
        this.mH264StreamParseThread = new H264StreamParseThread(new IH264DataListener() {
            public void onH264Frame(byte[] data) {
                H264Player.this.mFimiMediaPlayer.addBufferData(data, 0, data.length);
            }
        });
        this.mH264StreamParseThread.start();
        this.mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    public void stop() {
        this.mFimiMediaPlayer.stop();
        this.mHandler.removeMessages(0);
        if (this.mH264StreamParseThread != null) {
            this.mH264StreamParseThread.release();
        }
    }

    public void addBufferData(byte[] data, int start, int length) {
        if (this.mH264StreamParseThread != null) {
            this.mH264StreamParseThread.notityParse(data);
        }
    }

    public void countFps() {
        if (this.mIFrameDataListener != null) {
            int c = this.mFimiMediaPlayer.getFpsIndex();
            this.mIFrameDataListener.onCountFrame(c - this.count, this.mFimiMediaPlayer.getRemainder(), this.mH264StreamParseThread.getFpvSize());
            this.count = c;
        }
    }

    public void setX8VideoFrameBufferListener(OnX8VideoFrameBufferListener x8VideoFrameBufferListener) {
        this.mFimiMediaPlayer.setX8VideoFrameBufferListener(x8VideoFrameBufferListener);
    }

    public void setEnableCallback(int enbale) {
        this.mFimiMediaPlayer.setEnableCallback(enbale);
    }

    public int getLostSeq() {
        return this.mFimiMediaPlayer.getH264FrameLostSeq();
    }
}
