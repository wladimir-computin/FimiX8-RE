package com.fimi.network.entity;

import java.io.Serializable;

public class AccessTokenEntity implements Serializable {
    private String access_token;
    private long expires_in;

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return this.expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }
}
