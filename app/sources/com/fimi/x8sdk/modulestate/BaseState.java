package com.fimi.x8sdk.modulestate;

public abstract class BaseState {
    public final int LOGIN_STATE_CONNECT = 1;
    public final int LOGIN_STATE_DISCONNECT = 2;
    public final int LOGIN_STATE_IDLE = 0;
    private int hwVerion;
    private int loginState;
    private int model;
    private int softVerion = -1;
    private int type;

    public abstract boolean isAvailable();

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getModel() {
        return this.model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getSoftVerion() {
        return this.softVerion;
    }

    public void setSoftVerion(int softVerion) {
        this.softVerion = softVerion;
    }

    public int getHwVerion() {
        return this.hwVerion;
    }

    public void setHwVerion(int hwVerion) {
        this.hwVerion = hwVerion;
    }

    public int getLoginState() {
        return this.loginState;
    }

    public void setLoginState(int loginState) {
        this.loginState = loginState;
    }
}
