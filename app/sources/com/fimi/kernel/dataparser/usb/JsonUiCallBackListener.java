package com.fimi.kernel.dataparser.usb;

import com.alibaba.fastjson.JSONObject;

public interface JsonUiCallBackListener<T> {
    void onComplete(JSONObject jSONObject, T t);
}
