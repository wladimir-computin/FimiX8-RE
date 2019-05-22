package com.fimi.album.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.fimi.album.iview.IBroadcastPreform;

public class DeleteItemReceiver extends BroadcastReceiver {
    private IBroadcastPreform mIBroadcastPreform;

    public void onReceive(Context context, Intent intent) {
        if (this.mIBroadcastPreform != null) {
            this.mIBroadcastPreform.onReceive(context, intent);
        }
    }

    public void setIReceiver(IBroadcastPreform IReceiver) {
        this.mIBroadcastPreform = IReceiver;
    }
}
