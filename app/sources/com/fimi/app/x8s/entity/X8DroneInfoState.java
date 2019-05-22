package com.fimi.app.x8s.entity;

import com.fimi.app.x8s.controls.fcsettting.X8DroneInfoStateController.Mode;
import com.fimi.app.x8s.controls.fcsettting.X8DroneInfoStateController.State;

public class X8DroneInfoState {
    private int errorEvent;
    private String info;
    private Mode mode;
    private String name;
    private State state;

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getErrorEvent() {
        return this.errorEvent;
    }

    public void setErrorEvent(int errorEvent) {
        this.errorEvent = errorEvent;
    }
}
