package com.fimi.kernel.utils;

import android.text.TextUtils;
import java.io.File;

public class FileTool {
    public static void deleteFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
