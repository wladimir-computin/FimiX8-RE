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
import android.util.Base64;
import android.util.Log;
import com.facebook.imageutils.JfifUtil;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

@Deprecated
public class SecurePreferencesOld implements SharedPreferences {
    private static final String AES_KEY_ALG = "AES";
    private static final String BACKUP_PBE_KEY_ALG = "PBEWithMD5AndDES";
    private static final int ITERATIONS = 2000;
    private static final int KEY_SIZE = 256;
    private static final String PRIMARY_PBE_KEY_ALG = "PBKDF2WithHmacSHA1";
    private static final String PROVIDER = "BC";
    private static final String TAG = SecurePreferencesOld.class.getName();
    private static SharedPreferences sFile;
    private static byte[] sKey;
    private static boolean sLoggingEnabled = false;
    private static HashMap<OnSharedPreferenceChangeListener, OnSharedPreferenceChangeListener> sOnSharedPreferenceChangeListeners;

    public static class Editor implements android.content.SharedPreferences.Editor {
        private android.content.SharedPreferences.Editor mEditor;

        /* synthetic */ Editor(AnonymousClass1 x0) {
            this();
        }

        private Editor() {
            this.mEditor = SecurePreferencesOld.sFile.edit();
        }

        public android.content.SharedPreferences.Editor putString(String key, String value) {
            this.mEditor.putString(SecurePreferencesOld.encrypt(key), SecurePreferencesOld.encrypt(value));
            return this;
        }

        public android.content.SharedPreferences.Editor putStringNoEncrypted(String key, String value) {
            this.mEditor.putString(SecurePreferencesOld.encrypt(key), value);
            return this;
        }

        @TargetApi(11)
        public android.content.SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            Set<String> encryptedValues = new HashSet(values.size());
            for (String value : values) {
                encryptedValues.add(SecurePreferencesOld.encrypt(value));
            }
            this.mEditor.putStringSet(SecurePreferencesOld.encrypt(key), encryptedValues);
            return this;
        }

        public android.content.SharedPreferences.Editor putInt(String key, int value) {
            this.mEditor.putString(SecurePreferencesOld.encrypt(key), SecurePreferencesOld.encrypt(Integer.toString(value)));
            return this;
        }

        public android.content.SharedPreferences.Editor putLong(String key, long value) {
            this.mEditor.putString(SecurePreferencesOld.encrypt(key), SecurePreferencesOld.encrypt(Long.toString(value)));
            return this;
        }

        public android.content.SharedPreferences.Editor putFloat(String key, float value) {
            this.mEditor.putString(SecurePreferencesOld.encrypt(key), SecurePreferencesOld.encrypt(Float.toString(value)));
            return this;
        }

        public android.content.SharedPreferences.Editor putBoolean(String key, boolean value) {
            this.mEditor.putString(SecurePreferencesOld.encrypt(key), SecurePreferencesOld.encrypt(Boolean.toString(value)));
            return this;
        }

        public android.content.SharedPreferences.Editor remove(String key) {
            this.mEditor.remove(SecurePreferencesOld.encrypt(key));
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

    public SecurePreferencesOld(Context context) {
        if (sFile == null) {
            sFile = PreferenceManager.getDefaultSharedPreferences(context);
        }
        try {
            String key = generateAesKeyName(context);
            String value = sFile.getString(key, null);
            if (value == null) {
                value = generateAesKeyValue();
                sFile.edit().putString(key, value).commit();
            }
            sKey = decode(value);
            sOnSharedPreferenceChangeListeners = new HashMap(10);
        } catch (Exception e) {
            if (sLoggingEnabled) {
                Log.e(TAG, "Error init:" + e.getMessage());
            }
            throw new IllegalStateException(e);
        }
    }

    private static String encode(byte[] input) {
        return Base64.encodeToString(input, 3);
    }

    private static byte[] decode(String input) {
        return Base64.decode(input, 3);
    }

    private static String generateAesKeyName(Context context) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        SecretKey key;
        char[] password = context.getPackageName().toCharArray();
        byte[] salt = getDeviceSerialNumber(context).getBytes();
        try {
            key = generatePBEKey(password, salt, PRIMARY_PBE_KEY_ALG, 2000, 256);
        } catch (NoSuchAlgorithmException e) {
            key = generatePBEKey(password, salt, BACKUP_PBE_KEY_ALG, 2000, 256);
        }
        return encode(key.getEncoded());
    }

    @Deprecated
    private static SecretKey generatePBEKey(char[] passphraseOrPin, byte[] salt, String algorthm, int iterations, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        if (iterations == 0) {
            iterations = 1000;
        }
        return SecretKeyFactory.getInstance(algorthm, PROVIDER).generateSecret(new PBEKeySpec(passphraseOrPin, salt, iterations, keyLength));
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

    @Deprecated
    private static String generateAesKeyValue() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        KeyGenerator generator = KeyGenerator.getInstance(AES_KEY_ALG);
        try {
            generator.init(256, random);
        } catch (Exception e) {
            try {
                generator.init(JfifUtil.MARKER_SOFn, random);
            } catch (Exception e2) {
                generator.init(128, random);
            }
        }
        return encode(generator.generateKey().getEncoded());
    }

    @Deprecated
    private static String encrypt(String cleartext) {
        if (cleartext == null || cleartext.length() == 0) {
            return cleartext;
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_KEY_ALG, PROVIDER);
            cipher.init(1, new SecretKeySpec(sKey, AES_KEY_ALG));
            return encode(cipher.doFinal(cleartext.getBytes("UTF-8")));
        } catch (Exception e) {
            if (sLoggingEnabled) {
                Log.w(TAG, "encrypt", e);
            }
            return null;
        }
    }

    @Deprecated
    private static String decrypt(String ciphertext) {
        if (ciphertext == null || ciphertext.length() == 0) {
            return ciphertext;
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_KEY_ALG, PROVIDER);
            cipher.init(2, new SecretKeySpec(sKey, AES_KEY_ALG));
            return new String(cipher.doFinal(decode(ciphertext)), "UTF-8");
        } catch (Exception e) {
            if (sLoggingEnabled) {
                Log.w(TAG, "decrypt", e);
            }
            return null;
        }
    }

    public Map<String, String> getAll() {
        Map<String, ?> encryptedMap = sFile.getAll();
        Map<String, String> decryptedMap = new HashMap(encryptedMap.size());
        for (Entry<String, ?> entry : encryptedMap.entrySet()) {
            try {
                decryptedMap.put(decrypt((String) entry.getKey()), decrypt(entry.getValue().toString()));
            } catch (Exception e) {
            }
        }
        return decryptedMap;
    }

    public String getString(String key, String defaultValue) {
        String encryptedValue = sFile.getString(encrypt(key), null);
        return encryptedValue != null ? decrypt(encryptedValue) : defaultValue;
    }

    public String getStringUnencrypted(String key, String defaultValue) {
        String nonEncryptedValue = sFile.getString(encrypt(key), null);
        return nonEncryptedValue != null ? nonEncryptedValue : defaultValue;
    }

    @TargetApi(11)
    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        Set<String> encryptedSet = sFile.getStringSet(encrypt(key), null);
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
        String encryptedValue = sFile.getString(encrypt(key), null);
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
        String encryptedValue = sFile.getString(encrypt(key), null);
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
        String encryptedValue = sFile.getString(encrypt(key), null);
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
        String encryptedValue = sFile.getString(encrypt(key), null);
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
        return sFile.contains(encrypt(key));
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
        sFile.registerOnSharedPreferenceChangeListener(listener);
    }

    public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener, boolean decryptKeys) {
        if (decryptKeys) {
            OnSharedPreferenceChangeListener secureListener = new OnSharedPreferenceChangeListener() {
                private OnSharedPreferenceChangeListener mInsecureListener = listener;

                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    try {
                        String decryptedKey = SecurePreferencesOld.decrypt(key);
                        if (decryptedKey != null) {
                            this.mInsecureListener.onSharedPreferenceChanged(sharedPreferences, decryptedKey);
                        }
                    } catch (Exception e) {
                        Log.w(SecurePreferencesOld.TAG, "Unable to decrypt key: " + key);
                    }
                }
            };
            sOnSharedPreferenceChangeListeners.put(listener, secureListener);
            sFile.registerOnSharedPreferenceChangeListener(secureListener);
            return;
        }
        registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        if (sOnSharedPreferenceChangeListeners.containsKey(listener)) {
            sFile.unregisterOnSharedPreferenceChangeListener((OnSharedPreferenceChangeListener) sOnSharedPreferenceChangeListeners.remove(listener));
            return;
        }
        sFile.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
