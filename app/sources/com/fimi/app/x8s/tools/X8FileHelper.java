package com.fimi.app.x8s.tools;

import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.FileUtil;
import com.fimi.x8sdk.X8FcLogManager;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class X8FileHelper {
    private static String[] zipFileSuffix = new String[]{X8FcLogManager.prexFC, X8FcLogManager.prexCM, X8FcLogManager.prexAPP, X8FcLogManager.prexFcStatus};

    public void test() {
        Matcher matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher("[INFO][2018-04 10:29:08 911][http-nio-6900-exec-8]");
        if (matcher.find()) {
            System.out.println(matcher.groupCount());
            System.out.println(matcher.group(0));
        }
    }

    public static void deleteFiles() {
        boolean b = FileUtil.deleteFile(new File(DirectoryPath.getX8B2oxPath()));
    }

    public static List<File> listDirs() {
        return FileUtil.listDirs(new File(DirectoryPath.getX8B2oxPath()), X8FcLogManager.prexSD, X8FcLogManager.getInstance().getCurrentWrite());
    }

    public static long getDirSize(File file) {
        long len = 0;
        List<File> list = FileUtil.listFiles3(file, zipFileSuffix);
        if (list != null && list.size() > 0) {
            for (File f : list) {
                len += f.length();
            }
        }
        return len;
    }
}
