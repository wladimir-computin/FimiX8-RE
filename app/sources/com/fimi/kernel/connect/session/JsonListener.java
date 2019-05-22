package com.fimi.kernel.connect.session;

import com.alibaba.fastjson.JSONObject;

public interface JsonListener {
    void onProcess(int i, JSONObject jSONObject);
}
