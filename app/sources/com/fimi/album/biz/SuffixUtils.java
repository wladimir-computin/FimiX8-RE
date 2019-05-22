package com.fimi.album.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuffixUtils {
    private static SuffixUtils mSuffixUtils;
    public String fileFormatJpg = ".JPG";
    public String fileFormatMp4 = ".MP4";
    public String fileFormatRlv = ".RLV";
    public String fileFormatThm = ".THM";
    String[] photoStrings = new String[]{".jpg", this.fileFormatJpg, ".png", "PNG"};
    private List<String> photoSuffixList = new ArrayList();
    String[] videoStrings = new String[]{this.fileFormatThm};
    private List<String> videoSuffixList = new ArrayList();
    private List<String> videoThumSuffixList = new ArrayList();

    private SuffixUtils() {
        this.videoSuffixList.addAll(Arrays.asList(this.videoStrings));
        this.photoSuffixList.addAll(Arrays.asList(this.photoStrings));
    }

    public static SuffixUtils obtain() {
        if (mSuffixUtils == null) {
            synchronized (SuffixUtils.class) {
                if (mSuffixUtils == null) {
                    mSuffixUtils = new SuffixUtils();
                }
            }
        }
        return mSuffixUtils;
    }

    public boolean judgeFileType(String filePathSuffix) {
        if (!filePathSuffix.contains(".")) {
            return false;
        }
        filePathSuffix = filePathSuffix.substring(filePathSuffix.lastIndexOf("."), filePathSuffix.length());
        if (this.videoSuffixList.contains(filePathSuffix)) {
            return true;
        }
        if (this.photoSuffixList.contains(filePathSuffix)) {
        }
        return false;
    }

    public boolean judgeVideo(String filePathSuffix) {
        if (!filePathSuffix.contains(".")) {
            return false;
        }
        if (this.videoSuffixList.contains(filePathSuffix.substring(filePathSuffix.lastIndexOf("."), filePathSuffix.length()))) {
            return true;
        }
        return false;
    }

    public boolean judgePhotho(String filePathSuffix) {
        if (!filePathSuffix.contains(".")) {
            return false;
        }
        if (this.photoSuffixList.contains(filePathSuffix.substring(filePathSuffix.lastIndexOf("."), filePathSuffix.length()))) {
            return true;
        }
        return false;
    }
}
