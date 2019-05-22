package com.fimi.x8sdk.command;

import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.x8sdk.command.X8BaseCmd.X8S_Module;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.cmd.CmdAiAutoPhoto;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePoints;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction;
import com.fimi.x8sdk.entity.GpsInfoCmd;
import java.util.Calendar;

public class FcCollection extends X8BaseCmd {
    public static final byte MSG_ATUO_BATTERRY = (byte) 5;
    public static final byte MSG_BLACK_BOX_30 = (byte) 48;
    public static final byte MSG_BLACK_BOX_31 = (byte) 49;
    public static final byte MSG_BLACK_BOX_32 = (byte) 50;
    public static final byte MSG_CHECK_IMU = (byte) 8;
    public static final byte MSG_CLOUD_CALIBRATION = (byte) 44;
    public static final byte MSG_CLOUD_CALIBRATION_CHECK = (byte) 45;
    public static final byte MSG_FC_AUTO_LAND = (byte) 21;
    public static final byte MSG_FC_AUTO_LAND_EXIT = (byte) 24;
    public static final byte MSG_FC_AUTO_NAVIGATION_STATE = (byte) 1;
    public static final byte MSG_FC_AUTO_TAKE_OFF = (byte) 16;
    public static final byte MSG_FC_AUTO_TAKE_OFF_EXIT = (byte) 19;
    public static final byte MSG_FC_ERRCODE = (byte) 4;
    public static final byte MSG_FC_HEART = (byte) 1;
    public static final byte MSG_FC_POINT_2_POINT_EXCUTE = (byte) 48;
    public static final byte MSG_FC_POINT_2_POINT_EXITE = (byte) 51;
    public static final byte MSG_FC_POINT_2_POINT_PAUSE = (byte) 49;
    public static final byte MSG_FC_POINT_2_POINT_RESUME = (byte) 50;
    public static final byte MSG_FC_SIGNAL_STATE = (byte) 3;
    public static final byte MSG_FC_SPORT_STATE = (byte) 2;
    public static final byte MSG_GET_CALI = (byte) 6;
    public static final byte MSG_GET_FC_AI_FOLLOW_ENABLE_BACK = (byte) 11;
    public static final byte MSG_GET_FC_PARAM = (byte) 6;
    public static final byte MSG_GET_FC_POINT_2_POINT = (byte) 53;
    public static final byte MSG_GET_FOLLOW_MODLE = (byte) 86;
    public static final byte MSG_GET_FOLLOW_SPEED = (byte) 89;
    public static final byte MSG_GET_IMU = (byte) 7;
    public static final byte MSG_GET_LINE_SET_POINTS = (byte) 38;
    public static final byte MSG_GET_LINE_SET_POINTSACTION = (byte) 39;
    public static final byte MSG_GET_LOSTACTION = (byte) 13;
    public static final byte MSG_GET_RETURN_HEIGHT = (byte) 9;
    public static final byte MSG_GET_SCREW_PRAMETER = (byte) 104;
    public static final byte MSG_GET_SPORT_MODE = (byte) 4;
    public static final byte MSG_GET_SURROUND_CIRCLE_DOT = (byte) 69;
    public static final byte MSG_GET_SURROUND_DEVICE_ORIENTATION = (byte) 73;
    public static final byte MSG_GET_SURROUND_SPEED = (byte) 71;
    public static final byte MSG_GROUP_CAMERA = (byte) 2;
    public static final byte MSG_GROUP_FC_BLACK_BOX = (byte) 10;
    public static final byte MSG_GROUP_FC_CTR = (byte) 4;
    public static final byte MSG_GROUP_FC_GIMBAL = (byte) 9;
    public static final byte MSG_GROUP_FC_MAINTENANCE = (byte) 13;
    public static final byte MSG_GROUP_FC_NAVI = (byte) 3;
    public static final byte MSG_GROUP_FC_NOFLY = (byte) 17;
    public static final byte MSG_GROUP_FC_TELEMETRY = (byte) 12;
    public static final byte MSG_GROUP_FC_TIME = (byte) 8;
    public static final byte MSG_GROUP_RC = (byte) 14;
    public static final byte MSG_GROUP_RC_CALI = (byte) 11;
    public static final byte MSG_GROUP_RC_CTRL = (byte) 11;
    public static final byte MSG_HOME_INFO = (byte) 6;
    public static final byte MSG_ID_AUTOSEND_PANORAMA_PHOTOGRAPH = (byte) 106;
    public static final byte MSG_ID_GET_AP_MODE = (byte) 21;
    public static final byte MSG_ID_GET_AUTO_HOME = (byte) 40;
    public static final byte MSG_ID_GET_BRAKE_SENS = (byte) 36;
    public static final byte MSG_ID_GET_LOW_POWER_OPERATION = (byte) 23;
    public static final byte MSG_ID_GET_OPTIC_FLOW = (byte) 15;
    public static final byte MSG_ID_GET_PILOT_MODE = (byte) 2;
    public static final byte MSG_ID_GET_RC_CTRL_MODE = (byte) 18;
    public static final byte MSG_ID_GET_RC_ChANGE_DIRECTION = (byte) 19;
    public static final byte MSG_ID_GET_ROCKER_EXP = (byte) 26;
    public static final byte MSG_ID_GET_SENSITIVITY = (byte) 38;
    public static final byte MSG_ID_GET_YAW_TRIP = (byte) 34;
    public static final byte MSG_ID_SET_AP_MODE = (byte) 10;
    public static final byte MSG_ID_SET_AP_MODE_RESTART = (byte) 15;
    public static final byte MSG_ID_SET_AUTO_HOME = (byte) 39;
    public static final byte MSG_ID_SET_BRAKE_SENS = (byte) 35;
    public static final byte MSG_ID_SET_DISABLE_TRIPOD = (byte) 42;
    public static final byte MSG_ID_SET_DISENABLE_AERAILSHOT = (byte) 44;
    public static final byte MSG_ID_SET_ENABLE_AERAILSHOT = (byte) 43;
    public static final byte MSG_ID_SET_ENABLE_TRIPOD = (byte) 41;
    public static final byte MSG_ID_SET_LOW_POWER_OPERATION = (byte) 24;
    public static final byte MSG_ID_SET_OPTIC_FLOW = (byte) 14;
    public static final byte MSG_ID_SET_PANORAMA_PHOTOGRAPH = (byte) 105;
    public static final byte MSG_ID_SET_PANORAMA_PHOTOGRAPH_STOP = (byte) 111;
    public static final byte MSG_ID_SET_PILOT_MODE = (byte) 1;
    public static final byte MSG_ID_SET_RC_CTRL_MODE = (byte) 17;
    public static final byte MSG_ID_SET_RC_FIVE_KEY = (byte) 16;
    public static final byte MSG_ID_SET_ROCKER_EXP = (byte) 25;
    public static final byte MSG_ID_SET_SENSITIVITY = (byte) 37;
    public static final byte MSG_ID_SET_YAW_TRIP = (byte) 33;
    public static final byte MSG_LOCK_MOTOR_STATE = (byte) 54;
    public static final byte MSG_NFZ_STATE = (byte) 3;
    public static final byte MSG_PILOT_MODE_FACTORY = (byte) 3;
    public static final byte MSG_PILOT_MODE_PRIMARY = (byte) 0;
    public static final byte MSG_PILOT_MODE_SEMI_SENIOR = (byte) 1;
    public static final byte MSG_PILOT_MODE_SENIOR = (byte) 2;
    public static final byte MSG_RC_CHECK_CMD = (byte) 15;
    public static final byte MSG_RC_SET_CMD = (byte) 14;
    public static final byte MSG_RC_STATE_CMD = (byte) 4;
    public static final byte MSG_REFUSE_NO_FLY = (byte) 1;
    public static final byte MSG_REQ_NO_FLY_NORMAL = (byte) 2;
    public static final byte MSG_REST_SYSTEM_PARAMS = (byte) -119;
    public static final byte MSG_SET_ACCURATE_LANDING = (byte) 108;
    public static final byte MSG_SET_AUTO_PHOTO_EXCUTE = (byte) 56;
    public static final byte MSG_SET_AUTO_PHOTO_EXIT = (byte) 59;
    public static final byte MSG_SET_AUTO_PHOTO_PAUSE = (byte) 57;
    public static final byte MSG_SET_AUTO_PHOTO_RESUME = (byte) 58;
    public static final byte MSG_SET_AUTO_PHOTO_VALUE = (byte) 60;
    public static final byte MSG_SET_CALI = (byte) 5;
    public static final byte MSG_SET_DISENABLE_ACCURATE = (byte) 52;
    public static final byte MSG_SET_DISENABLE_FIXWING = (byte) 48;
    public static final byte MSG_SET_DISENABLE_HEADING_FREE = (byte) 46;
    public static final byte MSG_SET_ENABLE_ACCURATE = (byte) 51;
    public static final byte MSG_SET_ENABLE_FIXWING = (byte) 47;
    public static final byte MSG_SET_ENABLE_FIXWING_STATE = (byte) 49;
    public static final byte MSG_SET_ENDABLE_HEADING_FREE = (byte) 45;
    public static final byte MSG_SET_FC_AI_FOLLOW_ENABLE_BACK = (byte) 10;
    public static final byte MSG_SET_FC_PARAM = (byte) 5;
    public static final byte MSG_SET_FC_POINT_2_POINT = (byte) 52;
    public static final byte MSG_SET_FC_RETURE_HOME_EXCUTE = (byte) 26;
    public static final byte MSG_SET_FC_RETURE_HOME_EXITE = (byte) 29;
    public static final byte MSG_SET_FC_RETURE_HOME_PAUSE = (byte) 27;
    public static final byte MSG_SET_FC_RETURE_HOME_RESUME = (byte) 28;
    public static final byte MSG_SET_FOLLOW_CLOSE = (byte) 97;
    public static final byte MSG_SET_FOLLOW_ERROR_CODE = (byte) 87;
    public static final byte MSG_SET_FOLLOW_EXCUTE = (byte) 80;
    public static final byte MSG_SET_FOLLOW_EXIT = (byte) 83;
    public static final byte MSG_SET_FOLLOW_MODLE = (byte) 85;
    public static final byte MSG_SET_FOLLOW_NOTITY_FC = (byte) 98;
    public static final byte MSG_SET_FOLLOW_OPEN = (byte) 96;
    public static final byte MSG_SET_FOLLOW_PAUSE = (byte) 81;
    public static final byte MSG_SET_FOLLOW_RESUME = (byte) 82;
    public static final byte MSG_SET_FOLLOW_SPEED = (byte) 88;
    public static final byte MSG_SET_FOLLOW_STANDBY = (byte) 84;
    public static final byte MSG_SET_GET_ACCURATE = (byte) 53;
    public static final byte MSG_SET_HEADING_FREE_UPDATE = (byte) 50;
    public static final byte MSG_SET_HOME_POINT = (byte) 90;
    public static final byte MSG_SET_LINE_EXCUTE = (byte) 32;
    public static final byte MSG_SET_LINE_EXIT = (byte) 35;
    public static final byte MSG_SET_LINE_PAUSE = (byte) 33;
    public static final byte MSG_SET_LINE_RESUME = (byte) 34;
    public static final byte MSG_SET_LINE_SET_POINTS = (byte) 36;
    public static final byte MSG_SET_LINE_SET_POINTSACTION = (byte) 37;
    public static final byte MSG_SET_LOSTACTION = (byte) 12;
    public static final byte MSG_SET_RETURN_HEIGHT = (byte) 8;
    public static final byte MSG_SET_SCREW_EXITE = (byte) 102;
    public static final byte MSG_SET_SCREW_PAUSE = (byte) 100;
    public static final byte MSG_SET_SCREW_PRAMETER = (byte) 103;
    public static final byte MSG_SET_SCREW_RESUME = (byte) 101;
    public static final byte MSG_SET_SCREW_START = (byte) 99;
    public static final byte MSG_SET_SPORT_MODE = (byte) 3;
    public static final byte MSG_SET_SURROUND_CIRCLE_DOT = (byte) 68;
    public static final byte MSG_SET_SURROUND_DEVICE_ORIENTATION = (byte) 72;
    public static final byte MSG_SET_SURROUND_EXCUTE = (byte) 64;
    public static final byte MSG_SET_SURROUND_EXIT = (byte) 67;
    public static final byte MSG_SET_SURROUND_PAUSE = (byte) 65;
    public static final byte MSG_SET_SURROUND_RESUME = (byte) 66;
    public static final byte MSG_SET_SURROUND_SPEED = (byte) 70;
    public static final byte MSG_SYNC_FC_GPS = (byte) 5;
    public static final byte MSG_SYNC_FC_PRESSURE = (byte) 6;
    public static final byte MSG_SYNC_FC_TIME = (byte) 4;
    public static final int MSG_SYS_CTRL_MODE = 107;
    public static final byte MSG_UPDATE_SURROUND_STATE = (byte) 74;
    public static final byte RC_MODE_AMERICAN = (byte) 1;
    public static final byte RC_MODE_CHINESE = (byte) 3;
    public static final byte RC_MODE_JAPANESE = (byte) 2;
    public static final int SENSORTYPE_IMUM = 1;
    public static final int SENSORTYPE_IMUS = 2;
    public static byte X8_FC_SET_AP_MODE_CE = (byte) 0;
    public static byte X8_FC_SET_AP_MODE_FCC = (byte) 1;
    public static final byte X8_MSG_RC_CANCEL_CODE = (byte) 3;
    public static final byte X8_MSG_RC_MATCH_CODE = (byte) 1;
    public static final byte X8_MSG_RC_MATCH_RT = (byte) 2;
    private BaseCommand aiLinePoint;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBack;

    public FcCollection(IPersonalDataCallBack callBack, UiCallBackListener uiCallBack) {
        this.personalDataCallBack = callBack;
        this.uiCallBack = uiCallBack;
    }

    private X8SendCmd getFCBase(byte moduleName) {
        LinkPacket4 packet4 = new LinkPacket4();
        packet4.getHeader4().setType((byte) 1);
        packet4.getHeader4().setEncryptType((byte) 0);
        packet4.getHeader4().setSrcId((byte) X8S_Module.MODULE_GCS.ordinal());
        packet4.getHeader4().setDestId(moduleName);
        packet4.getHeader4().setSeq(this.seqIndex);
        X8SendCmd x8SendCmd = new X8SendCmd(packet4);
        x8SendCmd.setPersonalDataCallBack(this.personalDataCallBack);
        x8SendCmd.setUiCallBack(this.uiCallBack);
        return x8SendCmd;
    }

    private X8SendCmd getFCBase(byte moduleName, int type) {
        LinkPacket4 packet4 = new LinkPacket4();
        packet4.getHeader4().setType((byte) type);
        packet4.getHeader4().setEncryptType((byte) 0);
        packet4.getHeader4().setSrcId((byte) X8S_Module.MODULE_GCS.ordinal());
        packet4.getHeader4().setDestId(moduleName);
        packet4.getHeader4().setSeq(this.seqIndex);
        X8SendCmd x8SendCmd = new X8SendCmd(packet4);
        x8SendCmd.setPersonalDataCallBack(this.personalDataCallBack);
        x8SendCmd.setUiCallBack(this.uiCallBack);
        x8SendCmd.setReSendNum(0);
        return x8SendCmd;
    }

    public X8SendCmd takeOff(byte cmdID) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, cmdID, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd land(byte cmdID) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, cmdID, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd reponseNoFlyNormal() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_NFZ.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 17, (byte) 1, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getNoFlyNormal() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_NFZ.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 17, (byte) 2, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setPilotMode(byte mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 1, (byte) 0, (byte) 0, mode});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getPilotMode() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 2, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setReturnHeight(float height) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[8];
        payload[0] = (byte) 4;
        payload[1] = (byte) 8;
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        byte[] h = ByteUtil.float2byte(height);
        System.arraycopy(h, 0, payload, 4, h.length);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getReturnHeight() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 9, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setLostAction(byte action) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 12, (byte) 0, (byte) 0, action});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getLostAction() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 13, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setFcParam(byte paramIndex, float paramData) {
        Log.i("moweiru", "paramData:" + paramData);
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        payload[0] = (byte) 4;
        payload[1] = (byte) 5;
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        payload[4] = paramIndex;
        byte[] params = ByteUtil.float2byte(paramData);
        System.arraycopy(params, 0, payload, 5, params.length);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getFcParam(byte parmIndex) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 6, (byte) 0, (byte) 0, parmIndex});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowCmd(int msgCmd) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = (byte) msgCmd;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowVcEnable(int msgCmd) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = (byte) msgCmd;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowModle(int type) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, MSG_SET_FOLLOW_MODLE, (byte) 0, (byte) 0, (byte) type});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand getAiFollowModle() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, MSG_GET_FOLLOW_MODLE, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowSpeed(int value) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[6];
        int i = 0 + 1;
        payload[0] = (byte) 3;
        int i2 = i + 1;
        payload[i] = MSG_SET_FOLLOW_SPEED;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) (value >> 0);
        i2 = i + 1;
        payload[i] = (byte) (value >> 8);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiFollowSpeed() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 3;
        int i2 = i + 1;
        payload[i] = MSG_GET_FOLLOW_SPEED;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowEnableBack(int flag) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 10, (byte) 0, (byte) 0, (byte) flag});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiFollowEnableBack() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        payload[0] = (byte) 4;
        payload[1] = (byte) 11;
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setAiFollowPoint2Point(double longitude, double latitude, int altitude, int speed) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[25];
        int i = 0 + 1;
        payload[0] = (byte) 3;
        int i2 = i + 1;
        payload[i] = (byte) 52;
        i2 += 2;
        System.arraycopy(ByteHexHelper.getDoubleBytes(longitude), 0, payload, i2, 8);
        i2 += 8;
        System.arraycopy(ByteHexHelper.getDoubleBytes(latitude), 0, payload, i2, 8);
        i2 += 8;
        i = i2 + 1;
        payload[i2] = (byte) (altitude >> 0);
        i2 = i + 1;
        payload[i] = (byte) (altitude >> 8);
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) speed;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        String hex = ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd setAiFollowPoint2PointExcute(byte msgId) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = msgId;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiSurroundExcute(byte msgId) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = msgId;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setAiSurroundPoint(int msgId, double longitude, double latitude, float altitude, double longitudeTakeoff, double latitudeTakeoff, float altitudeTakeoff, int type) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[44];
        int i = 0 + 1;
        payload[0] = (byte) 3;
        int i2 = i + 1;
        payload[i] = (byte) msgId;
        i2 += 2;
        System.arraycopy(ByteHexHelper.getDoubleBytes(longitude), 0, payload, i2, 8);
        i2 += 8;
        System.arraycopy(ByteHexHelper.getDoubleBytes(latitude), 0, payload, i2, 8);
        i2 += 8;
        int alt = (int) (10.0f * altitude);
        i = i2 + 1;
        payload[i2] = (byte) (alt >> 0);
        i2 = i + 1;
        payload[i] = (byte) (alt >> 8);
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        System.arraycopy(ByteHexHelper.getDoubleBytes(longitudeTakeoff), 0, payload, i2, 8);
        i2 += 8;
        System.arraycopy(ByteHexHelper.getDoubleBytes(latitudeTakeoff), 0, payload, i2, 8);
        i2 += 8;
        alt = (int) (10.0f * altitudeTakeoff);
        i = i2 + 1;
        payload[i2] = (byte) (alt >> 0);
        i2 = i + 1;
        payload[i] = (byte) (alt >> 8);
        i = i2 + 1;
        payload[i2] = (byte) type;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        String hex = ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd setAiSurroundSpeed(byte msgId, int value) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[8];
        int i = 0 + 1;
        payload[0] = (byte) 3;
        int i2 = i + 1;
        payload[i] = msgId;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) (value >> 0);
        i2 = i + 1;
        payload[i] = (byte) (value >> 8);
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiSurroundOrientation(byte msgId, int value) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[8];
        int i = 0 + 1;
        payload[0] = (byte) 3;
        int i2 = i + 1;
        payload[i] = msgId;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) value;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiSurroundSpeed() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = MSG_GET_SURROUND_SPEED;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiSurroundOrientation() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = MSG_GET_SURROUND_DEVICE_ORIENTATION;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiSurroundPoint() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = MSG_GET_SURROUND_CIRCLE_DOT;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiRetureHome(int msgId) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = (byte) msgId;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiLinePoints(CmdAiLinePoints points) {
        int i = 1;
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[58];
        int i2 = 0 + 1;
        payload[0] = (byte) 3;
        int i3 = i2 + 1;
        payload[i2] = (byte) 36;
        i2 = i3 + 1;
        payload[i3] = (byte) 0;
        i3 = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i3 + 1;
        payload[i3] = (byte) points.nPos;
        i3 = i2 + 1;
        payload[i2] = (byte) points.count;
        i2 = i3 + 1;
        payload[i3] = (byte) 0;
        i3 = i2 + 1;
        payload[i2] = (byte) 0;
        System.arraycopy(ByteHexHelper.getDoubleBytes(points.longitude), 0, payload, i3, 8);
        i3 += 8;
        System.arraycopy(ByteHexHelper.getDoubleBytes(points.latitude), 0, payload, i3, 8);
        i3 += 8;
        i2 = i3 + 1;
        payload[i3] = (byte) (points.altitude >> 0);
        i3 = i2 + 1;
        payload[i2] = (byte) (points.altitude >> 8);
        int angle1 = ((int) points.angle) * 100;
        i2 = i3 + 1;
        payload[i3] = (byte) (angle1 >> 0);
        i3 = i2 + 1;
        payload[i2] = (byte) (angle1 >> 8);
        i2 = i3 + 1;
        payload[i3] = (byte) (points.gimbalPitch >> 0);
        i3 = i2 + 1;
        payload[i2] = (byte) (points.gimbalPitch >> 8);
        i2 = i3 + 1;
        payload[i3] = (byte) points.speed;
        i3 = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i3 + 1;
        payload[i3] = (byte) 0;
        i3 = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i3 + 1;
        payload[i3] = (byte) (((points.autoRecord << 4) | points.pioEnbale) | 2);
        i3 = i2 + 1;
        payload[i2] = (byte) (points.orientation | (points.rotation << 4));
        i2 = i3 + 1;
        if (points.orientation != 1) {
            i = 0;
        }
        payload[i3] = (byte) i;
        i3 = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i3 + 1;
        payload[i3] = (byte) points.compeletEvent;
        i3 = i2 + 1;
        payload[i2] = (byte) points.disconnectEvent;
        System.arraycopy(ByteHexHelper.getDoubleBytes(points.longitudePIO), 0, payload, i3, 8);
        i3 += 8;
        System.arraycopy(ByteHexHelper.getDoubleBytes(points.latitudePIO), 0, payload, i3, 8);
        i3 += 8;
        i2 = i3 + 1;
        payload[i3] = (byte) (points.altitudePIO >> 0);
        i3 = i2 + 1;
        payload[i2] = (byte) (points.altitudePIO >> 8);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiLinePointsAction(CmdAiLinePointsAction action) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[56];
        int i = 0 + 1;
        payload[0] = (byte) 3;
        int i2 = i + 1;
        payload[i] = (byte) 37;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) action.pos;
        i2 = i + 1;
        payload[i] = (byte) action.count;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) action.cmd0;
        i2 = i + 1;
        payload[i] = (byte) action.cmd1;
        i2 += 14;
        i = i2 + 1;
        payload[i2] = (byte) action.time;
        i2 = i + 1;
        payload[i] = (byte) action.para0;
        i = i2 + 1;
        payload[i2] = (byte) action.para1;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand getAiLinePointsAction(int number) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        payload[0] = (byte) 3;
        payload[1] = (byte) 39;
        payload[4] = (byte) number;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiLineExcute() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = (byte) 32;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiLineExite() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = (byte) 35;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand getAiLinePoint(int number) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        payload[0] = (byte) 3;
        payload[1] = (byte) 38;
        payload[4] = (byte) number;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setAiAutoPhotoValue(CmdAiAutoPhoto aiAutoPhotoValue) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[11];
        int i = 0 + 1;
        payload[0] = (byte) 3;
        int i2 = i + 1;
        payload[i] = MSG_SET_AUTO_PHOTO_VALUE;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) (aiAutoPhotoValue.angle >> 0);
        i2 = i + 1;
        payload[i] = (byte) (aiAutoPhotoValue.angle >> 8);
        i = i2 + 1;
        payload[i2] = (byte) (aiAutoPhotoValue.routeLength >> 0);
        i2 = i + 1;
        payload[i] = (byte) (aiAutoPhotoValue.routeLength >> 8);
        i = i2 + 1;
        payload[i2] = (byte) aiAutoPhotoValue.speed;
        i2 = i + 1;
        payload[i] = (byte) aiAutoPhotoValue.config;
        i = i2 + 1;
        payload[i2] = (byte) aiAutoPhotoValue.mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiAutoPhotoExcute() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = MSG_SET_AUTO_PHOTO_EXCUTE;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiAutoPhotoExit() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 3;
        payload[1] = MSG_SET_AUTO_PHOTO_EXIT;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setCalibrationStart(int type, int cmd, int mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[7];
        int i = 0 + 1;
        payload[0] = (byte) 13;
        int i2 = i + 1;
        payload[i] = (byte) 5;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) type;
        i2 = i + 1;
        payload[i] = (byte) cmd;
        i = i2 + 1;
        payload[i2] = (byte) mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setAircrftCalibrationStart(int type, int cmd, int mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[7];
        int i = 0 + 1;
        payload[0] = (byte) 13;
        int i2 = i + 1;
        payload[i] = (byte) 5;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) type;
        i2 = i + 1;
        payload[i] = (byte) cmd;
        i = i2 + 1;
        payload[i2] = (byte) mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand getAircrftCalibrationState(int sensorType, int type) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal(), 0);
        payload = new byte[6];
        int i = 0 + 1;
        payload[0] = (byte) 13;
        int i2 = i + 1;
        payload[i] = (byte) 6;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) sensorType;
        i2 = i + 1;
        payload[i] = (byte) type;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand getCalibrationState(int sensorType, int type) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[6];
        int i = 0 + 1;
        payload[0] = (byte) 13;
        int i2 = i + 1;
        payload[i] = (byte) 6;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) sensorType;
        i2 = i + 1;
        payload[i] = (byte) type;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setSyncTimeCmd() {
        Calendar calendar = Calendar.getInstance();
        short year = (short) calendar.get(1);
        byte month = (byte) (calendar.get(2) + 1);
        byte day = (byte) calendar.get(5);
        byte hour = (byte) calendar.get(11);
        byte min = (byte) calendar.get(12);
        byte second = (byte) calendar.get(13);
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal(), 0);
        byte[] payload = new byte[11];
        int i = 0 + 1;
        payload[0] = (byte) 8;
        int i2 = i + 1;
        payload[i] = (byte) 4;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) (year & 255);
        i2 = i + 1;
        payload[i] = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & year) >> 8);
        i = i2 + 1;
        payload[i2] = month;
        i2 = i + 1;
        payload[i] = day;
        i = i2 + 1;
        payload[i2] = hour;
        i2 = i + 1;
        payload[i] = min;
        i = i2 + 1;
        payload[i2] = second;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setApMode(byte mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_REPEATER_RC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 14;
        int i2 = i + 1;
        payload[i] = (byte) 10;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setApModeRestart() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_REPEATER_RC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 14;
        int i2 = i + 1;
        payload[i] = (byte) 15;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 1;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getApMode() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 21;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setLowPowerOperation(int lowPowerValue, int seriousLowPowerValue, int lowPowerOpt, int seriousLowPowerOpt) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[8];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 24;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) lowPowerValue;
        i2 = i + 1;
        payload[i] = (byte) seriousLowPowerValue;
        i = i2 + 1;
        payload[i2] = (byte) lowPowerOpt;
        i2 = i + 1;
        payload[i] = (byte) seriousLowPowerOpt;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getLowPowerOperation() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = MSG_ID_GET_LOW_POWER_OPERATION;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getIMUInfoCmd(int imuType) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 12;
        int i2 = i + 1;
        payload[i] = (byte) 7;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) imuType;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkIMUInfoCmd() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 13;
        int i2 = i + 1;
        payload[i] = (byte) 7;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkIMUException(int sensorType) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 13;
        int i2 = i + 1;
        payload[i] = (byte) 8;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) sensorType;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getCheckIMUStatus(int sensorType) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        payload[0] = (byte) 13;
        payload[1] = (byte) 8;
        payload[4] = (byte) sensorType;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setCloudCalibrationCmd(int status) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = (byte) 44;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) status;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkCloudCalibrationCmd() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = (byte) 45;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd RCMatchOrCancelCode(int doneType) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_RC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 14;
        int i2 = i + 1;
        payload[i] = (byte) doneType;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkMatchCodeProgress() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_RC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 14;
        int i2 = i + 1;
        payload[i] = (byte) 2;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd rcCalibration(int cmdType) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_RC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 11;
        int i2 = i + 1;
        payload[i] = (byte) 14;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) cmdType;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkRCCalibration() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_RC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 11;
        int i2 = i + 1;
        payload[i] = (byte) 15;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setOpticFlow(boolean isOpen) {
        int i = 0;
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i2 = 0 + 1;
        payload[0] = (byte) 4;
        int i3 = i2 + 1;
        payload[i2] = (byte) 14;
        i2 = i3 + 1;
        payload[i3] = (byte) 0;
        i3 = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i3 + 1;
        if (isOpen) {
            i = 1;
        }
        payload[i3] = (byte) i;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getOpticFlow() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 15;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAttitudeSensitivity(int rollPercent, int pitchPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 37;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 3;
        i2 = i + 1;
        payload[i] = (byte) rollPercent;
        i = i2 + 1;
        payload[i2] = (byte) pitchPercent;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setYawSensitivity(int yawPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 37;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 4;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) yawPercent;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getSensitivity() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 38;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setBrakeSens(int rollPercent, int pitchPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 35;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 3;
        i2 = i + 1;
        payload[i] = (byte) rollPercent;
        i = i2 + 1;
        payload[i2] = (byte) pitchPercent;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getBrakeSens() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 36;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setYawTrip(int yawValue) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 33;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 4;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) yawValue;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getYawTrip() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 34;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setUpDownRockerExp(int throttlePercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = MSG_ID_SET_ROCKER_EXP;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 8;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) throttlePercent;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd restSystemParams() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = MSG_REST_SYSTEM_PARAMS;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 1;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setLockMotor(int lock) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = MSG_LOCK_MOTOR_STATE;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) lock;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setSportMode(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[6];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 3;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 7;
        i2 = i + 1;
        payload[i] = (byte) enable;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getSportMode() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 4;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setLeftRightRockerExp(int yawPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = MSG_ID_SET_ROCKER_EXP;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 4;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) yawPercent;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGoBackRockerExp(int rollPercent, int pitchPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = MSG_ID_SET_ROCKER_EXP;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 3;
        i2 = i + 1;
        payload[i] = (byte) rollPercent;
        i = i2 + 1;
        payload[i2] = (byte) pitchPercent;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getRockerExp() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 4;
        int i2 = i + 1;
        payload[i] = (byte) 26;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setHomePoint(float height, double lat, double lng, int mode, float accuracy) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[32];
        payload[0] = (byte) 3;
        payload[1] = (byte) 90;
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        int i = 4 + 1;
        payload[4] = (byte) mode;
        int i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        System.arraycopy(ByteUtil.float2byte(accuracy), 0, payload, i2, 4);
        i2 += 4;
        System.arraycopy(ByteUtil.float2byte(height), 0, payload, i2, 4);
        i2 += 4;
        System.arraycopy(ByteHexHelper.getDoubleBytes(lng), 0, payload, i2, 8);
        i2 += 8;
        System.arraycopy(ByteHexHelper.getDoubleBytes(lat), 0, payload, i2, 8);
        i2 += 8;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setCtrlMode(byte mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_RC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 11;
        int i2 = i + 1;
        payload[i] = (byte) 17;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getCtrlMode() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_RC.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 11;
        int i2 = i + 1;
        payload[i] = (byte) 18;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAutoHomePoint(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 39, (byte) 0, (byte) 0, (byte) enable});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAutoHomePoint() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, MSG_ID_GET_AUTO_HOME, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setEnableFixwing() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, MSG_SET_ENABLE_FIXWING, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setDisenableFixwing() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 48, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setEnableHeadingFree() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 45, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setUpdateHeadingFree() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 50, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setDisenableHeadingFree() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, MSG_SET_DISENABLE_HEADING_FREE, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setEnableTripod(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 4;
        if (enable == 1) {
            payload[1] = MSG_ID_SET_ENABLE_TRIPOD;
        } else if (enable == 0) {
            payload[1] = MSG_ID_SET_DISABLE_TRIPOD;
        }
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setEnableAerailShot(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        payload[0] = (byte) 4;
        if (enable == 1) {
            payload[1] = MSG_ID_SET_ENABLE_AERAILSHOT;
        } else if (enable == 0) {
            payload[1] = (byte) 44;
        }
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewPrameter(AckAiScrewPrameter prameter) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        payload = new byte[12];
        int i = 0 + 1;
        payload[0] = (byte) 3;
        int i2 = i + 1;
        payload[i] = MSG_SET_SCREW_PRAMETER;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) (prameter.getDistance() >> 0);
        i2 = i + 1;
        payload[i] = (byte) (prameter.getDistance() >> 8);
        i = i2 + 1;
        payload[i2] = (byte) prameter.getCiclePeriod();
        i2 = i + 1;
        payload[i] = (byte) prameter.getVertSpeed();
        i = i2 + 1;
        payload[i2] = (byte) prameter.getRTHTostart();
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getScrewPrameter() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, MSG_GET_SCREW_PRAMETER, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewStart() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, MSG_SET_SCREW_START, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewPause() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, MSG_SET_SCREW_PAUSE, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewResume() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, MSG_SET_SCREW_RESUME, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewExite() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, MSG_SET_SCREW_EXITE, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAccurateLanding(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 4;
        if (enable == 1) {
            payload[1] = (byte) 51;
        } else {
            payload[1] = (byte) 52;
        }
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAccurateLanding() {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 4, (byte) 53, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd sysCtrlMode2AiVc(int ctrlMode) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[]{(byte) 3, (byte) 107, (byte) 0, (byte) 0, (byte) ctrlMode};
        sendCmd.setAddToSendQueue(false);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setPanoramaPhotographType(int panoramaPhotographType) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, MSG_ID_SET_PANORAMA_PHOTOGRAPH, (byte) 0, (byte) 0, (byte) panoramaPhotographType});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setPanoramaPhotographState(byte state) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 3, state, (byte) 0, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setPressureInfo(float alt, float hPa) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal(), 0);
        byte[] payload = new byte[12];
        payload[0] = (byte) 8;
        payload[1] = (byte) 6;
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        System.arraycopy(ByteUtil.float2byte(alt), 0, payload, 4, 4);
        int i = 4 + 4;
        System.arraycopy(ByteUtil.float2byte(hPa), 0, payload, i, 4);
        i += 4;
        sendCmd.setAddToSendQueue(false);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGpsInfo(GpsInfoCmd gps) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_FC.ordinal(), 0);
        byte[] payload = new byte[32];
        payload[0] = (byte) 8;
        payload[1] = (byte) 5;
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        System.arraycopy(ByteHexHelper.getDoubleBytes(gps.mLongitude), 0, payload, 4, 8);
        int i = 4 + 8;
        System.arraycopy(ByteHexHelper.getDoubleBytes(gps.mLatitude), 0, payload, i, 8);
        i += 8;
        System.arraycopy(ByteUtil.float2byte(gps.mAltitude), 0, payload, i, 4);
        i += 4;
        int i2 = i + 1;
        payload[i] = (byte) gps.mHorizontalAccuracyMeters;
        i = i2 + 1;
        payload[i2] = (byte) gps.mVerticalAccuracyMeters;
        System.arraycopy(ByteUtil.float2byte(gps.mSpeed), 0, payload, i, 4);
        i += 4;
        i2 = i + 1;
        payload[i] = (byte) (gps.mBearing >> 0);
        i = i2 + 1;
        payload[i2] = (byte) (gps.mBearing >> 8);
        sendCmd.setAddToSendQueue(false);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }
}
