package com.fimi.x8sdk.connect.usb;

import com.fimi.x8sdk.connect.usb.USBConnStatusManager.UsbStatus;

public interface UsbStatusListener {
    void notifyUsbStatusChanged(UsbStatus usbStatus);
}
