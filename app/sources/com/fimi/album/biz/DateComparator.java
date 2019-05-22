package com.fimi.album.biz;

import com.fimi.album.entity.MediaModel;
import java.util.Comparator;

public class DateComparator {
    public static Comparator createDateComparator() {
        return new Comparator<MediaModel>() {
            public int compare(MediaModel mediaModel, MediaModel t1) {
                return Long.valueOf(t1.getCreateDate()).compareTo(Long.valueOf(mediaModel.getCreateDate()));
            }
        };
    }
}
