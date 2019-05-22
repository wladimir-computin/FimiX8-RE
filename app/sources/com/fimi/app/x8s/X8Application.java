package com.fimi.app.x8s;

import android.content.Context;
import android.os.Environment;
import com.fimi.TcpClient;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.test.LogSaveLocalHelper;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.FimiAppContext;
import com.fimi.kernel.exception.CrashHandler;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.common.GlobalConfig.Builder;
import com.fimi.x8sdk.map.MapType;
import java.io.File;

public class X8Application {
    public static int ANIMATION_WIDTH;
    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;
    public static boolean Type2 = true;
    public static boolean enableGesture = true;
    public static boolean isAoaTopActivity;
    public static boolean isLocalVideo = false;
    public static boolean isLuancher;

    public void onCreate(Context context) {
        LogSaveLocalHelper logSaveLocalHelper = LogSaveLocalHelper.getInstance(context, Environment.getExternalStorageDirectory() + File.separator + "x8/x8s_log");
        logSaveLocalHelper.setTag("DDLog");
        logSaveLocalHelper.start();
        isLuancher = true;
        FimiAppContext.initKernel(context);
        CrashHandler.getInstance().init(context);
        initSDK(context);
        X8AiConfig.getInstance().init();
        TcpClient.createInit();
    }

    private void initSDK(Context context) {
        Builder builder = new Builder();
        builder.setMapType(SPStoreManager.getInstance().getBoolean(Constants.X8_MAP_OPTION, false) ? MapType.AMap : MapType.GoogleMap).setMapStyle(SPStoreManager.getInstance().getInt(Constants.X8_MAP_STYLE) == 0 ? Constants.X8_GENERAL_MAP_STYLE_NORMAL : Constants.X8_GENERAL_MAP_STYLE_SATELLITE).setRectification(SPStoreManager.getInstance().getBoolean(Constants.X8_MAP_RECTIFYIN_OPTION, true)).setShowLog(SPStoreManager.getInstance().getBoolean(Constants.X8_SHOW_LOG_OPTION, true)).setShowmMtric(SPStoreManager.getInstance().getBoolean(Constants.X8_UNITY_OPTION, true)).setGridLine(SPStoreManager.getInstance().getInt(Constants.X8_GLINE_LINE_OPTION));
        builder.setLowReturn(SPStoreManager.getInstance().getBoolean(Constants.X8_LOW_POWER_RETURN, true));
        builder.setLowLanding(SPStoreManager.getInstance().getBoolean(Constants.X8_LOW_POWER_LANDING, true));
        GlobalConfig.getInstance().init(builder);
        X8NumberUtil.resetPrexString(context);
    }
}
