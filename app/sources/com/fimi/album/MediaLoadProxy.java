package com.fimi.album;

import com.fimi.album.download.interfaces.IMediaFileLoad;

public class MediaLoadProxy implements IMediaFileLoad {
    IMediaFileLoad load;

    public void setMediaLoad(IMediaFileLoad load) {
        this.load = load;
    }

    public void startLoad() {
        this.load.startLoad();
    }

    public void stopLoad() {
        this.load.stopLoad();
    }
}
