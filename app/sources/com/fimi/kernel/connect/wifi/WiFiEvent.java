package com.fimi.kernel.connect.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import java.util.List;

public class WiFiEvent {
    public static int CONNECT_OKAY_MSG = 1;
    public static int PASSWORD_ERROR_MSG = 0;
    public static int WIFI_SCAN_OUTTIME = 2;
    private int msg_id;
    private List<ScanResult> slist;
    private WifiInfo wifiInfo;

    public WifiInfo getWifiInfo() {
        return this.wifiInfo;
    }

    public void setWifiInfo(WifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

    public List<ScanResult> getSlist() {
        return this.slist;
    }

    public void setSlist(List<ScanResult> slist) {
        this.slist = slist;
    }

    public int getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }
}
