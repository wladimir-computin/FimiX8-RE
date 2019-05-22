package com.fimi.libperson.entity;

import android.graphics.Bitmap;
import android.net.Uri;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ImageSource {
    private final Bitmap bitmap;
    private final Integer resource;
    private final boolean tile;
    private final Uri uri;

    public static ImageSource asset(String assetName) {
        if (assetName != null) {
            return uri("file:///android_asset/" + assetName);
        }
        throw new NullPointerException("Asset name must not be null");
    }

    public static ImageSource uri(String uri) {
        if (uri == null) {
            throw new NullPointerException("Uri must not be null");
        }
        if (!uri.contains("://")) {
            if (uri.startsWith("/")) {
                uri = uri.substring(1);
            }
            uri = "file:///" + uri;
        }
        return new ImageSource(Uri.parse(uri));
    }

    private ImageSource(Uri uri) {
        String uriString = uri.toString();
        if (uriString.startsWith("file:///") && !new File(uriString.substring("file:///".length() - 1)).exists()) {
            try {
                uri = Uri.parse(URLDecoder.decode(uriString, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }
        this.bitmap = null;
        this.uri = uri;
        this.resource = null;
        this.tile = true;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Uri getUri() {
        return this.uri;
    }

    public Integer getResource() {
        return this.resource;
    }

    public boolean isTile() {
        return this.tile;
    }
}
