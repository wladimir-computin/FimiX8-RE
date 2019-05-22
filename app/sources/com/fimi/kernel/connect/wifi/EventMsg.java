package com.fimi.kernel.connect.wifi;

public abstract class EventMsg {
    private Object msg_content;
    private int msg_id;
    private msgType type;

    public enum msgType {
        error,
        warn,
        notify,
        normal
    }

    public int getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public msgType getType() {
        return this.type;
    }

    public void setType(msgType type) {
        this.type = type;
    }

    public Object getMsg_content() {
        return this.msg_content;
    }

    public void setMsg_content(Object msg_content) {
        this.msg_content = msg_content;
    }
}
