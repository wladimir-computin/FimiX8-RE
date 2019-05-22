package com.fimi.app.x8s.media;

public class H264Frame {
    private byte[] data = new byte[1048576];
    private int index;
    private boolean isFirstFrame;
    private boolean isFrame;
    private State state = State.a1;

    enum State {
        a1,
        a2,
        a3,
        B1,
        pload,
        a4,
        a5,
        a6,
        B2
    }

    public void onSeqErrorReset() {
        if (this.isFirstFrame) {
            this.state = State.pload;
            this.index = 0;
            return;
        }
        reset();
    }

    private void reset() {
        this.state = State.a1;
        this.index = 0;
    }

    public byte[] getDataBuf() {
        this.state = State.pload;
        byte[] buf;
        if (this.isFirstFrame) {
            buf = new byte[this.index];
            buf[3] = (byte) 1;
            System.arraycopy(this.data, 0, buf, 4, this.index - 4);
            this.index = 0;
            return buf;
        }
        this.isFirstFrame = true;
        buf = new byte[(this.index - 4)];
        System.arraycopy(this.data, 0, buf, 0, this.index - 4);
        this.index = 0;
        return buf;
    }

    public boolean parse(byte b) {
        this.isFrame = false;
        byte[] bArr = this.data;
        int i = this.index;
        this.index = i + 1;
        bArr[i] = b;
        switch (this.state) {
            case a1:
                if (b != (byte) 0) {
                    reset();
                    break;
                }
                this.state = State.a2;
                break;
            case a2:
                if (b != (byte) 0) {
                    reset();
                    break;
                }
                this.state = State.a3;
                break;
            case a3:
                if (b != (byte) 0) {
                    reset();
                    break;
                }
                this.state = State.B1;
                break;
            case B1:
                if (b != (byte) 1) {
                    reset();
                    break;
                }
                this.state = State.pload;
                break;
            case pload:
                if (b == (byte) 0) {
                    this.state = State.a4;
                    break;
                }
                break;
            case a4:
                if (b != (byte) 0) {
                    this.state = State.pload;
                    break;
                }
                this.state = State.a5;
                break;
            case a5:
                if (b != (byte) 0) {
                    this.state = State.pload;
                    break;
                }
                this.state = State.a6;
                break;
            case a6:
                if (b != (byte) 1) {
                    this.state = State.pload;
                    break;
                }
                this.isFrame = true;
                break;
        }
        return this.isFrame;
    }

    public void setData(byte[] buffer, int start, int len) {
        System.arraycopy(buffer, start, this.data, this.index, len);
        this.index += len;
    }

    public void resetIndex() {
        this.index = 0;
    }

    public byte[] getDataBuf2() {
        byte[] buf = new byte[this.index];
        System.arraycopy(this.data, 0, buf, 0, this.index);
        this.index = 0;
        return buf;
    }
}
