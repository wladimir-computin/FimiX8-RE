package com.fimi.x8sdk.entity;

import java.io.Serializable;

public class CameraSystemState implements Serializable {
    private String awbVal;
    private String evVal;
    private String isoVal;
    private String shutterVal;

    public String getEvVal() {
        return this.evVal;
    }

    public void setEvVal(String evVal) {
        this.evVal = evVal;
    }

    public String getIsoVal() {
        return this.isoVal;
    }

    public void setIsoVal(String isoVal) {
        this.isoVal = isoVal;
    }

    public String getShutterVal() {
        return this.shutterVal;
    }

    public void setShutterVal(String shutterVal) {
        this.shutterVal = shutterVal;
    }

    public String getAwbVal() {
        return this.awbVal;
    }

    public void setAwbVal(String awbVal) {
        this.awbVal = awbVal;
    }
}
