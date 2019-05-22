package com.fimi.kernel.permission;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import com.fimi.kernel.base.BaseAppManager;
import com.github.moduth.blockcanary.internal.BlockInfo;
import java.util.ArrayList;
import java.util.List;

public class PermissionManager {
    public static final int ACTION_LOCATION_SOURCE_SETTINGS = 1314;
    public static String[] PERMISSIONS_CAMERA = new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"};
    public static String[] PERMISSIONS_COARSE_LOCATION = new String[]{"android.permission.ACCESS_COARSE_LOCATION"};
    public static String[] PERMISSIONS_EXTERNAL_STORAGE = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static String[] PERMISSIONS_INTERNET = new String[]{"android.permission.INTERNET", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    public static String[] PERMISSIONS_RECORD_AUDIO = new String[]{"android.permission.RECORD_AUDIO"};
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 2;
    private static final int REQUEST_ACCESS_EXTERNAL_STORAGE = 4;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 3;
    private static final int REQUEST_BLUETOOTH = 5;
    private static final int REQUEST_BLUETOOTH_ADMIN = 6;
    public static final int REQUEST_CAMERA = 7;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_RECORD_AUDIO = 8;
    public static final int REQUEST_X9_PERMISSIONS = 9;
    private static final String[] permissionsArray = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION"};
    private static List<String> permissionsList = new ArrayList();

    public static void requestStoragePermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    public static void requestCoarseLocationPermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 2);
        }
    }

    public static boolean hasLocaltionPermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return false;
        }
        return true;
    }

    public static boolean shouldShowLocaltionPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(BaseAppManager.getInstance().getForwardActivity(), "android.permission.ACCESS_COARSE_LOCATION")) {
            return true;
        }
        return false;
    }

    public static void requestWritePermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
        }
    }

    public static void requestFind_LocationPermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 3);
        }
    }

    public static void requestNetPermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.INTERNET") != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), PERMISSIONS_INTERNET, 1);
        }
    }

    public static void requestBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.BLUETOOTH") != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.BLUETOOTH"}, 5);
        }
    }

    public static void requestBluetoothAdminPermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.BLUETOOTH_ADMIN") != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.BLUETOOTH_ADMIN"}, 6);
        }
    }

    public static void requestCameraPermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.CAMERA") != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.CAMERA"}, 7);
        }
    }

    public static void requestRecordAudioPermissions() {
        if (ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.RECORD_AUDIO") != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.RECORD_AUDIO"}, 8);
        }
    }

    public static boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private static boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), permission) == -1;
    }

    public static final boolean isLocationEnable(Context context) {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        boolean networkProvider = locationManager.isProviderEnabled(BlockInfo.KEY_NETWORK);
        boolean gpsProvider = locationManager.isProviderEnabled("gps");
        if (networkProvider || gpsProvider) {
            return true;
        }
        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        if (VERSION.SDK_INT >= 19) {
            try {
                if (Secure.getInt(context.getContentResolver(), "location_mode") != 0) {
                    return true;
                }
                return false;
            } catch (SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } else if (TextUtils.isEmpty(Secure.getString(context.getContentResolver(), "location_providers_allowed"))) {
            return false;
        } else {
            return true;
        }
    }

    public static void checkRequiredPermission(Activity activity) {
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != 0) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(activity, (String[]) permissionsList.toArray(new String[permissionsList.size()]), 9);
        }
    }
}
