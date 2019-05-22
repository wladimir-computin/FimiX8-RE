package com.fimi.x8sdk.connect;

import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.entity.ConectState;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.rtp.X8Rtp;

public class DeviceMonitorThread extends Thread {
    ConectState conectState = new ConectState();
    boolean isConnectDrone;
    boolean isConnectRelay;
    public boolean isLoop = true;

    public void run() {
        super.run();
        while (this.isLoop) {
            AckVersion fcVersion;
            if (!X8Rtp.simulationTest) {
                if (StateManager.getInstance().getX8Drone().isFcTimeOut()) {
                    this.isConnectDrone = false;
                } else {
                    fcVersion = StateManager.getInstance().getX8Drone().getVersion();
                    if (fcVersion != null) {
                        this.isConnectDrone = fcVersion.getSoftVersion() > 0;
                    } else {
                        this.isConnectDrone = false;
                    }
                }
                if (StateManager.getInstance().getRelayState().isRlTimeOut()) {
                    this.isConnectRelay = false;
                } else {
                    AckVersion relayVer = StateManager.getInstance().getRelayState().getRelayVersion();
                    if (relayVer == null) {
                        this.isConnectRelay = false;
                    } else {
                        this.isConnectRelay = relayVer.getSoftVersion() > 0;
                    }
                }
            } else if (StateManager.getInstance().getX8Drone().isFcTimeOut()) {
                this.isConnectDrone = false;
                this.isConnectRelay = false;
            } else {
                fcVersion = StateManager.getInstance().getX8Drone().getVersion();
                if (fcVersion != null) {
                    boolean z = fcVersion.getSoftVersion() > 0;
                    this.isConnectDrone = z;
                    this.isConnectRelay = z;
                } else {
                    this.isConnectDrone = false;
                    this.isConnectRelay = false;
                }
            }
            this.conectState.setConnectRelay(this.isConnectRelay);
            this.conectState.setConnectDrone(this.isConnectDrone);
            StateManager.getInstance().onConnectState(this.conectState);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void exit() {
        this.isLoop = false;
        interrupt();
    }
}
