package com.securepreferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import com.tozny.crypto.android.AesCbcWithIntegrity;
import com.tozny.crypto.android.AesCbcWithIntegrity.SecretKeys;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SecurePreferences implements SharedPreferences {
    private static final String TAG = SecurePreferences.class.getName();
    private static boolean sLoggingEnabled = false;
    private SecretKeys keys;
    private String sharedPrefFilename;
    private SharedPreferences sharedPreferences;

    public class Editor implements android.content.SharedPreferences.Editor {
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

    public SecurePreferences(Context context, String password, String sharedPrefFilename) {
        this(context, null, password, sharedPrefFilename);
    }

    public SecurePreferences(Context context, SecretKeys secretKey, String sharedPrefFilename) {
        this(context, secretKey, null, sharedPrefFilename);
    }

    private SecurePreferences(Context context, SecretKeys secretKey, String password, String sharedPrefFilename) {
        if (this.sharedPreferences == null) {
            this.sharedPreferences = getSharedPreferenceFile(context, sharedPrefFilename);
        }
        if (secretKey != null) {
            this.keys = secretKey;
        } else if (TextUtils.isEmpty(password)) {
            try {
                String key = generateAesKeyName(context);
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
                this.keys = AesCbcWithIntegrity.generateKeyFromPassword(password, getDeviceSerialNumber(context).getBytes());
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
        this.sharedPrefFilename = this.sharedPrefFilename;
        if (TextUtils.isEmpty(prefFilename)) {
            return PreferenceManager.getDefaultSharedPreferences(context);
        }
        return context.getSharedPreferences(prefFilename, 0);
    }

    public void destroyKeys() {
        this.keys = null;
    }

    private static String generateAesKeyName(Context context) throws GeneralSecurityException {
        SecretKeys generatedKeyName = AesCbcWithIntegrity.generateKeyFromPassword(context.getPackageName(), getDeviceSerialNumber(context).getBytes());
        if (generatedKeyName != null) {
            return hashPrefKey(generatedKeyName.toString());
        }
        throw new GeneralSecurityException("Key not generated");
    }

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

    /* JADX WARNING: Missing block: B:11:?, code skipped:
            return null;
     */
    public static java.lang.String hashPrefKey(java.lang.String r5) {
        /*
        r3 = "SHA-256";
        r1 = java.security.MessageDigest.getInstance(r3);	 Catch:{ NoSuchAlgorithmException -> 0x001b, UnsupportedEncodingException -> 0x0029 }
        r3 = "UTF-8";
        r0 = r5.getBytes(r3);	 Catch:{ NoSuchAlgorithmException -> 0x001b, UnsupportedEncodingException -> 0x0029 }
        r3 = 0;
        r4 = r0.length;	 Catch:{ NoSuchAlgorithmException -> 0x001b, UnsupportedEncodingException -> 0x0029 }
        r1.update(r0, r3, r4);	 Catch:{ NoSuchAlgorithmException -> 0x001b, UnsupportedEncodingException -> 0x0029 }
        r3 = r1.digest();	 Catch:{ NoSuchAlgorithmException -> 0x001b, UnsupportedEncodingException -> 0x0029 }
        r4 = 2;
        r3 = android.util.Base64.encodeToString(r3, r4);	 Catch:{ NoSuchAlgorithmException -> 0x001b, UnsupportedEncodingException -> 0x0029 }
    L_0x001a:
        return r3;
    L_0x001b:
        r2 = move-exception;
        r3 = sLoggingEnabled;
        if (r3 == 0) goto L_0x0027;
    L_0x0020:
        r3 = TAG;
        r4 = "Problem generating hash";
        android.util.Log.w(r3, r4, r2);
    L_0x0027:
        r3 = 0;
        goto L_0x001a;
    L_0x0029:
        r2 = move-exception;
        r3 = sLoggingEnabled;
        if (r3 == 0) goto L_0x0027;
    L_0x002e:
        r3 = TAG;
        r4 = "Problem generating hash";
        android.util.Log.w(r3, r4, r2);
        goto L_0x0027;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.securepreferences.SecurePreferences.hashPrefKey(java.lang.String):java.lang.String");
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

    /* JADX WARNING: Missing block: B:14:?, code skipped:
            return null;
     */
    private java.lang.String decrypt(java.lang.String r5) {
        /*
        r4 = this;
        r2 = android.text.TextUtils.isEmpty(r5);
        if (r2 == 0) goto L_0x0007;
    L_0x0006:
        return r5;
    L_0x0007:
        r0 = new com.tozny.crypto.android.AesCbcWithIntegrity$CipherTextIvMac;	 Catch:{ GeneralSecurityException -> 0x0013, UnsupportedEncodingException -> 0x0021 }
        r0.<init>(r5);	 Catch:{ GeneralSecurityException -> 0x0013, UnsupportedEncodingException -> 0x0021 }
        r2 = r4.keys;	 Catch:{ GeneralSecurityException -> 0x0013, UnsupportedEncodingException -> 0x0021 }
        r5 = com.tozny.crypto.android.AesCbcWithIntegrity.decryptString(r0, r2);	 Catch:{ GeneralSecurityException -> 0x0013, UnsupportedEncodingException -> 0x0021 }
        goto L_0x0006;
    L_0x0013:
        r1 = move-exception;
        r2 = sLoggingEnabled;
        if (r2 == 0) goto L_0x001f;
    L_0x0018:
        r2 = TAG;
        r3 = "decrypt";
        android.util.Log.w(r2, r3, r1);
    L_0x001f:
        r5 = 0;
        goto L_0x0006;
    L_0x0021:
        r1 = move-exception;
        r2 = sLoggingEnabled;
        if (r2 == 0) goto L_0x001f;
    L_0x0026:
        r2 = TAG;
        r3 = "decrypt";
        android.util.Log.w(r2, r3, r1);
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.securepreferences.SecurePreferences.decrypt(java.lang.String):java.lang.String");
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
        return encryptedValue != null ? decrypt(encryptedValue) : defaultValue;
    }

    public String getUnencryptedString(String key, String defaultValue) {
        String nonEncryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        return nonEncryptedValue != null ? nonEncryptedValue : defaultValue;
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

    public void handlePasswordChange(String newPassword, Context context) throws GeneralSecurityException {
        SecretKeys newKey = AesCbcWithIntegrity.generateKeyFromPassword(newPassword, getDeviceSerialNumber(context).getBytes());
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
