package com.fimi.kernel.network.okhttp.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class PersistentCookieStore implements CookieStore {
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String LOG_TAG = "PersistentCookieStore";
    private final SharedPreferences cookiePrefs;
    private final HashMap<String, ConcurrentHashMap<String, HttpCookie>> cookies = new HashMap();

    public PersistentCookieStore(Context context) {
        this.cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        for (Entry<String, ?> entry : this.cookiePrefs.getAll().entrySet()) {
            if (!(((String) entry.getValue()) == null || ((String) entry.getValue()).startsWith(COOKIE_NAME_PREFIX))) {
                for (String name : TextUtils.split((String) entry.getValue(), ",")) {
                    String encodedCookie = this.cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        HttpCookie decodedCookie = decodeCookie(encodedCookie);
                        if (decodedCookie != null) {
                            if (!this.cookies.containsKey(entry.getKey())) {
                                this.cookies.put(entry.getKey(), new ConcurrentHashMap());
                            }
                            ((ConcurrentHashMap) this.cookies.get(entry.getKey())).put(name, decodedCookie);
                        }
                    }
                }
            }
        }
    }

    public void add(URI uri, HttpCookie cookie) {
        String name = getCookieToken(uri, cookie);
        if (!cookie.hasExpired()) {
            if (!this.cookies.containsKey(uri.getHost())) {
                this.cookies.put(uri.getHost(), new ConcurrentHashMap());
            }
            ((ConcurrentHashMap) this.cookies.get(uri.getHost())).put(name, cookie);
        } else if (this.cookies.containsKey(uri.toString())) {
            ((ConcurrentHashMap) this.cookies.get(uri.getHost())).remove(name);
        }
        Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.putString(uri.getHost(), TextUtils.join(",", ((ConcurrentHashMap) this.cookies.get(uri.getHost())).keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableHttpCookie(cookie)));
        prefsWriter.commit();
    }

    /* Access modifiers changed, original: protected */
    public String getCookieToken(URI uri, HttpCookie cookie) {
        return cookie.getName() + cookie.getDomain();
    }

    public List<HttpCookie> get(URI uri) {
        ArrayList<HttpCookie> ret = new ArrayList();
        if (this.cookies.containsKey(uri.getHost())) {
            ret.addAll(((ConcurrentHashMap) this.cookies.get(uri.getHost())).values());
        }
        return ret;
    }

    public boolean removeAll() {
        Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.commit();
        this.cookies.clear();
        return true;
    }

    public boolean remove(URI uri, HttpCookie cookie) {
        String name = getCookieToken(uri, cookie);
        if (!this.cookies.containsKey(uri.getHost()) || !((ConcurrentHashMap) this.cookies.get(uri.getHost())).containsKey(name)) {
            return false;
        }
        ((ConcurrentHashMap) this.cookies.get(uri.getHost())).remove(name);
        Editor prefsWriter = this.cookiePrefs.edit();
        if (this.cookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
            prefsWriter.remove(COOKIE_NAME_PREFIX + name);
        }
        prefsWriter.putString(uri.getHost(), TextUtils.join(",", ((ConcurrentHashMap) this.cookies.get(uri.getHost())).keySet()));
        prefsWriter.commit();
        return true;
    }

    public List<HttpCookie> getCookies() {
        ArrayList<HttpCookie> ret = new ArrayList();
        for (String key : this.cookies.keySet()) {
            ret.addAll(((ConcurrentHashMap) this.cookies.get(key)).values());
        }
        return ret;
    }

    public List<URI> getURIs() {
        ArrayList<URI> ret = new ArrayList();
        for (String key : this.cookies.keySet()) {
            try {
                ret.add(new URI(key));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /* Access modifiers changed, original: protected */
    public String encodeCookie(SerializableHttpCookie cookie) {
        if (cookie == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(os).writeObject(cookie);
            return byteArrayToHexString(os.toByteArray());
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in encodeCookie", e);
            return null;
        }
    }

    /* Access modifiers changed, original: protected */
    public HttpCookie decodeCookie(String cookieString) {
        HttpCookie cookie = null;
        try {
            return ((SerializableHttpCookie) new ObjectInputStream(new ByteArrayInputStream(hexStringToByteArray(cookieString))).readObject()).getCookie();
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in decodeCookie", e);
            return cookie;
        } catch (ClassNotFoundException e2) {
            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e2);
            return cookie;
        }
    }

    /* Access modifiers changed, original: protected */
    public String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 255;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /* Access modifiers changed, original: protected */
    public byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
