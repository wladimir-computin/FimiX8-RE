package com.fimi.x8sdk.connect;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

public class ConnectStatusManager {
    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public void onDataRecieved(LinkPacket4 packet4) {
    }
}
