package com.fimi.thirdpartysdk.login;

import android.content.Context;
import com.fimi.kernel.store.shared.SPStoreManager;

public class LoginResultCache {
    public static String getMacAlgorithm(Context context, int scope) {
        return SPStoreManager.getInstance().getString("macAlgorithm");
    }

    public static void setMacAlgorithm(Context context, int scope, String macAlgorithm) {
        SPStoreManager.getInstance().saveString("macAlgorithm", macAlgorithm);
    }

    public static String getMacKey(Context context, int scope) {
        return SPStoreManager.getInstance().getString("mackey");
    }

    public static void setMacKey(Context context, int scope, String macKey) {
        SPStoreManager.getInstance().saveString("mackey", macKey);
    }

    public static String getAccessToken(Context context, int scope) {
        return SPStoreManager.getInstance().getString("accessToken");
    }

    public static void setAccessToken(Context context, int scope, String accessToken) {
        SPStoreManager.getInstance().saveString("accessToken", accessToken);
    }
}
