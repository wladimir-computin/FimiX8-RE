package com.fimi.x8sdk.command;

import com.alibaba.fastjson.JSON;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.dataparser.CameraJsonData;
import com.fimi.x8sdk.modulestate.StateManager;

public class CameraJsonCollection extends X8BaseCmd {
    public static final int CMD_BURNIN_FW = 8;
    public static final int CMD_CANCEL_GET_FILE = 1287;
    public static final int CMD_CD = 1283;
    public static final int CMD_CHANGE_BITRATE = 16;
    public static final int CMD_CONTINUE_CAPTURE_STOP = 770;
    public static final int CMD_DEL_FILE = 1281;
    public static final int CMD_DIGITAL_ZOOM = 14;
    public static final int CMD_DIGITAL_ZOOM_INFO = 15;
    public static final int CMD_FORCE_SPLIT = 516;
    public static final int CMD_FORMAT = 4;
    public static final int CMD_GET_ALL_CURRENT_SETTINGS = 3;
    public static final int CMD_GET_BATTERY_LEVEL = 13;
    public static final int CMD_GET_DEVICE_INFO = 11;
    public static final int CMD_GET_DV_VERSION = 18;
    public static final int CMD_GET_FILE = 1285;
    public static final int CMD_GET_MEDIAINFO = 1026;
    public static final int CMD_GET_NUMB_FILES = 6;
    public static final int CMD_GET_RECORD_TIME = 515;
    public static final int CMD_GET_REMAIN_TIME = 19;
    public static final int CMD_GET_SETTING = 1;
    public static final int CMD_GET_SINGLE_SETTING_OPTIONS = 9;
    public static final int CMD_GET_SPACE = 5;
    public static final int CMD_GET_THUMB = 1025;
    public static final int CMD_GET_WIFI_SETTING = 1539;
    public static final int CMD_LS = 1282;
    public static final int CMD_NOTIFICATION = 7;
    public static final int CMD_POWER_MANAGER = 12;
    public static final int CMD_PUT_FILE = 1286;
    public static final int CMD_PUT_GPS_INFO = 10;
    public static final int CMD_PWD = 1284;
    public static final int CMD_QUERY_SESSION_HOLDER = 1793;
    public static final int CMD_RECORD_START = 513;
    public static final int CMD_RECORD_STOP = 514;
    public static final int CMD_RESETVF = 259;
    public static final int CMD_RESET_DEFAULT = 17;
    public static final int CMD_SET_CLIENT_INFO = 261;
    public static final int CMD_SET_MEDIA_ATTRIBUTE = 1027;
    public static final int CMD_SET_SETTING = 2;
    public static final int CMD_SET_WIFI_SETTING = 1538;
    public static final int CMD_START_SESSION = 257;
    public static final int CMD_STOP_SESSION = 258;
    public static final int CMD_STOP_VF = 260;
    public static final int CMD_TAKE_PHOTO = 769;
    public static final int CMD_WIFI_RESTART = 1537;
    public static final String CUR_TIME = "cur_time";
    public static final String KEY_AE_BIAS = "ae_bias";
    public static final String KEY_AE_ISO = "iso";
    public static final String KEY_APP_STATUS = "app_status";
    public static final String KEY_AWB = "awb";
    public static final String KEY_CAMERA_LOCK = "camera_clock";
    public static final String KEY_CAMERA_STYLE = "camera_style";
    public static final String KEY_CAPTURE_MODE = "capture_mode";
    public static final String KEY_CONTRAST = "contrast";
    public static final String KEY_DEFAULT_SYSTEM = "default_setting";
    public static final String KEY_DEF_SETTING = "default_setting";
    public static final String KEY_DE_CONTROL_AUTO = "auto";
    public static final String KEY_DE_CONTROL_MANUAL = "manual";
    public static final String KEY_DE_CONTROL_TYPE = "de_control";
    public static final String KEY_DIGITAL_EFFECT = "digital_effect";
    public static final String KEY_DROOM = "dzoom";
    public static final String KEY_IMAGE_FORMAT = "photo_format";
    public static final String KEY_METERMING_MODE = "metering_mode";
    public static final String KEY_NOTICE_TYPE_AE_BIAS = "ae_bias";
    public static final String KEY_NOTICE_TYPE_EVBIAS = "EVBIAS";
    public static final String KEY_NOTICE_TYPE_SYNC = "sync";
    public static final String KEY_PHOTO_MODE = "capture_mode";
    public static final String KEY_PHOTO_QUALITY = "photo_quality";
    public static final String KEY_PHOTO_SIZE = "photo_size";
    public static final String KEY_PHOTO_STAMP = "photo_stamp";
    public static final String KEY_PHOTO_TIMELAPSE = "photo_timelapse";
    public static final String KEY_RECORD_AUTO_LOW_LIGHT = "auto_low_light";
    public static final String KEY_RECORD_MODE = "record_mode";
    public static final String KEY_RVAL = "ravl";
    public static final String KEY_SATURATION = "saturation";
    public static final String KEY_SAVE_LOW_RESOLUTION_CLIP = "save_low_resolution_clip";
    public static final String KEY_SHARPNESS = "sharpness";
    public static final String KEY_SHUTTER_TIME = "shutter_time";
    public static final String KEY_STREAM_OUT_TYPE = "stream_out_type";
    public static final String KEY_SYSTEM_TTYPE = "system_type";
    public static final String KEY_TIMELAPSE_PHOTO = "timelapse_capture";
    public static final String KEY_TIMELAPSE_VIDEO = "timelapase_record";
    public static final String KEY_VIDEO_LOOP_BACK = "video_loop_back";
    public static final String KEY_VIDEO_QUALITY = "video_quality";
    public static final String KEY_VIDEO_RESOLUTION = "video_resolution";
    public static final String KEY_VIDEO_SRT = "video_srt";
    public static final String KEY_VIDEO_STAMP = "video_stamp";
    public static final String KEY_VIDEO_STANDARD = "video_standard";
    public static final String KEY_VIDEO_TIMELAPSE = "video_timelapse";
    public static final String SD_STATUS = "sd_status";
    public static final String VALUE_IMAGE_JPG = "JPG";
    public static final String VALUE_IMAGE_RAW = "JPG+DNG";
    public static final String VALUE_NTSC = "NTSC";
    public static final String VALUE_ON = "on";
    public static final String VALUE_PAL = "PAL";
    public static final String VALUE_PHOTO_QUALITY_16V9_12M = "12M (4608x2592 16:9)";
    public static final String VALUE_PHOTO_QUALITY_16V9_8M_4K = "8M (4056x3040 4:3)";
    public static final String VALUE_PHOTO_QUALITY_4V3_12M_4K = "12M (4056x3040 4:3)";
    public static final String VALUE_PHOTO_QUALITY_4V3_16M = "16M (4608x3456 4:3)";
    public static final String VALUE_PHOTO_QUALITY_4V3_8M = "8M (3264x2448 4:3)";
    public static final String VALUE_VIDEO_RESOLUTION_1080P_100F = "1920x1080 100P 16:9";
    public static final String VALUE_VIDEO_RESOLUTION_1080P_25F = "1920x1080 25P 16:9";
    public static final String VALUE_VIDEO_RESOLUTION_1080P_30F = "1920x1080 30P 16:9";
    public static final String VALUE_VIDEO_RESOLUTION_1080P_50F = "1920x1080 50P 16:9";
    public static final String VALUE_VIDEO_RESOLUTION_1080P_60F = "1920x1080 60P 16:9";
    public static final String VALUE_VIDEO_RESOLUTION_1440P_60F_2K = "2560x1440 30P 16:9";
    public static final String VALUE_VIDEO_RESOLUTION_2160P_24F_4K = "3840x2160 24P 16:9";
    public static final String VALUE_VIDEO_RESOLUTION_2160P_30F_4K = "3840x2160 30P 16:9";
    public static final String VALUE_VIDEO_RESOLUTION_720P = "1280x720 120P 16:9";
    public static final String VALUE_YES = "yes";
    public static boolean isClearData = false;
    public static final String[] rulerValues = new String[]{"-3.0", "-2.7", "-2.3", "-2.0", "-1.7", "-1.3", "-1.0", "-0.7", "-0.3", "0.0", "+0.3", "+0.7", "+1.0", "+1.3", "+1.7", "+2.0", "+2.3", "+2.7", "+3.0"};
    private final String KEY_CAMERA_ROI = "roi";

    /* Access modifiers changed, original: protected */
    public CameraJsonData createBaseCmd(int msgID, String param, String type) {
        CameraJsonData cmd = new CameraJsonData();
        cmd.setMsg_id(msgID);
        cmd.setParam(param);
        cmd.setType(type);
        cmd.setToken(StateManager.getInstance().getCamera().getToken());
        return cmd;
    }

    public X8BaseCamJsonData startSession() {
        String sendString = JSON.toJSONString(createBaseCmd(257, null, null));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(257);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setCamera(String param) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, "system_type"));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setCamKey("system_type");
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setVideoResolution(String param) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, "video_resolution"));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey("video_resolution");
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setPhotoSize(String param) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, "photo_size"));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey("photo_size");
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setPhotoFormat(String param) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, "photo_format"));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey("photo_format");
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData formatTFCard(JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(4, "C", null));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(4);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData defaultSystem(JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(2, "on", "default_setting"));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey("default_setting");
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraEV() {
        String sendString = JSON.toJSONString(createBaseCmd(1, null, "ae_bias"));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(1);
        baseCamJsonData.setCamKey("ae_bias");
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData deleteFile(String path, JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(1281, path, null));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.setMsgId(1281);
        baseCamJsonData.setCamKey("1281");
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraISO() {
        String sendString = JSON.toJSONString(createBaseCmd(1, null, KEY_AE_ISO));
        HostLogBack.getInstance().writeLog("Alanqiu  =================getCameraShutter:" + sendString);
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(1);
        baseCamJsonData.setCamKey(KEY_AE_ISO);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraIsoOptions() {
        String sendString = JSON.toJSONString(createBaseCmd(9, KEY_AE_ISO, null));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(9);
        baseCamJsonData.setCamKey(null);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraShutterOptions() {
        String sendString = JSON.toJSONString(createBaseCmd(9, KEY_SHUTTER_TIME, null));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(9);
        baseCamJsonData.setCamKey(null);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraAWB() {
        String sendString = JSON.toJSONString(createBaseCmd(1, null, "awb"));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(1);
        baseCamJsonData.setCamKey("awb");
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraShutter() {
        String sendString = JSON.toJSONString(createBaseCmd(1, null, KEY_SHUTTER_TIME));
        HostLogBack.getInstance().writeLog("Alanqiu  =================getCameraShutter:" + sendString);
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(1);
        baseCamJsonData.setCamKey(KEY_SHUTTER_TIME);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setCameraIso(String param) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, KEY_AE_ISO));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey(KEY_AE_ISO);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setCameraDeControl(String param, JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, KEY_DE_CONTROL_TYPE));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey(KEY_DE_CONTROL_TYPE);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setCameraShutterTime(String param) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, KEY_SHUTTER_TIME));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey(KEY_SHUTTER_TIME);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraCurParams(JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(3, null, null));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(3);
        baseCamJsonData.setCamKey(null);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setCameraEV(String param) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param + " EV", "ae_bias"));
        HostLogBack.getInstance().writeLog("Alanqiu  ============setCameraEV:" + sendString);
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey("ae_bias");
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraKeyOptions(String paramKey) {
        String sendString = JSON.toJSONString(createBaseCmd(9, paramKey, null));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(9);
        baseCamJsonData.setCamKey(null);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraKeyOptions(String paramKey, JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(9, paramKey, null));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(9);
        baseCamJsonData.setCamKey(null);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setCameraKeyParam(String param, String key, JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, key));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey(key);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCurCameraParams(String paramKey, JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(1, null, paramKey));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(1);
        baseCamJsonData.setCamKey(paramKey);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setCameraFocuse(String param, JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, KEY_DROOM));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey(KEY_DROOM);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraFocuse(JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(1, null, KEY_DROOM));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(1);
        baseCamJsonData.setCamKey(KEY_DROOM);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData getCameraFocuseValues(JsonUiCallBackListener callBackListener) {
        String sendString = JSON.toJSONString(createBaseCmd(9, KEY_DROOM, null));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(9);
        baseCamJsonData.setCamKey(null);
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.setJsonUiCallBackListener(callBackListener);
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }

    public X8BaseCamJsonData setInterestMetering(String param) {
        String sendString = JSON.toJSONString(createBaseCmd(2, param, "roi"));
        X8BaseCamJsonData baseCamJsonData = new X8BaseCamJsonData();
        baseCamJsonData.setAddToSendQueue(false);
        baseCamJsonData.setOutTime(1000);
        baseCamJsonData.setMsgId(2);
        baseCamJsonData.setCamKey("roi");
        baseCamJsonData.setPayLoad(sendString.getBytes());
        baseCamJsonData.packCmd();
        return baseCamJsonData;
    }
}
