package com.fimi.kernel.network.okhttp.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class RequestParams {
    public ConcurrentHashMap<String, Object> fileParams;
    public ConcurrentHashMap<String, String> urlParams;

    public RequestParams() {
        this((Map) null);
    }

    public RequestParams(Map<String, String> source) {
        this.urlParams = new ConcurrentHashMap();
        this.fileParams = new ConcurrentHashMap();
        if (source != null) {
            for (Entry<String, String> entry : source.entrySet()) {
                put((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {
        });
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            this.urlParams.put(key, value);
        }
    }

    public void put(String key, Object object) throws FileNotFoundException {
        if (key != null) {
            this.fileParams.put(key, object);
        }
    }

    public boolean hasParams() {
        if (this.urlParams.size() > 0 || this.fileParams.size() > 0) {
            return true;
        }
        return false;
    }
}
