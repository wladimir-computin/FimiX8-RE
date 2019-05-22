package com.fimi.kernel.fds;

import com.fimi.kernel.store.shared.SPStoreManager;

public class ResultCache {
    public static String getMacAlgorithm(int scope) {
        return SPStoreManager.getInstance().getString("macAlgorithm");
    }

    public static void setMacAlgorithm(int scope, String macAlgorithm) {
        SPStoreManager.getInstance().saveString("macAlgorithm", macAlgorithm);
    }

    public static String getMacKey(int scope) {
        return SPStoreManager.getInstance().getString("mackey");
    }

    public static void setMacKey(int scope, String macKey) {
        SPStoreManager.getInstance().saveString("mackey", macKey);
    }

    public static String getAccessToken(int scope) {
        return SPStoreManager.getInstance().getString("accessToken");
    }

    public static void setAccessToken(int scope, String accessToken) {
        SPStoreManager.getInstance().saveString("accessToken", accessToken);
    }
}
