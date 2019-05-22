package com.fimi.kernel.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import com.fimi.kernel.animutils.IOUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class AbAppUtil {
    private static int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;
    public static List<String[]> mProcessList = null;

    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(NTLMConstants.FLAG_UNIDENTIFIED_11);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent("android.intent.action.DELETE");
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        for (RunningServiceInfo si : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    public static boolean stopRunningService(Context context, String className) {
        Intent intent_service = null;
        try {
            intent_service = new Intent(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            return context.stopService(intent_service);
        }
        return false;
    }

    public static int getNumCores() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            }).length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivity == null) {
                return false;
            }
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected() && info.getState() == State.CONNECTED) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isGpsEnabled(Context context) {
        return ((LocationManager) context.getSystemService("location")).isProviderEnabled("gps");
    }

    public static boolean isMobile(Context context) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetInfo == null || activeNetInfo.getType() != 0) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0085 A:{SYNTHETIC, Splitter:B:38:0x0085} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008a A:{SYNTHETIC, Splitter:B:41:0x008a} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0067 A:{SYNTHETIC, Splitter:B:19:0x0067} */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x006c A:{SYNTHETIC, Splitter:B:22:0x006c} */
    public static boolean importDatabase(android.content.Context r12, java.lang.String r13, int r14) {
        /*
        r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r9 = 0;
        r7 = 0;
        r6 = 0;
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x009b }
        r10.<init>();	 Catch:{ Exception -> 0x009b }
        r11 = "/data/data/";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x009b }
        r11 = r12.getPackageName();	 Catch:{ Exception -> 0x009b }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x009b }
        r11 = "/databases/";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x009b }
        r10 = r10.append(r13);	 Catch:{ Exception -> 0x009b }
        r3 = r10.toString();	 Catch:{ Exception -> 0x009b }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x009b }
        r4.<init>(r3);	 Catch:{ Exception -> 0x009b }
        r10 = r4.exists();	 Catch:{ Exception -> 0x009b }
        if (r10 != 0) goto L_0x0074;
    L_0x0031:
        r10 = r4.getParentFile();	 Catch:{ Exception -> 0x009b }
        r10 = r10.exists();	 Catch:{ Exception -> 0x009b }
        if (r10 != 0) goto L_0x0042;
    L_0x003b:
        r10 = r4.getParentFile();	 Catch:{ Exception -> 0x009b }
        r10.mkdirs();	 Catch:{ Exception -> 0x009b }
    L_0x0042:
        r4.createNewFile();	 Catch:{ Exception -> 0x009b }
        r10 = r12.getResources();	 Catch:{ Exception -> 0x009b }
        r9 = r10.openRawResource(r14);	 Catch:{ Exception -> 0x009b }
        r8 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x009b }
        r8.<init>(r4);	 Catch:{ Exception -> 0x009b }
        r0 = new byte[r1];	 Catch:{ Exception -> 0x0060, all -> 0x0098 }
        r2 = 0;
    L_0x0055:
        r2 = r9.read(r0);	 Catch:{ Exception -> 0x0060, all -> 0x0098 }
        if (r2 <= 0) goto L_0x0070;
    L_0x005b:
        r10 = 0;
        r8.write(r0, r10, r2);	 Catch:{ Exception -> 0x0060, all -> 0x0098 }
        goto L_0x0055;
    L_0x0060:
        r5 = move-exception;
        r7 = r8;
    L_0x0062:
        r5.printStackTrace();	 Catch:{ all -> 0x0082 }
        if (r7 == 0) goto L_0x006a;
    L_0x0067:
        r7.close();	 Catch:{ Exception -> 0x0090 }
    L_0x006a:
        if (r9 == 0) goto L_0x006f;
    L_0x006c:
        r9.close();	 Catch:{ Exception -> 0x0092 }
    L_0x006f:
        return r6;
    L_0x0070:
        r8.flush();	 Catch:{ Exception -> 0x0060, all -> 0x0098 }
        r7 = r8;
    L_0x0074:
        r6 = 1;
        if (r7 == 0) goto L_0x007a;
    L_0x0077:
        r7.close();	 Catch:{ Exception -> 0x008e }
    L_0x007a:
        if (r9 == 0) goto L_0x006f;
    L_0x007c:
        r9.close();	 Catch:{ Exception -> 0x0080 }
        goto L_0x006f;
    L_0x0080:
        r10 = move-exception;
        goto L_0x006f;
    L_0x0082:
        r10 = move-exception;
    L_0x0083:
        if (r7 == 0) goto L_0x0088;
    L_0x0085:
        r7.close();	 Catch:{ Exception -> 0x0094 }
    L_0x0088:
        if (r9 == 0) goto L_0x008d;
    L_0x008a:
        r9.close();	 Catch:{ Exception -> 0x0096 }
    L_0x008d:
        throw r10;
    L_0x008e:
        r10 = move-exception;
        goto L_0x007a;
    L_0x0090:
        r10 = move-exception;
        goto L_0x006a;
    L_0x0092:
        r10 = move-exception;
        goto L_0x006f;
    L_0x0094:
        r11 = move-exception;
        goto L_0x0088;
    L_0x0096:
        r11 = move-exception;
        goto L_0x008d;
    L_0x0098:
        r10 = move-exception;
        r7 = r8;
        goto L_0x0083;
    L_0x009b:
        r5 = move-exception;
        goto L_0x0062;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.utils.AbAppUtil.importDatabase(android.content.Context, java.lang.String, int):boolean");
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();
        } else {
            mResources = context.getResources();
        }
        return mResources.getDisplayMetrics();
    }

    public static void showSoftInput(Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(0, 2);
    }

    public static void closeSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 2);
        }
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            return info;
        }
    }

    public static ApplicationInfo getApplicationInfo(Context context, String processName) {
        if (processName == null) {
            return null;
        }
        for (ApplicationInfo appInfo : context.getApplicationContext().getPackageManager().getInstalledApplications(8192)) {
            if (processName.equals(appInfo.processName)) {
                return appInfo;
            }
        }
        return null;
    }

    public static void killProcesses(Context context, int pid, String processName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        try {
            String packageName;
            if (processName.indexOf(":") == -1) {
                packageName = processName;
            } else {
                packageName = processName.split(":")[0];
            }
            activityManager.killBackgroundProcesses(packageName);
            Method forceStopPackage = activityManager.getClass().getDeclaredMethod("forceStopPackage", new Class[]{String.class});
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(activityManager, new Object[]{packageName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String runCommand(String[] command, String workdirectory) {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            if (workdirectory != null) {
                builder.directory(new File(workdirectory));
            }
            builder.redirectErrorStream(true);
            InputStream in = builder.start().getInputStream();
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                result = result + new String(buffer);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String runScript(String script) {
        String sRet = "";
        try {
            final Process m_process = Runtime.getRuntime().exec(script);
            final StringBuilder sbread = new StringBuilder();
            Thread tout = new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(m_process.getInputStream()), 8192);
                    while (true) {
                        try {
                            String ls_1 = bufferedReader.readLine();
                            if (ls_1 != null) {
                                sbread.append(ls_1).append(IOUtils.LINE_SEPARATOR_UNIX);
                            } else {
                                try {
                                    bufferedReader.close();
                                    return;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            try {
                                bufferedReader.close();
                                return;
                            } catch (IOException e22) {
                                e22.printStackTrace();
                                return;
                            }
                        } catch (Throwable th) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                            throw th;
                        }
                    }
                }
            });
            tout.start();
            final StringBuilder sberr = new StringBuilder();
            Thread terr = new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(m_process.getErrorStream()), 8192);
                    while (true) {
                        try {
                            String ls_1 = bufferedReader.readLine();
                            if (ls_1 != null) {
                                sberr.append(ls_1).append(IOUtils.LINE_SEPARATOR_UNIX);
                            } else {
                                try {
                                    bufferedReader.close();
                                    return;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            try {
                                bufferedReader.close();
                                return;
                            } catch (IOException e22) {
                                e22.printStackTrace();
                                return;
                            }
                        } catch (Throwable th) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                            throw th;
                        }
                    }
                }
            });
            terr.start();
            int retvalue = m_process.waitFor();
            while (tout.isAlive()) {
                Thread.sleep(50);
            }
            if (terr.isAlive()) {
                terr.interrupt();
            }
            String stdout = sbread.toString();
            return stdout + sberr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0061 A:{SYNTHETIC, Splitter:B:15:0x0061} */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0070 A:{SYNTHETIC, Splitter:B:22:0x0070} */
    public static boolean getRootPermission(android.content.Context r8) {
        /*
        r4 = r8.getPackageCodePath();
        r5 = 0;
        r2 = 0;
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r6.<init>();	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r7 = "chmod 777 ";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r6 = r6.append(r4);	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r0 = r6.toString();	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r6 = java.lang.Runtime.getRuntime();	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r7 = "su";
        r5 = r6.exec(r7);	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r3 = new java.io.DataOutputStream;	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r6 = r5.getOutputStream();	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r3.<init>(r6);	 Catch:{ Exception -> 0x005d, all -> 0x006d }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        r6.<init>();	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        r6 = r6.append(r0);	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        r7 = "\n";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        r6 = r6.toString();	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        r3.writeBytes(r6);	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        r6 = "exit\n";
        r3.writeBytes(r6);	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        r3.flush();	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        r5.waitFor();	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        if (r3 == 0) goto L_0x0052;
    L_0x004f:
        r3.close();	 Catch:{ Exception -> 0x0058 }
    L_0x0052:
        r5.destroy();	 Catch:{ Exception -> 0x0058 }
    L_0x0055:
        r6 = 1;
        r2 = r3;
    L_0x0057:
        return r6;
    L_0x0058:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0055;
    L_0x005d:
        r1 = move-exception;
    L_0x005e:
        r6 = 0;
        if (r2 == 0) goto L_0x0064;
    L_0x0061:
        r2.close();	 Catch:{ Exception -> 0x0068 }
    L_0x0064:
        r5.destroy();	 Catch:{ Exception -> 0x0068 }
        goto L_0x0057;
    L_0x0068:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0057;
    L_0x006d:
        r6 = move-exception;
    L_0x006e:
        if (r2 == 0) goto L_0x0073;
    L_0x0070:
        r2.close();	 Catch:{ Exception -> 0x0077 }
    L_0x0073:
        r5.destroy();	 Catch:{ Exception -> 0x0077 }
    L_0x0076:
        throw r6;
    L_0x0077:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0076;
    L_0x007c:
        r6 = move-exception;
        r2 = r3;
        goto L_0x006e;
    L_0x007f:
        r1 = move-exception;
        r2 = r3;
        goto L_0x005e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.utils.AbAppUtil.getRootPermission(android.content.Context):boolean");
    }

    public static List<String[]> getProcessRunningInfo() {
        List<String[]> processList = null;
        try {
            return parseProcessRunningInfo(runCommandTopN1());
        } catch (Exception e) {
            e.printStackTrace();
            return processList;
        }
    }

    public static String runCommandTopN1() {
        String result = null;
        try {
            return runCommand(new String[]{"/system/bin/top", "-n", "1"}, "/system/bin/");
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    public static List<String[]> parseProcessRunningInfo(String info) {
        List<String[]> processList = new ArrayList();
        String tempString = "";
        boolean bIsProcInfo = false;
        String[] rows = info.split("[\n]+");
        for (String tempString2 : rows) {
            if (tempString2.indexOf("PID") != -1) {
                bIsProcInfo = true;
            } else if (bIsProcInfo) {
                String[] columns = tempString2.trim().split("[ ]+");
                if (columns.length == 10 && !columns[9].startsWith("/system/bin/")) {
                    processList.add(columns);
                }
            }
        }
        return processList;
    }

    public static long getAvailMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static long getTotalMemory(Context context) {
        long memory = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            memory = (long) (Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]).intValue() * 1024);
            bufferedReader.close();
            return memory;
        } catch (Exception e) {
            e.printStackTrace();
            return memory;
        }
    }

    public static boolean isFastClick(int minClickDelayTime) {
        MIN_CLICK_DELAY_TIME = minClickDelayTime;
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - lastClickTime <= ((long) MIN_CLICK_DELAY_TIME)) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
