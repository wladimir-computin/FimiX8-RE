package com.fimi.kernel.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;

public class VideoDuration {
    public static long getVideoDuration(Context context, String filePath) {
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(filePath);
            long dur = Long.parseLong(mmr.extractMetadata(9));
            if (dur == 0) {
                return (long) MediaPlayer.create(context, Uri.parse(filePath)).getDuration();
            }
            return dur;
        } catch (Exception e) {
            return 0;
        }
    }
}
