package com.fimi.x8sdk.controller;

import com.fimi.kernel.connect.session.VideodDataListener;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.presenter.VideoPresenter;

public class VideoManager extends BasePresenter {
    VideoPresenter presenter;

    public VideoManager(VideodDataListener listener) {
        this.presenter = new VideoPresenter(listener);
    }

    public void removeVideoDataListener() {
        if (this.presenter != null) {
            this.presenter.removeVideaListener();
            this.presenter = null;
        }
    }
}
