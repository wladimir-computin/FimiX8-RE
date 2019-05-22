package com.fimi.x8sdk.command;

public abstract class X8BaseCmd {
    protected static final byte ENCRYPRTION_YES = (byte) 1;
    protected static final byte ENCRYPTION_NO = (byte) 0;
    protected static final byte FM_LINK_VERSION = (byte) 4;
    public static final byte MODULE_BATTERY = (byte) 15;
    public static final byte MODULE_CAMERA = (byte) 3;
    public static final byte MODULE_CV = (byte) 10;
    public static final byte MODULE_ESC = (byte) 18;
    public static final byte MODULE_FC = (byte) 2;
    public static final byte MODULE_GCS = (byte) 7;
    public static final byte MODULE_GIMBAL = (byte) 8;
    public static final byte MODULE_NFZ = (byte) 17;
    public static final byte MODULE_RC = (byte) 13;
    public static final byte MODULE_REPEATER_VEHICLE = (byte) 14;
    public static final byte MODULE_REPEAT_RC = (byte) 16;
    public static final byte MODULE_SERVO = (byte) 19;
    public static final byte MODULE_UAV = (byte) 1;
    protected static final byte TYPE_ACK = (byte) 2;
    protected static final byte TYPE_AUTOSEND = (byte) 0;
    protected static final byte TYPE_CMD = (byte) 1;
    static int count = 0;
    protected short seqIndex = (short) 0;

    public enum X8S_Module {
        MODULE_IDLE,
        MODULE_UAV,
        MODULE_FC,
        MODULE_CAMERA,
        MODULE_OPTFLOW,
        MODULE_OBSAVOID,
        MODULE_HTTP,
        MODULE_GCS,
        MODULE_GIMBAL,
        MODULE_BLACKBOX,
        MODULE_CV,
        MODULE_SV_DWN,
        MODULE_SV_FW,
        MODULE_RC,
        MODULE_REPEATER_VEHICLE,
        MODULE_BATTERY,
        MODULE_REPEATER_RC,
        MODULE_NFZ,
        MODULE_ESC,
        MODULE_SERVO,
        MODULE_Default0X14,
        MODULE_Default0X15,
        MODULE_ULTRASONIC
    }

    public X8BaseCmd() {
        int i = count;
        count = i + 1;
        this.seqIndex = (short) i;
        if (count == 32766) {
            count = 0;
        }
    }
}
