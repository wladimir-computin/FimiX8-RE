package com.fimi.x8sdk.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.x8sdk.connect.CommunicationManager;
import com.fimi.x8sdk.connect.ConnectType;

public class ConnectRcManager {
    private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";
    private static ConnectRcManager instance = new ConnectRcManager();
    boolean isRequestPermission = false;
    boolean isTryConnect;
    private PendingIntent mPermissionIntent;

    public static ConnectRcManager getInstance() {
        return instance;
    }

    public ConnectRcManager() {
        initSessionAndNotice();
    }

    private void initSessionAndNotice() {
        SessionManager.initInstance();
        NoticeManager.initInstance();
    }

    public synchronized void connectRC(Context mContext) {
        if (!this.isTryConnect) {
            this.isTryConnect = true;
            UsbManager usbManager = (UsbManager) mContext.getSystemService("usb");
            this.mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
            if (usbManager != null) {
                UsbAccessory[] accessories = usbManager.getAccessoryList();
                UsbAccessory accessory = accessories == null ? null : accessories[0];
                if (accessory != null) {
                    if (usbManager.hasPermission(accessory)) {
                        CommunicationManager.getCommunicationManager().setAccessory(accessory);
                        CommunicationManager.getCommunicationManager().startConnectThread(mContext, ConnectType.Aoa);
                    } else if (!this.isRequestPermission) {
                        usbManager.requestPermission(accessory, this.mPermissionIntent);
                        this.isRequestPermission = true;
                    }
                }
            }
            this.isTryConnect = false;
        }
    }

    public void unConnectRC() {
        CommunicationManager.getCommunicationManager().stopConnectThread();
    }
}
