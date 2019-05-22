package com.fimi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public abstract class NetworkStateReceiver extends BroadcastReceiver {
    public static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String ACTION_WIFISTATE_CHANGE = "android.net.wifi.STATE_CHANGE";

    public enum NetworkType {
        Wifi,
        Mobile,
        None
    }

    public abstract void onNetworkStateChange(NetworkType networkType, Context context);

    public void onReceive(Context context, Intent intent) {
        if (ACTION_CONNECTIVITY_CHANGE.equals(intent.getAction()) || intent.getAction().equals(ACTION_WIFISTATE_CHANGE)) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
            State wifiState = cm.getNetworkInfo(1).getState();
            if (cm.getNetworkInfo(0) != null) {
                State mobileState = cm.getNetworkInfo(0).getState();
                if (isNetworkConnected(wifiState)) {
                    onNetworkStateChange(NetworkType.Wifi, context);
                } else if (isNetworkConnected(mobileState)) {
                    onNetworkStateChange(NetworkType.Mobile, context);
                } else {
                    onNetworkStateChange(NetworkType.None, context);
                }
            }
        }
    }

    private boolean isNetworkConnected(State state) {
        if (state == null || State.CONNECTED != state) {
            return false;
        }
        return true;
    }
}
