package com.fimi.player;

import android.graphics.SurfaceTexture;

public interface ISurfaceTextureHolder {
    SurfaceTexture getSurfaceTexture();

    void setSurfaceTexture(SurfaceTexture surfaceTexture);
}
