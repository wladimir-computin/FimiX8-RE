package com.fimi.kernel.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.fimi.kernel.FimiAppContext;
import com.fimi.kernel.R;
import com.fimi.kernel.widget.statusbar.StatusBarView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.twitter.sdk.android.core.internal.scribe.SyndicatedSdkImpressionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class StatusBarUtil {
    public static final int DEFAULT_STATUS_BAR_ALPHA = 0;
    public static final int FLAG_NOTCH_SUPPORT = 65536;
    public static final int NOTCH_IN_SCREEN_VOIO = 32;
    public static final int ROUNDED_IN_SCREEN_VOIO = 8;

    public static void setColor(Activity activity, @ColorInt int color) {
        setColor(activity, color, 0);
    }

    public static void setColor(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        } else if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count <= 0 || !(decorView.getChildAt(count - 1) instanceof StatusBarView)) {
                decorView.addView(createStatusBarView(activity, color, statusBarAlpha));
            } else {
                decorView.getChildAt(count - 1).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            }
            setRootView(activity);
        }
    }

    public static void setColorForSwipeBack(Activity activity, int color) {
        setColorForSwipeBack(activity, color, 0);
    }

    public static void setColorForSwipeBack(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 19) {
            ViewGroup contentView = (ViewGroup) activity.findViewById(16908290);
            contentView.setPadding(0, getStatusBarHeight(activity), 0, 0);
            contentView.setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            setTransparentForWindow(activity);
        }
    }

    public static void setColorNoTranslucent(Activity activity, @ColorInt int color) {
        setColor(activity, color, 0);
    }

    @Deprecated
    public static void setColorDiff(Activity activity, @ColorInt int color) {
        if (VERSION.SDK_INT >= 19) {
            transparentStatusBar(activity);
            ViewGroup contentView = (ViewGroup) activity.findViewById(16908290);
            if (contentView.getChildCount() > 1) {
                contentView.getChildAt(1).setBackgroundColor(color);
            } else {
                contentView.addView(createStatusBarView(activity, color));
            }
            setRootView(activity);
        }
    }

    public static void setTranslucent(Activity activity) {
        setTranslucent(activity, 0);
    }

    public static void setTranslucent(Activity activity, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 19) {
            setTransparent(activity);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTranslucentForCoordinatorLayout(Activity activity, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 19) {
            transparentStatusBar(activity);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTransparent(Activity activity) {
        if (VERSION.SDK_INT >= 19) {
            transparentStatusBar(activity);
            setRootView(activity);
        }
    }

    @Deprecated
    public static void setTranslucentDiff(Activity activity) {
        if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            setRootView(activity);
        }
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    public static void setColorNoTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 19) {
            if (VERSION.SDK_INT >= 21) {
                activity.getWindow().addFlags(Integer.MIN_VALUE);
                activity.getWindow().clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
                activity.getWindow().setStatusBarColor(0);
            } else {
                activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            }
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (contentLayout.getChildCount() <= 0 || !(contentLayout.getChildAt(0) instanceof StatusBarView)) {
                contentLayout.addView(createStatusBarView(activity, color), 0);
            } else {
                contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            }
            if (!((contentLayout instanceof LinearLayout) || contentLayout.getChildAt(1) == null)) {
                contentLayout.getChildAt(1).setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity) + contentLayout.getPaddingTop(), contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
            }
            setDrawerLayoutProperty(drawerLayout, contentLayout);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout, ViewGroup drawerLayoutContentLayout) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    @Deprecated
    public static void setColorForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (contentLayout.getChildCount() <= 0 || !(contentLayout.getChildAt(0) instanceof StatusBarView)) {
                contentLayout.addView(createStatusBarView(activity, color), 0);
            } else {
                contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, 0));
            }
            if (!((contentLayout instanceof LinearLayout) || contentLayout.getChildAt(1) == null)) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            setDrawerLayoutProperty(drawerLayout, contentLayout);
        }
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        setTranslucentForDrawerLayout(activity, drawerLayout, 0);
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 19) {
            setTransparentForDrawerLayout(activity, drawerLayout);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTransparentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        if (VERSION.SDK_INT >= 19) {
            if (VERSION.SDK_INT >= 21) {
                activity.getWindow().addFlags(Integer.MIN_VALUE);
                activity.getWindow().clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
                activity.getWindow().setStatusBarColor(0);
            } else {
                activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            }
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (!((contentLayout instanceof LinearLayout) || contentLayout.getChildAt(1) == null)) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            setDrawerLayoutProperty(drawerLayout, contentLayout);
        }
    }

    @Deprecated
    public static void setTranslucentForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout) {
        if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            contentLayout.setFitsSystemWindows(true);
            contentLayout.setClipToPadding(true);
            ((ViewGroup) drawerLayout.getChildAt(1)).setFitsSystemWindows(false);
            drawerLayout.setFitsSystemWindows(false);
        }
    }

    public static void setTransparentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, int statusBarAlpha, View needOffsetView) {
        if (VERSION.SDK_INT >= 19) {
            setTransparentForWindow(activity);
            addTranslucentView(activity, statusBarAlpha);
            if (needOffsetView != null) {
                ((MarginLayoutParams) needOffsetView.getLayoutParams()).setMargins(0, getStatusBarHeight(activity), 0, 0);
            }
        }
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    public static void setTransparentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, int statusBarAlpha, View needOffsetView) {
        setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
        if (VERSION.SDK_INT >= 19 && VERSION.SDK_INT < 21) {
            clearPreviousSetting(activity);
        }
    }

    @TargetApi(19)
    private static void clearPreviousSetting(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        if (count > 0 && (decorView.getChildAt(count - 1) instanceof StatusBarView)) {
            decorView.removeViewAt(count - 1);
            ((ViewGroup) ((ViewGroup) activity.findViewById(16908290)).getChildAt(0)).setPadding(0, 0, 0, 0);
        }
    }

    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(16908290);
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    private static StatusBarView createStatusBarView(Activity activity, @ColorInt int color) {
        StatusBarView statusBarView = new StatusBarView(activity);
        statusBarView.setLayoutParams(new LayoutParams(-1, getStatusBarHeight(activity)));
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private static StatusBarView createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        StatusBarView statusBarView = new StatusBarView(activity);
        statusBarView.setLayoutParams(new LayoutParams(-1, getStatusBarHeight(activity)));
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    private static void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(16908290);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    private static void setTransparentForWindow(Activity activity) {
        if (VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(0);
            activity.getWindow().getDecorView().setSystemUiVisibility(FimiAppContext.UI_HEIGHT);
        } else if (VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(NTLMConstants.FLAG_UNIDENTIFIED_9, NTLMConstants.FLAG_UNIDENTIFIED_9);
        }
    }

    @TargetApi(19)
    private static void transparentStatusBar(Activity activity) {
        if (VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_10);
            activity.getWindow().setStatusBarColor(0);
            return;
        }
        activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
    }

    private static StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
        StatusBarView statusBarView = new StatusBarView(activity);
        statusBarView.setLayoutParams(new LayoutParams(-1, getStatusBarHeight(activity)));
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }

    public static int getStatusBarHeight(Context context) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", SyndicatedSdkImpressionEvent.CLIENT_NAME));
    }

    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        float a = 1.0f - (((float) alpha) / 255.0f);
        return ((ViewCompat.MEASURED_STATE_MASK | (((int) (((double) (((float) ((color >> 16) & 255)) * a)) + 0.5d)) << 16)) | (((int) (((double) (((float) ((color >> 8) & 255)) * a)) + 0.5d)) << 8)) | ((int) (((double) (((float) (color & 255)) * a)) + 0.5d));
    }

    @TargetApi(19)
    public static void transparencyBar(Activity activity) {
        if (VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            window.getDecorView().setSystemUiVisibility(FimiAppContext.UI_HEIGHT);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        } else if (VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(NTLMConstants.FLAG_UNIDENTIFIED_9, NTLMConstants.FLAG_UNIDENTIFIED_9);
        }
    }

    public static void setStatusBarColor(Activity activity, int colorId) {
        if (VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            window.getDecorView().setSystemUiVisibility(FimiAppContext.UI_HEIGHT);
        } else if (VERSION.SDK_INT >= 19) {
            transparencyBar(activity);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);
        }
    }

    public static int StatusBarLightMode(Activity activity) {
        if (VERSION.SDK_INT >= 19) {
            boolean isMiui9 = false;
            String systemProperty = getSystemProperty("ro.miui.ui.version.name");
            if (!(systemProperty == null || "".equals(systemProperty))) {
                try {
                    try {
                        if (Integer.parseInt(systemProperty.substring(1)) >= 9) {
                            isMiui9 = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (isMiui9 && VERSION.SDK_INT >= 23) {
                transparencyBar(activity);
                activity.getWindow().getDecorView().setSystemUiVisibility(9216);
            } else if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                transparencyBar(activity);
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                transparencyBar(activity);
            } else if (VERSION.SDK_INT >= 23) {
                transparencyBar(activity);
                activity.getWindow().getDecorView().setSystemUiVisibility(9216);
            } else {
                setStatusBarColor(activity, R.color.kernal_status_status);
            }
        }
        return 0;
    }

    public static void StatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(9216);
        }
    }

    public static void StatusBarDarkMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(0);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003e A:{SYNTHETIC, Splitter:B:13:0x003e} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0047 A:{SYNTHETIC, Splitter:B:18:0x0047} */
    public static java.lang.String getSystemProperty(java.lang.String r8) {
        /*
        r1 = 0;
        r5 = java.lang.Runtime.getRuntime();	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r6 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r6.<init>();	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r7 = "getprop ";
        r6 = r6.append(r7);	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r6 = r6.append(r8);	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r6 = r6.toString();	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r4 = r5.exec(r6);	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r2 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r5 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r6 = r4.getInputStream();	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r5.<init>(r6);	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r6 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r2.<init>(r5, r6);	 Catch:{ IOException -> 0x003a, all -> 0x0044 }
        r3 = r2.readLine();	 Catch:{ IOException -> 0x0052, all -> 0x004f }
        r2.close();	 Catch:{ IOException -> 0x0052, all -> 0x004f }
        if (r2 == 0) goto L_0x0038;
    L_0x0035:
        r2.close();	 Catch:{ IOException -> 0x004b }
    L_0x0038:
        r1 = r2;
    L_0x0039:
        return r3;
    L_0x003a:
        r0 = move-exception;
    L_0x003b:
        r3 = 0;
        if (r1 == 0) goto L_0x0039;
    L_0x003e:
        r1.close();	 Catch:{ IOException -> 0x0042 }
        goto L_0x0039;
    L_0x0042:
        r5 = move-exception;
        goto L_0x0039;
    L_0x0044:
        r5 = move-exception;
    L_0x0045:
        if (r1 == 0) goto L_0x004a;
    L_0x0047:
        r1.close();	 Catch:{ IOException -> 0x004d }
    L_0x004a:
        throw r5;
    L_0x004b:
        r5 = move-exception;
        goto L_0x0038;
    L_0x004d:
        r6 = move-exception;
        goto L_0x004a;
    L_0x004f:
        r5 = move-exception;
        r1 = r2;
        goto L_0x0045;
    L_0x0052:
        r0 = move-exception;
        r1 = r2;
        goto L_0x003b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.utils.StatusBarUtil.getSystemProperty(java.lang.String):java.lang.String");
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        if (window == null) {
            return false;
        }
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= bit ^ -1;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        if (window == null) {
            return false;
        }
        Class clazz = window.getClass();
        try {
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int darkModeFlag = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
            if (dark) {
                extraFlagField.invoke(window, new Object[]{Integer.valueOf(darkModeFlag), Integer.valueOf(darkModeFlag)});
            } else {
                extraFlagField.invoke(window, new Object[]{Integer.valueOf(0), Integer.valueOf(darkModeFlag)});
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNotch(Context context) {
        if (VERSION.SDK_INT < 19) {
            return false;
        }
        if ("Xiaomi".equals(Build.BRAND)) {
            return hasNotchInScreenAtMi(context);
        }
        return hasNotchInScreen(context);
    }

    public static Integer getInt(Context context, String key, int def) throws IllegalArgumentException {
        Integer ret = Integer.valueOf(def);
        try {
            Class SystemProperties = context.getClassLoader().loadClass("android.os.SystemProperties");
            return (Integer) SystemProperties.getMethod("getInt", new Class[]{String.class, Integer.TYPE}).invoke(SystemProperties, new Object[]{new String(key), new Integer(def)});
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            return Integer.valueOf(def);
        }
    }

    public static boolean hasNotchInScreenAtMi(Context context) {
        if (getInt(context, "ro.miui.notch", 0).intValue() == 1) {
            return true;
        }
        return false;
    }

    public static boolean hasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            Class HwNotchSizeUtil = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            return ((Boolean) HwNotchSizeUtil.getMethod("hasNotchInScreen", new Class[0]).invoke(HwNotchSizeUtil, new Object[0])).booleanValue();
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
            return ret;
        } catch (NoSuchMethodException e2) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
            return ret;
        } catch (Exception e3) {
            Log.e("test", "hasNotchInScreen Exception");
            return ret;
        } catch (Throwable th) {
            return ret;
        }
    }

    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            Class HwNotchSizeUtil = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            return (int[]) HwNotchSizeUtil.getMethod("getNotchSize", new Class[0]).invoke(HwNotchSizeUtil, new Object[0]);
        } catch (ClassNotFoundException e) {
            Log.e("test", "getNotchSize ClassNotFoundException");
            return ret;
        } catch (NoSuchMethodException e2) {
            Log.e("test", "getNotchSize NoSuchMethodException");
            return ret;
        } catch (Exception e3) {
            Log.e("test", "getNotchSize Exception");
            return ret;
        } catch (Throwable th) {
            return ret;
        }
    }

    @TargetApi(19)
    public static void setFullScreenWindowLayoutInDisplayCutout(Window window) {
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            try {
                Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
                Object layoutParamsExObj = layoutParamsExCls.getConstructor(new Class[]{WindowManager.LayoutParams.class}).newInstance(new Object[]{layoutParams});
                layoutParamsExCls.getMethod("addHwFlags", new Class[]{Integer.TYPE}).invoke(layoutParamsExObj, new Object[]{Integer.valueOf(65536)});
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                Log.e("test", "hw notch screen flag api error");
            } catch (Exception e2) {
                Log.e("test", "other Exception");
            }
        }
    }

    public static boolean hasNotchInScreenAtOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static boolean hasNotchInScreenAtVoio(Context context) {
        boolean ret = false;
        try {
            Class FtFeature = context.getClassLoader().loadClass("com.util.FtFeature");
            return ((Boolean) FtFeature.getMethod("isFeatureSupport", new Class[]{Integer.TYPE}).invoke(FtFeature, new Object[]{Integer.valueOf(32)})).booleanValue();
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
            return ret;
        } catch (NoSuchMethodException e2) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
            return ret;
        } catch (Exception e3) {
            Log.e("test", "hasNotchInScreen Exception");
            return ret;
        } catch (Throwable th) {
            return ret;
        }
    }
}
