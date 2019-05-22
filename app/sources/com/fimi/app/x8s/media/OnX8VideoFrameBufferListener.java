package com.fimi.app.x8s.media;

import java.nio.ByteBuffer;

public interface OnX8VideoFrameBufferListener {
    void onFrameBuffer(byte[] bArr);

    void onH264Frame(ByteBuffer byteBuffer);
}
