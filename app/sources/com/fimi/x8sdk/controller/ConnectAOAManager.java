package com.fimi.x8sdk.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import com.fimi.x8sdk.connect.CommunicationManager;
import com.fimi.x8sdk.connect.ConnectType;
import com.fimi.x8sdk.listener.ConnectStatusListener.IEngineCallback;

public class ConnectAOAManager {
    private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";
    IEngineCallback callback;
    boolean isRequestPermission = false;
    private volatile boolean mConnected = false;
    Context mContext;
    private PendingIntent mPermissionIntent;
    UsbManager usbManager;

    public void conectAOA() {
        UsbAccessory accessory = null;
        UsbAccessory[] accessories;
        if (this.mConnected) {
            if (this.usbManager != null) {
                accessories = this.usbManager.getAccessoryList();
                if (accessories != null) {
                    accessory = accessories[0];
                }
                if (accessory == null) {
                    this.mConnected = false;
                    this.callback.onConnectionClosed();
                    CommunicationManager.getCommunicationManager().stopConnectThread();
                }
            }
        } else if (this.usbManager != null) {
            accessories = this.usbManager.getAccessoryList();
            if (accessories != null) {
                accessory = accessories[0];
            }
            if (accessory == null) {
                return;
            }
            if (this.usbManager.hasPermission(accessory)) {
                CommunicationManager.getCommunicationManager().setAccessory(accessory);
                CommunicationManager.getCommunicationManager().startConnectThread(this.mContext, ConnectType.Aoa);
                this.callback.onConnected();
                this.mConnected = true;
            } else if (!this.isRequestPermission) {
                this.usbManager.requestPermission(accessory, this.mPermissionIntent);
                this.callback.onConnectionEstablished();
                this.isRequestPermission = true;
            }
        }
    }

    public ConnectAOAManager(Context mContext) {
        this.mContext = mContext;
        this.mContext = mContext;
        this.usbManager = (UsbManager) mContext.getSystemService("usb");
        this.mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        this.callback = this.callback;
    }

    public void unConnectAOA() {
        CommunicationManager.getCommunicationManager().stopConnectThread();
        this.mConnected = false;
        this.callback.onConnectionClosed();
    }
}
