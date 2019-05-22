package com.fimi.kernel.connect.interfaces.ble;

import android.bluetooth.BluetoothDevice;

public interface IFindLastDevice {
    boolean onFind(boolean z, BluetoothDevice bluetoothDevice);
}
