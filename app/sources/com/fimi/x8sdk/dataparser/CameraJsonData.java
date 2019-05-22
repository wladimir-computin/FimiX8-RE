package com.fimi.x8sdk.dataparser;

import java.io.Serializable;

public class CameraJsonData implements Serializable {
    private int fetch_size;
    private String md5sum;
    private int msg_id = 0;
    private int offset;
    private String[] options;
    private String param;
    private String rem_size;
    private long size = 0;
    private int token = 0;
    private String type;

    public int getToken() {
        return this.token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParam() {
        return this.param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String[] getOptions() {
        return this.options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getRem_size() {
        return this.rem_size;
    }

    public void setRem_size(String rem_size) {
        this.rem_size = rem_size;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getFetch_size() {
        return this.fetch_size;
    }

    public void setFetch_size(int fetch_size) {
        this.fetch_size = fetch_size;
    }

    public String getMd5sum() {
        return this.md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
