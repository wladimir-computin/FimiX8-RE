package com.fimi.kernel.network.okhttp.https;

import ch.qos.logback.core.net.ssl.SSL;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpsUtils {

    private static class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            this.defaultTrustManager = HttpsUtils.chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                this.defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException e) {
                this.localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public static SSLSocketFactory getSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance(SSL.DEFAULT_PROTOCOL);
            sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e2) {
            throw new AssertionError(e2);
        } catch (KeyStoreException e3) {
            throw new AssertionError(e3);
        }
    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        TrustManager[] trustManagerArr = null;
        if (certificates == null || certificates.length <= 0) {
            return trustManagerArr;
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int length = certificates.length;
            int i = 0;
            int index = 0;
            while (i < length) {
                InputStream certificate = certificates[i];
                int index2 = index + 1;
                keyStore.setCertificateEntry(Integer.toString(index), certificateFactory.generateCertificate(certificate));
                if (certificate != null) {
                    try {
                        certificate.close();
                    } catch (IOException e) {
                    }
                }
                i++;
                index = index2;
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return trustManagerArr;
        } catch (CertificateException e3) {
            e3.printStackTrace();
            return trustManagerArr;
        } catch (KeyStoreException e4) {
            e4.printStackTrace();
            return trustManagerArr;
        } catch (Exception e5) {
            e5.printStackTrace();
            return trustManagerArr;
        }
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        KeyManager[] keyManagerArr = null;
        if (bksFile == null || password == null) {
            return keyManagerArr;
        }
        try {
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return keyManagerArr;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return keyManagerArr;
        } catch (UnrecoverableKeyException e3) {
            e3.printStackTrace();
            return keyManagerArr;
        } catch (CertificateException e4) {
            e4.printStackTrace();
            return keyManagerArr;
        } catch (IOException e5) {
            e5.printStackTrace();
            return keyManagerArr;
        } catch (Exception e6) {
            e6.printStackTrace();
            return keyManagerArr;
        }
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    public static SSLSocketFactory initSSLSocketFactory() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance(SSL.DEFAULT_PROTOCOL);
            sslContext.init(null, new X509TrustManager[]{initTrustManager()}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }

    public static X509TrustManager initTrustManager() {
        return new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
    }
}
