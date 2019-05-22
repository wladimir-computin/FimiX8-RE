package com.fimi.x8sdk.connect.usb;

import android.content.Context;
import android.hardware.usb.UsbAccessory;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.connect.usb.AOAConnect;
import com.fimi.kernel.connect.usb.IUSBStatusListener;
import com.fimi.x8sdk.connect.DataChanel;
import com.fimi.x8sdk.connect.IConnectHandler;

public class UsbConnectThread extends Thread implements IConnectHandler {
    UsbAccessory accessory;
    AOAConnect aoaConnect;
    DataChanel dataChanel = new DataChanel();
    Context mContext;
    private IUSBStatusListener mIAoaConnectListener;

    public UsbConnectThread(Context mContext, UsbAccessory accessory, IUSBStatusListener mIAoaConnectListener) {
        this.mContext = mContext;
        this.accessory = accessory;
        this.mIAoaConnectListener = mIAoaConnectListener;
        start();
    }

    public void run() {
        super.run();
        if (this.aoaConnect == null) {
            this.aoaConnect = new AOAConnect(this.mContext, this.accessory, this.dataChanel, this.mIAoaConnectListener);
            if (this.aoaConnect.isAoaDeviceConecect()) {
                this.aoaConnect.startSession();
                SessionManager.getInstance().addSession(this.aoaConnect);
                return;
            }
            this.aoaConnect.closeUsbAccessory();
        }
    }

    public void exit() {
        interrupt();
        if (this.aoaConnect != null) {
            this.aoaConnect.closeSession();
            this.aoaConnect = null;
        }
    }
}
