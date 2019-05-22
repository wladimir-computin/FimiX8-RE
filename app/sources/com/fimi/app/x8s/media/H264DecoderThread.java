package com.fimi.app.x8s.media;

import android.util.Log;
import com.fimi.app.x8s.X8Application;
import java.util.concurrent.LinkedBlockingDeque;

public class H264DecoderThread extends Thread implements IH264DataListener {
    public LinkedBlockingDeque<byte[]> cmdQuene = new LinkedBlockingDeque();
    private int count;
    private boolean isCount;
    private boolean isWait = false;
    int lastSeq = -1;
    H264Decoder mDecoder;
    H264Frame mH264Frame = new H264Frame();
    private H264Player mH264Player;
    private IFrameDataListener mIFrameDataListener;
    private H264Packet mPacket = new H264Packet(new IH264DataListener() {
        public void onH264Frame(byte[] data) {
            if (H264DecoderThread.this.isCount) {
                H264DecoderThread.this.count = H264DecoderThread.this.count + 1;
                if (System.currentTimeMillis() - H264DecoderThread.this.time > 1000) {
                    H264DecoderThread.this.isCount = false;
                    if (H264DecoderThread.this.mIFrameDataListener != null) {
                        H264DecoderThread.this.mIFrameDataListener.onCountFrame(H264DecoderThread.this.count, 0, 0);
                    }
                    H264DecoderThread.this.count = 0;
                }
            } else {
                H264DecoderThread.this.isCount = true;
                H264DecoderThread.this.time = System.currentTimeMillis();
                H264DecoderThread.this.count = H264DecoderThread.this.count + 1;
            }
            H264DecoderThread.this.cmdQuene.offer(data);
        }
    });
    private boolean mStopFlag = false;
    int seq = 0;
    int startIndex = 0;
    private long time;
    long timeoutUs = 10000;

    public H264DecoderThread(H264Decoder h264Decoder, IFrameDataListener mIFrameDataListener) {
        this.mDecoder = h264Decoder;
        this.mIFrameDataListener = mIFrameDataListener;
        if (!X8Application.isLocalVideo) {
            this.startIndex = 14;
        }
    }

    public H264DecoderThread(H264Player mH264Player, IFrameDataListener mIFrameDataListener) {
        this.mH264Player = mH264Player;
        this.mIFrameDataListener = mIFrameDataListener;
        if (!X8Application.isLocalVideo) {
            this.startIndex = 14;
        }
    }

    public void run() {
        if (this.mDecoder != null) {
            androidDecode();
        }
        if (this.mH264Player != null) {
            ffmpegDecode();
        }
    }

    public void androidDecode() {
    }

    public void ffmpegDecode() {
        while (!this.mStopFlag) {
            if (!this.cmdQuene.isEmpty()) {
                byte[] data = (byte[]) this.cmdQuene.poll();
                if (data != null) {
                    this.mH264Player.decodeBuffer(data, data.length);
                }
            }
        }
    }

    public void notityDecode(byte[] data) {
        if (X8Application.Type2) {
            notityDecode12(data);
        } else {
            notityDecode1(data);
        }
    }

    private void notityDecode12(byte[] data) {
        this.mPacket.onPacket(data);
    }

    private void notityDecode1(byte[] data) {
        for (int i = this.startIndex; i < data.length; i++) {
            if (this.mH264Frame.parse(data[i])) {
                if (this.isCount) {
                    this.count++;
                    if (System.currentTimeMillis() - this.time > 1000) {
                        this.isCount = false;
                        if (this.mIFrameDataListener != null) {
                            this.mIFrameDataListener.onCountFrame(this.count, 0, 0);
                        }
                        this.count = 0;
                    }
                } else {
                    this.isCount = true;
                    this.time = System.currentTimeMillis();
                    this.count++;
                }
                this.cmdQuene.offer(this.mH264Frame.getDataBuf());
            }
        }
    }

    public void packetData(byte[] data) {
        if (data.length > 14 && data[0] == Byte.MIN_VALUE) {
            this.seq = ((data[2] & 255) << 8) | (data[3] & 255);
            if (this.lastSeq == -1) {
                if ((data[12] & 255) == 124 && data[14] == (byte) 0 && data[15] == (byte) 0 && data[16] == (byte) 0 && data[17] == (byte) 1) {
                    this.mH264Frame.setData(data, 14, data.length - 14);
                    this.lastSeq = this.seq;
                }
            } else if (this.seq - this.lastSeq > 1) {
                this.lastSeq = this.seq;
                this.mH264Frame.resetIndex();
            } else if ((data[12] & 255) != 124) {
                this.mH264Frame.setData(data, 14, data.length - 14);
                this.lastSeq = this.seq;
            } else if (data[14] == (byte) 0 && data[15] == (byte) 0 && data[16] == (byte) 0 && data[17] == (byte) 1) {
                this.cmdQuene.offer(this.mH264Frame.getDataBuf());
                this.mH264Frame.setData(data, 14, data.length - 14);
                if (this.seq == 32767) {
                    this.lastSeq = -1;
                } else {
                    this.lastSeq = this.seq;
                }
            }
        }
    }

    public void decode(byte[] data) {
        this.cmdQuene.offer(data);
    }

    public void sendSignal() {
        synchronized (this.cmdQuene) {
            if (this.isWait) {
                Log.i("istep", "sendSignal notify " + this.cmdQuene.size());
                if (this.cmdQuene.size() > 10) {
                    Log.i("zdy", "sendSignal notify");
                    this.isWait = false;
                    this.cmdQuene.notify();
                } else if (this.cmdQuene.size() == 0) {
                    try {
                        Log.i("zdy", "sendSignal wait");
                        this.cmdQuene.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                this.isWait = true;
                try {
                    Log.i("zdy", "sendSignal wait");
                    this.cmdQuene.wait();
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return;
    }

    public void release() {
        this.mStopFlag = true;
        interrupt();
    }

    public void onH264Frame(byte[] data) {
        decode(data);
    }
}
