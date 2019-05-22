package com.fimi.album.entity;

import com.fimi.album.adapter.BodyRecycleViewHolder;
import java.io.Serializable;

public class VideoDurationTion implements Serializable {
    String formateVideoTime;
    BodyRecycleViewHolder holder;
    MediaModel mediaModel;
    private int position;

    public VideoDurationTion(BodyRecycleViewHolder holder, MediaModel mediaModel, int position) {
        this.holder = holder;
        this.mediaModel = mediaModel;
        this.position = position;
    }

    public BodyRecycleViewHolder getHolder() {
        return this.holder;
    }

    public void setHolder(BodyRecycleViewHolder holder) {
        this.holder = holder;
    }

    public MediaModel getMediaModel() {
        return this.mediaModel;
    }

    public void setMediaModel(MediaModel mediaModel) {
        this.mediaModel = mediaModel;
    }

    public String getFormateVideoTime() {
        return this.formateVideoTime;
    }

    public void setFormateVideoTime(String formateVideoTime) {
        this.formateVideoTime = formateVideoTime;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
