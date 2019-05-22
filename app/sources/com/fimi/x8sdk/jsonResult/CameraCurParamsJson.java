package com.fimi.x8sdk.jsonResult;

import ch.qos.logback.core.CoreConstants;
import java.io.Serializable;
import java.util.List;

public class CameraCurParamsJson implements Serializable {
    private int msg_id;
    private List<CurParamsJson> param;
    private int rval;

    public int getRval() {
        return this.rval;
    }

    public void setRval(int rval) {
        this.rval = rval;
    }

    public int getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public List<CurParamsJson> getParam() {
        return this.param;
    }

    public void setParam(List<CurParamsJson> param) {
        this.param = param;
    }

    public String toString() {
        return "CameraCurParamsJson{rval=" + this.rval + ", msg_id=" + this.msg_id + ", param=" + this.param + CoreConstants.CURLY_RIGHT;
    }
}
