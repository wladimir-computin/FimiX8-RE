package com.fimi.x8sdk.appsetting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fimi.host.HostConstants;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.entity.X8AppSettingLog;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.modulestate.VersionState;
import com.github.moduth.blockcanary.internal.BlockInfo;
import org.json.JSONException;

public class DeviceVersionFactory {
    public static Describe getFc() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleFcAckVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe getCamera() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleCameraVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe getGimbal() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleGimbalVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe getRc() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleRcVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe getBattery() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleBatteryVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe etRcRelay() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleRepeaterRcVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe getComputerVision() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleCvVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe getFcRelay() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleRepeaterVehicleVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe getEsc() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleEscVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe getNfz() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleNfzVersion();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static Describe getUltrasonic() {
        VersionState v = StateManager.getInstance().getVersionState();
        AckVersion version = v.getModuleUltrasonic();
        Describe describe = new Describe();
        if (!(v == null || version == null)) {
            try {
                IdInfoJson info = new IdInfoJson();
                info.setA(version.getIdA());
                info.setB(version.getIdB());
                info.setC(version.getIdC());
                info.setD(version.getIdD());
                describe.setDescribe(version.getShowDetails());
                describe.setIdInfo(info);
                describe.setVersion(version.getSoftVersion());
            } catch (Exception e) {
            }
        }
        return describe;
    }

    public static void main(String[] a) {
        try {
            System.out.println(getAllDataJsonString());
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }

    public static String getAllDataJsonString() throws JSONException {
        Object allObjcet = new JSONObject();
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
        dataObject.put("startupTime", new ValueFloat((float) X8AppSettingLog.STARTUPTIME));
        dataObject.put(BlockInfo.KEY_TIME_COST, DateUtil.getStringByFormat(System.currentTimeMillis(), HostConstants.FORMATDATE));
        return JSON.toJSONString(dataObject, SerializerFeature.PrettyFormat);
    }
}
