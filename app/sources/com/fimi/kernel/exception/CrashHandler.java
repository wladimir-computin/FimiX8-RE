package com.fimi.kernel.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.fimi.kernel.animutils.IOUtils;
import com.github.moduth.blockcanary.internal.BlockInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CrashHandler implements UncaughtExceptionHandler {
    private static String CRASH_LOG_FILEPATH = "/FimiLogger/CrashLogger/";
    public static final String TAG = "CrashHandler";
    private static CrashHandler instance;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Map<String, String> infos = new HashMap();
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (handleException(ex) || this.mDefaultHandler == null) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
            return;
        }
        this.mDefaultHandler.uncaughtException(thread, ex);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        collectDeviceInfo(this.mContext);
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(CrashHandler.this.mContext, "很抱歉,程序出现异常,即将退出.", 0).show();
                Looper.loop();
            }
        }.start();
        saveCatchInfo2File(ex);
        return true;
    }

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 1);
            if (pi != null) {
                String versionCode = pi.versionCode + "";
                this.infos.put(BlockInfo.KEY_VERSION_NAME, pi.versionName == null ? "null" : pi.versionName);
                this.infos.put(BlockInfo.KEY_VERSION_CODE, versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e2) {
                Log.e(TAG, "an error occured when collect crash info", e2);
            }
        }
    }

    private String getFilePath() {
        String file_dir = "";
        boolean isSDCardExist = "mounted".equals(Environment.getExternalStorageState());
        boolean isRootDirExist = Environment.getExternalStorageDirectory().exists();
        if (isSDCardExist && isRootDirExist) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + CRASH_LOG_FILEPATH;
        }
        return file_dir;
    }

    private String saveCatchInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String> entry : this.infos.entrySet()) {
            String value = (String) entry.getValue();
            sb.append(((String) entry.getKey()) + "=" + value + IOUtils.LINE_SEPARATOR_UNIX);
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        sb.append(writer.toString());
        try {
            String fileName = "crash-" + this.formatter.format(new Date()) + "-" + System.currentTimeMillis() + ".txt";
            String file_dir = getFilePath();
            File dir = new File(file_dir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(file_dir + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            sendCrashLog2PM(file_dir + fileName);
            fos.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            return null;
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:28:0x0060=Splitter:B:28:0x0060, B:19:0x0049=Splitter:B:19:0x0049} */
    private void sendCrashLog2PM(java.lang.String r10) {
        /*
        r9 = this;
        r6 = new java.io.File;
        r6.<init>(r10);
        r6 = r6.exists();
        if (r6 != 0) goto L_0x0019;
    L_0x000b:
        r6 = r9.mContext;
        r7 = "日志文件不存在！";
        r8 = 0;
        r6 = android.widget.Toast.makeText(r6, r7, r8);
        r6.show();
    L_0x0018:
        return;
    L_0x0019:
        r1 = 0;
        r3 = 0;
        r5 = 0;
        r2 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x008a, IOException -> 0x005f }
        r2.<init>(r10);	 Catch:{ FileNotFoundException -> 0x008a, IOException -> 0x005f }
        r4 = new java.io.BufferedReader;	 Catch:{ FileNotFoundException -> 0x008c, IOException -> 0x0083, all -> 0x007c }
        r6 = new java.io.InputStreamReader;	 Catch:{ FileNotFoundException -> 0x008c, IOException -> 0x0083, all -> 0x007c }
        r7 = "GBK";
        r6.<init>(r2, r7);	 Catch:{ FileNotFoundException -> 0x008c, IOException -> 0x0083, all -> 0x007c }
        r4.<init>(r6);	 Catch:{ FileNotFoundException -> 0x008c, IOException -> 0x0083, all -> 0x007c }
    L_0x002d:
        r5 = r4.readLine();	 Catch:{ FileNotFoundException -> 0x0046, IOException -> 0x0086, all -> 0x007f }
        if (r5 != 0) goto L_0x003c;
    L_0x0033:
        r4.close();	 Catch:{ IOException -> 0x0058 }
        r2.close();	 Catch:{ IOException -> 0x0058 }
        r3 = r4;
        r1 = r2;
        goto L_0x0018;
    L_0x003c:
        r6 = "info";
        r7 = r5.toString();	 Catch:{ FileNotFoundException -> 0x0046, IOException -> 0x0086, all -> 0x007f }
        android.util.Log.i(r6, r7);	 Catch:{ FileNotFoundException -> 0x0046, IOException -> 0x0086, all -> 0x007f }
        goto L_0x002d;
    L_0x0046:
        r0 = move-exception;
        r3 = r4;
        r1 = r2;
    L_0x0049:
        r0.printStackTrace();	 Catch:{ all -> 0x006f }
        r3.close();	 Catch:{ IOException -> 0x0053 }
        r1.close();	 Catch:{ IOException -> 0x0053 }
        goto L_0x0018;
    L_0x0053:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0018;
    L_0x0058:
        r0 = move-exception;
        r0.printStackTrace();
        r3 = r4;
        r1 = r2;
        goto L_0x0018;
    L_0x005f:
        r0 = move-exception;
    L_0x0060:
        r0.printStackTrace();	 Catch:{ all -> 0x006f }
        r3.close();	 Catch:{ IOException -> 0x006a }
        r1.close();	 Catch:{ IOException -> 0x006a }
        goto L_0x0018;
    L_0x006a:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0018;
    L_0x006f:
        r6 = move-exception;
    L_0x0070:
        r3.close();	 Catch:{ IOException -> 0x0077 }
        r1.close();	 Catch:{ IOException -> 0x0077 }
    L_0x0076:
        throw r6;
    L_0x0077:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0076;
    L_0x007c:
        r6 = move-exception;
        r1 = r2;
        goto L_0x0070;
    L_0x007f:
        r6 = move-exception;
        r3 = r4;
        r1 = r2;
        goto L_0x0070;
    L_0x0083:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0060;
    L_0x0086:
        r0 = move-exception;
        r3 = r4;
        r1 = r2;
        goto L_0x0060;
    L_0x008a:
        r0 = move-exception;
        goto L_0x0049;
    L_0x008c:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0049;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.exception.CrashHandler.sendCrashLog2PM(java.lang.String):void");
    }
}
