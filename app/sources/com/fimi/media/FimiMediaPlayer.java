package com.fimi.media;

import android.os.Handler;
import android.os.Message;
import com.fimi.app.x8s.media.OnX8VideoFrameBufferListener;
import java.nio.ByteBuffer;

public class FimiMediaPlayer {
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    synchronized (FimiMediaPlayer.object) {
                        if (FimiMediaPlayer.x8VideoFrameBufferListener != null) {
                            FimiMediaPlayer.x8VideoFrameBufferListener.onFrameBuffer((byte[]) msg.obj);
                        }
                    }
                    return;
                case 1:
                    synchronized (FimiMediaPlayer.object) {
                        if (FimiMediaPlayer.x8VideoFrameBufferListener != null) {
                            FimiMediaPlayer.x8VideoFrameBufferListener.onH264Frame((ByteBuffer) msg.obj);
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private static Object object = new Object();
    static int r1;
    static long time;
    static int w1;
    private static OnX8VideoFrameBufferListener x8VideoFrameBufferListener;

    public native void addBufferData(byte[] bArr, int i, int i2);

    public native void deInitDecode();

    public native int decodeBuffer(byte[] bArr, int i);

    public native void displayVersion();

    public native void enableLog(int i);

    public native int getFpsIndex();

    public native int getH264FrameLostSeq();

    public native int getRemainder();

    public native int initDecoder(int i, int i2, int i3);

    public native void setEnableCallback(int i);

    public native void setMediaDecType(int i);

    public native void setSurfaceView(Object obj);

    public native void setTddMode(int i);

    public native int start();

    public native void stop();

    public FimiMediaPlayer() {
        System.loadLibrary("ijkffmpeg");
        System.loadLibrary("h264decode");
    }

    public static void showLog(int w, int r, int m) {
    }

    public static long getJavaCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void onFrameBuffer(byte[] data, int type) {
        if (data == null) {
            return;
        }
        if (type == 1) {
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            mHandler.obtainMessage(1, buffer).sendToTarget();
        } else if (type == 0) {
            mHandler.obtainMessage(0, data).sendToTarget();
        }
    }

    public synchronized void setX8VideoFrameBufferListener(OnX8VideoFrameBufferListener listener) {
        synchronized (object) {
            x8VideoFrameBufferListener = listener;
        }
    }
}
