package com.fimi.kernel.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.IOException;

public class DirectoryPath {
    public static final int ROOT = 0;
    public static final String TAG = "DirectoryPath";
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_THUMBNAIL = 2;
    public static final int TYPE_VIDEO = 3;

    public static String getFimiAppPath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator + "FimiDir";
    }

    public static String getFwPath(String name) {
        return Environment.getExternalStorageDirectory().getPath() + "/FimiLogger/firmware/" + name;
    }

    public static String getX9MediaListPath(String name) {
        return Environment.getExternalStorageDirectory().getPath() + "/FimiLogger/X9/" + name;
    }

    public static String getAppLogPath() {
        return getFimiAppPath() + File.separator + "fmlog";
    }

    public static String getMediaImageLibrary() {
        return Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + "/MiSmartphoneGimbal/" + "Image/";
    }

    public static String getGh2MediaLibrary() {
        return Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + "/MiSmartphoneGimbal/";
    }

    public static String getGh2MediaLibrary(Context context, int type) {
        return getFolderPath(context, type, Environment.getExternalStorageDirectory().getPath() + File.separator + "DCIM" + File.separator + "MiSmartphoneGimbal");
    }

    public static String getGh2Path() {
        return Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + "/MiSmartphoneGimbal/";
    }

    public static String getFirmwarePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/FimiLogger/firmware";
    }

    public static String getFwTempFilePath() {
        return getFirmwarePath() + "/temp";
    }

    public static String getX9LocalMedia() {
        return Environment.getExternalStorageDirectory().getPath() + "/DCIM" + "/MiDroneMiniImage";
    }

    public static String getX8LocalMedia() {
        return Environment.getExternalStorageDirectory().getPath() + "/x8/media/orgin";
    }

    public static String getX8LocalSar() {
        return Environment.getExternalStorageDirectory().getPath() + "/DCIM" + "/FimiX8/SAR";
    }

    public static String getFileOfMd5(File file) {
        try {
            return AbMd5.getFileMD5String(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileOfMd5(String filePath) {
        return getFileOfMd5(new File(filePath));
    }

    private static String getFolderPath(Context context, int type, String rootPath) {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(rootPath);
        pathBuilder.append(File.separator);
        pathBuilder.append(File.separator);
        pathBuilder.append(File.separator);
        switch (type) {
            case 1:
                pathBuilder.append("Image");
                break;
            case 2:
                pathBuilder.append(".Thumbnail");
                break;
            case 3:
                pathBuilder.append("Image");
                break;
        }
        return pathBuilder.toString() + File.separator;
    }

    public static String getApkPath() {
        return Environment.getExternalStorageDirectory() + "/FimiLogger/apk/";
    }

    public static String getX8B2oxPath() {
        return Environment.getExternalStorageDirectory() + "/FimiLogger/x8/box";
    }
}
