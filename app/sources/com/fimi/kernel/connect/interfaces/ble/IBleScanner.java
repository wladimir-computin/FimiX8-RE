package com.fimi.kernel.connect.interfaces.ble;

public interface IBleScanner {
    boolean isOpenBluetooth();

    boolean openBluetooth();

    void setBleScanResult(IBleScanResult iBleScanResult);

    void setLastBleDevice(String str);

    void startBleScan();

    void stopBleScan();
}
