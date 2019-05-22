package com.fimi.kernel.store.sqlite.entity;

public class MediaDownloadInfo {
    private long compeleteZize;
    private long endPos;
    public Long id;
    private long startPos;
    private String url;

    public MediaDownloadInfo(Long id, long startPos, long endPos, long compeleteZize, String url) {
        this.id = id;
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteZize = compeleteZize;
        this.url = url;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStartPos() {
        return this.startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getEndPos() {
        return this.endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    public long getCompeleteZize() {
        return this.compeleteZize;
    }

    public void setCompeleteZize(long compeleteZize) {
        this.compeleteZize = compeleteZize;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
