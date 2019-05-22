package com.fimi.x8sdk.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import java.util.ArrayList;
import java.util.List;

public class XPermission {
    private static OnPermissionListener mOnPermissionListener;
    private static int mRequestCode = -1;

    public interface OnPermissionListener {
        void onPermissionDenied();

        void onPermissionGranted();
    }

    @TargetApi(23)
    public static void requestPermissions(Context context, int requestCode, String[] permissions, OnPermissionListener listener) {
        if (context instanceof Activity) {
            mOnPermissionListener = listener;
            List<String> deniedPermissions = getDeniedPermissions(context, permissions);
            if (deniedPermissions.size() > 0) {
                mRequestCode = requestCode;
                ((Activity) context).requestPermissions((String[]) deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
                return;
            } else if (mOnPermissionListener != null) {
                mOnPermissionListener.onPermissionGranted();
                return;
            } else {
                return;
            }
        }
        throw new RuntimeException("Context must be an Activity");
    }

    private static List<String> getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedPermissions = new ArrayList();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == -1) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mRequestCode != -1 && requestCode == mRequestCode && mOnPermissionListener != null) {
            if (verifyPermissions(grantResults)) {
                mOnPermissionListener.onPermissionGranted();
            } else {
                mOnPermissionListener.onPermissionDenied();
            }
        }
    }

    private static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != 0) {
                return false;
            }
        }
        return true;
    }

    public static void showTipsDialog(final Context context) {
        new Builder(context).setTitle((CharSequence) "提示信息").setMessage((CharSequence) "当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。").setNegativeButton((CharSequence) "取消", null).setPositiveButton((CharSequence) "确定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                XPermission.startAppSettings(context);
            }
        }).show();
    }

    private static void startAppSettings(Context context) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }
}
