package com.fimi.x8sdk.connect.usb;

public class USBConnStatusManager {
    private static USBConnStatusManager statusManager = new USBConnStatusManager();
    UsbStatus status = UsbStatus.UnConnect;

    public enum UsbStatus {
        UnConnect,
        Attach,
        Dettached
    }

    public static USBConnStatusManager getInstance() {
        return statusManager;
    }

    private USBConnStatusManager() {
    }

    public UsbStatus getUsbStatus() {
        return this.status;
    }

    public void setStatus(UsbStatus status) {
        this.status = status;
    }
}
