package com.fimi.kernel.connect.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class WiFiUtils {
    public static int netId = -1;
    public static ScanResult scanResult;

    public static boolean connectToAP(ScanResult device, String passkey, Context context) {
        if (device == null) {
            return false;
        }
        scanResult = device;
        WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        String networkSSID = device.SSID;
        String networkPass = passkey;
        String securityMode = getScanResultSecurity(device.capabilities);
        if (securityMode.equalsIgnoreCase("OPEN")) {
            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
            wifiConfiguration.allowedKeyManagement.set(0);
            netId = mWifiManager.addNetwork(wifiConfiguration);
            if (netId == -1) {
                return false;
            }
        } else if (securityMode.equalsIgnoreCase("WEP")) {
            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
            wifiConfiguration.wepKeys[0] = "\"" + networkPass + "\"";
            wifiConfiguration.wepTxKeyIndex = 0;
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.allowedGroupCiphers.set(0);
            netId = mWifiManager.addNetwork(wifiConfiguration);
            if (netId == -1) {
                return false;
            }
        } else {
            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
            wifiConfiguration.preSharedKey = "\"" + networkPass + "\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.status = 2;
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedKeyManagement.set(1);
            wifiConfiguration.allowedPairwiseCiphers.set(1);
            wifiConfiguration.allowedPairwiseCiphers.set(2);
            wifiConfiguration.allowedProtocols.set(1);
            wifiConfiguration.allowedProtocols.set(0);
            netId = mWifiManager.addNetwork(wifiConfiguration);
            if (netId == -1) {
                return false;
            }
        }
        return mWifiManager.enableNetwork(netId, true);
    }

    public static int isConfiguration(ScanResult scanResult, Context context) {
        List<WifiConfiguration> wifiConfigList = ((WifiManager) context.getSystemService("wifi")).getConfiguredNetworks();
        if (wifiConfigList == null || scanResult == null) {
            return -1;
        }
        for (int i = 0; i < wifiConfigList.size(); i++) {
            if (isEqualWifi(((WifiConfiguration) wifiConfigList.get(i)).SSID, scanResult.SSID)) {
                netId = ((WifiConfiguration) wifiConfigList.get(i)).networkId;
                return netId;
            }
        }
        return -1;
    }

    public static boolean isEqualWifi(String wifi1, String wifi2) {
        if (wifi1 == null || wifi2 == null) {
            return false;
        }
        if (wifi1.startsWith("\"")) {
            wifi1 = wifi1.substring(1, wifi1.length() - 1);
        }
        if (wifi2.startsWith("\"")) {
            wifi2 = wifi2.substring(1, wifi2.length() - 1);
        }
        return wifi1.equalsIgnoreCase(wifi2);
    }

    public static boolean connectWifi(WifiManager wifiManager, int netId) {
        if (netId == -1) {
            return false;
        }
        List<WifiConfiguration> wifiConfigList = wifiManager.getConfiguredNetworks();
        if (wifiConfigList == null || wifiConfigList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < wifiConfigList.size(); i++) {
            if (((WifiConfiguration) wifiConfigList.get(i)).networkId == netId) {
                boolean connectOkay = wifiManager.enableNetwork(netId, true);
                wifiManager.saveConfiguration();
                return connectOkay;
            }
        }
        return false;
    }

    private static String getScanResultSecurity(String capabilities) {
        String[] securityModes = new String[]{"WEP", "PSK", "EAP"};
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (capabilities.contains(securityModes[i])) {
                return securityModes[i];
            }
        }
        return "OPEN";
    }

    public static boolean isWifi(Context context) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetInfo == null || activeNetInfo.getType() != 1) {
            return false;
        }
        return true;
    }

    public static boolean isPhoneNet(Context context) {
        int i = 0;
        NetworkInfo mMobileNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(0);
        if (mMobileNetworkInfo == null) {
            return false;
        }
        boolean isAvailable = mMobileNetworkInfo.isAvailable();
        if (mMobileNetworkInfo.getState() == State.CONNECTED) {
            i = 1;
        }
        return i & isAvailable;
    }

    public static boolean netOkay(Context context) {
        int i = 1;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo mMobileNetworkInfo = connectivityManager.getNetworkInfo(0);
        NetworkInfo wifiWorkInfo = connectivityManager.getNetworkInfo(1);
        if (mMobileNetworkInfo == null) {
            return false;
        }
        int i2;
        boolean isAvailable = mMobileNetworkInfo.isAvailable();
        if (mMobileNetworkInfo.getState() == State.CONNECTED) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i2 &= isAvailable;
        isAvailable = wifiWorkInfo.isAvailable();
        if (wifiWorkInfo.getState() != State.CONNECTED) {
            i = 0;
        }
        return i2 | (i & isAvailable);
    }

    public static void disConnectCurWifi(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
            wifiManager.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getPhoneIp(Context application) {
        WifiManager wifiManager = (WifiManager) application.getSystemService("wifi");
        if (wifiManager.isWifiEnabled()) {
            return intToIp(wifiManager.getConnectionInfo().getIpAddress());
        }
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public static void removeNetId(Context context, String ssid) {
        if (context != null && ssid != null) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            List<WifiConfiguration> configurationList = wifiManager.getConfiguredNetworks();
            if (configurationList != null) {
                for (WifiConfiguration config : configurationList) {
                    if (isEqualWifi(config.SSID, ssid)) {
                        wifiManager.removeNetwork(config.networkId);
                        wifiManager.saveConfiguration();
                        break;
                    }
                }
            }
            netId = -1;
        }
    }

    public static void removeNetId(Context context) {
        if (context != null && netId > 0) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            wifiManager.removeNetwork(netId);
            wifiManager.saveConfiguration();
            netId = -1;
        }
    }
}
