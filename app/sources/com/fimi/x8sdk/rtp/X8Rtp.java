package com.fimi.x8sdk.rtp;

import android.content.Context;
import com.fimi.x8sdk.R;

public class X8Rtp {
    public static boolean isRtpAllShow = false;
    public static boolean simulationTest = false;

    public static String getFcNavString(Context context, int code) {
        String s = isRtpAllShow ? context.getString(R.string.cmd_fail) + "error code=" + code : "";
        switch (code) {
            case 1:
                return context.getString(R.string.x8_nav_rtp1);
            case 2:
                return context.getString(R.string.x8_nav_rtp2);
            case 3:
                return context.getString(R.string.x8_nav_rtp3);
            case 4:
                return context.getString(R.string.x8_nav_rtp4);
            case 12:
                return context.getString(R.string.x8_nav_rtp12);
            case 13:
                return context.getString(R.string.x8_nav_rtp13);
            case 14:
                return context.getString(R.string.x8_nav_rtp14);
            case 15:
                return context.getString(R.string.x8_nav_rtp15);
            case 16:
                return context.getString(R.string.x8_nav_rtp16);
            case 17:
                return context.getString(R.string.x8_nav_rtp17);
            case 19:
                return context.getString(R.string.x8_nav_rtp19);
            case 22:
                return context.getString(R.string.x8_nav_rtp22);
            case 23:
                return context.getString(R.string.x8_nav_rtp23);
            case 25:
                return context.getString(R.string.x8_nav_rtp25);
            case 26:
                return context.getString(R.string.x8_nav_rtp26);
            case 28:
                return context.getString(R.string.x8_nav_rtp28);
            case 29:
                return context.getString(R.string.x8_nav_rtp29);
            case 31:
                return context.getString(R.string.x8_nav_rtp31);
            case 33:
                return context.getString(R.string.x8_nav_rtp33);
            case 37:
                return context.getString(R.string.x8_nav_rtp37);
            case 38:
                return context.getString(R.string.x8_nav_rtp38);
            case 42:
                return context.getString(R.string.x8_nav_rtp42);
            case 43:
                return context.getString(R.string.x8_nav_rtp43);
            case 44:
                return context.getString(R.string.x8_nav_rtp44);
            case 45:
                return context.getString(R.string.x8_nav_rtp45);
            case 46:
                return context.getString(R.string.x8_nav_rtp46);
            case 47:
                return context.getString(R.string.x8_nav_rtp47);
            case 53:
                return context.getString(R.string.x8_nav_rtp53);
            case 54:
                return context.getString(R.string.x8_nav_rtp54);
            case 55:
                return context.getString(R.string.x8_nav_rtp55);
            case 56:
                return context.getString(R.string.x8_nav_rtp56);
            case 57:
                return context.getString(R.string.x8_nav_rtp57);
            case 58:
                return context.getString(R.string.x8_nav_rtp58);
            case 59:
                return context.getString(R.string.x8_nav_rtp59);
            case 60:
                return context.getString(R.string.x8_nav_rtp60);
            case 61:
                return context.getString(R.string.x8_nav_rtp61);
            case 62:
                return context.getString(R.string.x8_nav_rtp62);
            case 63:
                return context.getString(R.string.x8_nav_rtp63);
            case 64:
                return context.getString(R.string.x8_nav_rtp64);
            default:
                return s;
        }
    }

    public static String getRtpStringFcCtrl(Context context, int code) {
        String s = isRtpAllShow ? context.getString(R.string.cmd_fail) + "error code=" + code : "";
        switch (code) {
            case 1:
                return context.getString(R.string.x8_ctrl_rtp1);
            case 2:
                return context.getString(R.string.x8_ctrl_rtp2);
            case 80:
                return context.getString(R.string.x8_ctrl_rtp50);
            case 81:
                return context.getString(R.string.x8_ctrl_rtp51);
            case 96:
                return context.getString(R.string.x8_ctrl_rtp60);
            case 97:
                return context.getString(R.string.x8_ctrl_rtp61);
            case 98:
                return context.getString(R.string.x8_ctrl_rtp62);
            case 113:
                return context.getString(R.string.x8_ctrl_rtp71);
            case 114:
                return context.getString(R.string.x8_ctrl_rtp72);
            case 116:
                return context.getString(R.string.x8_ctrl_rtp74);
            case 117:
                return context.getString(R.string.x8_ctrl_rtp75);
            case 118:
                return context.getString(R.string.x8_ctrl_rtp76);
            case 119:
                return context.getString(R.string.x8_ctrl_rtp77);
            case 121:
                return context.getString(R.string.x8_ctrl_rtp79);
            case 122:
                return context.getString(R.string.x8_ctrl_rtp7A);
            case 123:
                return context.getString(R.string.x8_ctrl_rtp7B);
            case 124:
                return context.getString(R.string.x8_ctrl_rtp7C);
            case 126:
                return context.getString(R.string.x8_ctrl_rtp7E);
            default:
                return s;
        }
    }

    public static String getRtpStringCamera(Context context, int code) {
        String s = "";
        switch (code) {
            case 1:
                return context.getString(R.string.x8_camera_rtp1);
            case 3:
                return context.getString(R.string.x8_camera_rtp3);
            case 8:
                return context.getString(R.string.x8_camera_rtp8);
            case 9:
                return context.getString(R.string.x8_camera_rtp9);
            case 10:
                return context.getString(R.string.x8_camera_rtp10);
            default:
                return s;
        }
    }
}
