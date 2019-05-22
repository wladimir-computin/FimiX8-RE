package com.fimi.app.x8s.entity;

import ch.qos.logback.core.CoreConstants;

public class X8AilinePrameter {
    private float DEFAULE_SPEED = 2.0f;
    private int autoRecorde;
    private int disconnectActioin;
    private int endAction;
    private int orientation;
    private float speed = this.DEFAULE_SPEED;

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getDisconnectActioin() {
        return this.disconnectActioin;
    }

    public void setDisconnectActioin(int disconnectActioin) {
        this.disconnectActioin = disconnectActioin;
    }

    public int getEndAction() {
        return this.endAction;
    }

    public void setEndAction(int endAction) {
        this.endAction = endAction;
    }

    public int getAutoRecorde() {
        return this.autoRecorde;
    }

    public void setAutoRecorde(int autoRecorde) {
        this.autoRecorde = autoRecorde;
    }

    public String toString() {
        return "X8AilinePrameter{DEFAULE_SPEED=" + this.DEFAULE_SPEED + ", speed=" + this.speed + ", orientation=" + this.orientation + ", disconnectActioin=" + this.disconnectActioin + ", endAction=" + this.endAction + ", autoRecorde=" + this.autoRecorde + CoreConstants.CURLY_RIGHT;
    }
}
