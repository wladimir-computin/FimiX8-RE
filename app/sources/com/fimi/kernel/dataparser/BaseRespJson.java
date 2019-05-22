package com.fimi.kernel.dataparser;

public class BaseRespJson {
    public static final int ERROR_APP_NOT_READY = -22;
    public static final int ERROR_CARD_PROTECTED = -18;
    public static final int ERROR_HDMI_INSERTED = -16;
    public static final int ERROR_INVALID_OPERATION = -14;
    public static final int ERROR_INVALID_OPTION_VALUE = -13;
    public static final int ERROR_INVALID_PARAM = -25;
    public static final int ERROR_INVALID_PATH = -26;
    public static final int ERROR_INVALID_TOKEN = -4;
    public static final int ERROR_INVALID_TYPE = -24;
    public static final int ERROR_JSON_PACKAGE_ERROR = -7;
    public static final int ERROR_JSON_PACKAGE_TIMEOUT = -8;
    public static final int ERROR_JSON_SYNTAX_ERROR = -9;
    public static final int ERROR_NO_MORE_MEMORY = -19;
    public static final int ERROR_NO_MORE_SPACE = -17;
    public static final int ERROR_OPERATION_UNSUPPORTED = -23;
    public static final int ERROR_PIV_NOT_ALLOWED = -20;
    public static final int ERROR_REACH_MAX_CLNT = -5;
    public static final int ERROR_SESSION_START_FAIL = -3;
    public static final int ERROR_SYSTEM_BUSY = -21;
    public static final int ERROR_UNKNOWN_ERROR = -1;
    protected int msg_id;
    protected String param;
    protected int rval;
    protected String type;

    public String getParam() {
        return this.param;
    }

    public void setParam(String param) {
        this.param = param;
    }

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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getErrorMsg() {
        switch (this.rval) {
            case -26:
                return "ERROR_INVALID_PATH";
            case -25:
                return "ERROR_INVALID_PARAM";
            case -24:
                return "ERROR_INVALID_TYPE";
            case -23:
                return "ERROR_OPERATION_UNSUPPORTED";
            case -22:
                return "ERROR_APP_NOT_READY";
            case -21:
                return "ERROR_SYSTEM_BUSY";
            case -20:
                return "ERROR_PIV_NOT_ALLOWED";
            case -19:
                return "ERROR_NO_MORE_MEMORY";
            case -18:
                return "ERROR_CARD_PROTECTED";
            case -17:
                return "ERROR_NO_MORE_SPACE";
            case -16:
                return "ERROR_HDMI_INSERTED";
            case -14:
                return "ERROR_INVALID_OPERATION";
            case -13:
                return "ERROR_INVALID_OPTION_VALUE";
            case -9:
                return "ERROR_JSON_SYNTAX_ERROR";
            case -8:
                return "ERROR_JSON_PACKAGE_TIMEOUT";
            case -7:
                return "ERROR_JSON_PACKAGE_ERROR";
            case -5:
                return "ERROR_REACH_MAX_CLNT";
            case -4:
                return "ERROR_INVALID_TOKEN";
            case -3:
                return "ERROR_SESSION_START_FAIL";
            case -1:
                return "ERROR_UNKNOWN_ERROR";
            default:
                return "";
        }
    }
}
