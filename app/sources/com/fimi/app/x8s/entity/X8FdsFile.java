package com.fimi.app.x8s.entity;

import com.fimi.kernel.fds.FdsUploadState;
import com.fimi.kernel.fds.IFdsFileModel;

public abstract class X8FdsFile implements IFdsFileModel {
    protected String fileSuffix = "";
    protected String filefdsUrl = "";
    private int itemPostion;
    protected String objectName;
    private int sectionPostion;
    protected FdsUploadState state = FdsUploadState.IDLE;

    public int getSectionPostion() {
        return this.sectionPostion;
    }

    public void setSectionPostion(int sectionPostion) {
        this.sectionPostion = sectionPostion;
    }

    public int getItemPostion() {
        return this.itemPostion;
    }

    public void setItemPostion(int itemPostion) {
        this.itemPostion = itemPostion;
    }
}
