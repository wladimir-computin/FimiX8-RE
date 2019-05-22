package com.fimi.x8sdk.update.fwpack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PkgDetl implements Serializable {
    private static final long serialVersionUID = 1;
    private List<FwInfo> fws = new ArrayList();
    private int id1;
    private short mainVer;
    private short suberVer;

    public List<FwInfo> getFws() {
        return this.fws;
    }

    public void setFws(List<FwInfo> fws) {
        this.fws = fws;
    }

    public short getMainVer() {
        return this.mainVer;
    }

    public void setMainVer(short mainVer) {
        this.mainVer = mainVer;
    }

    public int getId1() {
        return this.id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public short getSuberVer() {
        return this.suberVer;
    }

    public void setSuberVer(short suberVer) {
        this.suberVer = suberVer;
    }

    public String toString() {
        return "PkgDetl [mainVer=" + this.mainVer + ", suberVer=" + this.suberVer + ", id1=" + this.id1 + ", fws=" + this.fws + "]";
    }
}
