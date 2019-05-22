package com.fimi.kernel.utils;

import android.content.Context;
import java.util.HashMap;

public class MediaSDK {
    private static HashMap mapDur = new HashMap();
    private static MediaSDK mediaSDK = null;

    public static MediaSDK getInstance() {
        if (mediaSDK == null) {
            mediaSDK = new MediaSDK();
        }
        return mediaSDK;
    }

    public void init(Context context) {
        FrescoImageLoader.initFresco(context);
    }

    public void shutdown() {
        FrescoImageLoader.shutdown();
    }

    public static HashMap<String, String> getMapDur() {
        return mapDur;
    }
}
