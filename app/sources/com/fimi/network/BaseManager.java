package com.fimi.network;

import android.text.TextUtils;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.MD5Util;
import com.fimi.kernel.utils.SystemParamUtil;
import com.fimi.network.oauth2.OauthConstant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class BaseManager {
    private String getSignMsg(RequestParams params) {
        StringBuffer buffer = new StringBuffer();
        Map<String, String> map = new TreeMap();
        if (params != null) {
            for (Entry<String, String> entry : params.urlParams.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        for (Entry entry2 : map.entrySet()) {
            if (!(entry2.getValue() == null || entry2.getKey().equals("signMsg"))) {
                buffer.append(entry2.getKey() + "=" + entry2.getValue() + "&");
            }
        }
        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        String signMsg = MD5Util.MD5(buffer.toString());
        System.out.println(buffer.toString());
        System.out.println("signMsg:" + signMsg);
        return signMsg;
    }

    public RequestParams getRequestParams(RequestParams params) {
        return getCommonParams(params);
    }

    public RequestParams getRequestParams() {
        return getCommonParams(null);
    }

    public RequestParams getCommonParams(RequestParams params) {
        int userType;
        if (params == null) {
            params = new RequestParams();
        }
        params.put("fimiId", HostConstants.getUserDetail().getFimiId());
        params.put("clientType", "0");
        params.put("clientVersion", SystemParamUtil.getVersionName());
        params.put("access_token", SPStoreManager.getInstance().getString(OauthConstant.ACCESS_TOKEN_SP));
        if (TextUtils.isEmpty(HostConstants.getUserDetail().getFimiId())) {
            userType = 1;
        } else {
            userType = 2;
        }
        params.put("user_type", userType + "");
        params.put("signMsg", getSignMsg(params));
        return params;
    }
}
