package com.fimi.kernel.connect.ble.scanner;

import android.content.Context;
import android.os.Build.VERSION;

public class BleScanner {
    private static final String TAG = BleScanner.class.getName();
    public BaseBleScanner bleScanner;

    public BleScanner(Context context, SimpleScanCallback callback) {
        if (VERSION.SDK_INT >= 21) {
            this.bleScanner = new LollipopBleScanner(callback);
        } else {
            this.bleScanner = new JellyBeanBleScanner(context, callback);
        }
    }

    public boolean isScanning() {
        return this.bleScanner.isScanning;
    }

    public void startBleScan() {
        this.bleScanner.onStartBleScan();
    }

    public void startBleScan(long timeoutMillis) {
        this.bleScanner.onStartBleScan(timeoutMillis);
    }

    public void stopBleScan() {
        this.bleScanner.onStopBleScan();
    }
}
