package com.fimi.kernel.security;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.fimi.kernel.security.AesCbcWithIntegrity.CipherTextIvMac;
import com.fimi.kernel.security.AesCbcWithIntegrity.SecretKeys;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SecurePreferences implements SharedPreferences {
    private static final int ORIGINAL_ITERATION_COUNT = 10000;
    private static final String TAG = SecurePreferences.class.getName();
    private static boolean sLoggingEnabled = false;
    private SecretKeys keys;
    private String salt;
    private String sharedPrefFilename;
    private SharedPreferences sharedPreferences;

    public final class Editor implements android.content.SharedPreferences.Editor {
        private android.content.SharedPreferences.Editor mEditor;

        private Editor() {
            this.mEditor = SecurePreferences.this.sharedPreferences.edit();
        }

        public android.content.SharedPreferences.Editor putString(String key, String value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(value));
            return this;
        }

        public android.content.SharedPreferences.Editor putUnencryptedString(String key, String value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), value);
            return this;
        }

        @TargetApi(11)
        public android.content.SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            Set<String> encryptedValues = new HashSet(values.size());
            for (String value : values) {
                encryptedValues.add(SecurePreferences.this.encrypt(value));
            }
            this.mEditor.putStringSet(SecurePreferences.hashPrefKey(key), encryptedValues);
            return this;
        }

        public android.content.SharedPreferences.Editor putInt(String key, int value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(Integer.toString(value)));
            return this;
        }

        public android.content.SharedPreferences.Editor putLong(String key, long value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(Long.toString(value)));
            return this;
        }

        public android.content.SharedPreferences.Editor putFloat(String key, float value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(Float.toString(value)));
            return this;
        }

        public android.content.SharedPreferences.Editor putBoolean(String key, boolean value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(Boolean.toString(value)));
            return this;
        }

        public android.content.SharedPreferences.Editor remove(String key) {
            this.mEditor.remove(SecurePreferences.hashPrefKey(key));
            return this;
        }

        public android.content.SharedPreferences.Editor clear() {
            this.mEditor.clear();
            return this;
        }

        public boolean commit() {
            return this.mEditor.commit();
        }

        @TargetApi(9)
        public void apply() {
            if (VERSION.SDK_INT >= 9) {
                this.mEditor.apply();
            } else {
                commit();
            }
        }
    }

    public SecurePreferences(Context context) {
        this(context, "", null);
    }

    public SecurePreferences(Context context, int iterationCount) {
        this(context, "", null, iterationCount);
    }

    public SecurePreferences(Context context, String password, String sharedPrefFilename) {
        this(context, password, null, sharedPrefFilename, 10000);
    }

    public SecurePreferences(Context context, String password, String sharedPrefFilename, int iterationCount) {
        this(context, null, password, null, sharedPrefFilename, iterationCount);
    }

    public SecurePreferences(Context context, SecretKeys secretKey, String sharedPrefFilename) {
        this(context, secretKey, null, null, sharedPrefFilename, 0);
    }

    public SecurePreferences(Context context, String password, String salt, String sharedPrefFilename, int iterationCount) {
        this(context, null, password, salt, sharedPrefFilename, iterationCount);
    }

    private SecurePreferences(Context context, SecretKeys secretKey, String password, String salt, String sharedPrefFilename, int iterationCount) {
        if (this.sharedPreferences == null) {
            this.sharedPreferences = getSharedPreferenceFile(context, sharedPrefFilename);
        }
        this.salt = salt;
        if (secretKey != null) {
            this.keys = secretKey;
        } else if (TextUtils.isEmpty(password)) {
            try {
                String key = generateAesKeyName(context, iterationCount);
                String keyAsString = this.sharedPreferences.getString(key, null);
                if (keyAsString == null) {
                    this.keys = AesCbcWithIntegrity.generateKey();
                    if (!this.sharedPreferences.edit().putString(key, this.keys.toString()).commit()) {
                        Log.w(TAG, "Key not committed to prefs");
                    }
                } else {
                    this.keys = AesCbcWithIntegrity.keys(keyAsString);
                }
                if (this.keys == null) {
                    throw new GeneralSecurityException("Problem generating Key");
                }
            } catch (GeneralSecurityException e) {
                if (sLoggingEnabled) {
                    Log.e(TAG, "Error init:" + e.getMessage());
                }
                throw new IllegalStateException(e);
            }
        } else {
            try {
                this.keys = AesCbcWithIntegrity.generateKeyFromPassword(password, getSalt(context).getBytes(), iterationCount);
                if (this.keys == null) {
                    throw new GeneralSecurityException("Problem generating Key From Password");
                }
            } catch (GeneralSecurityException e2) {
                if (sLoggingEnabled) {
                    Log.e(TAG, "Error init using user password:" + e2.getMessage());
                }
                throw new IllegalStateException(e2);
            }
        }
    }

    private SharedPreferences getSharedPreferenceFile(Context context, String prefFilename) {
        this.sharedPrefFilename = prefFilename;
        if (TextUtils.isEmpty(prefFilename)) {
            return PreferenceManager.getDefaultSharedPreferences(context);
        }
        return context.getSharedPreferences(prefFilename, 0);
    }

    public void destroyKeys() {
        this.keys = null;
    }

    private String generateAesKeyName(Context context, int iterationCount) throws GeneralSecurityException {
        return hashPrefKey(AesCbcWithIntegrity.generateKeyFromPassword(context.getPackageName(), getSalt(context).getBytes(), iterationCount).toString());
    }

    @SuppressLint({"HardwareIds"})
    private static String getDeviceSerialNumber(Context context) {
        try {
            String deviceSerial = (String) Build.class.getField("SERIAL").get(null);
            if (TextUtils.isEmpty(deviceSerial)) {
                return Secure.getString(context.getContentResolver(), "android_id");
            }
            return deviceSerial;
        } catch (Exception e) {
            return Secure.getString(context.getContentResolver(), "android_id");
        }
    }

    private String getSalt(Context context) {
        if (TextUtils.isEmpty(this.salt)) {
            return getDeviceSerialNumber(context);
        }
        return this.salt;
    }

    public static String hashPrefKey(String prefKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = prefKey.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            return Base64.encodeToString(digest.digest(), 2);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            if (sLoggingEnabled) {
                Log.w(TAG, "Problem generating hash", e);
            }
            return null;
        }
    }

    private String encrypt(String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            return AesCbcWithIntegrity.encrypt(cleartext, this.keys).toString();
        } catch (GeneralSecurityException e) {
            if (sLoggingEnabled) {
                Log.w(TAG, "encrypt", e);
            }
            return null;
        } catch (UnsupportedEncodingException e2) {
            if (sLoggingEnabled) {
                Log.w(TAG, "encrypt", e2);
            }
            return null;
        }
    }

    private String decrypt(String ciphertext) {
        if (TextUtils.isEmpty(ciphertext)) {
            return ciphertext;
        }
        try {
            return AesCbcWithIntegrity.decryptString(new CipherTextIvMac(ciphertext), this.keys);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            if (sLoggingEnabled) {
                Log.w(TAG, "decrypt", e);
            }
            return null;
        }
    }

    public Map<String, String> getAll() {
        Map<String, ?> encryptedMap = this.sharedPreferences.getAll();
        Map<String, String> decryptedMap = new HashMap(encryptedMap.size());
        for (Entry<String, ?> entry : encryptedMap.entrySet()) {
            try {
                Object cipherText = entry.getValue();
                if (!(cipherText == null || cipherText.equals(this.keys.toString()))) {
                    decryptedMap.put(entry.getKey(), decrypt(cipherText.toString()));
                }
            } catch (Exception e) {
                if (sLoggingEnabled) {
                    Log.w(TAG, "error during getAll", e);
                }
                decryptedMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return decryptedMap;
    }

    public String getString(String key, String defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        String decryptedValue = decrypt(encryptedValue);
        return (encryptedValue == null || decryptedValue == null) ? defaultValue : decryptedValue;
    }

    public String getEncryptedString(String key, String defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        return encryptedValue != null ? encryptedValue : defaultValue;
    }

    @TargetApi(11)
    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        Set<String> encryptedSet = this.sharedPreferences.getStringSet(hashPrefKey(key), null);
        if (encryptedSet == null) {
            return defaultValues;
        }
        Set<String> decryptedSet = new HashSet(encryptedSet.size());
        for (String encryptedValue : encryptedSet) {
            decryptedSet.add(decrypt(encryptedValue));
        }
        return decryptedSet;
    }

    public int getInt(String key, int defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    public long getLong(String key, long defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    public float getFloat(String key, float defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    public boolean contains(String key) {
        return this.sharedPreferences.contains(hashPrefKey(key));
    }

    @SuppressLint({"CommitPrefEdits"})
    public void handlePasswordChange(String newPassword, Context context, int iterationCount) throws GeneralSecurityException {
        SecretKeys newKey = AesCbcWithIntegrity.generateKeyFromPassword(newPassword, getSalt(context).getBytes(), iterationCount);
        Map<String, ?> allOfThePrefs = this.sharedPreferences.getAll();
        Map<String, String> unencryptedPrefs = new HashMap(allOfThePrefs.size());
        for (String prefKey : allOfThePrefs.keySet()) {
            String prefValue = allOfThePrefs.get(prefKey);
            if (prefValue instanceof String) {
                unencryptedPrefs.put(prefKey, decrypt(prefValue));
            }
        }
        destroyKeys();
        android.content.SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.clear();
        editor.commit();
        this.sharedPreferences = null;
        this.sharedPreferences = getSharedPreferenceFile(context, this.sharedPrefFilename);
        this.keys = newKey;
        android.content.SharedPreferences.Editor updatedEditor = this.sharedPreferences.edit();
        for (String prefKey2 : unencryptedPrefs.keySet()) {
            updatedEditor.putString(prefKey2, encrypt((String) unencryptedPrefs.get(prefKey2)));
        }
        updatedEditor.commit();
    }

    public void handlePasswordChange(String newPassword, Context context) throws GeneralSecurityException {
        handlePasswordChange(newPassword, context, 10000);
    }

    public Editor edit() {
        return new Editor();
    }

    public static boolean isLoggingEnabled() {
        return sLoggingEnabled;
    }

    public static void setLoggingEnabled(boolean loggingEnabled) {
        sLoggingEnabled = loggingEnabled;
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener, boolean decryptKeys) {
        if (!decryptKeys) {
            registerOnSharedPreferenceChangeListener(listener);
        }
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
