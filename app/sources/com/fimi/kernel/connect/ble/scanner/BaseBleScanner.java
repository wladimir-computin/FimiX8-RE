package com.fimi.kernel.connect.ble.scanner;

import android.os.Handler;

public abstract class BaseBleScanner {
    public static final long defaultTimeout = 10000;
    protected boolean isScanning;
    protected Handler timeoutHandler = new Handler();
    protected Runnable timeoutRunnable = new Runnable() {
        public void run() {
            BaseBleScanner.this.onStopBleScan();
            BaseBleScanner.this.onBleScanFailed(BleScanState.SCAN_TIMEOUT);
        }
    };

    public abstract void onBleScanFailed(BleScanState bleScanState);

    public abstract void onStartBleScan();

    public abstract void onStartBleScan(long j);

    public abstract void onStopBleScan();
}
