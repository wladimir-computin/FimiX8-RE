package com.fimi.app.x8s.media;

import android.util.Log;
import com.fimi.x8sdk.command.FcCollection;

public class H264Packet {
    int MAX_SHORT = 65535;
    byte[] h264Frame = new byte[1048576];
    private IH264DataListener ih264DataListener;
    int index = 0;
    int lastSeq = -1;
    int len = 0;
    private int seq;

    public H264Packet(IH264DataListener ih264DataListener) {
        this.ih264DataListener = ih264DataListener;
    }

    public void onPacket(byte[] data) {
        this.len = data.length;
        if (this.len >= 14) {
            this.seq = ((data[2] & 255) << 8) | (data[3] & 255);
            byte[] buffer;
            if (this.lastSeq == -1) {
                if (data[12] == (byte) 124 && data[18] == (byte) 39) {
                    System.arraycopy(data, 14, this.h264Frame, this.index, this.len - 14);
                    this.index += this.len - 14;
                    this.lastSeq = this.seq;
                }
            } else if (this.seq - this.lastSeq > 1) {
                Log.i("zdy", "error frame" + this.lastSeq + " " + this.seq);
                System.arraycopy(data, 14, this.h264Frame, this.index, this.len - 14);
                buffer = new byte[this.index];
                System.arraycopy(this.h264Frame, 0, buffer, 0, this.index);
                Log.i("zdy", "fram =" + buffer.length);
                this.lastSeq = this.seq;
                this.index = 0;
            } else {
                if (this.index != 0) {
                    if (data[12] == (byte) 124 && data[13] != FcCollection.MSG_SET_SURROUND_EXCUTE && data[18] == (byte) 39) {
                        buffer = new byte[this.index];
                        System.arraycopy(this.h264Frame, 0, buffer, 0, this.index);
                        Log.i("zdy", "fram =" + buffer.length);
                        this.ih264DataListener.onH264Frame(buffer);
                        this.index = 0;
                    }
                    System.arraycopy(data, 14, this.h264Frame, this.index, this.len - 14);
                    this.index += this.len - 14;
                } else if (data[12] == (byte) 124 && data[18] == (byte) 39) {
                    System.arraycopy(data, 14, this.h264Frame, this.index, this.len - 14);
                    this.index += this.len - 14;
                    Log.i("zdy", "ccccc frame");
                }
                this.lastSeq = this.seq;
            }
        }
    }
}
