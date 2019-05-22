package com.fimi.kernel.base;

public class EventMessage<T> {
    private String key;
    private T message;

    public EventMessage(String key, T message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getMessage() {
        return this.message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
