package com.fimi.kernel.connect;

public abstract class BaseConnect {
    private String clientAddress;
    private String clientName;

    public enum DeviceConnectState {
        IDEL,
        CONNECTED,
        DISCONNECT
    }

    public abstract void closeSession();

    public abstract boolean isDeviceConnected();

    public abstract void sendCmd(BaseCommand baseCommand);

    public abstract void sendJsonCmd(BaseCommand baseCommand);

    public abstract void startSession();

    public void sendTimeCmd(BaseCommand cmd) {
    }

    public void sendLog(String msg) {
    }

    public void receiveLog(String msg) {
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }
}
