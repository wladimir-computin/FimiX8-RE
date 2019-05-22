package com.fimi.kernel.utils;

import com.file.zip.ZipEntry;
import com.file.zip.ZipFile;
import com.file.zip.ZipOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class ZipTool {
    public static final int EXIST_UNZIPFILE = 2;
    public static final int EXIST_ZIPFILE = 4;
    public static final int NOTEXIST_ZIPFILE = 1;
    public static final int NULL_ZIPPATH = 0;
    public static final int ZIPOPTION_FAIL = 5;
    public static final int ZIPOPTION_SUCCESS = 3;

    public static int unzip(String zipFilePath, String unzipPath) {
        if (zipFilePath == null || "".equals(zipFilePath.trim())) {
            return 0;
        }
        File dirFile = new File(unzipPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(zipFilePath);
        if (!file.exists()) {
            return 1;
        }
        try {
            ZipFile zipFile = new ZipFile(file, "GBK");
            Enumeration<? extends ZipEntry> entries = zipFile.getEntries();
            String unzipAbpath = dirFile.getAbsolutePath();
            while (entries.hasMoreElements()) {
                unzipEachFile(zipFile, (ZipEntry) entries.nextElement(), unzipAbpath);
            }
            return 3;
        } catch (IOException e) {
            e.printStackTrace();
            return 5;
        }
    }

    private static void unzipEachFile(ZipFile zipFile, ZipEntry entry, String unzipAbpath) {
        File tempFile;
        Exception e;
        Throwable th;
        byte[] buffer = new byte[5120];
        String name = entry.getName();
        String fileName = name;
        String tempDir = "";
        if (entry.isDirectory()) {
            tempFile = new File(unzipAbpath + File.separator + name);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
        }
        int index = name.lastIndexOf(File.separator);
        if (index != -1) {
            fileName = name.substring(index, name.length());
            tempDir = name.substring(0, index);
            File tempDirFile = new File(unzipAbpath + File.separator + tempDir);
            if (!tempDirFile.exists()) {
                tempDirFile.mkdirs();
            }
        }
        tempFile = new File(unzipAbpath + File.separator + tempDir + fileName);
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = zipFile.getInputStream(entry);
            if (!tempFile.exists()) {
                tempFile.createNewFile();
            }
            FileOutputStream fos2 = new FileOutputStream(tempFile);
            while (true) {
                try {
                    int readSize = is.read(buffer);
                    if (readSize > 0) {
                        fos2.write(buffer, 0, readSize);
                    } else {
                        try {
                            is.close();
                            fos2.close();
                            fos = fos2;
                            return;
                        } catch (IOException e2) {
                            new File(unzipAbpath).delete();
                            e2.printStackTrace();
                            fos = fos2;
                            return;
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    fos = fos2;
                    try {
                        new File(unzipAbpath).delete();
                        e.printStackTrace();
                        try {
                            is.close();
                            fos.close();
                        } catch (IOException e22) {
                            new File(unzipAbpath).delete();
                            e22.printStackTrace();
                            return;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            is.close();
                            fos.close();
                        } catch (IOException e222) {
                            new File(unzipAbpath).delete();
                            e222.printStackTrace();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    is.close();
                    fos.close();
                    throw th;
                }
            }
        } catch (Exception e4) {
            e = e4;
            new File(unzipAbpath).delete();
            e.printStackTrace();
            is.close();
            fos.close();
        }
    }

    public static int zip(String newZipPath, File[] wantZipFile) {
        Throwable th;
        int i = 0;
        File file = new File(newZipPath);
        if (file.exists()) {
            return 4;
        }
        String filePath = file.getAbsolutePath();
        File dirFile = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        ZipOutputStream zos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ZipOutputStream zos2 = new ZipOutputStream(new FileOutputStream(file));
            try {
                int length = wantZipFile.length;
                while (i < length) {
                    File tempFile = wantZipFile[i];
                    if (tempFile.exists()) {
                        zipEachFile(zos2, tempFile, "");
                    }
                    i++;
                }
                try {
                    zos2.close();
                    return 3;
                } catch (IOException e) {
                    file.delete();
                    e.printStackTrace();
                    return 5;
                }
            } catch (IOException e2) {
                zos = zos2;
                try {
                    file.delete();
                    try {
                        zos.close();
                        return 5;
                    } catch (IOException e3) {
                        file.delete();
                        e3.printStackTrace();
                        return 5;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        zos.close();
                        throw th;
                    } catch (IOException e32) {
                        file.delete();
                        e32.printStackTrace();
                        return 5;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                zos = zos2;
                zos.close();
                throw th;
            }
        } catch (IOException e4) {
            file.delete();
            zos.close();
            return 5;
        }
    }

    private static void zipEachFile(ZipOutputStream zos, File zipFile, String baseDir) {
        IOException e;
        ZipEntry zipEntry;
        Throwable th;
        if (zipFile.isDirectory()) {
            baseDir = baseDir + zipFile.getName() + File.separator;
            for (File tempFile : zipFile.listFiles()) {
                zipEachFile(zos, tempFile, baseDir);
            }
            return;
        }
        FileInputStream fis = null;
        try {
            FileInputStream fis2;
            ZipEntry entry = new ZipEntry(baseDir + zipFile.getName());
            try {
                zos.putNextEntry(entry);
                fis2 = new FileInputStream(zipFile);
            } catch (IOException e2) {
                e = e2;
                zipEntry = entry;
                try {
                    e.printStackTrace();
                    try {
                        fis.close();
                        zos.closeEntry();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        fis.close();
                        zos.closeEntry();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                zipEntry = entry;
                fis.close();
                zos.closeEntry();
                throw th;
            }
            try {
                byte[] buffer = new byte[5120];
                while (true) {
                    int readSize = fis2.read(buffer);
                    if (readSize > 0) {
                        zos.write(buffer, 0, readSize);
                    } else {
                        try {
                            fis2.close();
                            zos.closeEntry();
                            zipEntry = entry;
                            return;
                        } catch (IOException e322) {
                            e322.printStackTrace();
                            zipEntry = entry;
                            return;
                        }
                    }
                }
            } catch (IOException e4) {
                e322 = e4;
                fis = fis2;
                zipEntry = entry;
                e322.printStackTrace();
                fis.close();
                zos.closeEntry();
            } catch (Throwable th4) {
                th = th4;
                fis = fis2;
                zipEntry = entry;
                fis.close();
                zos.closeEntry();
                throw th;
            }
        } catch (IOException e5) {
            e322 = e5;
            e322.printStackTrace();
            fis.close();
            zos.closeEntry();
        }
    }
}
