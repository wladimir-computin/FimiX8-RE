package com.fimi.kernel;

import android.os.Build;
import com.fimi.host.common.ProductEnum;

public class Constants {
    public static final String CHANGELANGUGE = "com.fimi.app.changelanguge";
    public static final int CHINASERVICE = 1;
    public static final String COUNTRYCODE = "CountryCode";
    public static int CRC16_LENGTH = 14;
    public static final int EUROPESERVICE = 3;
    public static final String GH2_NEWBIE_GUIDE_KEY = "gh2_newbie_guide_key";
    public static final int INDIA = 4;
    public static final String LANGUAGETYPE = "select_languagetype";
    public static int LogFileSize = 35;
    public static final String MANUFACTURER_MODE_KEY = "manufacturer_mode_key";
    public static final int MAX_PROGRESS = 100;
    public static final int OTHERSERVICE = 7;
    public static final int RUSISA = 5;
    public static final String SERVICECODE = "ServiceCode";
    public static final String SERVICE_ITEM_KEY = "service_item_key";
    public static final int SOUTHEAST_ASIA = 6;
    public static final String SP_PERSON_USER_TYPE = "sp_person_user_type";
    public static String SUPPORT4K_KEY = "support4k_key";
    public static final String UPDATING_KEY = "updating_key";
    public static final int USSERVICE = 2;
    public static final String VIDEO_FILE_SUFFIX = ".mp4";
    public static final String VIDEO_RESOLUTION_KEY = "video_resolution_key";
    public static final int X8_BATTERY_MOODEL = 3;
    public static final int X8_BATTERY_TYPE = 5;
    public static final int X8_CAMERA_MOODEL = 2;
    public static final int X8_CAMERA_TYPE = 4;
    public static final int X8_CV_MOODEL = 1;
    public static final int X8_CV_TYPE = 9;
    public static final int X8_ESC_MOODEL = 0;
    public static final int X8_ESC_TYPE = 14;
    public static final int X8_FC_MOODEL = 3;
    public static final int X8_FC_TYPE = 0;
    public static final int X8_FLIGHT_CONTROL_RELAY_MOODEL = 3;
    public static final int X8_FLIGHT_CONTROL_RELAY_TYPE = 12;
    public static final int X8_GIMBAL_MOODEL = 6;
    public static final int X8_GIMBAL_TYPE = 3;
    public static final int X8_NFZ_MOODEL = 3;
    public static final int X8_NFZ_TYPE = 10;
    public static final int X8_RC_MOODEL = 3;
    public static final int X8_RC_TYPE = 1;
    public static final int X8_REPEATER_RC_MOODEL = 3;
    public static final int X8_REPEATER_RC_TYPE = 11;
    public static final int X8_ULTRASONIC_MOODEL = 1;
    public static final int X8_ULTRASONIC_TYPE = 13;
    public static final String X8_UPDATE_EVENT_KEY = "x8_update_event_key";
    public static String X9_BEGNNER_GUIDE = "x9_begnner_guide";
    public static String X9_BEGNNER_GUIDE_SETTING = "x9_begnner_guide_setting";
    public static final int X9_MCU_MOODEL = 0;
    public static final int X9_MCU_TYPE = 13;
    public static final int X9_SYS_MODEL = 4;
    public static final int X9_SYS_TYPE = 0;
    static final APPType appType = APPType.Online;
    public static boolean isChangeCameraVisiable = false;
    public static boolean isCheckDeviceConnect = true;
    public static final boolean isDebug = false;
    public static boolean isRefreshMainView = true;
    public static boolean isShowUserProtocol = true;
    public static final boolean isUmengOnline = true;
    public static boolean isUpgradeView = false;
    public static final String[] manufacturerModes = new String[]{"samsungSM-A9000", "xiaomiRedmi Note 5A"};
    public static int panoramaType = 0;
    public static final ProductEnum productType = ProductEnum.X8S;

    public enum APPType {
        Online,
        Factory
    }

    public enum UserType {
        Ideal,
        Guest,
        Register
    }

    public static boolean isFactoryApp() {
        return appType.equals(APPType.Factory);
    }

    public static boolean isTaggingModels() {
        boolean modelTag = false;
        for (String string : manufacturerModes) {
            if (string.contentEquals(Build.BRAND + Build.MODEL)) {
                return true;
            }
            modelTag = false;
        }
        return modelTag;
    }
}
