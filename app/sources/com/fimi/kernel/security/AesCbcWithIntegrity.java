package com.fimi.kernel.security;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import android.support.media.ExifInterface;
import android.util.Base64;
import android.util.Log;
import com.facebook.imagepipeline.memory.BitmapCounterProvider;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCbcWithIntegrity {
    private static final int AES_KEY_LENGTH_BITS = 128;
    private static final boolean ALLOW_BROKEN_PRNG = false;
    public static final int BASE64_FLAGS = 2;
    private static final String CIPHER = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final int HMAC_KEY_LENGTH_BITS = 256;
    private static final int IV_LENGTH_BYTES = 16;
    private static final String PBE_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int PBE_ITERATION_COUNT = 10000;
    private static final int PBE_SALT_LENGTH_BITS = 128;
    static final AtomicBoolean prngFixed = new AtomicBoolean(false);

    public static class CipherTextIvMac {
        private final byte[] cipherText;
        private final byte[] iv;
        private final byte[] mac;

        public byte[] getCipherText() {
            return this.cipherText;
        }

        public byte[] getIv() {
            return this.iv;
        }

        public byte[] getMac() {
            return this.mac;
        }

        public CipherTextIvMac(byte[] c, byte[] i, byte[] h) {
            this.cipherText = new byte[c.length];
            System.arraycopy(c, 0, this.cipherText, 0, c.length);
            this.iv = new byte[i.length];
            System.arraycopy(i, 0, this.iv, 0, i.length);
            this.mac = new byte[h.length];
            System.arraycopy(h, 0, this.mac, 0, h.length);
        }

        public CipherTextIvMac(String base64IvAndCiphertext) {
            String[] civArray = base64IvAndCiphertext.split(":");
            if (civArray.length != 3) {
                throw new IllegalArgumentException("Cannot parse iv:ciphertext:mac");
            }
            this.iv = Base64.decode(civArray[0], 2);
            this.mac = Base64.decode(civArray[1], 2);
            this.cipherText = Base64.decode(civArray[2], 2);
        }

        public static byte[] ivCipherConcat(byte[] iv, byte[] cipherText) {
            byte[] combined = new byte[(iv.length + cipherText.length)];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
            return combined;
        }

        public String toString() {
            String ivString = Base64.encodeToString(this.iv, 2);
            return String.format(ivString + ":" + Base64.encodeToString(this.mac, 2) + ":" + Base64.encodeToString(this.cipherText, 2), new Object[0]);
        }

        public int hashCode() {
            return ((((Arrays.hashCode(this.cipherText) + 31) * 31) + Arrays.hashCode(this.iv)) * 31) + Arrays.hashCode(this.mac);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            CipherTextIvMac other = (CipherTextIvMac) obj;
            if (!Arrays.equals(this.cipherText, other.cipherText)) {
                return false;
            }
            if (!Arrays.equals(this.iv, other.iv)) {
                return false;
            }
            if (Arrays.equals(this.mac, other.mac)) {
                return true;
            }
            return false;
        }
    }

    public static final class PrngFixes {
        private static final byte[] BUILD_FINGERPRINT_AND_DEVICE_SERIAL = getBuildFingerprintAndDeviceSerial();
        private static final int VERSION_CODE_JELLY_BEAN = 16;
        private static final int VERSION_CODE_JELLY_BEAN_MR2 = 18;

        public static class LinuxPRNGSecureRandom extends SecureRandomSpi {
            private static final File URANDOM_FILE = new File("/dev/urandom");
            private static final Object sLock = new Object();
            private static DataInputStream sUrandomIn;
            private static OutputStream sUrandomOut;
            private boolean mSeeded;

            /* Access modifiers changed, original: protected */
            public void engineSetSeed(byte[] bytes) {
                try {
                    OutputStream out;
                    synchronized (sLock) {
                        out = getUrandomOutputStream();
                    }
                    out.write(bytes);
                    out.flush();
                    this.mSeeded = true;
                } catch (IOException e) {
                    try {
                        Log.w(PrngFixes.class.getSimpleName(), "Failed to mix seed into " + URANDOM_FILE);
                    } finally {
                        this.mSeeded = true;
                    }
                }
            }

            /* Access modifiers changed, original: protected */
            public void engineNextBytes(byte[] bytes) {
                if (!this.mSeeded) {
                    engineSetSeed(PrngFixes.generateSeed());
                }
                try {
                    DataInputStream in;
                    synchronized (sLock) {
                        in = getUrandomInputStream();
                    }
                    synchronized (in) {
                        in.readFully(bytes);
                    }
                } catch (IOException e) {
                    throw new SecurityException("Failed to read from " + URANDOM_FILE, e);
                }
            }

            /* Access modifiers changed, original: protected */
            public byte[] engineGenerateSeed(int size) {
                byte[] seed = new byte[size];
                engineNextBytes(seed);
                return seed;
            }

            private DataInputStream getUrandomInputStream() {
                DataInputStream dataInputStream;
                synchronized (sLock) {
                    if (sUrandomIn == null) {
                        try {
                            sUrandomIn = new DataInputStream(new FileInputStream(URANDOM_FILE));
                        } catch (IOException e) {
                            throw new SecurityException("Failed to open " + URANDOM_FILE + " for reading", e);
                        }
                    }
                    dataInputStream = sUrandomIn;
                }
                return dataInputStream;
            }

            private OutputStream getUrandomOutputStream() throws IOException {
                OutputStream outputStream;
                synchronized (sLock) {
                    if (sUrandomOut == null) {
                        sUrandomOut = new FileOutputStream(URANDOM_FILE);
                    }
                    outputStream = sUrandomOut;
                }
                return outputStream;
            }
        }

        private static class LinuxPRNGSecureRandomProvider extends Provider {
            public LinuxPRNGSecureRandomProvider() {
                super("LinuxPRNG", 1.0d, "A Linux-specific random number provider that uses /dev/urandom");
                put("SecureRandom.SHA1PRNG", LinuxPRNGSecureRandom.class.getName());
                put("SecureRandom.SHA1PRNG ImplementedIn", ExifInterface.TAG_SOFTWARE);
            }
        }

        private PrngFixes() {
        }

        public static void apply() {
            applyOpenSSLFix();
            installLinuxPRNGSecureRandom();
        }

        private static void applyOpenSSLFix() throws SecurityException {
            if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 18) {
                try {
                    Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_seed", new Class[]{byte[].class}).invoke(null, new Object[]{generateSeed()});
                    int bytesRead = ((Integer) Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_load_file", new Class[]{String.class, Long.TYPE}).invoke(null, new Object[]{"/dev/urandom", Integer.valueOf(1024)})).intValue();
                    if (bytesRead != 1024) {
                        throw new IOException("Unexpected number of bytes read from Linux PRNG: " + bytesRead);
                    }
                } catch (Exception e) {
                    throw new SecurityException("Failed to seed OpenSSL PRNG", e);
                }
            }
        }

        /* JADX WARNING: Missing block: B:9:0x002b, code skipped:
            if (r3[0].getClass().getSimpleName().equals(com.fimi.kernel.security.AesCbcWithIntegrity.PrngFixes.LinuxPRNGSecureRandomProvider.class.getSimpleName()) != false) goto L_0x0036;
     */
        private static void installLinuxPRNGSecureRandom() throws java.lang.SecurityException {
            /*
            r6 = 1;
            r4 = android.os.Build.VERSION.SDK_INT;
            r5 = 18;
            if (r4 <= r5) goto L_0x0008;
        L_0x0007:
            return;
        L_0x0008:
            r4 = "SecureRandom.SHA1PRNG";
            r3 = java.security.Security.getProviders(r4);
            r5 = java.security.Security.class;
            monitor-enter(r5);
            if (r3 == 0) goto L_0x002d;
        L_0x0013:
            r4 = r3.length;	 Catch:{ all -> 0x0074 }
            if (r4 < r6) goto L_0x002d;
        L_0x0016:
            r4 = 0;
            r4 = r3[r4];	 Catch:{ all -> 0x0074 }
            r4 = r4.getClass();	 Catch:{ all -> 0x0074 }
            r4 = r4.getSimpleName();	 Catch:{ all -> 0x0074 }
            r6 = com.fimi.kernel.security.AesCbcWithIntegrity.PrngFixes.LinuxPRNGSecureRandomProvider.class;
            r6 = r6.getSimpleName();	 Catch:{ all -> 0x0074 }
            r4 = r4.equals(r6);	 Catch:{ all -> 0x0074 }
            if (r4 != 0) goto L_0x0036;
        L_0x002d:
            r4 = new com.fimi.kernel.security.AesCbcWithIntegrity$PrngFixes$LinuxPRNGSecureRandomProvider;	 Catch:{ all -> 0x0074 }
            r4.<init>();	 Catch:{ all -> 0x0074 }
            r6 = 1;
            java.security.Security.insertProviderAt(r4, r6);	 Catch:{ all -> 0x0074 }
        L_0x0036:
            r1 = new java.security.SecureRandom;	 Catch:{ all -> 0x0074 }
            r1.<init>();	 Catch:{ all -> 0x0074 }
            r4 = r1.getProvider();	 Catch:{ all -> 0x0074 }
            r4 = r4.getClass();	 Catch:{ all -> 0x0074 }
            r4 = r4.getSimpleName();	 Catch:{ all -> 0x0074 }
            r6 = com.fimi.kernel.security.AesCbcWithIntegrity.PrngFixes.LinuxPRNGSecureRandomProvider.class;
            r6 = r6.getSimpleName();	 Catch:{ all -> 0x0074 }
            r4 = r4.equals(r6);	 Catch:{ all -> 0x0074 }
            if (r4 != 0) goto L_0x0077;
        L_0x0053:
            r4 = new java.lang.SecurityException;	 Catch:{ all -> 0x0074 }
            r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0074 }
            r6.<init>();	 Catch:{ all -> 0x0074 }
            r7 = "new SecureRandom() backed by wrong Provider: ";
            r6 = r6.append(r7);	 Catch:{ all -> 0x0074 }
            r7 = r1.getProvider();	 Catch:{ all -> 0x0074 }
            r7 = r7.getClass();	 Catch:{ all -> 0x0074 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0074 }
            r6 = r6.toString();	 Catch:{ all -> 0x0074 }
            r4.<init>(r6);	 Catch:{ all -> 0x0074 }
            throw r4;	 Catch:{ all -> 0x0074 }
        L_0x0074:
            r4 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0074 }
            throw r4;
        L_0x0077:
            r2 = 0;
            r4 = "SHA1PRNG";
            r2 = java.security.SecureRandom.getInstance(r4);	 Catch:{ NoSuchAlgorithmException -> 0x00b7 }
        L_0x007e:
            r4 = r2.getProvider();	 Catch:{ all -> 0x0074 }
            r4 = r4.getClass();	 Catch:{ all -> 0x0074 }
            r4 = r4.getSimpleName();	 Catch:{ all -> 0x0074 }
            r6 = com.fimi.kernel.security.AesCbcWithIntegrity.PrngFixes.LinuxPRNGSecureRandomProvider.class;
            r6 = r6.getSimpleName();	 Catch:{ all -> 0x0074 }
            r4 = r4.equals(r6);	 Catch:{ all -> 0x0074 }
            if (r4 != 0) goto L_0x00c0;
        L_0x0096:
            r4 = new java.lang.SecurityException;	 Catch:{ all -> 0x0074 }
            r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0074 }
            r6.<init>();	 Catch:{ all -> 0x0074 }
            r7 = "SecureRandom.getInstance(\"SHA1PRNG\") backed by wrong Provider: ";
            r6 = r6.append(r7);	 Catch:{ all -> 0x0074 }
            r7 = r2.getProvider();	 Catch:{ all -> 0x0074 }
            r7 = r7.getClass();	 Catch:{ all -> 0x0074 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0074 }
            r6 = r6.toString();	 Catch:{ all -> 0x0074 }
            r4.<init>(r6);	 Catch:{ all -> 0x0074 }
            throw r4;	 Catch:{ all -> 0x0074 }
        L_0x00b7:
            r0 = move-exception;
            r4 = new java.lang.SecurityException;	 Catch:{ all -> 0x0074 }
            r6 = "SHA1PRNG not available";
            r4.<init>(r6, r0);	 Catch:{ all -> 0x0074 }
            goto L_0x007e;
        L_0x00c0:
            monitor-exit(r5);	 Catch:{ all -> 0x0074 }
            goto L_0x0007;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.security.AesCbcWithIntegrity$PrngFixes.installLinuxPRNGSecureRandom():void");
        }

        private static byte[] generateSeed() {
            try {
                ByteArrayOutputStream seedBuffer = new ByteArrayOutputStream();
                DataOutputStream seedBufferOut = new DataOutputStream(seedBuffer);
                seedBufferOut.writeLong(System.currentTimeMillis());
                seedBufferOut.writeLong(System.nanoTime());
                seedBufferOut.writeInt(Process.myPid());
                seedBufferOut.writeInt(Process.myUid());
                seedBufferOut.write(BUILD_FINGERPRINT_AND_DEVICE_SERIAL);
                seedBufferOut.close();
                return seedBuffer.toByteArray();
            } catch (IOException e) {
                throw new SecurityException("Failed to generate seed", e);
            }
        }

        private static String getDeviceSerialNumber() {
            try {
                return (String) Build.class.getField("SERIAL").get(null);
            } catch (Exception e) {
                return null;
            }
        }

        private static byte[] getBuildFingerprintAndDeviceSerial() {
            StringBuilder result = new StringBuilder();
            String fingerprint = Build.FINGERPRINT;
            if (fingerprint != null) {
                result.append(fingerprint);
            }
            String serial = getDeviceSerialNumber();
            if (serial != null) {
                result.append(serial);
            }
            try {
                return result.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UTF-8 encoding not supported");
            }
        }
    }

    public static class SecretKeys {
        private SecretKey confidentialityKey;
        private SecretKey integrityKey;

        public SecretKeys(SecretKey confidentialityKeyIn, SecretKey integrityKeyIn) {
            setConfidentialityKey(confidentialityKeyIn);
            setIntegrityKey(integrityKeyIn);
        }

        public SecretKey getConfidentialityKey() {
            return this.confidentialityKey;
        }

        public void setConfidentialityKey(SecretKey confidentialityKey) {
            this.confidentialityKey = confidentialityKey;
        }

        public SecretKey getIntegrityKey() {
            return this.integrityKey;
        }

        public void setIntegrityKey(SecretKey integrityKey) {
            this.integrityKey = integrityKey;
        }

        public String toString() {
            return Base64.encodeToString(getConfidentialityKey().getEncoded(), 2) + ":" + Base64.encodeToString(getIntegrityKey().getEncoded(), 2);
        }

        public int hashCode() {
            return ((this.confidentialityKey.hashCode() + 31) * 31) + this.integrityKey.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            SecretKeys other = (SecretKeys) obj;
            if (!this.integrityKey.equals(other.integrityKey)) {
                return false;
            }
            if (this.confidentialityKey.equals(other.confidentialityKey)) {
                return true;
            }
            return false;
        }
    }

    public static String keyString(SecretKeys keys) {
        return keys.toString();
    }

    public static SecretKeys keys(String keysStr) throws InvalidKeyException {
        String[] keysArr = keysStr.split(":");
        if (keysArr.length != 2) {
            throw new IllegalArgumentException("Cannot parse aesKey:hmacKey");
        }
        byte[] confidentialityKey = Base64.decode(keysArr[0], 2);
        if (confidentialityKey.length != 16) {
            throw new InvalidKeyException("Base64 decoded key is not 128 bytes");
        }
        byte[] integrityKey = Base64.decode(keysArr[1], 2);
        if (integrityKey.length == 32) {
            return new SecretKeys(new SecretKeySpec(confidentialityKey, 0, confidentialityKey.length, CIPHER), new SecretKeySpec(integrityKey, HMAC_ALGORITHM));
        }
        throw new InvalidKeyException("Base64 decoded key is not 256 bytes");
    }

    public static SecretKeys generateKey() throws GeneralSecurityException {
        fixPrng();
        KeyGenerator keyGen = KeyGenerator.getInstance(CIPHER);
        keyGen.init(128);
        return new SecretKeys(keyGen.generateKey(), new SecretKeySpec(randomBytes(32), HMAC_ALGORITHM));
    }

    public static SecretKeys generateKeyFromPassword(String password, byte[] salt) throws GeneralSecurityException {
        return generateKeyFromPassword(password, salt, 10000);
    }

    public static SecretKeys generateKeyFromPassword(String password, byte[] salt, int iterationCount) throws GeneralSecurityException {
        fixPrng();
        byte[] keyBytes = SecretKeyFactory.getInstance(PBE_ALGORITHM).generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterationCount, BitmapCounterProvider.MAX_BITMAP_COUNT)).getEncoded();
        return new SecretKeys(new SecretKeySpec(copyOfRange(keyBytes, 0, 16), CIPHER), new SecretKeySpec(copyOfRange(keyBytes, 16, 48), HMAC_ALGORITHM));
    }

    public static SecretKeys generateKeyFromPassword(String password, String salt) throws GeneralSecurityException {
        return generateKeyFromPassword(password, salt, 10000);
    }

    public static SecretKeys generateKeyFromPassword(String password, String salt, int iterationCount) throws GeneralSecurityException {
        return generateKeyFromPassword(password, Base64.decode(salt, 2), iterationCount);
    }

    public static byte[] generateSalt() throws GeneralSecurityException {
        return randomBytes(128);
    }

    public static String saltString(byte[] salt) {
        return Base64.encodeToString(salt, 2);
    }

    public static byte[] generateIv() throws GeneralSecurityException {
        return randomBytes(16);
    }

    private static byte[] randomBytes(int length) throws GeneralSecurityException {
        fixPrng();
        byte[] b = new byte[length];
        new SecureRandom().nextBytes(b);
        return b;
    }

    public static CipherTextIvMac encrypt(String plaintext, SecretKeys secretKeys) throws UnsupportedEncodingException, GeneralSecurityException {
        return encrypt(plaintext, secretKeys, "UTF-8");
    }

    public static CipherTextIvMac encrypt(String plaintext, SecretKeys secretKeys, String encoding) throws UnsupportedEncodingException, GeneralSecurityException {
        return encrypt(plaintext.getBytes(encoding), secretKeys);
    }

    public static CipherTextIvMac encrypt(byte[] plaintext, SecretKeys secretKeys) throws GeneralSecurityException {
        byte[] iv = generateIv();
        Cipher aesCipherForEncryption = Cipher.getInstance(CIPHER_TRANSFORMATION);
        aesCipherForEncryption.init(1, secretKeys.getConfidentialityKey(), new IvParameterSpec(iv));
        iv = aesCipherForEncryption.getIV();
        byte[] byteCipherText = aesCipherForEncryption.doFinal(plaintext);
        return new CipherTextIvMac(byteCipherText, iv, generateMac(CipherTextIvMac.ivCipherConcat(iv, byteCipherText), secretKeys.getIntegrityKey()));
    }

    private static void fixPrng() {
        if (!prngFixed.get()) {
            synchronized (PrngFixes.class) {
                if (!prngFixed.get()) {
                    PrngFixes.apply();
                    prngFixed.set(true);
                }
            }
        }
    }

    public static String decryptString(CipherTextIvMac civ, SecretKeys secretKeys, String encoding) throws UnsupportedEncodingException, GeneralSecurityException {
        return new String(decrypt(civ, secretKeys), encoding);
    }

    public static String decryptString(CipherTextIvMac civ, SecretKeys secretKeys) throws UnsupportedEncodingException, GeneralSecurityException {
        return decryptString(civ, secretKeys, "UTF-8");
    }

    public static byte[] decrypt(CipherTextIvMac civ, SecretKeys secretKeys) throws GeneralSecurityException {
        if (constantTimeEq(generateMac(CipherTextIvMac.ivCipherConcat(civ.getIv(), civ.getCipherText()), secretKeys.getIntegrityKey()), civ.getMac())) {
            Cipher aesCipherForDecryption = Cipher.getInstance(CIPHER_TRANSFORMATION);
            aesCipherForDecryption.init(2, secretKeys.getConfidentialityKey(), new IvParameterSpec(civ.getIv()));
            return aesCipherForDecryption.doFinal(civ.getCipherText());
        }
        throw new GeneralSecurityException("MAC stored in civ does not match computed MAC.");
    }

    public static byte[] generateMac(byte[] byteCipherText, SecretKey integrityKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance(HMAC_ALGORITHM);
        sha256_HMAC.init(integrityKey);
        return sha256_HMAC.doFinal(byteCipherText);
    }

    public static boolean constantTimeEq(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        if (result == 0) {
            return true;
        }
        return false;
    }

    private static byte[] copyOfRange(byte[] from, int start, int end) {
        int length = end - start;
        byte[] result = new byte[length];
        System.arraycopy(from, start, result, 0, length);
        return result;
    }
}
