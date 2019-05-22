package com.fimi.kernel.connect.interfaces;

public interface IConnectResultListener {
    void onConnectError(String str);

    void onConnected(String str);

    void onDeviceConnect();

    void onDeviceDisConnnect();

    void onDisconnect(String str);
}
