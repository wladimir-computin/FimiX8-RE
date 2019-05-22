package com.fimi.kernel.connect.interfaces.ble;

import android.bluetooth.BluetoothDevice;
import java.util.List;

public interface IBleScanResult {
    void autoConnect(BluetoothDevice bluetoothDevice);

    void onBleScanResults(List<BluetoothDevice> list);

    void onConnectErro();

    void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr);
}
