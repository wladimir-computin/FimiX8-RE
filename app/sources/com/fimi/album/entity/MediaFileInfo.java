package com.fimi.album.entity;

public class MediaFileInfo {
    private String attr;
    private String gentime;
    private String md5;
    private String name;
    private String size;
    private String thumbsize;
    private String time;

    public String getThumbsize() {
        return this.thumbsize;
    }

    public void setThumbsize(String thumbsize) {
        this.thumbsize = thumbsize;
    }

    public String getGentime() {
        return this.gentime;
    }

    public void setGentime(String gentime) {
        this.gentime = gentime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAttr() {
        return this.attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String toString() {
        return this.name;
    }
}
