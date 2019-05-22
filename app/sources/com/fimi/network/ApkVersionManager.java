package com.fimi.network;

import ch.qos.logback.core.joran.action.Action;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.host.HostConstants;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.network.entity.NetModel;
import java.util.HashMap;
import java.util.Iterator;

public class ApkVersionManager extends BaseManager {
    public final String OPEN_FLY_LOG = "open_fly_log";
    public final String OPEN_SIXPOINT_CLIBRATE = "open_sixpoint_calibrate";
    public final String OPEN_STATE = "on";
    public HashMap<String, String> appSettingHashMap = new HashMap();

    public interface AppSettingListener {
        void onAppSettingListener();
    }

    public void getOnlineNewApkFileInfo(String packageName, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = getRequestParams();
        requestParams.put("pkgName", packageName);
        CommonOkHttpClient.get(CommonRequest.createGetRequest(HostConstants.NEW_APK_URL + "getApkDetail2", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void getAppSetting(final AppSettingListener appSettingListener) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(HostConstants.GET_APP_SETTING + "getAppSetting", getRequestParams()), new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                if (netModel.getData() != null) {
                    ApkVersionManager.this.appSettingHashMap.clear();
                    try {
                        Iterator<Object> iterator = JSON.parseArray(netModel.getData().toString()).iterator();
                        while (iterator.hasNext()) {
                            JSONObject jsonObjectData = (JSONObject) iterator.next();
                            ApkVersionManager.this.appSettingHashMap.put(jsonObjectData.getString(Action.KEY_ATTRIBUTE), jsonObjectData.getString("value"));
                        }
                        appSettingListener.onAppSettingListener();
                    } catch (Exception e) {
                        HostLogBack.getInstance().writeLog("Alanqiu  ==============getAppSetting:" + e.getMessage());
                    }
                }
            }

            public void onFailure(Object reasonObj) {
            }
        }));
    }

    public boolean isOpen(String key) {
        if (this.appSettingHashMap == null) {
            return false;
        }
        String value = (String) this.appSettingHashMap.get(key);
        if (value == null || !value.equalsIgnoreCase("on")) {
            return false;
        }
        return true;
    }
}
