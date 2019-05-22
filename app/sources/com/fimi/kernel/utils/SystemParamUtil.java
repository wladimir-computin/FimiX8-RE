package com.fimi.kernel.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.fimi.kernel.FimiAppContext;
import java.util.List;
import java.util.Locale;

public class SystemParamUtil {
    public static String getModelName() {
        return Build.MODEL;
    }

    public static String getManufacturerName() {
        return Build.MANUFACTURER;
    }

    public static String getAndroidId(Context ctx) {
        return "" + Secure.getString(ctx.getContentResolver(), "android_id");
    }

    public static String getDeviceID(Context context) {
        return Build.MODEL + "," + ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    public static String getLocalLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getVersionName() {
        PackageInfo info = getPackageInfo();
        if (info == null) {
            return null;
        }
        return info.versionName;
    }

    public static int getVersionCode() {
        PackageInfo info = getPackageInfo();
        if (info == null) {
            return 0;
        }
        return info.versionCode;
    }

    public static PackageInfo getPackageInfo() {
        Context mContext = FimiAppContext.getContext();
        PackageInfo info = null;
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return info;
        }
    }

    public static String getPackageName() {
        PackageInfo info = getPackageInfo();
        if (info == null) {
            return null;
        }
        return info.packageName;
    }

    public static boolean isWifiNetwork(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && networkInfo.getTypeName().equalsIgnoreCase("WIFI")) {
            return true;
        }
        return false;
    }

    public static boolean isSDFreeSize(int maxSize) {
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((((long) sf.getAvailableBlocks()) * ((long) sf.getBlockSize())) / 1024) / 1024 <= ((long) maxSize);
    }

    public static boolean isTopActivy(String cmdName, Activity activity) {
        List<RunningTaskInfo> runningTaskInfos = ((ActivityManager) activity.getSystemService("activity")).getRunningTasks(1);
        String cmpNameTemp = null;
        if (runningTaskInfos != null) {
            cmpNameTemp = ((RunningTaskInfo) runningTaskInfos.get(0)).topActivity.getClassName();
        }
        if (cmpNameTemp == null) {
            return false;
        }
        return cmpNameTemp.equals(cmdName);
    }

    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            return ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return localVersion;
        }
    }
}
