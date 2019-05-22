package com.fimi.kernel.network.okhttp.cookie;

import java.util.ArrayList;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public final class SimpleCookieJar implements CookieJar {
    private final List<Cookie> allCookies = new ArrayList();

    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.allCookies.addAll(cookies);
    }

    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> result;
        result = new ArrayList();
        for (Cookie cookie : this.allCookies) {
            if (cookie.matches(url)) {
                result.add(cookie);
            }
        }
        return result;
    }
}
