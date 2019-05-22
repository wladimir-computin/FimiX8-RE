package com.fimi.app.x8s.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {
    private static Map<String, SoftReference<Bitmap>> imageCache = new HashMap();

    public static SoftReference<Bitmap> addBitmapToCache(Context context, int res) {
        SoftReference<Bitmap> softBitmap = new SoftReference(BitmapFactory.decodeResource(context.getResources(), res));
        imageCache.put("" + res, softBitmap);
        return softBitmap;
    }

    public static Bitmap getBitmapByPath(Context context, int res) {
        SoftReference<Bitmap> softBitmap = (SoftReference) imageCache.get("" + res);
        if (softBitmap == null) {
            return (Bitmap) addBitmapToCache(context, res).get();
        }
        Bitmap bitmap = (Bitmap) softBitmap.get();
        return bitmap == null ? (Bitmap) addBitmapToCache(context, res).get() : bitmap;
    }
}
