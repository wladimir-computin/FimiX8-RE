package com.fimi.x8sdk.entity;

import ch.qos.logback.core.CoreConstants;
import com.fimi.x8sdk.modulestate.StateManager;

public class ConectState {
    private boolean isCameraConnect;
    private boolean isConnectDrone;
    private boolean isConnectRelay;

    public boolean isCameraConnect() {
        return this.isCameraConnect;
    }

    public void setCameraConnect(boolean cameraConnect) {
        this.isCameraConnect = cameraConnect;
    }

    public boolean isConnectDrone() {
        return this.isConnectDrone;
    }

    public void setConnectDrone(boolean connectDrone) {
        if (!(connectDrone || this.isConnectDrone == connectDrone)) {
            StateManager.getInstance().getVersionState().setModuleFcAckVersion(null);
            StateManager.getInstance().getX8Drone().clearState();
        }
        this.isConnectDrone = connectDrone;
    }

    public boolean isConnectRelay() {
        return this.isConnectRelay;
    }

    public void setConnectRelay(boolean connectRelay) {
        if (!(connectRelay || this.isConnectRelay == connectRelay)) {
            StateManager.getInstance().getVersionState().clearVersion();
        }
        this.isConnectRelay = connectRelay;
    }

    public String toString() {
        return "ConectState{isConnectDrone=" + this.isConnectDrone + ", isConnectRelay=" + this.isConnectRelay + ", isCameraConnect=" + this.isCameraConnect + CoreConstants.CURLY_RIGHT;
    }
}
