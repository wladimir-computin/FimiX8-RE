package com.fimi.x8sdk.entity;

import com.fimi.x8sdk.X8FcLogManager;
import com.fimi.x8sdk.appsetting.ValueSensity;

public class X8AppSettingLog {
    public static int CC;
    public static int FB_pitch;
    public static int FB_roll;
    public static int FB_thro;
    public static int FB_yaw;
    public static int FE_pitch;
    public static int FE_roll;
    public static int FE_thro;
    public static int FE_yaw;
    public static int FS_pitch;
    public static int FS_roll;
    public static int FS_thro;
    public static int FS_yaw;
    public static int FY_pitch;
    public static int FY_roll;
    public static int FY_thro;
    public static int FY_yaw;
    public static int RCNOTUPDATECNT;
    public static int STARTUPTIME;
    public static int SYSERRORCODE;
    public static int SYSSTATE;
    public static int TOTALCAPACITY;
    public static int UVC;
    public static boolean accurateLanding;
    public static float distanceLimit;
    public static boolean followAB;
    public static boolean followRP;
    public static float heightLimit;
    public static int lostAction;
    public static int lowPower;
    public static boolean pilotMode;
    public static float returnHeight;
    public static float speedLimit;
    public static boolean sportMode;

    public static void setFs(int p, int r, int t, int y) {
        if (p == FS_pitch && r == FS_roll && FS_thro == t && FS_yaw == y) {
            FS_pitch = p;
            FS_roll = r;
            FS_thro = t;
            FS_yaw = y;
            return;
        }
        X8FcLogManager.getInstance().appValueSensityChange("feelYawTrip", new ValueSensity((float) FS_pitch, (float) FS_roll, (float) FS_thro, (float) FS_yaw), new ValueSensity((float) p, (float) r, (float) t, (float) y));
        FS_pitch = p;
        FS_roll = r;
        FS_thro = t;
        FS_yaw = y;
    }

    public static void setFb(int p, int r, int t, int y) {
        if (p == FB_pitch && r == FB_roll && FB_thro == t && FB_yaw == y) {
            FB_pitch = p;
            FB_roll = r;
            FB_thro = t;
            FB_yaw = y;
            return;
        }
        X8FcLogManager.getInstance().appValueSensityChange("feelSensitivity", new ValueSensity((float) FB_pitch, (float) FB_roll, (float) FB_thro, (float) FB_yaw), new ValueSensity((float) p, (float) r, (float) t, (float) y));
        FB_pitch = p;
        FB_roll = r;
        FB_thro = t;
        FB_yaw = y;
    }

    public static void setYawTrip(int p, int r, int t, int y) {
        if (p == FY_pitch && r == FY_roll && FY_thro == t && FY_yaw == y) {
            FY_pitch = p;
            FY_roll = r;
            FY_thro = t;
            FY_yaw = y;
            return;
        }
        X8FcLogManager.getInstance().appValueSensityChange("feelYawTrip", new ValueSensity((float) FY_pitch, (float) FY_roll, (float) FY_thro, (float) FY_yaw), new ValueSensity((float) p, (float) r, (float) t, (float) y));
        FY_pitch = p;
        FY_roll = r;
        FY_thro = t;
        FY_yaw = y;
    }

    public static void setExp(int p, int r, int t, int y) {
        if (p == -1) {
            p = FE_pitch;
        }
        if (r == -1) {
            r = FE_roll;
        }
        if (t == -1) {
            t = FE_thro;
        }
        if (y == -1) {
            y = FE_yaw;
        }
        X8FcLogManager.getInstance().appValueSensityChange("feelExp", new ValueSensity((float) FE_pitch, (float) FE_roll, (float) FE_thro, (float) FE_yaw), new ValueSensity((float) p, (float) r, (float) t, (float) y));
        FE_pitch = p;
        FE_thro = t;
        FE_yaw = y;
    }

    public static void onChangePilotMode(boolean b) {
        if (pilotMode != b) {
            X8FcLogManager.getInstance().appValueBoleanChange("accLand", pilotMode, b);
            pilotMode = b;
        }
    }

    public static void onChangeLostAciton(int action) {
        if (lostAction != action) {
            X8FcLogManager.getInstance().appValueFloatChange("lostAction", (float) lostAction, (float) action);
            lostAction = action;
        }
    }

    public static void onChangeAccurateLanding(boolean b) {
        if (accurateLanding != b) {
            X8FcLogManager.getInstance().appValueBoleanChange("accLand", accurateLanding, b);
            accurateLanding = b;
        }
    }

    public static void noChangeFollowRP(boolean b) {
        if (followRP != b) {
            X8FcLogManager.getInstance().appValueBoleanChange("followRP", followRP, b);
            followRP = b;
        }
    }

    public static void setFollowAB(boolean b) {
        if (followAB != b) {
            X8FcLogManager.getInstance().appValueBoleanChange("followAB", followAB, b);
            followAB = b;
        }
    }

    public static void setCc(int cc) {
        if (CC != cc) {
            X8FcLogManager.getInstance().appValueFloatChange("cc", (float) CC, (float) cc);
            CC = cc;
        }
    }

    public static void setUvc(int uvc) {
        if (UVC != uvc) {
            X8FcLogManager.getInstance().appValueFloatChange("uvc", (float) UVC, (float) uvc);
            UVC = uvc;
        }
    }

    public static void setTotalCapacity(int totalCapacity) {
        if (TOTALCAPACITY != totalCapacity) {
            X8FcLogManager.getInstance().appValueFloatChange("totalCapacity", (float) TOTALCAPACITY, (float) totalCapacity);
            TOTALCAPACITY = totalCapacity;
        }
    }

    public static void setRcNotUpdateCnt(int rcNotUpdateCnt) {
        if (RCNOTUPDATECNT != rcNotUpdateCnt) {
            X8FcLogManager.getInstance().appValueFloatChange("rcNotUpdateCnt", (float) RCNOTUPDATECNT, (float) rcNotUpdateCnt);
            RCNOTUPDATECNT = rcNotUpdateCnt;
        }
    }

    public static void setStartUpTime(int startUpTime) {
        if (STARTUPTIME != startUpTime) {
            STARTUPTIME = startUpTime;
        }
    }

    public static void setRcState(int rcState) {
        if (SYSSTATE != rcState) {
            X8FcLogManager.getInstance().appValueFloatChange("sysState", (float) SYSSTATE, (float) rcState);
            SYSSTATE = rcState;
        }
    }

    public static void setRcErrorState(int rcErrorState) {
        if (SYSERRORCODE != rcErrorState) {
            X8FcLogManager.getInstance().appValueFloatChange("sysErrorCode", (float) SYSERRORCODE, (float) rcErrorState);
            SYSERRORCODE = rcErrorState;
        }
    }

    public static void onDataChange() {
    }

    public static void setSportMode(boolean b) {
        if (sportMode != b) {
            X8FcLogManager.getInstance().appValueBoleanChange("sportMode", sportMode, b);
            sportMode = b;
        }
    }

    public static void setPilotMode(boolean b) {
        if (pilotMode != b) {
            X8FcLogManager.getInstance().appValueBoleanChange("pilotMode", pilotMode, b);
            pilotMode = b;
        }
    }

    public static void setLowPower(int l) {
        if (lowPower != l) {
            X8FcLogManager.getInstance().appValueFloatChange("lowPower", (float) lowPower, (float) l);
            lowPower = l;
        }
    }

    public static void setReturnHeight(float h) {
        if (returnHeight != h) {
            X8FcLogManager.getInstance().appValueFloatChange("returnHeight", returnHeight, h);
            returnHeight = h;
        }
    }

    public static void setHeightLimit(float h) {
        if (heightLimit != h) {
            X8FcLogManager.getInstance().appValueFloatChange("heightLimit", heightLimit, h);
            heightLimit = h;
        }
    }

    public static void setDistanceLimit(float d) {
        if (distanceLimit != d) {
            X8FcLogManager.getInstance().appValueFloatChange("distanceLimit", distanceLimit, d);
            distanceLimit = d;
        }
    }

    public static void setSpeedLimit(float s) {
        if (speedLimit != s) {
            X8FcLogManager.getInstance().appValueFloatChange("speedLimit", speedLimit, s);
            speedLimit = s;
        }
    }
}
