package com.fimi.x8sdk.common;

public class Constants {
    public static final int A12_MSGID_CMD_UPLOAD = 3;
    public static final String A12_TCP_CMD_HOST = "192.168.42.1";
    public static final int A12_TCP_CMD_PORT = 10010;
    public static final int A12_TCP_File_PORT = 10011;
    public static String FIVE_KEY_CENTRE_KEY = "five_key_centre_key";
    public static String FIVE_KEY_DOWN_KEY = "five_key_down_key";
    public static String FIVE_KEY_LEFT_KEY = "five_key_left_key";
    public static String FIVE_KEY_RIGHT_KEY = "five_key_right_key";
    public static String FIVE_KEY_UP_KEY = "five_key_up_key";
    public static final int X8_GENERAL_GRID_LINE_CENTER_POINT = 1;
    public static final int X8_GENERAL_GRID_LINE_NINE_DIAGONAL = 3;
    public static final int X8_GENERAL_GRID_LINE_NINE_LINES = 2;
    public static final int X8_GENERAL_GRID_LINE_NOTHING = 0;
    public static int X8_GENERAL_MAP_STYLE_NORMAL = 0;
    public static int X8_GENERAL_MAP_STYLE_SATELLITE = 1;
    public static String X8_GLINE_LINE_OPTION = "x8_gline_line_option";
    public static int X8_INDEX_OPTION_LAND = 1;
    public static int X8_INDEX_OPTION_NONE = 0;
    public static int X8_INDEX_OPTION_RETURN = 2;
    public static String X8_LOW_POWER_LANDING = "x8_low_power_landing";
    public static String X8_LOW_POWER_RETURN = "x8_low_power_return";
    public static String X8_MAP_OPTION = "x8_map_option";
    public static String X8_MAP_RECTIFYIN_OPTION = "x8_map_rectifyin_option";
    public static String X8_MAP_STYLE = "x8_map_style_option";
    public static String X8_SHOW_LOG_OPTION = "x8_show_log_option";
    public static String X8_UNITY_OPTION = "x8_unity_option";

    public class X8S_Fw_Type {
        public static final byte BATTERY = (byte) 5;
        public static final byte CAMERA = (byte) 4;
        public static final byte CV = (byte) 9;
        public static final byte ESC = (byte) 14;
        public static final byte FC = (byte) 0;
        public static final byte FC_RL = (byte) 12;
        public static final byte GIMBAL = (byte) 3;
        public static final byte NOFLYZONE = (byte) 10;
        public static final byte RC = (byte) 1;
        public static final byte RC_RL = (byte) 11;
        public static final byte SERVO = (byte) -1;
        public static final byte ULTRASONIC = (byte) 13;
    }
}
