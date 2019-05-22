package com.fimi.kernel.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.alibaba.fastjson.JSONObject;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import java.io.Serializable;

public class JsonMessage implements Serializable {
    private JSONObject jsonRt;
    private int msg_id;
    private JsonUiCallBackListener uiCallBackListener;

    public JsonMessage(int msgId, JSONObject jsonObject, JsonUiCallBackListener listener) {
        this.msg_id = msgId;
        this.jsonRt = jsonObject;
        this.uiCallBackListener = listener;
    }

    public int getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public JSONObject getJsonRt() {
        return this.jsonRt;
    }

    public void setJsonRt(JSONObject jsonRt) {
        this.jsonRt = jsonRt;
    }

    public JsonUiCallBackListener getUiCallBackListener() {
        return this.uiCallBackListener;
    }

    public void setUiCallBackListener(JsonUiCallBackListener uiCallBackListener) {
        this.uiCallBackListener = uiCallBackListener;
    }

    public String toString() {
        return "JsonMessage{msg_id=" + this.msg_id + ", jsonRt=" + this.jsonRt.toJSONString() + ", uiCallBackListener=" + this.uiCallBackListener + CoreConstants.CURLY_RIGHT;
    }
}
