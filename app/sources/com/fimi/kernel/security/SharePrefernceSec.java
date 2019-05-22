package com.fimi.kernel.security;

import android.content.SharedPreferences;
import com.fimi.kernel.FimiAppContext;

public class SharePrefernceSec {
    private static final String KEY_SP_STORE_MANAGER = "SPStoreManager";
    private static final boolean isEncrypt = true;

    public static SharedPreferences getSharedPreferences(String key_storeName) {
        return new SecurePreferences(FimiAppContext.getContext(), "", key_storeName);
    }

    public static SharedPreferences getSharedPreferences() {
        return new SecurePreferences(FimiAppContext.getContext(), "", KEY_SP_STORE_MANAGER);
    }
}
