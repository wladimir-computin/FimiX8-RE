package com.fimi.kernel.fds;

import com.file.zip.ZipEntry;
import com.file.zip.ZipOutputStream;
import com.fimi.kernel.utils.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Zip2Fds {
    public static final int EXIST_UNZIPFILE = 2;
    public static final int EXIST_ZIPFILE = 4;
    public static final int NOTEXIST_ZIPFILE = 1;
    public static final int NULL_ZIPPATH = 0;
    public static final int ZIPOPTION_FAIL = 5;
    public static final int ZIPOPTION_SUCCESS = 3;
    FileInputStream fis = null;
    private FileOutputStream fos;
    private boolean isStop;
    private ZipOutputStream zos = null;

    public boolean log2Zip(File file, String[] suffix) {
        String zipName = file.getName() + ".zip";
        List<File> list = FileUtil.listFiles3(file, suffix);
        if (list == null || list.size() <= 0) {
            return false;
        }
        int code = zip2(file.getAbsolutePath(), zipName, list);
        if (code == 3 || code == 4) {
            return true;
        }
        return false;
    }

    private void zipEachFile(ZipOutputStream zos, File zipFile, String baseDir) {
        IOException e;
        Throwable th;
        try {
            ZipEntry entry = new ZipEntry(baseDir + zipFile.getName());
            ZipEntry zipEntry;
            try {
                zos.putNextEntry(entry);
                this.fis = new FileInputStream(zipFile);
                byte[] buffer = new byte[5120];
                while (true) {
                    int readSize = this.fis.read(buffer);
                    if (readSize <= 0 || this.isStop) {
                        try {
                            this.fis.close();
                            this.fis = null;
                            zos.closeEntry();
                            zipEntry = entry;
                            return;
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            zipEntry = entry;
                            return;
                        }
                    }
                    zos.write(buffer, 0, readSize);
                }
            } catch (IOException e3) {
                e2 = e3;
                zipEntry = entry;
                try {
                    e2.printStackTrace();
                    try {
                        this.fis.close();
                        this.fis = null;
                        zos.closeEntry();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        this.fis.close();
                        this.fis = null;
                        zos.closeEntry();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                zipEntry = entry;
                this.fis.close();
                this.fis = null;
                zos.closeEntry();
                throw th;
            }
        } catch (IOException e4) {
            e222 = e4;
            e222.printStackTrace();
            this.fis.close();
            this.fis = null;
            zos.closeEntry();
        }
    }

    public int zip2(String fileDir, String name, List<File> wantZipFile) {
        File file1 = new File(fileDir + "/" + name);
        if (file1.exists()) {
            return 4;
        }
        File file = new File(fileDir + "/" + ("" + name.hashCode() + ".zip"));
        try {
            this.fos = new FileOutputStream(file);
            this.zos = new ZipOutputStream(this.fos);
            loop0:
            for (File tempFile : wantZipFile) {
                if (tempFile.exists()) {
                    if (!this.isStop) {
                        zipEachFile(this.zos, tempFile, "");
                    }
                }
            }
            if (!this.isStop) {
                file.renameTo(file1);
            }
            return 3;
        } catch (IOException e) {
            file.delete();
            return 5;
        } finally {
            try {
                this.zos.close();
            } catch (IOException e2) {
                file.delete();
                e2.printStackTrace();
                return 5;
            }
        }
    }

    public void stop() {
        this.isStop = true;
        if (this.fos != null) {
            try {
                this.fos.close();
            } catch (IOException e) {
            }
            this.fos = null;
        }
        if (this.zos != null) {
            try {
                this.zos.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (this.fis != null) {
            try {
                this.fis.close();
            } catch (IOException e3) {
            }
            this.fis = null;
        }
    }
}
