package com.fimi.kernel.utils;

import android.content.Context;
import android.os.Environment;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class FileUtil {
    public static final int BUFSIZE = 8192;

    public static String[] getFileList(String path, FilenameFilter filter) {
        File mPath = new File(path);
        try {
            mPath.mkdirs();
            if (mPath.exists()) {
                return mPath.list(filter);
            }
        } catch (SecurityException e) {
        }
        return new String[0];
    }

    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals("mounted");
        } catch (Exception e) {
            LogUtil.e("", "手机SD卡不能用:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFile(File file) {
        try {
            if (!isCanUseSD()) {
                return false;
            }
            if (file == null) {
                return true;
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File deleteFile : files) {
                    deleteFile(deleteFile);
                }
                file.delete();
            } else {
                file.delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String createFileNmae(String extension) {
        String formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(new Date());
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        return formatDate + extension;
    }

    public static List<File> listFiles(String file, String format) {
        return listFiles(new File(file), format, null);
    }

    public static List<File> listFiles(File file, final String extension, final String content) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return null;
        }
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                if (content == null || content.equals("")) {
                    return arg1.endsWith(extension);
                }
                return arg1.contains(content) && arg1.endsWith(extension);
            }
        });
        if (files == null) {
            return null;
        }
        List<File> list = new ArrayList(Arrays.asList(files));
        sortList(list, false);
        return list;
    }

    public static List<File> listFiles2(File file, final String endwith1, final String endwith2) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return null;
        }
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(endwith2) || arg1.endsWith(endwith1);
            }
        });
        if (files == null) {
            return null;
        }
        List<File> list = new ArrayList(Arrays.asList(files));
        sortList(list, false);
        return list;
    }

    public static void sortList(List<File> list, final boolean asc) {
        Collections.sort(list, new Comparator<File>() {
            public int compare(File file, File newFile) {
                if (file.lastModified() > newFile.lastModified()) {
                    if (asc) {
                        return 1;
                    }
                    return -1;
                } else if (file.lastModified() == newFile.lastModified()) {
                    return 0;
                } else {
                    if (asc) {
                        return -1;
                    }
                    return 1;
                }
            }
        });
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0025 A:{SYNTHETIC, Splitter:B:15:0x0025} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0031 A:{SYNTHETIC, Splitter:B:21:0x0031} */
    public static void addFileContent(java.lang.String r6, byte[] r7) {
        /*
        r1 = 0;
        r2 = new java.io.RandomAccessFile;	 Catch:{ IOException -> 0x001f }
        r3 = "rw";
        r2.<init>(r6, r3);	 Catch:{ IOException -> 0x001f }
        r4 = r2.length();	 Catch:{ IOException -> 0x003d, all -> 0x003a }
        r2.seek(r4);	 Catch:{ IOException -> 0x003d, all -> 0x003a }
        r2.write(r7);	 Catch:{ IOException -> 0x003d, all -> 0x003a }
        if (r2 == 0) goto L_0x0040;
    L_0x0014:
        r2.close();	 Catch:{ IOException -> 0x0019 }
        r1 = r2;
    L_0x0018:
        return;
    L_0x0019:
        r0 = move-exception;
        r0.printStackTrace();
        r1 = r2;
        goto L_0x0018;
    L_0x001f:
        r0 = move-exception;
    L_0x0020:
        r0.printStackTrace();	 Catch:{ all -> 0x002e }
        if (r1 == 0) goto L_0x0018;
    L_0x0025:
        r1.close();	 Catch:{ IOException -> 0x0029 }
        goto L_0x0018;
    L_0x0029:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0018;
    L_0x002e:
        r3 = move-exception;
    L_0x002f:
        if (r1 == 0) goto L_0x0034;
    L_0x0031:
        r1.close();	 Catch:{ IOException -> 0x0035 }
    L_0x0034:
        throw r3;
    L_0x0035:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0034;
    L_0x003a:
        r3 = move-exception;
        r1 = r2;
        goto L_0x002f;
    L_0x003d:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0020;
    L_0x0040:
        r1 = r2;
        goto L_0x0018;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.utils.FileUtil.addFileContent(java.lang.String, byte[]):void");
    }

    public static byte[] getFileBytes(String filePath) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            while (true) {
                int n = fis.read(b);
                if (n != -1) {
                    bos.write(b, 0, n);
                } else {
                    fis.close();
                    bos.close();
                    return bos.toByteArray();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return buffer;
        } catch (IOException e2) {
            e2.printStackTrace();
            return buffer;
        }
    }

    public static long getFileLenght(String path) {
        return new File(path).length();
    }

    public static void meragerFiles(String outFileName, String[] fileNames) {
        FileChannel outChannel = null;
        try {
            outChannel = new FileOutputStream(outFileName).getChannel();
            for (String fw : fileNames) {
                FileChannel fc = new FileInputStream(fw).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(8192);
                while (fc.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e2) {
                }
            }
        } catch (Throwable th) {
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e3) {
                }
            }
        }
    }

    public static void createFile(String pathName) {
        File f = new File(pathName);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mergeMultipleVideoFile(String outPath, String videoPath1, String videoPath2) {
        try {
            List<String> fileList = new ArrayList();
            List<Movie> movieList = new ArrayList();
            fileList.add(videoPath1);
            fileList.add(videoPath2);
            for (String filePath : fileList) {
                movieList.add(MovieCreator.build(filePath));
            }
            List<Track> videoTracks = new LinkedList();
            List<Track> audioTracks = new LinkedList();
            for (Movie movie : movieList) {
                for (Track track : movie.getTracks()) {
                    if (track.getHandler().equals("soun")) {
                        audioTracks.add(track);
                    }
                    if (track.getHandler().equals("vide")) {
                        videoTracks.add(track);
                    }
                }
            }
            Movie result = new Movie();
            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack((Track[]) audioTracks.toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack((Track[]) videoTracks.toArray(new Track[videoTracks.size()])));
            }
            writeMergeNewFile(outPath, result);
            deleteFile(new File(videoPath1));
            deleteFile(new File(videoPath2));
            fileList.clear();
            movieList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0035 A:{SYNTHETIC, Splitter:B:21:0x0035} */
    /* JADX WARNING: Removed duplicated region for block: B:33:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0029 A:{SYNTHETIC, Splitter:B:15:0x0029} */
    private static void writeMergeNewFile(java.lang.String r5, com.googlecode.mp4parser.authoring.Movie r6) {
        /*
        r2 = 0;
        r4 = new com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;	 Catch:{ Exception -> 0x0023 }
        r4.<init>();	 Catch:{ Exception -> 0x0023 }
        r0 = r4.build(r6);	 Catch:{ Exception -> 0x0023 }
        r3 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0023 }
        r3.<init>(r5);	 Catch:{ Exception -> 0x0023 }
        r4 = r3.getChannel();	 Catch:{ Exception -> 0x0041, all -> 0x003e }
        r0.writeContainer(r4);	 Catch:{ Exception -> 0x0041, all -> 0x003e }
        if (r3 == 0) goto L_0x0044;
    L_0x0018:
        r3.close();	 Catch:{ Exception -> 0x001d }
        r2 = r3;
    L_0x001c:
        return;
    L_0x001d:
        r1 = move-exception;
        r1.printStackTrace();
        r2 = r3;
        goto L_0x001c;
    L_0x0023:
        r1 = move-exception;
    L_0x0024:
        r1.printStackTrace();	 Catch:{ all -> 0x0032 }
        if (r2 == 0) goto L_0x001c;
    L_0x0029:
        r2.close();	 Catch:{ Exception -> 0x002d }
        goto L_0x001c;
    L_0x002d:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x001c;
    L_0x0032:
        r4 = move-exception;
    L_0x0033:
        if (r2 == 0) goto L_0x0038;
    L_0x0035:
        r2.close();	 Catch:{ Exception -> 0x0039 }
    L_0x0038:
        throw r4;
    L_0x0039:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0038;
    L_0x003e:
        r4 = move-exception;
        r2 = r3;
        goto L_0x0033;
    L_0x0041:
        r1 = move-exception;
        r2 = r3;
        goto L_0x0024;
    L_0x0044:
        r2 = r3;
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.utils.FileUtil.writeMergeNewFile(java.lang.String, com.googlecode.mp4parser.authoring.Movie):void");
    }

    public static void createFileAndPaperFile(String fileName) {
        File file = new File(fileName);
        if (fileName.indexOf(".") != -1) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!file.exists()) {
            file.mkdir();
        }
    }

    public static String fileToString(String filePath) {
        try {
            if (!new File(filePath).exists()) {
                return null;
            }
            StringBuffer buffer = new StringBuffer();
            BufferedReader bf = new BufferedReader(new FileReader(filePath));
            while (true) {
                String s = bf.readLine();
                if (s != null) {
                    buffer.append(s.trim());
                } else {
                    bf.close();
                    return buffer.toString();
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String fileToString(InputStream in) {
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String s = bf.readLine();
                if (s != null) {
                    buffer.append(s.trim());
                } else {
                    bf.close();
                    in.close();
                    return buffer.toString();
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean deleteFiles(File file, String prexFL, String prexFLSD) {
        try {
            if (!isCanUseSD()) {
                return false;
            }
            if (file == null) {
                return true;
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File deleteFile : files) {
                    deleteFile(deleteFile);
                }
            } else if (file.getName().endsWith(prexFL) || file.getName().endsWith(prexFLSD)) {
                file.delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readAssertResource(Context context, String strAssertFileName) {
        String strResponse = "";
        try {
            return getStringFromInputStream(context.getAssets().open(strAssertFileName));
        } catch (IOException e) {
            e.printStackTrace();
            return strResponse;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x001e A:{SYNTHETIC, Splitter:B:11:0x001e} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0033 A:{SYNTHETIC, Splitter:B:23:0x0033} */
    private static java.lang.String getStringFromInputStream(java.io.InputStream r6) {
        /*
        r0 = 0;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x003e, all -> 0x0030 }
        r4 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x003e, all -> 0x0030 }
        r4.<init>(r6);	 Catch:{ IOException -> 0x003e, all -> 0x0030 }
        r1.<init>(r4);	 Catch:{ IOException -> 0x003e, all -> 0x0030 }
    L_0x0010:
        r2 = r1.readLine();	 Catch:{ IOException -> 0x001a, all -> 0x003b }
        if (r2 == 0) goto L_0x0026;
    L_0x0016:
        r3.append(r2);	 Catch:{ IOException -> 0x001a, all -> 0x003b }
        goto L_0x0010;
    L_0x001a:
        r4 = move-exception;
        r0 = r1;
    L_0x001c:
        if (r0 == 0) goto L_0x0021;
    L_0x001e:
        r0.close();	 Catch:{ IOException -> 0x0037 }
    L_0x0021:
        r4 = r3.toString();
        return r4;
    L_0x0026:
        if (r1 == 0) goto L_0x0040;
    L_0x0028:
        r1.close();	 Catch:{ IOException -> 0x002d }
        r0 = r1;
        goto L_0x0021;
    L_0x002d:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0021;
    L_0x0030:
        r4 = move-exception;
    L_0x0031:
        if (r0 == 0) goto L_0x0036;
    L_0x0033:
        r0.close();	 Catch:{ IOException -> 0x0039 }
    L_0x0036:
        throw r4;
    L_0x0037:
        r4 = move-exception;
        goto L_0x0021;
    L_0x0039:
        r5 = move-exception;
        goto L_0x0036;
    L_0x003b:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0031;
    L_0x003e:
        r4 = move-exception;
        goto L_0x001c;
    L_0x0040:
        r0 = r1;
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.utils.FileUtil.getStringFromInputStream(java.io.InputStream):java.lang.String");
    }

    public static List<File> listDirs(File file, final String endwith2, final String endwith1) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return null;
        }
        final Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{3}");
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                if (!new File(arg0.getAbsolutePath() + "/" + arg1).isDirectory() || arg1.equals(endwith1)) {
                    return false;
                }
                int len = arg1.length();
                if ((len != 23 && len != 26) || !p.matcher(arg1).find()) {
                    return false;
                }
                if (len == 23) {
                    return true;
                }
                if (arg1.endsWith(endwith2)) {
                    return true;
                }
                return false;
            }
        });
        if (files == null) {
            return null;
        }
        List<File> list = new ArrayList(Arrays.asList(files));
        sortList(list, false);
        return list;
    }

    public static List<File> listFiles3(File file, final String[] suffix) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return null;
        }
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                for (String endwith : suffix) {
                    if (arg1.endsWith(endwith)) {
                        return true;
                    }
                }
                return false;
            }
        });
        if (files == null) {
            return null;
        }
        List<File> list = new ArrayList(Arrays.asList(files));
        sortList(list, false);
        return list;
    }
}
