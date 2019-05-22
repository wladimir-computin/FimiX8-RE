package com.fimi.kernel.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;

@TargetApi(12)
public class VolleyImageCache implements ImageCache {
    private static LruCache<String, Bitmap> mCache;
    private static VolleyImageCache mImageCache;

    public VolleyImageCache() {
        mCache = new LruCache<String, Bitmap>(((int) Runtime.getRuntime().maxMemory()) / 8) {
            /* Access modifiers changed, original: protected */
            public int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public static VolleyImageCache getInstance() {
        if (mImageCache == null) {
            mImageCache = new VolleyImageCache();
        }
        return mImageCache;
    }

    public Bitmap getBitmap(String cacheKey) {
        return (Bitmap) mCache.get(cacheKey);
    }

    public void putBitmap(String cacheKey, Bitmap bitmap) {
        mCache.put(cacheKey, bitmap);
    }

    public void removeBitmap(String requestUrl, int maxWidth, int maxHeight) {
        mCache.remove(getCacheKey(requestUrl, maxWidth, maxHeight));
    }

    public String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth).append("#H").append(maxHeight).append(url).toString();
    }

    public void clearBitmap() {
        mCache.evictAll();
    }
}
