package com.fimi.x8sdk.modulestate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;
import java.util.ArrayList;
import java.util.List;

public class X8CameraSettings {
    private static String focusSetting = "2.0";
    private static List<String> focusSettingList = new ArrayList();
    public static boolean hasGetFocusSetting;
    public static boolean hasGetFoucusSettingValues;

    public static void setFocusSetting(String s) {
        focusSetting = s;
    }

    public static void getSettings(CameraManager cameraManager) {
        if (!hasGetFocusSetting) {
            cameraManager.getCameraFocuse(new JsonUiCallBackListener() {
                public void onComplete(JSONObject rt, Object o) {
                    if (rt != null) {
                        CameraParamsJson paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class);
                        if (paramsJson != null) {
                            String param = paramsJson.getParam();
                            if (param != null && !param.equals("")) {
                                X8CameraSettings.focusSetting = paramsJson.getParam();
                                X8CameraSettings.hasGetFocusSetting = true;
                            }
                        }
                    }
                }
            });
        }
        if (!hasGetFoucusSettingValues) {
            cameraManager.getCameraFocuseValues(new JsonUiCallBackListener() {
                public void onComplete(JSONObject rt, Object o) {
                    if (rt != null) {
                        CameraParamsJson paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class);
                        if (paramsJson != null) {
                            List<String> param = paramsJson.getOptions();
                            if (param != null && param.size() > 0) {
                                X8CameraSettings.focusSettingList = paramsJson.getOptions();
                                X8CameraSettings.hasGetFoucusSettingValues = true;
                            }
                        }
                    }
                }
            });
        }
    }

    public static List<String> getFocusSettingList() {
        if (focusSettingList.size() <= 0) {
            for (int i = 10; i <= 30; i++) {
                focusSettingList.add("" + (((float) i) / 10.0f));
            }
        }
        return focusSettingList;
    }

    public static void reset() {
        hasGetFocusSetting = false;
        hasGetFoucusSettingValues = false;
        focusSettingList.clear();
    }

    public static int getFocuse() {
        return (int) (Float.parseFloat(focusSetting) * 10.0f);
    }

    public static int[] getMinMaxFocuse() {
        getFocusSettingList();
        if (focusSettingList.size() <= 2) {
            return new int[]{10, 30};
        }
        int min = (int) (Float.parseFloat((String) focusSettingList.get(0)) * 10.0f);
        int max = (int) (Float.parseFloat((String) focusSettingList.get(focusSettingList.size() - 1)) * 10.0f);
        return new int[]{min, max};
    }
}
