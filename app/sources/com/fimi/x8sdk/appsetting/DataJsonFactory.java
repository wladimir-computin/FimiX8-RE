package com.fimi.x8sdk.appsetting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fimi.host.HostConstants;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.x8sdk.entity.X8AppSettingLog;
import com.github.moduth.blockcanary.internal.BlockInfo;
import com.umeng.commonsdk.proguard.g;

public class DataJsonFactory {
    public static String getAllDataJsonString() {
        Object allObjcet = new JSONObject();
        allObjcet.put("flightController", DeviceVersionFactory.getFc());
        allObjcet.put("camera", DeviceVersionFactory.getCamera());
        allObjcet.put("gimbal", DeviceVersionFactory.getGimbal());
        allObjcet.put("remoteController", DeviceVersionFactory.getRc());
        allObjcet.put(g.W, DeviceVersionFactory.getBattery());
        allObjcet.put("rcRelay", DeviceVersionFactory.etRcRelay());
        allObjcet.put("computerVision", DeviceVersionFactory.getComputerVision());
        allObjcet.put("fcRelay", DeviceVersionFactory.getFcRelay());
        allObjcet.put("esc", DeviceVersionFactory.getEsc());
        allObjcet.put("nfz", DeviceVersionFactory.getNfz());
        allObjcet.put("Ultrasonic", DeviceVersionFactory.getUltrasonic());
        allObjcet.put("speedLimit", new ValueFloat(X8AppSettingLog.speedLimit));
        allObjcet.put("distanceLimit", new ValueFloat(X8AppSettingLog.distanceLimit));
        allObjcet.put("heightLimit", new ValueFloat(X8AppSettingLog.heightLimit));
        allObjcet.put("returnHeight", new ValueFloat(X8AppSettingLog.returnHeight));
        allObjcet.put("pilotMode", new ValueBoolean(X8AppSettingLog.pilotMode));
        allObjcet.put("sportMode", new ValueBoolean(X8AppSettingLog.sportMode));
        allObjcet.put("lostAction", new ValueFloat((float) X8AppSettingLog.lostAction));
        allObjcet.put("accLand", new ValueBoolean(X8AppSettingLog.accurateLanding));
        allObjcet.put("followRP", new ValueBoolean(X8AppSettingLog.followRP));
        allObjcet.put("followAB", new ValueBoolean(X8AppSettingLog.followAB));
        allObjcet.put("lowPower", new ValueFloat((float) X8AppSettingLog.lowPower));
        allObjcet.put("feelSensitivity", new SensityJson(new ValueSensity((float) X8AppSettingLog.FS_pitch, (float) X8AppSettingLog.FS_roll, (float) X8AppSettingLog.FS_thro, (float) X8AppSettingLog.FS_yaw)));
        allObjcet.put("feelBrake", new SensityJson(new ValueSensity((float) X8AppSettingLog.FB_pitch, (float) X8AppSettingLog.FB_roll, (float) X8AppSettingLog.FB_thro, (float) X8AppSettingLog.FB_yaw)));
        allObjcet.put("feelYawTrip", new SensityJson(new ValueSensity((float) X8AppSettingLog.FY_pitch, (float) X8AppSettingLog.FY_roll, (float) X8AppSettingLog.FY_thro, (float) X8AppSettingLog.FY_yaw)));
        allObjcet.put("feelExp", new SensityJson(new ValueSensity((float) X8AppSettingLog.FE_pitch, (float) X8AppSettingLog.FE_roll, (float) X8AppSettingLog.FE_thro, (float) X8AppSettingLog.FE_yaw)));
        allObjcet.put("cc", new ValueFloat((float) X8AppSettingLog.CC));
        allObjcet.put("uvc", new ValueFloat((float) X8AppSettingLog.UVC));
        allObjcet.put("totalCapacity", new ValueFloat((float) X8AppSettingLog.TOTALCAPACITY));
        allObjcet.put("rcNotUpdateCnt", new ValueFloat((float) X8AppSettingLog.RCNOTUPDATECNT));
        allObjcet.put("sysErrorCode", new ValueFloat((float) X8AppSettingLog.SYSERRORCODE));
        allObjcet.put("sysState", new ValueFloat((float) X8AppSettingLog.SYSSTATE));
        Object dataObject = new JSONObject();
        dataObject.put("data", allObjcet);
        dataObject.put("startupTime", "" + X8AppSettingLog.STARTUPTIME);
        dataObject.put(BlockInfo.KEY_TIME_COST, DateUtil.getStringByFormat(System.currentTimeMillis(), HostConstants.FORMATDATE));
        return JSON.toJSONString(dataObject, SerializerFeature.PrettyFormat);
    }

    public static String onValueChange(String tag, float oldV, float newV) {
        Object dataObject = new JSONObject();
        String json = "";
        Object object = new JSONObject();
        object.put(tag, new ValueFloatChange(oldV, newV));
        dataObject.put("data", object);
        dataObject.put("startupTime", "" + X8AppSettingLog.STARTUPTIME);
        dataObject.put(BlockInfo.KEY_TIME_COST, DateUtil.getStringByFormat(System.currentTimeMillis(), HostConstants.FORMATDATE));
        return JSON.toJSONString(dataObject, SerializerFeature.PrettyFormat);
    }

    public static String onValueBooleanChange(String tag, boolean oldV, boolean newV) {
        Object dataObject = new JSONObject();
        String json = "";
        Object object = new JSONObject();
        object.put(tag, new ValueBooleanChange(oldV, newV));
        dataObject.put("data", object);
        dataObject.put("startupTime", "" + X8AppSettingLog.STARTUPTIME);
        dataObject.put(BlockInfo.KEY_TIME_COST, DateUtil.getStringByFormat(System.currentTimeMillis(), HostConstants.FORMATDATE));
        return JSON.toJSONString(dataObject, SerializerFeature.PrettyFormat);
    }

    public static String appValueSensityChange(String tag, ValueSensity oldV, ValueSensity newV) {
        Object dataObject = new JSONObject();
        String json = "";
        Object object = new JSONObject();
        object.put(tag, new SensityJsonChange(oldV, newV));
        dataObject.put("data", object);
        dataObject.put("startupTime", "" + X8AppSettingLog.STARTUPTIME);
        dataObject.put(BlockInfo.KEY_TIME_COST, DateUtil.getStringByFormat(System.currentTimeMillis(), HostConstants.FORMATDATE));
        return JSON.toJSONString(dataObject, SerializerFeature.PrettyFormat);
    }
}
