package com.fimi.x8sdk.listener;

import com.alibaba.fastjson.JSONObject;
import com.fimi.x8sdk.dataparser.AckCamJsonInfo;

public interface JsonCallBackListener<T extends AckCamJsonInfo> {
    void onFail(int i, int i2, String str);

    void onJSONSuccess(JSONObject jSONObject);

    void onSuccess(T t);

    void outTime();
}
