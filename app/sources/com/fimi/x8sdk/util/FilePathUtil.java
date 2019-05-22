package com.fimi.x8sdk.util;

import android.os.Environment;

public class FilePathUtil {
    public static String UPDATE_CONFIG_FILE = "fm_package_30.ini";
    public static String path = Environment.getExternalStorageDirectory().getPath();

    public static String getX8RootPath() {
        return path + "/x8";
    }

    public static String getFmPackageInfo() {
        return getFwPath() + "/" + UPDATE_CONFIG_FILE;
    }

    public static String getFwPath() {
        return getX8RootPath() + "/firmware";
    }

    public static String getFwTempFilePath() {
        return getFwPath() + "/temp";
    }
}
