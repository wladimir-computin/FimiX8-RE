package com.fimi.kernel.connect.ble.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import com.fimi.kernel.utils.LogUtil;

public class JellyBeanBleScanner extends BaseBleScanner {
    private static final String TAG = JellyBeanBleScanner.class.getName();
    private LeScanCallback leScanCallback = new LeScanCallback() {
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            JellyBeanBleScanner.this.mScanCallback.onBleScan(device, rssi, scanRecord);
        }
    };
    public BluetoothAdapter mBluetooth = null;
    private SimpleScanCallback mScanCallback = null;

    public JellyBeanBleScanner(Context context, SimpleScanCallback callback) {
        this.mScanCallback = callback;
        this.mBluetooth = ((BluetoothManager) context.getSystemService("bluetooth")).getAdapter();
    }

    public void onStartBleScan(long timeoutMillis) {
        long delay;
        if (timeoutMillis == 0) {
            delay = 10000;
        } else {
            delay = timeoutMillis;
        }
        if (this.mBluetooth != null) {
            this.isScanning = this.mBluetooth.startLeScan(this.leScanCallback);
            this.timeoutHandler.postDelayed(this.timeoutRunnable, delay);
            LogUtil.i(TAG, "mBluetooth.startLeScan() " + this.isScanning);
            return;
        }
        this.mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
    }

    public void onStartBleScan() {
        if (this.mBluetooth != null) {
            this.isScanning = this.mBluetooth.startLeScan(this.leScanCallback);
            LogUtil.i(TAG, "mBluetooth.startLeScan() " + this.isScanning);
            return;
        }
        this.mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
    }

    public void onStopBleScan() {
        this.isScanning = false;
        if (this.mBluetooth != null) {
            this.mBluetooth.stopLeScan(this.leScanCallback);
        }
    }

    public void onBleScanFailed(BleScanState scanState) {
        this.mScanCallback.onBleScanFailed(scanState);
    }
}
